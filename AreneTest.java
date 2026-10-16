public class AreneTest {

    private Arene a;

    // Constructeur qui crée une arène 5x5
    public AreneTest() {
        a = new Arene(5);
    }

    // Test la création du plateau : il doit être vide au début
    public void testInitialisation() {
        System.out.println("Test initialisation plateau (doit être vide) :");
        a.afficheTableau();
        System.out.println();
    }

    // Test des méthodes caseLibre() et limiteAtteinte()
    public void testCaseLibreEtLimiteAtteinte() {
        System.out.println("Test caseLibre et limiteAtteinte :");
        System.out.println("caseLibre(0,0) = " + a.caseLibre(0, 0)); // doit être vrai car vide
        System.out.println("caseLibre(-1,0) = " + a.caseLibre(-1, 0)); // faux car hors plateau
        System.out.println("limiteAtteinte(-1,0) = " + a.limiteAtteinte(-1, 0)); // vrai car hors limites
        System.out.println("limiteAtteinte(4,4) = " + a.limiteAtteinte(4, 4)); // faux car dans limites
        System.out.println();
    }

    // Test jouerCoup() avec quelques coups et test de estLegal()
    public void testJouerCoupEtEstLegal() {
        System.out.println("Test jouerCoup et estLegal :");
        a.jouerCoup(0, 0, 1); // case libre, coup joué
        a.jouerCoup(1, 1, 2); // case libre, coup joué
        a.jouerCoup(5, 5, 1); // hors limites, doit être ignoré
        a.afficheTableau();

        System.out.println("estLegal(0,0,1) = " + a.estLegal(0, 0, 1)); // false, case déjà prise
        System.out.println("estLegal(2,2,1) = " + a.estLegal(2, 2, 1)); // true, case libre
        System.out.println();
    }

    // Test de la méthode adversaire()
    public void testAdversaire() {
        System.out.println("Test adversaire :");
        System.out.println("Adversaire de 1 = " + a.adversaire(1)); // doit être 2
        System.out.println("Adversaire de 2 = " + a.adversaire(2)); // doit être 1
        System.out.println();
    }

    // Test estPlein() avant et après remplissage complet du plateau
    public void testEstPlein() {
        System.out.println("Test estPlein :");
        System.out.println("Plateau plein ? " + a.estPlein()); // false au début
        // On remplit tout le plateau avec des 1
        for (int i = 0; i < a.taille; i++) {
            for (int j = 0; j < a.taille; j++) {
                a.plateau[i][j] = 1;
            }
        }
        a.afficheTableau();
        System.out.println("Plateau plein après remplissage ? " + a.estPlein()); // true maintenant
        System.out.println();
    }

    // Test compterCases() pour chaque joueur
    public void testCompterCases() {
        System.out.println("Test compterCases :");
        a.afficheTableau();
        System.out.println("Cases joueur 1 : " + a.compterCases(1)); // nombre de cases avec 1
        System.out.println("Cases joueur 2 : " + a.compterCases(2)); // nombre de cases avec 2
        System.out.println();
    }

    // Test calculerScoreGroupesVivants() sur un groupe de cases 1
    public void testCalculerScoreGroupesVivants() {
        System.out.println("Test calculerScoreGroupesVivants :");
        // Réinitialiser plateau vide
        a = new Arene(5);
        // Remplir une grande zone avec des 1 (un gros groupe)
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                a.plateau[i][j] = 1;
            }
        }
        a.afficheTableau();
        System.out.println("Score groupes vivants joueur 1 : " + a.calculerScoreGroupesVivants(1));
        System.out.println();
    }

    // Test encoreCaseLibre() dans différents cas
    public void testEncoreCaseLibre() {
        System.out.println("Test encoreCaseLibre :");
        System.out.println("Encore case libre joueur 1 ? " + a.encoreCaseLibre(1));
        // Remplir tout sauf une case
        a = new Arene(3);
        for (int i = 0; i < a.taille; i++) {
            for (int j = 0; j < a.taille; j++) {
                a.plateau[i][j] = 1;
            }
        }
        a.afficheTableau();
        a.plateau[1][1] = 0; // on libère une case
        a.afficheTableau();
        System.out.println("Encore case libre joueur 1 ? " + a.encoreCaseLibre(1));
        a.plateau[1][1] = 1; // on remet la case prise
        a.afficheTableau();
        System.out.println("Encore case libre joueur 1 après coup ? " + a.encoreCaseLibre(1));
        System.out.println();
    }

    public static void main(String[] args) {
        AreneTest test = new AreneTest();

        test.testInitialisation();
        test.testCaseLibreEtLimiteAtteinte();
        test.testJouerCoupEtEstLegal();
        test.testAdversaire();
        test.testEstPlein();
        test.testCompterCases();
        test.testCalculerScoreGroupesVivants();
        test.testEncoreCaseLibre();
    }
}
