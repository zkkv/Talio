package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class HomeScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private HBox panel;

    @FXML
    private AnchorPane anchorPane;

    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    public void createList(){
        BorderPane bp = new BorderPane();
        bp.setPrefHeight(274);
        bp.setPrefWidth(126);
        bp.setStyle("-fx-background-color: #d9cdad; -fx-border-color: black;");
        Button button = new Button(":");
        button.setAlignment(Pos.TOP_CENTER);
        button.setLayoutX(95.0);
        button.setLayoutY(6.0);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setMnemonicParsing(false);
        button.setStyle("-fx-background-color: #a3957c;");
        Label label = new Label();
        label.setAlignment(Pos.TOP_CENTER);
        label.setLayoutX(12.0);
        label.setLayoutY(9.0);
        label.setPrefHeight(18.0);
        label.setPrefWidth(95.0);
        label.setText("Name");
        Font font = new Font(16.0);
        label.setFont(font);
        AnchorPane ap = new AnchorPane();
        ap.setPrefHeight(34.0);
        ap.getChildren().add(label);
        ap.getChildren().add(button);
        bp.setTop(ap);
        VBox vbox = new VBox();
        vbox.setPrefHeight(221.0);
        vbox.setPrefWidth(109.0);
        vbox.setAlignment(Pos.CENTER);
        bp.setCenter(vbox);
        panel.getChildren().add(bp);
    }
}
