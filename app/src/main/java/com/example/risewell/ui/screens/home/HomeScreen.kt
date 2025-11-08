package com.example.risewell.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.risewell.data.model.Persona

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPersonaSelected: (Persona) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("RiseWell")
                        Text(
                            "Votre coach santé IA",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profil"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Choisissez votre assistant :",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(Persona.values()) { persona ->
                    PersonaCard(
                        persona = persona,
                        onClick = { onPersonaSelected(persona) }
                    )
                }
            }
        }
    }
}

@Composable
fun PersonaCard(
    persona: Persona,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = getPersonaContainerColor(persona)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = getPersonaIcon(persona),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = getPersonaTitle(persona),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = getPersonaDescription(persona),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun getPersonaContainerColor(persona: Persona): androidx.compose.ui.graphics.Color {
    return when (persona) {
        Persona.COACH -> MaterialTheme.colorScheme.primaryContainer
        Persona.NUTRITIONIST -> MaterialTheme.colorScheme.tertiaryContainer
        Persona.MOTIVATOR -> MaterialTheme.colorScheme.secondaryContainer
        Persona.CONSULTANT -> MaterialTheme.colorScheme.surfaceVariant
    }
}

private fun getPersonaIcon(persona: Persona): ImageVector {
    return when (persona) {
        Persona.COACH -> Icons.Default.Favorite
        Persona.NUTRITIONIST -> Icons.Default.ShoppingCart
        Persona.MOTIVATOR -> Icons.Default.Star
        Persona.CONSULTANT -> Icons.Default.Build
    }
}

private fun getPersonaTitle(persona: Persona): String {
    return when (persona) {
        Persona.COACH -> "Coach Sportif"
        Persona.NUTRITIONIST -> "Nutritionniste"
        Persona.MOTIVATOR -> "Motivateur"
        Persona.CONSULTANT -> "Consultant"
    }
}

private fun getPersonaDescription(persona: Persona): String {
    return when (persona) {
        Persona.COACH -> "Plans d'entraînement personnalisés"
        Persona.NUTRITIONIST -> "Plans de repas équilibrés"
        Persona.MOTIVATOR -> "Inspiration quotidienne"
        Persona.CONSULTANT -> "Conseils santé globaux"
    }
}
