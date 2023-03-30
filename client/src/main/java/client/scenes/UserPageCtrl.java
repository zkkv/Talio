package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UserPageCtrl {

    private final BoardOverviewService boardOverviewService;

    private final BoardUserIdentifier boardUserIdentifier;

    private final MainCtrl mainCtrl;
    @FXML
    private TextField userName;

    @Inject
    public UserPageCtrl(BoardOverviewService boardOverviewService,
                        BoardUserIdentifier boardUserIdentifier, MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.boardUserIdentifier = boardUserIdentifier;
        this.mainCtrl = mainCtrl;
    }

    public void logIn(){
        //TODO check if it exists
        boardUserIdentifier.setCurrentUser(boardOverviewService.getUser(userName.getText()));
        mainCtrl.showStartPage();
    }
    public void createUser(){
        //TODO check if it exists already
        boardUserIdentifier.setCurrentUser(boardOverviewService.createUser(userName.getText()));
        mainCtrl.showStartPage();
    }
}