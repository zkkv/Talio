package client.scenes;

import client.services.BoardOverviewService;
import com.google.inject.Inject;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

    /**
     * Adds the icons to the stage
     *
     * @param stage the stage for which the icons need to be set
     */
    private void addIcons(Stage stage) {
        /* Icon created by Freepik - Flaticon */
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon16.png"));
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon32.png"));
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon64.png"));
        stage.getIcons().add(new Image("file:client/src/main/resources/img/icon128.png"));
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
            mainCtrl.showUserPage();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setTitle("Connection Error");
            alert.setHeaderText(null);
            alert.setContentText("The application could not connect to the server. " +
                    "Check the correctness of the IP-address and try again.");

            Stage currentStage = (Stage) ipAddress.getScene().getWindow();
            alert.initOwner(currentStage);
            alert.showAndWait();
        }
    }
}
