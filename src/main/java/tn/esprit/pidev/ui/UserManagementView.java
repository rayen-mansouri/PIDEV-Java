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

public class UserManagementView extends VBox {
    private final AppNavigator navigator;

    public UserManagementView(AppNavigator navigator) {
        this.navigator = navigator;

        getStyleClass().add("page");
        setSpacing(28);
        setPadding(new Insets(48, 24, 64, 24));

        HBox header = buildHeader();
        VBox userCard = buildUserCard();
        VBox roleCard = buildRoleCard();

        Hyperlink backLink = new Hyperlink("Back to homepage");
        backLink.getStyleClass().add("link");
        backLink.setOnAction(event -> navigator.showHome());

        getChildren().addAll(header, userCard, roleCard, backLink);
    }

    private HBox buildHeader() {
        Label eyebrow = new Label("User Management");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Users, access, and roles");
        title.getStyleClass().add("page-title");
        title.setWrapText(true);

        Label subtitle = new Label("CRUD UI for User and Role. Actions are visual only.");
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

    private VBox buildUserCard() {
        TableView<UserRow> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<UserRow, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<UserRow, String> lastNameColumn = new TableColumn<>("Nom");
        lastNameColumn.setCellValueFactory(data -> data.getValue().lastNameProperty());

        TableColumn<UserRow, String> firstNameColumn = new TableColumn<>("Prenom");
        firstNameColumn.setCellValueFactory(data -> data.getValue().firstNameProperty());

        TableColumn<UserRow, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> data.getValue().emailProperty());

        TableColumn<UserRow, String> statusColumn = new TableColumn<>("Statut");
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        table.getColumns().addAll(idColumn, lastNameColumn, firstNameColumn, emailColumn, statusColumn);
        table.setItems(FXCollections.observableArrayList(
            new UserRow("#US-01", "Hamdi", "Leila", "leila@farm.tn", "Active"),
            new UserRow("#US-07", "Gharbi", "Youssef", "youssef@farm.tn", "Pending")
        ));

        VBox list = new VBox(12, buildListHeader("Search users", "2 records"), table);

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(16);
        form.setVgap(16);

        addField(form, 0, 0, "Nom", new TextField());
        addField(form, 1, 0, "Prenom", new TextField());
        addField(form, 0, 1, "Email", new TextField());
        addField(form, 1, 1, "Mot de passe", new TextField());
        ComboBox<String> statusSelect = new ComboBox<>(
            FXCollections.observableArrayList("Active", "Pending", "Suspended")
        );
        statusSelect.getSelectionModel().selectFirst();
        addField(form, 0, 2, "Statut", statusSelect);

        VBox formBox = new VBox(12, new Label("Create / Edit user"), form, buildFormActions());

        HBox grid = new HBox(24, list, formBox);
        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        return buildCard("User", "Manage accounts, email, and status.", "Add user", grid);
    }

    private VBox buildRoleCard() {
        TableView<RoleRow> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<RoleRow, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<RoleRow, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<RoleRow, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(data -> data.getValue().descriptionProperty());

        table.getColumns().addAll(idColumn, nameColumn, descriptionColumn);
        table.setItems(FXCollections.observableArrayList(
            new RoleRow("#RL-01", "Admin", "Full access to management"),
            new RoleRow("#RL-03", "Supervisor", "Read/write for operational teams")
        ));

        VBox list = new VBox(12, buildListHeader("Search roles", "2 records"), table);

        GridPane form = new GridPane();
        form.getStyleClass().add("form-grid");
        form.setHgap(16);
        form.setVgap(16);

        addField(form, 0, 0, "Nom", new TextField());
        addField(form, 1, 0, "Description", new TextField());

        VBox formBox = new VBox(12, new Label("Create / Edit role"), form, buildFormActions());

        HBox grid = new HBox(24, list, formBox);
        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        return buildCard("Role", "Define permissions and descriptions.", "Add role", grid);
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

    private static class UserRow {
        private final SimpleStringProperty id;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty email;
        private final SimpleStringProperty status;

        private UserRow(String id, String lastName, String firstName, String email, String status) {
            this.id = new SimpleStringProperty(id);
            this.lastName = new SimpleStringProperty(lastName);
            this.firstName = new SimpleStringProperty(firstName);
            this.email = new SimpleStringProperty(email);
            this.status = new SimpleStringProperty(status);
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

        public SimpleStringProperty emailProperty() {
            return email;
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }

    private static class RoleRow {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty description;

        private RoleRow(String id, String name, String description) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.description = new SimpleStringProperty(description);
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleStringProperty descriptionProperty() {
            return description;
        }
    }
}
