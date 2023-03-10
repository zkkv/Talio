package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.awt.*;

public class HomeScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private HBox panel;
    @FXML
    private VBox button;

    @FXML
    private VBox list;

    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    public void createList(){
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX(21.0);
        vbox.setLayoutY(93.0);
        vbox.setPrefHeight(36.0);
        vbox.setPrefWidth(126.0);
        vbox.setStyle("-fx-background-color: #d9cdad; -fx-border-color: black;");
        panel.getChildren().add(vbox);
    }

    public void addTask() {
        mainCtrl.showAddTask();
        VBox task = new VBox();
        task.setAlignment(Pos.CENTER);
        task.setPrefHeight(36);
        task.setPrefWidth(100);
        task.setStyle("-fx-border-color: black");
        Label title = new Label("Card");
        title.setFont(new Font(17));
        list.getChildren().remove(button);
        list.getChildren().add(task);
        task.getChildren().add(title);
        list.getChildren().add(button);
    }

    public void disconnect() {
        ServerUtils.closeConnection();
        mainCtrl.showClientConnectPage();
    }

}
