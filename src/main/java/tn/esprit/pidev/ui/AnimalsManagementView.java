package tn.esprit.pidev.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AnimalsManagementView extends VBox {
    private final AppNavigator navigator;

    public AnimalsManagementView(AppNavigator navigator) {
        this.navigator = navigator;

        getStyleClass().add("page");
        setSpacing(28);
        setPadding(new Insets(48, 24, 64, 24));

        HBox header = buildHeader();
        VBox card = buildPlaceholderCard();

        Hyperlink backLink = new Hyperlink("Back to homepage");
        backLink.getStyleClass().add("link");
        backLink.setOnAction(event -> navigator.showHome());

        getChildren().addAll(header, card, backLink);
    }

    private HBox buildHeader() {
        Label eyebrow = new Label("Animals Management");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Animals management UI placeholder");
        title.getStyleClass().add("page-title");
        title.setWrapText(true);

        Label subtitle = new Label("No animal tables were provided in the schema images. This is a UI-only placeholder.");
        subtitle.getStyleClass().add("page-subtitle");
        subtitle.setWrapText(true);

        VBox text = new VBox(8, eyebrow, title, subtitle);

        Button exportButton = new Button("Export");
        exportButton.getStyleClass().addAll("btn", "ghost");

        Button newButton = new Button("New record");
        newButton.getStyleClass().addAll("btn", "primary");
        newButton.setDisable(true);

        HBox actions = new HBox(12, exportButton, newButton);
        actions.setAlignment(Pos.TOP_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(20, text, spacer, actions);
        header.getStyleClass().add("page-header");
        header.setAlignment(Pos.TOP_LEFT);
        return header;
    }

    private VBox buildPlaceholderCard() {
        Label cardTitle = new Label("Animals");
        cardTitle.getStyleClass().add("page-title");

        Label cardSubtitle = new Label("Add tables to enable CRUD UI for livestock, health, and productivity.");
        cardSubtitle.getStyleClass().add("page-subtitle");
        cardSubtitle.setWrapText(true);

        Button importButton = new Button("Import");
        importButton.getStyleClass().addAll("btn", "ghost");

        Button newAnimalButton = new Button("New animal");
        newAnimalButton.getStyleClass().addAll("btn", "primary");
        newAnimalButton.setDisable(true);

        HBox actions = new HBox(12, importButton, newAnimalButton);
        actions.getStyleClass().add("actions");

        Label emptyTitle = new Label("No tables configured");
        emptyTitle.getStyleClass().add("page-title");

        Label emptyText = new Label("Add an animal-related table (ex: Animal, Sante, Vaccination) to generate a full CRUD UI.");
        emptyText.getStyleClass().add("page-subtitle");
        emptyText.setWrapText(true);

        Button createButton = new Button("Create first table");
        createButton.getStyleClass().addAll("btn", "primary");

        Button requestButton = new Button("Request schema update");
        requestButton.getStyleClass().addAll("btn", "ghost");

        HBox emptyActions = new HBox(12, createButton, requestButton);
        emptyActions.getStyleClass().add("actions");

        VBox emptyState = new VBox(12, emptyTitle, emptyText, emptyActions);

        VBox card = new VBox(16, cardTitle, cardSubtitle, actions, emptyState);
        card.getStyleClass().add("card");
        return card;
    }
}
