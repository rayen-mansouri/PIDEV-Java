package tn.esprit.pidev.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tn.esprit.pidev.model.Equipment;
import tn.esprit.pidev.service.EquipmentService;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class EquipmentListView extends VBox {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final AppNavigator navigator;
    private final EquipmentService equipmentService;
    private final TableView<Equipment> tableView = new TableView<>();

    public EquipmentListView(AppNavigator navigator, EquipmentService equipmentService) {
        this.navigator = navigator;
        this.equipmentService = equipmentService;

        getStyleClass().add("page");
        setSpacing(28);
        setPadding(new Insets(48, 24, 64, 24));

        HBox header = buildHeader();
        VBox card = buildTableCard();

        Hyperlink backLink = new Hyperlink("Back to homepage");
        backLink.getStyleClass().add("link");
        backLink.setOnAction(event -> navigator.showHome());

        getChildren().addAll(header, card, backLink);
        loadData();
    }

    private HBox buildHeader() {
        Label eyebrow = new Label("Equipment Management");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Track, update, and maintain your farm equipment.");
        title.getStyleClass().add("page-title");
        title.setWrapText(true);

        VBox text = new VBox(8, eyebrow, title);

        Button addButton = new Button("Add equipment");
        addButton.getStyleClass().addAll("btn", "primary");
        addButton.setOnAction(event -> navigator.showEquipmentForm(true, -1));

        HBox header = new HBox(20, text, addButton);
        header.getStyleClass().add("page-header");
        header.setAlignment(Pos.TOP_LEFT);
        return header;
    }

    private VBox buildTableCard() {
        tableView.getStyleClass().add("table-view");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(buildEmptyLabel());

        TableColumn<Equipment, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());

        TableColumn<Equipment, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Equipment, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));

        TableColumn<Equipment, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        TableColumn<Equipment, String> dateColumn = new TableColumn<>("Purchase Date");
        dateColumn.setCellValueFactory(data -> {
            if (data.getValue().getPurchaseDate() == null) {
                return new SimpleStringProperty("-");
            }
            return new SimpleStringProperty(data.getValue().getPurchaseDate().format(DATE_FORMAT));
        });

        TableColumn<Equipment, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(buildActionsCell());

        tableView.getColumns().addAll(idColumn, nameColumn, typeColumn, statusColumn, dateColumn, actionsColumn);

        VBox card = new VBox(tableView);
        card.getStyleClass().add("card");
        return card;
    }

    private Callback<TableColumn<Equipment, Void>, TableCell<Equipment, Void>> buildActionsCell() {
        return column -> new TableCell<>() {
            private final Button viewButton = new Button("View");
            private final Button editButton = new Button("Edit");
            private final HBox actions = new HBox(10, viewButton, editButton);

            {
                actions.getStyleClass().add("actions");
                viewButton.getStyleClass().addAll("btn", "ghost");
                editButton.getStyleClass().addAll("btn", "ghost");

                viewButton.setOnAction(event -> {
                    Equipment equipment = getTableView().getItems().get(getIndex());
                    navigator.showEquipmentDetails(equipment.getId());
                });
                editButton.setOnAction(event -> {
                    Equipment equipment = getTableView().getItems().get(getIndex());
                    navigator.showEquipmentForm(false, equipment.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actions);
                }
            }
        };
    }

    private Label buildEmptyLabel() {
        Label empty = new Label("No equipment yet.");
        empty.getStyleClass().add("empty");
        return empty;
    }

    private void loadData() {
        try {
            ObservableList<Equipment> items = FXCollections.observableArrayList(equipmentService.findAll());
            tableView.setItems(items);
        } catch (SQLException e) {
            Dialogs.showError("Unable to load equipment", e.getMessage());
        }
    }
}
