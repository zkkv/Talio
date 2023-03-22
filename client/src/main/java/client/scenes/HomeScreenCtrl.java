package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import commons.Pair;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;


public class HomeScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private final ListMenuCtrl listMenuCtrl;

    private final AddTaskCtrl addTaskCtrl;

    @FXML
    private HBox panel;


    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl, ListMenuCtrl listMenuCtrl, AddTaskCtrl addTaskCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.listMenuCtrl = listMenuCtrl;
        this.addTaskCtrl = addTaskCtrl;
    }

    public void createList() {
        CardList newCardList = new CardList(new ArrayList<>(), "");
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

    public VBox initializeListVBox(CardList cardList, BorderPane bp) {
        //List Body
        bp.setPrefHeight(274);
        bp.setPrefWidth(126);
        bp.setStyle("-fx-background-color: #d9cdad; -fx-border-color: black;");

        //List Name
        TextField label = new TextField(cardList.title);
        label.setStyle("-fx-background-color: #d9cdad;" +
                " -fx-border-color: #d9cdad; -fx-font-size: 12; -fx-wrap-text: true");
        label.setPromptText("Enter list name...");
        label.setId(String.valueOf(cardList.id));
        label.setAlignment(Pos.CENTER);
        configureTextField(label);

        //List Button
        Button button = new Button(":");
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
        menu(bp,button, cardList,label);

        return vbox;
    }

    private void configureTextField(TextField label) {
        label.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    CardList cardList = server.getCardList(Long.parseLong(label.getId()));
                    Pair<CardList,String> request = new Pair<>(cardList, label.getText());
                    server.updateCardListTitle(request);
                }
            }
        });
    }

    public void drawCardList(CardList cardList){
        long cardListId = cardList.id;

        BorderPane bp = new BorderPane();

        VBox vbox = initializeListVBox(cardList, bp);
        var listOfCards = server.getCardsOfCardList(cardListId);

        for (Card card : listOfCards) {
            drawCard(vbox, null, card.title, cardListId, card);
        }

        drawAddCardButton(vbox, cardListId);
        bp.setCenter(vbox);
        panel.getChildren().add(bp);
    }

    public void drawAddCardButton(VBox vbox, long cardListId){
        Button addCard = new Button("+");
        addCard.setAlignment(Pos.CENTER);
        addCard.setMnemonicParsing(false);
        addCard.setPrefHeight(36);
        addCard.setPrefWidth(100);
        addCard.setStyle("-fx-border-color: black;");
        addCard.setOnAction(event -> {
            String title = "Card";
            Card card = server.addCardToCardList(new Card(title), cardListId);
            drawCard(vbox, addCard, title, cardListId, card);
            drawAddCardButton(vbox, cardListId);
        });
        vbox.getChildren().add(addCard);
    }
    public void menu(BorderPane bp, Button button, CardList cardList, TextField label) {
        ContextMenu cm = new ContextMenu();
        MenuItem remove = new MenuItem("Remove list");
        MenuItem edit = new MenuItem("Edit list");
        cm.getItems().add(remove);
        cm.getItems().add(edit);
        button.setContextMenu(cm);
        remove.setOnAction(event -> {
            panel.getChildren().remove(bp);
            server.removeCardListToBoard(cardList);
        });
        edit.setOnAction(event -> {
            mainCtrl.showListMenu(button, cardList, bp);
            listMenuCtrl.changeListLabel(cardList,label);
        });
        button.setOnMouseClicked(event -> {
            cm.show(button, event.getScreenX(), event.getScreenY());
        });
    }

    public void drawCard(VBox vbox, Button button, String title, long cardListId, Card card){
//        Card card = server.addCardToCardList(new Card(title), cardListId);
        Button task = new Button(title);
        task.setId(String.valueOf(card.id));
        task.setAlignment(Pos.CENTER);
        task.setMnemonicParsing(false);
        task.setPrefHeight(36);
        task.setPrefWidth(100);
        task.setStyle("-fx-border-color: black");
        task.setOnAction(event -> {
            mainCtrl.showAddTask(task);
            addTaskCtrl.configureEditButton(card);
        });

        if (button != null) {
            vbox.getChildren().remove(button);
        }
        vbox.getChildren().add(task);

    }

    public void disconnect() {
        ServerUtils.closeConnection();
        mainCtrl.showClientConnectPage();
    }

    public Card selectCard(Card card){
        return card;
    }
}

