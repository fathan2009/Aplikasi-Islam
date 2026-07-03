package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserProfile
import com.example.util.PrayerTimesCalculator
import com.example.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: AppViewModel,
    userProfile: UserProfile
) {
    val scrollState = rememberScrollState()

    var username by remember(userProfile.username) { mutableStateOf(userProfile.username) }
    var isDarkTheme by remember(userProfile.isDarkTheme) { mutableStateOf(userProfile.isDarkTheme) }
    var isGpsEnabled by remember(userProfile.isGpsEnabled) { mutableStateOf(userProfile.isGpsEnabled) }
    var selectedCity by remember(userProfile.selectedCity) { mutableStateOf(userProfile.selectedCity) }

    var notifyFajr by remember(userProfile.notifyFajr) { mutableStateOf(userProfile.notifyFajr) }
    var notifyDhuhr by remember(userProfile.notifyDhuhr) { mutableStateOf(userProfile.notifyDhuhr) }
    var notifyAsr by remember(userProfile.notifyAsr) { mutableStateOf(userProfile.notifyAsr) }
    var notifyMaghrib by remember(userProfile.notifyMaghrib) { mutableStateOf(userProfile.notifyMaghrib) }
    var notifyIsha by remember(userProfile.notifyIsha) { mutableStateOf(userProfile.notifyIsha) }

    var showCitySelectorDropdown by remember { mutableStateOf(false) }

    // Save changes helper
    val triggerSave = {
        viewModel.saveProfile(
            UserProfile(
                id = 1,
                username = username,
                profilePicturePath = userProfile.profilePicturePath,
                isDarkTheme = isDarkTheme,
                isGpsEnabled = isGpsEnabled,
                selectedCity = selectedCity,
                notifyFajr = notifyFajr,
                notifyDhuhr = notifyDhuhr,
                notifyAsr = notifyAsr,
                notifyMaghrib = notifyMaghrib,
                notifyIsha = notifyIsha
            )
        )
    }

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
            // Header Section
            Text(
                text = "Profil Muslim",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = "Kelola preferensi akun Anda, aktifkan notifikasi azan fardu, dan sesuaikan penentuan koordinat jadwal salat.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // User Profile Card (Picture & Username field)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile picture avatar (Generated picture)
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_profile_avatar),
                            contentDescription = "Foto Profil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Edit Username Field
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            triggerSave()
                        },
                        label = { Text("Nama Pengguna") },
                        placeholder = { Text("Masukkan nama Anda...") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "User icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("username_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            // General Settings Card (Theme, Location, GPS)
            Text(
                text = "Pengaturan Aplikasi",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Dark Mode Toggle
                    RowSettingItem(
                        icon = Icons.Default.DarkMode,
                        title = "Tema Gelap",
                        subtitle = "Aktifkan latar belakang gelap pelindung mata.",
                        action = {
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = {
                                    isDarkTheme = it
                                    triggerSave()
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                                    checkedTrackColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.testTag("dark_theme_switch")
                            )
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // GPS Automatic Toggle
                    RowSettingItem(
                        icon = Icons.Default.GpsFixed,
                        title = "Lokasi Otomatis (GPS)",
                        subtitle = "Mengambil koordinat jadwal salat secara otomatis.",
                        action = {
                            Switch(
                                checked = isGpsEnabled,
                                onCheckedChange = {
                                    isGpsEnabled = it
                                    triggerSave()
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                                    checkedTrackColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.testTag("gps_switch")
                            )
                        }
                    )

                    // If GPS is disabled, show City selection dropdown
                    if (!isGpsEnabled) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { showCitySelectorDropdown = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("select_city_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationCity,
                                            contentDescription = "City"
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Kota: $selectedCity", fontWeight = FontWeight.SemiBold)
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Dropdown"
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = showCitySelectorDropdown,
                                onDismissRequest = { showCitySelectorDropdown = false },
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                PrayerTimesCalculator.INDONESIAN_CITIES.forEach { city ->
                                    DropdownMenuItem(
                                        text = { Text(city.name, fontWeight = FontWeight.Medium) },
                                        onClick = {
                                            selectedCity = city.name
                                            triggerSave()
                                            showCitySelectorDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Notification/Azan Settings Card
            Text(
                text = "Pilihan Notifikasi Azan",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    NotificationSettingItem(
                        name = "Azan Subuh",
                        checked = notifyFajr,
                        onCheckedChange = {
                            notifyFajr = it
                            triggerSave()
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    NotificationSettingItem(
                        name = "Azan Dzuhur",
                        checked = notifyDhuhr,
                        onCheckedChange = {
                            notifyDhuhr = it
                            triggerSave()
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    NotificationSettingItem(
                        name = "Azan Ashar",
                        checked = notifyAsr,
                        onCheckedChange = {
                            notifyAsr = it
                            triggerSave()
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    NotificationSettingItem(
                        name = "Azan Maghrib",
                        checked = notifyMaghrib,
                        onCheckedChange = {
                            notifyMaghrib = it
                            triggerSave()
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    NotificationSettingItem(
                        name = "Azan Isya",
                        checked = notifyIsha,
                        onCheckedChange = {
                            notifyIsha = it
                            triggerSave()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RowSettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    action: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        action()
    }
}

@Composable
fun NotificationSettingItem(
    name: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = name,
                tint = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.surface,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.testTag("switch_${name.replace(" ", "_").lowercase()}")
        )
    }
}
