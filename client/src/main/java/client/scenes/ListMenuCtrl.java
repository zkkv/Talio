package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.UnaryOperator;

public class ListMenuCtrl {

    private final BoardOverviewService boardOverviewService;

    private final BoardUserIdentifier boardUserIdentifier;

    private final MainCtrl mainCtrl;

    @FXML
    private Button listMenuButton;
    @FXML
    private TextField listMenuTextField;

    @FXML
    private Label listNameErrorLabel;

    private Timer listNameErrorTimer;


    @Inject
    public ListMenuCtrl(BoardOverviewService boardOverviewService,
                        BoardUserIdentifier boardUserIdentifier,
                        MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.boardUserIdentifier = boardUserIdentifier;
        this.mainCtrl = mainCtrl;
    }


    /**
     * Sets up board name constraints and the error message
     * which is shown in case they are violated.
     * Error message disappears in some time after no action is taken.
     *
     * @author Kirill Zhankov
     */
    public void setUpTextField() {
        final String REGEXP = "[a-zA-Z0-9_ \\-!@#$%^&*()~\"]*";
        final int MAX_LENGTH = 30;
        final int SHOW_DURATION_MS = 6000;

        listNameErrorLabel.setWrapText(true);
        listNameErrorLabel.setTextAlignment(TextAlignment.JUSTIFY);
        listNameErrorLabel.setFont(Font.font(15));
        listNameErrorLabel.setText("Board name has to be no more than " + MAX_LENGTH
                + " characters long and can contain only letters, "
                + "digits, spaces and any of: _-!@#$%^&*()~\" but "
                + "it cannot start or end with spaces.");

        // This filter either returns the changed value of the field or null which indicates
        // incorrect input.
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String input = change.getControlNewText();
            if (input.matches(REGEXP) && input.length() <= MAX_LENGTH) {
                listNameErrorLabel.setVisible(false);
                return change;
            }
            else {
                listNameErrorLabel.setVisible(true);
                if (listNameErrorTimer != null) {
                    listNameErrorTimer.cancel();
                }
                listNameErrorTimer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        listNameErrorLabel.setVisible(false);
                    }
                };
                listNameErrorTimer.schedule(task, SHOW_DURATION_MS);
                return null;
            }
        };

        // This thing tracks user input using the filter
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        listMenuTextField.setTextFormatter(textFormatter);
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


    public void changeListLabel(CardList cardList, TextField listLabel){
        listMenuButton.setOnAction(event -> {
            final String REGEXP = "\\S(.*\\S)?";
            String input = listMenuTextField.getText();

            if(input.equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                addIcons((Stage) alert.getDialogPane().getScene().getWindow());
                alert.setTitle("Incorrect Name");
                alert.setContentText("List name cannot be left blank!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
            else if (!input.matches(REGEXP)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                addIcons((Stage) alert.getDialogPane().getScene().getWindow());
                alert.setTitle("Incorrect Name");
                alert.setHeaderText(null);
                alert.setContentText("List name cannot start or end with spaces.");
                alert.showAndWait();
            }
            else {
                listLabel.setText(listMenuTextField.getText());
                boardOverviewService.updateCardListTitle(cardList.getId(),
                        listLabel.getText(), boardUserIdentifier.getCurrentBoard());
            }
        });
    }
}