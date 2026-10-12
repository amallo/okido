package com.babatunde.okido.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.babatunde.okido.core.AppAccess
import com.babatunde.okido.ui.theme.OkidoColors
import okido.shared.generated.resources.Res
import okido.shared.generated.resources.hourglass_outline
import okido.shared.generated.resources.infinity
import okido.shared.generated.resources.lock
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private fun AppAccess.icon(): DrawableResource = when (this) {
    AppAccess.Blocked -> Res.drawable.lock
    AppAccess.Timed -> Res.drawable.hourglass_outline
    AppAccess.Always -> Res.drawable.infinity
}

private fun AppAccess.color(): Color = when (this) {
    AppAccess.Blocked -> OkidoColors.SecondaryLabel
    AppAccess.Timed -> OkidoColors.Orange
    AppAccess.Always -> OkidoColors.Green
}

/** What the access means for the child, shown beside the app's name. */
private fun AppAccess.status(): String = when (this) {
    AppAccess.Blocked -> "toujours bloquée"
    AppAccess.Timed -> "temps limité"
    AppAccess.Always -> "toujours ouverte"
}

/** An access as an icon, in its color when [selected], named for screen readers. */
@Composable
internal fun AccessIcon(access: AppAccess, selected: Boolean = true, modifier: Modifier = Modifier.size(20.dp)) {
    Icon(
        painter = painterResource(access.icon()),
        contentDescription = access.label(),
        tint = if (selected) access.color() else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

@Composable
internal fun AccessStatus(access: AppAccess) {
    Text(text = access.status(), style = MaterialTheme.typography.bodyMedium, color = access.color())
}
