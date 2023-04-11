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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.UnaryOperator;

public class JoinBoardCtrl {

    @FXML
    private TextField boardKey;

    @FXML
    private Label keyErrorLabel;

    private Timer keyErrorTimer;

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

    /**
     * Sets up board key constraints and the error message
     * which is shown in case they are violated.
     * Error message disappears in some time after no action is taken.
     *
     * @author Kirill Zhankov
     */
    public void setUpTextField() {
        final String REGEXP = "[a-zA-Z0-9]*";
        final int MAX_LENGTH = 10;
        final int SHOW_DURATION_MS = 4000;

        keyErrorLabel.setWrapText(true);
        keyErrorLabel.setTextAlignment(TextAlignment.JUSTIFY);
        keyErrorLabel.setFont(Font.font(15));
        keyErrorLabel.setText("Board key has to be exactly " + MAX_LENGTH
                + " characters long and can contain only letters and digits.");

        // This filter either returns the changed value of the field or null which indicates
        // incorrect input.
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String input = change.getControlNewText();
            if (input.matches(REGEXP) && input.length() <= MAX_LENGTH) {
                keyErrorLabel.setVisible(false);
                return change;
            }
            else {
                keyErrorLabel.setVisible(true);
                if (keyErrorTimer != null) {
                    keyErrorTimer.cancel();
                }
                keyErrorTimer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        keyErrorLabel.setVisible(false);
                    }
                };
                keyErrorTimer.schedule(task, SHOW_DURATION_MS);
                return null;
            }
        };

        // This thing tracks user input using the filter
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        boardKey.setTextFormatter(textFormatter);
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
                alert.setOnCloseRequest(event -> {
                    boardKey.setText("");
                });
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setHeaderText(null);
            alert.setTitle("Board doesn't exist");
            alert.setContentText("There doesn't exist such a board");
            alert.showAndWait();
            alert.setOnCloseRequest(event -> {
                boardKey.setText("");
            });}
        catch (BadRequestException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setHeaderText(null);
            alert.setTitle("Incorrect key");
            alert.setContentText("Board key field can't be left blank");
            alert.setOnCloseRequest(event -> {
                boardKey.setText("");
            });
            alert.showAndWait();
        }
    }

    public void goBack(){
        mainCtrl.showStartPage();
    }

    public void setField(String s) {
        boardKey.setText(s);
    }
}