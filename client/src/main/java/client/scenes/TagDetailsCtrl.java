package client.scenes;

import client.services.BoardUserIdentifier;
import client.services.TagsListService;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.UnaryOperator;

public class TagDetailsCtrl {

    private final TagsListService tagsListService;
    private final MainCtrl mainCtrl;
    private final BoardUserIdentifier boardUserIdentifier;

    @FXML
    private Button nameChangeButton;
    @FXML
    private TextField nameTextField;

    @FXML
    private Label tagErrorLabel;

    private Timer tagErrorTimer;

    /**
     * A constructor for the class.
     *
     * @param tagsListService       the service it is going use
     * @param mainCtrl              the controller to which it is bounded
     * @param boardUserIdentifier   the identifier object used to get current board
     * @author                      Vinchentzo Bunardzhiev
     */
    @Inject
    public TagDetailsCtrl(TagsListService tagsListService,
                        MainCtrl mainCtrl,
                        BoardUserIdentifier boardUserIdentifier) {
        this.tagsListService = tagsListService;
        this.mainCtrl = mainCtrl;
        this.boardUserIdentifier = boardUserIdentifier;
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
     * Sets up board name constraints and the error message
     * which is shown in case they are violated.
     * Error message disappears in some time after no action is taken.
     *
     * @author Kirill Zhankov
     */
    public void setUpTextField() {
        final String REGEXP = "[a-zA-Z0-9_ \\-!@#$%^&*()~\"]*";
        final int MAX_LENGTH = 20;
        final int SHOW_DURATION_MS = 6000;

        tagErrorLabel.setWrapText(true);
        tagErrorLabel.setTextAlignment(TextAlignment.JUSTIFY);
        tagErrorLabel.setFont(Font.font(14));
        tagErrorLabel.setText("Tag has to be no more than " + MAX_LENGTH
                + " characters long and can contain only letters, "
                + "digits, spaces and any of: _-!@#$%^&*()~\" but "
                + "it cannot start or end with spaces.");

        // This filter either returns the changed value of the field or null which indicates
        // incorrect input.
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String input = change.getControlNewText();
            if (input.matches(REGEXP) && input.length() <= MAX_LENGTH) {
                tagErrorLabel.setVisible(false);
                return change;
            }
            else {
                tagErrorLabel.setVisible(true);
                if (tagErrorTimer != null) {
                    tagErrorTimer.cancel();
                }
                tagErrorTimer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        tagErrorLabel.setVisible(false);
                    }
                };
                tagErrorTimer.schedule(task, SHOW_DURATION_MS);
                return null;
            }
        };

        // This thing tracks user input using the filter
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        nameTextField.setTextFormatter(textFormatter);
    }


    /**
     * A method for changing the name of the tag when the button is pressed
     * @param tag the tag whose name should be changed
     * @param tagButton the button whose text should be changed
     */
    public void changeTagName(Tag tag, Button tagButton){
        nameTextField.setText(tag.getTitle());
        nameChangeButton.setOnAction(event -> {
            final String REGEXP = "\\S(.*\\S)?";
            String input = nameTextField.getText();

            if(input.equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                addIcons((Stage) alert.getDialogPane().getScene().getWindow());
                alert.setTitle("Incorrect Tag");
                alert.setHeaderText(null);
                alert.setContentText("Tag cannot be blank");
                alert.showAndWait();
            }
            else if (!input.matches(REGEXP)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                addIcons((Stage) alert.getDialogPane().getScene().getWindow());
                alert.setTitle("Incorrect Tag");
                alert.setHeaderText(null);
                alert.setContentText("Tag cannot start or end with spaces.");
                alert.showAndWait();
            }
            else {
                tagsListService.updateTagName(tag.getId(), nameTextField.getText(),
                        boardUserIdentifier.getCurrentBoard());
                mainCtrl.closeTagDetails();
            }
        });
    }

    /**
     * A method to set the text inside the text field
     * @param name the name to which the text field would be set
     */
    public void setName(String name) {
        this.nameTextField.setText(name);
    }

}
