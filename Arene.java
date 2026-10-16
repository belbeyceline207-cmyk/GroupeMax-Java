public class Arene {
    int[][] plateau;
    int N = 1; // joueur 1
    int B = 2; // joueur 2
    int taille;

    // constructeur : crée un plateau vide taille x taille
    public Arene(int taille) {
        this.taille = taille;
        this.plateau = new int[taille][taille];
    }

    // affiche le plateau dans la console (valeurs 0,1,2)
    public void afficheTableau() {
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                System.out.print(plateau[i][j] + " ");
            }
            System.out.println();
        }
    }

    // affiche le plateau graphiquement (avec des cases et des pions)
    public void afficherPlateau() {
        int tailleCase = 100;

        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                int x = j * tailleCase;
                int y = i * tailleCase;

                // case de fond en gris clair
                Rectangle fond = new Rectangle(x, y, tailleCase, tailleCase);
                fond.setColor(Color.LIGHT_GRAY);
                fond.fill();

                // elle dessine un pion noir ou blanc selon joueur
                if (plateau[i][j] == 1 || plateau[i][j] == 2) {
                    int marge = 20;
                    int taillePion = tailleCase - 2 * marge; // pion plus petit que la case
                    Ellipse pion = new Ellipse(x + marge, y + marge, taillePion, taillePion);
                    if (plateau[i][j] == 1) {
                        pion.setColor(Color.BLACK);
                    } else {
                        pion.setColor(Color.WHITE);
                    }
                    pion.fill();
                }

                // dessine la grille (contour des cases)
                Rectangle contour = new Rectangle(x, y, tailleCase, tailleCase);
                contour.setColor(Color.BLACK);
                contour.draw();
            }
        }

        numeroColonnes(); // affiche num colonnes
        numeroLignes(); // affiche num lignes
    }

    // affiche les numéros des colonnes en bas
    public void numeroColonnes() {
        int tailleCase = 100; // Je définis la taille d'une case (en pixels)

        for (int j = 0; j < taille; j++) {
            int x = j * tailleCase + 40; // Je calcule la position horizontale (x) du numéro de colonne
            int y = taille * tailleCase + 10; // Je place les numéros en dessous du plateau (position verticale y)

            new Text(x, y, "" + j).draw(); // J'affiche le numéro de colonne (j) à la position (x, y)
        }
    }

    // affiche les numéros des lignes à droite
    public void numeroLignes() {
        int tailleCase = 100; // Je définis la taille d'une case (en pixels)

        for (int i = 0; i < taille; i++) {
            int x = taille * tailleCase + 10; // Je place les numéros à droite du plateau (position horizontale x)
            int y = i * tailleCase + 60; // Je calcule la position verticale (y) du numéro de ligne

            new Text(x, y, "" + i).draw(); // J'affiche le numéro de ligne (i) à la position (x, y)
        }
    }

    // regarde si on est hors limites (en dehors du plateau)
    public boolean limiteAtteinte(int x, int y) {
        return !(x >= 0 && y >= 0 && x < taille && y < taille);
    }

    // case libre = dans limites + case vide (0)
    public boolean caseLibre(int x, int y) {
        return (!limiteAtteinte(x, y) && plateau[x][y] == 0);
    }

    // joue un coup si c'est légal (remplit case par numéro joueur)
    public void jouerCoup(int x, int y, int joueur) {
        if (estLegal(x, y, joueur)) {
            plateau[x][y] = joueur;
        }
    }

    // retourne le numéro de l'adversaire
    public int adversaire(int joueur) {
        if (joueur == N) {
            return B;
        } else {
            return N;
        }
    }

    // vérifie si un coup est légal : case libre + pas dans zone réservée
    public boolean estLegal(int x, int y, int joueur) {
        boolean[][] visite = new boolean[taille][taille];
        int[] compteur = new int[] { 0 };

        return caseLibre(x, y) && !(estZoneReservee(x, y, joueur, visite, compteur));
    }

    // vérifie si la case est dans une zone réservée à l'adversaire
    public boolean estZoneReservee(int x, int y, int joueur, boolean[][] visite, int[] compteur) {
        if (limiteAtteinte(x, y) || visite[x][y]) {
            return true; // hors limites ou déjà visité -> zone bloquée
        }
        visite[x][y] = true;
        if (plateau[x][y] == joueur) {
            return false; // case du joueur = pas réservée
        }

        if (plateau[x][y] == adversaire(joueur)) {
            return true; // case adverse = zone réservée
        }

        if (plateau[x][y] == 0) {
            compteur[0]++;
            if (compteur[0] >= 10) {
                return false; // assez de cases libres = pas zone réservée
            }

            // regarde autour dans les 4 directions
            return estZoneReservee(x + 1, y, joueur, visite, compteur)
                    && estZoneReservee(x - 1, y, joueur, visite, compteur)
                    && estZoneReservee(x, y + 1, joueur, visite, compteur)
                    && estZoneReservee(x, y - 1, joueur, visite, compteur);
        }
        return true;
    }

    // regarde si le plateau est plein
    public boolean estPlein() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (plateau[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // compte le nombre de cases occupées par le joueur
    public int compterCases(int joueur) {
        int cmp = 0;
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (plateau[i][j] == joueur) {
                    cmp++;
                }
            }
        }
        return cmp;
    }

    // compte la taille d'un groupe connecté du joueur à partir d'une case
    private int compterTailleGroupe(int x, int y, int joueur, boolean[][] visite) {
        if (limiteAtteinte(x, y))
            return 0;
        if (visite[x][y])
            return 0;
        if (plateau[x][y] != joueur)
            return 0;

        visite[x][y] = true;
        int taille = 1;
        // regarde autour dans les 4 directions
        taille += compterTailleGroupe(x + 1, y, joueur, visite);// droite
        taille += compterTailleGroupe(x - 1, y, joueur, visite);// gauche
        taille += compterTailleGroupe(x, y + 1, joueur, visite);// haut
        taille += compterTailleGroupe(x, y - 1, joueur, visite);// bas

        return taille;
    }

    // calcule le score total des groupes vivants (taille >= 10) du joueur
    public double calculerScoreGroupesVivants(int joueur) {
        boolean[][] visite = new boolean[taille][taille];
        double scoreTotal = 0;

        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (!visite[i][j] && plateau[i][j] == joueur) {
                    int tailleGroupe = compterTailleGroupe(i, j, joueur, visite);
                    if (tailleGroupe >= 10) {
                        scoreTotal += tailleGroupe;
                    }
                }
            }
        }
        if (joueur == B) {
            return scoreTotal = scoreTotal + 0.5; // blanc gagne un demi-point de bonus
        }
        return scoreTotal;
    }

    // regarde s'il reste au moins une case libre où le joueur peut jouer
    public boolean encoreCaseLibre(int joueur) {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (estLegal(i, j, joueur)) {
                    return true;
                }
            }
        }
        return false;
    }

}
