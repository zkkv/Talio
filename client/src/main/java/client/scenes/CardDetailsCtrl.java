package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CardDetailsCtrl {

    private final BoardOverviewService boardOverviewService;

    private final BoardUserIdentifier boardUserIdentifier;

    private final MainCtrl mainCtrl;

    @FXML
    private Label title;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Button saveDescriptionButton;

    @Inject
    public CardDetailsCtrl(BoardOverviewService boardOverviewService,
                           BoardUserIdentifier boardUserIdentifier,
                           MainCtrl mainCtrl) {
        this.boardOverviewService = boardOverviewService;
        this.boardUserIdentifier = boardUserIdentifier;
        this.mainCtrl = mainCtrl;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void configureSaveDescriptionButton(Card card, HBox cardContainer) {
        saveDescriptionButton.setOnAction(event -> {
            System.out.println(descriptionField.getText());
            if(descriptionField.getText().equals("")){
                boardOverviewService.updateCardDescription(card.getId(), " ",
                        boardUserIdentifier.getCurrentBoard());
                card.setDescription("");
            }
            else {
                String description = descriptionField.getText();
                boardOverviewService.updateCardDescription(card.getId(), description,
                        boardUserIdentifier.getCurrentBoard());
                card.setDescription(description);
            }

            HBox iconsAndTask = (HBox) cardContainer.getChildren().get(0);
            VBox cardDetails = (VBox) iconsAndTask.getChildren().get(0);
            ImageView descriptionIcon = (ImageView) cardDetails.getChildren().get(0);
            descriptionIcon.setVisible(card.hasDescription());
        });
        descriptionField.setText(card.getDescription());
    }


}
