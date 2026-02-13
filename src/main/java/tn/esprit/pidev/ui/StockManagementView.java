package tn.esprit.pidev.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class StockManagementView extends VBox {
    private final AppNavigator navigator;

    public StockManagementView(AppNavigator navigator) {
        this.navigator = navigator;

        getStyleClass().add("page");
        setSpacing(28);
        setPadding(new Insets(48, 24, 64, 24));

        HBox header = buildHeader();
        VBox produitCard = buildProduitCard();
        VBox stockCard = buildStockCard();
        VBox locationCard = buildLocationCard();

        Hyperlink backLink = new Hyperlink("Back to homepage");
        backLink.getStyleClass().add("link");
        backLink.setOnAction(event -> navigator.showHome());

        getChildren().addAll(header, produitCard, stockCard, locationCard, backLink);
    }

    private HBox buildHeader() {
        Label eyebrow = new Label("Stock Management");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Stock, products, and storage");
        title.getStyleClass().add("page-title");
        title.setWrapText(true);

        Label subtitle = new Label("CRUD UI for Produit, Stock, and LieuStockage. Actions are visual only.");
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

    private VBox buildProduitCard() {
        TableView<ProduitRow> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ProduitRow, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<ProduitRow, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<ProduitRow, String> categoryColumn = new TableColumn<>("Categorie");
        categoryColumn.setCellValueFactory(data -> data.getValue().categoryProperty());

        TableColumn<ProduitRow, String> unitColumn = new TableColumn<>("Unite");
        unitColumn.setCellValueFactory(data -> data.getValue().unitProperty());

        table.getColumns().addAll(idColumn, nameColumn, categoryColumn, unitColumn);
        table.setItems(FXCollections.observableArrayList(
            new ProduitRow("#PR-014", "Fertilizer NPK", "Input", "kg"),
            new ProduitRow("#PR-021", "Sunflower Seeds", "Seed", "bag")
        ));

        VBox list = new VBox(12, buildListHeader("Search produits", "2 records"), table);

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(16);
        form.setVgap(16);

        addField(form, 0, 0, "Nom", new TextField());
        addField(form, 1, 0, "Categorie", new TextField());
        addField(form, 0, 1, "Unite", new TextField());

        VBox formBox = new VBox(12, new Label("Create / Edit produit"), form, buildFormActions());

        HBox grid = new HBox(24, list, formBox);
        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        return buildCard("Produit", "Manage product catalog and units.", "Add produit", grid);
    }

    private VBox buildStockCard() {
        TableView<StockRow> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StockRow, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<StockRow, String> quantityColumn = new TableColumn<>("QuantiteStockee");
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty());

        TableColumn<StockRow, String> thresholdColumn = new TableColumn<>("SeuilAlerte");
        thresholdColumn.setCellValueFactory(data -> data.getValue().thresholdProperty());

        TableColumn<StockRow, String> statusColumn = new TableColumn<>("Statut");
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        table.getColumns().addAll(idColumn, quantityColumn, thresholdColumn, statusColumn);
        table.setItems(FXCollections.observableArrayList(
            new StockRow("#ST-188", "320", "120", "Healthy"),
            new StockRow("#ST-204", "70", "100", "Low")
        ));

        VBox list = new VBox(12, buildListHeader("Search stock", "2 records"), table);

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(16);
        form.setVgap(16);

        addField(form, 0, 0, "Quantite stockee", new TextField());
        addField(form, 1, 0, "Seuil alerte", new TextField());
        ComboBox<String> statusSelect = new ComboBox<>(
            FXCollections.observableArrayList("Healthy", "Low", "Critical")
        );
        statusSelect.getSelectionModel().selectFirst();
        addField(form, 0, 1, "Statut", statusSelect);

        VBox formBox = new VBox(12, new Label("Create / Edit stock"), form, buildFormActions());

        HBox grid = new HBox(24, list, formBox);
        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        return buildCard("Stock", "Track quantities and alert thresholds.", "Add stock", grid);
    }

    private VBox buildLocationCard() {
        TableView<LocationRow> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<LocationRow, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<LocationRow, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<LocationRow, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(data -> data.getValue().typeProperty());

        TableColumn<LocationRow, String> capacityColumn = new TableColumn<>("CapaciteMax");
        capacityColumn.setCellValueFactory(data -> data.getValue().capacityProperty());

        TableColumn<LocationRow, String> locationColumn = new TableColumn<>("Localisation");
        locationColumn.setCellValueFactory(data -> data.getValue().locationProperty());

        table.getColumns().addAll(idColumn, nameColumn, typeColumn, capacityColumn, locationColumn);
        table.setItems(FXCollections.observableArrayList(
            new LocationRow("#LS-03", "Central Silo", "Grain", "900", "North Yard"),
            new LocationRow("#LS-11", "Cold Room", "Refrigerated", "120", "Processing Wing")
        ));

        VBox list = new VBox(12, buildListHeader("Search lieux", "2 records"), table);

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(16);
        form.setVgap(16);

        addField(form, 0, 0, "Nom", new TextField());
        addField(form, 1, 0, "Type", new TextField());
        addField(form, 0, 1, "Capacite max", new TextField());
        addField(form, 1, 1, "Localisation", new TextField());

        VBox formBox = new VBox(12, new Label("Create / Edit lieu"), form, buildFormActions());

        HBox grid = new HBox(24, list, formBox);
        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        return buildCard("LieuStockage", "Define storage locations and capacities.", "Add lieu", grid);
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

    private static class ProduitRow {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty category;
        private final SimpleStringProperty unit;

        private ProduitRow(String id, String name, String category, String unit) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.category = new SimpleStringProperty(category);
            this.unit = new SimpleStringProperty(unit);
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleStringProperty categoryProperty() {
            return category;
        }

        public SimpleStringProperty unitProperty() {
            return unit;
        }
    }

    private static class StockRow {
        private final SimpleStringProperty id;
        private final SimpleStringProperty quantity;
        private final SimpleStringProperty threshold;
        private final SimpleStringProperty status;

        private StockRow(String id, String quantity, String threshold, String status) {
            this.id = new SimpleStringProperty(id);
            this.quantity = new SimpleStringProperty(quantity);
            this.threshold = new SimpleStringProperty(threshold);
            this.status = new SimpleStringProperty(status);
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public SimpleStringProperty quantityProperty() {
            return quantity;
        }

        public SimpleStringProperty thresholdProperty() {
            return threshold;
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }

    private static class LocationRow {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty type;
        private final SimpleStringProperty capacity;
        private final SimpleStringProperty location;

        private LocationRow(String id, String name, String type, String capacity, String location) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.type = new SimpleStringProperty(type);
            this.capacity = new SimpleStringProperty(capacity);
            this.location = new SimpleStringProperty(location);
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

        public SimpleStringProperty capacityProperty() {
            return capacity;
        }

        public SimpleStringProperty locationProperty() {
            return location;
        }
    }
}
