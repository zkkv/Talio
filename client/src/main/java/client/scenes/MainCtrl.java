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
import commons.CardList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private Stage listMenuStage;
    private Stage tagsListStage;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene board;

    private AddTaskCtrl addTaskCtrl;

    private Scene addTask;

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

    private AdminOverviewCtrl adminOverviewCtrl;
    private Scene adminOverview;

    private TagsListCtrl tagsListCtrl;
    private Scene tagsList;

    public void initialize(Stage primaryStage,
                           Pair<BoardOverviewCtrl, Parent> board,
                           Pair<ClientConnectCtrl, Parent> clientConnect,
                           Pair<StartPageCtrl, Parent> startPage,
                           Pair<AddTaskCtrl, Parent> addTask,
                           Pair<ListMenuCtrl, Parent> listMenu,
                           Pair<CreateBoardCtrl, Parent> createBoard,
                           Pair<BoardSettingsCtrl, Parent> boardSettings,
                           Pair<UserPageCtrl,Parent> userPage,
                           Pair<JoinBoardCtrl,Parent> joinBoard,
                           Pair<AdminLoginCtrl,Parent> adminLogin,
                           Pair<AdminOverviewCtrl,Parent> adminOverview,
                           Pair<TagsListCtrl,Parent> tagsList) {
        this.primaryStage = primaryStage;

        /* Icon created by Freepik - Flaticon */
        primaryStage.getIcons().add(new Image("file:client/src/main/resources/img/icon16.png"));
        primaryStage.getIcons().add(new Image("file:client/src/main/resources/img/icon32.png"));
        primaryStage.getIcons().add(new Image("file:client/src/main/resources/img/icon64.png"));
        primaryStage.getIcons().add(new Image("file:client/src/main/resources/img/icon128.png"));

        this.clientConnectCtrl = clientConnect.getKey();
        this.clientConnect = new Scene(clientConnect.getValue());

        this.startPageCtrl = startPage.getKey();
        this.startPage = new Scene(startPage.getValue());

        this.boardOverviewCtrl = board.getKey();
        this.board = new Scene(board.getValue());

        this.addTaskCtrl = addTask.getKey();
        this.addTask = new Scene(addTask.getValue());

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

        this.adminOverviewCtrl = adminOverview.getKey();
        this.adminOverview = new Scene(adminOverview.getValue());

        this.tagsListCtrl = tagsList.getKey();
        this.tagsList = new Scene(tagsList.getValue());
        this.tagsListStage = new Stage();
        this.tagsListStage.setScene(this.tagsList);

        showClientConnectPage();
        primaryStage.show();
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

    public void subscribeForUpdates(Board board){
        boardOverviewCtrl.subscribeForUpdates(board);
    }

    public void showAddTask(Label label) {
        primaryStage.setTitle("Talio: Adding Task");
        addTaskCtrl.setLabel(label);
        primaryStage.setScene(addTask);
        setMinSizeForCardDetails();
    }

    public void showBoardSettings() {
        primaryStage.setTitle("Talio: Board Settings");
        boardSettingsCtrl.setBoardKey();
        primaryStage.setScene(boardSettings);
    }

    public void changeName(Label label, String title){
        label.setText(title);
    }

    public void showListMenu(Button button, CardList cardList, BorderPane borderPane){
        listMenuCtrl.setCardListBorderPane(cardList, borderPane);
        if(!listMenuStage.isShowing()){
            listMenuStage.setTitle("Talio: List Menu");
            listMenuStage.show();
        }
        else{
            listMenuStage.hide();
        }
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

    public void showAdminOverview(){
        primaryStage.setTitle("Talio: Admin Overview");
        primaryStage.setScene(adminOverview);
        adminOverviewCtrl.initBoardList();
    }

    /**
     * Pops up a scene with all the tags in the current board
     */
    public void showAllTagsList() {
        if(!tagsListStage.isShowing()){
            tagsListStage.setTitle("Talio: Tag List");
            tagsListStage.show();
        }
        else{
            tagsListStage.hide();
        }
    }

}