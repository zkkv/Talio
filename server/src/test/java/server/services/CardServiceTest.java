package server.services;

import commons.Card;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBoard() {
        Card expectedCard = new Card();
        expectedCard.setId(1L);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(expectedCard));

        Card actualCard = cardService.getCard(1L);

        assertEquals(expectedCard, actualCard);
    }

    @Test
    public void testGetAllBoards() {
        List<Card> expectedCards = new ArrayList<>();
        Card card1 = new Card();
        card1.setId(1L);
        expectedCards.add(card1);

        Card card2 = new Card();
        card2.setId(2L);
        expectedCards.add(card2);

        when(cardRepository.findAll()).thenReturn(expectedCards);

        List<Card> actualCards = cardService.getAllCards();

        assertEquals(expectedCards, actualCards);
    }

    @Test
    public void testExists() {
        when(cardRepository.existsById(anyLong())).thenReturn(true);

        boolean exists = cardService.exists(1L);

        assertTrue(exists);
    }

    @Test
    public void testDelete() {
        doNothing().when(cardRepository).deleteById(anyLong());

        cardService.delete(1L);

        verify(cardRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSave() {
        Card cardToSave = new Card();


        Card expectedSavedCard = new Card();
        expectedSavedCard.setId(1L);


        when(cardRepository.save(cardToSave)).thenReturn(expectedSavedCard);

        Card actualSavedCard = cardService.save(cardToSave);

        assertEquals(expectedSavedCard, actualSavedCard);
    }

    @Test
    public void testGetTags() {
        Card cardWithTags = new Card();
        cardWithTags.setId(2L);

        List<Tag> tagsForCard = new ArrayList<>();
        List<Tag> tagsToExcept = new ArrayList<>();

        Tag tag = new Tag();
        tag.setId(1L);

        tagsForCard.add(tag);
        tagsForCard.add(tag);

        tagsToExcept.add(tag);
        tagsToExcept.add(tag);

        cardWithTags.setTags(tagsForCard);

        when(cardRepository.findTagsById(cardWithTags.getId())).thenReturn(tagsToExcept);

        List<Tag> actualList = cardService.getTags(cardWithTags.getId());

        assertEquals(tagsToExcept, actualList);
    }


}
