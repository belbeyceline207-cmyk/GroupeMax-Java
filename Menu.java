import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Menu {
    Arene a;
    Joueur joueur1;
    Joueur joueur2;
    Scanner scanner = new Scanner(System.in);
    int passeConsecutif = 0; // Compteur pour suivre les passes consécutives
    String nomFichierSauvegarde = null; // Nom du fichier où on sauvegarde la partie
    boolean sauvegardeActive = false; // Indique si la sauvegarde est activée ou pas

    // Affiche le menu principal avec les choix possibles
    public void afficherMenuPrincipal() {
        System.out.println(" Menu Principal ");
        System.out.println("1. Nouvelle partie");
        System.out.println("2. Charger la dernière sauvegarde");
        System.out.println("3. Quitter");
        System.out.print("Votre choix : ");
    }

    // Lance la boucle principale du menu pour gérer les choix utilisateur
    public void lancer() throws IOException {
        while (true) {
            afficherMenuPrincipal();
            int choix = scanner.nextInt();
            scanner.nextLine(); // Pour consommer le retour à la ligne après nextInt()

            if (choix == 1) {
                nouvellePartie();
            } else if (choix == 2) {
                chargerPartie();
            } else if (choix == 3) {
                System.out.println("Au revoir !");
                break;
            } else {
                System.out.println("Choix invalide !");
            }
        }
    }

    // Demande quel type de joueur on veut : Humain, IA simple ou IA avancée
    public String demanderRole(String joueur) {
        System.out.print(joueur + " : Humain(H) , IA simple (IAs) , IA Avancée (IAa)? ");
        String reponse = scanner.nextLine();

        if (reponse.equals("H") || reponse.equals("IAs") || reponse.equals("IAa")) {
            return reponse;
        } else {
            System.out.println("Réponse invalide. Veuillez répondre par 'H' ou 'IAs3' ou 'IAa'.");
            return demanderRole(joueur); // Je rappelle la méthode pour forcer une bonne réponse
        }
    }

    // Commence une nouvelle partie, demande si on veut sauvegarder et la taille du
    // plateau
    public void nouvellePartie() throws IOException {
        System.out.print("Souhaitez-vous sauvegarder la partie ? (o/n) : ");
        String reponse = scanner.nextLine();

        if (reponse.equalsIgnoreCase("o")) {
            sauvegardeActive = true;
            System.out.print("Entrez le nom du fichier (ex: partie.txt) : ");
            nomFichierSauvegarde = scanner.nextLine();
        } else if (reponse.equalsIgnoreCase("n")) {
            sauvegardeActive = false;
        } else {
            nouvellePartie(); // Je relance la méthode si la réponse est pas valide
        }

        System.out.print("Taille de l'arène : ");
        int taille = scanner.nextInt();
        scanner.nextLine(); // consommer retour ligne

        a = new Arene(taille);

        // On demande le type pour chaque joueur
        String type1 = demanderRole("Joueur Noir");
        joueur1 = new Joueur(type1, 1, a, this);

        String type2 = demanderRole("Joueur Blanc");
        joueur2 = new Joueur(type2, 2, a, this);

        boucleDeJeu(); // On lance la boucle de jeu après initialisation
    }

    // Retourne l'adversaire d'un joueur donné
    public Joueur adversaire(Joueur joueur) {
        if (joueur == joueur1) {
            return joueur2;
        } else {
            return joueur1;
        }
    }

    // La boucle principale du jeu, elle tourne tant que le plateau n'est pas plein
    // et pas 2 passes consécutives
    public void boucleDeJeu() throws IOException {
        Joueur joueurActuel = joueur1;

        while (!a.estPlein() && passeConsecutif < 2) {
            a.afficherPlateau();
            System.out.println("Tour du joueur " + joueurActuel.joueurCouleur(joueurActuel.couleur));
            joueurActuel.calculeCoup();

            // Sauvegarde automatique après chaque coup si activée
            if (sauvegardeActive) {
                sauvegarderPartie(joueurActuel, joueurActuel.coup);
            }

            joueurActuel = adversaire(joueurActuel); // Passe au joueur suivant
        }

        a.afficherPlateau();
        System.out.println("Fin de la partie !");
        afficherResultatsFinaux(); // Affiche le résultat final
    }

    // Affiche les scores finaux et annonce le gagnant ou égalité
    public void afficherResultatsFinaux() {
        double scoreNoir = a.calculerScoreGroupesVivants(joueur1.couleur);
        double scoreBlanc = a.calculerScoreGroupesVivants(joueur2.couleur);

        System.out.println("Score final Joueur Noir : " + scoreNoir);
        System.out.println("Score final Joueur Blanc : " + scoreBlanc);

        if (scoreNoir > scoreBlanc) {
            System.out.println("Le joueur Noir gagne !");
        } else if (scoreBlanc > scoreNoir) {
            System.out.println("Le joueur Blanc gagne !");
        } else {
            System.out.println("Match nul !");
        }
    }

    // Sauvegarde l'état de la partie dans un fichier texte
    public void sauvegarderPartie(Joueur joueur, int[] coup) throws IOException {
        if (!sauvegardeActive || nomFichierSauvegarde == null) {
            return; // Sauvegarde non activée
        }
        // J'ouvre en mode append pour ajouter à la fin du fichier sans écraser

        BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichierSauvegarde, true));
        // J'ouvre le fichier en mode écriture (true = ajout à la fin du fichier, sans
        // écraser l'ancien contenu)

        // J'écris le coup joué, ou PASS si le joueur a passé son tour

        if (!joueur.pass) {
            writer.write("COUP " + joueur.couleur + " " + coup[0] + " " + coup[1]);
        } else {
            writer.write("PASS " + joueur.couleur);
        }
        writer.newLine();
        // J'écris la taille de l'arène pour pouvoir recharger

        writer.write(String.valueOf(a.taille));
        writer.newLine();
        // J'écris le plateau ligne par ligne
        for (int i = 0; i < a.taille; i++) {
            for (int j = 0; j < a.taille; j++) {
                writer.write(String.valueOf(a.plateau[i][j]));
                if (j != a.taille - 1) {
                    writer.write(" ");
                }
            }
            writer.newLine();
        }

        writer.close();
        System.out.println("Sauvegarde effectuée !");
    }

    // cette méthode permet de charger une ancienne partie depuis un fichier texte.
    // elle lit le fichier, récupère le dernier plateau enregistré et relance la
    // partie avec.
    public void chargerPartie() throws IOException {
        // je demande à l'utilisateur de saisir le nom du fichier qu'il veut charger
        System.out.print("Nom du fichier à charger : ");
        String nomFichier = scanner.nextLine(); // Je récupère ce que l'utilisateur tape

        // j'ouvre le fichier en lecture pour pouvoir le parcourir ligne par ligne
        BufferedReader reader = new BufferedReader(new FileReader(nomFichier));

        // je prépare les variables qui vont me servir à récupérer les infos du fichier
        String ligne;
        int taille = 0; // pour la taille du plateau
        int[][] dernierPlateau = null; // pour stocker le dernier plateau trouvé

        // je parcours toutes les lignes du fichier une par une
        while ((ligne = reader.readLine()) != null) {
            // je saute les lignes vides (parfois il y a des sauts de ligne inutiles)
            if (ligne.trim().isEmpty()) {
                continue;
            }

            // j'ignore les lignes qui correspondent aux coups ou aux passes "COUP" et
            // "PASS"
            if (ligne.startsWith("COUP") || ligne.startsWith("PASS")) {
                continue;
            }

            // si la ligne contient uniquement des chiffres, c'est sûrement la taille du
            // plateau
            if (ligne.trim().matches("\\d+")) {
                // Je convertis cette ligne en entier pour avoir la taille
                taille = Integer.parseInt(ligne.trim());

                // Je crée un tableau temporaire pour y mettre les cases du plateau
                int[][] plateauTemp = new int[taille][taille];

                // je lis les lignes suivantes pour remplir chaque ligne du plateau
                for (int i = 0; i < taille; i++) {
                    String plateauLigne = reader.readLine(); // Je lis une ligne du plateau

                    // Si la ligne est vide ou qu'il n'y a plus rien à lire, j'arrête
                    if (plateauLigne == null || plateauLigne.trim().isEmpty()) {
                        break;
                    }

                    // Je découpe la ligne en plusieurs valeurs grâce aux espaces
                    String[] valeurs = plateauLigne.trim().split(" ");

                    // Et je convertis chaque valeur en entier pour remplir mon tableau
                    for (int j = 0; j < taille; j++) {
                        plateauTemp[i][j] = Integer.parseInt(valeurs[j]);
                    }
                }

                // Je garde ce tableau comme étant le dernier plateau valide trouvé
                dernierPlateau = plateauTemp;
            }
        }

        // Une fois la lecture finie, je ferme le fichier
        reader.close();

        // Si j'ai bien trouvé un plateau valide
        if (dernierPlateau != null) {
            // Je crée une nouvelle arène si elle n'existait pas encore
            if (a == null) {
                a = new Arene(taille);
            }

            // Je mets à jour l'arène avec le plateau que j'ai chargé
            a.plateau = dernierPlateau;

            // Je montre le plateau chargé à l'utilisateur
            System.out.println("Dernier plateau chargé :");
            a.afficherPlateau();

            // Je redemande à l'utilisateur quel type de joueur il veut (IA ou humain)
            String type1 = demanderRole("Joueur Noir");
            joueur1 = new Joueur(type1, 1, a, this);

            String type2 = demanderRole("Joueur Blanc");
            joueur2 = new Joueur(type2, 2, a, this);

            // Et je relance la partie avec ces joueurs
            boucleDeJeu();
        } else {
            // Si aucun plateau n'a été trouvé, je signale une erreur
            System.out.println("Erreur : aucun plateau chargé.");
        }
    }

    // Point d'entrée du programme, lance le menu principal
    public static void main(String[] args) throws IOException {
        Menu menu = new Menu();
        menu.lancer();
    }
}
