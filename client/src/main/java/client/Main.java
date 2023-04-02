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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @SuppressWarnings("checkstyle:methodlength")
    @Override
	public void start(Stage primaryStage) throws IOException {

        var board = FXML.load(
                BoardOverviewCtrl.class,
                "client", "scenes", "BoardOverview.fxml");
        var clientConnect = FXML.load(
				ClientConnectCtrl.class,
				"client", "scenes", "ClientConnect.fxml");
        var startPage = FXML.load(
				StartPageCtrl.class,
                "client", "scenes", "StartPage.fxml");
        var addTask = FXML.load(
				AddTaskCtrl.class,
				"client", "scenes", "AddTask.fxml");
        var listMenu = FXML.load(
				ListMenuCtrl.class,
				"client", "scenes", "ListMenu.fxml");
        var createBoard = FXML.load(
				CreateBoardCtrl.class,
                "client", "scenes", "CreateBoard.fxml");
        var boardSettings = FXML.load(
				BoardSettingsCtrl.class,
                "client", "scenes", "BoardSettings.fxml");
        var joinBoard = FXML.load(
				JoinBoardCtrl.class,
                "client", "scenes", "JoinBoard.fxml");
        var userPage = FXML.load(
				UserPageCtrl.class,
                "client", "scenes", "UserPage.fxml");
        var adminLogin = FXML.load(
				AdminLoginCtrl.class,
				"client", "scenes", "AdminLogin.fxml");
        var adminOverview = FXML.load(
				AdminOverviewCtrl.class,
                "client", "scenes", "AdminOverview.fxml");
        var cardDetails = FXML.load(
				CardDetailsCtrl.class,
				"client", "scenes", "CardDetails.fxml");
        var tagsList = FXML.load(
				TagsListCtrl.class,
            "client", "scenes", "TagsList.fxml");


        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        mainCtrl.initialize(
                primaryStage,
                board,
                clientConnect,
				startPage,
				addTask,
				listMenu,
				createBoard,
                boardSettings,
				userPage,
				joinBoard,
				adminLogin,
				adminOverview,
				cardDetails,
                tagsList);

        primaryStage.setOnCloseRequest(event -> tagsList.getKey().stopPolling());
    }
}
