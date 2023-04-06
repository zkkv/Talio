package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.List;

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
        try {
            if(userName.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User name cannot be blank!");
                alert.show();
                alert.setOnCloseRequest(event -> {
                    userName.clear();
                });
            }
            else {
                User user = boardOverviewService.getUser(userName.getText());
                boardUserIdentifier.setCurrentUser(user);
                mainCtrl.showStartPage();
                initBoardUpdates();
            }
        }
        catch (NotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("This username doesn't exist. Try with a different name.");
            alert.show();
            alert.setOnCloseRequest(event -> {
                userName.clear();
            });
        }
    }
    public void createUser(){
        try {
            if(userName.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User name cannot be blank!");
                alert.show();
                alert.setOnCloseRequest(event -> {
                    userName.clear();
                });
            }
            else {
                User user = boardOverviewService.createUser(userName.getText());
                boardUserIdentifier.setCurrentUser(user);
                mainCtrl.showStartPage();
            }
        }
        catch (BadRequestException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("This username already exist. Try with a different name.");
            alert.show();
            alert.setOnCloseRequest(event -> {
                userName.clear();
            });
        }
    }

    public void initBoardUpdates(){
        List<Board> userBoards = boardOverviewService.getUserBoards(boardUserIdentifier
                .getCurrentUser().getUserName());
        for(Board board : userBoards){
            mainCtrl.subscribeForAllUpdates(board);
        }
    }

    public void setField(String s) {
        userName.setText(s);
    }
}