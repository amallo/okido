# Logo Okido

`okido-icon.svg` : un « O » en forme de sablier, généré avec [Recraft](https://www.recraft.ai) puis converti en vectoriel.

## Couleurs
Les mêmes que dans `OkidoColors` (`OkidoTheme.kt`) :

| Rôle | Hex |
|---|---|
| Forme (indigo) | `#5856D6` |
| Fond (blanc) | `#FFFFFF` |
| Sable (orange) | `#FF9500` |

## Réglages Recraft
- Style : *Icon*, ou *Vector art* puis *Flat*.
- Palette : ajouter les 3 couleurs ci-dessus dans le sélecteur. Recraft les respecte mieux qu'écrites dans le prompt.
- Format : carré 1:1, export en SVG.

## Prompt
```
App icon for a kids screen-time app. A single bold rounded letter "O" shaped like a soft hourglass: the ring pinches gently at the middle, with a small pile of orange sand resting at the bottom inside. Indigo #5856D6 shape centered on a plain white #FFFFFF background. Flat vector, geometric, thick even strokes, rounded ends, friendly and calm, minimal, high contrast, readable at small sizes. No text, no letters other than the O, no gradients, no shadows, no lock, no eye, no clock hands.
```

Variante « cocon », qui remplace la deuxième phrase :
```
A single bold rounded letter "O" like a protective cocoon, with a small solid orange dot nestled inside near the bottom.
```

## Contraintes d'icône
- Android (adaptive icon) : le motif doit tenir dans les 66 dp centraux d'une zone de 108 dp.
- iOS : 1024×1024, sans transparence.
