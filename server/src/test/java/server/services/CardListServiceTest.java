package server.services;

import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.CardListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CardListServiceTest {
    @Mock
    private CardListRepository cardListRepository;

    @InjectMocks
    private CardListService cardListService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBoard() {
        CardList expectedcardList = new CardList();
        expectedcardList.setId(1L);

        when(cardListRepository.findById(1L)).thenReturn(Optional.of(expectedcardList));

        CardList actualCardList = cardListService.getCardList(1L);

        assertEquals(expectedcardList, actualCardList);
    }

    @Test
    public void testGetAllBoards() {
        List<CardList> expectedCardLists = new ArrayList<>();
        CardList cardList1 = new CardList();
        cardList1.setId(1L);
        expectedCardLists.add(cardList1);

        CardList cardList2 = new CardList();
        cardList2.setId(2L);
        expectedCardLists.add(cardList2);

        when(cardListRepository.findAll()).thenReturn(expectedCardLists);

        List<CardList> actualCardLists = cardListService.getAllCardLists();

        assertEquals(expectedCardLists, actualCardLists);
    }

    @Test
    public void testExists() {
        when(cardListRepository.existsById(anyLong())).thenReturn(true);

        boolean exists = cardListService.exists(1L);

        assertTrue(exists);
    }

    @Test
    public void testDelete() {
        doNothing().when(cardListRepository).deleteById(anyLong());

        cardListService.delete(1L);

        verify(cardListRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSave() {
        CardList cardListToSave = new CardList();


        CardList expectedSavedCardList = new CardList();
        expectedSavedCardList.setId(1L);


        when(cardListRepository.save(cardListToSave)).thenReturn(expectedSavedCardList);

        CardList actualSavedCardList = cardListService.save(cardListToSave);

        assertEquals(expectedSavedCardList, actualSavedCardList);
    }
}
