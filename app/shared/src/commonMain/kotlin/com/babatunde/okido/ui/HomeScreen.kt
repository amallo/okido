package com.babatunde.okido.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babatunde.okido.core.LaunchableApp

@Composable
@Preview
fun HomeScreen(
    apps: List<LaunchableApp> = listOf(
        LaunchableApp(id = "com.example.camera", label = "Appareil photo"),
        LaunchableApp(id = "com.example.clock", label = "Horloge"),
    ),
    onLaunch: (LaunchableApp) -> Unit = {},
    onMoreTime: () -> Unit = {},
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.safeContentPadding().padding(16.dp)) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(apps, key = { it.id }) { app ->
                    Text(
                        text = app.label,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLaunch(app) }
                            .padding(vertical = 16.dp),
                    )
                    HorizontalDivider()
                }
            }
            OutlinedButton(onClick = onMoreTime, modifier = Modifier.fillMaxWidth()) {
                Text("Plus de temps")
            }
        }
    }
}
