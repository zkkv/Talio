package client.scenes;

import client.services.BoardOverviewService;
import com.google.inject.Inject;

public class TagsListCtrl {

    private final BoardOverviewService boardOverviewService;

    private final MainCtrl mainCtrl;


    @Inject
    public TagsListCtrl(BoardOverviewService boardOverviewService, MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.mainCtrl = mainCtrl;
    }
}