package interfaces;

/**
 * Interface Document - définit les caractéristiques et méthodes
 * communes à tous les types de documents de la bibliothèque.
 */
public interface Document {

    // ---- Getters ----
    String getTitre();
    String getAuteur();
    double getPrix();
    int getNombreExemplaires();

    // ---- Setters ----
    void setNombreExemplaires(int nombre);

    /**
     * Affiche les détails complets du document.
     */
    void afficherDetails();

    /**
     * Retourne une ligne formatée pour l'écriture dans les fichiers .txt
     */
    String toFileLine();
}