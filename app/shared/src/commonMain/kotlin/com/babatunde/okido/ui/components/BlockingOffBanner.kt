package com.babatunde.okido.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babatunde.okido.ui.theme.OkidoTheme

/**
 * Says that Okido no longer blocks apps. Read by the child as much as by a parent, so it only states the fact
 * and who can fix it: turning blocking back on goes through the parent settings, never straight to Android's.
 */
@Composable
fun BlockingOffBanner(modifier: Modifier = Modifier) {
    Text(
        text = "Okido ne bloque plus les apps. Un parent peut le réactiver dans les réglages parent.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Composable
@Preview
private fun BlockingOffBannerPreview() {
    OkidoTheme { BlockingOffBanner() }
}
