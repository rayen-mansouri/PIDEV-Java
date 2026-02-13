package tn.esprit.pidev.ui;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.Objects;

public class AppShell extends BorderPane {
    private static final double SIDEBAR_WIDTH = 300;
    private static final double SIDEBAR_COLLAPSED_WIDTH = 96;

    private final WebView sidebarView;
    private final WebEngine engine;
    private final URL sidebarUrl;
    private boolean sidebarCollapsed = false;

    public AppShell(AppNavigator navigator, Node content) {
        sidebarView = new WebView();
        setSidebarWidth(SIDEBAR_WIDTH);

        engine = sidebarView.getEngine();
        engine.setJavaScriptEnabled(true);
        sidebarUrl = Objects.requireNonNull(
            AppShell.class.getResource("/web/sidebar.html"),
            "Missing sidebar.html resource"
        );

        engine.locationProperty().addListener((observable, oldValue, newValue) -> {
            handleNavigation(navigator, newValue);
        });

        engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState != Worker.State.SUCCEEDED) {
                return;
            }
            JSObject window = (JSObject) engine.executeScript("window");
            window.setMember("javaBridge", new WebViewBridge(navigator, this::toggleSidebar));
            injectSidebarHandlers();
            applySidebarState();
        });

        engine.load(sidebarUrl.toExternalForm());
        setLeft(sidebarView);
        setCenter(content);
    }

    private void handleNavigation(AppNavigator navigator, String newValue) {
        if (newValue == null) {
            return;
        }
        if (newValue.startsWith("app://toggle")) {
            toggleSidebar();
            engine.load(sidebarUrl.toExternalForm());
            return;
        }
        if (newValue.startsWith("app://home")) {
            navigator.showHome();
            engine.load(sidebarUrl.toExternalForm());
            return;
        }
        if (newValue.startsWith("app://equipment")) {
            navigator.showEquipmentList();
            engine.load(sidebarUrl.toExternalForm());
            return;
        }
        if (newValue.startsWith("app://animals")) {
            navigator.showAnimalsManagement();
            engine.load(sidebarUrl.toExternalForm());
            return;
        }
        if (newValue.startsWith("app://stock")) {
            navigator.showStockManagement();
            engine.load(sidebarUrl.toExternalForm());
            return;
        }
        if (newValue.startsWith("app://culture")) {
            navigator.showCultureManagement();
            engine.load(sidebarUrl.toExternalForm());
            return;
        }
        if (newValue.startsWith("app://users")) {
            navigator.showUserManagement();
            engine.load(sidebarUrl.toExternalForm());
            return;
        }
        if (newValue.startsWith("app://workers")) {
            navigator.showWorkerManagement();
            engine.load(sidebarUrl.toExternalForm());
        }
    }

    private void toggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        setSidebarWidth(sidebarCollapsed ? SIDEBAR_COLLAPSED_WIDTH : SIDEBAR_WIDTH);
        applySidebarState();
    }

    private void applySidebarState() {
        try {
            engine.executeScript(
                "document.body.classList.toggle('sidebar-collapsed', " + sidebarCollapsed + ");"
            );
        } catch (Exception ignored) {
            // Ignore script errors during navigation or initial load.
        }
    }

    private void injectSidebarHandlers() {
        try {
            engine.executeScript(
                "(function(){" +
                "function routeFromHref(href){" +
                "  if(!href){return null;}" +
                "  if(href.indexOf('app://')!==0){return null;}" +
                "  return href.replace('app://','');" +
                "}" +
                "var links=document.querySelectorAll('a.nav-link');" +
                "links.forEach(function(link){" +
                "  link.addEventListener('click',function(evt){" +
                "    if(!window.javaBridge){return;}" +
                "    var route=routeFromHref(link.getAttribute('href'));" +
                "    if(route){window.javaBridge.navigate(route);evt.preventDefault();}" +
                "  });" +
                "});" +
                "var toggle=document.querySelector('.sidebar-toggle');" +
                "if(toggle){" +
                "  toggle.addEventListener('click',function(evt){" +
                "    if(window.javaBridge){window.javaBridge.toggleSidebar();evt.preventDefault();}" +
                "  });" +
                "}" +
                "})();"
            );
        } catch (Exception ignored) {
            // Ignore script errors during navigation or initial load.
        }
    }

    private void setSidebarWidth(double width) {
        sidebarView.setPrefWidth(width);
        sidebarView.setMinWidth(width);
        sidebarView.setMaxWidth(width);
    }
}
