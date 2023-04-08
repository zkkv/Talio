package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

    public void logIn(){
        try {
            if(userName.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                addIcons((Stage) alert.getDialogPane().getScene().getWindow());
                alert.setTitle("Incorrect Username");
                alert.setContentText("Username cannot be blank!");
                alert.setOnCloseRequest(event -> {
                    userName.clear();
                });
                alert.setHeaderText(null);
                alert.showAndWait();
            }
            else {
                User user = boardOverviewService.getUser(userName.getText());
                boardUserIdentifier.setCurrentUser(user);
                mainCtrl.showStartPage();
                initBoardUpdates();
            }
        }
        catch (NotFoundException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setTitle("Username doesn't exist");
            alert.setContentText("This username doesn't exist. Try with a different name.");
            alert.setOnCloseRequest(event -> {
                userName.clear();
            });
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }
    public void createUser(){
        try {
            if(userName.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                addIcons((Stage) alert.getDialogPane().getScene().getWindow());
                alert.setContentText("Username cannot be blank!");
                alert.setOnCloseRequest(event -> {
                    userName.clear();
                });
                alert.setHeaderText(null);
                alert.showAndWait();
            }
            else {
                User user = boardOverviewService.createUser(userName.getText());
                boardUserIdentifier.setCurrentUser(user);
                mainCtrl.showStartPage();
            }
        }
        catch (BadRequestException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setTitle("Username doesn't exist");
            alert.setContentText("This username already exist. Try with a different name.");
            alert.setOnCloseRequest(event -> {
                userName.clear();
            });
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void initBoardUpdates(){
        List<Board> userBoards = boardOverviewService.getUserBoards(boardUserIdentifier
                .getCurrentUser().getUserName());
        for(Board board : userBoards){
            mainCtrl.subscribeForAllUpdates(board);
        }
    }

    public void disconnect() {
        boardOverviewService.closeServerConnection();
        mainCtrl.showClientConnectPage();
    }
}