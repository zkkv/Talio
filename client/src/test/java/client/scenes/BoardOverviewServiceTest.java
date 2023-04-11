package client.scenes;

import client.services.BoardOverviewService;
import client.utils.ServerUtils;
import commons.*;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BoardOverviewServiceTest {
    @Mock
    private ServerUtils serverUtils;

    @InjectMocks
    private BoardOverviewService boardOverviewService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCardList() {
        Board board = new Board(new ArrayList<>(), "asd");
        CardList cardList = new CardList();

        when(serverUtils.addCardListToBoard(cardList, board)).thenReturn(cardList);
        CardList actual = boardOverviewService.addCardList(cardList, board);

        assertEquals(cardList, actual);
    }

    @Test
    public void testAddCard() {
        Board board = new Board();
        CardList cardList = new CardList();
        cardList.setId(1L);
        Card card = new Card();

        when(serverUtils.addCardToCardList(card, 1L, board)).thenReturn(card);
        Card actual = boardOverviewService.addCard(card, 1L, board);

        assertEquals(card, actual);
    }

    @Test
    public void testGetCardLists() {
        Board board = new Board();
        List<CardList> cardLists = new ArrayList<>();
        when(serverUtils.getAllCardLists(board)).thenReturn(cardLists);
        List<CardList> actual = boardOverviewService.getCardLists(board);

        assertEquals(cardLists, actual);
    }

    @Test
    public void testUpdateBoardTitle() {
        Board board = new Board();
        String title = "asd";
        when(serverUtils.updateBoardTitle(board, title)).thenReturn(board);
        Board actual = boardOverviewService.updateBoardTitle(board, title);

        assertEquals(board, actual);
    }

    @Test
    public void testRemoveBoard() {
        Board board = new Board();

        when(serverUtils.removeBoard(board)).thenReturn(Response.accepted().build());
        boardOverviewService.removeBoard(board);

        verify(serverUtils, times(1)).removeBoard(board);
    }

    @Test
    public void testRemoveCard() {
        Board board = new Board();
        CardList cardList = new CardList();
        cardList.setId(1L);
        Card card = new Card();

        when(serverUtils.removeCardFromList(card, 1L,
                board)).thenReturn(Response.accepted().build());
        boardOverviewService.removeCard(card, 1L, board);

        verify(serverUtils, times(1))
                .removeCardFromList(card, 1L, board);
    }

    @Test
    public void testRemoveCardWhenDragged() {
        Board board = new Board();
        CardList cardList = new CardList();
        cardList.setId(1L);
        Card card = new Card();

        when(serverUtils.removeCardFromListWhenDragged(card, 1L,
                board)).thenReturn(Response.accepted().build());
        boardOverviewService.removeCardWhenDragged(card, 1L, board);

        verify(serverUtils, times(1))
                .removeCardFromListWhenDragged(card, 1L, board);
    }

    @Test
    public void testRemoveCardList() {
        Board board = new Board();
        CardList cardList = new CardList();

        when(serverUtils.removeCardListFromBoard(cardList,
                board)).thenReturn(Response.accepted().build());
        boardOverviewService.removeCardList(cardList, board);

        verify(serverUtils, times(1))
                .removeCardListFromBoard(cardList, board);
    }

    @Test
    public void testAddSubtask() {
        Board board = new Board();
        SubTask subTask = new SubTask();
        Card card = new Card();
        card.setId(1L);

        when(serverUtils.addSubTaskToCard(subTask, 1L,
                board)).thenReturn(subTask);


        SubTask actual = boardOverviewService.addSubTask(subTask, 1L, board);

        assertEquals(subTask, actual);
    }

    @Test
    public void testGetBoardByKey() {

        Board board = new Board();
        String key = "Asd";
        board.setKey(key);

        when(serverUtils.getBoardByKey(key)).thenReturn(board);


        Board actual = boardOverviewService.getBoardByKey(key);

        assertEquals(board, actual);
    }

    @Test
    public void testAddBoardToUser() {

        Board board = new Board();
        String key = "Asd";
        String username = "fgh";
        board.setKey(key);
        User user = new User(username, new ArrayList<>());

        when(serverUtils.addBoardToUser(key, username)).thenReturn(user);


        User actual = boardOverviewService.addBoardToUser(key, username);

        assertEquals(user, actual);
    }

    @Test
    public void testGetUserBoards() {

        Board board = new Board();
        String username = "fgh";
        List<Board> boards = new ArrayList<>();
        boards.add(board);

        when(serverUtils.getUserBoards(username)).thenReturn(boards);


        List<Board> actual = boardOverviewService.getUserBoards(username);

        assertEquals(boards, actual);
    }

    @Test
    public void testGetUser() {
        String username = "asd";
        User user = new User(username, new ArrayList<>());

        when(serverUtils.getUser(username)).thenReturn(user);


        User actual = boardOverviewService.getUser(username);

        assertEquals(user, actual);
    }

    @Test
    public void testCreateUser() {
        String username = "asd";
        User user = new User(username, new ArrayList<>());

        when(serverUtils.createUser(username)).thenReturn(user);


        User actual = boardOverviewService.createUser(username);

        assertEquals(user, actual);
    }

    @Test
    public void testAddCardAtIndex() {
        Board board = new Board();
        CardList cardList = new CardList(new ArrayList<>(),"asd");
        Card card = new Card();
        cardList.setId(1L);
        card.setId(2L);

        when(serverUtils.addCardToCardListWithIndex(card,
                2L,1L, 0,board)).thenReturn(card);


        Card actual = boardOverviewService.addCardAtIndex(card,2L,
                1L,0,board);

        assertEquals(card, actual);
    }
    @Test
    public void testGetCards() {
        CardList cardList = new CardList(new ArrayList<>(),"asd");
        cardList.setId(1L);

        when(serverUtils.getCardsOfCardList(1L)).thenReturn(cardList.getCards());


        List<Card> actual = boardOverviewService.getCards(1L);

        assertEquals(cardList.getCards(), actual);
    }

    @Test
    public void testGetCardList() {
        CardList cardList = new CardList(new ArrayList<>(),"asd");
        cardList.setId(1L);

        when(serverUtils.getCardList(1L)).thenReturn(cardList);


        CardList actual = boardOverviewService.getCardList(1L);

        assertEquals(cardList, actual);
    }

    @Test
    public void testGetBoard() {
        Board board = new Board();
        board.setId(1L);

        when(serverUtils.getBoard(1L)).thenReturn(board);


        Board actual = boardOverviewService.getBoard(1L);

        assertEquals(board, actual);
    }

    @Test
    public void testCreateBoard() {
        Board board = new Board();
        board.setId(1L);

        when(serverUtils.createBoard("title","user")).thenReturn(board);


        Board actual = boardOverviewService.createBoard("title","user");

        assertEquals(board, actual);
    }

    @Test
    public void testGetAllBoards() {
        List<Board> boards= new ArrayList<>();

        Board board = new Board();
        board.setId(1L);
        boards.add(board);
        when(serverUtils.getAllBoards()).thenReturn(boards);


        List<Board> actual = boardOverviewService.getAllBoards();

        assertEquals(boards, actual);
    }

    @Test
    public void testRemoveBoardForUser() {
        Board board = new Board();
        User user = new User("user",new ArrayList<>());
        board.setId(1L);
        when(serverUtils.removeBoardForUser("user",board)).thenReturn(user);


        User actual = boardOverviewService.removeBoardForUser(board,"user");

        assertEquals(user, actual);
    }

    @Test
    public void testUpdateCardListTitle() {
        Board board = new Board();
        CardList cardList = new CardList();
        board.setId(1L);
        cardList.setId(2L);
        when(serverUtils.updateCardListTitle(2L,"title",board)).thenReturn(cardList);


        CardList actual = boardOverviewService.updateCardListTitle(2L,"title",board);

        assertEquals(cardList, actual);
    }

    @Test
    public void testUpdateCardTitle() {
        Board board = new Board();
        Card card = new Card();
        board.setId(1L);
        card.setId(2L);
        when(serverUtils.updateCardTitle(2L,"title",board)).thenReturn(card);


        Card actual = boardOverviewService.updateCardTitle(2L,"title",board);

        assertEquals(card, actual);
    }

    @Test
    public void testUpdateCardDesc() {
        Board board = new Board();
        Card card = new Card();
        board.setId(1L);
        card.setId(2L);
        when(serverUtils.updateCardDescription(2L,"desc",board)).thenReturn(card);


        Card actual = boardOverviewService.updateCardDescription(2L,"desc",board);

        assertEquals(card, actual);
    }

    @Test
    public void testUpdateSubTaskTitle() {
        Board board = new Board();
        SubTask task = new SubTask();
        task.setId(3L);
        Card card = new Card();
        board.setId(1L);
        card.setId(2L);
        when(serverUtils.updateTitleSubTask(3L,"title",board,card)).thenReturn(task);


        SubTask actual = boardOverviewService.updateTitleSubTask(3L,"title",board,card);

        assertEquals(task, actual);
    }

    @Test
    public void testUpdateSubTaskIsChecked() {
        Board board = new Board();
        SubTask task = new SubTask();
        task.setId(3L);
        Card card = new Card();
        board.setId(1L);
        card.setId(2L);
        when(serverUtils.updateIsChecked(3L,true,board,card)).thenReturn(task);


        SubTask actual = boardOverviewService.updateCheckboxTask(3L,true,board,card);

        assertEquals(task, actual);
    }

    @Test
    public void testRemoveSubTask() {
        Board board = new Board();
        SubTask task = new SubTask();
        task.setId(3L);
        Card card = new Card();
        board.setId(1L);
        card.setId(2L);
        when(serverUtils.removeSubTask(task,2L,board)).thenReturn(Response.accepted().build());


        boardOverviewService.removeSubTask(task,2L,board);

        verify(serverUtils, times(1)).removeSubTask(task,2L,board);
    }

    @Test
    public void testUpdateCardSubtasks() {
        Board board = new Board();
        SubTask task = new SubTask();
        List<SubTask> subTasks = new ArrayList<>();
        task.setId(3L);
        Card card = new Card();
        board.setId(1L);
        card.setId(2L);
        subTasks.add(task);
        when(serverUtils.updateCardSubTasks(2L,subTasks,board)).thenReturn(card);


        Card actual = boardOverviewService.updateCardSubTasks(2L,subTasks,board);

        assertEquals(card, actual);
    }

}



