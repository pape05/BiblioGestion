package modeles;

import interfaces.Document;

/**
 * Classe abstraite Periodique - classe parente de Revue et Article.
 */
public abstract class Periodique implements Document {

    protected String titre;
    protected String auteur;
    protected double prix;
    protected String periode;
    protected int nombreExemplaires;

    // ---- Constructeur ----
    public Periodique(String titre, String auteur, double prix,
                      String periode, int nombreExemplaires) {
        this.titre             = titre;
        this.auteur            = auteur;
        this.prix              = prix;
        this.periode           = periode;
        this.nombreExemplaires = nombreExemplaires;
    }

    // ---- Getters ----
    @Override public String getTitre()          { return titre; }
    @Override public String getAuteur()         { return auteur; }
    @Override public double getPrix()           { return prix; }
    @Override public int getNombreExemplaires() { return nombreExemplaires; }
    public String getPeriode()                  { return periode; }

    // ---- Setters ----
    @Override
    public void setNombreExemplaires(int nombre) {
        this.nombreExemplaires = nombre;
    }

    /** Format : titre && auteur && prix && periode && nombreExemplaires */
    @Override
    public String toFileLine() {
        return titre + " && " + auteur + " && " + prix
               + " && " + periode + " && " + nombreExemplaires;
    }

    @Override
    public abstract void afficherDetails();
}