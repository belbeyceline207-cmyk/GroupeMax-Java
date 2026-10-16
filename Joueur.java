import java.util.Random;
import java.util.Scanner;

public class Joueur {
    boolean pass = false;// Indique si le joueur a passé son tour
    String type; // Type du joueur : "H" pour Humain, "IAs" pour IA simple, "IAa" pour IA avancée
    int couleur;// Couleur du joueur : 1 = Noir, 2 = Blanc
    Arene arene;
    Scanner scanner = new Scanner(System.in);
    Menu menu = new Menu();
    int[] coup;// pour recuperer les coordonnees du dernier coup joué

    // Constructeur
    public Joueur(String type, int couleur, Arene arene, Menu menu) {
        this.type = type;
        this.couleur = couleur;
        this.arene = arene;
        this.menu = menu;
    }

    public String joueurCouleur(int joueur) {
        if (joueur == 1) {
            return "Noir";
        } else {
            return "Blanc";
        }
    }
    // Fonction pour le jeu du joueur Humain

    public int[] jouerCoupHumain() {
        pass = false;
        System.out.print("Voulez-vous passer votre tour ? (o/n) : ");
        String reponse = scanner.nextLine();

        if (reponse.equalsIgnoreCase("o")) {
            // Le joueur choisit de passer
            System.out.println("Vous avez passé votre tour.");
            menu.passeConsecutif++;
            pass = true;
            coup = null;
        } else if (reponse.equalsIgnoreCase("n")) {
            // Le joueur choisit de jouer
            int ligne = demanderCoordonnee("Entrez la ligne de votre prochain coup");
            int colonne = demanderCoordonnee("Entrez la colonne de votre prochain coup");

            if (arene.estLegal(ligne, colonne, couleur)) {// tester si le coup est valide
                arene.jouerCoup(ligne, colonne, couleur);// jouer le coup
                System.out.println("Coup joué !");
                menu.passeConsecutif = 0;
                pass = false;
                coup = new int[] { ligne, colonne };// recuperer les coordonnes du coup

            } else {
                System.out.println("Coup illégal, veuillez réessayer !");
                jouerCoupHumain(); // recommence si le coup est illegal
            }
        } else {
            // On redemande si la reponse n'est pas la bonne
            jouerCoupHumain();
        }
        return coup;

    }

    //// Fonction pour le jeu du joueur IA
    public int[] jouerCoupIA() {
        pass = false;
        // L'IA passe son tour automatiquement si il n'y a aucun coup legal
        if (!arene.encoreCaseLibre(couleur)) {
            System.out.println("IA passe son tour.");
            menu.passeConsecutif++;
            pass = true;
            coup = null;
        } else {
            // IA cherche un coup aléatoire jusqu'à en trouver un légal
            Random r = new Random();
            int ligne = 0;
            int colonne = 0;
            while (!arene.estLegal(ligne, colonne, couleur)) {
                ligne = r.nextInt(arene.taille);
                colonne = r.nextInt(arene.taille);
            }
            // Joue le coup choisi
            arene.jouerCoup(ligne, colonne, couleur);

            menu.passeConsecutif = 0;
            // pass=true;
            System.out.println("IA joue en (" + ligne + "," + colonne + ")");
            coup = new int[] { ligne, colonne };
        }
        return coup;
    }
    // Demande une coordonnée à l'utilisateur

    public int demanderCoordonnee(String message) {
        System.out.print(message + " : ");
        while (!scanner.hasNextInt()) {
            System.out.println("Entrée invalide, utilisez un entier.");
            System.out.print(message + " : ");
            scanner.next(); // consommer la mauvaise entrée
        }
        int coordonnee = scanner.nextInt();
        scanner.nextLine(); // Consomme retour à la ligne
        return coordonnee;
    }

    // Méthode qui appelle le bon type de stratégie selon le joueur
    public void calculeCoup() {
        if (type.equals("H")) {
            jouerCoupHumain();
        } else if (type.equals("IAs")) {
            jouerCoupIA();
        } else {
            jouerCoupIAAvance();
        }
    }

    // IA avancée : cherche un coup proche de ses propres pions
    public int[] jouerCoupIAAvance() {
        // Parcourt tout le plateau
        for (int x = 0; x < arene.taille; x++) {
            for (int y = 0; y < arene.taille; y++) {
                // Si le coup est légal et proche d’un pion du même joueur
                if (arene.estLegal(x, y, couleur) && joueProcheDePiecePropre(x, y, couleur)) {
                    arene.jouerCoup(x, y, couleur);
                    System.out.println("IA joue en (" + x + "," + y + ")");
                    menu.passeConsecutif = 0;
                    coup = new int[] { x, y };
                    return coup;
                }
            }
        }

        // Sinon on joue un coup aléatoire(IA aléatoire)
        coup = jouerCoupIA();
        return coup;
    }

    private boolean joueProcheDePiecePropre(int x, int y, int couleur) {
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int i = 0; i < directions.length; i++) {
            int nx = x + directions[i][0];
            int ny = y + directions[i][1];
            if (!arene.limiteAtteinte(nx, ny)) {
                if (arene.plateau[nx][ny] == couleur) {
                    return true;// Une case voisine est de même couleur
                }
            }
        }
        return false;// Aucune case voisine alliée
    }

}
