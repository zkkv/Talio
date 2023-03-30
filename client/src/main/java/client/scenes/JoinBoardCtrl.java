package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
import javafx.fxml.FXML;
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
        user= boardOverviewService.addBoardToUser(boardKey.getText(), user.getUserName());
        boardIdentifier.setCurrentUser(user);
        Board board = boardOverviewService.getBoardByKey(boardKey.getText());
        boardIdentifier.setCurrentBoard(board);
        mainCtrl.subscribeForUpdates(board);
        mainCtrl.showBoardPage();
    }
}
