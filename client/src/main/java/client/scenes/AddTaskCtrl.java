package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddTaskCtrl {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

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

    public void create() {
        title.clear();
        mainCtrl.showBoardPage();
    }



}
