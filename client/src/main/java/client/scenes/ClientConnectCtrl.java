package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        catch (IOException | ClassCastException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Connection Error");
            alert.setContentText("The application could not connect to the server. " +
                    "Check the correctness of the IP-address and try again.");

            Stage currentStage = (Stage) ipAddress.getScene().getWindow();
            alert.initOwner(currentStage);
            alert.showAndWait();
        }

    }

}
