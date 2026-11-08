package modeles;

/**
 * Classe Article - hérite de Periodique.
 */
public class Article extends Periodique {

    public Article(String titre, String auteur, double prix,
                   String periode, int nombreExemplaires) {
        super(titre, auteur, prix, periode, nombreExemplaires);
    }

    @Override
    public void afficherDetails() {
        System.out.println("=== ARTICLE ===");
        System.out.println("Titre       : " + titre);
        System.out.println("Auteur      : " + auteur);
        System.out.println("Prix        : " + prix + " FCFA");
        System.out.println("Période     : " + periode);
        System.out.println("Exemplaires : " + nombreExemplaires);
        System.out.println("===============");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article autre = (Article) o;
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