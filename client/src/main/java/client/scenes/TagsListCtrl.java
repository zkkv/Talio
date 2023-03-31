package client.scenes;

import client.services.BoardOverviewService;
import com.google.inject.Inject;

public class TagsListCtrl {

    private final BoardOverviewService boardOverviewService;

    private final MainCtrl mainCtrl;

    /**
     * A constructor for the class
     * @param boardOverviewService  the service it is going use
     * @param mainCtrl the controller to which it is bounded
     */
    @Inject
    public TagsListCtrl(BoardOverviewService boardOverviewService, MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.mainCtrl = mainCtrl;
    }
}