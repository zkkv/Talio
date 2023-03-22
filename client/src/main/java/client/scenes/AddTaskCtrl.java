package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Pair;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddTaskCtrl {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private Label label;

    @FXML
    private TextField title;

    @FXML
    private Button editButton;

    @Inject
    public AddTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void configureEditButton(Card card){
        editButton.setOnAction(event -> {
            mainCtrl.changeName(label,title.getText());
            mainCtrl.showBoardPage();
            Pair<Card, String> request = new Pair<>(card, title.getText());
            server.updateCardTitle(request);
        });
    }
    public void cancel() {
        title.clear();
        mainCtrl.showBoardPage();
    }
    public void setLabel(Label label){
        this.label = label;
    }
}
