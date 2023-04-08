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
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class JoinBoardCtrl {

    @FXML
    private TextField boardKey;

    private final BoardOverviewService boardOverviewService;

    private final MainCtrl mainCtrl;

    private final BoardUserIdentifier boardIdentifier;

    @Inject
    public JoinBoardCtrl(BoardOverviewService boardOverviewService,
                         MainCtrl mainCtrl, BoardUserIdentifier boardIdentifier) {
        this.boardOverviewService = boardOverviewService;
        this.mainCtrl = mainCtrl;
        this.boardIdentifier = boardIdentifier;
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

    public void joinBoard(){
        User user = boardIdentifier.getCurrentUser();
        try {
            if (user.getJoinedBoards().contains(boardOverviewService
                    .getBoardByKey(boardKey.getText()))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                addIcons((Stage) alert.getDialogPane().getScene().getWindow());
                alert.setTitle("Board has been joined before");
                alert.setHeaderText(null);
                alert.setContentText("You have already joined this board");
                alert.showAndWait();
            }
            else {
                user = boardOverviewService.addBoardToUser(boardKey.getText(), user.getUserName());
                boardIdentifier.setCurrentUser(user);
                Board board = boardOverviewService.getBoardByKey(boardKey.getText());
                boardIdentifier.setCurrentBoard(board);
                mainCtrl.subscribeForAllUpdates(board);
                mainCtrl.showBoardPage();
            }
        }
        catch (NotFoundException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setHeaderText(null);
            alert.setTitle("Board doesn't exist");
            alert.setContentText("There doesn't exist such a board");
            alert.showAndWait();
        }
        catch (BadRequestException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setHeaderText(null);
            alert.setTitle("Incorrect key");
            alert.setContentText("Board key field can't be left blank");
            alert.showAndWait();
        }
    }

    public void goBack(){
        mainCtrl.showStartPage();
    }
}