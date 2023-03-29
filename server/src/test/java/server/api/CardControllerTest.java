package server.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.services.CardService;


public class CardControllerTest {

    @Mock
    private CardService cardService;

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
        when(cardService.getCard(1L)).thenReturn(cardToRename);
        when(cardService.save(cardToRename)).thenReturn(cardToRename);

        ResponseEntity<Card> actualResponse = cardController.updateTitle("New Title",1L);
        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(cardToRename.getTitle(),actualResponse.getBody().getTitle());
    }


}
