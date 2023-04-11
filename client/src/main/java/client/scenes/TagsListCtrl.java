package client.scenes;

import client.services.BoardUserIdentifier;
import client.services.TagsListService;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.*;
import java.util.function.Consumer;

public class TagsListCtrl {

    private final TagsListService tagsListService;
    private final MainCtrl mainCtrl;
    private final BoardUserIdentifier boardUserIdentifier;
    private final TagDetailsCtrl tagDetailsCtrl;

    private Set<Tag> drawnTags;

    @FXML
    private VBox vbox;

    /**
     * A constructor for the class.
     *
     * @param tagsListService       the service it is going use
     * @param mainCtrl              the controller to which it is bounded
     * @param boardUserIdentifier   the identifier object used to get current board
     * @param tagDetailsCtrl        the controller which is bounded to it
     * @author                      Vinchentzo Bunardzhiev
     */
    @Inject
    public TagsListCtrl(TagsListService tagsListService,
                        MainCtrl mainCtrl,
                        BoardUserIdentifier boardUserIdentifier,
                        TagDetailsCtrl tagDetailsCtrl) {
        this.tagsListService = tagsListService;
        this.mainCtrl = mainCtrl;
        this.boardUserIdentifier = boardUserIdentifier;
        this.tagDetailsCtrl = tagDetailsCtrl;
        drawnTags = new HashSet<>();
    }


    /**
     * Initial method for drawing all tags called from outside.
     *
     * @author Kirill Zhankov
     */
    public void drawTags() {
        vbox.setAlignment(Pos.TOP_CENTER);
        addRetrievedTags(boardUserIdentifier.getCurrentBoard());
    }


    /**
     * Draws all tags retrieved from a server followed by a "+" button.
     *
     * @param currentBoard  board to get tags from
     * @author              Kirill Zhankov
     */
    private void addRetrievedTags(Board currentBoard) {
        vbox.getChildren().clear();
        var tags = tagsListService.getAllTags(currentBoard.getId());
        for (Tag tag : tags) {
            drawTag(tag);
            drawnTags.add(tag);
        }
        drawAddTagButton();
    }


    /**
     * Draws the given tag based on its values.
     *
     * @param tag   tag to draw
     * @author      Kirill Zhankov
     */
    private void drawTag(Tag tag) {
        Button tagButton = new Button();
        HBox tagBox = new HBox();
        Button removeTag = new Button("\u2A2F");

        configureTagButton(tagButton, tag);
        configureRemoveTagButton(tagBox,removeTag,tag);
        configureTagBox(tagBox);

        tagBox.getChildren().add(tagButton);
        tagBox.getChildren().add(removeTag);
        vbox.getChildren().add(tagBox);
    }

    /**
     *
     * @param tagBox    the hbox to which the new button is added
     * @param removeTag the button which is added to the tag
     * @param tag   the tag that is deleted upon pressing the button
     */
    private void configureRemoveTagButton(HBox tagBox, Button removeTag, Tag tag) {
        removeTag.setOnAction(event -> {
            vbox.getChildren().remove(tagBox);
            tagsListService.removeTagFromBoard(tag.getId(),boardUserIdentifier.getCurrentBoard());
        });
    }


    /**
     * Configures the tag with retrieved values and applies styles.
     *
     * @param tagButton button to configure
     * @param tag       tag with values
     * @author          Kirill Zhankov
     */
    private void configureTagButton(Button tagButton, Tag tag) {
        tagButton.setOnAction(event -> {
            mainCtrl.showTagDetails();
            tagDetailsCtrl.changeTagName(tag, tagButton);
        });

        tagButton.setFocusTraversable(false);
        tagButton.setAlignment(Pos.CENTER);
        tagButton.setMnemonicParsing(false);
        tagButton.setPrefHeight(Button.USE_COMPUTED_SIZE);
        tagButton.setPrefWidth(140);
        tagButton.setMinHeight(Button.USE_PREF_SIZE);
        tagButton.setMinWidth(140);
        tagButton.setWrapText(true);

        String color = "rgb(" + tag.getRed() + ", " + tag.getGreen() + ", " + tag.getBlue() + ");";
        String title = tag.getTitle();
        tagButton.setStyle("-fx-border-color: black; -fx-background-color: " + color);
        tagButton.setText(title);
    }


    /**
     * Draws the "+" button.
     *
     * @author Kirill Zhankov
     */
    private void drawAddTagButton(){
        Button addTagButton = new Button("\uFF0B");
        HBox tagBox = new HBox();

        configureTagBox(tagBox);
        configureAddTagButton(addTagButton);
        configureAddTagHint(addTagButton);
        addTagButton.setOnAction(event -> {
            vbox.getChildren().remove(vbox.getChildren().size() - 1);
            createTag();
            drawAddTagButton();
        });
        tagBox.getChildren().add(addTagButton);
        vbox.getChildren().add(tagBox);
    }


    /**
     * Configures the container for tag button and "+" button.
     *
     * @param tagBox    HBox to configure
     * @author          Kirill Zhankov
     */
    private void configureTagBox(HBox tagBox) {
        tagBox.setStyle("-fx-start-margin: 20; -fx-end-margin: 20");
        tagBox.setSpacing(3);
        tagBox.setPadding(new Insets(5, 20, 5, 20));
        tagBox.setAlignment(Pos.CENTER);
        tagBox.setFillHeight(true);
    }


    /**
     * Confugures the "+" button and applies styles.
     *
     * @param addTagButton  button to configure
     * @author              Kirill Zhankov
     */
    private void configureAddTagButton(Button addTagButton){
        addTagButton.setFocusTraversable(false);
        addTagButton.setAlignment(Pos.CENTER);
        addTagButton.setMnemonicParsing(false);
        addTagButton.setPrefHeight(29);
        addTagButton.setPrefWidth(90);
        addTagButton.setMinHeight(29);
        addTagButton.setMinWidth(90);
        addTagButton.setStyle("-fx-border-color: black;");
    }


    /**
     * Creates a tag, adds it to the database and then draws it.
     *
     * @author Kirill Zhankov
     */
    private void createTag() {
        Random rng = new Random();
        int r = rng.nextInt(100, 256);
        int g = rng.nextInt(100, 256);
        int b = rng.nextInt(100, 256);

        Tag newTag = tagsListService.addTagToBoard(
                new Tag("Tag", r, g, b, new ArrayList<>()),
                boardUserIdentifier.getCurrentBoard().getId());
        drawTag(newTag);
        drawnTags.add(newTag);
    }


    /**
     * Configures the hint which appears when mouse hovers over the "+" button.
     *
     * @param addTagButton  button to configure the hint for
     * @author              Kirill Zhankov
     */
    private void configureAddTagHint(Button addTagButton){
        Tooltip tooltip = new Tooltip("Add a new tag");
        tooltip.setFont(Font.font("Verdana", 14));
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.ZERO);
        addTagButton.setTooltip(tooltip);
    }


    /**
     * Registers for tag updates on the board {@code currentBoard} and draws the tag
     * once there are any.
     *
     * @param currentBoard  board to register updates on
     * @see                 client.utils.ServerUtils#registerForTagUpdates(long, Consumer)
     * @author              Kirill Zhankov
     */
    public void registerForTagUpdates(Board currentBoard) {
        // The lambda expression draws the tag once there is a response from the server.
        // See ServerUtils.registerForTagUpdates() to better understand.
        tagsListService.registerForTagUpdates(
                currentBoard.getId(),
                tag -> {
                    if(mainCtrl.isTagsListShowing()) {
                        addRetrievedTags(boardUserIdentifier.getCurrentBoard());
                    }
                });
    }


    /**
     * Stops the polling.
     *
     * @author Kirill Zhankov
     */
    public void stopPolling() {
        tagsListService.stopPolling();
    }
}