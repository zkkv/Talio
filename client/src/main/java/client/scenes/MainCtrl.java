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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private Stage listMenuStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private HomeScreenCtrl homeScreenCtrl;
    private Scene home;

    private AddTaskCtrl addTaskCtrl;

    private Scene addTask;

    private ClientConnectCtrl clientConnectCtrl;
    private Scene clientConnect;

    private StartPageCtrl startPageCtrl;
    private Scene startPage;

    private ListMenuCtrl listMenuCtrl;
    private Scene listMenu;

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<HomeScreenCtrl, Parent> home,
                           Pair<ClientConnectCtrl, Parent> clientConnect,
                           Pair<StartPageCtrl, Parent> startPage,
                           Pair<AddTaskCtrl, Parent> addTask, Pair<ListMenuCtrl, Parent> listMenu) {
        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.clientConnectCtrl = clientConnect.getKey();
        this.clientConnect = new Scene(clientConnect.getValue());

        this.startPageCtrl = startPage.getKey();
        this.startPage = new Scene(startPage.getValue());

        this.homeScreenCtrl = home.getKey();
        this.home = new Scene(home.getValue());

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
    }

    public void showStartPage() {
        primaryStage.setTitle("Talio: Start page");
        primaryStage.setScene(startPage);
    }

    public void showBoardPage() {
        primaryStage.setTitle("Talio: Board page");
        primaryStage.setScene(home);
    }

    public void loadBoardOverview(){
        homeScreenCtrl.addRetrievedCardLists();
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showAddTask(Label label) {
        primaryStage.setTitle("Talio: Adding Task");
        addTaskCtrl.setLabel(label);
        primaryStage.setScene(addTask);
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
    public void closeMenu (){
        listMenuStage.hide();
    }
}