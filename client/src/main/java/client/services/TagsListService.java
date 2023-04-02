package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
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
}
