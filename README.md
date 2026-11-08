# BiblioGestion

Application de gestion de bibliotheque universitaire developpee en Java avec JavaFX, dans un contexte senegalais.

## Description

BiblioGestion permet a un gestionnaire de bibliotheque de gerer l'ensemble des operations quotidiennes liees aux documents, aux adherents, aux emprunts et aux amendes depuis une interface graphique unique au theme sombre inspire des couleurs du drapeau senegalais (vert, jaune, rouge).

## Fonctionnalites

- **Gestion des documents** : ajout et consultation de livres, revues et articles avec titre, auteur, prix (FCFA) et nombre d'exemplaires
- **Gestion des adherents** : inscription avec matricule unique, nom, prenom et email, recherche par matricule
- **Emprunts** : maximum 3 documents par adherent, verification de disponibilite, duree de 30 jours
- **Retours** : enregistrement avec indication d'etat (abime ou retard)
- **Reservations** : valables 3 jours, decremente les exemplaires disponibles
- **Amendes** : retard = 25% du prix, document abime = 100% du prix, paiement avec suivi du solde et historique

## Regles metier

| Regle | Detail |
|-------|--------|
| Emprunt max | 3 documents par adherent |
| Duree emprunt | 30 jours |
| Duree reservation | 3 jours |
| Amende retard | 25% du prix du document |
| Amende dommage | 100% du prix du document |
| Matricule | Unique par adherent |

## Technologies

- Java 17
- JavaFX 17
- Fichiers texte plats (.txt)
- Visual Studio Code

## Prerequis

- Java JDK 17 ou superieur
- JavaFX SDK 17 (a telecharger separement)

## Compilation et execution

Compiler :

```bash
javac -encoding UTF-8 --module-path chemin/vers/javafx-sdk/lib --add-modules javafx.controls,javafx.graphics -d out -sourcepath src src/MainBibliotheque.java src/gui/MainWindow.java src/gestion/Bibliotheque.java src/gestion/BaseDeDonnees.java src/interfaces/Document.java src/modeles/Adherent.java src/modeles/Livre.java src/modeles/Revue.java src/modeles/Article.java src/modeles/Periodique.java
```

Executer :

```bash
java --module-path chemin/vers/javafx-sdk/lib --add-modules javafx.controls,javafx.graphics -cp out MainBibliotheque
```

## Structure du projet

```
BiblioGestion/
  src/
    MainBibliotheque.java        -- Point d'entree
    gui/
      MainWindow.java            -- Interface graphique JavaFX
    gestion/
      Bibliotheque.java          -- Logique metier
      BaseDeDonnees.java         -- Persistance fichiers texte
    interfaces/
      Document.java              -- Interface commune des documents
    modeles/
      Livre.java                 -- Modele livre
      Revue.java                 -- Modele revue
      Article.java               -- Modele article
      Periodique.java            -- Classe abstraite pour revues et articles
      Adherent.java              -- Modele adherent
  BaseDeDonnees/
    adherent.txt                 -- Donnees des adherents
    livre.txt                    -- Donnees des livres
    revue.txt                    -- Donnees des revues
    article.txt                  -- Donnees des articles
    emprunt.txt                  -- Emprunts en cours
    taxe.txt                     -- Historique des amendes
```

## Auteur

Projet universitaire - Licence 3
