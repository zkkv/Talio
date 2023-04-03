package server.api;

import commons.Board;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.services.BoardService;
import server.services.TagService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TagControllerTest {
    @Mock
    private TagService tagService;
    @Mock
    private BoardService boardService;
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private TagController tagController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateTitle(){
        Board board =new Board();
        board.setId(1L);

        Tag tag = new Tag();
        tag.setId(2L);
        tag.setTitle("Title 1");

        when(tagService.getTag(2L)).thenReturn(tag);
        when(tagService.save(tag)).thenReturn(tag);
        when(boardService.getBoard(1L)).thenReturn(board);

        ResponseEntity<Tag> actualResponse = tagController.updateTitle("Title 2",2L,1L);

        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals("Title 2",tag.getTitle());
    }

    @Test
    public void testRemove() {
        Tag tagToRemove = new Tag();
        tagToRemove.setId(1L);
        Board board = new Board();
        board.setId(2L);
        List<Tag> tags = new ArrayList<>();
        tags.add(tagToRemove);
        board.setTags(tags);

        when(tagService.getTag(1L)).thenReturn(tagToRemove);

        when(boardService.getBoard(2L)).thenReturn(board);
        when(boardService.save(any(Board.class))).thenReturn(board);

        doNothing().when(tagService).removeTag(1L);

        ResponseEntity<Tag> actualResponse = tagController.removeTag(2L,1L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(tagToRemove, actualResponse.getBody());
    }
}
