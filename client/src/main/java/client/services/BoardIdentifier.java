package client.services;

import com.google.inject.Singleton;
import commons.Board;

@Singleton
public class BoardIdentifier {
    private Board currentBoard;

    public void setCurrentBoard(Board board) {
        currentBoard = board;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }
}
