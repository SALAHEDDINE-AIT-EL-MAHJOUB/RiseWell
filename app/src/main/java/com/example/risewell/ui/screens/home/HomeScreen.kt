package com.example.risewell.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.risewell.R
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
                        Text(
                            "RiseWell",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            "Votre coach santé IA",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profil",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 0.dp
            ),
            modifier = modifier.fillMaxSize()
        ) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Choisissez votre assistant :",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            
            items(Persona.values()) { persona ->
                PersonaCard(
                    persona = persona,
                    onClick = { onPersonaSelected(persona) }
                )
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
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(20.dp),
        color = getPersonaContainerColor(persona)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Image with rounded corners and shadow effect
            Surface(
                modifier = Modifier
                    .size(80.dp),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 2.dp
            ) {
                Image(
                    painter = painterResource(id = getPersonaImage(persona)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = getPersonaTitle(persona),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = getPersonaDescription(persona),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
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

private fun getPersonaImage(persona: Persona): Int {
    return when (persona) {
        Persona.COACH -> R.drawable.coach
        Persona.NUTRITIONIST -> R.drawable.nutrition
        Persona.MOTIVATOR -> R.drawable.motivation
        Persona.CONSULTANT -> R.drawable.persona_consultant
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
