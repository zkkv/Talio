package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;


public class HomeScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private HBox panel;

    @FXML
    private AnchorPane anchorPane;

    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }


    public void createList() {
        drawCardList("Label");
        server.addCardListToBoard(new CardList(new ArrayList<>(), "Label"));
    }

    public void addRetrievedCardLists() {
        for (int i = 0; i < server.getAllCardLists().size(); i++) {
            drawCardList(server.getAllCardLists().get(i).title);
        }
    }


    public void drawCardList(String text){
        BorderPane bp = new BorderPane();
        bp.setPrefHeight(274);
        bp.setPrefWidth(126);
        bp.setStyle("-fx-background-color: #d9cdad; -fx-border-color: black;");
        Button button = new Button(":");
        button.setAlignment(Pos.TOP_CENTER);
        button.setLayoutX(95.0);
        button.setLayoutY(6.0);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setMnemonicParsing(false);
        button.setStyle("-fx-background-color: #a3957c;");

        //List Name
        TextField label = new TextField();
        label.setStyle("-fx-background-color: #d9cdad; -fx-border-color: #d9cdad; -fx-font-size: 12; -fx-wrap-text: true");
        label.setPromptText("Enter list name...");
        label.setId("listName");
        label.setAlignment(Pos.CENTER);

        //List Header
        HBox hbox = new HBox();
        hbox.setStyle("-fx-start-margin: 10; -fx-end-margin: 10");
        hbox.setSpacing(3);
        hbox.getChildren().add(label);
        hbox.getChildren().add(button);
        bp.setTop(hbox);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPrefHeight(221.0);
        vbox.setPrefWidth(109.0);
        vbox.setSpacing(10.0);
        drawAddCardButton(vbox);
        bp.setCenter(vbox);
        panel.getChildren().add(bp);
    }
    public void drawAddCardButton(VBox vbox){
        Button addCard = new Button("+");
        addCard.setAlignment(Pos.CENTER);
        addCard.setMnemonicParsing(false);
        addCard.setPrefHeight(36);
        addCard.setPrefWidth(100);
        addCard.setStyle("-fx-border-color: black;");
        addCard.setOnAction(event -> {
            drawCard(vbox,addCard,"Card");
        });
        vbox.getChildren().add(addCard);
    }
    public void drawCard(VBox vbox,Button button, String title){
        Button task = new Button(title);
        task.setAlignment(Pos.CENTER);
        task.setMnemonicParsing(false);
        task.setPrefHeight(36);
        task.setPrefWidth(100);
        task.setStyle("-fx-border-color: black");
        task.setOnAction(event -> {
            mainCtrl.showAddTask(task);
        });
        vbox.getChildren().remove(button);
        vbox.getChildren().add(task);
        drawAddCardButton(vbox);
    }

    public void disconnect() {
        ServerUtils.closeConnection();
        mainCtrl.showClientConnectPage();
    }
}

