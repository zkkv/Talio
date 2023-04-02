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

import java.util.*;

public class TagsListCtrl {

    private final TagsListService tagsListService;
    private final MainCtrl mainCtrl;
    private final BoardUserIdentifier boardUserIdentifier;

    private Set<Tag> drawnTags;

    @FXML
    private VBox vbox;

    /**
     * A constructor for the class.
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
        System.out.println("addRetrievedTags");
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
        System.out.println("drawTag " + tag.toString());
        Button tagButton = new Button();
        HBox tagBox = new HBox();

        configureTagButton(tagButton, tag);
        configureTagBox(tagBox);

        tagBox.getChildren().add(tagButton);
        vbox.getChildren().add(tagBox);
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
            // TODO Open scene with editing
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
    }


    /**
     * Draws the "+" button.
     *
     * @author Kirill Zhankov
     */
    private void drawAddTagButton(){
        Button addTagButton = new Button("+");
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
        addTagButton.setPrefHeight(34);
        addTagButton.setPrefWidth(140);
        addTagButton.setMinHeight(34);
        addTagButton.setMinWidth(140);
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

    public void registerForTagUpdates(Board currentBoard) {
        // The lambda expression draws the tag once there is a response from the server.
        // See ServerUtils.registerForTagUpdates() to better understand.
        System.out.println("Registering for updates on board " + currentBoard.toString());
        tagsListService.registerForTagUpdates(
                currentBoard.getId(),
                tag -> {
                    System.out.println("Consumer start");
                    System.out.println(tag.toString());
                    if (!drawnTags.contains(tag)) {
                        vbox.getChildren().remove(vbox.getChildren().size() - 1);
                        drawTag(tag);
                        drawAddTagButton();
                    }
                    System.out.println("Consumer end");
                });
    }

    public void stopPolling() {
        tagsListService.stopPolling();
    }
}