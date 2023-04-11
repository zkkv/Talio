package server.services;

import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserBoards() {
        List<Board> userBoards = new ArrayList<>();
        Board board1 = new Board();
        Board board2 = new Board();
        userBoards.add(board1);
        userBoards.add(board2);

        User user = new User("S", userBoards);

        when(userRepository.findUserByUserName("S")).thenReturn(user);

        List<Board> actualBoards = userService.getUserBoards("S");

        assertEquals(userBoards, actualBoards);
    }

    @Test
    public void testSave() {
        User user = new User();
        user.setUserName("S");

        when(userRepository.save(user)).thenReturn(user);

        User actualUser = userService.save(user);

        assertEquals(user, actualUser);
    }

    @Test
    public void testCreateUser() {
        User user = new User("S", new ArrayList<>());

        when(userRepository.save(user)).thenReturn(user);

        User actualUser = userService.createUser("S");

        assertEquals(user, actualUser);
    }

    @Test
    public void testGetUser() {
        User user = new User();
        user.setUserName("S");

        when(userRepository.findUserByUserName("S")).thenReturn(user);

        User actualUser = userService.getUser("S");

        assertEquals(user, actualUser);
    }

    @Test
    public void testExists() {
        User user = new User();
        user.setUserName("S");

        when(userRepository.existsByUserName("S")).thenReturn(true);

        boolean actual = userService.exists("S");

        assertEquals(true, actual);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        User user2 = new User();
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(users,actualUsers);
    }
}
