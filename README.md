# **TDLMaster - Votre Application de Gestion de Tâches**

Bienvenue sur le dépôt de TDLMaster, l'application Android conçue pour transformer votre gestion des tâches. TDLMaster vous permet de créer des listes de tâches hebdomadaires et de suivre vos objectifs de long terme avec la liste spéciale "Étoile du Nord".

### **Fonctionnalités**

- **Listes Hebdomadaires :** Créez et gérez des listes de tâches avec une durée par défaut d'une semaine, modifiable selon vos besoins.
- **Liste "Étoile du Nord" :** Définissez et suivez vos objectifs annuels dans une liste spéciale, séparée de vos tâches quotidiennes ou hebdomadaires.
- **Synchronisation en Temps Réel :** Vos listes et tâches sont synchronisées sur tous vos appareils, permettant une gestion fluide peu importe où vous êtes.
- **Gestes Tactiles Intuitifs :** Organisez facilement vos tâches avec des gestes de glisser-déposer, cochez les tâches complétées d'un simple toucher, et accédez aux options de tâche avec un appui long.

### **Prérequis**

- Android SDK
- Firebase account pour la synchronisation en temps réel

### **Installation**

1. Clonez ce dépôt sur votre machine locale.
2. Ouvrez le projet dans Android Studio.
3. Configurez Firebase en suivant la documentation officielle.
4. Exécutez l'application sur votre émulateur ou dispositif Android.

### **Utilisation**

- **Créer une liste :** Accédez à l'interface de création de liste depuis le menu principal.
- **Ajouter une tâche :** Dans une liste ouverte, utilisez le bouton "+" pour ajouter une nouvelle tâche.
- **Modifier/Supprimer une tâche :** Un appui long sur une tâche ouvre le menu contextuel d'options.
- **Cocher une tâche :** Touchez simplement la tâche pour la marquer comme complétée.

### **Technologies Utilisées**

- Android Studio pour le développement.
- SQLite pour la persistence locale des données.
- SharedPreferences pour stocker les préférences utilisateur.
- Firebase pour la synchronisation en temps réel et la sauvegarde sur le cloud.

### Liste des fonctionnalités implémentées :

[ ] Utilisation de l'Api Preference
[ ] Ecriture/lecture dans un Fichier, usage de InputStream ou OutputStream
[ ] Utilisation de SQLite
[ ] Utilisation de Room
[ ] Utilisation de Firebase
[ ] Nombre d'activités ou fragment supérieur ou égal à 3
[ ] Gestion du bouton Back (message pour confirmer que l'on veut réellement quitter l'application)
[ ] L'affichage d'une liste avec son adapter
[ ] L'affichage d'une liste avec un custom adapter (avec gestion d’événement)
[ ] La pertinence d'utilisation des layouts (L'application doit être responsive et supporter: portrait/paysage et tablette)
[ ] L'utilisation de d’événement améliorant l'ux (pex: swipe). Préciser :
[ ] La réalisation de composant graphique custom (Paint 2D, Calendrier,...) Préciser :
[ ] Les taches en background (codage du démarrage d'un thread)
[ ] Le codage d'un menu (contextuel ou non, utilisation d'un menu en resource XML)
[ ] L'application de pattern (Reactive programming, singleton, MVC,...) Liste :
