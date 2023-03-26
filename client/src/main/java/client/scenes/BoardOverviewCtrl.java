package client.scenes;
import client.services.BoardOverviewService;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class BoardOverviewCtrl {
    private final BoardOverviewService boardOverviewService;
    private final MainCtrl mainCtrl;

    private final ListMenuCtrl listMenuCtrl;

    private final AddTaskCtrl addTaskCtrl;

    @FXML
    private HBox panel;

    @FXML
    // Used for requesting focus
    private Label hiddenLabel;

    @FXML
    private Label addConfirmationLabel;


    @Inject
    public BoardOverviewCtrl(BoardOverviewService boardOverviewService, MainCtrl mainCtrl,
                             ListMenuCtrl listMenuCtrl, AddTaskCtrl addTaskCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.mainCtrl = mainCtrl;
        this.listMenuCtrl = listMenuCtrl;
        this.addTaskCtrl = addTaskCtrl;
    }

    public void createList() {
        CardList newCardList = new CardList(new ArrayList<>(), "");
        newCardList = boardOverviewService.addCardList(newCardList);

        addConfirmationLabel.setVisible(true);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                addConfirmationLabel.setVisible(false);
            }
        };
        timer.schedule(task, 3000);

        drawCardList(newCardList);
    }

    public void createCard(VBox vbox, Button button, String title, long cardListId) {
        Card newCard = new Card(title);
        newCard = boardOverviewService.addCard(newCard, cardListId);
        drawCard(vbox, button, title, cardListId, newCard);
    }

    public void addRetrievedCardLists() {
        hiddenLabel.requestFocus();
        var lists = boardOverviewService.getCardLists();
        panel.getChildren().clear();
        for (CardList list : lists) {
            drawCardList(list);
        }
    }

    public void cardMenu(VBox vbox, HBox hbox, Button button, long cardListId, Card card) {
        ContextMenu menu = new ContextMenu();
        MenuItem remove = new MenuItem("Remove card");
        configureCardMenu(button, menu, remove);

        remove.setOnAction(event -> {
            vbox.getChildren().remove(hbox);
            boardOverviewService.removeCard(card,cardListId);
        });

        button.setOnMouseClicked(event -> {
            menu.show(button, event.getScreenX(), event.getScreenY());
        });
    }

    public void listMenu(ScrollPane sp,
                         BorderPane bp,
                         Button button,
                         CardList cardList,
                         TextField label) {
        ContextMenu cm = new ContextMenu();
        MenuItem remove = new MenuItem("Remove list");
        MenuItem edit = new MenuItem("Edit list");
        configureListMenu(button, cm, remove, edit);
        remove.setOnAction(event -> {
            panel.getChildren().remove(sp);
            boardOverviewService.removeCardList(cardList);
        });
        edit.setOnAction(event -> {
            mainCtrl.showListMenu(button, cardList, bp);
            listMenuCtrl.changeListLabel(cardList,label);
        });
        button.setOnMouseClicked(event -> {
            cm.show(button, event.getScreenX(), event.getScreenY());
        });
    }

    public HBox getNewDroppedCard(long cardListId, VBox vbox, Dragboard db) {
        String title = db.getString();
        Card newCard = new Card(title);
        newCard = boardOverviewService.addCard(newCard,cardListId);
        HBox card = drawCardAfterDrop(vbox, title, cardListId, newCard);
        return card;
    }

    public HBox getNewDroppedCardWithIndex(long cardListId, VBox vbox,
                                           Dragboard db, int dropIndex) {
        String title = db.getString();
        Card newCard = new Card(title);
        newCard = boardOverviewService.addCardAtIndex(newCard,cardListId,dropIndex);
        HBox card = drawCardAfterDrop(vbox, title, cardListId, newCard);
        return card;
    }

    public void drawCardList(CardList cardList){
        long cardListId = cardList.getId();

        BorderPane bp = new BorderPane();
        VBox vbox = new VBox();
        ScrollPane sp = new ScrollPane();

        configureCardListScrollPane(bp, sp);

        configureCardListVBox(cardList, bp, sp,vbox);

        var listOfCards = boardOverviewService.getCards(cardListId);

        for (Card card : listOfCards) {
            drawCard(vbox, null, card.getTitle(), cardListId, card);
        }
        //dragging detected
        configureCardListVBoxOnDragOver(vbox);
        //drop detected
        configureCardListVBoxOnDragDropped(cardList, cardListId, vbox);

        drawAddCardButton(vbox, cardListId);

        bp.setCenter(vbox);

        panel.getChildren().add(sp);
    }

    private void configureCardListVBoxOnDragOver(VBox vbox) {
        vbox.setOnDragOver(event -> {
            if (event.getGestureSource() != vbox && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });
    }

    private void configureCardListScrollPane(BorderPane bp, ScrollPane sp) {
        sp.setContent(bp);
        sp.setStyle("-fx-background-color: black;");
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setPannable(true);
    }

    private void configureCardListVBoxOnDragDropped(CardList cardList, long cardListId, VBox vbox) {
        vbox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                int dropIndex = -1;
                //find the right place in the card list
                for (int i = 0; i < vbox.getChildren().size(); i++) {
                    Node child = vbox.getChildren().get(i);
                    if (event.getY() <= child.getBoundsInParent().getMinY() +
                            child.getBoundsInParent().getHeight()) {
                        dropIndex = i;
                        break;
                    }
                }
                if (dropIndex == -1) { //if it is dropped under all the cards
                    HBox card = getNewDroppedCard(cardListId, vbox, db);

                    vbox.getChildren().add(vbox.getChildren().size() - 1, card);
                }
                else {
                    if(dropIndex== cardList.getCards().size()){ //card is dropped on the "+" button
                        HBox card = getNewDroppedCard(cardListId, vbox, db);

                        vbox.getChildren().add(dropIndex, card);
                    }
                    else {
                        HBox card = getNewDroppedCardWithIndex(cardListId, vbox, db, dropIndex);

                        vbox.getChildren().add(dropIndex, card);
                    }
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void drawAddCardButton(VBox vbox, long cardListId){
        Button addCard = new Button("+");
        configureAddCardButton(addCard);
        addCard.setOnAction(event -> {
            String title = "Card";
            createCard(vbox, addCard, title, cardListId);
            drawAddCardButton(vbox, cardListId);
        });
        vbox.getChildren().add(addCard);
    }

    private void configureAddCardButton(Button addCard) {
        addCard.setFocusTraversable(false);
        addCard.setAlignment(Pos.CENTER);
        addCard.setMnemonicParsing(false);
        addCard.setPrefHeight(36);
        addCard.setPrefWidth(100);
        addCard.setStyle("-fx-border-color: black;");
    }

    private void drawCard(VBox vbox, Button button, String title, long cardListId, Card cardEntity){
        HBox card = makeNewCard(vbox, title, cardListId, cardEntity);
        if (button != null) {
            vbox.getChildren().remove(button);
        }
        vbox.getChildren().add(card);
    }

    private HBox drawCardAfterDrop(VBox vbox, String title, long cardListId, Card cardEntity){
        HBox card = makeNewCard(vbox, title, cardListId, cardEntity);
        return card;
    }

    private HBox makeNewCard(VBox vbox, String title, long cardListId, Card cardEntity) {
        HBox card = new HBox();

        Label task = new Label(title);
        Button menu = new Button(":");
        configureNewCard(cardEntity, card, task, menu);

        cardMenu(vbox, card, menu, cardListId, cardEntity);
        task.setOnMouseClicked(event -> {
            mainCtrl.showAddTask(task);
            addTaskCtrl.configureEditButton(cardEntity);
        });

        card.setOnDragDetected(event -> {
            Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            configureDragboardAndClipboard(vbox, card, task, event, db, content);
            boardOverviewService.removeCard(cardEntity,cardListId);
        });
        return card;
    }

    private void configureDragboardAndClipboard(VBox vbox, HBox card, Label task,
                                                MouseEvent event, Dragboard db,
                                                ClipboardContent content) {
        content.putString(task.getText());
        db.setContent(content);
        db.setDragView(card.snapshot(null, null));
        event.consume();

        vbox.getChildren().remove(card);
    }

    private void configureNewCard(Card cardEntity, HBox card, Label task, Button menu) {
        card.getChildren().add(task);
        card.getChildren().add(menu);
        card.setAlignment(Pos.CENTER);

        menu.setPrefHeight(36);
        menu.setPrefWidth(20);
        menu.setStyle("-fx-border-color: black");
        menu.setMnemonicParsing(false);

        task.setAlignment(Pos.CENTER);
        task.setPrefHeight(36);
        task.setPrefWidth(80);
        task.setStyle("-fx-border-color: black");
        task.setId(String.valueOf(cardEntity.getId()));
    }

    private void configureCardListVBox(CardList cardList, BorderPane bp, ScrollPane sp, VBox vbox) {
        //List Body
        bp.setPrefHeight(274);
        bp.setPrefWidth(126);
        bp.setStyle("-fx-border-color: black;");

        //List Name
        TextField label = new TextField(cardList.getTitle());
        final String NORMAL_TITLE_STYLE = "-fx-background-color: #d9cdad;" +
                " -fx-border-color: #d9cdad; -fx-font-size: 12; -fx-wrap-text: true";
        String HOVERED_BUTTON_STYLE = "-fx-background-color: #fadebe;" +
                " -fx-border-color: #d9cdad; -fx-font-size: 12; -fx-wrap-text: true";
        label.setStyle(NORMAL_TITLE_STYLE);
        label.setPromptText("Enter list name...");
        label.setId(String.valueOf(cardList.getId()));
        label.setAlignment(Pos.CENTER);
        configureTextField(label, NORMAL_TITLE_STYLE, HOVERED_BUTTON_STYLE);

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

        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPrefHeight(221.0);
        vbox.setPrefWidth(109.0);
        vbox.setSpacing(10.0);
        listMenu(sp, bp, button, cardList,label);
    }

    private void configureTextField(TextField label,
                                    final String NORMAL_BUTTON_STYLE,
                                    final String HOVERED_BUTTON_STYLE) {
        label.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    long cardListId = Long.parseLong(label.getId());
                    boardOverviewService.updateCardListTitle(cardListId,label.getText());
                }
            }
        });

        label.setOnMouseEntered(e -> label.setStyle(HOVERED_BUTTON_STYLE));
        label.setOnMouseExited(e -> label.setStyle(NORMAL_BUTTON_STYLE));
    }



    private void configureListMenu(Button button, ContextMenu cm, MenuItem remove, MenuItem edit) {
        cm.getItems().add(remove);
        cm.getItems().add(edit);
        button.setContextMenu(cm);
    }


    private void configureCardMenu(Button button, ContextMenu menu, MenuItem remove) {
        menu.getItems().add(remove);
        menu.setStyle("-fx-border-color: black");
        button.setContextMenu(menu);
    }

    public void disconnect() {
        boardOverviewService.closeServerConnection();
        mainCtrl.showClientConnectPage();
    }
}

