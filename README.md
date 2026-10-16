# GroupeMax — Jeu de stratégie en Java

## Présentation

**GroupeMax** est un jeu de stratégie au tour par tour développé en Java dans le cadre d’un projet universitaire en Licence Informatique.

Le jeu se déroule sur une arène carrée composée de cases. Deux joueurs, Noir et Blanc, placent leurs pions sur le plateau en respectant les règles de placement.
L'objectif est de former des groupes de pions connectés afin d'obtenir le meilleur score possible.

## Fonctionnalités

- **Gestion du plateau** : création, affichage graphique et représentation dans la console.
- **Validation des coups** : vérification des limites, des cases occupées et des zones réservées.
- **Différents types de joueurs** :
  - Humain
  - IA simple jouant aléatoirement
  - IA avancée privilégiant les cases proches de ses propres pions
- **Calcul des scores** : comptage des groupes connectés d'au moins 10 pions.
- **Gestion des tours** : possibilité de passer son tour et détection de la fin de partie.
- **Sauvegarde et chargement** : enregistrement automatique de l'état du plateau dans un fichier texte et reprise d'une partie sauvegardée.

## Technologies utilisées

- **Langage :** Java
- **Affichage graphique :** bibliothèque de dessin utilisée pour représenter le plateau
- **Gestion des fichiers :** BufferedReader, BufferedWriter, FileReader et FileWriter
- **Entrées utilisateur :** Scanner

## Structure du projet

Classe/Rôle 
  - Arene : Gère le plateau, les coups légaux, les groupes et les scores. 
  - Joueur :  Gère les actions des joueurs humains et les stratégies des IA. 
  - Menu : Gère le menu principal, le déroulement des parties, les sauvegardes et leur chargement. 
  - AreneTest : Contient des tests manuels des principales fonctionnalités de la classe Arene. 

## Installation et exécution

### Prérequis

- Un JDK Java installé.
- La bibliothèque graphique utilisée par le projet, si elle n'est pas déjà disponible dans l'environnement de développement.

### Lancer le projet

1. Cloner le dépôt :

   ```bash
   git clone https://github.com/belbeyceline207-cmyk/GroupeMax-Java.git
   ```

2. Ouvrir le projet dans un environnement Java compatible.
3. Vérifier que la bibliothèque graphique nécessaire est configurée.
4. Exécuter la méthode `main()` de la classe `Menu`.

Le menu principal permet ensuite de démarrer une nouvelle partie, de charger une sauvegarde ou de quitter le programme.

## Déroulement d'une partie

1. Choisir de commencer une nouvelle partie.
2. Activer ou non la sauvegarde.
3. Définir la taille de l'arène.
4. Choisir le type de joueur pour Noir et Blanc.
5. Jouer à tour de rôle jusqu'à ce que le plateau soit plein ou que les deux joueurs passent consécutivement.
6. Consulter les scores finaux et le résultat de la partie.

## Tests

La classe AreneTest permet de tester manuellement plusieurs fonctionnalités :

- L'initialisation du plateau.
- Les limites et les cases libres.
- La légalité des coups.
- L'identification de l'adversaire.
- La détection d'un plateau plein.
- Le comptage des cases et le calcul des scores.
- La recherche de coups encore possibles.

## Contexte

Projet universitaire réalisé en Licence Informatique, mettant en pratique la programmation orientée objet, la récursivité, 
la gestion de tableaux à deux dimensions, les interactions utilisateur et la manipulation de fichiers en Java.

## Aperçu du jeu
Voici un aperçu de GroupeMax pendant une partie entre un joueur humain et une IA :

<img width="1342" height="689" alt="image" src="https://github.com/user-attachments/assets/5919ef44-7427-4e39-a58f-7153375bfaec" />

