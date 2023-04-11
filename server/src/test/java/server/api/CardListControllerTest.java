package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
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
import server.services.CardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CardListControllerTest {

    @Mock
    private CardListService cardListService;

    @Mock
    private BoardService boardService;
    @Mock
    private CardService cardService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private CardListController cardListController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        List<CardList> expectedCardLists = new ArrayList<>();
        CardList cardList1 = new CardList();
        cardList1.setId(1L);
        expectedCardLists.add(cardList1);

        CardList cardList2 = new CardList();
        cardList2.setId(2L);
        expectedCardLists.add(cardList2);

        when(cardListService.getAllCardLists()).thenReturn(expectedCardLists);

        List<CardList> actualCardLists = cardListController.getAll().getBody();

        assertEquals(expectedCardLists, actualCardLists);
    }

    @Test
    public void testGetByIdWithValidId() {
        CardList expectedCardList = new CardList();
        expectedCardList.setId(1L);

        when(cardListService.exists(1L)).thenReturn(true);
        when(cardListService.getCardList(1L)).thenReturn(expectedCardList);

        ResponseEntity<CardList> actualResponse = cardListController.getById(1L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedCardList, actualResponse.getBody());
    }

    @Test
    public void testGetByIdWithInvalidId() {
        when(cardListService.exists(-1L)).thenReturn(false);

        ResponseEntity<CardList> actualResponse = cardListController.getById(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
    }

    @Test
    public void testGetCards() {
        List<Card> expectedCards = new ArrayList<>();
        Card card1 = new Card();
        card1.setId(1L);
        expectedCards.add(card1);

        Card card2 = new Card();
        card2.setId(2L);
        expectedCards.add(card2);

        CardList cardList = new CardList();
        cardList.setCards(expectedCards);

        when(cardListService.getCardList(1L)).thenReturn(cardList);

        ResponseEntity<List<Card>> actualResponse = cardListController.getCards(1L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedCards, actualResponse.getBody());

    }

    @Test
    public void testAddCard() {
        CardList cardList = new CardList(new ArrayList<>(),"List 1");
        Card card = new Card();
        card.setId(1L);

        Board board = new Board();
        board.setId(2L);

        when(cardService.save(card)).thenReturn(card);
        when(cardListService.getCardList(1L)).thenReturn(cardList);
        when(cardListService.save(cardList)).thenReturn(cardList);
        when(boardService.getBoard(2L)).thenReturn(board);

        ResponseEntity<Card> actualResponse = cardListController.addCard(card, 1L,2L);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(card, actualResponse.getBody());
        assertEquals(cardList.getCards().size(),1);
    }

    @Test
    public void testAddCardAtIndex(){

        Card savedCard = new Card();
        savedCard.setId(1L);

        CardList cardList = new CardList();
        cardList.setId(1L);
        List<Card> cards = new ArrayList<>();
        cards.add(new Card());
        cards.add(new Card());
        cardList.setCards(cards);

        Board board = new Board();
        board.setId(2L);

        when(boardService.getBoard(2L)).thenReturn(board);

        when(cardService.save(savedCard)).thenReturn(savedCard);
        when(cardListService.getCardList(1L)).thenReturn(cardList);

        ResponseEntity<Card> actualResponse =
            cardListController.addCardAtIndex(savedCard,1L, 1L,1,2L);

        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(cardList.getCards().get(1),cardService.getCard(1L));

    }
    @Test
    public void testUpdateCardListTitle(){
        CardList cardListToRename = new CardList();
        cardListToRename.setTitle("Title 1");
        Board board = new Board();
        board.setId(2L);

        when(boardService.getBoard(2L)).thenReturn(board);
        when(cardListService.getCardList(1L)).thenReturn(cardListToRename);
        when(cardListService.save(cardListToRename)).thenReturn(cardListToRename);

        ResponseEntity<CardList> actualResponse = cardListController.updateTitle("New Title",
            1L,2L);

        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(cardListToRename.getTitle(),actualResponse.getBody().getTitle());
    }

    @Test
    public void testRemoveCardValid(){
        List<Card> cards = new ArrayList<>();
        Card cardToBeRemoved = new Card();
        cardToBeRemoved.setId(1L);
        CardList cardList = new CardList(cards,"List 1");
        cardList.setId(2L);
        Board board = new Board();
        board.setId(2L);

        when(boardService.getBoard(2L)).thenReturn(board);

        when(cardListService.exists(2L)).thenReturn(true);
        when(cardService.exists(1L)).thenReturn(true);

        when(cardListService.getCardList(2L)).thenReturn(cardList);
        when(cardService.getCard(1L)).thenReturn(cardToBeRemoved);

        when(cardListService.save(cardList)).thenReturn(cardList);

        ResponseEntity<Card> actualResponse = cardListController.removeCard(2L,
            1L,3L);

        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(cardToBeRemoved,actualResponse.getBody());
        assertEquals(0,cardList.getCards().size());
    }

    @Test
    public void testRemoveCardInvalidCard(){
        when(cardService.exists(-1L)).thenReturn(false);

        ResponseEntity<Card> actualResponse = cardListController.removeCard(2L,-1L,3L);

        assertEquals(HttpStatus.NOT_FOUND,actualResponse.getStatusCode());
    }

    @Test
    public void testRemoveCardInvalidCardList(){
        when(cardListService.exists(-1L)).thenReturn(false);

        ResponseEntity<Card> actualResponse = cardListController.removeCard(-1L,2L,3L);

        assertEquals(HttpStatus.NOT_FOUND,actualResponse.getStatusCode());
    }

    @Test
    public void testRemoveCardForDragValid(){
        List<Card> cards = new ArrayList<>();
        Card cardToBeRemoved = new Card();
        cardToBeRemoved.setId(1L);
        CardList cardList = new CardList(cards,"List 1");
        cardList.setId(2L);
        Board board = new Board();
        board.setId(2L);

        when(boardService.getBoard(2L)).thenReturn(board);

        when(cardListService.exists(2L)).thenReturn(true);
        when(cardService.exists(1L)).thenReturn(true);

        when(cardListService.getCardList(2L)).thenReturn(cardList);
        when(cardService.getCard(1L)).thenReturn(cardToBeRemoved);

        when(cardListService.save(cardList)).thenReturn(cardList);

        ResponseEntity<Card> actualResponse = cardListController.removeCardFromList(2L,
            1L,3L);

        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertEquals(cardToBeRemoved,actualResponse.getBody());
        assertEquals(0,cardList.getCards().size());
    }

    @Test
    public void testRemoveCardForDragInvalidCard(){
        when(cardService.exists(-1L)).thenReturn(false);

        ResponseEntity<Card> actualResponse = cardListController.removeCardFromList(2L,-1L,3L);

        assertEquals(HttpStatus.NOT_FOUND,actualResponse.getStatusCode());
    }

    @Test
    public void testRemoveCardForDragInvalidCardList(){
        when(cardListService.exists(-1L)).thenReturn(false);

        ResponseEntity<Card> actualResponse = cardListController.removeCardFromList(-1L,2L,3L);

        assertEquals(HttpStatus.NOT_FOUND,actualResponse.getStatusCode());
    }
}