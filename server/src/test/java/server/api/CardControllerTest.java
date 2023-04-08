package server.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import commons.Board;
import commons.Card;
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
import server.services.CardService;
import server.services.TagService;


public class CardControllerTest {

    @Mock
    private CardService cardService;

    @Mock
    private BoardService boardService;

    @Mock
    private TagService tagService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        List<Card> expectedCards = new ArrayList<>();
        Card card1 = new Card();
        card1.setId(1L);
        expectedCards.add(card1);

        Card card2 = new Card();
        card2.setId(2L);
        expectedCards.add(card2);

        when(cardService.getAllCards()).thenReturn(expectedCards);

        List<Card> actualCards = cardController.getAll().getBody();

        assertEquals(expectedCards, actualCards);
    }

    @Test
    public void testGetByIdWithValidId() {
        Card expectedCard = new Card();
        expectedCard.setId(1L);

        when(cardService.exists(1L)).thenReturn(true);
        when(cardService.getCard(1L)).thenReturn(expectedCard);

        ResponseEntity<Card> actualResponse = cardController.getById(1L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedCard, actualResponse.getBody());
    }

    @Test
    public void testGetByIdWithInvalidId() {
        when(cardService.exists(-1L)).thenReturn(false);

        ResponseEntity<Card> actualResponse = cardController.getById(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
    }


    @Test
    public void testUpdateTitleValid() {
        Card cardToRename = new Card();
        cardToRename.setTitle("Title 1");
        Board board = new Board();
        board.setId(2L);
        when(cardService.getCard(1L)).thenReturn(cardToRename);
        when(cardService.save(cardToRename)).thenReturn(cardToRename);
        when(boardService.getBoard(2L)).thenReturn(board);


        ResponseEntity<Card> actualResponse = cardController.updateTitle("New Title",1L,2L);
        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(cardToRename.getTitle(),actualResponse.getBody().getTitle());
    }

    @Test
    public void testAddTag() {
        Tag tag = new Tag();
        tag.setId(2L);

        Card card = new Card();
        card.setId(1L);
        card.setTags(new ArrayList<>(Arrays.asList(tag)));

        Board board = new Board();
        board.setId(3L);

        when(cardService.getCard(1L)).thenReturn(card);
        when(cardService.save(any(Card.class))).thenReturn(card);
        when(boardService.getBoard(3L)).thenReturn(board);

        when(tagService.save(any(Tag.class))).thenReturn(tag);

        ResponseEntity<Tag> actualResponse = cardController.addTag(tag, 1L,3L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(tag, actualResponse.getBody());
    }

    @Test
    public void testRemoveTag() {
        Card cardWithTags = new Card();
        cardWithTags.setId(1L);

        Tag tag = new Tag();
        tag.setId(2L);

        List<Tag> tags = new ArrayList<>();
        tags.add(tag);

        cardWithTags.setTags(tags);

        Board board = new Board();
        board.setId(3L);

        when(tagService.getTag(2L)).thenReturn(tag);
        when(tagService.save(tag)).thenReturn(tag);
        when(cardService.getCard(1L)).thenReturn(cardWithTags);
        when(boardService.getBoard(3L)).thenReturn(board);

        ResponseEntity<Tag> actualResponse = cardController.removeTag(2L, 1L,3L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(tag, actualResponse.getBody());
    }

    @Test
    public void testGetAllTags() {
        Tag tag1 = new Tag();
        tag1.setId(1L);
        Tag tag2 = new Tag();
        tag2.setId(2L);

        Card card = new Card();
        card.setId(3L);

        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        Board board = new Board();
        board.setId(4L);

        card.setTags(tags);

        when(cardService.getCard(3L)).thenReturn(card);
        when(cardService.save(any(Card.class))).thenReturn(card);
        when(tagService.save(any(Tag.class))).thenReturn(tag1);
        when(tagService.save(any(Tag.class))).thenReturn(tag2);
        when(boardService.getBoard(4L)).thenReturn(board);

        cardController.addTag(tag1, 3L,4L);
        cardController.addTag(tag1, 3L,4L);

        ResponseEntity<List<Tag>> actualResponse = cardController.getAllTags(3L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(tags, actualResponse.getBody());
    }
}