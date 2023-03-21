package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
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

    @Inject
    public AddTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void cancel() {
        title.clear();
        mainCtrl.showBoardPage();
    }
    public void setLabel(Label label){
        this.label = label;
    }
    public void create() {
        mainCtrl.changeName(label,title.getText());
        mainCtrl.showBoardPage();
    }
}
