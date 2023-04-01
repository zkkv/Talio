package client.scenes;

import client.services.BoardOverviewService;
import com.google.inject.Inject;
import commons.Card;
import commons.SubTask;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class CardDetailsCtrl {

    private final BoardOverviewService boardOverviewService;

    private final MainCtrl mainCtrl;

    @FXML
    private Label title;

    @FXML
    private VBox subtasks;

    private Card card;


    @Inject
    public CardDetailsCtrl(BoardOverviewService boardOverviewService,
                           MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
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
}