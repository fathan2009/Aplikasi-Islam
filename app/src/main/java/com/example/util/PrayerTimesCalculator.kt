package com.example.util

import java.util.Calendar
import kotlin.math.*

object PrayerTimesCalculator {
    // City definitions
    data class City(
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val timezone: Double
    )

    val INDONESIAN_CITIES = listOf(
        City("Jakarta", -6.2088, 106.8456, 7.0),
        City("Surabaya", -7.2575, 112.7521, 7.0),
        City("Bandung", -6.9175, 107.6191, 7.0),
        City("Medan", 3.5952, 98.6722, 7.0),
        City("Makassar", -5.1477, 119.4327, 8.0),
        City("Yogyakarta", -7.7956, 110.3695, 7.0),
        City("Semarang", -6.9932, 110.4203, 7.0),
        City("Palembang", -2.9909, 104.7567, 7.0),
        City("Denpasar", -8.6705, 115.2126, 8.0),
        City("Aceh (Banda Aceh)", 5.5483, 95.3238, 7.0),
        City("Jayapura", -2.5489, 140.7178, 9.0),
        City("Pontianak", -0.0263, 109.3425, 7.0),
        City("Banjarmasin", -3.3186, 114.5944, 8.0),
        City("Samarinda", -0.5022, 117.1536, 8.0),
        City("Manado", 1.4748, 124.8421, 8.0),
        City("Ambon", -3.6954, 128.1814, 9.0)
    )

    data class PrayerTimes(
        val imsak: String,
        val fajr: String,
        val sunrise: String,
        val dhuhr: String,
        val asr: String,
        val maghrib: String,
        val isha: String
    )

    // Calculate prayer times
    fun calculate(
        lat: Double,
        lng: Double,
        timezone: Double,
        calendar: Calendar
    ): PrayerTimes {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Julian Date
        val m = if (month <= 2) month + 12 else month
        val y = if (month <= 2) year - 1 else year
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        val jd = floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5

        val d = jd - 2451545.0
        val g = d2r(280.460 + 0.9856474 * d)
        val q = d2r(357.528 + 0.9856003 * d)
        
        // Solar longitude
        val lambda = g + d2r(1.915 * sin(q) + 0.020 * sin(2 * q))
        
        // Obliquity of ecliptic
        val epsilon = d2r(23.439 - 0.0000004 * d)
        
        // Declination
        val declination = asin(sin(epsilon) * sin(lambda))
        
        // Right Ascension
        var ra = atan2(cos(epsilon) * sin(lambda), cos(lambda))
        if (ra < 0) ra += 2 * PI
        ra /= (2 * PI) / 24.0 // RA in hours

        // Equation of Time (in hours)
        val eot = (g * 24.0 / (2 * PI)) - ra
        
        // Mid day (Dhuhr)
        var midDay = 12.0 + timezone - (lng / 15.0) - eot
        if (midDay < 0) midDay += 24.0
        if (midDay >= 24) midDay -= 24.0

        // Fajr Hour Angle (Indonesian Ministry uses 20.0 degrees)
        val fajrAngle = d2r(20.0)
        val fajrHA = calculateHourAngle(-fajrAngle, lat, declination) ?: d2r(15.0)

        // Sunrise Hour Angle
        val sunriseAngle = d2r(0.833)
        val sunriseHA = calculateHourAngle(-sunriseAngle, lat, declination) ?: d2r(15.0)

        // Asr Shadow angle (Shafi'i: shadow length factor = 1)
        val asrShadowFactor = 1.0
        val gAsr = acos((sin(atan(1.0 / (asrShadowFactor + tan(abs(d2r(lat) - declination))))) - sin(d2r(lat)) * sin(declination)) / (cos(d2r(lat)) * cos(declination)))
        val asrHA = if (gAsr.isNaN()) d2r(15.0) else gAsr

        // Maghrib Hour Angle
        val maghribAngle = d2r(0.833)
        val maghribHA = calculateHourAngle(-maghribAngle, lat, declination) ?: d2r(15.0)

        // Isha Hour Angle (Indonesian Ministry uses 18.0 degrees)
        val ishaAngle = d2r(18.0)
        val ishaHA = calculateHourAngle(-ishaAngle, lat, declination) ?: d2r(15.0)

        // Convert angles to hours
        val fajrTime = midDay - r2d(fajrHA) / 15.0
        val sunriseTime = midDay - r2d(sunriseHA) / 15.0
        val dhuhrTime = midDay + (3.0 / 60.0) // 3-minute buffer
        val asrTime = midDay + r2d(asrHA) / 15.0
        val maghribTime = midDay + r2d(maghribHA) / 15.0
        val ishaTime = midDay + r2d(ishaHA) / 15.0
        val imsakTime = fajrTime - (10.0 / 60.0) // 10 mins before Fajr

        return PrayerTimes(
            imsak = formatTime(imsakTime),
            fajr = formatTime(fajrTime),
            sunrise = formatTime(sunriseTime),
            dhuhr = formatTime(dhuhrTime),
            asr = formatTime(asrTime),
            maghrib = formatTime(maghribTime),
            isha = formatTime(ishaTime)
        )
    }

    private fun d2r(d: Double): Double = d * PI / 180.0
    private fun r2d(r: Double): Double = r * 180.0 / PI

    private fun calculateHourAngle(angle: Double, lat: Double, declination: Double): Double? {
        val latRad = d2r(lat)
        val num = sin(angle) - sin(latRad) * sin(declination)
        val den = cos(latRad) * cos(declination)
        val cosHA = num / den
        return if (cosHA in -1.0..1.0) acos(cosHA) else null
    }

    private fun formatTime(hours: Double): String {
        var h = hours
        if (h.isNaN()) return "--:--"
        while (h < 0) h += 24.0
        while (h >= 24) h -= 24.0
        val totalMinutes = floor(h * 60.0 + 0.5).toInt()
        val m = totalMinutes % 60
        val hr = (totalMinutes / 60) % 24
        return String.format("%02d:%02d", hr, m)
    }
}
