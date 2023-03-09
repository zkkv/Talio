package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.awt.*;

public class HomeScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private HBox panel;
    @FXML
    private ScrollPane scroll;

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

}
