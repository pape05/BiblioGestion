package modeles;

import interfaces.Document;

/**
 * Classe Livre - représente un livre dans la bibliothèque.
 * Implémente l'interface Document.
 */
public class Livre implements Document {

    private String titre;
    private String auteur;
    private double prix;
    private int nombrePages;
    private int nombreExemplaires;

    // ---- Constructeur ----
    public Livre(String titre, String auteur, double prix,
                 int nombrePages, int nombreExemplaires) {
        this.titre            = titre;
        this.auteur           = auteur;
        this.prix             = prix;
        this.nombrePages      = nombrePages;
        this.nombreExemplaires = nombreExemplaires;
    }

    // ---- Getters ----
    @Override public String getTitre()          { return titre; }
    @Override public String getAuteur()         { return auteur; }
    @Override public double getPrix()           { return prix; }
    @Override public int getNombreExemplaires() { return nombreExemplaires; }
    public int getNombrePages()                 { return nombrePages; }

    // ---- Setters ----
    @Override
    public void setNombreExemplaires(int nombre) {
        this.nombreExemplaires = nombre;
    }

    @Override
    public void afficherDetails() {
        System.out.println("=== LIVRE ===");
        System.out.println("Titre       : " + titre);
        System.out.println("Auteur      : " + auteur);
        System.out.println("Prix        : " + prix + " FCFA");
        System.out.println("Pages       : " + nombrePages);
        System.out.println("Exemplaires : " + nombreExemplaires);
        System.out.println("=============");
    }

    /** Format : titre && auteur && prix && nombrePages && nombreExemplaires */
    @Override
    public String toFileLine() {
        return titre + " && " + auteur + " && " + prix
               + " && " + nombrePages + " && " + nombreExemplaires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livre)) return false;
        Livre autre = (Livre) o;
        return titre.equalsIgnoreCase(autre.titre)
                && auteur.equalsIgnoreCase(autre.auteur);
    }

    @Override
    public int hashCode() {
        return titre.toLowerCase().hashCode()
                + auteur.toLowerCase().hashCode();
    }

    @Override
    public String toString() { return titre; }
}