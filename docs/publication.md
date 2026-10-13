# Publier une version d'Okido

Okido est publié sur Google Play et sur F-Droid. Les deux partent du même tag git.

## Une fois pour toutes

**Clé de signature dans le trousseau macOS** (service `okido-release`). Le build de release les lit ; les builds debug n'y touchent pas.

```
security add-generic-password -s okido-release -a OKIDO_KEYSTORE -w "$(base64 -i ~/okido-upload.jks)"
security add-generic-password -s okido-release -a OKIDO_KEYSTORE_PASSWORD -w
security add-generic-password -s okido-release -a OKIDO_KEY_PASSWORD -w
```

L'alias de la clé reste dans `app/local.properties` (`OKIDO_KEY_ALIAS`). Garder une copie du `.jks` hors de cet ordinateur.

**Vérification des développeurs Android.** Depuis 2026, un appareil Android certifié n'installe une app hors Play que si son nom de paquet et sa clé de signature sont enregistrés chez Google. Dans la Play Console, enregistrer `com.babatunde.okido` avec la clé qui signe l'APK publié sur F-Droid (la clé d'upload). La clé de signature de Play y figure déjà.

## À chaque version

1. Dans `app/version.properties`, augmenter `versionName` et `versionCode`.
2. Écrire le changelog dans `fastlane/metadata/android/fr-FR/changelogs/<versionCode>.txt` et `en-US/` (500 caractères au plus).
3. Committer, puis taguer : `git tag vX.Y.Z && git push && git push --tags`.
4. Construire l'APK signé : `./gradlew :androidApp:assembleRelease`. Le keystore est décodé dans `androidApp/build/signing/`, puis supprimé à la fin du build.
5. Publier l'APK sur GitHub, sous le nom attendu par F-Droid :
   ```
   cp androidApp/build/outputs/apk/release/androidApp-release.apk okido-X.Y.Z.apk
   gh release create vX.Y.Z okido-X.Y.Z.apk --notes-file ../fastlane/metadata/android/fr-FR/changelogs/<versionCode>.txt
   ```
6. Google Play : `./gradlew :androidApp:publishReleaseBundle` (piste interne).

F-Droid détecte le nouveau tag, recompile Okido et compare son APK au nôtre. S'ils sont identiques, il publie le nôtre, avec notre signature.

## Bon à savoir

- **Build reproductible.** F-Droid exige un APK identique octet pour octet. Rien dans le build ne doit dépendre de la machine, de la date ou du dossier. Pour vérifier, construire `assembleRelease` dans deux copies du projet et comparer les APK avec `cmp`.
- **Deux signatures.** Play re-signe l'app avec sa propre clé. L'Okido de Play et celui de F-Droid ne se mettent donc pas à jour l'un par-dessus l'autre : passer de l'un à l'autre demande de désinstaller.
- **La recette F-Droid** est dans `docs/fdroid/com.babatunde.okido.yml`. On la propose une seule fois, par merge request sur `gitlab.com/fdroid/fdroiddata`. Les versions suivantes sont détectées seules.
