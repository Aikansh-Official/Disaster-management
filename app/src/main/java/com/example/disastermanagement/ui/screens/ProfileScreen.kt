package com.example.disastermanagement.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.disastermanagement.ui.components.Card
import com.example.disastermanagement.ui.state.BadgeState
import com.example.disastermanagement.ui.state.TrainingModule
import com.example.disastermanagement.ui.theme.*

@Composable
fun ProfileScreen(
    readinessScore: Float,
    modules: List<TrainingModule>,
    drillsCompleted: Int,
    badges: List<BadgeState>,
    onViewAllBadges: () -> Unit,
    modifier: Modifier = Modifier
) {
    var readinessTarget by remember { mutableFloatStateOf(0f) }
    var knowledgeTarget by remember { mutableFloatStateOf(0f) }
    var drillsTarget by remember { mutableFloatStateOf(0f) }
    var equipmentTarget by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(readinessScore, modules, drillsCompleted) {
        readinessTarget = readinessScore.coerceIn(0f, 1f)
        knowledgeTarget = if (modules.isEmpty()) 0f else modules.count { it.progress >= 1f }.toFloat() / modules.size
        drillsTarget = (drillsCompleted / 10f).coerceIn(0f, 1f)
        equipmentTarget = 0.9f
    }

    val readinessProgress by animateFloatAsState(readinessTarget, label = "profile_readiness")
    val knowledgeProgress by animateFloatAsState(knowledgeTarget, label = "profile_knowledge")
    val drillsProgress by animateFloatAsState(drillsTarget, label = "profile_drills")
    val equipmentProgress by animateFloatAsState(equipmentTarget, label = "profile_equipment")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Profile Header Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBfxF4vPpjmZP2vl7BLyksIyMh3Uyh62PdJin8QDYULwB0OPjL11uI1e7PBJavBGB-xTGj7H_-9qOKZ-ZFmOcrrfz6R-5jI-s3Wrr2ics3pfBR3h7ZJNwNpqaLvq1cLOW5wlrJcCFFVlNh221zyPW7IMIX30hFVWAzlMCLOYfFZrqJEjV7Lk5W9UoW9JTz5LnFPPs76iD_VNITcQlcX1hkJoEv117VdT1a67m-4aVW1lMxSyf_8BwWpmhjkEe9jnZj3r3T1rvsXtH--",
                        contentDescription = "Profile Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(SurfaceVariant),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(SecondaryContainer)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Verified,
                            contentDescription = "Verified",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = "USER",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Surface(
                    color = SurfaceContainerHigh,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MilitaryTech,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Silver Responder",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Readiness Score Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Circular Progress
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { readinessProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = PrimaryContainer,
                        trackColor = SurfaceContainerHigh,
                        strokeWidth = 10.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = (readinessProgress * 100).toInt().toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                        Text(
                            text = "Readiness",
                            style = MaterialTheme.typography.labelSmall,
                            color = Outline
                        )
                    }
                }

                // Stats Breakdown
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatProgressBar(label = "Knowledge", value = knowledgeProgress, color = PrimaryContainer)
                    StatProgressBar(label = "Drills Completed", value = drillsProgress, color = InversePrimary)
                    StatProgressBar(label = "Equipment Prepped", value = equipmentProgress, color = SecondaryContainer)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Badges Section Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Badges",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    TextButton(onClick = onViewAllBadges) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("View All", color = PrimaryContainer)
                            Icon(
                                Icons.Outlined.ChevronRight,
                                contentDescription = null,
                                tint = PrimaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    badges.forEach { badge ->
                        val visuals = badgeVisuals(badge)
                        ProfileBadgeItem(
                            name = badge.title,
                            icon = visuals.first,
                            bgColor = visuals.second,
                            iconColor = visuals.third,
                            isLocked = !badge.unlocked,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Leaderboard Section Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.EmojiEvents, contentDescription = null, tint = SecondaryContainer)
                        Text(
                            text = "Leaderboard",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Surface(
                        color = SurfaceContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Top 10%",
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LeaderboardUserItem(rank = 1, name = "Sarah M.", points = 1240, avatarText = "SM")
                    LeaderboardUserItem(rank = 2, name = "James R.", points = 1180, avatarText = "JR")
                    LeaderboardUserItem(
                        rank = 3,
                        name = "You",
                        points = 1050,
                        isCurrentUser = true,
                        avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDrZf25tm_PB1OzyZEN6RTuyqjtUEm1mBMFUqCLlqSX4C2Ng5CrC6kYTbWXohjBBrgMyIaqKC5m5Wu6e5X-y-YD3LqaRvhWRM3x1E8sjKzg5npnrg_LCvAzUFQDxeOB2_kPA9XIn7SG_MW6tWluFV3LnAIxjZasDETR1v2QPwwJbWaUgMmYTDiC_hJf9eTWk64cfg5Au3KgQOJwK_iytZn5jRCrBU04mzETZIBcy0P1AaFmi6uAIbktnI68FfY-wEgvdZhUsDq_D3l3"
                    )
                    LeaderboardUserItem(rank = 4, name = "Elena L.", points = 980, avatarText = "EL")
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun StatProgressBar(label: String, value: Float, color: Color) {
    val clamped = value.coerceIn(0f, 1f)
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Text(
                text = "${(clamped * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
        LinearProgressIndicator(
            progress = { clamped },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = color,
            trackColor = SurfaceContainerHigh
        )
    }
}

private fun badgeVisuals(badge: BadgeState): Triple<ImageVector, Color, Color> {
    return when (badge.key) {
        "fire_safety" -> Triple(Icons.Outlined.LocalFireDepartment, SurfaceContainer, PrimaryContainer)
        "first_aid" -> Triple(Icons.Outlined.MedicalServices, SecondaryFixed, Secondary)
        "flood_prep" -> Triple(Icons.Outlined.WaterDrop, TertiaryFixed, Tertiary)
        else -> Triple(Icons.Outlined.Emergency, SurfaceVariant, Outline)
    }
}

@Composable
fun ProfileBadgeItem(
    name: String,
    icon: ImageVector,
    bgColor: Color,
    iconColor: Color,
    isLocked: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(if (isLocked) bgColor.copy(alpha = 0.5f) else bgColor)
                .then(if (!isLocked) Modifier.border(1.dp, iconColor.copy(alpha = 0.2f), CircleShape) else Modifier),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = if (isLocked) iconColor.copy(alpha = 0.5f) else iconColor,
                modifier = Modifier.size(32.dp)
            )
            if (isLocked) {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Locked",
                    tint = Outline,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(16.dp)
                )
            }
        }
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = if (isLocked) TextSecondary.copy(alpha = 0.6f) else TextPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun LeaderboardUserItem(
    rank: Int,
    name: String,
    points: Int,
    avatarText: String? = null,
    avatarUrl: String? = null,
    isCurrentUser: Boolean = false
) {
    Surface(
        color = if (isCurrentUser) SurfaceContainerLow else Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        border = if (isCurrentUser) BorderStroke(1.dp, PrimaryContainer.copy(alpha = 0.2f)) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = rank.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = if (isCurrentUser) PrimaryContainer else Outline,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.Center
            )

            if (avatarUrl != null) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(1.dp, PrimaryFixed, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatarText ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryContainer
                    )
                }
            }

            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Normal,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${java.text.NumberFormat.getIntegerInstance().format(points)} pts",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (isCurrentUser) PrimaryContainer else TextPrimary
            )
        }
    }
}
