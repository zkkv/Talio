package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
import javafx.fxml.FXML;
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
        User user = boardUserIdentifier.getCurrentUser();
        Board board = boardOverviewService.createBoard(boardTitle.getText(),user.getUserName());
        mainCtrl.subscribeForUpdates(board);
        mainCtrl.showStartPage();
    }
}