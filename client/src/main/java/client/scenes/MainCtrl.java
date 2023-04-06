/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import commons.Board;
import commons.Card;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private Stage listMenuStage;
    private Stage tagsListStage;
    private Stage tagDetailsStage;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene board;

    private ClientConnectCtrl clientConnectCtrl;
    private Scene clientConnect;

    private StartPageCtrl startPageCtrl;
    private Scene startPage;

    private ListMenuCtrl listMenuCtrl;
    private Scene listMenu;

    private CreateBoardCtrl createBoardCtrl;
    private Scene createBoard;

    private BoardSettingsCtrl boardSettingsCtrl;
    private Scene boardSettings;

    private UserPageCtrl userPageCtrl;
    private Scene userPage;

    private JoinBoardCtrl joinBoardCtrl;
    private Scene joinBoard;

    private AdminLoginCtrl adminLoginCtrl;
    private Scene adminLogin;

    private CardDetailsCtrl cardDetailsCtrl;

    private Scene cardDetails;

    private TagsListCtrl tagsListCtrl;
    private Scene tagsList;

    private TagDetailsCtrl tagDetailsCtrl;
    private Scene tagDetails;
    private TagsInCardCtrl tagsInCardCtrl;
    private Scene tagsInCard;

    @SuppressWarnings("checkstyle:methodlength")
    public void initialize(Stage primaryStage,
                           Pair<BoardOverviewCtrl, Parent> board,
                           Pair<ClientConnectCtrl, Parent> clientConnect,
                           Pair<StartPageCtrl, Parent> startPage,
                           Pair<ListMenuCtrl, Parent> listMenu,
                           Pair<CreateBoardCtrl, Parent> createBoard,
                           Pair<BoardSettingsCtrl, Parent> boardSettings,
                           Pair<UserPageCtrl,Parent> userPage,
                           Pair<JoinBoardCtrl,Parent> joinBoard,
                           Pair<AdminLoginCtrl,Parent> adminLogin,
                           Pair<CardDetailsCtrl, Parent> cardDetails,
                           Pair<TagsListCtrl,Parent> tagsList,
                           Pair<TagDetailsCtrl, Parent> tagDetails,
                           Pair<TagsInCardCtrl, Parent> tagsInCard) {
        this.primaryStage = primaryStage;
        addIcons(primaryStage);

        this.clientConnectCtrl = clientConnect.getKey();
        this.clientConnect = new Scene(clientConnect.getValue());

        this.startPageCtrl = startPage.getKey();
        this.startPage = new Scene(startPage.getValue());

        this.boardOverviewCtrl = board.getKey();
        this.board = new Scene(board.getValue());

        this.listMenuCtrl = listMenu.getKey();
        this.listMenu = new Scene(listMenu.getValue());
        this.listMenuStage = new Stage();
        this.listMenuStage.setScene(this.listMenu);

        this.createBoardCtrl = createBoard.getKey();
        this.createBoard = new Scene(createBoard.getValue());

        this.boardSettingsCtrl = boardSettings.getKey();
        this.boardSettings = new Scene(boardSettings.getValue());

        this.userPageCtrl = userPage.getKey();
        this.userPage = new Scene(userPage.getValue());

        this.joinBoardCtrl = joinBoard.getKey();
        this.joinBoard = new Scene(joinBoard.getValue());

        this.adminLoginCtrl = adminLogin.getKey();
        this.adminLogin = new Scene(adminLogin.getValue());

        this.cardDetailsCtrl = cardDetails.getKey();
        this.cardDetails = new Scene(cardDetails.getValue());

        this.tagsListCtrl = tagsList.getKey();
        this.tagsList = new Scene(tagsList.getValue());
        this.tagsListStage = new Stage();
        this.tagsListStage.setScene(this.tagsList);
        tagsListStage.initModality(Modality.APPLICATION_MODAL);

        this.tagDetailsCtrl = tagDetails.getKey();
        this.tagDetails = new Scene(tagDetails.getValue());
        this.tagDetailsStage = new Stage();
        this.tagDetailsStage.setScene(this.tagDetails);
        tagDetailsStage.initModality(Modality.APPLICATION_MODAL);

        this.tagsInCardCtrl = tagsInCard.getKey();
        this.tagsInCard = new Scene(tagsInCard.getValue());

        showClientConnectPage();
        primaryStage.show();
    }

    /**
     * Adds the icons to the application
     *
     * @param stage the stage for which the icons need to be set
     */
    private static void addIcons(Stage stage) {
        /* Icon created by Freepik - Flaticon */
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon16.png"));
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon32.png"));
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon64.png"));
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon128.png"));
    }

    public void showClientConnectPage() {
        primaryStage.setTitle("Talio: Client connect");
        primaryStage.setScene(clientConnect);
        setMinSize();
    }

    public void showUserPage(){
        primaryStage.setTitle("Talio: User selection");
        primaryStage.setScene(userPage);
    }

    public void showJoinBoard(){
        primaryStage.setTitle("Talio: Join Board");
        primaryStage.setScene(joinBoard);
    }

    public void showStartPage() {
        primaryStage.setTitle("Talio: Start page");
        primaryStage.setScene(startPage);
        startPageCtrl.initBoardList();
    }

    public void showCreateBoardPage() {
        primaryStage.setTitle("Talio: Create a new Board");
        primaryStage.setScene(createBoard);
    }

    public void showBoardPage() {
        primaryStage.setTitle("Talio: Board page");
        boardOverviewCtrl.drawBoard();
        primaryStage.setScene(board);
    }

    public void subscribeForAllUpdates(Board board){
        boardOverviewCtrl.subscribeForUpdates(board);
    }

    public void showBoardSettings(String title) {
        primaryStage.setTitle("Talio: Board Settings");
        boardSettingsCtrl.setBoardKey();
        boardSettingsCtrl.setBoardTitle(title);
        primaryStage.setScene(boardSettings);
    }

    public void changeName(Label label, String title){
        label.setText(title);
    }

    public void showListMenu(){
        if(!listMenuStage.isShowing()){
            listMenuStage.setTitle("Talio: List Menu");
            listMenuStage.show();
        }
        else{
            listMenuStage.hide();
        }
    }

    /**
     * This method changes the primaryStage to the card details scene
     * and also the title of the stage.
     * It selects the card object, its container and its label
     * which are passed on to cardDetalsCtrl
     * @param title
     * @param cardEntity
     */
    public void showCardDetails(String title, Card cardEntity) {
        primaryStage.setTitle("Talio: Card Details");
        cardDetailsCtrl.setTitle(title);
        primaryStage.setScene(cardDetails);
        cardDetailsCtrl.setCard(cardEntity);
        cardDetailsCtrl.addRetrievedSubTasks(cardEntity);
        cardDetailsCtrl.setDescription(cardEntity);
        cardDetailsCtrl.updateProgressBar();
        initTagsInCardDetails();
    }

    public void configureSaveButton(Card card, GridPane gridPane){
        cardDetailsCtrl.configureSaveButton(card,gridPane);
    }

    public void setMinSize(){
        primaryStage.setMinHeight(550);
        primaryStage.setMinWidth(800);
    }
    public void setMinSizeForCardDetails(){
        primaryStage.setMinHeight(170);
        primaryStage.setMinWidth(297);
    }
    public void closeMenu (){
        listMenuStage.hide();
    }

    /**
     * Sets the scene to adminLogin and passes the admin password to adminLoginCtrl
     *
     * @param pass  String with the admin password
     * @author      Kirill Zhankov
     */
    public void showAdminLogin(String pass){
        primaryStage.setTitle("Talio: Admin Login");
        primaryStage.setScene(adminLogin);
        adminLoginCtrl.setPass(pass);
    }

    /**
     * Pops up a scene with all the tags in the current board
     */
    public void showAllTagsList() {
        tagsListStage.setTitle("Talio: Tag List");
        tagsListStage.setMinWidth(300);
        tagsListStage.setMinHeight(450);
        tagsListStage.setResizable(false);
        tagDetailsStage.setOnCloseRequest(e -> tagsListCtrl.stopPolling());
        tagsListStage.show();
        addIcons(tagsListStage);
        tagsListCtrl.drawTags();
    }

    /**
     * A method to show the tags in the card
     * @param card is the card from which to get the tags
     */
    public void showAllTagsListWithinACard(Card card) {
        primaryStage.setTitle("Talio: Card Tags");
        tagsInCardCtrl.openTagsInCard(card);
        primaryStage.setScene(tagsInCard);
    }

    /**
     * A method with which we show the tags of the cards inside the card details
     */
    public void initTagsInCardDetails(){
        cardDetailsCtrl.addRetrievedTags();
    }

    /**
     * A method which shows the scene for editing the tag
     */
    public void showTagDetails() {
        tagDetailsStage.setTitle("Talio: Tag Details");
        tagDetailsStage.setResizable(false);
        tagDetailsStage.show();
        addIcons(tagDetailsStage);
    }

}