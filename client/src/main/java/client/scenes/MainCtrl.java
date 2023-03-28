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

    public void initialize(Stage primaryStage,
                           Pair<BoardOverviewCtrl, Parent> board,
                           Pair<ClientConnectCtrl, Parent> clientConnect,
                           Pair<StartPageCtrl, Parent> startPage,
                           Pair<AddTaskCtrl, Parent> addTask, Pair<ListMenuCtrl, Parent> listMenu) {
        this.primaryStage = primaryStage;

        /* Icon created by Freepik - Flaticon */
        primaryStage.getIcons().add(new Image("file:client/img/icon16.png"));
        primaryStage.getIcons().add(new Image("file:client/img/icon32.png"));
        primaryStage.getIcons().add(new Image("file:client/img/icon64.png"));
        primaryStage.getIcons().add(new Image("file:client/img/icon128.png"));

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

        showClientConnectPage();
        primaryStage.show();
    }

    public void showClientConnectPage() {
        primaryStage.setTitle("Talio: Client connect");
        primaryStage.setScene(clientConnect);
        setMinSize();

    }

    public void showStartPage() {
        primaryStage.setTitle("Talio: Start page");
        primaryStage.setScene(startPage);
    }

    public void showBoardPage() {
        primaryStage.setTitle("Talio: Board page");
        primaryStage.setScene(board);
    }

    public void loadBoardOverview(){
        boardOverviewCtrl.addRetrievedCardLists();
    }

    public void showAddTask(Label label) {
        primaryStage.setTitle("Talio: Adding Task");
        addTaskCtrl.setLabel(label);
        primaryStage.setScene(addTask);
        setMinSizeForCardDetails();
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
}