package client.scenes;

import client.services.BoardUserIdentifier;
import client.services.TagsListService;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class TagDetailsCtrl {

    private final TagsListService tagsListService;
    private final MainCtrl mainCtrl;
    private final BoardUserIdentifier boardUserIdentifier;

    @FXML
    private Button nameChangeButton;
    @FXML
    private TextField nameTextField;

    /**
     * A constructor for the class.
     *
     * @param tagsListService       the service it is going use
     * @param mainCtrl              the controller to which it is bounded
     * @param boardUserIdentifier   the identifier object used to get current board
     * @author                      Vinchentzo Bunardzhiev
     */
    @Inject
    public TagDetailsCtrl(TagsListService tagsListService,
                        MainCtrl mainCtrl,
                        BoardUserIdentifier boardUserIdentifier) {
        this.tagsListService = tagsListService;
        this.mainCtrl = mainCtrl;
        this.boardUserIdentifier = boardUserIdentifier;
    }

    public void changeTagName(Tag tag, Button tagButton){
        nameTextField.setText(tag.getTitle());
        nameChangeButton.setOnAction(event -> {
            tagsListService.updateTagName(tag.getId(), nameTextField.getText(),
                boardUserIdentifier.getCurrentBoard());
            tag.setTitle(nameTextField.getText());
            tagButton.setText(nameTextField.getText());
        });
    }

    public void setName(String name) {
        this.nameTextField.setText(name);
    }

}
