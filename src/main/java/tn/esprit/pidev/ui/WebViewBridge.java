package tn.esprit.pidev.ui;

public class WebViewBridge {
    private final AppNavigator navigator;
    private final Runnable toggleAction;

    public WebViewBridge(AppNavigator navigator, Runnable toggleAction) {
        this.navigator = navigator;
        this.toggleAction = toggleAction;
    }

    public void navigate(String route) {
        if (route == null) {
            return;
        }
        switch (route.toLowerCase()) {
            case "home" -> navigator.showHome();
            case "equipment", "equipments" -> navigator.showEquipmentList();
            case "animals" -> navigator.showAnimalsManagement();
            case "stock" -> navigator.showStockManagement();
            case "culture" -> navigator.showCultureManagement();
            case "users" -> navigator.showUserManagement();
            case "workers" -> navigator.showWorkerManagement();
            default -> {
            }
        }
    }

    public void toggleSidebar() {
        if (toggleAction != null) {
            toggleAction.run();
        }
    }
}
