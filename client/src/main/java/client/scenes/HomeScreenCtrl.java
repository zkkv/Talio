package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;


public class HomeScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private final ListMenuCtrl listMenuCtrl;

    @FXML
    private HBox panel;


    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl, ListMenuCtrl listMenuCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.listMenuCtrl = listMenuCtrl;
    }

    public void createList() {
        CardList newCardList = new CardList(new ArrayList<>(), "Label");
        newCardList = server.addCardListToBoard(newCardList);
        drawCardList(newCardList);
    }

    public void addRetrievedCardLists() {
        var lists = server.getAllCardLists();
        panel.getChildren().clear();
        for (CardList list : lists) {
            drawCardList(list);
        }
    }

    public void drawCardList(CardList cardList){
        long id = cardList.id;
        String text = cardList.title;

        BorderPane bp = new BorderPane();
        bp.setPrefHeight(274);
        bp.setPrefWidth(126);

        //List Name
        TextField label = new TextField(text);
        label.setStyle("-fx-background-color: #d9cdad;" +
                " -fx-border-color: #d9cdad; -fx-font-size: 12; -fx-wrap-text: true");
        label.setPromptText("Enter list name...");
        label.setId("listName");
        label.setAlignment(Pos.CENTER);

        //List Button
        bp.setStyle("-fx-background-color: #d9cdad; -fx-border-color: black;");

        Button button = new Button(":");
        button.setOnAction(event -> {
            mainCtrl.showListMenu(button, cardList, bp);
            selectListLabelAndObject(label);
        });
        button.setAlignment(Pos.TOP_CENTER);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setMnemonicParsing(false);
        button.setStyle("-fx-background-color: #a3957c;");

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

        var listOfCards = server.getCardsOfCardList(id);

        for (Card card : listOfCards) {
            drawCard(vbox, null, card.title, id);
        }

        drawAddCardButton(vbox, id);
        bp.setCenter(vbox);
        panel.getChildren().add(bp);
        listMenuCtrl.deletingList(panel);
    }

    public void drawAddCardButton(VBox vbox, long id){
        Button addCard = new Button("+");
        addCard.setAlignment(Pos.CENTER);
        addCard.setMnemonicParsing(false);
        addCard.setPrefHeight(36);
        addCard.setPrefWidth(100);
        addCard.setStyle("-fx-border-color: black;");
        addCard.setOnAction(event -> {
            String title = "Card";
            drawCard(vbox, addCard, title, id);
            server.addCardToCardList(new Card(title), id);
        });
        vbox.getChildren().add(addCard);
    }

    public void drawCard(VBox vbox, Button button, String title, long id){
        Button task = new Button(title);
        task.setAlignment(Pos.CENTER);
        task.setMnemonicParsing(false);
        task.setPrefHeight(36);
        task.setPrefWidth(100);
        task.setStyle("-fx-border-color: black");
        task.setOnAction(event -> {
            mainCtrl.showAddTask(task);
        });

        if (button != null) {
            vbox.getChildren().remove(button);
        }
        vbox.getChildren().add(task);
        drawAddCardButton(vbox, id);
    }

    public void disconnect() {
        ServerUtils.closeConnection();
        mainCtrl.showClientConnectPage();
    }

    public void selectListLabelAndObject(TextField label){
        listMenuCtrl.changeListLabel(label);
    }
}

