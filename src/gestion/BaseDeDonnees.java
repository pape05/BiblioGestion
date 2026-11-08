package gestion;

import modeles.*;
import java.io.*;
import java.util.*;

/**
 * Classe BaseDeDonnees - gère la persistance des données dans des fichiers .txt
 * Crée automatiquement le dossier BaseDeDonnees et tous les fichiers nécessaires.
 */
public class BaseDeDonnees {

    private static final String DOSSIER = "BaseDeDonnees";
    private static final String FICHIER_ADHERENTS = DOSSIER + "/adherent.txt";
    private static final String FICHIER_LIVRES    = DOSSIER + "/livre.txt";
    private static final String FICHIER_REVUES    = DOSSIER + "/revue.txt";
    private static final String FICHIER_ARTICLES  = DOSSIER + "/article.txt";
    private static final String FICHIER_EMPRUNTS  = DOSSIER + "/emprunt.txt";
    private static final String FICHIER_TAXES     = DOSSIER + "/taxe.txt";

    // ---- Constructeur : crée le dossier et les fichiers si inexistants ----
    public BaseDeDonnees() {
        initialiser();
    }

    /**
     * Crée le dossier BaseDeDonnees et tous les fichiers .txt nécessaires.
     */
    public void initialiser() {
        File dossier = new File(DOSSIER);
        if (!dossier.exists()) {
            dossier.mkdir();
            System.out.println("📁 Dossier créé : " + dossier.getAbsolutePath());
        } else {
            System.out.println("📁 Dossier existant : " + dossier.getAbsolutePath());
        }

        creerFichierSiAbsent(FICHIER_ADHERENTS, "adherent.txt");
        creerFichierSiAbsent(FICHIER_LIVRES,    "livre.txt");
        creerFichierSiAbsent(FICHIER_REVUES,    "revue.txt");
        creerFichierSiAbsent(FICHIER_ARTICLES,  "article.txt");
        creerFichierSiAbsent(FICHIER_EMPRUNTS,  "emprunt.txt");
        creerFichierSiAbsent(FICHIER_TAXES,     "taxe.txt");
    }

    private void creerFichierSiAbsent(String chemin, String nom) {
        File f = new File(chemin);
        try {
            if (!f.exists()) {
                f.createNewFile();
                System.out.println("📄 Fichier créé : " + nom);
            }
        } catch (IOException e) {
            System.out.println("❌ Erreur création fichier " + nom + " : " + e.getMessage());
        }
    }

    // ================================================================
    //  ÉCRITURE
    // ================================================================

    /** Enregistre un adhérent dans adherent.txt */
    public void enregistrerAdherent(Adherent a) {
        ecrireLigne(FICHIER_ADHERENTS, a.toFileLine());
    }

    /** Met à jour la ligne d'un adhérent (réécriture complète du fichier) */
    public void mettreAJourAdherent(List<Adherent> adherents) {
        effacerFichier(FICHIER_ADHERENTS);
        for (Adherent a : adherents) {
            ecrireLigne(FICHIER_ADHERENTS, a.toFileLine());
        }
    }

    /** Enregistre un livre dans livre.txt */
    public void enregistrerLivre(Livre l) {
        ecrireLigne(FICHIER_LIVRES, l.toFileLine());
    }

    /** Enregistre une revue dans revue.txt */
    public void enregistrerRevue(Revue r) {
        ecrireLigne(FICHIER_REVUES, r.toFileLine());
    }

    /** Enregistre un article dans article.txt */
    public void enregistrerArticle(Article a) {
        ecrireLigne(FICHIER_ARTICLES, a.toFileLine());
    }

    /** Met à jour tous les livres (réécriture complète) */
    public void mettreAJourLivres(List<Livre> livres) {
        effacerFichier(FICHIER_LIVRES);
        for (Livre l : livres) {
            ecrireLigne(FICHIER_LIVRES, l.toFileLine());
        }
    }

    /** Met à jour toutes les revues (réécriture complète) */
    public void mettreAJourRevues(List<Revue> revues) {
        effacerFichier(FICHIER_REVUES);
        for (Revue r : revues) {
            ecrireLigne(FICHIER_REVUES, r.toFileLine());
        }
    }

    /** Met à jour tous les articles (réécriture complète) */
    public void mettreAJourArticles(List<Article> articles) {
        effacerFichier(FICHIER_ARTICLES);
        for (Article a : articles) {
            ecrireLigne(FICHIER_ARTICLES, a.toFileLine());
        }
    }

    /** Enregistre ou met à jour un emprunt dans emprunt.txt */
    public void enregistrerEmprunt(Adherent a) {
        ecrireLigne(FICHIER_EMPRUNTS, a.toEmpruntLine());
    }

    /** Met à jour tous les emprunts (réécriture complète) */
    public void mettreAJourEmprunts(List<Adherent> adherents) {
        effacerFichier(FICHIER_EMPRUNTS);
        for (Adherent a : adherents) {
            ecrireLigne(FICHIER_EMPRUNTS, a.toEmpruntLine());
        }
    }

    /**
     * Enregistre une taxe dans taxe.txt
     * Format : natureTaxe && montant && matricule && situation
     */
    public void enregistrerTaxe(String natureTaxe, double montant,
                                 String matricule, String situation) {
        String ligne = natureTaxe + " && " + montant + " && "
                + matricule + " && " + situation;
        ecrireLigne(FICHIER_TAXES, ligne);
    }

    /** Met à jour la situation d'une taxe (payé/impayé) */
    public void mettreAJourTaxe(String matricule, String ancienneSituation,
                                  String nouvelleSituation) {
        List<String> lignes = lireFichier(FICHIER_TAXES);
        effacerFichier(FICHIER_TAXES);
        for (String ligne : lignes) {
            String[] parts = ligne.split(" && ");
            if (parts.length == 4
                    && parts[2].trim().equals(matricule)
                    && parts[3].trim().equals(ancienneSituation)) {
                parts[3] = " " + nouvelleSituation;
                ligne = parts[0] + " && " + parts[1] + " && "
                        + parts[2] + " &&" + parts[3];
            }
            ecrireLigne(FICHIER_TAXES, ligne);
        }
    }

    // ================================================================
    //  LECTURE
    // ================================================================

    /** Charge tous les livres depuis livre.txt */
    public List<Livre> chargerLivres() {
        List<Livre> livres = new ArrayList<>();
        for (String ligne : lireFichier(FICHIER_LIVRES)) {
            String[] p = ligne.split(" && ");
            if (p.length == 5) {
                try {
                    livres.add(new Livre(p[0], p[1],
                            Double.parseDouble(p[2].trim()),
                            Integer.parseInt(p[3].trim()),
                            Integer.parseInt(p[4].trim())));
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Ligne ignorée (livre) : " + ligne);
                }
            }
        }
        return livres;
    }

    /** Charge toutes les revues depuis revue.txt */
    public List<Revue> chargerRevues() {
        List<Revue> revues = new ArrayList<>();
        for (String ligne : lireFichier(FICHIER_REVUES)) {
            String[] p = ligne.split(" && ");
            if (p.length == 5) {
                try {
                    revues.add(new Revue(p[0], p[1],
                            Double.parseDouble(p[2].trim()),
                            p[3].trim(),
                            Integer.parseInt(p[4].trim())));
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Ligne ignorée (revue) : " + ligne);
                }
            }
        }
        return revues;
    }

    /** Charge tous les articles depuis article.txt */
    public List<Article> chargerArticles() {
        List<Article> articles = new ArrayList<>();
        for (String ligne : lireFichier(FICHIER_ARTICLES)) {
            String[] p = ligne.split(" && ");
            if (p.length == 5) {
                try {
                    articles.add(new Article(p[0], p[1],
                            Double.parseDouble(p[2].trim()),
                            p[3].trim(),
                            Integer.parseInt(p[4].trim())));
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Ligne ignorée (article) : " + ligne);
                }
            }
        }
        return articles;
    }

    /** Charge tous les adhérents depuis adherent.txt */
    public List<Adherent> chargerAdherents() {
        List<Adherent> adherents = new ArrayList<>();
        for (String ligne : lireFichier(FICHIER_ADHERENTS)) {
            String[] p = ligne.split(" && ");
            if (p.length >= 4) {
                adherents.add(new Adherent(p[0].trim(), p[1].trim(),
                        p[2].trim(), p[3].trim()));
            }
        }
        return adherents;
    }

    /**
     * Restaure les emprunts depuis emprunt.txt.
     * Format : matricule && titreDoc1 && titreDoc2 && titreDoc3
     */
    public void restaurerEmprunts(List<Adherent> adherents,
                                   List<interfaces.Document> documents) {
        for (String ligne : lireFichier(FICHIER_EMPRUNTS)) {
            String[] p = ligne.split(" && ");
            if (p.length >= 2) {
                String matricule = p[0].trim();
                Adherent adherent = null;
                for (Adherent a : adherents) {
                    if (a.getMatricule().equals(matricule)) {
                        adherent = a;
                        break;
                    }
                }
                if (adherent == null) continue;
                for (int i = 1; i < p.length; i++) {
                    String titreDoc = p[i].trim();
                    if (titreDoc.equals("Rien")) continue;
                    for (interfaces.Document doc : documents) {
                        if (doc.getTitre().equalsIgnoreCase(titreDoc)) {
                            adherent.getDocumentsEmpruntes().add(doc);
                            doc.setNombreExemplaires(doc.getNombreExemplaires() - 1);
                            break;
                        }
                    }
                }
            }
        }
    }

    /** Charge toutes les lignes de taxe.txt */
    public List<String> chargerTaxes() {
        return lireFichier(FICHIER_TAXES);
    }

    // ================================================================
    //  UTILITAIRES PRIVÉS
    // ================================================================

    private void ecrireLigne(String chemin, String ligne) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(chemin, true))) {
            bw.write(ligne);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("❌ Erreur écriture dans " + chemin
                    + " : " + e.getMessage());
        }
    }

    private List<String> lireFichier(String chemin) {
        List<String> lignes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(chemin))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (!ligne.trim().isEmpty()) {
                    lignes.add(ligne);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Erreur lecture " + chemin
                    + " : " + e.getMessage());
        }
        return lignes;
    }

    private void effacerFichier(String chemin) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(chemin, false))) {
            bw.write("");
        } catch (IOException e) {
            System.out.println("❌ Erreur effacement " + chemin
                    + " : " + e.getMessage());
        }
    }
}