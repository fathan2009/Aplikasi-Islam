package com.example.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.UserProfile
import com.example.model.Article
import com.example.model.DummyArticles
import com.example.model.DummyHadiths
import com.example.model.Hadith
import com.example.repository.UserProfileRepository
import com.example.util.HijriDateConverter
import com.example.util.PrayerTimesCalculator
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = UserProfileRepository(database.userProfileDao())

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    // Reactive Profile state
    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .map { it ?: UserProfile() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    // Current location coordinates (if using GPS)
    private val _gpsLocation = MutableStateFlow<Location?>(null)
    val gpsLocation = _gpsLocation.asStateFlow()

    // Real-time tick for countdown
    private val _currentTime = MutableStateFlow(Calendar.getInstance())
    val currentTime = _currentTime.asStateFlow()

    // Prayer times state
    val prayerTimes: StateFlow<PrayerTimesCalculator.PrayerTimes> = combine(
        userProfile,
        _gpsLocation,
        _currentTime
    ) { profile, gpsLoc, time ->
        val city = PrayerTimesCalculator.INDONESIAN_CITIES.find { it.name.equals(profile.selectedCity, ignoreCase = true) }
            ?: PrayerTimesCalculator.INDONESIAN_CITIES[0]

        val lat = if (profile.isGpsEnabled && gpsLoc != null) gpsLoc.latitude else city.latitude
        val lng = if (profile.isGpsEnabled && gpsLoc != null) gpsLoc.longitude else city.longitude
        val tz = if (profile.isGpsEnabled && gpsLoc != null) {
            if (lng >= 135.0) 9.0 else if (lng >= 120.0) 8.0 else 7.0
        } else {
            city.timezone
        }

        PrayerTimesCalculator.calculate(lat, lng, tz, time)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PrayerTimesCalculator.calculate(-6.2088, 106.8456, 7.0, Calendar.getInstance())
    )

    // Current city label to show in UI
    val currentCityLabel: StateFlow<String> = combine(userProfile, _gpsLocation) { profile, gpsLoc ->
        if (profile.isGpsEnabled) {
            if (gpsLoc != null) {
                String.format(Locale.getDefault(), "GPS: %.4f, %.4f", gpsLoc.latitude, gpsLoc.longitude)
            } else {
                "GPS: Mencari Lokasi..."
            }
        } else {
            profile.selectedCity
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "Jakarta"
    )

    // Hijri date
    val currentHijriDate: StateFlow<String> = _currentTime.map {
        HijriDateConverter.getHijriDate(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HijriDateConverter.getHijriDate(Calendar.getInstance())
    )

    // Filter category for articles
    private val _selectedArticleCategory = MutableStateFlow("Semua")
    val selectedArticleCategory = _selectedArticleCategory.asStateFlow()

    // Filtered articles
    val articles: StateFlow<List<Article>> = _selectedArticleCategory.map { category ->
        if (category == "Semua") {
            DummyArticles.list
        } else {
            DummyArticles.list.filter { it.category.equals(category, ignoreCase = true) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DummyArticles.list
    )

    // Hadith Search Query
    private val _hadithQuery = MutableStateFlow("")
    val hadithQuery = _hadithQuery.asStateFlow()

    // Filtered Hadiths
    val hadiths: StateFlow<List<Hadith>> = _hadithQuery.map { query ->
        if (query.isBlank()) {
            DummyHadiths.list
        } else {
            DummyHadiths.list.filter {
                it.source.contains(query, ignoreCase = true) ||
                it.number.contains(query, ignoreCase = true) ||
                it.indonesianTranslation.contains(query, ignoreCase = true) ||
                it.arabicText.contains(query) ||
                it.explanation.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DummyHadiths.list
    )

    // Next Prayer info
    data class NextPrayerInfo(
        val name: String,
        val time: String,
        val countdown: String
    )

    val nextPrayerInfo: StateFlow<NextPrayerInfo> = combine(prayerTimes, _currentTime) { times, now ->
        calculateNextPrayer(times, now)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NextPrayerInfo("-", "--:--", "--j --m")
    )

    private var tickJob: Job? = null

    init {
        viewModelScope.launch {
            val existing = repository.getUserProfileOneShot()
            if (existing == null) {
                repository.saveUserProfile(UserProfile())
            } else if (existing.isGpsEnabled) {
                requestGpsLocation()
            }
        }
        startClockTicks()
    }

    private fun startClockTicks() {
        tickJob?.cancel()
        tickJob = viewModelScope.launch {
            while (true) {
                _currentTime.value = Calendar.getInstance()
                delay(60000) // update every minute
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun requestGpsLocation() {
        viewModelScope.launch {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    if (loc != null) {
                        _gpsLocation.value = loc
                    } else {
                        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                            .addOnSuccessListener { currentLoc ->
                                if (currentLoc != null) {
                                    _gpsLocation.value = currentLoc
                                }
                            }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
            if (profile.isGpsEnabled) {
                requestGpsLocation()
            } else {
                _gpsLocation.value = null
            }
        }
    }

    fun setArticleCategory(category: String) {
        _selectedArticleCategory.value = category
    }

    fun searchHadith(query: String) {
        _hadithQuery.value = query
    }

    private fun calculateNextPrayer(times: PrayerTimesCalculator.PrayerTimes, now: Calendar): NextPrayerInfo {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(now.time)
        
        val prayerList = listOf(
            Pair("Subuh", times.fajr),
            Pair("Dzuhur", times.dhuhr),
            Pair("Ashar", times.asr),
            Pair("Maghrib", times.maghrib),
            Pair("Isya", times.isha)
        )

        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val nowMs = now.timeInMillis

        for (prayer in prayerList) {
            try {
                val prayerDate = parser.parse("$todayStr ${prayer.second}")
                if (prayerDate != null && prayerDate.time > nowMs) {
                    val diffMs = prayerDate.time - nowMs
                    val diffHrs = diffMs / (1000 * 60 * 60)
                    val diffMins = (diffMs / (1000 * 60)) % 60
                    return NextPrayerInfo(
                        name = prayer.first,
                        time = prayer.second,
                        countdown = "${diffHrs}j ${diffMins}m"
                    )
                }
            } catch (e: Exception) {
                // Ignore parsing errors
            }
        }

        // If none is greater, next prayer is Fajr tomorrow
        try {
            val tomorrow = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
            }
            val tomorrowStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tomorrow.time)
            val fajrTomorrowDate = parser.parse("$tomorrowStr ${times.fajr}")
            if (fajrTomorrowDate != null) {
                val diffMs = fajrTomorrowDate.time - nowMs
                val diffHrs = diffMs / (1000 * 60 * 60)
                val diffMins = (diffMs / (1000 * 60)) % 60
                return NextPrayerInfo(
                    name = "Subuh",
                    time = times.fajr,
                    countdown = "${diffHrs}j ${diffMins}m (Esok)"
                )
            }
        } catch (e: Exception) {
            // Ignore
        }

        return NextPrayerInfo("Subuh", times.fajr, "--j --m")
    }
}
