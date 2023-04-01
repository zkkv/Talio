package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;

public class TagsListService {

    private final ServerUtils serverUtils;

    @Inject
    public TagsListService(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public Tag addTagToBoard(Tag tag, long boardId) {
        return serverUtils.addTagToBoard(tag, boardId);
    }
}
