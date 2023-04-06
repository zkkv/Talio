package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong Password");
            alert.setContentText("Please try again with a different password");
            alert.show();
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