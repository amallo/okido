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
| L'enfant sait que le contrôle est actif (ICO 11) | ✅ | Le compagnon et les minutes restantes sont visibles à l'accueil, puis l'écran « Temps écoulé ». |
| Réglages protecteurs par défaut (ICO 7) | ✅ | Passer l'étape des apps à l'onboarding laisse toutes les apps bloquées. |
| Pas de récompense aléatoire, pas de notification | ✅ | Le compagnon ne réagit à rien et ne réclame jamais l'enfant (voir `docs/idees/compagnon.md`). |
| La récompense n'est jamais du temps d'écran | ✅ | C'est une règle produit. |
| Pas de pub, pas d'abonnement | ✅ (principe) | Okido+ sera un achat unique. Pas encore implémenté. |
| Engagements publiés sous forme courte (CNIL 6) | ⚠️ À faire | Une liste de quelques lignes, dans la fiche du store et dans l'app. |
| Okido expliqué à l'enfant avec ses mots (ICO 11, CNIL 5) | ⚠️ À faire | Un écran court qui dit pourquoi Okido est là et ce qu'il fait, sans rien cacher. |
| Diagnostic attentionnel complet | ⚠️ À faire | Passer chaque écran au crible de la méthode Designers Éthiques, une fois l'onboarding terminé. |

## Règles pour les futures features

- Aucune donnée ne quitte l'appareil, et l'app ne demande pas la permission `INTERNET`.
- Pas d'historique de l'activité de l'enfant.
- L'enfant sait toujours qu'Okido est actif, et pourquoi.
- Pas de récompense aléatoire, de série à tenir, de compte à rebours anxiogène, ni de notification qui rappelle l'enfant.
- Aucun personnage ne montre de joie liée à l'écran, ni de tristesse ou de fatigue liée à la fin du temps.
- Les réglages par défaut sont les plus protecteurs.
- Les choix du parent sont présentés honnêtement, sans culpabiliser.
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
