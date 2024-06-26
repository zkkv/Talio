package client.scenes;
import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;


public class BoardOverviewCtrl implements Initializable {
    private final BoardOverviewService boardOverviewService;

    private BoardUserIdentifier boardUserIdentifier;
    private final MainCtrl mainCtrl;

    private final ListMenuCtrl listMenuCtrl;

    private final CardDetailsCtrl cardDetailsCtrl;

    @FXML
    private HBox panel;

    @FXML
    private Label boardTitle;

    @FXML
    private Button settings;

    @FXML
    // Used for requesting focus
    private Label hiddenLabel;

    @FXML
    private Label addConfirmationLabel;

    private Timer listWasAddedTimer;

    private Timer listNameErrorTimer;

    @Inject
    public BoardOverviewCtrl(BoardOverviewService boardOverviewService, MainCtrl mainCtrl,
                             ListMenuCtrl listMenuCtrl,
                             BoardUserIdentifier boardUserIdentifier,
                             CardDetailsCtrl cardDetailsCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.mainCtrl = mainCtrl;
        this.listMenuCtrl = listMenuCtrl;
        this.boardUserIdentifier = boardUserIdentifier;
        this.cardDetailsCtrl = cardDetailsCtrl;
    }

    /**
     * Adds the icons to the stage
     *
     * @param stage the stage for which the icons need to be set
     */
    private void addIcons(Stage stage) {
        /* Icon created by Freepik - Flaticon */
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon16.png"));
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon32.png"));
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon64.png"));
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon128.png"));
    }

    public void createList(Button addList) {
        CardList newCardList = new CardList(new ArrayList<>(), "");
        newCardList = boardOverviewService.addCardList(newCardList,
            boardUserIdentifier.getCurrentBoard());

        addConfirmationLabel.setVisible(true);
        if (listWasAddedTimer != null) {
            listWasAddedTimer.cancel();
        }
        listWasAddedTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                addConfirmationLabel.setVisible(false);
            }
        };
        listWasAddedTimer.schedule(task, 3000);
        panel.getChildren().remove(addList);
        drawCardList(newCardList);
        addListButton();
    }

    private void addListButton(){
        Button addList = new Button("\uFF0B");
        configureAddListButton(addList);
        addList.setOnAction(event -> {
            createList(addList);
        });
        addListMessage(addList);
        panel.getChildren().add(addList);
    }

    private void addListMessage(Button addList){
        Tooltip tooltip = new Tooltip("Add a new list");
        tooltip.setFont(Font.font("Verdana", 14));
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.ZERO);
        addList.setTooltip(tooltip);
    }
    public void createCard(VBox vbox, Button button, String title, long cardListId) {
        Card newCard = new Card(title, new ArrayList<>(),new ArrayList<>());
        newCard = boardOverviewService.addCard(newCard, cardListId,
            boardUserIdentifier.getCurrentBoard());
        drawCard(vbox, button, title, cardListId, newCard);
    }

    public void addRetrievedCardLists(Board currentBoard) {
        hiddenLabel.requestFocus();
        var lists = boardOverviewService.getCardLists(currentBoard);
        panel.getChildren().clear();
        for (CardList list : lists) {
            drawCardList(list);
        }
        addListButton();
    }

    public void drawBoard() {
        addRetrievedCardLists(boardUserIdentifier.getCurrentBoard());
        configureBoardTitle();
    }

    public void configureBoardTitle() {
        boardTitle.setText(boardUserIdentifier.getCurrentBoard().getTitle());
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
            boardOverviewService.removeCardList(cardList,boardUserIdentifier.getCurrentBoard());
            hiddenLabel.requestFocus();
        });
        edit.setOnAction(event -> {
            mainCtrl.showListMenu();
            listMenuCtrl.changeListLabel(cardList,label);
        });
        button.setOnMouseClicked(event -> {
            cm.show(button, event.getScreenX(), event.getScreenY());
        });
    }

    public GridPane getNewDroppedCardWithIndex(long cardListId, VBox vbox,
                                               Dragboard db, int dropIndex) {
        long cardId = Long.parseLong((db.getString()));
        Card newCard = new Card();
        newCard = boardOverviewService.addCardAtIndex(newCard, cardId,cardListId,dropIndex,
            boardUserIdentifier.getCurrentBoard());
        GridPane card = drawCardAfterDrop(vbox, "", cardListId, newCard);
        return card;
    }

    public void drawCardList(CardList cardList){
        long cardListId = cardList.getId();

        BorderPane bp = new BorderPane();
        VBox vbox = new VBox();
        ScrollPane sp = new ScrollPane();

        vbox.setPadding(new Insets(10.0));

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
                    GridPane card = getNewDroppedCardWithIndex(cardListId,
                        vbox, db, (vbox.getChildren().size() - 1));

                    vbox.getChildren().add(vbox.getChildren().size() - 1, card);
                }
                else {
                    if(dropIndex== vbox.getChildren().size()-1){ //card is dropped on the "+" button
                        GridPane card = getNewDroppedCardWithIndex(cardListId, vbox, db, dropIndex);

                        vbox.getChildren().add(dropIndex, card);
                    }
                    else {
                        GridPane card = getNewDroppedCardWithIndex(cardListId, vbox, db, dropIndex);

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
        Button addCard = new Button("\uFF0B");
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
        addCard.setPrefHeight(34);
        addCard.setPrefWidth(140);
        addCard.setMinHeight(34);
        addCard.setMinWidth(140);
        addCard.setStyle("-fx-border-color: black;");
    }
    private void configureAddListButton(Button addList){
        addList.setFocusTraversable(false);
        addList.setAlignment(Pos.CENTER);
        addList.setMnemonicParsing(false);
        addList.setPrefHeight(70);
        addList.setPrefWidth(70);
        addList.setFont(Font.font(20));
        addList.setStyle("-fx-border-color: black;" );
    }

    private void drawCard(VBox vbox, Button button, String title, long cardListId, Card cardEntity){
        GridPane card = makeNewCard(vbox, title, cardListId, cardEntity);
        if (button != null) {
            vbox.getChildren().remove(button);
        }
        vbox.getChildren().add(card);
    }

    private GridPane drawCardAfterDrop(VBox vbox, String title, long cardListId, Card cardEntity){
        GridPane card = makeNewCard(vbox, title, cardListId, cardEntity);
        return card;
    }

    private GridPane makeNewCard(VBox vbox, String title, long cardListId, Card cardEntity) {
        GridPane card = new GridPane();

        HBox icons = configureIcon(cardEntity);
        Label task = new Label(title);
        configureNewCard(cardEntity, card, task, icons,vbox,cardListId);

        card.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                mainCtrl.showCardDetails(title,cardEntity);
                mainCtrl.configureSaveButton(cardEntity,card);
            }
        });

        card.setOnDragDetected(event -> {
            Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            configureDragboardAndClipboard(vbox, card, cardEntity, event, db, content);
            boardOverviewService.removeCardWhenDragged(cardEntity,cardListId,
                boardUserIdentifier.getCurrentBoard());
        });

        card.setOnDragDone(event -> {
            if (!event.getGestureSource().getClass().equals(GridPane.class)) {
                boardOverviewService.removeCard(cardEntity,cardListId,
                    boardUserIdentifier.getCurrentBoard());
                event.consume();
            }
        });


        return card;
    }

    public HBox configureIcon(Card card) {
        Image icon = new Image("img/descriptionIcon.png");
        ImageView descriptionIcon = new ImageView(icon);
        descriptionIcon.setFitWidth(15);
        descriptionIcon.setFitHeight(15);
        descriptionIcon.setPreserveRatio(true);

        if(!card.hasDescription()) {
            descriptionIcon.setVisible(false);
        }

        List<SubTask> listSubTask = card.getTasks();
        int numberOfTasks = listSubTask.size();
        int numberOfChecked = 0;
        for(SubTask subTask: listSubTask) {
            if(subTask.isChecked()) {
                numberOfChecked++;
            }
        }
        Label progressOfSubTasks = new Label();
        if(numberOfTasks != 0) {
            int progress = (int) Math.round((double) numberOfChecked / numberOfTasks * 100);
            if (progress < 100) {
                progressOfSubTasks.setText(progress + "%");
            }
            else {
                progressOfSubTasks.setText("Done");
            }
        }
        progressOfSubTasks.setAlignment(Pos.CENTER);
        progressOfSubTasks.setMinWidth(30);

        HBox iconList = new HBox(progressOfSubTasks, descriptionIcon);
        iconList.setStyle("-fx-background-color: #DAD2BF;");
        HBox.setMargin(descriptionIcon, new Insets(3,3,3,3));
        iconList.setSpacing(3);

        return iconList;
    }

    private void configureDragboardAndClipboard(VBox vbox, GridPane card, Card cardEntity,
                                                MouseEvent event, Dragboard db,
                                                ClipboardContent content) {
        content.putString(Long.toString(cardEntity.getId()));
        db.setContent(content);
        db.setDragView(card.snapshot(null, null));
        event.consume();

        vbox.getChildren().remove(card);
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private void configureNewCard(Card cardEntity,
                                  GridPane card,
                                  Label task,
                                  HBox icons,
                                  VBox cardListVbox,
                                  long cardListId) {
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        row1.setMinHeight(5.0);
        row1.setPrefHeight(5.0);
        row2.setVgrow(Priority.ALWAYS);
        row3.setMinHeight(18.0);
        row3.setPrefHeight(18.0);


        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        col1.setMinWidth(10.0);
        col1.setPrefWidth(100.0);
        col2.setMinWidth(10.0);
        col2.setPrefWidth(100.0);
        col3.setMinWidth(10.0);
        col3.setPrefWidth(50.0);
        card.getColumnConstraints().addAll(col1, col2, col3);

        card.getRowConstraints().addAll(row1,row2,row3);
        Button remove = new Button("\u2A2F");
        icons.setStyle("-fx-background-color: white");
        remove.setOnAction(event -> {
            cardListVbox.getChildren().remove(card);
            boardOverviewService.removeCard(cardEntity,
                    cardListId,
                    boardUserIdentifier.getCurrentBoard());
        });
        remove.setPrefHeight(20);
        remove.setMinHeight(20);
        remove.setPrefWidth(20);
        remove.setMinWidth(20);
        remove.setFont(new Font(10));
        card.add(remove, 2, 0);

        card.add(icons, 0, 0,2,1);
        card.add(task, 0, 1, 3, 1);

        ScrollPane scrollPane = new ScrollPane();
        card.add(scrollPane, 0, 2, 3, 1);
        HBox tagList = new HBox();

        card.setPadding(new Insets(15));
        BorderStroke borderStroke = new BorderStroke(
            null, null, null, null,
            BorderStrokeStyle.SOLID,
            BorderStrokeStyle.SOLID,
            BorderStrokeStyle.SOLID,
            BorderStrokeStyle.SOLID,
            null,
            new BorderWidths(1), // width of the border
            null
        );
        card.setBorder(new Border(borderStroke));
        card.setVgap(10);

        tagList.setMinHeight(1.0);
        tagList.setPrefHeight(22.0);
        tagList.setPrefWidth(100.0);
        tagList.setSpacing(2);

        scrollPane.setMinHeight(1.0);
        scrollPane.setMinWidth(1.0);
        scrollPane.setPrefHeight(25.0);
        scrollPane.setPrefWidth(100.0);
        scrollPane.setContent(tagList);

        task.setPrefHeight(Label.USE_COMPUTED_SIZE);
        task.setWrapText(true);
        task.setFont(Font.font(14));

        card.setStyle("-fx-background-color: white");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        for (Tag tag:cardEntity.getTags()) {
            Circle circle = new Circle(8.5);
            circle.setFill(Color.color(tag.getRed()/255.0,
                    tag.getGreen()/255.0,tag.getBlue()/255.0));
            tagList.getChildren().add(circle);
        }
        task.setId(String.valueOf(cardEntity.getId()));
    }

    private void configureCardListVBox(CardList cardList, BorderPane bp, ScrollPane sp, VBox vbox) {
        //List Body
        bp.setPrefHeight(370);
        bp.setPrefWidth(175);
        bp.setStyle("-fx-background-color: #d9cdad;"+"-fx-border-color: black;");

        //List Name
        TextField label = new TextField(cardList.getTitle());
        final String NORMAL_TITLE_STYLE = "-fx-background-color: #d9cdad;" +
            " -fx-border-color: #d9cdad; -fx-wrap-text: true";
        final String HOVERED_BUTTON_STYLE = "-fx-background-color: #fadebe;" +
            " -fx-border-color: #d9cdad; -fx-wrap-text: true";
        label.setStyle(NORMAL_TITLE_STYLE);
        label.setPromptText("Enter list name...");
        label.setId(String.valueOf(cardList.getId()));
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font(14));
        configureTextField(label, NORMAL_TITLE_STYLE, HOVERED_BUTTON_STYLE);

        //List Button
        Button button = new Button("\u22EE");
        button.setFont(Font.font("Segoe UI Symbol", 16));
        button.setMinHeight(35);
        button.setStyle("-fx-background-color: #BEB38D");

        //List Header
        HBox hbox = new HBox();
        hbox.setStyle("-fx-start-margin: 20; -fx-end-margin: 20");
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

        String oldLabelValue = label.getText();

        // When unfocused
        label.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                handleListNameEdit(label, oldLabelValue);
            }
        });

        setupLabelConstraints(label);

        label.setOnMouseEntered(e -> label.setStyle(HOVERED_BUTTON_STYLE));
        label.setOnMouseExited(e -> label.setStyle(NORMAL_BUTTON_STYLE));
    }

    /**
     * Handle CardList title renaming and send a server request
     * to update the title in the backend
     *
     * @param label The TextField that fetches the new name
     * @param oldLabelValue The old name of the CardList
     */
    private void handleListNameEdit (TextField label, String oldLabelValue) {
        final String REGEXP = "\\S(.*\\S)?";
        String input = label.getText();

        if(input.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setHeaderText(null);
            alert.setTitle("Incorrect Name");
            alert.setContentText("List name cannot be left blank!");
            alert.showAndWait();
            label.setText(oldLabelValue);
        }
        else if (!input.matches(REGEXP)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setTitle("Incorrect Name");
            alert.setHeaderText(null);
            alert.setContentText("List name cannot start or end with spaces.");
            alert.showAndWait();
            label.setText(label.getText().trim());
        }
        else {
            long cardListId = Long.parseLong(label.getId());
            boardOverviewService.updateCardListTitle(cardListId, label.getText(),
                    boardUserIdentifier.getCurrentBoard());
        }
    }

    /**
     * Sets up label constraints and the error message
     * which is shown in case they are violated.
     * Error message disappears in some time after no action is taken.
     *
     * @author Kirill Zhankov
     */
    private void setupLabelConstraints(TextField label) {
        final String REGEXP = "[a-zA-Z0-9_ \\-!@#$%^&*()~\"]*";
        final int MAX_LENGTH = 30;
        final int SHOW_DURATION_MS = 6000;

        Tooltip tooltip = new Tooltip();
        tooltip.setFont(Font.font(15));
        tooltip.setWrapText(true);
        tooltip.setText("List name has to be no more than " + MAX_LENGTH
                + " characters long and can contain only letters, "
                + "digits,\nspaces and any of: _-!@#$%^&*()~\" but "
                + "it cannot start or end with spaces.");

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String input = change.getControlNewText();
            if (input.matches(REGEXP) && input.length() <= MAX_LENGTH) {
                Platform.runLater(tooltip::hide);
                return change;
            }
            else {
                tooltip.setAutoHide(true);

                double x = label.localToScreen(label.getBoundsInLocal()).getMinX();
                double y = label.localToScreen(label.getBoundsInLocal()).getMinY();

                tooltip.show(label.getScene().getWindow(), x, y);
                final double TOOLTIP_OFFSET_Y = tooltip.getHeight();
                tooltip.setX(x);
                tooltip.setY(y - TOOLTIP_OFFSET_Y);

                if (listNameErrorTimer != null) {
                    listNameErrorTimer.cancel();
                }
                listNameErrorTimer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(tooltip::hide);
                    }
                };
                listNameErrorTimer.schedule(task, SHOW_DURATION_MS);
                return null;
            }
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        label.setTextFormatter(textFormatter);
    }


    private void configureListMenu(Button button, ContextMenu cm, MenuItem remove, MenuItem edit) {
        cm.getItems().add(remove);
        cm.getItems().add(edit);
        button.setContextMenu(cm);
    }

    public void subscribeForUpdates(Board board){
        boardOverviewService.registerForUpdates("/topic/board/"+board.getId(), Board.class,b -> {
            Platform.runLater(()->{
                boardUserIdentifier.setCurrentBoard(b);
                drawBoard();
            });
        });
        boardOverviewService.registerForUpdates("/topic/board/remove", Board.class, b -> {
            Platform.runLater(() -> {
                boardUserIdentifier.setCurrentBoard(b);
                mainCtrl.showStartPage();
            });
        });
        boardOverviewService.registerForUpdates("/topic/board/"+board.getId()+"/tag",
            Board.class, b -> {
                Platform.runLater(() -> {
                    boardUserIdentifier.setCurrentBoard(b);
                    if(mainCtrl.isTagsListShowing()) {
                        mainCtrl.initTags();
                    }
                });
            });
        boardOverviewService.registerForUpdates("/topic/board/"+board.getId()+"/card-details",
                Card.class,card -> {
                Platform.runLater(()->{
                    if(mainCtrl.isCardDetailsShowing()){
                        mainCtrl.showCardDetails(card.getTitle(), card);
                    }
                    if(mainCtrl.isTagsInCardShowing()){
                        mainCtrl.showAllTagsListWithinACard(card);
                    }
                });
            });
        boardOverviewService.registerForUpdates("/topic/board/"+board.getId()+"/card",
                Board.class,b -> {
                    Platform.runLater(()->{
                        if(mainCtrl.isCardDetailsShowing()){
                            mainCtrl.showBoardPage();
                        }
                    });
                });
    }

    public void configureSettings() {
        ContextMenu settingsMenu = new ContextMenu();
        MenuItem changeBoard = new MenuItem();
        MenuItem disconnect = new MenuItem();
        MenuItem boardSettings = new MenuItem();
        settingsMenu.getItems().add(boardSettings);
        settingsMenu.getItems().add(changeBoard);
        settingsMenu.getItems().add(disconnect);
        settings.setContextMenu(settingsMenu);
        changeBoard.setText("Change Board");
        disconnect.setText("Disconnect");
        boardSettings.setText("Board Settings");

        boardSettings.setOnAction(event -> {
            mainCtrl.showBoardSettings(boardUserIdentifier.getCurrentBoard().getTitle());
        });

        changeBoard.setOnAction(event -> {
            mainCtrl.showStartPage();
        });

        disconnect.setOnAction(event -> {
            disconnect();
        });

        settings.setOnMouseClicked(event -> {
            settingsMenu.show(settings, event.getScreenX(), event.getScreenY());
        });
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureSettings();
    }

    /**
     * Uses the method from main ctrl to show the tag list when the button is clicked
     */
    public void showAllTagsList() {
        mainCtrl.showAllTagsList();
    }

    public void disconnect() {
        boardOverviewService.closeServerConnection();
        mainCtrl.showClientConnectPage();
    }
}
