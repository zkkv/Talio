package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Card;
import commons.SubTask;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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

    private final MainCtrl mainCtrl;

    private final BoardUserIdentifier boardUserIdentifier;

    @FXML
    private Label title;

    @FXML
    private VBox subtasks;

    private Card card;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Button saveDescriptionButton;

    @Inject
    public CardDetailsCtrl(BoardOverviewService boardOverviewService,
                           BoardUserIdentifier boardUserIdentifier,
                           MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.boardUserIdentifier = boardUserIdentifier;
        this.mainCtrl = mainCtrl;
    }

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
        subTask = boardOverviewService.addSubTask(subTask, card.getId());
        subTaskSetUp(subTask, "SubTask", false);
    }

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

    public void checkboxSetUp(SubTask subTask, CheckBox checkBox, boolean checked) {
        checkBox.selectedProperty().set(checked);
        checkBox.setPrefHeight(36);
        checkBox.setPrefWidth(36);
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            subTask.setChecked(!checked);
            boardOverviewService.updateCheckboxTask(subTask.getId(), !checked);
        });
    }

    public void editSubTask(SubTask task, HBox subTask, Button rename,
                            TextField text, Button delete, Label name) {
        rename.setOnAction(event -> {
            if (!text.getText().equals("")) {
                name.setText(text.getText());
                task.setName(text.getText());
                boardOverviewService.updateTitleSubTask(task.getId(), text.getText());
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

    private void editSubTaskMessage(Label label) {
        Tooltip tooltip = new Tooltip("Click to edit");
        tooltip.setFont(Font.font("Verdana", 14));
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.ZERO);
        label.setTooltip(tooltip);
    }

    public void addRetrievedSubTasks(Card card) {
        this.card = card;
        subtasks.getChildren().clear();
        for (SubTask subTask : card.getTasks())
            subTaskSetUp(subTask, subTask.getName(), subTask.isChecked());
    }

    public void deleteSubTask(SubTask task, HBox subTask) {
        subtasks.getChildren().remove(subTask);
        boardOverviewService.removeSubTask(task, card.getId());
    }
    public void configureSaveDescriptionButton(Card card, HBox cardContainer) {
        saveDescriptionButton.setOnAction(event -> {
            if(descriptionField.getText().equals("")){
                boardOverviewService.updateCardDescription(card.getId(), " ",
                        boardUserIdentifier.getCurrentBoard());
                card.setDescription("");
            }
            else {
                String description = descriptionField.getText();
                boardOverviewService.updateCardDescription(card.getId(), description,
                        boardUserIdentifier.getCurrentBoard());
                card.setDescription(description);
            }

            HBox iconsAndTask = (HBox) cardContainer.getChildren().get(0);
            VBox cardDetails = (VBox) iconsAndTask.getChildren().get(0);
            ImageView descriptionIcon = (ImageView) cardDetails.getChildren().get(0);
            descriptionIcon.setVisible(card.hasDescription());
        });
        descriptionField.setText(card.getDescription());
    }

    public void rearrange(HBox hbox, SubTask subTask) {
        subtasks.setOnDragOver(event -> {
            if (event.getGestureSource() != subTask && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        hbox.setOnDragDetected(event -> {
            Dragboard db = hbox.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            configureDragboardAndClipboard(subtasks, hbox, subTask, event, db, content);

        });
    }

    private void configureDragboardAndClipboard(VBox vbox, HBox hbox, SubTask subTask,
                                                MouseEvent event, Dragboard db,
                                                ClipboardContent content) {
        content.putString(subTask.getName());
        db.setContent(content);
        db.setDragView(hbox.snapshot(null, null));
        event.consume();

        vbox.getChildren().remove(hbox);
        this.card.getTasks().remove(subTask);
        configureCardListVBoxOnDragDropped(subTask, hbox, vbox);
    }

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

                } else {
                    vbox.getChildren().add(dropIndex, hbox);
                    this.card.getTasks().add(dropIndex, subTask);
                }

            }
            success = true;
            event.setDropCompleted(success);
            event.consume();
        });
    }
    public void saveSubTaskOrder(){
        boardOverviewService.updateCardSubTasks( this.card.getId(),this.card.getTasks(), boardUserIdentifier.getCurrentBoard());
    }


}