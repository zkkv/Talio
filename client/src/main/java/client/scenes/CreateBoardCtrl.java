package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
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

public class CreateBoardCtrl {
    private final BoardOverviewService boardOverviewService;

    private final BoardUserIdentifier boardUserIdentifier;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField boardTitle;

    @FXML
    private Label nameErrorLabel;

    private Timer nameErrorTimer;

    @Inject
    public CreateBoardCtrl(BoardOverviewService boardOverviewService,
                           BoardUserIdentifier boardUserIdentifier, MainCtrl mainCtrl){
        this.boardOverviewService = boardOverviewService;
        this.boardUserIdentifier = boardUserIdentifier;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Sets up board key constraints and the error message
     * which is shown in case they are violated.
     * Error message disappears in some time after no action is taken.
     *
     * @author Kirill Zhankov
     */
    public void setUpTextField() {
        final String REGEXP = "[a-zA-Z0-9_ -]*";
        final int MAX_LENGTH = 30;
        final int SHOW_DURATION_MS = 6000;

        nameErrorLabel.setWrapText(true);
        nameErrorLabel.setTextAlignment(TextAlignment.JUSTIFY);
        nameErrorLabel.setFont(Font.font(15));
        nameErrorLabel.setText("Board name has to be no more than " + MAX_LENGTH
                + " characters long and can contain only letters, "
                + "digits, hyphen (-), underscore (_) and spaces. "
                + "It cannot start or end with a space.");

        // This filter either returns the changed value of the field or null which indicates
        // incorrect input.
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String input = change.getControlNewText();
            if (input.matches(REGEXP) && input.length() <= MAX_LENGTH) {
                nameErrorLabel.setVisible(false);
                return change;
            }
            else {
                nameErrorLabel.setVisible(true);
                if (nameErrorTimer != null) {
                    nameErrorTimer.cancel();
                }
                nameErrorTimer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        nameErrorLabel.setVisible(false);
                    }
                };
                nameErrorTimer.schedule(task, SHOW_DURATION_MS);
                return null;
            }
        };

        // This thing tracks user input using the filter
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        boardTitle.setTextFormatter(textFormatter);
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
        final String REGEXP = "[a-zA-Z0-9_-]+( [a-zA-Z0-9_-]+)*";
        String input = boardTitle.getText();

        if(input.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setTitle("Incorrect Name");
            alert.setHeaderText(null);
            alert.setContentText("Board name cannot be blank");
            alert.showAndWait();
        }
        else if (!input.matches(REGEXP)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setTitle("Incorrect Name");
            alert.setHeaderText(null);
            alert.setContentText("Board name cannot start or end with a space.");
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