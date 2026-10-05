package com.babatunde.okido.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babatunde.okido.core.LaunchableApp
import com.babatunde.okido.ui.components.GroupedRow
import com.babatunde.okido.ui.components.GroupedSection
import com.babatunde.okido.ui.components.TimeRing
import com.babatunde.okido.ui.theme.OkidoTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun HomeScreen(
    apps: List<LaunchableApp>,
    remaining: Duration,
    onLaunch: (LaunchableApp) -> Unit = {},
    onMoreTime: () -> Unit = {},
    onLockNow: () -> Unit = {},
    onParentSettings: () -> Unit = {},
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.safeContentPadding(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            fullWidth {
                Text(
                    text = "Okido",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            fullWidth {
                GroupedSection(header = "Temps restant") {
                    TimeRing(
                        remaining = remaining,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 24.dp),
                    )
                }
            }
            fullWidth {
                Text(
                    text = "APPS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 32.dp, top = 8.dp),
                )
            }
            if (apps.isEmpty()) {
                fullWidth {
                    Text(
                        text = "Aucune app pour l'instant.\nUn parent peut en ajouter avec +",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    )
                }
            }
            items(apps, key = { it.id }) { app -> AppTile(app, onClick = { onLaunch(app) }) }
            item(key = "add") { AddAppTile(onClick = onParentSettings) }
            fullWidth {
                GroupedSection(modifier = Modifier.padding(top = 8.dp)) {
                    GroupedRow(
                        label = "Demander plus de temps",
                        labelColor = MaterialTheme.colorScheme.primary,
                        onClick = onMoreTime,
                    )
                    GroupedRow(
                        label = "Bloquer maintenant",
                        labelColor = MaterialTheme.colorScheme.error,
                        onClick = onLockNow,
                        showDivider = false,
                    )
                }
            }
            fullWidth {
                TextButton(onClick = onParentSettings) {
                    Text("Réglages parent", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

private fun LazyGridScope.fullWidth(content: @Composable () -> Unit) {
    item(span = { GridItemSpan(maxLineSpan) }) { content() }
}

@Composable
private fun AppTile(app: LaunchableApp, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppIcon(app, modifier = Modifier.size(60.dp))
        Spacer(Modifier.height(6.dp))
        Text(
            text = app.label,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/** Opens the parent settings, so adding an app still asks for the parent code. */
@Composable
private fun AddAppTile(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(AppIconShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Text("+", fontSize = 32.sp, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Ajouter",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
@Preview
private fun HomePreview() {
    OkidoTheme {
        HomeScreen(
            apps = listOf(
                LaunchableApp(id = "com.example.camera", label = "Appareil photo"),
                LaunchableApp(id = "com.example.clock", label = "Horloge"),
            ),
            remaining = 25.minutes,
        )
    }
}

@Composable
@Preview
private fun HomeEmptyPreview() {
    OkidoTheme { HomeScreen(apps = emptyList(), remaining = 3.minutes) }
}
