package modeles;

import interfaces.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Adherent - représente un membre de la bibliothèque.
 * Gère ses emprunts, réservations et amendes.
 */
public class Adherent {

    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private List<Document> documentsEmpruntes;   // max 3 documents
    private List<Document> documentsReserves;    // max 3 jours de réservation
    private double soldeAmendes;                 // montant total des amendes dues

    // ---- Constructeur ----
    public Adherent(String matricule, String nom, String prenom, String email) {
        this.matricule           = matricule;
        this.nom                 = nom;
        this.prenom              = prenom;
        this.email               = email;
        this.documentsEmpruntes  = new ArrayList<>();
        this.documentsReserves   = new ArrayList<>();
        this.soldeAmendes        = 0.0;
    }

    // ---- Getters ----
    public String getMatricule()                    { return matricule; }
    public String getNom()                          { return nom; }
    public String getPrenom()                       { return prenom; }
    public String getEmail()                        { return email; }
    public List<Document> getDocumentsEmpruntes()   { return documentsEmpruntes; }
    public List<Document> getDocumentsReserves()    { return documentsReserves; }
    public double getSoldeAmendes()                 { return soldeAmendes; }

    // ---- Setters ----
    public void setSoldeAmendes(double solde)       { this.soldeAmendes = solde; }

    /**
     * Emprunter un document (max 3 documents).
     * @return true si l'emprunt est accepté, false sinon.
     */
    public boolean emprunter(Document doc) {
        if (documentsEmpruntes.size() >= 3) {
            System.out.println("❌ " + prenom + " a déjà 3 documents empruntés.");
            return false;
        }
        if (doc.getNombreExemplaires() <= 0) {
            System.out.println("❌ Aucun exemplaire disponible pour : " + doc.getTitre());
            return false;
        }
        documentsEmpruntes.add(doc);
        doc.setNombreExemplaires(doc.getNombreExemplaires() - 1);
        System.out.println("✅ Emprunt validé : " + doc.getTitre()
                + " par " + prenom + " " + nom);
        return true;
    }

    /**
     * Rendre un document emprunté.
     * @return true si le retour est accepté, false sinon.
     */
    public boolean rendreDocument(Document doc) {
        if (!documentsEmpruntes.contains(doc)) {
            System.out.println("❌ Ce document n'est pas dans vos emprunts.");
            return false;
        }
        documentsEmpruntes.remove(doc);
        doc.setNombreExemplaires(doc.getNombreExemplaires() + 1);
        System.out.println("✅ Retour enregistré : " + doc.getTitre());
        return true;
    }

    /**
     * Réserver un document (max 3 jours).
     * @return true si la réservation est acceptée, false sinon.
     */
    public boolean reserverDocument(Document doc) {
        if (documentsReserves.contains(doc)) {
            System.out.println("❌ Ce document est déjà réservé.");
            return false;
        }
        documentsReserves.add(doc);
        System.out.println("✅ Réservation effectuée : " + doc.getTitre()
                + " (valable 3 jours)");
        return true;
    }

    /**
     * Payer une amende.
     * @param montant montant à payer
     */
    public void payerTaxe(double montant) {
        if (montant <= 0 || montant > soldeAmendes) {
            System.out.println("❌ Montant invalide.");
            return;
        }
        soldeAmendes -= montant;
        System.out.println("✅ Paiement de " + montant + " FCFA enregistré. "
                + "Solde restant : " + soldeAmendes + " FCFA");
    }

    /**
     * Affiche les documents empruntés par cet adhérent.
     */
    public void afficherEmprunts() {
        System.out.println("=== Emprunts de " + prenom + " " + nom + " ===");
        if (documentsEmpruntes.isEmpty()) {
            System.out.println("Aucun document emprunté.");
        } else {
            for (Document doc : documentsEmpruntes) {
                System.out.println("  - " + doc.getTitre()
                        + " (" + doc.getClass().getSimpleName() + ")");
            }
        }
        System.out.println("Amendes dues : " + soldeAmendes + " FCFA");
        System.out.println("==============================");
    }

    /**
     * Format fichier adherent.txt :
     * matricule && nom && prenom && email && nombreDocsEmpruntes
     */
    public String toFileLine() {
        return matricule + " && " + nom + " && " + prenom
                + " && " + email + " && " + documentsEmpruntes.size();
    }

    /**
     * Format fichier emprunt.txt :
     * matricule && titreDoc1 && titreDoc2 && titreDoc3
     */
    public String toEmpruntLine() {
        String[] docs = {"Rien", "Rien", "Rien"};
        for (int i = 0; i < documentsEmpruntes.size(); i++) {
            docs[i] = documentsEmpruntes.get(i).getTitre();
        }
        return matricule + " && " + docs[0] + " && " + docs[1] + " && " + docs[2];
    }

    @Override
    public String toString() {
        return matricule + " - " + prenom + " " + nom;
    }
}