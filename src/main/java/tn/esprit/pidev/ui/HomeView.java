package tn.esprit.pidev.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.Objects;

public class HomeView extends StackPane {
    public HomeView(AppNavigator navigator) {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        engine.setJavaScriptEnabled(true);

        URL homeUrl = Objects.requireNonNull(
            HomeView.class.getResource("/web/home.html"),
            "Missing home.html resource"
        );

        engine.locationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (newValue.startsWith("app://equipment")) {
                navigator.showEquipmentList();
                engine.load(homeUrl.toExternalForm());
            } else if (newValue.startsWith("app://animals")) {
                navigator.showAnimalsManagement();
                engine.load(homeUrl.toExternalForm());
            } else if (newValue.startsWith("app://stock")) {
                navigator.showStockManagement();
                engine.load(homeUrl.toExternalForm());
            } else if (newValue.startsWith("app://culture")) {
                navigator.showCultureManagement();
                engine.load(homeUrl.toExternalForm());
            } else if (newValue.startsWith("app://users")) {
                navigator.showUserManagement();
                engine.load(homeUrl.toExternalForm());
            } else if (newValue.startsWith("app://workers")) {
                navigator.showWorkerManagement();
                engine.load(homeUrl.toExternalForm());
            }
        });

        engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState != Worker.State.SUCCEEDED) {
                return;
            }
            JSObject window = (JSObject) engine.executeScript("window");
            window.setMember("javaBridge", new WebViewBridge(navigator, null));
            injectHomeHandlers(engine);
        });

        engine.load(homeUrl.toExternalForm());
        getChildren().add(webView);
    }

    private void injectHomeHandlers(WebEngine engine) {
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
                "})();"
            );
        } catch (Exception ignored) {
            // Ignore script errors during navigation or initial load.
        }
    }
}
