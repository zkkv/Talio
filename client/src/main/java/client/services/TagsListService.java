package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;

import java.util.List;

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
}
