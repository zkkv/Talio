package client.scenes;

import client.services.BoardIdentifier;
import client.services.BoardOverviewService;
import com.google.inject.Inject;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddTaskCtrl {

    private final BoardOverviewService boardOverviewService;

    private final BoardIdentifier boardIdentifier;

    private final MainCtrl mainCtrl;

    private Label label;

    @FXML
    private TextField title;

    @FXML
    private Button editButton;

    @Inject
    public AddTaskCtrl(BoardOverviewService boardOverviewService, BoardIdentifier boardIdentifier,
                       MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.boardIdentifier = boardIdentifier;
        this.mainCtrl = mainCtrl;
    }

    public void configureEditButton(Card card) {
        editButton.setOnAction(event -> {
            mainCtrl.changeName(label, title.getText());
            mainCtrl.showBoardPage();
            boardOverviewService.updateCardTitle(card.getId(), title.getText(),
                boardIdentifier.getCurrentBoard());
            card.setTitle(title.getText());
        });

        title.setText(card.getTitle());
    }

    public void cancel() {
        title.clear();
        mainCtrl.showBoardPage();
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}
