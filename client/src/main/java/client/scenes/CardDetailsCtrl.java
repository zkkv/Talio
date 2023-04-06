package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import client.services.TagsListService;
import com.google.inject.Inject;
import commons.Card;
import commons.SubTask;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;


public class CardDetailsCtrl {

    private final BoardOverviewService boardOverviewService;
    private final TagsListService tagsListService;

    private final MainCtrl mainCtrl;

    private final BoardUserIdentifier boardUserIdentifier;

    @FXML
    private TextField title;

    @FXML
    private VBox subtasks;

    @FXML
    private VBox tags;

    private Card card;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Button saveButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ProgressIndicator progressIndicator;

    @Inject
    public CardDetailsCtrl(BoardOverviewService boardOverviewService,
                           BoardUserIdentifier boardUserIdentifier,
                           MainCtrl mainCtrl,
                           TagsListService tagsListService) {
        this.boardOverviewService = boardOverviewService;
        this.boardUserIdentifier = boardUserIdentifier;
        this.mainCtrl = mainCtrl;
        this.tagsListService = tagsListService;
    }

    /**
     * The functionality of the button for closing the scene
     *
     * @author Sofia Dimieva
     */
    public void closeCardDetails(){
        mainCtrl.showBoardPage();
    }
    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void addSubTask() {
        SubTask subTask = new SubTask();
        subTask = boardOverviewService.addSubTask(subTask, card.getId(),
            boardUserIdentifier.getCurrentBoard());
        this.card.getTasks().add(subTask);
        subTaskSetUp(subTask, "SubTask", false);
        updateProgressBar();
    }

    /**
     * The method creates the subtask in the
     * front-end and adds  functionality to some of its buttons
     *
     * @author Sofia Dimieva
     * @param task the task object
     * @param taskName
     * @param checked
     */
    public void subTaskSetUp(SubTask task, String taskName, boolean checked) {
        HBox subTask = new HBox();
        subTask.setAlignment(Pos.CENTER_LEFT);

        CheckBox checkBox = new CheckBox();
        checkboxSetUp(task, checkBox, checked);

        Label name = new Label(taskName);
        name.setPrefWidth(150);

        TextField text = new TextField();
        text.setPrefWidth(150);

        subTask.getChildren().add(checkBox);

        Button delete = new Button("x");
        delete.setOnAction(event -> {
            deleteSubTask(task, subTask);
        });

        Button rename = new Button("rename");

        subTask.getChildren().add(name);

        editSubTaskMessage(name);

        subTask.getChildren().add(delete);

        editSubTask(task, subTask, rename, text, delete, name);

        subtasks.getChildren().add(subTask);
        rearrange(subTask,task);
    }

    /**
     * The method sets up the checkbox and adds a lister which changes the value
     * in the object whenever the checkbox is ticked or unticked
     *
     * @author Sofia Dimieva
     * @param subTask
     * @param checkBox
     * @param checked
     */
    public void checkboxSetUp(SubTask subTask, CheckBox checkBox, boolean checked) {
        checkBox.selectedProperty().set(checked);
        checkBox.setPrefHeight(36);
        checkBox.setPrefWidth(36);

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if(oldValue.equals(true)) {
                boardOverviewService.updateCheckboxTask(subTask.getId(), false,
                    boardUserIdentifier.getCurrentBoard(), card);
            }
            else{
                boardOverviewService.updateCheckboxTask(subTask.getId(), true,
                    boardUserIdentifier.getCurrentBoard(), card);
            }
            updateProgressBar();
        });
    }
    /**
     * The method lets the user rename the subtask
     * @param task
     * @param subTask
     * @param rename
     * @param text
     * @param delete
     * @param name
     */
    public void editSubTask(SubTask task, HBox subTask, Button rename,
                            TextField text, Button delete, Label name) {
        rename.setOnAction(event -> {
            if (!text.getText().equals("")) {
                name.setText(text.getText());
                task.setName(text.getText());
                boardOverviewService.updateTitleSubTask(task.getId(),
                    text.getText(),boardUserIdentifier.getCurrentBoard(),card);
            }
            subTask.getChildren().remove(text);
            subTask.getChildren().remove(rename);
            subTask.getChildren().add(name);
            subTask.getChildren().add(delete);
        });
        name.setOnMouseClicked(event -> {
            subTask.getChildren().remove(name);
            subTask.getChildren().remove(delete);
            subTask.getChildren().add(text);
            subTask.getChildren().add(rename);
        });
    }

    /**
     * Shows a message when the subtask is hovered
     *
     * @author Sofia Dimieva
     * @param label
     */
    private void editSubTaskMessage(Label label) {
        Tooltip tooltip = new Tooltip("Click to edit");
        tooltip.setFont(Font.font("Verdana", 14));
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.ZERO);
        label.setTooltip(tooltip);
    }

    /**
     * When the scene is opened the method retrieves
     * all subtasks from the database
     *
     * @author Sofia Dimieva
     * @param card
     */
    public void addRetrievedSubTasks(Card card) {
        this.card = card;
        subtasks.getChildren().clear();
        for (SubTask subTask : card.getTasks())
            subTaskSetUp(subTask, subTask.getName(), subTask.isChecked());
    }

    /**
     * A method for deleting a subtask
     *
     * @author Sofia Dimieva
     * @param task
     * @param subTask
     */
    public void deleteSubTask(SubTask task, HBox subTask) {
        subtasks.getChildren().remove(subTask);
        boardOverviewService.removeSubTask(task, card.getId(),
            boardUserIdentifier.getCurrentBoard());
        this.card.getTasks().remove(task);
        updateProgressBar();
    }

    /**
     * This method saves all changes made to the title and the description
     * of a card.
     * It is called when the user pressed the save button.
     * @param card
     * @param cardContainer
     */
    public void configureSaveButton(Card card, GridPane cardContainer) {
        if(card.getDescription().trim().equals("")) {
            card.setDescription("");
        }


        saveButton.setOnAction(event -> {
            updateCardTitle(card);
            updateCardDescription(card);
            updateCardDescriptionIcon(card, cardContainer);
        });

        title.setText(card.getTitle());
        descriptionField.setText(card.getDescription());
    }

    /**
     * Updates card description on board overview corresponding to the text
     * in the description text area.
     * If the text area is empty or has white spaces, the card will have
     * an empty description
     * @param card card of which method changes the title of
     */
    public void updateCardDescription(Card card) {
        if(descriptionField.getText().trim().equals("")){
            boardOverviewService.updateCardDescription(card.getId(), " ",
                    boardUserIdentifier.getCurrentBoard());
            card.setDescription("");
        }
        else {
            String description = descriptionField.getText();
            boardOverviewService.updateCardDescription(card.getId(), description,
                    boardUserIdentifier.getCurrentBoard());
        }
    }

    /**
     * Selects and makes description icon visible or invisible,
     * depending on if the card has a description or not, respectively
     * @param card card object
     * @param cardContainer card container which holds the description icon
     */
    public void updateCardDescriptionIcon(Card card, GridPane cardContainer) {
        HBox icons = (HBox) cardContainer.getChildren().get(1);
        ImageView descriptionIcon = (ImageView) icons.getChildren().get(1);
        descriptionIcon.setVisible(card.hasDescription());
    }

    /**
     * Updates card title on board overview corresponding to the text
     * in the title text field.
     * If the text field is empty or has white spaces when the method is called
     * then a warning will pop up, otherwise the title will be saved
     * and changed.
     * @param card card of which method changes the title of
     */
    public void updateCardTitle(Card card) {
        if(title.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("You cannot leave the title field blank!");
            alert.showAndWait();
        }
        else {
            boardOverviewService.updateCardTitle(card.getId(), title.getText(),
                    boardUserIdentifier.getCurrentBoard());
        }

    }

    /**
     * The method sets an event for on drag detected
     * in the VBox containing all the subtasks
     *
     * @author Sofia Dimieva
     * @param hbox
     * @param subTaskObject
     */
    public void rearrange(HBox hbox, SubTask subTaskObject) {
        subtasks.setOnDragOver(event -> {
            if (event.getGestureSource() != subTaskObject && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        hbox.setOnDragDetected(event -> {
            Dragboard db = hbox.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            configureDragboardAndClipboard(subtasks, hbox, subTaskObject, event, db, content);

        });
    }

    /**
     * The method removes the selected subtasks and
     * lets the user see a snapshot of it while
     * choosing where to drop it
     *
     * @author Sofia Dimieva
     * @param vbox
     * @param hbox
     * @param subTask
     * @param event
     * @param db
     * @param content
     */
    private void configureDragboardAndClipboard(VBox vbox, HBox hbox, SubTask subTask,
                                                MouseEvent event, Dragboard db,
                                                ClipboardContent content) {
        content.putString(subTask.getName());
        db.setContent(content);
        db.setDragView(hbox.snapshot(null, null));
        event.consume();

        vbox.getChildren().remove(hbox);
        this.card.getTasks().remove(subTask);
        boardOverviewService.updateCardSubTasks( this.card.getId(),
                this.card.getTasks(), boardUserIdentifier.getCurrentBoard());
        configureCardListVBoxOnDragDropped(subTask, hbox, vbox);
    }

    /**
     *The method checks where is the tasks
     * dropped and adds it on the correct index
     *
     * @author Sofia Dimieva
     * @param subTask
     * @param hbox
     * @param vbox
     */
    private void configureCardListVBoxOnDragDropped(SubTask subTask, HBox hbox, VBox vbox) {
        subtasks.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                int dropIndex = -1;
                //find the right place in the subtask list
                for (int i = 0; i < vbox.getChildren().size(); i++) {
                    Node child = vbox.getChildren().get(i);
                    if (event.getY() <= child.getBoundsInParent().getMinY() +
                            child.getBoundsInParent().getHeight() / 2) {
                        dropIndex = i;
                        break;
                    }
                }
                //if it is not dropped on an element from the list
                if (dropIndex < 0) {
                    vbox.getChildren().add(vbox.getChildren().size(), hbox);
                    this.card.getTasks().add(vbox.getChildren().size()-1, subTask);
                    boardOverviewService.updateCardSubTasks( this.card.getId(),
                            this.card.getTasks(), boardUserIdentifier.getCurrentBoard());
                }
                else {
                    vbox.getChildren().add(dropIndex, hbox);
                    this.card.getTasks().add(dropIndex, subTask);
                    boardOverviewService.updateCardSubTasks( this.card.getId(),
                            this.card.getTasks(), boardUserIdentifier.getCurrentBoard());
                }

            }
            success = true;
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Updates the progress bar when a subtask is added/removed and when a checkbox is
     * checked/unchecked.
     *
     * @author Diana Sutac
     */
    public void updateProgressBar() {
        int numberOfChecked = 0;

        for(Node node : subtasks.getChildren()) {
            if(node instanceof HBox) {
                HBox subTask = (HBox) node;
                CheckBox checkBox = (CheckBox) subTask.getChildren().get(0);
                if(checkBox.isSelected()) {
                    numberOfChecked++;
                }
            }
        }

        int numberOfSubTasks = subtasks.getChildren().size();
        double progress = (double) numberOfChecked / numberOfSubTasks;
        progressBar.setProgress(progress);
        progressIndicator.setProgress(progress);
    }

    /**
     * A method to show the scene with the list of tags of the cards
     * and the list of tags of the board
     */
    public void addTag() {
        mainCtrl.showAllTagsListWithinACard(card);
    }

    /**
     * A method which shows the tags inside the card details
     */
    public void addRetrievedTags() {
        tags.setAlignment(Pos.TOP_CENTER);
        tags.getChildren().clear();
        var tagsFromCard = tagsListService.getAllTagsFromCard(card.getId());
        for (Tag tag : tagsFromCard) {
            drawTagForCard(tag);
        }
    }

    /**
     * A helper method to make the new tag
     * @param tag
     */
    private void drawTagForCard(Tag tag) {
        Button tagButton = new Button();
        HBox tagBox = new HBox();

        configureTagButtonForCard(tagBox, tagButton, tag);

        tagBox.getChildren().add(tagButton);
        tags.getChildren().add(tagBox);
    }

    /**
     * A helper method to show the tag in its colors
     * @param tagBox is the Hbox in which is our button
     * @param tagButton the button with which you could delete the card easily
     * @param tag the tag which to remove from the card if it is pressed
     */
    private void configureTagButtonForCard(HBox tagBox, Button tagButton, Tag tag) {
        tagButton.setOnAction(event -> {
            tags.getChildren().remove(tagBox);
            tagsListService.removeTagFromCard(tag, card.getId(),
                boardUserIdentifier.getCurrentBoard());
        });

        tagButton.setFocusTraversable(false);
        tagButton.setAlignment(Pos.CENTER);
        tagButton.setMnemonicParsing(false);
        tagButton.setPrefHeight(30);
        tagButton.setPrefWidth(140);
        tagButton.setMinHeight(30);
        tagButton.setMinWidth(140);

        String color = "rgb(" + tag.getRed() + ", " + tag.getGreen() + ", " + tag.getBlue() + ");";
        String title = tag.getTitle();
        tagButton.setStyle("-fx-border-color: black; -fx-background-color: " + color);
        tagButton.setText(title);

        tagBox.setStyle("-fx-start-margin: 20; -fx-end-margin: 20");
        tagBox.setSpacing(3);
        tagBox.setPadding(new Insets(5, 20, 5, 20));
        tagBox.setAlignment(Pos.CENTER);
        tagBox.setFillHeight(true);
    }

    public void setDescription(Card cardEntity) {
        descriptionField.setText(cardEntity.getDescription());
    }
}