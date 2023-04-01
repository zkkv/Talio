package client.scenes;

import client.services.BoardUserIdentifier;
import client.services.TagsListService;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

// TODO Retrieve old tags
public class TagsListCtrl {

    private final TagsListService tagsListService;
    private final MainCtrl mainCtrl;
    private final BoardUserIdentifier boardUserIdentifier;

    @FXML
    private VBox vbox;

    /**
     * A constructor for the class
     *
     * @param tagsListService       the service it is going use
     * @param mainCtrl              the controller to which it is bounded
     * @param boardUserIdentifier   the identifier object used to get current board
     * @author                      Vinchentzo Bunardzhiev
     */
    @Inject
    public TagsListCtrl(TagsListService tagsListService,
                        MainCtrl mainCtrl,
                        BoardUserIdentifier boardUserIdentifier) {
        this.tagsListService = tagsListService;
        this.mainCtrl = mainCtrl;
        this.boardUserIdentifier = boardUserIdentifier;
    }

    public void drawTags() {
        vbox.setAlignment(Pos.TOP_CENTER);
        addRetrievedTags(boardUserIdentifier.getCurrentBoard());
    }

    private void addRetrievedTags(Board currentBoard) {
        vbox.getChildren().clear();
        var tags = currentBoard.getTags();
        for (Tag tag : tags) {
            drawTag(tag);
        }
        drawAddTagButton();
    }

    private void drawTag(Tag tag) {
        Button tagButton = new Button();
        HBox tagBox = new HBox();

        configureTagButton(tagButton, tag);
        configureTagBox(tagBox);

        tagBox.getChildren().add(tagButton);
        if (!vbox.getChildren().isEmpty()) {
            vbox.getChildren().remove(vbox.getChildren().size() - 1);
        }
        vbox.getChildren().add(tagBox);
    }

    private void configureTagButton(Button tagButton, Tag tag) {
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
    }

    private void drawAddTagButton(){
        Button addTagButton = new Button("+");
        HBox tagBox = new HBox();
        configureTagBox(tagBox);
        configureAddTagButton(addTagButton);
        configureAddTagHint(addTagButton);
        addTagButton.setOnAction(event -> {
            createTag();
            drawAddTagButton();
        });
        tagBox.getChildren().add(addTagButton);
        vbox.getChildren().add(tagBox);
    }

    private void configureTagBox(HBox tagBox) {
        tagBox.setStyle("-fx-start-margin: 20; -fx-end-margin: 20");
        tagBox.setSpacing(3);
        tagBox.setAlignment(Pos.CENTER);
        tagBox.setFillHeight(true);
    }

    private void configureAddTagButton(Button addTagButton){
        addTagButton.setFocusTraversable(false);
        addTagButton.setAlignment(Pos.CENTER);
        addTagButton.setMnemonicParsing(false);
        addTagButton.setPrefHeight(34);
        addTagButton.setPrefWidth(140);
        addTagButton.setMinHeight(34);
        addTagButton.setMinWidth(140);
        addTagButton.setStyle("-fx-border-color: black;");
    }

    private void createTag() {
        Random rng = new Random();
        int r = rng.nextInt(100, 256);
        int g = rng.nextInt(100, 256);
        int b = rng.nextInt(100, 256);

        Tag newTag = tagsListService.addTagToBoard(
                new Tag("Tag", r, g, b, new ArrayList<>()),
                boardUserIdentifier.getCurrentBoard().getId());
        System.out.println(newTag.toString());
        drawTag(newTag);
    }

    private void configureAddTagHint(Button addTagButton){
        Tooltip tooltip = new Tooltip("Add a new tag");
        tooltip.setFont(Font.font("Verdana", 14));
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.ZERO);
        addTagButton.setTooltip(tooltip);
    }

}