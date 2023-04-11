package server.api;

import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.BoardService;
import server.services.UserService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BoardService boardService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUserValid(){
        User user = new User();
        user.setUserName("S");

        when(userService.createUser("S")).thenReturn(user);
        when(userService.exists("S")).thenReturn(false);
        ResponseEntity<User> actualResponse = userController.createUser("S");

        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(user,actualResponse.getBody());
    }

    @Test
    public void testCreateUserInvalid(){
        User user = new User();
        user.setUserName("S");

        when(userService.createUser("S")).thenReturn(user);
        when(userService.exists("S")).thenReturn(true);
        ResponseEntity<User> actualResponse = userController.createUser("S");

        assertEquals(HttpStatus.BAD_REQUEST,actualResponse.getStatusCode());
    }

    @Test
    public void testAddBoardToUser(){
        User user = new User("S",new ArrayList<>());

        Board board = new Board();
        board.setKey("asdfgh");
        board.setId(1L);

        when(userService.getUser("S")).thenReturn(user);
        when(boardService.getBoardByKey("asdfgh")).thenReturn(board);
        when(userService.save(user)).thenReturn(user);

        ResponseEntity<User> actualResponse = userController.addBoardToUser("S","asdfgh");

        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(board,actualResponse.getBody().getJoinedBoards().get(0));
    }

    @Test
    public void testGetUserValid(){
        User user = new User();
        user.setUserName("S");

        when(userService.getUser("S")).thenReturn(user);
        when(userService.exists("S")).thenReturn(true);
        ResponseEntity<User> actualResponse = userController.getUser("S");

        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(user,actualResponse.getBody());
    }

    @Test
    public void testGetUserInvalid(){
        User user = new User();
        user.setUserName("S");

        when(userService.getUser("S")).thenReturn(user);
        when(userService.exists("S")).thenReturn(false);
        ResponseEntity<User> actualResponse = userController.getUser("S");

        assertEquals(HttpStatus.NOT_FOUND,actualResponse.getStatusCode());
    }

    @Test
    public void removeBoardForUser(){
        Board board = new Board();
        User user = new User("S",new ArrayList<>());
        user.getJoinedBoards().add(board);

        when(userService.getUser("S")).thenReturn(user);
        when(userService.save(user)).thenReturn(user);
        ResponseEntity<User> actualResponse = userController.removeBoardForUser("S",board);

        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(0,actualResponse.getBody().getJoinedBoards().size());
    }
}
