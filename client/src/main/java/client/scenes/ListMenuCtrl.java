package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ListMenuCtrl {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    @FXML
    private Button listMenuButton;
    @FXML
    private TextField listMenuTextField;


    @Inject
    public ListMenuCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void changeListLabel(TextField listLabela){
        listMenuButton.setOnAction(event -> listLabel.setText(listMenuTextField.getText()));
    }


}
