package server.services;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import server.database.BoardRepository;

class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBoard() {
        Board expectedBoard = new Board();
        expectedBoard.setId(1L);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(expectedBoard));

        Board actualBoard = boardService.getBoard(1L);

        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    public void testGetAllBoards() {
        List<Board> expectedBoards = new ArrayList<>();
        Board board1 = new Board();
        board1.setId(1L);
        expectedBoards.add(board1);

        Board board2 = new Board();
        board2.setId(2L);
        expectedBoards.add(board2);

        when(boardRepository.findAll()).thenReturn(expectedBoards);

        List<Board> actualBoards = boardService.getAllBoards();

        assertEquals(expectedBoards, actualBoards);
    }

    @Test
    public void testGetBoardByKey(){
        Board board =new Board();
        board.setKey("asd");

        when(boardRepository.findBoardByKey("asd")).thenReturn(board);

        Board actual = boardService.getBoardByKey("asd");

        assertEquals(board,actual);
    }
    @Test
    public void testExists() {
        when(boardRepository.existsById(anyLong())).thenReturn(true);

        boolean exists = boardService.exists(1L);

        assertTrue(exists);
    }

    @Test
    public void testDelete() {
        doNothing().when(boardRepository).deleteById(anyLong());

        boardService.delete(1L);

        verify(boardRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSave() {
        Board boardToSave = new Board();


        Board expectedSavedBoard = new Board();
        expectedSavedBoard.setId(1L);


        when(boardRepository.save(boardToSave)).thenReturn(expectedSavedBoard);

        Board actualSavedBoard = boardService.save(boardToSave);

        assertEquals(expectedSavedBoard, actualSavedBoard);
    }

    @Test
    public void testExistsByKey(){
        Board board = new Board();
        board.setKey("asd");

        when(boardRepository.existsByKey("asd")).thenReturn(true);

        boolean actual = boardService.existsByKey("asd");

        assertTrue(actual);
    }
}
