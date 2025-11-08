package com.example.risewell.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.risewell.data.model.ActivityLevel
import com.example.risewell.data.model.Goal
import com.example.risewell.data.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProfile: UserProfile?,
    onSaveProfile: (UserProfile) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var age by remember { mutableStateOf(userProfile?.age?.toString() ?: "") }
    var weight by remember { mutableStateOf(userProfile?.weightKg?.toString() ?: "") }
    var height by remember { mutableStateOf(userProfile?.heightCm?.toString() ?: "") }
    var selectedActivityLevel by remember {
        mutableStateOf(userProfile?.activityLevel ?: ActivityLevel.MODERATE_ACTIVE)
    }
    var selectedGoal by remember {
        mutableStateOf(userProfile?.goal ?: Goal.BETTER_HEALTH)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Activity Level", style = MaterialTheme.typography.titleMedium)
            ActivityLevelSelector(
                selected = selectedActivityLevel,
                onSelect = { selectedActivityLevel = it }
            )

            Text("Goal", style = MaterialTheme.typography.titleMedium)
            GoalSelector(
                selected = selectedGoal,
                onSelect = { selectedGoal = it }
            )

            Button(
                onClick = {
                    val profile = UserProfile(
                        id = userProfile?.id ?: 0,
                        age = age.toIntOrNull() ?: 0,
                        weightKg = weight.toFloatOrNull() ?: 0f,
                        heightCm = height.toIntOrNull() ?: 0,
                        activityLevel = selectedActivityLevel,
                        goal = selectedGoal
                    )
                    onSaveProfile(profile)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Profile")
            }
        }
    }
}

@Composable
fun ActivityLevelSelector(
    selected: ActivityLevel,
    onSelect: (ActivityLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ActivityLevel.values().forEach { level ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioButton(
                    selected = selected == level,
                    onClick = { onSelect(level) }
                )
                Text(
                    text = level.name.replace('_', ' '),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
fun GoalSelector(
    selected: Goal,
    onSelect: (Goal) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Goal.values().forEach { goal ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioButton(
                    selected = selected == goal,
                    onClick = { onSelect(goal) }
                )
                Text(
                    text = goal.name.replace('_', ' '),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}
