package client.scenes;

import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminOverviewCtrl implements Initializable {
    private final BoardOverviewService boardOverviewService;
    private final MainCtrl mainCtrl;

    private BoardUserIdentifier boardIdentifier;

    @FXML
    private VBox boardList;

    @FXML
    private ScrollPane scrollPane;

    @Inject
    public AdminOverviewCtrl(BoardOverviewService boardOverviewService,
                             MainCtrl mainCtrl, BoardUserIdentifier boardIdentifier) {
        this.boardOverviewService = boardOverviewService;
        this.mainCtrl = mainCtrl;
        this.boardIdentifier = boardIdentifier;
    }

    public void initBoardList(){
        boardList.getChildren().clear();
        List<Board> boards = boardOverviewService.getAllBoards();
        for(Board board:boards){
            GridPane boardTab = new GridPane();
            configureBoardTab(boardTab,board);
            boardList.getChildren().add(boardTab);
        }
    }

    public void configureBoardTab(GridPane gridPane,Board board){
        Label boardTitle = new Label();
        Button removeBoard = new Button();
        removeBoard.setText("X");
        boardTitle.setText(board.getTitle());
        boardTitle.setFont(new Font(20.0));

        removeBoard.setOnMouseClicked(event -> {
            boardOverviewService.removeBoard(board);
            boardList.getChildren().remove(gridPane);
        });

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(80);
        col2.setPercentWidth(20);
        gridPane.getColumnConstraints().addAll(col1, col2);
        gridPane.add(boardTitle,0,0);
        gridPane.add(removeBoard,1,0);
        gridPane.setPadding(new Insets(10.0));

        gridPane.setOnMouseClicked(e->{
            boardIdentifier.setCurrentBoard(board);
            mainCtrl.showBoardPage();
        });
    }

    public void goBack(){
        mainCtrl.showStartPage();
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPannable(true);
    }
}