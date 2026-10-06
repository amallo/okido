package com.babatunde.okido.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babatunde.okido.ui.components.PinPad
import com.babatunde.okido.ui.theme.OkidoColors
import com.babatunde.okido.ui.theme.OkidoTheme
import okido.shared.generated.resources.Res
import okido.shared.generated.resources.accessibility
import okido.shared.generated.resources.hourglass
import okido.shared.generated.resources.lock
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/** First launch only: what Okido does, the privacy promise, the parent PIN, then turning blocking on. */
@Composable
fun OnboardingScreen(
    state: OnboardingUiState,
    onContinue: () -> Unit = {},
    onBack: () -> Unit = {},
    onPinChange: (String) -> Unit = {},
    onSubmitPin: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().safeContentPadding()) {
            Box(modifier = Modifier.fillMaxWidth().height(48.dp)) {
                if (state.page in pagesWithBack) {
                    TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                        Text("Retour", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                ProgressDots(current = state.page.dotIndex(), modifier = Modifier.align(Alignment.Center))
            }
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (state.page) {
                    OnboardingPage.Welcome -> InfoPage(
                        icon = Res.drawable.hourglass,
                        iconColor = Color.White,
                        iconTint = Color.Unspecified,
                        title = "Bienvenue sur Okido",
                        body = "Choisis les apps de ton enfant et le temps à y passer. " +
                            "Quand le temps est écoulé, l'écran se verrouille jusqu'à ce que tu en accordes plus.",
                        action = "Continuer",
                        onAction = onContinue,
                    )
                    OnboardingPage.Privacy -> InfoPage(
                        icon = Res.drawable.lock,
                        iconColor = OkidoColors.Green,
                        title = "Tout reste sur ce téléphone",
                        body = "Aucun compte à créer, aucun cloud. Les réglages et le code parent restent " +
                            "sur cet appareil et ne sont jamais envoyés sur internet.",
                        action = "Continuer",
                        onAction = onContinue,
                    )
                    OnboardingPage.CreatePin -> PinPad(
                        title = "Crée ton code parent",
                        subtitle = "pour accorder du temps et ouvrir les réglages",
                        errorMessage = "Les codes ne correspondent pas, recommence",
                        pin = state.pin,
                        onPinChange = onPinChange,
                        onSubmit = onSubmitPin,
                        error = state.mismatch,
                        modifier = Modifier.align(Alignment.Center),
                    )
                    OnboardingPage.ConfirmPin -> PinPad(
                        title = "Confirme ton code",
                        subtitle = "Saisis-le une deuxième fois",
                        pin = state.pin,
                        onPinChange = onPinChange,
                        onSubmit = onSubmitPin,
                        modifier = Modifier.align(Alignment.Center),
                    )
                    OnboardingPage.EnableBlocking -> InfoPage(
                        icon = Res.drawable.accessibility,
                        iconColor = OkidoColors.Blue,
                        title = "Active le blocage",
                        body = "Okido utilise le service d'accessibilité pour savoir quelle app est ouverte " +
                            "et fermer celles qui ne sont pas autorisées. Rien ne quitte le téléphone.\n\n" +
                            "Dans les réglages, touche « Okido » puis active le service.",
                        action = "Ouvrir les réglages",
                        onAction = onOpenSettings,
                    )
                    OnboardingPage.Done -> Unit
                }
            }
        }
    }
}

private val pagesWithBack = setOf(OnboardingPage.Privacy, OnboardingPage.CreatePin, OnboardingPage.ConfirmPin)

/** Creating and confirming the PIN share one dot. */
private fun OnboardingPage.dotIndex(): Int = when (this) {
    OnboardingPage.Welcome -> 0
    OnboardingPage.Privacy -> 1
    OnboardingPage.CreatePin, OnboardingPage.ConfirmPin -> 2
    OnboardingPage.EnableBlocking, OnboardingPage.Done -> 3
}

@Composable
private fun ProgressDots(current: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(4) { index ->
            val color = if (index == current) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
            Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        }
    }
}

@Composable
private fun InfoPage(
    icon: DrawableResource,
    iconColor: Color,
    title: String,
    body: String,
    action: String,
    onAction: () -> Unit,
    iconTint: Color = Color.White,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier.size(96.dp).background(iconColor, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(52.dp),
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onAction,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp),
        ) {
            Text(action, style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
@Preview
private fun WelcomePreview() {
    OkidoTheme { OnboardingScreen(OnboardingUiState(page = OnboardingPage.Welcome)) }
}

@Composable
@Preview
private fun PrivacyPreview() {
    OkidoTheme { OnboardingScreen(OnboardingUiState(page = OnboardingPage.Privacy)) }
}

@Composable
@Preview
private fun CreatePinPreview() {
    OkidoTheme { OnboardingScreen(OnboardingUiState(page = OnboardingPage.CreatePin, pin = "25")) }
}

@Composable
@Preview
private fun EnableBlockingPreview() {
    OkidoTheme { OnboardingScreen(OnboardingUiState(page = OnboardingPage.EnableBlocking)) }
}
