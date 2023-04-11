package client.scenes;

import client.services.TagsListService;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.Tag;
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

public class TagsListServiceTest {
    @Mock
    private ServerUtils serverUtils;

    @InjectMocks
    private TagsListService tagsListService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTagToBoard() {
        Tag tag = new Tag();
        Board board = new Board();

        board.setId(1L);

        when(serverUtils.addTagToBoard(tag, 1L)).thenReturn(tag);

        Tag actual = tagsListService.addTagToBoard(tag, 1L);

        assertEquals(tag, actual);
    }

    @Test
    public void testGetAllTags() {
        Tag tag = new Tag();
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        Board board = new Board();

        board.setId(1L);

        when(serverUtils.getAllTags(1L)).thenReturn(tags);

        List<Tag> actual = tagsListService.getAllTags(1L);

        assertEquals(tags, actual);
    }

    @Test
    public void testGetAllTagsFromCard() {
        Tag tag = new Tag();
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        Card card = new Card();
        card.setId(1L);

        when(serverUtils.getAllTagsFromCard(1L)).thenReturn(tags);

        List<Tag> actual = tagsListService.getAllTagsFromCard(1L);

        assertEquals(tags, actual);
    }

    @Test
    public void testUpdateTagName() {
        Tag tag = new Tag();
        tag.setId(1L);
        Board board = new Board();
        String title = "asd";

        when(serverUtils.updateTagName(1L, title, board)).thenReturn(tag);

        Tag actual = tagsListService.updateTagName(1L, title, board);

        assertEquals(tag, actual);
    }

    @Test
    public void testRemoveTag() {
        Tag tag = new Tag();
        tag.setId(1L);
        Board board = new Board();

        when(serverUtils.removeTag(1L, board)).thenReturn(Response.accepted().build());
        tagsListService.removeTagFromBoard(1L, board);

        verify(serverUtils, times(1)).removeTag(1L, board);
    }

    @Test
    public void testRemoveTagFromCard() {
        Tag tag = new Tag();
        tag.setId(1L);
        Card card = new Card();
        card.setId(2L);
        Board board = new Board();

        when(serverUtils.removeTagFromCard(1L, 2L, board)).thenReturn(Response.accepted().build());
        tagsListService.removeTagFromCard(tag, 2L, board);

        verify(serverUtils, times(1)).removeTagFromCard(1L, 2L, board);
    }

    @Test
    public void testAddTagToCard() {
        Tag tag = new Tag();
        Card card = new Card();
        Board board = new Board();
        card.setId(1L);
        tag.setId(2L);

        when(serverUtils.addTagToCard(tag,1L,board)).thenReturn(tag);

        Tag actual = tagsListService.addTagToCard(tag,1L,board);

        assertEquals(tag,actual);
    }

}
