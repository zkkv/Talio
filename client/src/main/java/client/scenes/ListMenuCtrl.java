package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ListMenuCtrl {

    private final BoardOverviewService boardOverviewService;

    private final BoardUserIdentifier boardUserIdentifier;

    private final MainCtrl mainCtrl;
    private CardList cardList;
    private BorderPane bp;

    @FXML
    private Button listMenuButton;
    @FXML
    private TextField listMenuTextField;


    @Inject
    public ListMenuCtrl(BoardOverviewService boardOverviewService,
                        BoardUserIdentifier boardUserIdentifier,
                        MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.boardUserIdentifier = boardUserIdentifier;
        this.mainCtrl = mainCtrl;
    }

    public void changeListLabel(CardList cardList, TextField listLabel){
        listMenuButton.setOnAction(event -> {
            listLabel.setText(listMenuTextField.getText());
            boardOverviewService.updateCardListTitle(cardList.getId(),
                listLabel.getText(),boardUserIdentifier.getCurrentBoard());
        });
    }

    public void setCardListBorderPane(CardList cardList, BorderPane bp) {
        this.cardList = cardList;
        this.bp = bp;
    }



}
