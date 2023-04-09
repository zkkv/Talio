package client.scenes;

import client.services.BoardUserIdentifier;
import client.services.TagsListService;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class TagsInCardCtrl {

    private final TagsListService tagsListService;
    private final MainCtrl mainCtrl;
    private final BoardUserIdentifier boardUserIdentifier;

    private Card currentCard;
    private Set<Tag> tagsInCardSet;

    @FXML
    private VBox tagsInCard;

    @FXML
    private VBox tagsInBoard;

    /**
     * A constructor for the class.
     *
     * @param tagsListService       the service it is going use
     * @param mainCtrl              the controller to which it is bounded
     * @param boardUserIdentifier   the identifier object used to get current board
     * @author                      Vinchentzo Bunardzhiev
     */
    @Inject
    public TagsInCardCtrl(TagsListService tagsListService,
                          MainCtrl mainCtrl,
                          BoardUserIdentifier boardUserIdentifier) {
        this.tagsListService = tagsListService;
        this.mainCtrl = mainCtrl;
        this.boardUserIdentifier = boardUserIdentifier;
        tagsInCardSet = new HashSet<>();
    }

    /**
     * A method to show all the tags needed
     * @param currentCard
     */
    public void openTagsInCard(Card currentCard){
        this.currentCard = currentCard;
        drawTagsFromCard();
        drawTagsFromBoard();
    }

    /**
     * Initial method for drawing all tags of the board
     *
     * @author Vinchentzo Bunardzhiev
     */
    public void drawTagsFromBoard() {
        tagsInBoard.setAlignment(Pos.TOP_CENTER);
        addRetrievedTags(boardUserIdentifier.getCurrentBoard());
    }

    /**
     * Initial method for drawing all tags of the card
     *
     * @author Vinchentzo Bunardzhiev
     */
    public void drawTagsFromCard() {
        tagsInBoard.setAlignment(Pos.TOP_CENTER);
        addRetrievedTagsFromCard();
    }

    /**
     * Draws all tags retrieved from the board except the ones of the card
     *
     * @param currentBoard  board to get tags from
     * @author              Vinchentzo Bunardzhiev
     */
    private void addRetrievedTags(Board currentBoard) {
        tagsInBoard.getChildren().clear();
        var tags = tagsListService.getAllTags(currentBoard.getId());
        List<Tag> currentTagsOfCard = tagsListService.getAllTagsFromCard(currentCard.getId());
        for (Tag tag : tags) {
            if(!currentTagsOfCard.contains(tag)) {
                drawTag(tag);
            }
        }
    }

    /**
     * Draws all tags retrieved from the card
     *
     * @author              Vinchentzo Bunardzhiev
     */
    private void addRetrievedTagsFromCard() {
        tagsInCard.getChildren().clear();
        var tags = tagsListService.getAllTagsFromCard(currentCard.getId());
        for (Tag tag : tags) {
            drawTagForCard(tag);
        }
    }


    /**
     * Draws the given tag based on its values.
     *
     * @param tag   tag to draw
     * @author      Vinchentzo Bunardzhiev
     */
    private void drawTag(Tag tag) {
        Button tagButton = new Button();
        HBox tagBox = new HBox();

        configureTagButton(tagBox, tagButton, tag);
        configureTagBox(tagBox);

        tagBox.getChildren().add(tagButton);
        tagsInBoard.getChildren().add(tagBox);
    }

    /**
     * Draws the given tag based on its values.
     *
     * @param tag   tag to draw
     * @author      Vinchentzo Bunardzhiev
     */
    private void drawTagForCard(Tag tag) {
        Button tagButton = new Button();
        HBox tagBox = new HBox();

        configureTagButtonForCard(tagBox, tagButton, tag);
        configureTagBox(tagBox);

        tagBox.getChildren().add(tagButton);
        tagsInCard.getChildren().add(tagBox);
    }

    /**
     * Configures the tag with retrieved values and applies styles.
     *
     * @param tagButton button to configure
     * @param tag       tag with values
     * @author          Vinchentzo Bunardzhiev
     */
    private void configureTagButton(HBox tagBox, Button tagButton, Tag tag) {
        tagButton.setOnAction(event -> {
            tagsInBoard.getChildren().remove(tagBox);
            configureTagAfterSwitchingToCard(tagBox, tagButton, tag);
            tagsInCard.getChildren().add(tagBox);
            tagsListService.addTagToCard(tag, currentCard.getId(),
                boardUserIdentifier.getCurrentBoard());
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
     * A helper method which switches what the buttons do when a card is moved
     * from the board list to the card list
     * @param tagBox is the same Hbox with the button
     * @param tagButton the button which functionality we change
     * @param tag the tag which we move to the card
     */
    private void configureTagAfterSwitchingToCard(HBox tagBox, Button tagButton, Tag tag) {
        tagButton.setOnAction(event -> {
            tagsInCard.getChildren().remove(tagBox);
            configureTagAfterSwitchingToBoard(tagBox, tagButton, tag);
            tagsInBoard.getChildren().add(tagsInBoard.getChildren().size(), tagBox);
            tagsListService.removeTagFromCard(tag, currentCard.getId(),
                boardUserIdentifier.getCurrentBoard());
        });
    }

    /**
     * A helper method which switches what the buttons do when a card is moved
     * from the card list to the board
     * @param tagBox is the same Hbox with the button
     * @param tagButton the button which functionality we change
     * @param tag the tag which we move out of the card
     */
    private void configureTagAfterSwitchingToBoard(HBox tagBox, Button tagButton, Tag tag) {
        tagButton.setOnAction(event -> {
            tagsInBoard.getChildren().remove(tagBox);
            configureTagAfterSwitchingToCard(tagBox, tagButton, tag);
            tagsInCard.getChildren().add(tagBox);
            tagsListService.addTagToCard(tag, currentCard.getId(),
                boardUserIdentifier.getCurrentBoard());
        });
    }

    /**
     * Configures the tag with retrieved values and applies styles.
     *
     * @param tagButton button to configure
     * @param tag       tag with values
     * @author          Vinchentzo Bunardzhiev
     */
    private void configureTagButtonForCard(HBox tagBox, Button tagButton, Tag tag) {
        tagButton.setOnAction(event -> {
            tagsInCard.getChildren().remove(tagBox);
            configureTagAfterSwitchingToBoard(tagBox, tagButton, tag);
            tagsInBoard.getChildren().add(tagBox);
            tagsListService.removeTagFromCard(tag, currentCard.getId(),
                boardUserIdentifier.getCurrentBoard());
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
     * Configures the container for tag button
     *
     * @param tagBox    HBox to configure
     * @author          Vinchentzo Bunardzhiev
     */
    private void configureTagBox(HBox tagBox) {
        tagBox.setStyle("-fx-start-margin: 20; -fx-end-margin: 20");
        tagBox.setSpacing(3);
        tagBox.setPadding(new Insets(5, 20, 5, 20));
        tagBox.setAlignment(Pos.CENTER);
        tagBox.setFillHeight(true);
    }

    /**
     * A method which closes the window and loads the card tags inside its details
     */
    public void closeTagsInCard(){
        mainCtrl.showCardDetails(currentCard.getTitle(), currentCard);
    }
}