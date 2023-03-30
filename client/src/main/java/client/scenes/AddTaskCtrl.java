package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddTaskCtrl {

    private final BoardOverviewService boardOverviewService;

    private final BoardUserIdentifier boardUserIdentifier;

    private final MainCtrl mainCtrl;

    private Label label;

    @FXML
    private TextField title;

    @FXML
    private Button editButton;

    @Inject
    public AddTaskCtrl(BoardOverviewService boardOverviewService,
                       BoardUserIdentifier boardUserIdentifier, MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.boardUserIdentifier = boardUserIdentifier;
        this.mainCtrl = mainCtrl;
    }

    public void configureEditButton(Card card) {
        editButton.setOnAction(event -> {
            mainCtrl.changeName(label, title.getText());
            mainCtrl.showBoardPage();
            boardOverviewService.updateCardTitle(card.getId(), title.getText(),
                boardUserIdentifier.getCurrentBoard());
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
