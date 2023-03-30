package client.scenes;

import client.services.BoardIdentifier;
import client.services.BoardOverviewService;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateBoardCtrl {
    private final BoardOverviewService boardOverviewService;

    private final BoardIdentifier boardIdentifier;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField boardTitle;

    @Inject
    public CreateBoardCtrl(BoardOverviewService boardOverviewService,
                           BoardIdentifier boardIdentifier, MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.boardIdentifier = boardIdentifier;
        this.mainCtrl = mainCtrl;
    }

    public void createBoard() {
        Board board = boardOverviewService.createBoard(boardTitle.getText());
        mainCtrl.subscribeForUpdates(board);
        mainCtrl.showStartPage();
    }
}