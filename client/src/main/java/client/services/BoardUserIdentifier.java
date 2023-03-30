package client.services;

import com.google.inject.Singleton;
import commons.Board;
import commons.User;

@Singleton
public class BoardUserIdentifier {
    private Board currentBoard;
    private User currentUser;

    public void setCurrentBoard(Board board) {
        currentBoard = board;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
