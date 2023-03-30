package client.scenes;


import client.services.BoardOverviewService;
import client.services.BoardUserIdentifier;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StartPageCtrl implements Initializable {

    private final BoardOverviewService boardOverviewService;
    private final MainCtrl mainCtrl;

    private BoardUserIdentifier boardUserIdentifier;

    @FXML
    private VBox boardList;

    @FXML
    private ScrollPane scrollPane;

    @Inject
    public StartPageCtrl(BoardOverviewService boardOverviewService, MainCtrl mainCtrl,
                         BoardUserIdentifier boardUserIdentifier) {
        this.boardOverviewService = boardOverviewService;
        this.mainCtrl = mainCtrl;
        this.boardUserIdentifier = boardUserIdentifier;
    }

    public void initBoardList(){
        boardList.getChildren().clear();
        User user = boardUserIdentifier.getCurrentUser();
        List<Board> boards = boardOverviewService.getUserBoards(user.getUserName());
        for(Board board:boards){
            GridPane boardTab = new GridPane();
            configureBoardTab(boardTab,board);
            boardList.getChildren().add(boardTab);
        }
    }

    public void configureBoardTab(GridPane gridPane,Board board){
        Label boardTitle = new Label();
        Button removeBoard = new Button();
        Button leaveBoard = new Button();
        removeBoard.setText("X");
        leaveBoard.setText("Bye");
        boardTitle.setText(board.getTitle());
        boardTitle.setFont(new Font(20.0));

        removeBoard.setOnMouseClicked(event -> {
            boardOverviewService.removeBoard(board);
            boardList.getChildren().remove(gridPane);
        });


        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        col1.setPercentWidth(60);
        col2.setPercentWidth(20);
        col3.setPercentWidth(20);
        gridPane.getColumnConstraints().addAll(col1, col2, col3);
        gridPane.add(boardTitle,0,0);
        gridPane.add(leaveBoard,1,0);
        gridPane.add(removeBoard,2,0);
        gridPane.setPadding(new Insets(10.0));

        gridPane.setOnMouseClicked(e->{
            boardUserIdentifier.setCurrentBoard(board);
            mainCtrl.showBoardPage();
        });
    }

    public void createNewBoard(){
        mainCtrl.showCreateBoardPage();
    }

    //TODO check if you have already joined
    public void joinNewBoard(){
        mainCtrl.showJoinBoard();
    }

    public void loginAsAdmin(){
        String pass = boardOverviewService.getAdminPassword();
        mainCtrl.showAdminLogin(pass);
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