package tn.esprit.pidev.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CultureManagementView extends VBox {
    private final AppNavigator navigator;

    public CultureManagementView(AppNavigator navigator) {
        this.navigator = navigator;

        getStyleClass().add("page");
        setSpacing(28);
        setPadding(new Insets(48, 24, 64, 24));

        HBox header = buildHeader();
        VBox parcelleCard = buildParcelleCard();
        VBox cultureCard = buildCultureCard();
        VBox traitementCard = buildTraitementCard();

        Hyperlink backLink = new Hyperlink("Back to homepage");
        backLink.getStyleClass().add("link");
        backLink.setOnAction(event -> navigator.showHome());

        getChildren().addAll(header, parcelleCard, cultureCard, traitementCard, backLink);
    }

    private HBox buildHeader() {
        Label eyebrow = new Label("Culture Management");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Crops, parcels, and treatments");
        title.getStyleClass().add("page-title");
        title.setWrapText(true);

        Label subtitle = new Label("CRUD UI for Parcelle, Culture, and Traitement. Actions are visual only.");
        subtitle.getStyleClass().add("page-subtitle");
        subtitle.setWrapText(true);

        VBox text = new VBox(8, eyebrow, title, subtitle);

        Button exportButton = new Button("Export");
        exportButton.getStyleClass().addAll("btn", "ghost");

        Button newButton = new Button("New record");
        newButton.getStyleClass().addAll("btn", "primary");

        HBox actions = new HBox(12, exportButton, newButton);
        actions.setAlignment(Pos.TOP_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(20, text, spacer, actions);
        header.getStyleClass().add("page-header");
        header.setAlignment(Pos.TOP_LEFT);
        return header;
    }

    private VBox buildParcelleCard() {
        TableView<ParcelleRow> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ParcelleRow, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<ParcelleRow, String> locationColumn = new TableColumn<>("Localisation");
        locationColumn.setCellValueFactory(data -> data.getValue().locationProperty());

        TableColumn<ParcelleRow, String> surfaceColumn = new TableColumn<>("Surface");
        surfaceColumn.setCellValueFactory(data -> data.getValue().surfaceProperty());

        TableColumn<ParcelleRow, String> soilColumn = new TableColumn<>("TypeSol");
        soilColumn.setCellValueFactory(data -> data.getValue().soilProperty());

        TableColumn<ParcelleRow, String> statusColumn = new TableColumn<>("Statut");
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        table.getColumns().addAll(idColumn, locationColumn, surfaceColumn, soilColumn, statusColumn);
        table.setItems(FXCollections.observableArrayList(
            new ParcelleRow("#PA-01", "East Field", "12.4 ha", "Loam", "Active"),
            new ParcelleRow("#PA-07", "South Ridge", "8.1 ha", "Clay", "Resting")
        ));

        VBox list = new VBox(12, buildListHeader("Search parcelles", "2 records"), table);

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(16);
        form.setVgap(16);

        addField(form, 0, 0, "Localisation", new TextField());
        addField(form, 1, 0, "Surface", new TextField());
        addField(form, 0, 1, "Type sol", new TextField());
        ComboBox<String> statusSelect = new ComboBox<>(
            FXCollections.observableArrayList("Active", "Resting", "Planned")
        );
        statusSelect.getSelectionModel().selectFirst();
        addField(form, 1, 1, "Statut", statusSelect);

        VBox formBox = new VBox(12, new Label("Create / Edit parcelle"), form, buildFormActions());

        HBox grid = new HBox(24, list, formBox);
        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        return buildCard("Parcelle", "Register parcels, soil type, and status.", "Add parcelle", grid);
    }

    private VBox buildCultureCard() {
        TableView<CultureRow> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<CultureRow, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<CultureRow, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<CultureRow, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(data -> data.getValue().typeProperty());

        TableColumn<CultureRow, String> plantedColumn = new TableColumn<>("DatePlantation");
        plantedColumn.setCellValueFactory(data -> data.getValue().plantedProperty());

        TableColumn<CultureRow, String> harvestColumn = new TableColumn<>("DateRecoltePrevue");
        harvestColumn.setCellValueFactory(data -> data.getValue().harvestProperty());

        TableColumn<CultureRow, String> areaColumn = new TableColumn<>("Superficie");
        areaColumn.setCellValueFactory(data -> data.getValue().areaProperty());

        TableColumn<CultureRow, String> statusColumn = new TableColumn<>("Etat");
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        table.getColumns().addAll(idColumn, nameColumn, typeColumn, plantedColumn, harvestColumn, areaColumn, statusColumn);
        table.setItems(FXCollections.observableArrayList(
            new CultureRow("#CU-16", "Wheat", "Cereal", "2025-10-01", "2026-05-20", "6.5 ha", "On track"),
            new CultureRow("#CU-19", "Sunflower", "Oilseed", "2025-04-15", "2025-09-10", "4.2 ha", "At risk")
        ));

        VBox list = new VBox(12, buildListHeader("Search cultures", "2 records"), table);

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(16);
        form.setVgap(16);

        addField(form, 0, 0, "Nom", new TextField());
        addField(form, 1, 0, "Type", new TextField());
        addField(form, 0, 1, "Date plantation", new TextField());
        addField(form, 1, 1, "Date recolte prevue", new TextField());
        addField(form, 0, 2, "Superficie", new TextField());
        ComboBox<String> statusSelect = new ComboBox<>(
            FXCollections.observableArrayList("On track", "At risk", "Harvested")
        );
        statusSelect.getSelectionModel().selectFirst();
        addField(form, 1, 2, "Etat", statusSelect);

        VBox formBox = new VBox(12, new Label("Create / Edit culture"), form, buildFormActions());

        HBox grid = new HBox(24, list, formBox);
        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        return buildCard("Culture", "Plan plantings, harvest dates, and surface area.", "Add culture", grid);
    }

    private VBox buildTraitementCard() {
        TableView<TraitementRow> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TraitementRow, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<TraitementRow, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<TraitementRow, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(data -> data.getValue().typeProperty());

        TableColumn<TraitementRow, String> doseColumn = new TableColumn<>("DoseRecommandee");
        doseColumn.setCellValueFactory(data -> data.getValue().doseProperty());

        TableColumn<TraitementRow, String> frequencyColumn = new TableColumn<>("Frequence");
        frequencyColumn.setCellValueFactory(data -> data.getValue().frequencyProperty());

        table.getColumns().addAll(idColumn, nameColumn, typeColumn, doseColumn, frequencyColumn);
        table.setItems(FXCollections.observableArrayList(
            new TraitementRow("#TR-03", "Fungicide A", "Protection", "2.5 L/ha", "Every 14 days"),
            new TraitementRow("#TR-09", "Foliar Feed", "Nutrition", "1.2 L/ha", "Every 21 days")
        ));

        VBox list = new VBox(12, buildListHeader("Search traitements", "2 records"), table);

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(16);
        form.setVgap(16);

        addField(form, 0, 0, "Nom", new TextField());
        addField(form, 1, 0, "Type", new TextField());
        addField(form, 0, 1, "Dose recommandee", new TextField());
        addField(form, 1, 1, "Frequence", new TextField());

        VBox formBox = new VBox(12, new Label("Create / Edit traitement"), form, buildFormActions());

        HBox grid = new HBox(24, list, formBox);
        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        return buildCard("Traitement", "Track recommended doses and frequency.", "Add traitement", grid);
    }

    private VBox buildCard(String title, String subtitle, String primaryLabel, Node content) {
        Label cardTitle = new Label(title);
        cardTitle.getStyleClass().add("page-title");

        Label cardSubtitle = new Label(subtitle);
        cardSubtitle.getStyleClass().add("page-subtitle");
        cardSubtitle.setWrapText(true);

        Button importButton = new Button("Import");
        importButton.getStyleClass().addAll("btn", "ghost");

        Button primaryButton = new Button(primaryLabel);
        primaryButton.getStyleClass().addAll("btn", "primary");

        HBox actions = new HBox(12, importButton, primaryButton);
        actions.getStyleClass().add("actions");

        VBox card = new VBox(18, cardTitle, cardSubtitle, actions, content);
        card.getStyleClass().add("card");
        return card;
    }

    private HBox buildListHeader(String placeholder, String countText) {
        TextField search = new TextField();
        search.setPromptText(placeholder);

        Label pill = new Label(countText);
        pill.getStyleClass().add("page-subtitle");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(12, search, spacer, pill);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private void addField(GridPane grid, int col, int row, String labelText, Node input) {
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");

        VBox field = new VBox(6, label, input);
        grid.add(field, col, row);
    }

    private HBox buildFormActions() {
        Button saveButton = new Button("Save");
        saveButton.getStyleClass().addAll("btn", "primary");

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().addAll("btn", "ghost");

        HBox actions = new HBox(12, saveButton, resetButton);
        actions.getStyleClass().add("actions");
        return actions;
    }

    private static class ParcelleRow {
        private final SimpleStringProperty id;
        private final SimpleStringProperty location;
        private final SimpleStringProperty surface;
        private final SimpleStringProperty soil;
        private final SimpleStringProperty status;

        private ParcelleRow(String id, String location, String surface, String soil, String status) {
            this.id = new SimpleStringProperty(id);
            this.location = new SimpleStringProperty(location);
            this.surface = new SimpleStringProperty(surface);
            this.soil = new SimpleStringProperty(soil);
            this.status = new SimpleStringProperty(status);
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public SimpleStringProperty locationProperty() {
            return location;
        }

        public SimpleStringProperty surfaceProperty() {
            return surface;
        }

        public SimpleStringProperty soilProperty() {
            return soil;
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }

    private static class CultureRow {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty type;
        private final SimpleStringProperty planted;
        private final SimpleStringProperty harvest;
        private final SimpleStringProperty area;
        private final SimpleStringProperty status;

        private CultureRow(String id, String name, String type, String planted, String harvest, String area, String status) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.type = new SimpleStringProperty(type);
            this.planted = new SimpleStringProperty(planted);
            this.harvest = new SimpleStringProperty(harvest);
            this.area = new SimpleStringProperty(area);
            this.status = new SimpleStringProperty(status);
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }

        public SimpleStringProperty plantedProperty() {
            return planted;
        }

        public SimpleStringProperty harvestProperty() {
            return harvest;
        }

        public SimpleStringProperty areaProperty() {
            return area;
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }

    private static class TraitementRow {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty type;
        private final SimpleStringProperty dose;
        private final SimpleStringProperty frequency;

        private TraitementRow(String id, String name, String type, String dose, String frequency) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.type = new SimpleStringProperty(type);
            this.dose = new SimpleStringProperty(dose);
            this.frequency = new SimpleStringProperty(frequency);
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }

        public SimpleStringProperty doseProperty() {
            return dose;
        }

        public SimpleStringProperty frequencyProperty() {
            return frequency;
        }
    }
}
