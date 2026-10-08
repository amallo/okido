# Le compagnon qui se repose

> **Statut : en partie implémenté.** Le compagnon et son sable sont dans l'app (`ui/components/Companion.kt`). Le texte du compagnon et les rêves restent des idées.

Un petit compagnon se remplit de sable, donc de sommeil, au fil du temps d'écran. Il est **fatigué** quand le temps est écoulé. Il retrouve ses forces quand la tablette est posée.

## Le besoin

Aujourd'hui, la fin du temps est une coupure sèche : l'écran « Temps écoulé ».

On cherche plutôt à **donner envie** de se passer de l'écran, avec quatre contraintes :
- aucun contenu à tenir à jour ;
- tout fonctionne hors ligne ;
- aucune pub, aucun sponsor ;
- aucun temps d'écran offert en récompense.

## L'idée

- **Éveillé** : à l'accueil, quand il reste du temps. Il a les yeux ouverts et sourit, et son sable monte petit à petit.
- **Fatigué** : à l'écran « Temps écoulé ». Il bâille : « Je vais me reposer. Et toi, tu fais quoi ? »
- **Au retour**, après une pause, il est de nouveau reposé.

L'enfant n'est pas privé d'écran : il laisse son compagnon se reposer.

**Option : les rêves.** Au retour, le compagnon raconte un rêve en une phrase, avec un emoji : « J'ai rêvé d'une 🪶 plume. Et toi, tu en trouves une ? » Ça renvoie l'enfant vers le monde réel, sans aucun dessin à produire.

## Forme

Le « O » du logo, avec un visage. Il n'a ni bras ni jambes.

- **Le corps** : l'anneau indigo `#5856D6`.
- **Le visage** : deux yeux et une bouche. Éveillé, il a les yeux ronds et sourit. Fatigué, il a les paupières mi-closes et bâille.
- **Le sable orange `#FF9500`** : c'est le sommeil, comme celui du marchand de sable. Le compagnon fait office de sablier :
  - au début de la session, il n'y a qu'une fine couche au fond de l'anneau ;
  - le sable monte avec la part du temps accordé déjà utilisée ;
  - à la limite, que ce soit 5 min ou 1 h, l'anneau est rempli presque jusqu'aux yeux.
- **Le temps restant** reste affiché en minutes à côté, pour le parent et les enfants qui savent lire l'heure.
- **Si un parent rallonge une session en cours**, le début ne change pas : le sable redescend un peu.

On a écarté la forme sablier complète. Elle est parlante (le repos, c'est retourner le sablier), mais on y place mal le visage. Le « O » garde le sablier à l'intérieur, avec le sable, sans cesser d'être un petit être.

## Version minimale

- Un seul dessin, le « O » du logo, avec deux visages : éveillé et fatigué.
- Des animations lentes et discrètes : il respire et cligne des yeux quand il est éveillé ; il s'affaisse, bâille et laisse monter des « z » quand il est fatigué. Rien qui appelle l'enfant, aucune réaction au toucher.
- Une liste d'emojis avec une phrase chacun, pour les rêves.
- Pas de décor, pas de paliers de croissance.

## Règles

- **C'est le compagnon de l'appareil, pas celui d'un enfant.** Sur une tablette partagée, les frères et sœurs coopèrent au lieu de rivaliser, sans profils à gérer.
- **On ne peut pas interagir avec lui.** On ne le nourrit pas, on ne le caresse pas, il n'y a pas de mini-jeu. Sinon il deviendrait « mon ami dans la tablette », une nouvelle raison d'allumer l'écran.
- **Il ne meurt jamais** et ne culpabilise jamais l'enfant. Au pire, il est fatigué.
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
