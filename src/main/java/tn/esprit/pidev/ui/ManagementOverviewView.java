package tn.esprit.pidev.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManagementOverviewView extends VBox {
    private final AppNavigator navigator;

    public ManagementOverviewView(AppNavigator navigator, String eyebrowText, String headline, String summary) {
        this.navigator = navigator;

        getStyleClass().add("page");
        setSpacing(28);
        setPadding(new Insets(48, 24, 64, 24));

        HBox header = buildHeader(eyebrowText, headline);
        VBox card = buildCard(summary);

        Hyperlink backLink = new Hyperlink("Back to homepage");
        backLink.getStyleClass().add("link");
        backLink.setOnAction(event -> navigator.showHome());

        getChildren().addAll(header, card, backLink);
    }

    private HBox buildHeader(String eyebrowText, String headline) {
        Label eyebrow = new Label(eyebrowText);
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label(headline);
        title.getStyleClass().add("page-title");
        title.setWrapText(true);

        Label subtitle = new Label("Connect this module to live data and workflows when ready.");
        subtitle.getStyleClass().add("page-subtitle");
        subtitle.setWrapText(true);

        VBox text = new VBox(8, eyebrow, title, subtitle);

        HBox header = new HBox(20, text);
        header.getStyleClass().add("page-header");
        header.setAlignment(Pos.TOP_LEFT);
        return header;
    }

    private VBox buildCard(String summary) {
        Label cardTitle = new Label("Module overview");
        cardTitle.getStyleClass().add("detail-title");

        Label cardText = new Label(summary);
        cardText.getStyleClass().add("page-subtitle");
        cardText.setWrapText(true);

        VBox card = new VBox(12, cardTitle, cardText);
        card.getStyleClass().add("card");
        return card;
    }
}
