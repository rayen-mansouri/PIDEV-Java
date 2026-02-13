package tn.esprit.pidev.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.pidev.model.Equipment;
import tn.esprit.pidev.service.EquipmentService;

import java.sql.SQLException;
import java.util.Optional;

public class EquipmentFormView extends VBox {
    private final AppNavigator navigator;
    private final EquipmentService equipmentService;
    private final boolean isNew;
    private final int equipmentId;

    private final TextField nameField = new TextField();
    private final TextField typeField = new TextField();
    private final TextField statusField = new TextField();
    private final DatePicker purchaseDatePicker = new DatePicker();

    public EquipmentFormView(AppNavigator navigator, EquipmentService equipmentService, boolean isNew, int equipmentId) {
        this.navigator = navigator;
        this.equipmentService = equipmentService;
        this.isNew = isNew;
        this.equipmentId = equipmentId;

        getStyleClass().add("page");
        setSpacing(28);
        setPadding(new Insets(48, 24, 64, 24));

        HBox header = buildHeader();
        VBox card = buildFormCard();
        HBox actions = buildActions();

        getChildren().addAll(header, card, actions);
        loadEquipment();
    }

    private HBox buildHeader() {
        Label eyebrow = new Label(isNew ? "New Equipment" : "Edit Equipment");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label(isNew ? "Create a new equipment record." : "Update equipment details.");
        title.getStyleClass().add("page-title");
        title.setWrapText(true);

        VBox text = new VBox(8, eyebrow, title);

        HBox header = new HBox(20, text);
        header.getStyleClass().add("page-header");
        header.setAlignment(Pos.TOP_LEFT);
        return header;
    }

    private VBox buildFormCard() {
        nameField.setPromptText("Name");
        typeField.setPromptText("Type");
        statusField.setPromptText("Status");
        purchaseDatePicker.setPromptText("Purchase date");

        GridPane grid = new GridPane();
        grid.getStyleClass().add("form-grid");
        grid.setHgap(16);
        grid.setVgap(16);

        grid.add(buildField("Name", nameField), 0, 0);
        grid.add(buildField("Type", typeField), 1, 0);
        grid.add(buildField("Status", statusField), 0, 1);
        grid.add(buildField("Purchase Date", purchaseDatePicker), 1, 1);

        Button saveButton = new Button(isNew ? "Create" : "Update");
        saveButton.getStyleClass().addAll("btn", "primary");
        saveButton.setOnAction(event -> save());

        VBox form = new VBox(20, grid, saveButton);
        form.getStyleClass().add("form");

        VBox card = new VBox(form);
        card.getStyleClass().add("card");
        return card;
    }

    private HBox buildActions() {
        HBox actions = new HBox(12);
        actions.getStyleClass().add("inline-actions");

        if (!isNew) {
            Button deleteButton = new Button("Delete");
            deleteButton.getStyleClass().addAll("btn", "danger");
            deleteButton.setOnAction(event -> delete());
            actions.getChildren().add(deleteButton);
        }

        Hyperlink backLink = new Hyperlink("Back to list");
        backLink.getStyleClass().add("link");
        backLink.setOnAction(event -> navigator.showEquipmentList());
        actions.getChildren().add(backLink);

        return actions;
    }

    private VBox buildField(String label, javafx.scene.Node input) {
        Label fieldLabel = new Label(label);
        fieldLabel.getStyleClass().add("form-label");
        VBox wrapper = new VBox(6, fieldLabel, input);
        wrapper.getStyleClass().add("form-field");
        return wrapper;
    }

    private void loadEquipment() {
        if (isNew) {
            return;
        }
        try {
            Optional<Equipment> equipment = equipmentService.findById(equipmentId);
            if (equipment.isPresent()) {
                Equipment current = equipment.get();
                nameField.setText(current.getName());
                typeField.setText(current.getType());
                statusField.setText(current.getStatus());
                purchaseDatePicker.setValue(current.getPurchaseDate());
            } else {
                Dialogs.showError("Equipment not found", "The selected record is missing.");
                navigator.showEquipmentList();
            }
        } catch (SQLException e) {
            Dialogs.showError("Unable to load equipment", e.getMessage());
        }
    }

    private void save() {
        if (!validate()) {
            return;
        }
        Equipment equipment = new Equipment();
        equipment.setName(nameField.getText().trim());
        equipment.setType(typeField.getText().trim());
        equipment.setStatus(statusField.getText().trim());
        equipment.setPurchaseDate(purchaseDatePicker.getValue());

        try {
            if (isNew) {
                equipmentService.create(equipment);
            } else {
                equipment.setId(equipmentId);
                equipmentService.update(equipment);
            }
            navigator.showEquipmentList();
        } catch (SQLException e) {
            Dialogs.showError("Unable to save equipment", e.getMessage());
        }
    }

    private void delete() {
        if (!Dialogs.confirm("Delete equipment", "This will permanently remove the equipment.")) {
            return;
        }
        try {
            equipmentService.delete(equipmentId);
            navigator.showEquipmentList();
        } catch (SQLException e) {
            Dialogs.showError("Unable to delete equipment", e.getMessage());
        }
    }

    private boolean validate() {
        if (nameField.getText().trim().isEmpty()) {
            Dialogs.showError("Missing name", "Please enter a name.");
            return false;
        }
        if (typeField.getText().trim().isEmpty()) {
            Dialogs.showError("Missing type", "Please enter a type.");
            return false;
        }
        if (statusField.getText().trim().isEmpty()) {
            Dialogs.showError("Missing status", "Please enter a status.");
            return false;
        }
        return true;
    }
}
