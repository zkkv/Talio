package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.UnaryOperator;

public class UserPageCtrl {

    private final BoardOverviewService boardOverviewService;

    private final BoardUserIdentifier boardUserIdentifier;

    private final MainCtrl mainCtrl;
    @FXML
    private TextField userName;

    @FXML
    private Label usernameErrorLabel;

    private Timer usernameErrorTimer;

    @Inject
    public UserPageCtrl(BoardOverviewService boardOverviewService,
                        BoardUserIdentifier boardUserIdentifier, MainCtrl mainCtrl) {
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

    /**
     * Sets up username constraints and the error message
     * which is shown in case they are violated.
     * Error message disappears in some time after no action is taken.
     *
     * @author Kirill Zhankov
     */
    public void setUpTextField() {
        final String REGEXP = "[a-zA-Z0-9_-]*";
        final int MAX_LENGTH = 25;
        final int SHOW_DURATION_MS = 6000;

        usernameErrorLabel.setWrapText(true);
        usernameErrorLabel.setTextAlignment(TextAlignment.JUSTIFY);
        usernameErrorLabel.setFont(Font.font(13));
        usernameErrorLabel.setText("Username has to be less than " + MAX_LENGTH
                + " characters long and can contain only letters, "
                + "digits, hyphen (-) and underscore (_).");
        
        // This filter either returns the changed value of the field or null which indicates
        // incorrect input.
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String input = change.getControlNewText();
            if (input.matches(REGEXP) && input.length() <= MAX_LENGTH) {
                usernameErrorLabel.setVisible(false);
                return change;
            }
            else {
                usernameErrorLabel.setVisible(true);
                if (usernameErrorTimer != null) {
                    usernameErrorTimer.cancel();
                }
                usernameErrorTimer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        usernameErrorLabel.setVisible(false);
                    }
                };
                usernameErrorTimer.schedule(task, SHOW_DURATION_MS);
                return null;
            }
        };

        // This thing tracks user input using the filter
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        userName.setTextFormatter(textFormatter);
    }

    public void logIn(){
        try {
            if(userName.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                addIcons((Stage) alert.getDialogPane().getScene().getWindow());
                alert.setTitle("Incorrect Username");
                alert.setContentText("Username cannot be blank!");
                alert.setOnCloseRequest(event -> {
                    userName.clear();
                });
                alert.setHeaderText(null);
                alert.showAndWait();
            }
            else {
                User user = boardOverviewService.getUser(userName.getText());
                boardUserIdentifier.setCurrentUser(user);
                mainCtrl.showStartPage();
                initBoardUpdates();
            }
        }
        catch (NotFoundException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setTitle("Username doesn't exist");
            alert.setContentText("This username doesn't exist. Try with a different name.");
            alert.setOnCloseRequest(event -> {
                userName.clear();
            });
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }
    public void createUser(){
        try {
            if(userName.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                addIcons((Stage) alert.getDialogPane().getScene().getWindow());
                alert.setContentText("Username cannot be blank!");
                alert.setTitle("Incorrect Username");
                alert.setOnCloseRequest(event -> {
                    userName.clear();
                });
                alert.setHeaderText(null);
                alert.showAndWait();
            }
            else {
                User user = boardOverviewService.createUser(userName.getText());
                boardUserIdentifier.setCurrentUser(user);
                mainCtrl.showStartPage();
            }
        }
        catch (BadRequestException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            addIcons((Stage) alert.getDialogPane().getScene().getWindow());
            alert.setTitle("Username doesn't exist");
            alert.setContentText("This username already exist. Try with a different name.");
            alert.setOnCloseRequest(event -> {
                userName.clear();
            });
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void initBoardUpdates(){
        List<Board> userBoards = boardOverviewService.getUserBoards(boardUserIdentifier
                .getCurrentUser().getUserName());
        for(Board board : userBoards){
            mainCtrl.subscribeForAllUpdates(board);
        }
    }

    public void disconnect() {
        boardOverviewService.closeServerConnection();
        mainCtrl.showClientConnectPage();
    }
}