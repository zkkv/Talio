package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class CreateBoardCtrl {
    private final BoardOverviewService boardOverviewService;

    private final BoardUserIdentifier boardUserIdentifier;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField boardTitle;

    @Inject
    public CreateBoardCtrl(BoardOverviewService boardOverviewService,
                           BoardUserIdentifier boardUserIdentifier, MainCtrl mainCtrl){
        this.boardOverviewService = boardOverviewService;
        this.boardUserIdentifier = boardUserIdentifier;
        this.mainCtrl = mainCtrl;
    }

    public void createBoard(){
        if(boardTitle.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please type a correct name for the board");
            alert.show();
        }
        else {
            User user = boardUserIdentifier.getCurrentUser();
            Board board = boardOverviewService.createBoard(boardTitle.getText(),
                    user.getUserName());
            mainCtrl.subscribeForAllUpdates(board);
            mainCtrl.showStartPage();
        }
    }

    public void goBack(){
        mainCtrl.showStartPage();
    }

    public void setField(String s) {
        boardTitle.setText(s);
    }
}