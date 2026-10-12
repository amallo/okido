# Le compagnon qui s'endort

> **Statut : en partie implémenté.** Le compagnon et son sable sont dans l'app (`ui/components/Companion.kt`). Le texte du compagnon et les rêves restent des idées.

Un petit compagnon se remplit de sable, donc de sommeil, au fil du temps d'écran. Il **s'endort paisiblement** quand le temps est écoulé, et il est réveillé quand la tablette a été posée.

Son humeur ne dépend jamais du temps d'écran : il est toujours serein. Seule son activité change, entre éveillé et endormi.

## Le besoin

Aujourd'hui, la fin du temps est une coupure sèche : l'écran « Temps écoulé ».

On cherche plutôt à **donner envie** de se passer de l'écran, avec quatre contraintes :
- aucun contenu à tenir à jour ;
- tout fonctionne hors ligne ;
- aucune pub, aucun sponsor ;
- aucun temps d'écran offert en récompense.

## L'idée

- **Éveillé** : à l'accueil, quand il reste du temps. Il est calme, les yeux ouverts et la bouche neutre, et son sable monte petit à petit. Il ne sourit pas : l'écran ne doit pas être ce qui le rend content.
- **Endormi** : à l'écran « Temps écoulé ». Il dort paisiblement, avec un petit sourire : « C'est l'heure de ma sieste. Et toi, tu fais quoi ? »
- **Au retour**, après une pause, il est de nouveau éveillé.

L'enfant n'est pas privé d'écran, et il n'a pas épuisé son compagnon : c'est simplement l'heure de sa sieste.

**Option : les rêves.** Au retour, le compagnon raconte un rêve en une phrase, avec un emoji : « J'ai rêvé d'une 🪶 plume. Et toi, tu en trouves une ? » Ça renvoie l'enfant vers le monde réel, sans aucun dessin à produire.

## Forme

Le « O » du logo, avec un visage. Il n'a ni bras ni jambes.

- **Le corps** : l'anneau indigo `#5856D6`, légèrement resserré au milieu comme un sablier.
- **Le visage** : deux yeux et une bouche. Éveillé, il a les yeux ronds et la bouche neutre. Endormi, il a les yeux fermés et un petit sourire.
- **Le sable orange `#FF9500`** : c'est le sommeil, comme celui du marchand de sable. Le compagnon fait office de sablier :
  - au début de la session, il n'y a qu'une fine couche au fond de l'anneau ;
  - le sable monte avec la part du temps accordé déjà utilisée ;
  - à la limite, que ce soit 5 min ou 1 h, l'anneau est rempli presque jusqu'aux yeux.
- **Le temps restant** reste affiché en minutes à côté, pour le parent et les enfants qui savent lire l'heure.
- **Si un parent rallonge une session en cours**, le début ne change pas : le sable redescend un peu.

On a écarté la forme sablier complète. Elle est parlante (le repos, c'est retourner le sablier), mais on y place mal le visage. Le « O » garde le sablier à l'intérieur, avec le sable, sans cesser d'être un petit être.

## Version minimale

- Un seul dessin, le « O » du logo, avec deux visages : éveillé et endormi.
- Des animations lentes et discrètes : il respire et cligne des yeux quand il est éveillé ; il respire lentement et laisse monter des « z » quand il dort. Il ne bâille pas et ne s'affaisse pas. Rien qui appelle l'enfant, aucune réaction au toucher.
- Une liste d'emojis avec une phrase chacun, pour les rêves.
- Pas de décor, pas de paliers de croissance.

## Règles

- **C'est le compagnon de l'appareil, pas celui d'un enfant.** Sur une tablette partagée, les frères et sœurs coopèrent au lieu de rivaliser, sans profils à gérer.
- **On ne peut pas interagir avec lui.** On ne le nourrit pas, on ne le caresse pas, il n'y a pas de mini-jeu. Sinon il deviendrait « mon ami dans la tablette », une nouvelle raison d'allumer l'écran.
- **Il ne meurt jamais** et ne culpabilise jamais l'enfant.
- **Son humeur ne dépend jamais du temps d'écran** : il est toujours serein, il est seulement éveillé ou endormi.
- **La récompense n'est jamais du temps d'écran.**
- **Pas de notification** pour réclamer l'enfant.
- **Tout reste sur l'appareil.**

## Pistes écartées pour l'instant

On les écarte parce qu'elles sont trop complexes pour démarrer :
- le compagnon qui part explorer dehors et rapporte des souvenirs ;
- une fenêtre, une pièce, une étagère à souvenirs ;
- des paliers de croissance, des accessoires, des saisons ;
- plusieurs compagnons à adopter, une piste pour Okido+ ;
- un profil par enfant.

## Idées voisines discutées

- **Rituel de fin** : « Qu'est-ce que tu vas faire maintenant ? », avec 4 grosses icônes (dehors, jouer, dessiner, lire). L'enfant choisit lui-même.
- **Cartes du foyer** : le parent écrit ses propres idées (« les Kapla », « vélo avec papa »). C'est hors ligne par nature.
- **Suggestions de lecture « Connais-tu… ? »** : l'enfant va en parler à ses parents.
  - Difficile à tenir à jour sur une app hors ligne.
  - Toujours **non sponsorisé**, avec des liens neutres.
- **LLM local** : seulement côté parent, pour générer des cartes que le parent relit. Jamais en direct avec l'enfant, et jamais pour inventer des livres.
