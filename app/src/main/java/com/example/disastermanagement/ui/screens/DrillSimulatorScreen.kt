package com.example.disastermanagement.ui.screens

import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.disastermanagement.ui.components.Card
import com.example.disastermanagement.ui.state.DrillQuestion
import com.example.disastermanagement.ui.state.DrillOption
import com.example.disastermanagement.ui.theme.*

@Composable
fun DrillSimulatorScreen(
    moduleName: String,
    reviewMode: Boolean,
    questions: List<DrillQuestion>,
    imageUrl: String,
    onDrillCompleted: () -> Unit,
    onBackToLearn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drillQuestions = if (questions.isNotEmpty()) questions else listOf(
        DrillQuestion(
            prompt = "What is your immediate action?",
            options = listOf(
                DrillOption("A", "Run outside"),
                DrillOption("B", "Hide under desk"),
                DrillOption("C", "Stand by window")
            ),
            correctOptionId = "B",
            explanation = "During shaking, Drop, Cover, and Hold On under sturdy shelter."
        )
    )

    val currentQuestionIndex = rememberSaveable(moduleName) { mutableIntStateOf(0) }
    var selectedOption by rememberSaveable(moduleName) { mutableStateOf<String?>(null) }
    var remainingSeconds by rememberSaveable(moduleName) { mutableIntStateOf(45) }
    var timedOut by rememberSaveable(moduleName) { mutableStateOf(false) }
    var resultCorrect by rememberSaveable(moduleName) { mutableStateOf<Boolean?>(null) }

    val activeQuestionIndex = currentQuestionIndex.intValue.coerceIn(0, drillQuestions.lastIndex)
    val activeQuestion = drillQuestions[activeQuestionIndex]
    val isFinalQuestion = activeQuestionIndex == drillQuestions.lastIndex

    val timerRef = remember { mutableStateOf<CountDownTimer?>(null) }
    DisposableEffect(moduleName, activeQuestionIndex) {
        timerRef.value?.cancel()
        timerRef.value = object : CountDownTimer(45_000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                remainingSeconds = (millisUntilFinished / 1000L).toInt()
            }

            override fun onFinish() {
                remainingSeconds = 0
                if (resultCorrect == null) {
                    timedOut = true
                    resultCorrect = false
                    if (!reviewMode && isFinalQuestion) {
                        onDrillCompleted()
                    }
                }
            }
        }.start()

        onDispose {
            timerRef.value?.cancel()
            timerRef.value = null
        }
    }

    val optionsEnabled = !timedOut && resultCorrect == null
    val resultMessage = when {
        timedOut -> "Time's up. ${activeQuestion.explanation}"
        resultCorrect == true -> activeQuestion.explanation
        else -> activeQuestion.explanation
    }
    val resultActionLabel = if (isFinalQuestion) "Back to Learn" else "Next"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = TertiaryFixed,
                shape = RoundedCornerShape(50.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Sensors,
                        contentDescription = null,
                        tint = OnTertiaryFixed,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Active Drill",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = OnTertiaryFixed
                    )
                }
            }

            Text(
                text = moduleName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Primary,
                lineHeight = 32.sp
            )

            Text(
                text = "Respond quickly to secure your safety.",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Timer Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = null,
                    tint = TertiaryContainer,
                    modifier = Modifier.size(32.dp)
                )

                Text(
                    text = "00:${remainingSeconds.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    fontSize = 36.sp
                )

                Text(
                    text = "TIME REMAINING",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Outline,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Scenario Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceContainerLow)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Earthquake drill scenario",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Question
        Text(
            text = activeQuestion.prompt,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = OnBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Options
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            activeQuestion.options.forEach { option ->
                OptionButton(
                    letter = option.id,
                    text = option.text,
                    isSelected = selectedOption == option.id,
                    enabled = optionsEnabled
                ) { selectedOption = option.id }
            }
        }

        if (timedOut) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Time's up",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFBA1A1A)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Confirm Button
        Button(
            onClick = {
                timerRef.value?.cancel()
                val isCorrect = selectedOption == activeQuestion.correctOptionId
                resultCorrect = isCorrect
                timedOut = false
                if (!reviewMode && isFinalQuestion) {
                    onDrillCompleted()
                }
            },
            enabled = selectedOption != null && optionsEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SecondaryContainer,
                disabledContainerColor = Color(0xFF9AA0A6)
            ),
            shape = RoundedCornerShape(50.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Confirm Selection",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        if (resultCorrect != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (resultCorrect == true) Color(0xFFDFF8E7) else Color(0xFFFFE2E2))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = if (resultCorrect == true) Color(0xFF0A7F42) else Color(0xFFBA1A1A)
                        )
                        Text(
                            text = if (resultCorrect == true) "Correct decision" else if (timedOut) "Time's up" else "Not quite right",
                            fontWeight = FontWeight.Bold,
                            color = if (resultCorrect == true) Color(0xFF0A7F42) else Color(0xFFBA1A1A)
                        )
                    }
                    Text(
                        text = resultMessage,
                        color = if (resultCorrect == true) Color(0xFF0A7F42) else Color(0xFF7A1C1C)
                    )
                    Button(
                        onClick = {
                            if (isFinalQuestion) {
                                onBackToLearn()
                            } else {
                                currentQuestionIndex.intValue += 1
                                selectedOption = null
                                remainingSeconds = 45
                                timedOut = false
                                resultCorrect = null
                            }
                        },
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(resultActionLabel)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun OptionButton(
    letter: String,
    text: String,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = { if (enabled) onClick() },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) SurfaceContainer else SurfaceContainerLowest.copy(alpha = if (enabled) 1f else 0.6f),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, PrimaryContainer)
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.5f))
        }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) PrimaryContainer else SurfaceContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Primary
                )
            }

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                color = OnBackground,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Selected",
                    tint = PrimaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
