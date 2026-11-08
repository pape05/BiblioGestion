package gestion;

import interfaces.Document;
import modeles.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Classe Bibliotheque - cerveau du système.
 * Gère les documents, les adhérents, les emprunts et les amendes.
 */
public class Bibliotheque {

    private List<Document>  documents;
    private List<Adherent>  adherents;
    private BaseDeDonnees   bdd;

    // Durée d'emprunt en jours
    public static final int DUREE_EMPRUNT    = 30;
    // Durée max de réservation en jours
    public static final int DUREE_RESERVATION = 3;

    // ---- Constructeur ----
    public Bibliotheque() {
        this.documents = new ArrayList<>();
        this.adherents = new ArrayList<>();
        this.bdd       = new BaseDeDonnees();
        chargerDonnees();
    }

    public BaseDeDonnees getBaseDeDonnees() { return bdd; }

    // ================================================================
    //  CHARGEMENT INITIAL
    // ================================================================

    /** Charge toutes les données depuis les fichiers .txt au démarrage */
    private void chargerDonnees() {
        documents.addAll(bdd.chargerLivres());
        documents.addAll(bdd.chargerRevues());
        documents.addAll(bdd.chargerArticles());
        adherents.addAll(bdd.chargerAdherents());
        bdd.restaurerEmprunts(adherents, documents);
        System.out.println("✅ Données chargées : "
                + documents.size() + " documents, "
                + adherents.size() + " adhérents.");
    }

    /**
     * Insère les données de test uniquement si la bibliothèque est vide.
     */
    public void insererDonneesTestSiVide() {
        if (!documents.isEmpty() || !adherents.isEmpty()) {
            return;
        }

        ajouterDocument(new Livre("L'Enfant Noir",              "Camara Laye",       2500, 180, 10));
        ajouterDocument(new Livre("Les Bouts de Bois de Dieu",  "Sembène Ousmane",   3000, 320, 8));
        ajouterDocument(new Livre("Une si Longue Lettre",       "Mariama Bâ",        2000, 150, 12));
        ajouterDocument(new Livre("Le Monde s'effondre",        "Chinua Achebe",     2800, 230, 6));
        ajouterDocument(new Livre("Soundjata",                  "Djibril Niane",     1500, 120, 15));

        ajouterDocument(new Revue("La Revue Informatique", "Microsoft",       1000, "decembre-2021", 50));
        ajouterDocument(new Revue("Science & Vie",         "Mondadori",       1200, "janvier-2023",  30));
        ajouterDocument(new Revue("National Geographic",   "Disney",          1500, "mars-2023",     25));
        ajouterDocument(new Revue("Le Monde Diplomatique", "Le Monde",        800,  "fevrier-2023",  20));
        ajouterDocument(new Revue("Jeune Afrique",         "JA Media Group",  900,  "avril-2023",    40));

        ajouterDocument(new Article("Ubuntu et Linux",            "Les Linuxiens",    500, "janvier-2023",  100));
        ajouterDocument(new Article("Intelligence Artificielle",  "MIT Press",        600, "mars-2023",     80));
        ajouterDocument(new Article("Cybersécurité en Afrique",   "AfricaTech",       450, "fevrier-2023",  60));
        ajouterDocument(new Article("Java 17 Nouveautés",         "Oracle Blog",      300, "decembre-2022", 90));
        ajouterDocument(new Article("Le Cloud Computing",          "AWS Team",         400, "avril-2023",    70));

        enregistrerAdherent(new Adherent("001", "Ndiaye",   "Akawa",   "akawa@gmail.com"));
        enregistrerAdherent(new Adherent("002", "Diallo",   "Mamadou", "mamadou@gmail.com"));
        enregistrerAdherent(new Adherent("003", "Sow",      "Fatou",   "fatou@gmail.com"));
        enregistrerAdherent(new Adherent("004", "Mbaye",    "Ibrahima","ibrahima@gmail.com"));
        enregistrerAdherent(new Adherent("005", "Diouf",    "Aminata", "aminata@gmail.com"));

        System.out.println("✅ Données de test insérées.");
    }

    // ================================================================
    //  GESTION DES DOCUMENTS
    // ================================================================

    /**
     * Ajoute un document dans la bibliothèque et le sauvegarde.
     */
    public void ajouterDocument(Document doc) {
        documents.add(doc);
        if (doc instanceof Livre)   bdd.enregistrerLivre((Livre) doc);
        else if (doc instanceof Revue)   bdd.enregistrerRevue((Revue) doc);
        else if (doc instanceof Article) bdd.enregistrerArticle((Article) doc);
        System.out.println("✅ Document ajouté : " + doc.getTitre());
    }

    /**
     * Vérifie si un document est disponible à l'emprunt.
     */
    public boolean verifierDisponibilite(Document doc) {
        return doc.getNombreExemplaires() > 0;
    }

    /**
     * Affiche tous les documents de la bibliothèque.
     */
    public void afficherTousDocuments() {
        if (documents.isEmpty()) {
            System.out.println("Aucun document dans la bibliothèque.");
            return;
        }
        System.out.println("\n====== CATALOGUE (" + documents.size() + " documents) ======");
        for (Document doc : documents) {
            doc.afficherDetails();
        }
    }

    /**
     * Recherche un document par son titre (insensible à la casse).
     */
    public Document rechercherDocument(String titre) {
        for (Document doc : documents) {
            if (doc.getTitre().equalsIgnoreCase(titre)) {
                return doc;
            }
        }
        System.out.println("❌ Document introuvable : " + titre);
        return null;
    }

    /**
     * Retourne la liste complète des documents.
     */
    public List<Document> getDocuments() {
        return documents;
    }

    // ================================================================
    //  GESTION DES ADHÉRENTS
    // ================================================================

    /**
     * Enregistre un nouvel adhérent.
     */
    public boolean enregistrerAdherent(Adherent adherent) {
        for (Adherent a : adherents) {
            if (a.getMatricule().equals(adherent.getMatricule())) {
                System.out.println("Matricule deja existant : "
                        + adherent.getMatricule());
                return false;
            }
        }
        adherents.add(adherent);
        bdd.enregistrerAdherent(adherent);
        System.out.println("Adherent enregistre : "
                + adherent.getPrenom() + " " + adherent.getNom());
        return true;
    }

    /**
     * Recherche un adhérent par son matricule.
     */
    public Adherent rechercherAdherent(String matricule) {
        for (Adherent a : adherents) {
            if (a.getMatricule().equals(matricule)) {
                return a;
            }
        }
        System.out.println("❌ Adhérent introuvable : " + matricule);
        return null;
    }

    /**
     * Retourne la liste de tous les adhérents.
     */
    public List<Adherent> getAdherents() {
        return adherents;
    }

    // ================================================================
    //  GESTION DES EMPRUNTS
    // ================================================================

    /**
     * Enregistre un emprunt : date d'emprunt + date de retour prévue.
     * @return true si l'emprunt est validé
     */
    public boolean enregistrerEmprunt(Adherent adherent, Document doc) {
        if (!verifierDisponibilite(doc)) {
            System.out.println("❌ Document indisponible : " + doc.getTitre());
            return false;
        }
        boolean ok = adherent.emprunter(doc);
        if (ok) {
            LocalDate dateEmprunt = LocalDate.now();
            LocalDate dateRetour  = dateEmprunt.plusDays(DUREE_EMPRUNT);
            System.out.println("📅 Date emprunt  : " + dateEmprunt);
            System.out.println("📅 Retour prévu  : " + dateRetour);
            bdd.enregistrerEmprunt(adherent);
            bdd.mettreAJourAdherent(adherents);
        }
        return ok;
    }

    /**
     * Gère le retour d'un document avec mise à jour de la disponibilité.
     * @param abime true si le document est rendu abîmé
     */
    public void gererRetour(Adherent adherent, Document doc,
                             boolean abime, boolean retard) {
        boolean ok = adherent.rendreDocument(doc);
        if (ok) {
            // Vérifier état et appliquer amende si nécessaire
            if (abime || retard) {
                taxerAmende(adherent, doc, abime, retard);
            }
            // Classer le document rendu (mise à jour fichier)
            classerDocumentRendu(doc);
            bdd.mettreAJourEmprunts(adherents);
            bdd.mettreAJourAdherent(adherents);
        }
    }

    /**
     * Classe un document rendu et met à jour les fichiers documents.
     */
    public void classerDocumentRendu(Document doc) {
        mettreAJourFichiersDocuments();
        System.out.println("📚 Document classé et mis à jour : " + doc.getTitre());
    }

    /**
     * Réécrit tous les fichiers documents avec les données actuelles en mémoire.
     */
    private void mettreAJourFichiersDocuments() {
        List<Livre> livres = new ArrayList<>();
        List<Revue> revues = new ArrayList<>();
        List<Article> articles = new ArrayList<>();
        for (Document d : documents) {
            if (d instanceof Livre)        livres.add((Livre) d);
            else if (d instanceof Revue)   revues.add((Revue) d);
            else if (d instanceof Article) articles.add((Article) d);
        }
        bdd.mettreAJourLivres(livres);
        bdd.mettreAJourRevues(revues);
        bdd.mettreAJourArticles(articles);
    }

    /**
     * Vérifie l'état du document rendu (abîmé ou non).
     */
    public boolean verifierEtatDocument(Document doc) {
        // Dans une vraie appli, l'agent saisit l'état manuellement via l'IHM
        System.out.println("🔍 Vérification de l'état : " + doc.getTitre());
        return true; // géré via l'IHM
    }

    // ================================================================
    //  GESTION DES AMENDES
    // ================================================================

    /**
     * Calcule et applique une amende à un adhérent.
     * - Retard  : 1/4 du prix du document
     * - Abîmé  : prix total du document
     */
    public void taxerAmende(Adherent adherent, Document doc,
                             boolean abime, boolean retard) {
        double montant = 0;
        String nature  = "";

        if (abime) {
            montant = doc.getPrix();
            nature  = "abime";
            System.out.println("⚠️ Document abîmé ! Amende : "
                    + montant + " FCFA");
        } else if (retard) {
            montant = doc.getPrix() / 4;
            nature  = "retard";
            System.out.println("⏰ Retard détecté ! Amende : "
                    + montant + " FCFA");
        }

        if (montant > 0) {
            adherent.setSoldeAmendes(
                    adherent.getSoldeAmendes() + montant);
            bdd.enregistrerTaxe(nature, montant,
                    adherent.getMatricule(), "impayé");
        }
    }

    /**
     * Enregistre le paiement d'une amende.
     */
    public void enregistrerAmendePaye(Adherent adherent, double montant) {
        adherent.payerTaxe(montant);
        bdd.mettreAJourTaxe(adherent.getMatricule(), "impayé", "payé");
        bdd.mettreAJourAdherent(adherents);
        System.out.println("💰 Amende payée pour : "
                + adherent.getPrenom() + " " + adherent.getNom());
    }

    // ================================================================
    //  RÉSERVATION
    // ================================================================

    /**
     * Réserve un document pour un adhérent (max 3 jours).
     */
    public boolean reserverDocument(Adherent adherent, Document doc) {
        if (!verifierDisponibilite(doc)) {
            System.out.println("❌ Document indisponible, réservation impossible : "
                    + doc.getTitre());
            return false;
        }
        boolean ok = adherent.reserverDocument(doc);
        if (ok) {
            doc.setNombreExemplaires(doc.getNombreExemplaires() - 1);
            LocalDate dateReservation = LocalDate.now();
            LocalDate expiration = dateReservation.plusDays(DUREE_RESERVATION);
            System.out.println("📅 Réservation expire le : " + expiration);
            mettreAJourFichiersDocuments();
        }
        return ok;
    }
}