package server.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import commons.Board;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import server.services.BoardService;
import server.services.CardListService;

class BoardControllerTest {
    @Mock
    private BoardService boardService;

    @Mock
    private CardListService cardListService;

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
        board1.setKey(1L);
        expectedBoards.add(board1);

        Board board2 = new Board();
        board2.setKey(2L);
        expectedBoards.add(board2);

        when(boardService.getAllBoards()).thenReturn(expectedBoards);

        List<Board> actualBoards = boardController.getAll().getBody();

        assertEquals(expectedBoards, actualBoards);
    }

    @Test
    public void testGetByIdWithValidId() {
        Board expectedBoard = new Board();
        expectedBoard.setKey(1L);

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
    public void testGetBoard() {
        List<Board> expectedBoards = new ArrayList<>();
        Board board = new Board();
        board.setKey(1L);
        expectedBoards.add(board);

        when(boardService.getAllBoards()).thenReturn(expectedBoards);

        ResponseEntity<Board> actualResponse = boardController.getOrCreateBoard();

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(board, actualResponse.getBody());
    }

    @Test
    public void testCreateBoard() {
        when(boardService.getAllBoards()).thenReturn(new ArrayList<>());

        Board expectedBoard = new Board(new ArrayList<>());
        expectedBoard.getCardLists().add(new CardList(new ArrayList<>(), "TO DO"));
        expectedBoard.getCardLists().add(new CardList(new ArrayList<>(), "DOING"));
        expectedBoard.getCardLists().add(new CardList(new ArrayList<>(), "DONE"));

        Board savedBoard = new Board();
        savedBoard.setKey(1L);

        when(boardService.save(any(Board.class))).thenReturn(savedBoard);

        ResponseEntity<Board> actualResponse = boardController.getOrCreateBoard();

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(savedBoard, actualResponse.getBody());
    }

    @Test
    public void testAddCardList() {
        CardList cardListToAdd = new CardList();
        cardListToAdd.setId(1L);

        List<CardList> cardLists = new ArrayList<>();
        Board board = new Board(cardLists);

        when(boardService.getAllBoards()).thenReturn(List.of(board));
        when(boardService.save(any(Board.class))).thenReturn(board);

        when(cardListService.save(any(CardList.class))).thenReturn(cardListToAdd);

        ResponseEntity<CardList> actualResponse = boardController.addCardList(cardListToAdd);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(cardListToAdd, actualResponse.getBody());
    }

    @Test
    public void testRemoveCardListValid() {
        CardList cardListToRemove = new CardList();
        cardListToRemove.setId(1L);

        List<CardList> cardLists = new ArrayList<>();
        cardLists.add(cardListToRemove);
        Board board = new Board(cardLists);

        when(boardService.getAllBoards()).thenReturn(List.of(board));
        when(boardService.save(any(Board.class))).thenReturn(board);

        when(cardListService.getCardList(1L)).thenReturn(cardListToRemove);
        when(cardListService.exists(1L)).thenReturn(true);
        doNothing().when(cardListService).delete(1L);

        ResponseEntity<CardList> actualResponse = boardController.removeCardList(1L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(cardListToRemove, actualResponse.getBody());
    }

    @Test
    public void testRemoveCardListInvalid() {
        when(cardListService.exists(1L)).thenReturn(false);

        ResponseEntity<CardList> actualResponse = boardController.removeCardList(1L);

        assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    }
}