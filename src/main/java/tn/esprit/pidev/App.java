package tn.esprit.pidev;

import javafx.application.Application;
import javafx.stage.Stage;
import tn.esprit.pidev.service.EquipmentService;
import tn.esprit.pidev.ui.AppNavigator;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        EquipmentService equipmentService = new EquipmentService();
        AppNavigator navigator = new AppNavigator(stage, equipmentService);
        navigator.showHome();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
