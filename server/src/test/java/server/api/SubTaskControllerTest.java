package server.api;

import commons.Board;
import commons.Card;
import commons.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.services.BoardService;
import server.services.CardService;
import server.services.SubTaskService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SubTaskControllerTest {

    @Mock
    private SubTaskService subTaskService;

    @Mock
    private BoardService boardService;

    @Mock
    private CardService cardService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private CardController cardController;

    @InjectMocks
    private SubTaskController subTaskController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        List<SubTask> expectedSubtasks = new ArrayList<>();
        SubTask subTask1 = new SubTask();
        subTask1.setId(1L);
        expectedSubtasks.add(subTask1);

        SubTask subTask2 = new SubTask();
        subTask2.setId(2L);
        expectedSubtasks.add(subTask2);

        when(subTaskService.getAllSubTasks()).thenReturn(expectedSubtasks);

        List<SubTask> actualSubTasks = subTaskController.getAll().getBody();

        assertEquals(expectedSubtasks, actualSubTasks);
    }

    @Test
    public void testGetByIdWithValidId() {
        SubTask expectedSubtask = new SubTask();
        expectedSubtask.setId(2L);

        when(subTaskService.exists(2L)).thenReturn(true);
        when(subTaskService.getSubTask(2L)).thenReturn(expectedSubtask);

        ResponseEntity<SubTask> actualResponse = subTaskController.getById(2L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedSubtask, actualResponse.getBody());
    }


    @Test
    public void testGetByIdWithInvalidId() {
        when(subTaskService.exists(-1L)).thenReturn(false);

        ResponseEntity<SubTask> actualResponse = subTaskController.getById(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
    }

    @Test
    public void testUpdateTitleSubTask() {
        SubTask titleSubtask = new SubTask();
        titleSubtask.setId(1L);
        titleSubtask.setName("Title 1");
        Board board = new Board();
        board.setId(2L);
        Card card = new Card();
        card.setId(3L);
        when(cardService.getCard(3L)).thenReturn(card);
        when(cardService.save(card)).thenReturn(card);
        when(subTaskService.getSubTask(1L)).thenReturn(titleSubtask);
        when(subTaskService.save(titleSubtask)).thenReturn(titleSubtask);
        when(boardService.getBoard(2L)).thenReturn(board);

        ResponseEntity<SubTask> actualResponse =
                subTaskController.updateTitleSubTask("New Title",1L,3L,2L);
        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(titleSubtask.getName(),actualResponse.getBody().getName());
    }
    @Test
    public void testAddCard() {
        Card card = new Card("Card 1",new ArrayList<>(),new ArrayList<>());
        SubTask subTaskToAdd = new SubTask();
        subTaskToAdd.setId(1L);

        Board board = new Board();
        board.setId(2L);

        when(subTaskService.save(subTaskToAdd)).thenReturn(subTaskToAdd);
        when(cardService.getCard(1L)).thenReturn(card);
        when(cardService.save(card)).thenReturn(card);
        when(boardService.getBoard(2L)).thenReturn(board);

        ResponseEntity<SubTask> actualResponse = cardController.addSubTask(subTaskToAdd, 1L,2L);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(subTaskToAdd, actualResponse.getBody());
        assertEquals(card.getTasks().size(),1);
    }

    @Test
    public void testDeleteCard(){
        SubTask subTaskToBeRemoved = new SubTask();
        subTaskToBeRemoved.setId(1L);
        Card card = new Card("Card",new ArrayList<>(),new ArrayList<>());
        card.setId(2L);
        Board board = new Board();
        board.setId(2L);

        when(boardService.getBoard(2L)).thenReturn(board);

        when(cardService.exists(2L)).thenReturn(true);
        when(subTaskService.exists(1L)).thenReturn(true);

        when(cardService.getCard(2L)).thenReturn(card);
        when(subTaskService.getSubTask(1L)).thenReturn(subTaskToBeRemoved);

        when(cardService.save(card)).thenReturn(card);

        ResponseEntity<SubTask> actualResponse = cardController.removeSubTask(2L, 1L,2L);

        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(subTaskToBeRemoved,actualResponse.getBody());
        assertEquals(0,card.getTasks().size());
    }

}
