package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Label;

import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;


public class HomeScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private HBox panel;
    @FXML
    private ScrollPane scroll;

    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void createList() {
        drawCardList("Label");
        server.addCardListToBoard(new CardList(new ArrayList<>(), "Label"));
    }

    public void drawCardList(String text) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX(21.0);
        vbox.setLayoutY(93.0);
        vbox.setPrefHeight(36.0);
        vbox.setPrefWidth(126.0);
        vbox.setStyle("-fx-background-color: #d9cdad; -fx-border-color: black;");
        HBox hbox = new HBox();
        Region region = new Region();
        Insets insets = new Insets(0, 20.0, 0, 0);
        region.setLayoutX(63.0);
        region.setLayoutY(10.0);
        region.setPadding(insets);
        Label label = new Label();
        Font font = new Font(16.0);
        label.setFont(font);
        label.setText(text);
        label.setAlignment(Pos.TOP_CENTER);
        Region region2 = new Region();
        Insets insets2 = new Insets(0, 5.0, 0, 0);
        region2.setPadding(insets2);
        Button button = new Button(":");
        button.setAlignment(Pos.TOP_CENTER);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setMnemonicParsing(false);
        button.setStyle("-fx-background-color: #a3957c;");
        Font font2 = new Font("Arial Nova Cond", 13.0);
        button.setFont(font2);
        hbox.getChildren().add(region);
        hbox.getChildren().add(label);
        hbox.getChildren().add(button);
        hbox.getChildren().add(region2);
        vbox.getChildren().add(hbox);
        panel.getChildren().add(vbox);
    }

    public void addRetrievedCardLists() {
        for (int i = 0; i < server.getAllCardLists().size(); i++) {
            drawCardList(server.getAllCardLists().get(i).title);
        }
    }
}