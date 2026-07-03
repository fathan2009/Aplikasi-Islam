package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.util.PrayerTimesCalculator
import com.example.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScheduleScreen(
    viewModel: AppViewModel,
    userProfile: UserProfile
) {
    val prayerTimes by viewModel.prayerTimes.collectAsState()
    val cityLabel by viewModel.currentCityLabel.collectAsState()
    val nextPrayer by viewModel.nextPrayerInfo.collectAsState()
    val scrollState = rememberScrollState()

    var showCityDropdown by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            // Screen Header Title
            Text(
                text = "Jadwal Salat",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = "Menampilkan waktu salat fardu dan sunah secara real-time berdasarkan titik koordinat presisi Anda.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Location Selector Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Lokasi Aktif",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = cityLabel,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.testTag("prayer_city_label")
                            )
                        }

                        // Dropdown trigger or refresh GPS
                        Row {
                            if (userProfile.isGpsEnabled) {
                                IconButton(
                                    onClick = { viewModel.requestGpsLocation() },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MyLocation,
                                        contentDescription = "Sync GPS",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            } else {
                                Box {
                                    Button(
                                        onClick = { showCityDropdown = true },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                            contentColor = MaterialTheme.colorScheme.primary
                                        ),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Text("Pilih Kota", fontWeight = FontWeight.Bold)
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Dropdown"
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showCityDropdown,
                                        onDismissRequest = { showCityDropdown = false },
                                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                                    ) {
                                        PrayerTimesCalculator.INDONESIAN_CITIES.forEach { city ->
                                            DropdownMenuItem(
                                                text = { Text(city.name, fontWeight = FontWeight.Medium) },
                                                onClick = {
                                                    viewModel.saveProfile(
                                                        userProfile.copy(
                                                            isGpsEnabled = false,
                                                            selectedCity = city.name
                                                        )
                                                    )
                                                    showCityDropdown = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (userProfile.isGpsEnabled) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "GPS info",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Lokasi menyesuaikan koordinat GPS secara real-time.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // List of Prayer Times fardhu & sunah
            Text(
                text = "Jadwal Hari Ini",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                PrayerRow(
                    name = "Imsak",
                    time = prayerTimes.imsak,
                    icon = Icons.Default.AccessTime,
                    isActive = nextPrayer.name == "Imsak"
                )
                PrayerRow(
                    name = "Subuh",
                    time = prayerTimes.fajr,
                    icon = Icons.Default.WbTwilight,
                    isActive = nextPrayer.name == "Subuh" || nextPrayer.name == "Subuh (Esok)"
                )
                PrayerRow(
                    name = "Terbit (Syuruq)",
                    time = prayerTimes.sunrise,
                    icon = Icons.Default.WbSunny,
                    isActive = nextPrayer.name == "Terbit"
                )
                PrayerRow(
                    name = "Dzuhur",
                    time = prayerTimes.dhuhr,
                    icon = Icons.Default.LightMode,
                    isActive = nextPrayer.name == "Dzuhur"
                )
                PrayerRow(
                    name = "Ashar",
                    time = prayerTimes.asr,
                    icon = Icons.Default.Timer,
                    isActive = nextPrayer.name == "Ashar"
                )
                PrayerRow(
                    name = "Maghrib",
                    time = prayerTimes.maghrib,
                    icon = Icons.Default.ModeNight,
                    isActive = nextPrayer.name == "Maghrib"
                )
                PrayerRow(
                    name = "Isya",
                    time = prayerTimes.isha,
                    icon = Icons.Default.NightsStay,
                    isActive = nextPrayer.name == "Isya"
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun PrayerRow(
    name: String,
    time: String,
    icon: ImageVector,
    isActive: Boolean
) {
    val containerColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    )
    val contentColor = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    val iconColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("prayer_row_$name")
            .border(
                width = if (isActive) 1.5.dp else 0.dp,
                color = if (isActive) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 3.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.background
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = name,
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.SemiBold,
                        color = contentColor
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) MaterialTheme.colorScheme.primary else contentColor
                    )
                )
                if (isActive) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            text = "SEKARANG",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 8.sp
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}
