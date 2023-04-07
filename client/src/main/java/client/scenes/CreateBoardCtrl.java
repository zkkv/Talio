package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

    public void createBoard(){
        if(boardTitle.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setHeaderText(null);
            alert.setContentText("Please type a correct name for the board");
            alert.showAndWait();
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