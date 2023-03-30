package server.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import commons.Board;
import commons.CardList;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.services.BoardService;
import server.services.CardListService;
import server.services.UserService;

class BoardControllerTest {
    @Mock
    private BoardService boardService;

    @Mock
    private CardListService cardListService;
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private UserService userService;

    @InjectMocks
    private BoardController boardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        List<Board> expectedBoards = new ArrayList<>();
        Board board1 = new Board();
        board1.setId(1L);
        expectedBoards.add(board1);

        Board board2 = new Board();
        board2.setId(2L);
        expectedBoards.add(board2);

        when(boardService.getAllBoards()).thenReturn(expectedBoards);

        List<Board> actualBoards = boardController.getAll().getBody();

        assertEquals(expectedBoards, actualBoards);
    }

    @Test
    public void testGetByIdWithValidId() {
        Board expectedBoard = new Board();
        expectedBoard.setId(1L);

        when(boardService.exists(1L)).thenReturn(true);
        when(boardService.getBoard(1L)).thenReturn(expectedBoard);

        ResponseEntity<Board> actualResponse = boardController.getById(1L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedBoard, actualResponse.getBody());
    }

    @Test
    public void testGetByIdWithInvalidId() {
        when(boardService.exists(-1L)).thenReturn(false);

        ResponseEntity<Board> actualResponse = boardController.getById(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
    }

    @Test
    public void testGetBoardValid() {
        Board expectedBoard = new Board();

        expectedBoard.setId(1L);
        when(boardService.exists(1L)).thenReturn(true);
        when(boardService.getBoard(1L)).thenReturn(expectedBoard);

        ResponseEntity<Board> actualResponse = boardController.getById(1L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedBoard, actualResponse.getBody());
    }

    @Test
    public void testGetBoardInvalid() {
        Board expectedBoard = new Board();

        expectedBoard.setId(-1L);
        when(boardService.exists(-1L)).thenReturn(false);
        ResponseEntity<Board> actualResponse = boardController.getById(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
    }

    @Test
    public void testGetAllCardLists() {
        Board expectedBoard = new Board();
        List<CardList> expectedCardLists = new ArrayList<>();
        expectedBoard.setCardLists(expectedCardLists);
        expectedBoard.setId(1L);
        when(boardService.getBoard(1L)).thenReturn(expectedBoard);

        ResponseEntity<List<CardList>> actualResponse = boardController.getAllCardLists(1L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedCardLists, actualResponse.getBody());
    }

    //TODO join board
    @Test
    public void testCreateBoard() {
        when(boardService.getAllBoards()).thenReturn(new ArrayList<>());

        Board expectedBoard = new Board(new ArrayList<>(),"b1");
        expectedBoard.getCardLists().add(new CardList(new ArrayList<>(), "TO DO"));
        expectedBoard.getCardLists().add(new CardList(new ArrayList<>(), "DOING"));
        expectedBoard.getCardLists().add(new CardList(new ArrayList<>(), "DONE"));

        Board savedBoard = new Board();
        savedBoard.setId(1L);
        savedBoard.setTitle("Title");
        User user = new User("user",new ArrayList<>());

        when(boardService.save(any(Board.class))).thenReturn(savedBoard);

        ResponseEntity<Board> actualResponse = boardController.createBoard("Title", user.getUserName());

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(savedBoard, actualResponse.getBody());
        assertEquals(user.getJoinedBoards().get(0), savedBoard);
    }

    @Test
    public void testAddCardList() {
        CardList cardListToAdd = new CardList();
        cardListToAdd.setId(1L);

        List<CardList> cardLists = new ArrayList<>();
        Board board = new Board(cardLists,"b1");
        board.setId(1L);

        when(boardService.getBoard(1L)).thenReturn(board);
        when(boardService.save(any(Board.class))).thenReturn(board);

        when(cardListService.save(any(CardList.class))).thenReturn(cardListToAdd);

        ResponseEntity<CardList> actualResponse = boardController.addCardList(cardListToAdd,1L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(cardListToAdd, actualResponse.getBody());
    }

    @Test
    public void testRemoveCardListValid() {
        CardList cardListToRemove = new CardList();
        cardListToRemove.setId(1L);

        List<CardList> cardLists = new ArrayList<>();
        cardLists.add(cardListToRemove);
        Board board = new Board(cardLists,"b1");
        board.setId(2L);

        when(boardService.getBoard(2L)).thenReturn(board);
        when(boardService.save(any(Board.class))).thenReturn(board);

        when(cardListService.getCardList(1L)).thenReturn(cardListToRemove);
        when(cardListService.exists(1L)).thenReturn(true);
        doNothing().when(cardListService).delete(1L);

        ResponseEntity<CardList> actualResponse = boardController.removeCardList(1L,2L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(cardListToRemove, actualResponse.getBody());
    }

    @Test
    public void testRemoveCardListInvalid() {
        when(cardListService.exists(1L)).thenReturn(false);

        ResponseEntity<CardList> actualResponse = boardController.removeCardList(1L,2L);

        assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    }
}