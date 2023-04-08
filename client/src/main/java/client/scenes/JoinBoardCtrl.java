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

    public void joinBoard(){
        User user = boardIdentifier.getCurrentUser();
        try {
            if (user.getJoinedBoards().contains(boardOverviewService
                    .getBoardByKey(boardKey.getText()))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.show();
                alert.setContentText("You have already joined this board");
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.show();
            alert.setContentText("There doesn't exist such board");
        }
        catch (BadRequestException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.show();
            alert.setContentText("Board key field can't be left blank");
        }
    }

    public void goBack(){
        mainCtrl.showStartPage();
    }
}