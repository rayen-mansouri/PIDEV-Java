package tn.esprit.pidev.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.pidev.model.Equipment;
import tn.esprit.pidev.service.EquipmentService;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class EquipmentDetailsView extends VBox {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final AppNavigator navigator;
    private final EquipmentService equipmentService;
    private final int equipmentId;

    public EquipmentDetailsView(AppNavigator navigator, EquipmentService equipmentService, int equipmentId) {
        this.navigator = navigator;
        this.equipmentService = equipmentService;
        this.equipmentId = equipmentId;

        getStyleClass().add("page");
        setSpacing(28);
        setPadding(new Insets(48, 24, 64, 24));

        loadDetails();
    }

    private void loadDetails() {
        try {
            Optional<Equipment> equipment = equipmentService.findById(equipmentId);
            if (equipment.isEmpty()) {
                Dialogs.showError("Equipment not found", "The selected record is missing.");
                navigator.showEquipmentList();
                return;
            }
            Equipment current = equipment.get();

            HBox header = buildHeader(current);
            VBox card = buildDetailsCard(current);
            Hyperlink back = new Hyperlink("Back to list");
            back.getStyleClass().add("link");
            back.setOnAction(event -> navigator.showEquipmentList());

            getChildren().setAll(header, card, back);
        } catch (SQLException e) {
            Dialogs.showError("Unable to load equipment", e.getMessage());
        }
    }

    private HBox buildHeader(Equipment equipment) {
        Label eyebrow = new Label("Equipment Details");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label(equipment.getName());
        title.getStyleClass().add("page-title");

        VBox text = new VBox(8, eyebrow, title);

        Button editButton = new Button("Edit");
        editButton.getStyleClass().addAll("btn", "ghost");
        editButton.setOnAction(event -> navigator.showEquipmentForm(false, equipmentId));

        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().addAll("btn", "danger");
        deleteButton.setOnAction(event -> delete());

        HBox actions = new HBox(12, editButton, deleteButton);
        actions.getStyleClass().add("actions");

        HBox header = new HBox(20, text, actions);
        header.getStyleClass().add("page-header");
        header.setAlignment(Pos.TOP_LEFT);
        return header;
    }

    private VBox buildDetailsCard(Equipment equipment) {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("details");
        grid.setHgap(18);
        grid.setVgap(18);

        grid.add(detailBlock("ID", String.valueOf(equipment.getId())), 0, 0);
        grid.add(detailBlock("Name", equipment.getName()), 1, 0);
        grid.add(detailBlock("Type", equipment.getType()), 0, 1);
        grid.add(detailBlock("Status", equipment.getStatus()), 1, 1);

        String dateText = equipment.getPurchaseDate() == null ? "-" : equipment.getPurchaseDate().format(DATE_FORMAT);
        grid.add(detailBlock("Purchase Date", dateText), 0, 2);

        VBox card = new VBox(grid);
        card.getStyleClass().add("card");
        return card;
    }

    private VBox detailBlock(String title, String value) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("detail-title");
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("detail-value");
        return new VBox(6, titleLabel, valueLabel);
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
}
