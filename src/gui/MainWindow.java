package gui;

import gestion.Bibliotheque;
import interfaces.Document;
import modeles.*;
import javafx.application.Application;
import javafx.animation.*;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

/**
 * Classe MainWindow - Interface graphique principale JavaFX.
 * Design moderne avec drapeau sénégalais et image de fond.
 */
public class MainWindow extends Application {

    private Bibliotheque bibliotheque;
    private BorderPane   root;
    private StackPane    contentArea;
    private Stage        stage;

    // Palette couleurs inspirée du Sénégal
    private static final String COLOR_VERT      = "#00853F";  // vert sénégal
    private static final String COLOR_JAUNE     = "#FDEF42";  // jaune sénégal
    private static final String COLOR_ROUGE     = "#E31B23";  // rouge sénégal
    private static final String COLOR_DARK      = "#1A1A2E";  // fond sombre
    private static final String COLOR_CARD      = "#16213E";  // carte
    private static final String COLOR_SIDEBAR   = "#0F3460";  // sidebar
    private static final String COLOR_ACCENT    = "#00853F";  // accent vert
    private static final String COLOR_WHITE     = "#FFFFFF";
    private static final String COLOR_LIGHT     = "#F0F4F8";
    private static final String COLOR_TEXT_GRAY = "#A0AEC0";

    // Bouton actif dans la sidebar
    private Button boutonActif = null;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        bibliotheque = new Bibliotheque();
        bibliotheque.insererDonneesTestSiVide();

        root = new BorderPane();

        // ---- Fond général dégradé ----
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1A1A2E, #16213E, #0F3460);");

        root.setTop(creerHeader());
        root.setLeft(creerSidebar());

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(25));
        root.setCenter(contentArea);

        afficherVueAccueil();

        Scene scene = new Scene(root, 1200, 750);
        primaryStage.initStyle(javafx.stage.StageStyle.DECORATED);
        primaryStage.setTitle("BiblioGestion Senegal");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
        primaryStage.toFront();
        primaryStage.requestFocus();
    }

    // ================================================================
    //  HEADER
    // ================================================================
    private HBox creerHeader() {
        HBox header = new HBox();
        header.setStyle(
            "-fx-background-color: linear-gradient(to right, #00853F, #1A1A2E);"
        );
        header.setPadding(new Insets(12, 25, 12, 25));
        header.setAlignment(Pos.CENTER_LEFT);

        // Drapeau Sénégal dessiné en JavaFX
        Canvas drapeau = creerDrapeauSenegal(60, 40);

        // Séparateur vertical
        Rectangle sep = new Rectangle(2, 40, Color.web("#FFFFFF44"));
        sep.setStyle("-fx-margin: 0 15 0 15;");

        // Titre
        VBox titreBox = new VBox(2);
        Label titre = new Label("BiblioGestion");
        titre.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titre.setTextFill(Color.WHITE);
        Label sousTitre = new Label("Système de Gestion de Bibliothèque");
        sousTitre.setFont(Font.font("Segoe UI", FontPosture.ITALIC, 12));
        sousTitre.setTextFill(Color.web("#CCCCCC"));
        titreBox.getChildren().addAll(titre, sousTitre);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Badge version
        Label badge = new Label("v1.0  🇸🇳");
        badge.setStyle(
            "-fx-background-color: rgba(255,255,255,0.15);" +
            "-fx-text-fill: white;" +
            "-fx-padding: 5 12 5 12;" +
            "-fx-background-radius: 20;" +
            "-fx-font-size: 12px;"
        );

        // Boutons de contrôle fenêtre
        Button btnReduire = creerBoutonFenetre("—");
        btnReduire.setOnAction(e -> stage.setIconified(true));

        Button btnAgrandir = creerBoutonFenetre("☐");
        btnAgrandir.setOnAction(e -> {
            stage.setMaximized(!stage.isMaximized());
            btnAgrandir.setText(stage.isMaximized() ? "❐" : "☐");
        });

        HBox boutonsFenetre = new HBox(5);
        boutonsFenetre.setAlignment(Pos.CENTER);
        boutonsFenetre.getChildren().addAll(btnReduire, btnAgrandir);

        HBox.setMargin(drapeau, new Insets(0, 10, 0, 0));
        HBox.setMargin(sep,    new Insets(0, 15, 0, 5));
        HBox.setMargin(titreBox, new Insets(0, 0, 0, 5));
        HBox.setMargin(boutonsFenetre, new Insets(0, 0, 0, 10));
        header.getChildren().addAll(drapeau, sep, titreBox, spacer, badge, boutonsFenetre);

        // Ombre sous le header
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.4));
        shadow.setOffsetY(3);
        header.setEffect(shadow);

        return header;
    }

    /**
     * Dessine le drapeau du Sénégal (vert | jaune | rouge + étoile verte).
     */
    private Canvas creerDrapeauSenegal(double largeur, double hauteur) {
        Canvas canvas = new Canvas(largeur, hauteur);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        double tiers = largeur / 3;

        // Bandes
        gc.setFill(Color.web("#00853F"));
        gc.fillRect(0, 0, tiers, hauteur);

        gc.setFill(Color.web("#FDEF42"));
        gc.fillRect(tiers, 0, tiers, hauteur);

        gc.setFill(Color.web("#E31B23"));
        gc.fillRect(tiers * 2, 0, tiers, hauteur);

        // Étoile verte au centre
        gc.setFill(Color.web("#00853F"));
        double cx = largeur / 2;
        double cy = hauteur / 2;
        double r  = hauteur * 0.28;
        double[] xs = new double[10];
        double[] ys = new double[10];
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 2 + i * Math.PI / 5;
            double rayon = (i % 2 == 0) ? r : r * 0.4;
            xs[i] = cx + rayon * Math.cos(angle);
            ys[i] = cy - rayon * Math.sin(angle);
        }
        gc.fillPolygon(xs, ys, 10);

        // Bordure
        gc.setStroke(Color.web("#FFFFFF44"));
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, largeur, hauteur);

        return canvas;
    }

    // ================================================================
    //  SIDEBAR
    // ================================================================
    private VBox creerSidebar() {
        VBox sb = new VBox(8);
        sb.setStyle("-fx-background-color: " + COLOR_SIDEBAR + ";");
        sb.setPadding(new Insets(25, 12, 25, 12));
        sb.setPrefWidth(210);

        // Label menu
        Label lblMenu = new Label("NAVIGATION");
        lblMenu.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        lblMenu.setTextFill(Color.web(COLOR_TEXT_GRAY));
        lblMenu.setPadding(new Insets(0, 0, 10, 8));

        Button btnAccueil   = creerBoutonMenu("🏠   Accueil",    () -> afficherVueAccueil());
        Button btnDocs      = creerBoutonMenu("📖   Documents",  () -> afficherVueDocuments());
        Button btnAdherents = creerBoutonMenu("👤   Adhérents",  () -> afficherVueAdherents());
        Button btnEmprunts  = creerBoutonMenu("🔄   Emprunts",   () -> afficherVueEmprunts());
        Button btnAmendes   = creerBoutonMenu("💰   Amendes",    () -> afficherVueAmendes());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Info bas sidebar
        VBox infoBox = new VBox(4);
        infoBox.setStyle(
            "-fx-background-color: rgba(255,255,255,0.05);" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10;"
        );
        Label lblInfo  = new Label("📚 BiblioGestion");
        lblInfo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        lblInfo.setTextFill(Color.WHITE);
        Label lblInfo2 = new Label("Université — L2A");
        lblInfo2.setFont(Font.font("Segoe UI", 10));
        lblInfo2.setTextFill(Color.web(COLOR_TEXT_GRAY));
        infoBox.getChildren().addAll(lblInfo, lblInfo2);

        sb.getChildren().addAll(lblMenu, btnAccueil, btnDocs,
                                 btnAdherents, btnEmprunts, btnAmendes,
                                 spacer, infoBox);
        return sb;
    }

    private Button creerBoutonMenu(String texte, Runnable action) {
        Button btn = new Button(texte);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPadding(new Insets(11, 15, 11, 15));
        btn.setFont(Font.font("Segoe UI", 13));
        appliquerStyleBoutonInactif(btn);

        btn.setOnMouseEntered(e -> {
            if (btn != boutonActif) appliquerStyleBoutonHover(btn);
        });
        btn.setOnMouseExited(e -> {
            if (btn != boutonActif) appliquerStyleBoutonInactif(btn);
        });
        btn.setOnAction(e -> {
            if (boutonActif != null) appliquerStyleBoutonInactif(boutonActif);
            boutonActif = btn;
            appliquerStyleBoutonActif(btn);
            action.run();
        });
        return btn;
    }

    private void appliquerStyleBoutonInactif(Button btn) {
        btn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #A0AEC0;" +
            "-fx-cursor: hand;" +
            "-fx-alignment: CENTER_LEFT;" +
            "-fx-background-radius: 8;"
        );
    }

    private void appliquerStyleBoutonHover(Button btn) {
        btn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-text-fill: white;" +
            "-fx-cursor: hand;" +
            "-fx-alignment: CENTER_LEFT;" +
            "-fx-background-radius: 8;"
        );
    }

    private void appliquerStyleBoutonActif(Button btn) {
        btn.setStyle(
            "-fx-background-color: " + COLOR_VERT + ";" +
            "-fx-text-fill: white;" +
            "-fx-cursor: hand;" +
            "-fx-alignment: CENTER_LEFT;" +
            "-fx-background-radius: 8;" +
            "-fx-font-weight: bold;"
        );
    }

    // ================================================================
    //  VUE ACCUEIL
    // ================================================================
    private void afficherVueAccueil() {
    contentArea.getChildren().clear();

    StackPane stackAccueil = new StackPane();

    Canvas fond = new Canvas();

    InvalidationListener redrawListener = obs -> {
        double w = stackAccueil.getWidth();
        double h = stackAccueil.getHeight();
        if (w > 0 && h > 0) {
            fond.setWidth(w);
            fond.setHeight(h);
            dessinerFondBibliotheque(fond.getGraphicsContext2D(), w, h);
        }
    };
    stackAccueil.widthProperty().addListener(redrawListener);
    stackAccueil.heightProperty().addListener(redrawListener);

    VBox contenu = new VBox(22);
    contenu.setAlignment(Pos.TOP_CENTER);
    contenu.setPadding(new Insets(35, 40, 35, 40));

    HBox entete = new HBox(15);
    entete.setAlignment(Pos.CENTER);
    Canvas drapeau = creerDrapeauSenegal(75, 50);

    VBox titreBox = new VBox(4);
    titreBox.setAlignment(Pos.CENTER_LEFT);
    Label lblBienvenue = new Label("Bienvenue sur BiblioGestion");
    lblBienvenue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
    lblBienvenue.setTextFill(Color.WHITE);
    lblBienvenue.setEffect(new DropShadow(6, Color.BLACK));

    Label lblSous = new Label("Systeme de gestion de bibliotheque -- Senegal");
    lblSous.setFont(Font.font("Segoe UI", FontPosture.ITALIC, 14));
    lblSous.setTextFill(Color.web("#FDEF42"));
    titreBox.getChildren().addAll(lblBienvenue, lblSous);
    entete.getChildren().addAll(drapeau, titreBox);

    HBox separateur = new HBox();
    separateur.setMaxWidth(500);
    separateur.setPrefHeight(4);
    Region r1 = new Region(); r1.setPrefWidth(167); r1.setPrefHeight(4);
    r1.setStyle("-fx-background-color: #00853F; -fx-background-radius: 4 0 0 4;");
    HBox.setHgrow(r1, Priority.ALWAYS);
    Region r2 = new Region(); r2.setPrefWidth(167); r2.setPrefHeight(4);
    r2.setStyle("-fx-background-color: #FDEF42;");
    HBox.setHgrow(r2, Priority.ALWAYS);
    Region r3 = new Region(); r3.setPrefWidth(167); r3.setPrefHeight(4);
    r3.setStyle("-fx-background-color: #E31B23; -fx-background-radius: 0 4 4 0;");
    HBox.setHgrow(r3, Priority.ALWAYS);
    separateur.getChildren().addAll(r1, r2, r3);
    separateur.setAlignment(Pos.CENTER);

    int nbDocs      = bibliotheque.getDocuments().size();
    int nbAdherents = bibliotheque.getAdherents().size();
    long nbLivres   = bibliotheque.getDocuments().stream()
                        .filter(d -> d instanceof Livre).count();
    long nbPeriodiques = bibliotheque.getDocuments().stream()
                        .filter(d -> d instanceof Periodique).count();

    FlowPane cartes = new FlowPane(20, 20);
    cartes.setAlignment(Pos.CENTER);
    cartes.getChildren().addAll(
        creerCarte("Documents",    String.valueOf(nbDocs),         COLOR_VERT),
        creerCarte("Adherents",    String.valueOf(nbAdherents),    "#3498DB"),
        creerCarte("Livres",       String.valueOf(nbLivres),       COLOR_ROUGE),
        creerCarte("Periodiques",  String.valueOf(nbPeriodiques),  "#9B59B6")
    );

    VBox citationBox = new VBox(6);
    citationBox.setAlignment(Pos.CENTER);
    citationBox.setStyle(
        "-fx-background-color: rgba(0,0,0,0.4);" +
        "-fx-background-radius: 12;" +
        "-fx-padding: 18 34 18 34;"
    );

    Label citation = new Label(
        "\"Un livre est un miroir : si un singe y regarde, il n'en peut sortir qu'un singe.\""
    );
    citation.setFont(Font.font("Segoe UI", FontPosture.ITALIC, 14));
    citation.setTextFill(Color.web("#E2E8F0"));
    citation.setWrapText(true);
    citation.setMaxWidth(600);
    citation.setAlignment(Pos.CENTER);

    Label auteurCitation = new Label("-- Georg Christoph Lichtenberg");
    auteurCitation.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
    auteurCitation.setTextFill(Color.web(COLOR_JAUNE));

    citationBox.getChildren().addAll(citation, auteurCitation);

    contenu.getChildren().addAll(entete, separateur, cartes, citationBox);

    stackAccueil.getChildren().addAll(fond, contenu);

    FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contenu);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);
    fadeIn.play();

    contentArea.getChildren().add(stackAccueil);
}

// ================================================================
//  FOND RESPONSIVE
// ================================================================

private void dessinerFondBibliotheque(GraphicsContext gc, double w, double h) {
    gc.clearRect(0, 0, w, h);
    gc.save();
    gc.scale(w / 1000.0, h / 650.0);

    gc.setFill(Color.web("#1a0a00"));
    gc.fillRect(0, 0, 1000, 650);

    gc.setFill(Color.web("#3d1f00"));
    gc.fillRect(0, 500, 1000, 150);
    for (int i = 0; i < 1000; i += 80) {
        gc.setStroke(Color.web("#2a1500"));
        gc.setLineWidth(1.5);
        gc.strokeLine(i, 500, i + 40, 650);
    }

    javafx.scene.paint.RadialGradient lumiere =
        new javafx.scene.paint.RadialGradient(
            0, 0, 0.5, 0.1, 0.6, true,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#fff8e1", 0.15)),
            new Stop(1, Color.web("#000000", 0.0))
        );
    gc.setFill(lumiere);
    gc.fillRect(0, 0, 1000, 650);

    dessinerEtagere(gc, 20,  80,  180, 4);
    dessinerEtagere(gc, 20,  240, 180, 4);
    dessinerEtagere(gc, 20,  390, 180, 3);
    dessinerEtagere(gc, 220, 80,  200, 5);
    dessinerEtagere(gc, 220, 250, 200, 5);
    dessinerEtagere(gc, 220, 410, 200, 4);
    dessinerEtagere(gc, 440, 60,  180, 4);
    dessinerEtagere(gc, 440, 220, 180, 5);
    dessinerEtagere(gc, 440, 380, 180, 3);
    dessinerEtagere(gc, 640, 80,  180, 4);
    dessinerEtagere(gc, 640, 240, 180, 5);
    dessinerEtagere(gc, 640, 400, 180, 4);
    dessinerEtagere(gc, 840, 100, 140, 3);
    dessinerEtagere(gc, 840, 260, 140, 4);
    dessinerEtagere(gc, 840, 410, 140, 3);

    dessinerLampe(gc, 300, 450);
    dessinerLampe(gc, 680, 450);

    javafx.scene.paint.LinearGradient overlay =
        new javafx.scene.paint.LinearGradient(
            0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#1A1A2E", 0.55)),
            new Stop(0.5, Color.web("#1A1A2E", 0.35)),
            new Stop(1.0, Color.web("#1A1A2E", 0.70))
        );
    gc.setFill(overlay);
    gc.fillRect(0, 0, 1000, 650);

    gc.restore();
}

// ================================================================
//  MÉTHODES UTILITAIRES DESSIN
// ================================================================

private void dessinerEtagere(GraphicsContext gc, double x, double y,
                               double largeur, int nbLivres) {
    double hauteurEtagere = 120;
    double epaisseur      = 12;

    // Structure bois de l'étagère
    gc.setFill(Color.web("#5c3317"));
    gc.fillRect(x, y, largeur, epaisseur);                          // planche haut
    gc.fillRect(x, y + hauteurEtagere, largeur, epaisseur);         // planche bas
    gc.fillRect(x, y, epaisseur, hauteurEtagere + epaisseur);       // côté gauche
    gc.fillRect(x + largeur - epaisseur, y, epaisseur,
                hauteurEtagere + epaisseur);                         // côté droit

    // Ombre portée étagère
    gc.setFill(Color.web("#000000", 0.3));
    gc.fillRect(x + 4, y + hauteurEtagere + epaisseur,
                largeur, 6);

    // Livres sur l'étagère
    String[] couleurs = {
        "#E74C3C","#3498DB","#2ECC71","#F39C12","#9B59B6",
        "#1ABC9C","#E67E22","#E91E63","#00BCD4","#8BC34A"
    };
    double xLivre = x + epaisseur + 4;
    double largeurTotale = largeur - epaisseur * 2 - 8;
    double largeurLivre  = largeurTotale / nbLivres - 2;

    for (int i = 0; i < nbLivres; i++) {
        double hauteurLivre = 70 + Math.random() * 35;
        double yLivre = y + epaisseur + (hauteurEtagere - hauteurLivre);

        // Corps du livre
        gc.setFill(Color.web(couleurs[i % couleurs.length]));
        gc.fillRect(xLivre, yLivre, largeurLivre, hauteurLivre);

        // Tranche du livre (effet 3D)
        gc.setFill(Color.web(couleurs[i % couleurs.length], 0.6));
        gc.fillRect(xLivre + largeurLivre, yLivre + 2,
                    3, hauteurLivre - 2);

        // Ligne décorative sur le dos
        gc.setStroke(Color.web("#FFFFFF", 0.3));
        gc.setLineWidth(1);
        gc.strokeLine(xLivre + 3, yLivre + 15,
                      xLivre + largeurLivre - 3, yLivre + 15);

        xLivre += largeurLivre + 2;
    }
}

private void dessinerLampe(GraphicsContext gc, double x, double y) {
    // Pied
    gc.setStroke(Color.web("#888888"));
    gc.setLineWidth(3);
    gc.strokeLine(x, y + 60, x, y + 10);
    gc.strokeLine(x - 30, y + 60, x + 30, y + 60);

    // Abat-jour
    gc.setFill(Color.web("#F5DEB3"));
    double[] xsLampe = {x - 25, x + 25, x + 15, x - 15};
    double[] ysLampe = {y + 10, y + 10, y - 20, y - 20};
    gc.fillPolygon(xsLampe, ysLampe, 4);

    // Halo lumineux
    javafx.scene.paint.RadialGradient halo =
        new javafx.scene.paint.RadialGradient(
            0, 0, x / 1000.0, (y + 30) / 650.0, 0.15, true,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#FFF8DC", 0.25)),
            new Stop(1, Color.web("#FFF8DC", 0.0))
        );
    gc.setFill(halo);
    gc.fillRect(x - 120, y - 30, 240, 200);
}

    private VBox creerCarte(String label,
                             String valeur, String couleur) {
        VBox carte = new VBox(8);
        carte.setAlignment(Pos.CENTER);
        carte.setPadding(new Insets(22, 34, 22, 34));
        carte.setMinWidth(160);
        String baseStyle =
            "-fx-background-color: rgba(255,255,255,0.07);" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + couleur + ";" +
            "-fx-border-width: 0 0 3 0;" +
            "-fx-border-radius: 14;";
        carte.setStyle(baseStyle);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(10);
        carte.setEffect(shadow);

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), carte);
        scaleUp.setToX(1.06);
        scaleUp.setToY(1.06);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), carte);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        carte.setOnMouseEntered(e -> {
            scaleUp.playFromStart();
            carte.setStyle(
                "-fx-background-color: rgba(255,255,255,0.12);" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: " + couleur + ";" +
                "-fx-border-width: 0 0 3 0;" +
                "-fx-border-radius: 14;"
            );
            shadow.setRadius(18);
        });
        carte.setOnMouseExited(e -> {
            scaleDown.playFromStart();
            carte.setStyle(baseStyle);
            shadow.setRadius(10);
        });

        Label lblValeur = new Label(valeur);
        lblValeur.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        lblValeur.setTextFill(Color.web(couleur));

        Label lblLabel  = new Label(label);
        lblLabel.setFont(Font.font("Segoe UI", 12));
        lblLabel.setTextFill(Color.web(COLOR_TEXT_GRAY));

        carte.getChildren().addAll(lblValeur, lblLabel);
        return carte;
    }

    // ================================================================
    //  VUE DOCUMENTS
    // ================================================================
    private void afficherVueDocuments() {
        contentArea.getChildren().clear();

        VBox vue = new VBox(15);
        vue.setPadding(new Insets(5));

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        Label titre = creerTitreSection("📖 Gestion des Documents");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnAjouter = creerBoutonAction("+ Ajouter", COLOR_VERT);
        btnAjouter.setOnAction(e -> ouvrirDialogueAjoutDocument());
        topBar.getChildren().addAll(titre, spacer, btnAjouter);

        TableView<Document> table = creerTableauStylise();

        TableColumn<Document, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getClass().getSimpleName()));
        colType.setPrefWidth(90);

        TableColumn<Document, String> colTitre = new TableColumn<>("Titre");
        colTitre.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getTitre()));
        colTitre.setPrefWidth(230);

        TableColumn<Document, String> colAuteur = new TableColumn<>("Auteur");
        colAuteur.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getAuteur()));
        colAuteur.setPrefWidth(160);

        TableColumn<Document, String> colPrix = new TableColumn<>("Prix (FCFA)");
        colPrix.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getPrix())));
        colPrix.setPrefWidth(100);

        TableColumn<Document, String> colExemp = new TableColumn<>("Exemplaires");
        colExemp.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getNombreExemplaires())));
        colExemp.setPrefWidth(100);

        TableColumn<Document, String> colDispo = new TableColumn<>("Statut");
        colDispo.setCellValueFactory(data -> {
            boolean dispo = bibliotheque.verifierDisponibilite(data.getValue());
            return new javafx.beans.property.SimpleStringProperty(
                dispo ? "✅ Disponible" : "❌ Indisponible");
        });
        colDispo.setPrefWidth(120);

        table.getColumns().addAll(colType, colTitre, colAuteur,
                                   colPrix, colExemp, colDispo);
        table.setItems(FXCollections.observableArrayList(
            bibliotheque.getDocuments()));
        VBox.setVgrow(table, Priority.ALWAYS);

        vue.getChildren().addAll(topBar, table);
        vue.setOpacity(0);
        contentArea.getChildren().add(vue);
        FadeTransition ft = new FadeTransition(Duration.millis(250), vue);
        ft.setToValue(1);
        ft.play();
    }

    private void ouvrirDialogueAjoutDocument() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un document");
        dialog.setHeaderText("Remplissez les informations du document");
        styliserDialog(dialog);

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(12);
        grid.setPadding(new Insets(20));

        ComboBox<String> cbType = new ComboBox<>();
        cbType.getItems().addAll("Livre", "Revue", "Article");
        cbType.setValue("Livre");
        cbType.setMaxWidth(Double.MAX_VALUE);

        TextField tfTitre  = creerChamp("Titre du document");
        TextField tfAuteur = creerChamp("Nom de l'auteur");
        TextField tfPrix   = creerChamp("Prix en FCFA");
        TextField tfExemp  = creerChamp("Nombre d'exemplaires");
        TextField tfExtra  = creerChamp("Nombre de pages");

        Label lblExtra = new Label("Nb pages :");
        lblExtra.setTextFill(Color.web(COLOR_TEXT_GRAY));

        cbType.setOnAction(e -> {
            if (cbType.getValue().equals("Livre")) {
                lblExtra.setText("Nb pages :");
                tfExtra.setPromptText("Nombre de pages");
            } else {
                lblExtra.setText("Période :");
                tfExtra.setPromptText("ex: decembre-2021");
            }
        });

        grid.add(styliserLabel("Type :"),        0, 0); grid.add(cbType,   1, 0);
        grid.add(styliserLabel("Titre :"),       0, 1); grid.add(tfTitre,  1, 1);
        grid.add(styliserLabel("Auteur :"),      0, 2); grid.add(tfAuteur, 1, 2);
        grid.add(styliserLabel("Prix (FCFA) :"), 0, 3); grid.add(tfPrix,   1, 3);
        grid.add(styliserLabel("Exemplaires :"), 0, 4); grid.add(tfExemp,  1, 4);
        grid.add(lblExtra,                       0, 5); grid.add(tfExtra,  1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(
            ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    String type   = cbType.getValue();
                    String titre  = tfTitre.getText().trim();
                    String auteur = tfAuteur.getText().trim();
                    double prix   = Double.parseDouble(tfPrix.getText().trim());
                    int    exemp  = Integer.parseInt(tfExemp.getText().trim());
                    String extra  = tfExtra.getText().trim();

                    if (titre.isEmpty() || auteur.isEmpty()) {
                        afficherAlerte("Erreur", "Titre et auteur obligatoires.");
                        return null;
                    }
                    switch (type) {
                        case "Livre":
                            bibliotheque.ajouterDocument(new Livre(titre, auteur,
                                prix, Integer.parseInt(extra), exemp)); break;
                        case "Revue":
                            bibliotheque.ajouterDocument(
                                new Revue(titre, auteur, prix, extra, exemp)); break;
                        case "Article":
                            bibliotheque.ajouterDocument(
                                new Article(titre, auteur, prix, extra, exemp)); break;
                    }
                    afficherVueDocuments();
                } catch (NumberFormatException ex) {
                    afficherAlerte("Erreur", "Vérifiez les champs numériques.");
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    // ================================================================
    //  VUE ADHÉRENTS
    // ================================================================
    private void afficherVueAdherents() {
        contentArea.getChildren().clear();
        VBox vue = new VBox(15);
        vue.setPadding(new Insets(5));

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        Label titre = creerTitreSection("👤 Gestion des Adhérents");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnAjouter = creerBoutonAction("+ Ajouter", COLOR_VERT);
        btnAjouter.setOnAction(e -> ouvrirDialogueAjoutAdherent());

        TextField tfSearch = creerChamp("🔍  Rechercher par matricule...");
        tfSearch.setPrefWidth(220);
        Button btnSearch = creerBoutonAction("Chercher", "#3498DB");

        topBar.getChildren().addAll(titre, spacer, tfSearch, btnSearch, btnAjouter);

        TableView<Adherent> table = creerTableauStylise();

        TableColumn<Adherent, String> colMat = new TableColumn<>("Matricule");
        colMat.setCellValueFactory(d ->
            new javafx.beans.property.SimpleStringProperty(d.getValue().getMatricule()));
        colMat.setPrefWidth(90);

        TableColumn<Adherent, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(d ->
            new javafx.beans.property.SimpleStringProperty(d.getValue().getNom()));
        colNom.setPrefWidth(120);

        TableColumn<Adherent, String> colPrenom = new TableColumn<>("Prénom");
        colPrenom.setCellValueFactory(d ->
            new javafx.beans.property.SimpleStringProperty(d.getValue().getPrenom()));
        colPrenom.setPrefWidth(120);

        TableColumn<Adherent, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(d ->
            new javafx.beans.property.SimpleStringProperty(d.getValue().getEmail()));
        colEmail.setPrefWidth(200);

        TableColumn<Adherent, String> colDocs = new TableColumn<>("Docs empruntés");
        colDocs.setCellValueFactory(d ->
            new javafx.beans.property.SimpleStringProperty(
                d.getValue().getDocumentsEmpruntes().size() + " / 3"));
        colDocs.setPrefWidth(110);

        TableColumn<Adherent, String> colAmende = new TableColumn<>("Amendes (FCFA)");
        colAmende.setCellValueFactory(d ->
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(d.getValue().getSoldeAmendes())));
        colAmende.setPrefWidth(120);

        table.getColumns().addAll(colMat, colNom, colPrenom,
                                   colEmail, colDocs, colAmende);
        ObservableList<Adherent> listeAdherents =
            FXCollections.observableArrayList(bibliotheque.getAdherents());
        table.setItems(listeAdherents);

        btnSearch.setOnAction(e -> {
            String mat = tfSearch.getText().trim();
            if (!mat.isEmpty()) {
                Adherent a = bibliotheque.rechercherAdherent(mat);
                if (a != null) {
                    table.setItems(FXCollections.observableArrayList(a));
                } else {
                    afficherAlerte("Introuvable",
                        "Aucun adhérent avec le matricule : " + mat);
                }
            } else {
                table.setItems(listeAdherents);
            }
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        vue.getChildren().addAll(topBar, table);
        vue.setOpacity(0);
        contentArea.getChildren().add(vue);
        FadeTransition ft = new FadeTransition(Duration.millis(250), vue);
        ft.setToValue(1);
        ft.play();
    }

    private void ouvrirDialogueAjoutAdherent() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Nouvel adhérent");
        dialog.setHeaderText("Enregistrement d'un nouvel adhérent");
        styliserDialog(dialog);

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(12);
        grid.setPadding(new Insets(20));

        TextField tfMat    = creerChamp("Ex: 006");
        TextField tfNom    = creerChamp("Nom de famille");
        TextField tfPrenom = creerChamp("Prénom");
        TextField tfEmail  = creerChamp("adresse@email.com");

        grid.add(styliserLabel("Matricule :"), 0, 0); grid.add(tfMat,    1, 0);
        grid.add(styliserLabel("Nom :"),       0, 1); grid.add(tfNom,    1, 1);
        grid.add(styliserLabel("Prénom :"),    0, 2); grid.add(tfPrenom, 1, 2);
        grid.add(styliserLabel("Email :"),     0, 3); grid.add(tfEmail,  1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(
            ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                String mat    = tfMat.getText().trim();
                String nom    = tfNom.getText().trim();
                String prenom = tfPrenom.getText().trim();
                String email  = tfEmail.getText().trim();
                if (mat.isEmpty() || nom.isEmpty()) {
                    afficherAlerte("Erreur", "Matricule et nom obligatoires.");
                    return null;
                }
                boolean ok = bibliotheque.enregistrerAdherent(
                    new Adherent(mat, nom, prenom, email));
                if (!ok) {
                    afficherAlerte("Doublon", "Le matricule " + mat + " existe deja.");
                    return null;
                }
                afficherVueAdherents();
            }
            return null;
        });
        dialog.showAndWait();
    }

    // ================================================================
    //  VUE EMPRUNTS
    // ================================================================
    private void afficherVueEmprunts() {
        contentArea.getChildren().clear();
        VBox vue = new VBox(15);
        vue.setPadding(new Insets(5));

        Label titre = creerTitreSection("🔄 Gestion des Emprunts & Retours");

        // Panneau Emprunt
        VBox paneEmprunt = creerPanneau("📥  Nouvel emprunt");
        GridPane gridE = new GridPane();
        gridE.setHgap(12); gridE.setVgap(12);
        gridE.setPadding(new Insets(15));

        TextField tfMatE   = creerChamp("Matricule de l'adhérent");
        TextField tfTitreE = creerChamp("Titre exact du document");
        Button btnEmprunter = creerBoutonAction("✅  Emprunter", COLOR_VERT);

        btnEmprunter.setOnAction(e -> {
            Adherent a = bibliotheque.rechercherAdherent(tfMatE.getText().trim());
            Document d = bibliotheque.rechercherDocument(tfTitreE.getText().trim());
            if (a == null) {
                afficherAlerte("Introuvable", "Aucun adherent avec ce matricule.");
            } else if (d == null) {
                afficherAlerte("Introuvable", "Aucun document avec ce titre.");
            } else {
                boolean ok = bibliotheque.enregistrerEmprunt(a, d);
                afficherInfo(ok ? "Emprunt enregistre avec succes !"
                                : "Emprunt refuse. Verifiez la disponibilite ou le nombre d'emprunts (max 3).");
            }
        });

        gridE.add(styliserLabel("Matricule :"), 0, 0); gridE.add(tfMatE,      1, 0);
        gridE.add(styliserLabel("Document :"),  0, 1); gridE.add(tfTitreE,    1, 1);
        gridE.add(btnEmprunter,                 1, 2);
        paneEmprunt.getChildren().add(gridE);

        // Panneau Retour
        VBox paneRetour = creerPanneau("📤  Retour de document");
        GridPane gridR = new GridPane();
        gridR.setHgap(12); gridR.setVgap(12);
        gridR.setPadding(new Insets(15));

        TextField tfMatR   = creerChamp("Matricule de l'adhérent");
        TextField tfTitreR = creerChamp("Titre du document à rendre");
        CheckBox cbAbime   = new CheckBox("Document abîmé");
        CheckBox cbRetard  = new CheckBox("Retard de retour");
        cbAbime.setTextFill(Color.web(COLOR_TEXT_GRAY));
        cbRetard.setTextFill(Color.web(COLOR_TEXT_GRAY));
        Button btnRendre = creerBoutonAction("🔙  Enregistrer le retour", COLOR_ROUGE);

        btnRendre.setOnAction(e -> {
            Adherent a = bibliotheque.rechercherAdherent(tfMatR.getText().trim());
            Document d = bibliotheque.rechercherDocument(tfTitreR.getText().trim());
            if (a == null) {
                afficherAlerte("Introuvable", "Aucun adherent avec ce matricule.");
            } else if (d == null) {
                afficherAlerte("Introuvable", "Aucun document avec ce titre.");
            } else {
                bibliotheque.gererRetour(a, d,
                    cbAbime.isSelected(), cbRetard.isSelected());
                afficherInfo("Retour enregistre avec succes !");
            }
        });

        gridR.add(styliserLabel("Matricule :"), 0, 0); gridR.add(tfMatR,   1, 0);
        gridR.add(styliserLabel("Document :"),  0, 1); gridR.add(tfTitreR, 1, 1);
        gridR.add(cbAbime,                      1, 2);
        gridR.add(cbRetard,                     1, 3);
        gridR.add(btnRendre,                    1, 4);
        paneRetour.getChildren().add(gridR);

        // Panneau Réservation
        VBox paneReserv = creerPanneau("🔖  Réserver un document");
        GridPane gridRes = new GridPane();
        gridRes.setHgap(12); gridRes.setVgap(12);
        gridRes.setPadding(new Insets(15));

        TextField tfMatRes   = creerChamp("Matricule");
        TextField tfTitreRes = creerChamp("Titre du document");
        Button btnReserver   = creerBoutonAction("📌  Réserver (3 jours)", "#9B59B6");

        btnReserver.setOnAction(e -> {
            Adherent a = bibliotheque.rechercherAdherent(tfMatRes.getText().trim());
            Document d = bibliotheque.rechercherDocument(tfTitreRes.getText().trim());
            if (a == null) {
                afficherAlerte("Introuvable", "Aucun adherent avec ce matricule.");
            } else if (d == null) {
                afficherAlerte("Introuvable", "Aucun document avec ce titre.");
            } else {
                boolean ok = bibliotheque.reserverDocument(a, d);
                afficherInfo(ok ? "Reservation effectuee (valable 3 jours) !"
                                : "Reservation refusee. Document indisponible ou deja reserve.");
            }
        });

        gridRes.add(styliserLabel("Matricule :"), 0, 0); gridRes.add(tfMatRes,   1, 0);
        gridRes.add(styliserLabel("Document :"),  0, 1); gridRes.add(tfTitreRes, 1, 1);
        gridRes.add(btnReserver,                  1, 2);
        paneReserv.getChildren().add(gridRes);

        HBox panneaux = new HBox(15);
        HBox.setHgrow(paneEmprunt, Priority.ALWAYS);
        HBox.setHgrow(paneRetour,  Priority.ALWAYS);
        HBox.setHgrow(paneReserv,  Priority.ALWAYS);
        panneaux.getChildren().addAll(paneEmprunt, paneRetour, paneReserv);

        vue.getChildren().addAll(titre, panneaux);
        vue.setOpacity(0);
        contentArea.getChildren().add(vue);
        FadeTransition ft = new FadeTransition(Duration.millis(250), vue);
        ft.setToValue(1);
        ft.play();
    }

    // ================================================================
    //  VUE AMENDES
    // ================================================================
    private void afficherVueAmendes() {
        contentArea.getChildren().clear();
        VBox vue = new VBox(15);
        vue.setPadding(new Insets(5));

        Label titre = creerTitreSection("💰 Gestion des Amendes");

        VBox panePayer = creerPanneau("💳  Payer une amende");
        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(12);
        grid.setPadding(new Insets(15));

        TextField tfMat     = creerChamp("Matricule de l'adhérent");
        TextField tfMontant = creerChamp("Montant en FCFA");
        Button btnPayer     = creerBoutonAction("✅  Valider le paiement", COLOR_VERT);

        btnPayer.setOnAction(e -> {
            Adherent a = bibliotheque.rechercherAdherent(tfMat.getText().trim());
            if (a == null) {
                afficherAlerte("Introuvable", "Aucun adherent avec ce matricule.");
            } else {
                try {
                    double montant = Double.parseDouble(tfMontant.getText().trim());
                    if (montant <= 0 || montant > a.getSoldeAmendes()) {
                        afficherAlerte("Erreur", "Montant invalide. Solde actuel : "
                                + a.getSoldeAmendes() + " FCFA");
                    } else {
                        bibliotheque.enregistrerAmendePaye(a, montant);
                        afficherInfo("Paiement enregistre !\nSolde restant : "
                                + a.getSoldeAmendes() + " FCFA");
                    }
                } catch (NumberFormatException ex) {
                    afficherAlerte("Erreur", "Montant invalide. Entrez un nombre.");
                }
            }
        });

        grid.add(styliserLabel("Matricule :"),    0, 0); grid.add(tfMat,     1, 0);
        grid.add(styliserLabel("Montant FCFA :"), 0, 1); grid.add(tfMontant, 1, 1);
        grid.add(btnPayer,                        1, 2);
        panePayer.getChildren().add(grid);

        // Historique taxes
        VBox paneHistorique = creerPanneau("📋  Historique des amendes");
        ListView<String> listTaxes = new ListView<>();
        listTaxes.setStyle(
            "-fx-background-color: rgba(255,255,255,0.05);" +
            "-fx-control-inner-background: transparent;" +
            "-fx-text-fill: white;"
        );
        listTaxes.setItems(FXCollections.observableArrayList(
            bibliotheque.getBaseDeDonnees().chargerTaxes()));
        listTaxes.setPrefHeight(180);
        paneHistorique.getChildren().add(listTaxes);
        VBox.setVgrow(paneHistorique, Priority.ALWAYS);

        vue.getChildren().addAll(titre, panePayer, paneHistorique);
        vue.setOpacity(0);
        contentArea.getChildren().add(vue);
        FadeTransition ft = new FadeTransition(Duration.millis(250), vue);
        ft.setToValue(1);
        ft.play();
    }

    // ================================================================
    //  COMPOSANTS UI RÉUTILISABLES
    // ================================================================

    private <T> TableView<T> creerTableauStylise() {
        TableView<T> table = new TableView<>();
        table.setStyle(
            "-fx-background-color: rgba(255,255,255,0.05);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: rgba(255,255,255,0.1);" +
            "-fx-border-radius: 10;" +
            "-fx-text-fill: white;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private VBox creerPanneau(String titreTexte) {
        VBox panneau = new VBox(10);
        panneau.setPadding(new Insets(18));
        String baseStyle =
            "-fx-background-color: rgba(255,255,255,0.07);" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: rgba(255,255,255,0.1);" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 1;";
        panneau.setStyle(baseStyle);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(8);
        panneau.setEffect(shadow);

        panneau.setOnMouseEntered(e -> {
            panneau.setStyle(
                "-fx-background-color: rgba(255,255,255,0.10);" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: rgba(255,255,255,0.15);" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 1;"
            );
            shadow.setRadius(14);
        });
        panneau.setOnMouseExited(e -> {
            panneau.setStyle(baseStyle);
            shadow.setRadius(8);
        });

        Label lbl = new Label(titreTexte);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lbl.setTextFill(Color.web(COLOR_VERT));

        Region separateur = new Region();
        separateur.setPrefHeight(1);
        separateur.setMaxWidth(Double.MAX_VALUE);
        separateur.setStyle("-fx-background-color: rgba(255,255,255,0.1);");

        panneau.getChildren().addAll(lbl, separateur);
        return panneau;
    }

    private Label creerTitreSection(String texte) {
        Label lbl = new Label(texte);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lbl.setTextFill(Color.WHITE);
        return lbl;
    }

    private Button creerBoutonFenetre(String texte) {
        Button btn = new Button(texte);
        btn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.15);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 2 10 2 10;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-min-width: 32;" +
            "-fx-min-height: 28;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.3);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 2 10 2 10;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-min-width: 32;" +
            "-fx-min-height: 28;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.15);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 2 10 2 10;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-min-width: 32;" +
            "-fx-min-height: 28;"
        ));
        return btn;
    }

    private Button creerBoutonAction(String texte, String couleur) {
        Button btn = new Button(texte);
        String baseStyle =
            "-fx-background-color: " + couleur + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 9 20 9 20;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;";
        btn.setStyle(baseStyle);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web(couleur, 0.4));
        shadow.setRadius(8);
        btn.setEffect(shadow);

        btn.setOnMouseEntered(e -> {
            btn.setStyle(baseStyle + "-fx-opacity: 0.85;");
            shadow.setRadius(14);
            btn.setScaleX(1.04);
            btn.setScaleY(1.04);
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle(baseStyle);
            shadow.setRadius(8);
            btn.setScaleX(1.0);
            btn.setScaleY(1.0);
        });
        btn.setOnMousePressed(e -> {
            btn.setScaleX(0.97);
            btn.setScaleY(0.97);
        });
        btn.setOnMouseReleased(e -> {
            btn.setScaleX(1.04);
            btn.setScaleY(1.04);
        });

        return btn;
    }

    private TextField creerChamp(String placeholder) {
        TextField tf = new TextField();
        tf.setPromptText(placeholder);
        String baseStyle =
            "-fx-background-color: rgba(255,255,255,0.1);" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: #6B7280;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 14 10 14;" +
            "-fx-border-color: rgba(255,255,255,0.15);" +
            "-fx-border-radius: 8;";
        tf.setStyle(baseStyle);
        tf.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                tf.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.12);" +
                    "-fx-text-fill: white;" +
                    "-fx-prompt-text-fill: #6B7280;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 10 14 10 14;" +
                    "-fx-border-color: " + COLOR_ACCENT + ";" +
                    "-fx-border-radius: 8;" +
                    "-fx-border-width: 1.5;"
                );
            } else {
                tf.setStyle(baseStyle);
            }
        });
        return tf;
    }

    private Label styliserLabel(String texte) {
        Label lbl = new Label(texte);
        lbl.setTextFill(Color.web(COLOR_TEXT_GRAY));
        lbl.setFont(Font.font("Segoe UI", 13));
        return lbl;
    }

    private void styliserDialog(Dialog<?> dialog) {
        dialog.getDialogPane().setStyle(
            "-fx-background-color: #1A1A2E;" +
            "-fx-border-color: rgba(255,255,255,0.1);"
        );
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}