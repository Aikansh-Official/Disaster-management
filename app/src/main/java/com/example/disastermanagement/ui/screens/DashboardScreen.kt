package com.example.disastermanagement.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.Water
import androidx.compose.material3.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.disastermanagement.ui.components.Card
import com.example.disastermanagement.ui.theme.*

@Composable
fun DashboardScreen(
    readinessScore: Float,
    drillsCompleted: Int,
    tipsRead: Int,
    onOpenSafetyMap: () -> Unit,
    onOpenTrainingHub: () -> Unit,
    onReviewEarthquake: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAlert by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Alert Banner
        if (showAlert) {
            AlertBanner(
                modifier = Modifier.padding(horizontal = 16.dp),
                onDismiss = { showAlert = false }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Card - Readiness Score
            ReadinessCard(readinessScore = readinessScore, onImproveScore = onOpenTrainingHub)

            // Drills Completed Card
            StatCard(
                icon = Icons.Outlined.FitnessCenter,
                title = "Drills Completed",
                value = drillsCompleted.toString(),
                suffix = "/10",
                iconBgColor = SurfaceVariant
            )

            // Tips Read Card
            StatCard(
                icon = Icons.Outlined.MenuBook,
                title = "Tips Read",
                value = tipsRead.toString(),
                iconBgColor = SurfaceVariant
            )

            // Local Risk Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Local Risk",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Downtown District",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            // Flood Risk Card
            RiskCard(
                icon = Icons.Outlined.Water,
                title = "Flood Risk",
                description = "Moderate probability during heavy monsoon season.",
                buttonText = "View Evac Route",
                iconColor = Color(0xFF006474),
                iconBgColor = SurfaceVariant,
                onClick = onOpenSafetyMap
            )

            // Earthquake Card
            RiskCard(
                icon = Icons.Outlined.Water, // Using water as placeholder for earthquake icon in image
                title = "Earthquake",
                description = "Low probability, high impact. Review drop-cover-hold.",
                buttonText = "Review Protocol",
                iconColor = Color(0xFF9b4500),
                iconBgColor = Color(0xFFFFEBDD),
                onClick = onReviewEarthquake
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AlertBanner(modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFDAD6))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Warning,
            contentDescription = "Warning",
            tint = Color(0xFFBA1A1A),
            modifier = Modifier.size(24.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Heavy Rain Warning",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF410002)
            )
            Text(
                text = "Expected rainfall of 50mm over the next 3 hours. Avoid low-lying areas.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF410002)
            )
        }

        IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Dismiss",
                tint = Color(0xFF410002),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ReadinessCard(readinessScore: Float, onImproveScore: () -> Unit) {
    var animationTarget by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(readinessScore) {
        animationTarget = readinessScore.coerceIn(0f, 1f)
    }
    val animatedProgress by animateFloatAsState(targetValue = animationTarget, label = "dashboard_readiness")

    Card(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.padding(20.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Overall Readiness",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "You are well prepared!\nKeep learning to reach\n100%.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.width(180.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier.size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = SecondaryContainer,
                        trackColor = SurfaceVariant,
                        strokeWidth = 10.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }
            }

            // Person Image
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAuBggjOioal6m1kooeTyiPQaHrV2F2cN48T6hunbO829NIvyl-WaR9GIwo9ACaEPEbmnFygNynks_KtB8CTXmbR3yA9ddeW6eWdOnerv3aS40ev-i0m8poPANtbD_il4qT8f98S2Nra1DgTKql0FLt3KMvs9g3abxPMEijZ3rt8xqm375Mx5hS4kzd4knCjzdVdThLTBx4HyqrQ86vgABdEighvyioPY_83lq3XNcC9WvbEkEE4Ko1QnB4g0qeuEaehu5hT_Z67uRI",
                contentDescription = null,
                modifier = Modifier
                    .size(width = 150.dp, height = 200.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = 10.dp, y = (-10).dp)
            )

            // Improve Score Button
            Button(
                onClick = onImproveScore,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SecondaryContainer
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 0.dp)
                    .height(40.dp)
            ) {
                Text(
                    "Improve Score",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    title: String,
    value: String,
    suffix: String = "",
    iconBgColor: Color = SurfaceVariant
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )
            }

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                if (suffix.isNotEmpty()) {
                    Text(
                        text = suffix,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RiskCard(
    icon: ImageVector,
    title: String,
    description: String,
    buttonText: String,
    iconColor: Color = Primary,
    iconBgColor: Color = SurfaceVariant,
    onClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            OutlinedButton(
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(36.dp),
                shape = RoundedCornerShape(50.dp),
                border = BorderStroke(1.dp, Primary),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
            ) {
                Text(
                    buttonText,
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )
            }
        }
    }
}
