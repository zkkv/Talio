package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;

import java.util.List;
import java.util.function.Consumer;


public class TagsListService {

    private final ServerUtils serverUtils;

    @Inject
    public TagsListService(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }


    /**
     * Adds the provided {@code tag} to the tag list of the
     * board with provided {@code boardId} using ServerUtils and returns the tag.
     *
     * @param tag       Tag to be added
     * @param boardId   id of the board to tag list of which tag should be added
     * @return          Added tag
     * @author          Kirill Zhankov
     */
    public Tag addTagToBoard(Tag tag, long boardId) {
        return serverUtils.addTagToBoard(tag, boardId);
    }


    /**
     * Returns a list of all tags of a board with {@code boardId} using ServerUtils.
     *
     * @param boardId   id of the board to get the tags from
     * @return          list of tags of the board
     * @author          Kirill Zhankov
     */
    public List<Tag> getAllTags(long boardId) {
        return serverUtils.getAllTags(boardId);
    }

    /**
     * A new method to get the tags of the cards from
     * @param cardId the card from which to get all the tags
     * @return the list of the tags of the card
     */
    public List<Tag> getAllTagsFromCard(long cardId) {
        return serverUtils.getAllTagsFromCard(cardId);
    }


    /**
     * Registers for tag updates of a board with {@code boardId} and executes the {@code consumer}
     * once there are any.
     *
     * @param boardId   id of the board for which updates should be tracked
     * @param consumer  consumer function which is executed once there is an update
     * @author          Kirill Zhankov
     */
    public void registerForTagUpdates(long boardId, Consumer<Tag> consumer) {
        serverUtils.registerForTagUpdates(boardId, consumer);
    }


    /**
     * Stops the polling.
     *
     * @author Kirill Zhankov
     */
    public void stopPolling() {
        serverUtils.stopPolling();
    }

    /**
     * A method to change the name of the tag
     *
     * @param tagId the id of the tag which name is updated
     * @param title the new name
     * @param board the baord in which the tag is switched
     * @return the tag with the updated name
     */
    public Tag updateTagName(long tagId, String title, Board board) {
        return serverUtils.updateTagName(tagId, title,board);
    }

    /**
     * A method to remove a tag
     *
     * @param tagId the id of the tag which is removed
     * @param board the board from which the tag is removed
     */
    public void removeTagFromBoard(long tagId,Board board){
        serverUtils.removeTag(tagId,board);
    }

    /**
     * A method do add the tag to the card
     * @param tag the tag to add
     * @param cardId the id of the card to which we add
     * @return the tag which we add
     */
    public Tag addTagToCard(Tag tag, long cardId){
        return serverUtils.addTagToCard(tag,cardId);
    }

    /**
     * A method to remove the tag from the card
     * @param tag the tag to remove
     * @param cardId the id of the card from which we remove
     */
    public void removeTagFromCard(Tag tag, long cardId){
        serverUtils.removeTagFromCard(tag.getId(),cardId);
    }
}
