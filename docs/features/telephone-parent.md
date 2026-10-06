# Téléphone parent

> **Statut : non implémenté.** Prévu pour la version payante sur le store.

Le parent accorde du temps depuis **son propre téléphone**, sans réseau, sans serveur et sans compte. Les deux téléphones sont appairés une seule fois par QR code. Ensuite, le parent affiche un QR code signé que le téléphone de l'enfant scanne.

## Le besoin

Aujourd'hui, pour accorder du temps, le parent doit :
- prendre le téléphone de l'enfant en main ;
- taper son code PIN devant l'enfant, qui finit par le connaître à force de regarder.

La feature vise un usage **à proximité** : à la maison, en voiture.
Le parent loin de l'enfant n'est pas concerné. Ce cas sera couvert par les plages d'utilisation, définies à l'avance sur le téléphone de l'enfant.

## Scénario

**Appairage, une seule fois (par exemple dans la voiture)**
1. Sur le téléphone de l'enfant : « Réglages parent », puis le code PIN, puis « Associer un téléphone parent ». Le scanner s'ouvre.
2. Sur le téléphone du parent : « Ajouter un enfant ». Un QR code d'appairage s'affiche.
3. L'enfant scanne. C'est fait.

**Pendant le trajet**
1. Sur l'écran « Temps écoulé », l'enfant touche « Scanner le code du parent ».
2. Le parent choisit « +30 min » et lui tend son écran.
3. Scan : le temps est accordé. Le parent n'a ni touché le téléphone de l'enfant, ni tapé son code devant lui.

> Si c'est le conducteur, tendre l'écran reste une manipulation du téléphone au volant. C'est donc au passager de le faire, ou à l'arrêt.

## Principes respectés

- Aucun compte, aucun cloud, aucun serveur, aucun réseau : tout passe par l'appareil photo.
- La seule source de confiance est une **signature cryptographique**. Le téléphone de l'enfant connaît la clé publique du parent et vérifie chaque commande.
- Le téléphone de l'enfant ne peut pas fabriquer de commandes : il n'a que la clé publique, jamais la clé privée.

## Une app, deux rôles

Pas d'app séparée : une seule app à publier et à maintenir, avec tout le code partagé.

L'onboarding demande : « Téléphone de l'enfant » ou « Téléphone du parent ».

Le rôle **parent** :
- saute la création du code PIN et l'étape du service d'accessibilité ;
- affiche un accueil avec :
  - les enfants appairés ;
  - les boutons de durée (5, 15, 30 min, 1 h), qui affichent le QR code en grand, luminosité au maximum ;
  - « Ajouter un enfant ».

## Appairage

- **Protégé par le code PIN** sur le téléphone de l'enfant. Sans lui, l'enfant pourrait appairer une fausse app « parent » installée sur un autre appareil, et s'accorder du temps tout seul.
- Le téléphone du parent génère une paire de clés **Ed25519** :
  - la clé privée ne quitte jamais le téléphone (protégée par l'Android Keystore) ;
  - la clé publique, avec le nom du parent, va dans le QR code d'appairage.
- Retour facultatif : le téléphone de l'enfant affiche `{id appareil, prénom}`, que le parent scanne, pour viser un enfant précis.
- **Changement ou perte du téléphone parent** : depuis « Réglages parent » (code PIN), on retire l'ancien téléphone et on appaire le nouveau. Les codes du téléphone retiré sont refusés.
- **Réinstallation ou effacement des données** de l'une des deux apps : les clés sont perdues, il faut appairer de nouveau.
- **Deuxième parent** : chacun appaire son téléphone. L'enfant accepte les deux.
- **Plusieurs enfants** : le parent appaire chaque téléphone d'enfant une fois.

## Accorder du temps

QR code de commande : `{action: GrantTime, durée, horodatage, nonce}` plus sa signature.

Le téléphone de l'enfant **refuse** la commande si :
- la signature n'est pas valide ;
- la clé n'est pas appairée ;
- le nonce a déjà été utilisé, c'est-à-dire que le même QR code est rescanné ;
- elle a plus de 10 minutes, avec une tolérance de quelques minutes pour les horloges un peu décalées.

Le code PIN reste toujours utilisable sur le téléphone de l'enfant : le scan est un moyen de plus, il ne remplace rien.

## Architecture prévue

Elle respecte les règles de `app/CLAUDE.md` :
- le core en Kotlin pur ;
- les cas d'usage ne dépendent que de ports ;
- les API système passent par des ports.

**Core** (TDD, un test à la fois)
- Modèle `ParentCommand` (`GrantTime(durée)`, horodatage, nonce), avec un encodage canonique et le format du QR code. Il est partagé par les deux rôles.
- Ports :
  - `PairedParents` : `add(key)`, `isPaired(key)`, `remove(key)` ;
  - `SignatureVerifier` : `verify(publicKey, message, signature)` ;
  - `SeenCommands` : `wasSeen(nonce)`, `remember(nonce)` ;
  - `CommandSigner` (côté parent) ;
  - `DeviceRoleStore`.
- Cas d'usage :
  - `PairParent(publicKey)` ;
  - `ApplyParentCommand(signedCommand)`, qui renvoie `Granted`, `InvalidSignature`, `UnknownParent`, `Replayed` ou `Expired`. Il accorde le temps via les mêmes ports que `GrantExtraTime`, sans injecter ce cas d'usage ;
  - `SignParentCommand(duration)` (côté parent) ;
  - `UnpairParent(key)`.

**Infra Android**
- **Ed25519** :
  - natif seulement à partir d'Android 13 (API 33), alors que `minSdk` vaut 24 ;
  - en dessous, il faut une bibliothèque hors réseau (BouncyCastle ou Tink) ;
  - la clé privée est alors chiffrée par une clé AES du Keystore plutôt que stockée directement dedans.
- `SharedPreferencesPairedParents` et `SharedPreferencesDeviceRole`.
- `SharedPreferencesSeenCommands`, qui purge les nonces expirés.
- QR code :
  - génération et lecture avec **ZXing**, qui fonctionne sans les services Google ;
  - aperçu caméra avec CameraX ;
  - permission `CAMERA` demandée seulement au moment de scanner.

**UI**
- Onboarding : écran du choix du rôle.
- Enfant :
  - bouton « Scanner le code du parent » sur l'écran « Temps écoulé » ;
  - section « Téléphones parent » dans les réglages parent (associer, retirer).
- Parent : accueil avec les durées et l'écran du QR code.

**iOS** : la feature fonctionne telle quelle, puisqu'elle ne passe que par l'appareil photo. La signature passe par CryptoKit, et le blocage reste Screen Time.

## Options écartées ou reportées

| Option | Décision | Raison |
|---|---|---|
| Bluetooth LE | Reportée | Seul moyen de « bloquer maintenant » ou de voir le temps restant sans que l'enfant fasse rien. Coûteux : permissions, fiabilité, écoute permanente, limites d'iOS en arrière-plan. À reconsidérer si le scan devient gênant à l'usage. |
| Wi-Fi local (mDNS et socket) | Écartée | Pas de réseau commun en voiture. Ouvre un port qui écoute sur le téléphone de l'enfant. |
| Google Nearby Connections, ML Kit | Écartées | Dépendance aux services Google, contraire à la promesse de vie privée. |
| Secret partagé (HMAC) | Écartée | Un téléphone d'enfant rooté permettrait de fabriquer des commandes. La signature asymétrique l'empêche. |
| Serveur ou push | Écartée | Contraire aux principes : offline first, aucun cloud. |

Le format des commandes signées est indépendant du transport. Un transport Bluetooth pourra s'ajouter plus tard sans rien changer à l'appairage ni au core.

## Ordre de mise en œuvre

1. Core :
   - `ParentCommand` et son encodage ;
   - `PairParent` ;
   - `ApplyParentCommand`.
2. Infra : signature, vérification, stockage.
3. QR code : génération et scan.
4. UI enfant : appairage, scan, retrait d'un parent.
5. Rôle parent : onboarding et accueil.

## Vérification

- `pnpm app:test`, puis le build Android et iOS.
- Sur deux émulateurs, ou un émulateur et un téléphone :
  1. Appairer.
  2. Accorder +5 min par QR code : l'écran se débloque.
  3. Rescanner le même QR code : refusé.
  4. QR code d'un parent non appairé : refusé.
  5. Retirer le parent : ses codes sont refusés.
