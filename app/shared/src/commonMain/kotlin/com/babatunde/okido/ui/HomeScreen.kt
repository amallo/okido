package com.babatunde.okido.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babatunde.okido.core.LaunchableApp
import com.babatunde.okido.ui.components.BlockingOffBanner
import com.babatunde.okido.ui.components.Companion
import com.babatunde.okido.ui.theme.OkidoTheme
import okido.shared.generated.resources.Res
import okido.shared.generated.resources.timer
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun HomeScreen(
    apps: List<LaunchableApp>,
    remaining: Duration,
    sleepiness: Float = 0f,
    icons: Map<String, ImageBitmap> = emptyMap(),
    blockingEnabled: Boolean = true,
    onLaunch: (LaunchableApp) -> Unit = {},
    onMoreTime: () -> Unit = {},
    onLockNow: () -> Unit = {},
    onParentSettings: () -> Unit = {},
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().safeContentPadding()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Okido",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.weight(1f),
                )
                TimePill(remaining)
            }
            if (!blockingEnabled) BlockingOffBanner(Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            // Only the apps scroll, so the time and the actions stay in view however many apps are allowed.
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Scrolls away with the apps, so it never takes room from them.
                fullWidth {
                    Box(Modifier.fillMaxWidth().padding(bottom = 8.dp), contentAlignment = Alignment.Center) {
                        Companion(sleepiness = sleepiness, modifier = Modifier.size(140.dp))
                    }
                }
                items(apps, key = { it.id }) { app -> AppTile(app, icons[app.id], onClick = { onLaunch(app) }) }
                item(key = "add") { AddAppTile(onClick = onParentSettings) }
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
            }
            // Stopping on one's own is the primary action, in the child's words; asking for more stays a plain
            // link, so the screen never invites the child to want more time.
            Button(
                onClick = onLockNow,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).height(52.dp),
            ) {
                Text("Faire une pause", style = MaterialTheme.typography.titleMedium)
            }
            TextButton(onClick = onMoreTime, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    "Demander plus de temps",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            TextButton(onClick = onParentSettings, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Réglages parent", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

/** The minutes left, like a status chip. The companion's sand shows the rest. */
@Composable
private fun TimePill(remaining: Duration) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.timer),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text("${remainingMinutes(remaining)} min", style = MaterialTheme.typography.titleMedium)
    }
}

/** Minutes left, rounded up, as shown to the child: "< 1" in the last minute. */
private fun remainingMinutes(remaining: Duration): String {
    val minutes = (remaining.inWholeSeconds + 59) / 60
    return if (minutes <= 1) "< 1" else "$minutes"
}

private fun LazyGridScope.fullWidth(content: @Composable () -> Unit) {
    item(span = { GridItemSpan(maxLineSpan) }) { content() }
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
            sleepiness = 0.6f,
        )
    }
}

@Composable
@Preview
private fun HomeBlockingOffPreview() {
    OkidoTheme {
        HomeScreen(
            apps = listOf(LaunchableApp(id = "com.example.camera", label = "Appareil photo")),
            remaining = 25.minutes,
            blockingEnabled = false,
        )
    }
}

@Composable
@Preview
private fun HomeManyAppsPreview() {
    OkidoTheme {
        HomeScreen(
            apps = (1..24).map { LaunchableApp(id = "com.example.app$it", label = "App $it") },
            remaining = 42.minutes,
        )
    }
}

@Composable
@Preview
private fun HomeEmptyPreview() {
    OkidoTheme { HomeScreen(apps = emptyList(), remaining = 3.minutes) }
}
