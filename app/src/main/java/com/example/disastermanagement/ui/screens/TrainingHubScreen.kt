package com.example.disastermanagement.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.disastermanagement.ui.state.TrainingModule
import com.example.disastermanagement.ui.theme.Background
import com.example.disastermanagement.ui.theme.PrimaryContainer
import com.example.disastermanagement.ui.theme.Surface
import com.example.disastermanagement.ui.theme.Tertiary
import com.example.disastermanagement.ui.theme.TextPrimary
import com.example.disastermanagement.ui.theme.TextSecondary

@Composable
fun TrainingHubScreen(
    modules: List<TrainingModule>,
    filters: List<String>,
    initialFilter: String?,
    onStartModule: (String) -> Unit,
    onReviewModule: (String) -> Unit
) {
    var selectedFilter by rememberSaveable { mutableStateOf(initialFilter ?: filters.firstOrNull().orEmpty()) }

    LaunchedEffect(initialFilter) {
        if (!initialFilter.isNullOrBlank()) {
            selectedFilter = initialFilter
        }
    }

    val filteredModules = modules.filter { module ->
        module.category.label.equals(selectedFilter, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        TopBar()

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                HeaderSection()
            }

            item {
                CategoryTabs(
                    filters = filters,
                    selectedFilter = selectedFilter,
                    onSelect = { selectedFilter = it }
                )
            }

            items(filteredModules, key = { it.title }) { module ->
                ModuleCard(
                    module = module,
                    onStart = { onStartModule(module.title) },
                    onReview = { onReviewModule(module.title) }
                )
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.Icon(Icons.Outlined.Menu, contentDescription = null, tint = PrimaryContainer)
        Text("Disaster Training", fontWeight = FontWeight.Bold, color = PrimaryContainer)
        androidx.compose.material3.Icon(Icons.Outlined.Notifications, contentDescription = null, tint = PrimaryContainer)
    }
}

@Composable
fun HeaderSection() {
    Column {
        Text(
            "Training Hub",
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text("Master the skills to stay safe", color = TextSecondary)
    }
}

@Composable
fun CategoryTabs(filters: List<String>, selectedFilter: String, onSelect: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        filters.forEach { label ->
            val selected = label == selectedFilter
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (selected) PrimaryContainer else Tertiary)
                    .clickable { onSelect(label) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(label, color = if (selected) Color.White else TextPrimary)
            }
        }
    }
}

@Composable
fun ModuleCard(module: TrainingModule, onStart: () -> Unit, onReview: () -> Unit) {
    val status = when {
        module.progress >= 1f -> "COMPLETED"
        module.progress > 0f -> "IN PROGRESS"
        else -> "START"
    }
    val badgeColor = when (status) {
        "COMPLETED" -> Color(0xFF00897B)
        "IN PROGRESS" -> Color(0xFFF57C00)
        else -> Color(0xFF1F2937)
    }

    var progressTarget by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(module.progress) {
        progressTarget = module.progress.coerceIn(0f, 1f)
    }
    val animatedProgress by animateFloatAsState(targetValue = progressTarget, label = "module_progress")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
            .clickable(enabled = status == "COMPLETED", onClick = onReview)
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = module.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(badgeColor)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(status, color = Color.White, fontSize = MaterialTheme.typography.labelSmall.fontSize)
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(module.title, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(module.description, color = TextSecondary)

        Spacer(Modifier.height(10.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            color = PrimaryContainer,
            modifier = Modifier.fillMaxWidth()
        )

        if (status != "COMPLETED") {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onStart,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F2937))
            ) {
                Text("START")
            }
        }
    }
}
