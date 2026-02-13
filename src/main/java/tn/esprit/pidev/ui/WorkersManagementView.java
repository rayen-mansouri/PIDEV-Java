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

public class WorkersManagementView extends VBox {
    private final AppNavigator navigator;

    public WorkersManagementView(AppNavigator navigator) {
        this.navigator = navigator;

        getStyleClass().add("page");
        setSpacing(28);
        setPadding(new Insets(48, 24, 64, 24));

        HBox header = buildHeader();
        VBox ouvrierCard = buildOuvrierCard();
        VBox affectationCard = buildAffectationCard();
        VBox paiementCard = buildPaiementCard();

        Hyperlink backLink = new Hyperlink("Back to homepage");
        backLink.getStyleClass().add("link");
        backLink.setOnAction(event -> navigator.showHome());

        getChildren().addAll(header, ouvrierCard, affectationCard, paiementCard, backLink);
    }

    private HBox buildHeader() {
        Label eyebrow = new Label("Workers Management");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Workers, assignments, and payments");
        title.getStyleClass().add("page-title");
        title.setWrapText(true);

        Label subtitle = new Label("CRUD UI for Ouvrier, Affectation, and Paiement. Actions are visual only.");
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

    private VBox buildOuvrierCard() {
        TableView<OuvrierRow> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<OuvrierRow, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<OuvrierRow, String> lastNameColumn = new TableColumn<>("Nom");
        lastNameColumn.setCellValueFactory(data -> data.getValue().lastNameProperty());

        TableColumn<OuvrierRow, String> firstNameColumn = new TableColumn<>("Prenom");
        firstNameColumn.setCellValueFactory(data -> data.getValue().firstNameProperty());

        TableColumn<OuvrierRow, String> roleColumn = new TableColumn<>("Poste");
        roleColumn.setCellValueFactory(data -> data.getValue().roleProperty());

        TableColumn<OuvrierRow, String> salaryColumn = new TableColumn<>("Salaire");
        salaryColumn.setCellValueFactory(data -> data.getValue().salaryProperty());

        TableColumn<OuvrierRow, String> availabilityColumn = new TableColumn<>("Disponibilite");
        availabilityColumn.setCellValueFactory(data -> data.getValue().availabilityProperty());

        table.getColumns().addAll(idColumn, lastNameColumn, firstNameColumn, roleColumn, salaryColumn, availabilityColumn);
        table.setItems(FXCollections.observableArrayList(
            new OuvrierRow("#OW-12", "Ben Ali", "Rim", "Field lead", "1200", "Available"),
            new OuvrierRow("#OW-18", "Jradi", "Omar", "Irrigation", "900", "Busy")
        ));

        VBox list = new VBox(12, buildListHeader("Search ouvriers", "2 records"), table);

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(16);
        form.setVgap(16);

        addField(form, 0, 0, "Nom", new TextField());
        addField(form, 1, 0, "Prenom", new TextField());
        addField(form, 0, 1, "Poste", new TextField());
        addField(form, 1, 1, "Salaire", new TextField());
        ComboBox<String> availabilitySelect = new ComboBox<>(
            FXCollections.observableArrayList("Available", "Busy", "On leave")
        );
        availabilitySelect.getSelectionModel().selectFirst();
        addField(form, 0, 2, "Disponibilite", availabilitySelect);

        VBox formBox = new VBox(12, new Label("Create / Edit ouvrier"), form, buildFormActions());

        HBox grid = new HBox(24, list, formBox);
        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        return buildCard("Ouvrier", "Track workforce roles and availability.", "Add ouvrier", grid);
    }

    private VBox buildAffectationCard() {
        TableView<AffectationRow> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AffectationRow, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<AffectationRow, String> taskColumn = new TableColumn<>("Tache");
        taskColumn.setCellValueFactory(data -> data.getValue().taskProperty());

        TableColumn<AffectationRow, String> startColumn = new TableColumn<>("DateDebut");
        startColumn.setCellValueFactory(data -> data.getValue().startProperty());

        TableColumn<AffectationRow, String> endColumn = new TableColumn<>("DateFin");
        endColumn.setCellValueFactory(data -> data.getValue().endProperty());

        table.getColumns().addAll(idColumn, taskColumn, startColumn, endColumn);
        table.setItems(FXCollections.observableArrayList(
            new AffectationRow("#AF-07", "Soil prep", "2026-02-01", "2026-02-08"),
            new AffectationRow("#AF-09", "Harvest crew", "2026-03-12", "2026-03-18")
        ));

        VBox list = new VBox(12, buildListHeader("Search affectations", "2 records"), table);

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(16);
        form.setVgap(16);

        addField(form, 0, 0, "Tache", new TextField());
        addField(form, 1, 0, "Date debut", new TextField());
        addField(form, 0, 1, "Date fin", new TextField());

        VBox formBox = new VBox(12, new Label("Create / Edit affectation"), form, buildFormActions());

        HBox grid = new HBox(24, list, formBox);
        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        return buildCard("Affectation", "Assign tasks with start and end dates.", "Add affectation", grid);
    }

    private VBox buildPaiementCard() {
        TableView<PaiementRow> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PaiementRow, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<PaiementRow, String> amountColumn = new TableColumn<>("Montant");
        amountColumn.setCellValueFactory(data -> data.getValue().amountProperty());

        TableColumn<PaiementRow, String> dateColumn = new TableColumn<>("DatePaiement");
        dateColumn.setCellValueFactory(data -> data.getValue().dateProperty());

        TableColumn<PaiementRow, String> statusColumn = new TableColumn<>("Statut");
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        table.getColumns().addAll(idColumn, amountColumn, dateColumn, statusColumn);
        table.setItems(FXCollections.observableArrayList(
            new PaiementRow("#PM-04", "420", "2026-01-28", "Paid"),
            new PaiementRow("#PM-11", "380", "2026-02-05", "Pending")
        ));

        VBox list = new VBox(12, buildListHeader("Search paiements", "2 records"), table);

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(16);
        form.setVgap(16);

        addField(form, 0, 0, "Montant", new TextField());
        addField(form, 1, 0, "Date paiement", new TextField());
        ComboBox<String> statusSelect = new ComboBox<>(
            FXCollections.observableArrayList("Paid", "Pending", "Failed")
        );
        statusSelect.getSelectionModel().selectFirst();
        addField(form, 0, 1, "Statut", statusSelect);

        VBox formBox = new VBox(12, new Label("Create / Edit paiement"), form, buildFormActions());

        HBox grid = new HBox(24, list, formBox);
        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        return buildCard("Paiement", "Payment history and status tracking.", "Add paiement", grid);
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

    private static class OuvrierRow {
        private final SimpleStringProperty id;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty role;
        private final SimpleStringProperty salary;
        private final SimpleStringProperty availability;

        private OuvrierRow(String id, String lastName, String firstName, String role, String salary, String availability) {
            this.id = new SimpleStringProperty(id);
            this.lastName = new SimpleStringProperty(lastName);
            this.firstName = new SimpleStringProperty(firstName);
            this.role = new SimpleStringProperty(role);
            this.salary = new SimpleStringProperty(salary);
            this.availability = new SimpleStringProperty(availability);
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public SimpleStringProperty lastNameProperty() {
            return lastName;
        }

        public SimpleStringProperty firstNameProperty() {
            return firstName;
        }

        public SimpleStringProperty roleProperty() {
            return role;
        }

        public SimpleStringProperty salaryProperty() {
            return salary;
        }

        public SimpleStringProperty availabilityProperty() {
            return availability;
        }
    }

    private static class AffectationRow {
        private final SimpleStringProperty id;
        private final SimpleStringProperty task;
        private final SimpleStringProperty start;
        private final SimpleStringProperty end;

        private AffectationRow(String id, String task, String start, String end) {
            this.id = new SimpleStringProperty(id);
            this.task = new SimpleStringProperty(task);
            this.start = new SimpleStringProperty(start);
            this.end = new SimpleStringProperty(end);
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public SimpleStringProperty taskProperty() {
            return task;
        }

        public SimpleStringProperty startProperty() {
            return start;
        }

        public SimpleStringProperty endProperty() {
            return end;
        }
    }

    private static class PaiementRow {
        private final SimpleStringProperty id;
        private final SimpleStringProperty amount;
        private final SimpleStringProperty date;
        private final SimpleStringProperty status;

        private PaiementRow(String id, String amount, String date, String status) {
            this.id = new SimpleStringProperty(id);
            this.amount = new SimpleStringProperty(amount);
            this.date = new SimpleStringProperty(date);
            this.status = new SimpleStringProperty(status);
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public SimpleStringProperty amountProperty() {
            return amount;
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }
}
