package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.CardList;
import commons.Pair;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ListMenuCtrl {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;
    private CardList cardList;
    private BorderPane bp;

    @FXML
    private Button listMenuButton;
    @FXML
    private Button removeList;
    @FXML
    private TextField listMenuTextField;


    @Inject
    public ListMenuCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void changeListLabel(CardList cardList, TextField listLabel){
        listMenuButton.setOnAction(event -> {
            listLabel.setText(listMenuTextField.getText());
            Pair<CardList,String> request = new Pair<>(cardList,listLabel.getText());
            server.updateCardListTitle(request);
        });
    }

    public void setCardListBorderPane(CardList cardList, BorderPane bp) {
        this.cardList = cardList;
        this.bp = bp;
    }

    public void deletingList(HBox panel) {
        removeList.setOnAction(event -> {
            panel.getChildren().remove(bp);
            server.removeCardListToBoard(this.cardList);
            mainCtrl.closeMenu();
        });
    }


}
