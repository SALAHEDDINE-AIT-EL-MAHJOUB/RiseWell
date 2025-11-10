package com.example.risewell.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.risewell.data.model.ActivityLevel
import androidx.compose.material.icons.filled.Edit
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
    var name by remember { mutableStateOf(userProfile?.name ?: "") }
    var selectedActivityLevel by remember {
        mutableStateOf(userProfile?.activityLevel ?: ActivityLevel.MODERATE_ACTIVE)
    }
    var selectedGoal by remember {
        mutableStateOf(userProfile?.goal ?: Goal.BETTER_HEALTH)
    }

    // editing state: if no profile provided start in edit mode, otherwise show read-only by default
    var isEditing by remember { mutableStateOf(userProfile == null) }

    // keep local fields in sync if the userProfile coming from parent changes (e.g. after saving)
    LaunchedEffect(userProfile) {
        userProfile?.let { profile ->
            name = profile.name
            age = profile.age.toString()
            weight = profile.weightKg.toString()
            height = profile.heightCm.toString()
            selectedActivityLevel = profile.activityLevel
            selectedGoal = profile.goal
            // switch to view mode when a profile exists
            isEditing = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header with circular avatar and title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    tonalElevation = 4.dp,
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = if (name.isNotBlank()) name else "Mon profil",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Mettez à jour vos informations personnelles",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isEditing) {
                // Form grouped in a card for clearer hierarchy
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nom") },
                            modifier = Modifier.fillMaxWidth()
                        )

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
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val profile = UserProfile(
                            id = userProfile?.id ?: 0,
                            name = name,
                            age = age.toIntOrNull() ?: 0,
                            weightKg = weight.toFloatOrNull() ?: 0f,
                            heightCm = height.toIntOrNull() ?: 0,
                            activityLevel = selectedActivityLevel,
                            goal = selectedGoal
                        )
                        onSaveProfile(profile)
                        // hide form and show profile summary
                        isEditing = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Save Profile", style = MaterialTheme.typography.labelLarge)
                }
            } else {
                // read-only summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Nom: ${name.ifBlank { "-" }}", style = MaterialTheme.typography.bodyLarge)
                        Text("Age: ${age.ifBlank { "-" }}", style = MaterialTheme.typography.bodyLarge)
                        Text("Weight: ${weight.ifBlank { "-" }} kg", style = MaterialTheme.typography.bodyLarge)
                        Text("Height: ${height.ifBlank { "-" }} cm", style = MaterialTheme.typography.bodyLarge)
                        Text("Activity: ${selectedActivityLevel.name.replace('_', ' ')}", style = MaterialTheme.typography.bodyLarge)
                        Text("Goal: ${selectedGoal.name.replace('_', ' ')}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
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
