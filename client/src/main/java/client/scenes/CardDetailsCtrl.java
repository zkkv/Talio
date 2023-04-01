package client.scenes;

import client.services.BoardOverviewService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CardDetailsCtrl {

    private final BoardOverviewService boardOverviewService;

    private final MainCtrl mainCtrl;

    @FXML
    private Label title;

    @Inject
    public CardDetailsCtrl(BoardOverviewService boardOverviewService, MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.mainCtrl = mainCtrl;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }


}
