package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserProfile
import com.example.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    userProfile: UserProfile,
    onNavigateToPrayerTimes: () -> Unit
) {
    val context = LocalContext.current
    val nextPrayer by viewModel.nextPrayerInfo.collectAsState()
    val hijriDate by viewModel.currentHijriDate.collectAsState()
    val cityLabel by viewModel.currentCityLabel.collectAsState()
    val scrollState = rememberScrollState()

    var showUpcomingFeatureDialog by remember { mutableStateOf<String?>(null) }

    val todayDateStr = remember {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
        sdf.format(Date())
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
        ) {
            // Header Section: Hero Banner and Greeting Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                // Generated Hero Image
                Image(
                    painter = painterResource(id = R.drawable.img_islam_hero),
                    contentDescription = "Islam Modern Hero",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark/Green Gradient Overlay for Text Readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                                )
                            )
                        )
                )

                // Greeting Content Overlay
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "Assalamualaikum,",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = userProfile.username,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.testTag("home_user_greeting")
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = cityLabel,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // Dates Panel
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-10).dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = todayDateStr,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hijriDate,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Date Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Next Prayer Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToPrayerTimes() }
                        .testTag("next_prayer_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Salat Berikutnya",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = nextPrayer.name,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Pukul ${nextPrayer.time}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Waktu Tersisa",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                            ) {
                                Text(
                                    text = nextPrayer.countdown,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Quick Action Grid (Future Features Placement)
                Text(
                    text = "Layanan Islami",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ServiceItem(
                        icon = Icons.Default.Book,
                        title = "Al-Qur'an",
                        onClick = { showUpcomingFeatureDialog = "Al-Qur'an" }
                    )
                    ServiceItem(
                        icon = Icons.Default.Explore,
                        title = "Kiblat",
                        onClick = { showUpcomingFeatureDialog = "Arah Kiblat" }
                    )
                    ServiceItem(
                        icon = Icons.Default.Favorite,
                        title = "Doa Harian",
                        onClick = { showUpcomingFeatureDialog = "Kumpulan Doa Harian" }
                    )
                    ServiceItem(
                        icon = Icons.Default.Event,
                        title = "Kalender",
                        onClick = { showUpcomingFeatureDialog = "Kalender Hijriah" }
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Daily Verse / Quote of the Day
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = "Quote Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "“Maka sesungguhnya beserta kesulitan ada kemudahan, sesungguhnya beserta kesulitan itu ada kemudahan.”",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "QS. Asy-Syarh: 5-6",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }
    }

    // Modal Dialog for Upcoming Features (Qur'an, Qibla, Daily Doa, Hijri Calendar)
    showUpcomingFeatureDialog?.let { featureName ->
        AlertDialog(
            onDismissRequest = { showUpcomingFeatureDialog = null },
            confirmButton = {
                TextButton(onClick = { showUpcomingFeatureDialog = null }) {
                    Text("Mengerti", fontWeight = FontWeight.Bold)
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Fitur Segera Hadir",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Fitur $featureName saat ini sedang dikembangkan dan akan segera tersedia lengkap di pembaruan versi mendatang. Terima kasih atas kesabaran Anda!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun RowScope.ServiceItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(16.dp),
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
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Akan Datang",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            ),
            textAlign = TextAlign.Center
        )
    }
}
