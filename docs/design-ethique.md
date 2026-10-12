# Design éthique

> **Statut : référence.** Ce document s'applique à toutes les features, existantes et futures.

Okido s'adresse à des enfants. La barre est donc plus haute que pour une app grand public : l'app doit protéger l'enfant **sans le surveiller**, et donner envie de poser l'écran **sans le manipuler**.

## Les références

**CNIL, recommandation 5 : un contrôle parental respectueux de l'enfant**
- Il doit être proportionné à l'âge et à la maturité de l'enfant, sans dispositif intrusif comme la géolocalisation permanente.
- Il doit être transparent : l'enfant est clairement informé du contrôle.
- La CNIL met en garde contre les fonctions trop intrusives : elles abîment la confiance, poussent l'enfant à cacher des choses et à s'autocensurer, et l'habituent à être surveillé.

**CNIL, recommandation 6 : l'information et les droits par le design**
- Des interfaces compréhensibles par un enfant : phrases courtes, icônes, information au bon moment.
- Des réglages protecteurs par défaut.
- Aucun dark pattern.
- Les engagements sont publiés sous une forme courte et compréhensible.

**ICO (Royaume-Uni), Children's Code** : 15 standards. Les plus utiles pour Okido sont :
- (1) l'intérêt de l'enfant passe en premier ;
- (7) les réglages sont protecteurs par défaut ;
- (8) on ne collecte que le strict nécessaire ;
- (11) **si un parent peut suivre l'activité de l'enfant, l'enfant voit un signe clair que le contrôle est actif** ;
- (13) pas de « nudge » : on ne met pas une option en avant pour pousser à un choix moins protecteur.

**Commission européenne, lignes directrices sur la protection des mineurs** (DSA, article 28, 14 juillet 2025)
- Pas de récompenses aléatoires ou intermittentes, ni de mécaniques proches du jeu d'argent.
- Pas de scroll infini ni de lecture automatique.
- Pas de signaux d'urgence.
- Ces lignes directrices visent les plateformes, mais elles servent de référence pour tout service utilisé par des enfants.

**Designers Éthiques** (collectif français)
- Ils proposent une méthode de diagnostic du design attentionnel : on parcourt l'app écran par écran pour vérifier que l'intention du designer et celle de l'utilisateur sont alignées.

## Okido face à ces critères

| Critère | État | Preuve |
|---|---|---|
| Aucune donnée collectée ni envoyée | ✅ | Le manifeste ne demande pas la permission `INTERNET`. Pas de compte, aucune dépendance d'analytics. |
| Pas de surveillance de l'activité | ✅ | Aucun historique : seule la session en cours est gardée (`SessionProgress`). Le parent ne voit pas ce que l'enfant a fait. |
| L'enfant sait que le contrôle est actif (ICO 11) | ✅ | Le compagnon et les minutes restantes sont visibles à l'accueil, puis l'écran « Bonne pause ! ». |
| Réglages protecteurs par défaut (ICO 7) | ✅ | Passer l'étape des apps à l'onboarding laisse toutes les apps bloquées. |
| Pas de récompense aléatoire, pas de notification | ✅ | Le compagnon ne réagit à rien et ne réclame jamais l'enfant (voir `docs/idees/compagnon.md`). |
| La récompense n'est jamais du temps d'écran | ✅ | C'est une règle produit. |
| Pas de pub, pas d'abonnement | ✅ (principe) | Okido+ sera un achat unique. Pas encore implémenté. |
| Engagements publiés sous forme courte (CNIL 6) | ⚠️ À faire | Une liste de quelques lignes, dans la fiche du store et dans l'app. |
| Okido expliqué à l'enfant avec ses mots (ICO 11, CNIL 5) | ⚠️ En partie | Les écrans du code parent expliquent à l'enfant que ce code est pour les parents. Reste un écran court qui dit pourquoi Okido est là et ce qu'il fait, sans rien cacher. |
| Diagnostic attentionnel complet | ✅ Fait le 12/10/2026 | Voir « Diagnostic écran par écran » plus bas. Des points restent ouverts. |

## Diagnostic écran par écran

Fait le 12/10/2026 : on a passé chaque écran au crible de la méthode Designers Éthiques et vérifié les contrastes (WCAG AA, 4,5:1 pour le texte courant).

### Ce qui va bien
- **Onboarding** :
  - les promesses (« tout reste sur ce téléphone ») sont vraies ;
  - la page « Pourquoi limiter » cite sa source et rappelle que c'est le parent qui choisit ;
  - le consentement à l'accessibilité dit ce qui est lu et ce qui ne l'est pas, et le message en cas de refus est neutre.
- **Réglages par défaut** : toutes les apps sont bloquées, même si le parent passe l'étape.
- **Choix de la durée** : des boutons identiques, sans durée longue mise en avant.
- **Temps restant** : affiché en minutes, sans secondes qui défilent.
- **Écrans du code parent** : ils expliquent à l'enfant que le code est pour les parents, et un mauvais code n'est pas grondé.

### Corrigé
| Constat | Correction |
|---|---|
| Sur l'écran de fin du temps, le seul gros bouton était « Demander plus de temps », et le texte relançait : « Demande à un parent s'il t'en faut encore. » C'est un nudge contraire au but de l'app (ICO 13). | Le bouton passe en secondaire (contour). Le titre « Temps écoulé », sec comme un verdict, devient « Bonne pause ! ». Le texte est celui du compagnon : « C'est l'heure de ma sieste. Et toi, tu fais quoi ? » |
| En thème clair, le gris des explications faisait 2,9:1, le rouge 3,2:1 et le bleu 3,6:1 : en dessous du minimum de 4,5:1. | Le gris passe à `#6C6C70` (4,7:1), le bleu à `#0062CC` (5,2:1) et le rouge à `#D70015` (4,8:1). |
| « Bloquer maintenant » était en rouge, la couleur du danger, et son nom sonnait comme une punition, alors qu'arrêter de soi-même est le geste à encourager. | Il devient « J'ai fini », dans la couleur du texte. |
| À l'accueil, « Demander plus de temps » était la première action, en bleu, même quand il restait du temps : une invitation permanente à en demander plus. | « J'ai fini » devient le bouton principal. « Demander plus de temps » n'est plus qu'un lien gris en dessous. |
| Si Android arrêtait le service d'accessibilité, Okido ne bloquait plus rien et personne ne le savait. | L'onboarding prévient que le blocage peut s'arrêter. Okido le vérifie à chaque retour sur l'accueil et le signale par un bandeau que l'enfant peut lire aussi. Le parent le réactive depuis les réglages parent. Il n'y a pas de notification permanente : ce serait du bruit, et elle ne servirait à rien une fois le service arrêté. |

### Reste à faire
- **Choix des apps (onboarding)** : « Continuer » et « Plus tard » font la même chose. N'en garder qu'un.
- **Lecteur d'écran** : le compagnon n'a pas de description. Ajouter « Okido est éveillé » ou « Okido dort ».
- **Code des réglages** : le sous-titre s'adresse au parent (« Saisis ton code parent ») et l'aide juste dessous à l'enfant. Passer à un sous-titre neutre.
- **Thème sombre** : le texte blanc sur le bouton bleu `#0A84FF` fait 3,6:1. Assombrir ce bleu.
- **À refaire** à chaque nouvel écran.

## Règles pour les futures features

- Aucune donnée ne quitte l'appareil, et l'app ne demande pas la permission `INTERNET`.
- Pas d'historique de l'activité de l'enfant.
- L'enfant sait toujours qu'Okido est actif, et pourquoi.
- Pas de récompense aléatoire, de série à tenir, de compte à rebours anxiogène, ni de notification qui rappelle l'enfant.
- Aucun personnage ne montre de joie liée à l'écran, ni de tristesse ou de fatigue liée à la fin du temps.
- Les réglages par défaut sont les plus protecteurs.
- Les choix du parent sont présentés honnêtement, sans culpabiliser.
- Les textes restent justes même lus par un enfant : on parle de l'appareil et des parents, jamais de « ton enfant ».
- Jamais de pub, jamais de sponsor, des liens neutres.
- Achat unique, et aucune fonction de protection réservée à la version payante.

## Sources

- CNIL, [Recommandation 5 : promouvoir des outils de contrôle parental respectueux de la vie privée et de l'intérêt de l'enfant](https://www.cnil.fr/fr/recommandation-5-promouvoir-des-outils-de-controle-parental-respectueux-de-la-vie-privee-et-de)
- CNIL, [Recommandation 6 : renforcer l'information et les droits des mineurs par le design](https://www.cnil.fr/fr/recommandation-6-renforcer-linformation-et-les-droits-des-mineurs-par-le-design)
- CNIL, [Les 8 recommandations pour renforcer la protection des mineurs en ligne](https://www.cnil.fr/fr/la-cnil-publie-8-recommandations-pour-renforcer-la-protection-des-mineurs-en-ligne)
- ICO, [Age appropriate design: a code of practice for online services](https://ico.org.uk/for-organisations/uk-gdpr-guidance-and-resources/childrens-information/childrens-code-guidance-and-resources/age-appropriate-design-a-code-of-practice-for-online-services/)
  - [Standard 11 : Parental controls](https://ico.org.uk/for-organisations/uk-gdpr-guidance-and-resources/childrens-information/childrens-code-guidance-and-resources/age-appropriate-design-a-code-of-practice-for-online-services/11-parental-controls/)
  - [Standard 13 : Nudge techniques](https://ico.org.uk/for-organisations/uk-gdpr-guidance-and-resources/childrens-information/childrens-code-guidance-and-resources/age-appropriate-design-a-code-of-practice-for-online-services/13-nudge-techniques/)
- Commission européenne, [Guidelines on the protection of minors](https://digital-strategy.ec.europa.eu/en/library/commission-publishes-guidelines-protection-minors)
- Designers Éthiques, [méthode pour évaluer la captation de l'attention](https://maisouvaleweb.fr/designers-ethiques-publient-methode-evaluer-captation-de-lattention/)
