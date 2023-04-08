package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AdminLoginCtrl {
    private final MainCtrl mainCtrl;

    private final BoardUserIdentifier boardUserIdentifier;

    private final BoardOverviewService boardOverviewService;

    private String pass;
    @FXML
    private TextField password;

    @Inject
    public AdminLoginCtrl(MainCtrl mainCtrl, BoardUserIdentifier boardUserIdentifier,
                          BoardOverviewService boardOverviewService) {
        this.mainCtrl = mainCtrl;
        this.boardUserIdentifier = boardUserIdentifier;
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

    /**
     * Allows admin to proceed to the next screen if the
     * password is correct or shows an alert otherwise.
     *
     * @author      Kirill Zhankov
     */
    public void loginAsAdmin(){
        if(password.getText().equals(pass)){
            boardUserIdentifier.setAdmin(true);
            mainCtrl.showStartPage();

            boardOverviewService.registerForUpdates("/topic/board",Board.class,board -> {
                Platform.runLater(()->{
                    if(boardUserIdentifier.isAdmin()){
                        mainCtrl.showStartPage();
                    }
                });
            });
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setTitle("Wrong Password");
            alert.setHeaderText(null);
            alert.setContentText("Please try again with a different password");
            alert.showAndWait();
        }
    }

    /**
     * Returns admin to the board selection page.
     *
     * @author      Kirill Zhankov
     */
    public void goBack(){
        mainCtrl.showStartPage();
    }

    /**
     * Sets admin password to pass.
     *
     * @param pass  string to set password to
     * @author      Kirill Zhankov
     */
    public void setPass(String pass) {
        this.pass = pass;
    }
}