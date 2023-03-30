package client.scenes;

import client.services.BoardIdentifier;
import client.services.BoardOverviewService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BoardSettingsCtrl {

    private final MainCtrl mainCtrl;

    private final BoardIdentifier boardIdentifier;
    private final BoardOverviewService boardOverviewService;

    @FXML
    private TextField boardTitle;


    @Inject
    public BoardSettingsCtrl(MainCtrl mainCtrl, BoardIdentifier boardIdentifier,
                             BoardOverviewService boardOverviewService) {
        this.mainCtrl = mainCtrl;
        this.boardIdentifier = boardIdentifier;
        this.boardOverviewService = boardOverviewService;
    }
    public void editBoardName() {
        boardIdentifier.setCurrentBoard(boardOverviewService.updateBoardTitle(
                boardIdentifier.getCurrentBoard(),
                boardTitle.getText()));
        mainCtrl.showBoardPage();
    }

    public void removeBoard() {
        boardOverviewService.removeBoard(boardIdentifier.getCurrentBoard());
        mainCtrl.showStartPage();
    }

    public void goBack(){
        mainCtrl.showBoardPage();
    }

}
