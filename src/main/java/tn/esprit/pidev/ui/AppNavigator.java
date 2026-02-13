package tn.esprit.pidev.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.pidev.service.EquipmentService;

public class AppNavigator {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 780;

    private final Stage stage;
    private final EquipmentService equipmentService;

    public AppNavigator(Stage stage, EquipmentService equipmentService) {
        this.stage = stage;
        this.equipmentService = equipmentService;
    }

    public void showHome() {
        HomeView view = new HomeView(this);
        show(view, "AgriSense 360");
    }

    public void showEquipmentList() {
        EquipmentListView view = new EquipmentListView(this, equipmentService);
        showWithSidebar(view, "Equipment Management");
    }

    public void showAnimalsManagement() {
        AnimalsManagementView view = new AnimalsManagementView(this);
        showWithSidebar(view, "Animals Management");
    }

    public void showStockManagement() {
        StockManagementView view = new StockManagementView(this);
        showWithSidebar(view, "Stock Management");
    }

    public void showCultureManagement() {
        CultureManagementView view = new CultureManagementView(this);
        showWithSidebar(view, "Culture Management");
    }

    public void showUserManagement() {
        UserManagementView view = new UserManagementView(this);
        showWithSidebar(view, "User Management");
    }

    public void showWorkerManagement() {
        WorkersManagementView view = new WorkersManagementView(this);
        showWithSidebar(view, "Workers Management");
    }

    public void showEquipmentForm(boolean isNew, int equipmentId) {
        EquipmentFormView view = new EquipmentFormView(this, equipmentService, isNew, equipmentId);
        showWithSidebar(view, isNew ? "Add Equipment" : "Edit Equipment");
    }

    public void showEquipmentDetails(int equipmentId) {
        EquipmentDetailsView view = new EquipmentDetailsView(this, equipmentService, equipmentId);
        showWithSidebar(view, "Equipment Details");
    }

    private void showWithSidebar(Parent content, String title) {
        AppShell shell = new AppShell(this, content);
        show(shell, title);
    }

    private void show(Parent root, String title) {
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        String stylesheet = AppNavigator.class.getResource("/styles/equipment.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
