# app — Kotlin Multiplatform (Android + iOS)

Le code de `shared/src/commonMain` tourne sur Android et iOS (UI comprise, via Compose Multiplatform).
Le code propre à une plateforme va dans `androidMain` / `iosMain`.

## Clean architecture (packages sous `com.babatunde.okido`)
- `core` : le métier. Entités, use cases, ports (interfaces). Kotlin pur, uniquement dans `commonMain`.
- `infra` : implémentations des ports de `core`, le plus souvent par plateforme (`androidMain` / `iosMain`).
  Chaque plateforme fournit `actual val infraModule` (Koin).
- `ui` : Compose et ViewModels. Consomme les use cases de `core`.
- `di` : assemblage Koin (`initKoin()`). Seul package qui connaît tous les autres.

## Règle de dépendance
- `core` n'importe ni Compose, ni Koin, ni API de plateforme, ni `infra`, ni `ui`.
- `infra` → `core`. `ui` → `core`, jamais `infra`.
- Une API système (surveillance, blocage…) devient un port dans `core` et une implémentation dans `infra`.
  Si l'API iOS n'existe qu'en Swift, l'implémenter dans `iosApp` et l'injecter dans Koin.

## Tests
- Use cases de `core` : `commonTest`, avec des fakes pour les ports.
