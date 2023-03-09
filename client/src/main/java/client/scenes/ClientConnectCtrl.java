package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import java.io.IOException;

public class ClientConnectCtrl {

    private final MainCtrl mainCtrl;

    @FXML
    private TextField ipAddress;


    @Inject
    public ClientConnectCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void connect() {
        try {
            ServerUtils.setServer(ipAddress.getText());
            ServerUtils.testConnection();
            mainCtrl.showStartPage();
        }
        catch (IOException e) {
            System.out.println("Wrong ip");
        }

    }

}
