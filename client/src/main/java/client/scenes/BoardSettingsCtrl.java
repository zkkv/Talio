package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class BoardSettingsCtrl {

    private final MainCtrl mainCtrl;

    private final BoardUserIdentifier boardIdentifier;
    private final BoardOverviewService boardOverviewService;

    @FXML
    private TextField boardTitle;

    @FXML
    private Label boardKey;

    @Inject
    public BoardSettingsCtrl(MainCtrl mainCtrl, BoardUserIdentifier boardIdentifier,
                             BoardOverviewService boardOverviewService) {
        this.mainCtrl = mainCtrl;
        this.boardIdentifier = boardIdentifier;
        this.boardOverviewService = boardOverviewService;
    }

    public void editBoardName() {
        Board board = boardOverviewService.updateBoardTitle(
            boardIdentifier.getCurrentBoard(),
            boardTitle.getText());
        boardIdentifier.setCurrentBoard(board);
        mainCtrl.showBoardPage();
    }

    public void removeBoard() {
        boardOverviewService.removeBoard(boardIdentifier.getCurrentBoard());
        mainCtrl.showStartPage();
    }

    public void leaveBoard() {
        User user = boardIdentifier.getCurrentUser();
        user = boardOverviewService.removeBoardForUser(boardIdentifier.getCurrentBoard(),
            user.getUserName());
        boardIdentifier.setCurrentUser(user);
        mainCtrl.showStartPage();
    }

    public void copyKeyToClipboard(){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(boardKey.getText());
        clipboard.setContent(clipboardContent);
    }

    public void goBack(){
        mainCtrl.showBoardPage();
    }

    public void setBoardKey(){
        boardKey.setText(boardIdentifier.getCurrentBoard().getKey());
    }

    public void setBoardTitle(String title) {
        boardTitle.setText(title);
    }
}