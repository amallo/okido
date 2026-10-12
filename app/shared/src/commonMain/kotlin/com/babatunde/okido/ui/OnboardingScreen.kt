package com.babatunde.okido.ui

import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.babatunde.okido.core.AppAccess
import com.babatunde.okido.core.AppSetting
import com.babatunde.okido.core.LaunchableApp
import com.babatunde.okido.ui.components.Companion
import com.babatunde.okido.ui.components.PinPad
import com.babatunde.okido.ui.theme.OkidoColors
import com.babatunde.okido.ui.theme.OkidoTheme
import okido.shared.generated.resources.Res
import okido.shared.generated.resources.accessibility
import okido.shared.generated.resources.hourglass
import okido.shared.generated.resources.lock
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/** First launch only: what Okido does, how much screen time is advised, the privacy promise, the parent PIN, the allowed apps, then turning blocking on. */
@Composable
fun OnboardingScreen(
    state: OnboardingUiState,
    onContinue: () -> Unit = {},
    onBack: () -> Unit = {},
    onPinChange: (String) -> Unit = {},
    onSubmitPin: () -> Unit = {},
    onAccessChange: (AppSetting, AppAccess) -> Unit = { _, _ -> },
    onOpenSettings: () -> Unit = {},
    onDecline: () -> Unit = {},
    onSetAsHome: () -> Unit = {},
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
                        illustration = { IconBadge(Res.drawable.hourglass, Color.White, tint = Color.Unspecified) },
                        title = "Bienvenue sur Okido",
                        body = "Choisis les apps de cet appareil et le temps à y passer. " +
                            "Quand le temps est écoulé, l'écran se verrouille jusqu'à ce qu'un parent en accorde plus.",
                        action = "Continuer",
                        onAction = onContinue,
                    )
                    OnboardingPage.Guidelines -> GuidelinesPage(onContinue)
                    OnboardingPage.Privacy -> InfoPage(
                        illustration = { IconBadge(Res.drawable.lock, OkidoColors.Green) },
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
                    OnboardingPage.ChooseApps -> ChooseAppsPage(state, onAccessChange, onContinue)
                    OnboardingPage.EnableBlocking -> InfoPage(
                        illustration = { IconBadge(Res.drawable.accessibility, OkidoColors.Blue) },
                        title = "Autoriser Okido à bloquer les apps",
                        body = "Pour bloquer les apps non autorisées, Okido utilise le service d'accessibilité " +
                            "d'Android. Il lit uniquement le nom de l'app affichée à l'écran, pour la comparer " +
                            "à ta liste d'apps autorisées et fermer celles qui ne le sont pas.\n\n" +
                            "Okido ne lit pas le contenu de l'écran ni ce qui est tapé. Rien n'est enregistré " +
                            "ni envoyé : tout reste sur ce téléphone.\n\n" +
                            "Si tu acceptes, les réglages d'Android s'ouvrent : touche « Okido » puis active le service.",
                        action = "J'accepte",
                        onAction = onOpenSettings,
                        secondaryAction = "Refuser",
                        onSecondaryAction = onDecline,
                        note = if (state.blockingDeclined) {
                            "Sans ce service, Okido ne peut pas bloquer les apps. Tu peux accepter quand tu veux."
                        } else {
                            null
                        },
                    )
                    OnboardingPage.SetAsHome -> InfoPage(
                        illustration = { IconBadge(Res.drawable.hourglass, Color.White, tint = Color.Unspecified) },
                        title = "Faire d'Okido l'écran d'accueil",
                        body = "Le bouton d'accueil du téléphone ramène alors toujours sur Okido, " +
                            "et jamais sur les autres apps.\n\n" +
                            "Les réglages d'Android s'ouvrent : choisis « Okido » comme appli d'écran d'accueil.",
                        action = "Choisir Okido",
                        onAction = onSetAsHome,
                    )
                    OnboardingPage.Done -> Unit
                }
            }
        }
    }
}

private val pagesWithBack = setOf(OnboardingPage.Guidelines, OnboardingPage.Privacy, OnboardingPage.CreatePin, OnboardingPage.ConfirmPin)

/** Creating and confirming the PIN share one dot. */
private fun OnboardingPage.dotIndex(): Int = when (this) {
    OnboardingPage.Welcome -> 0
    OnboardingPage.Guidelines -> 1
    OnboardingPage.Privacy -> 2
    OnboardingPage.CreatePin, OnboardingPage.ConfirmPin -> 3
    OnboardingPage.ChooseApps -> 4
    OnboardingPage.EnableBlocking -> 5
    OnboardingPage.SetAsHome, OnboardingPage.Done -> 6
}

@Composable
private fun ProgressDots(current: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(7) { index ->
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
private fun GuidelinesPage(onContinue: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))
        Companion(sleepiness = 0.5f, modifier = Modifier.size(104.dp))
        Spacer(Modifier.height(24.dp))
        Text("Pourquoi limiter les écrans ?", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Trop d'écrans prend la place du sommeil, du jeu et du mouvement, et fatigue les yeux des enfants.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        // The recommendation stands out as a quote, in the companion's colors.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "« Pour les enfants à partir de 6 ans, un usage modéré, encadré par un adulte. »",
                style = MaterialTheme.typography.titleLarge,
                color = OkidoColors.Indigo,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Commission « Enfants et écrans », remise au gouvernement en 2024",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "C'est toi qui choisis le temps accordé.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onContinue,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp),
        ) {
            Text("Continuer", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.height(16.dp))
    }
}

/** The same rows as in the parent settings, where the choice can be changed later. */
@Composable
private fun ChooseAppsPage(
    state: OnboardingUiState,
    onAccessChange: (AppSetting, AppAccess) -> Unit,
    onContinue: () -> Unit,
) {
    val apps = state.apps
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Quelles apps autoriser ?", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "L'accueil d'Okido ne montre que les apps autorisées. " +
                            "Toutes les autres sont bloquées.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Un parent pourra changer ce choix dans les réglages parent.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            itemsIndexed(apps, key = { _, setting -> setting.app.id }) { index, setting ->
                val isLast = index == apps.lastIndex
                AppAccessRow(
                    setting = setting,
                    icon = state.icons[setting.app.id],
                    onAccessChange = { onAccessChange(setting, it) },
                    showDivider = !isLast,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(cardSlice(isFirst = index == 0, isLast = isLast))
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                )
            }
        }
        Button(
            onClick = onContinue,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp, top = 16.dp).height(52.dp),
        ) {
            Text("Continuer", style = MaterialTheme.typography.titleMedium)
        }
        // Skipping leaves every app blocked; the parent settings are there for later.
        TextButton(onClick = onContinue, modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)) {
            Text("Plus tard", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun IconBadge(icon: DrawableResource, color: Color, tint: Color = Color.White) {
    Box(
        modifier = Modifier.size(96.dp).background(color, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(52.dp),
        )
    }
}

@Composable
private fun InfoPage(
    illustration: @Composable () -> Unit,
    title: String,
    body: String,
    action: String,
    onAction: () -> Unit,
    secondaryAction: String? = null,
    onSecondaryAction: () -> Unit = {},
    note: String? = null,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))
        illustration()
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
        if (secondaryAction != null) {
            TextButton(onClick = onSecondaryAction, modifier = Modifier.fillMaxWidth()) {
                Text(secondaryAction, style = MaterialTheme.typography.bodyLarge)
            }
        }
        if (note != null) {
            Text(
                text = note,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
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
private fun GuidelinesPreview() {
    OkidoTheme { OnboardingScreen(OnboardingUiState(page = OnboardingPage.Guidelines)) }
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
private fun ChooseAppsPreview() {
    OkidoTheme {
        OnboardingScreen(
            OnboardingUiState(
                page = OnboardingPage.ChooseApps,
                apps = listOf(
                    AppSetting(LaunchableApp(id = "com.example.camera", label = "Appareil photo"), AppAccess.Blocked),
                    AppSetting(LaunchableApp(id = "com.example.clock", label = "Horloge"), AppAccess.Timed),
                    AppSetting(LaunchableApp(id = "com.example.radio", label = "Radio"), AppAccess.Always),
                ),
            ),
        )
    }
}

@Composable
@Preview
private fun EnableBlockingPreview() {
    OkidoTheme { OnboardingScreen(OnboardingUiState(page = OnboardingPage.EnableBlocking)) }
}

@Composable
@Preview
private fun SetAsHomePreview() {
    OkidoTheme { OnboardingScreen(OnboardingUiState(page = OnboardingPage.SetAsHome)) }
}

@Composable
@Preview
private fun BlockingDeclinedPreview() {
    OkidoTheme { OnboardingScreen(OnboardingUiState(page = OnboardingPage.EnableBlocking, blockingDeclined = true)) }
}
