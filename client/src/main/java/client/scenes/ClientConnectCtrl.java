package client.scenes;

import client.services.BoardOverviewService;
import com.google.inject.Inject;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientConnectCtrl {

    private final MainCtrl mainCtrl;

    private final BoardOverviewService boardOverviewService;

    @FXML
    private TextField ipAddress;

    @Inject
    public ClientConnectCtrl(MainCtrl mainCtrl, BoardOverviewService boardOverviewService) {
        this.mainCtrl = mainCtrl;
        this.boardOverviewService = boardOverviewService;
    }

    public void connect() {
        connectTo(ipAddress.getText());
    }

    public void connectToDefaultServer() {
        connectTo("localhost:8080");
    }

    private void connectTo(String server) {
        try {
            boardOverviewService.setServerAddress(server);
            boardOverviewService.testServerConnection();
            mainCtrl.subscribeForUpdates();
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
