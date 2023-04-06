package client.services;

import com.google.inject.Singleton;
import commons.Board;
import commons.User;

@Singleton
public class BoardUserIdentifier {
    private Board currentBoard;
    private User currentUser;

    private boolean isAdmin;

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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}