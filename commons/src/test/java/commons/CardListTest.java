package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CardListTest {

    List<Card> list1;
    List<Card> list2;

    @BeforeEach
    public void initializeObjects() {
        Card card1 = new Card("title1",new ArrayList<>(),new ArrayList<>());
        Card card2 = new Card("title2",new ArrayList<>(),new ArrayList<>());
        Card card3 = new Card("title3",new ArrayList<>(),new ArrayList<>());

        Card card4 = new Card("title4",new ArrayList<>(),new ArrayList<>());
        Card card5 = new Card("title5",new ArrayList<>(),new ArrayList<>());
        Card card6 = new Card("title6",new ArrayList<>(),new ArrayList<>());

        list1 = new ArrayList<>(3);
        list1.add(card1);
        list1.add(card2);
        list1.add(card3);

        list2 = new ArrayList<>(3);
        list1.add(card4);
        list1.add(card5);
        list1.add(card6);
    }

    @Test
    public void constructorTest() {
        CardList cardList1 = new CardList(list1, "TODO");
        assertEquals("TODO", cardList1.getTitle());
        assertEquals(list1, cardList1.getCards());
    }

    @Test
    public void notEqualsSameListDifferentTitlesTest() {
        CardList cardList1 = new CardList(list1, "TODO");
        CardList cardList2 = new CardList(list1, "Done");
        assertNotEquals(cardList1, cardList2);
    }

    @Test
    public void notEqualsDifferentListsSameTitleTest() {
        CardList cardList1 = new CardList(list1, "TODO");
        CardList cardList2 = new CardList(list2, "TODO");
        assertNotEquals(cardList1, cardList2);
    }
    @Test
    public void notEqualsDifferentListsDifferentTitlesTest() {
        CardList cardList1 = new CardList(list1, "TODO");
        CardList cardList2 = new CardList(list2, "Done");
        assertNotEquals(cardList1, cardList2);
    }

    @Test
    public void equalsTest() {
        CardList cardList1 = new CardList(list1, "TODO");
        CardList cardList2 = new CardList(list1, "TODO");
        assertEquals(cardList1, cardList2);
    }

    @Test
    public void hashCodeTest() {
        CardList cardList1 = new CardList(list1, "TODO");
        CardList cardList2 = new CardList(list1, "TODO");
        assertEquals(cardList1.hashCode(), cardList2.hashCode());
    }

    @Test
    public void testGetId(){
        CardList cardList = new CardList();
        cardList.setId(1L);

        assertEquals(1L,cardList.getId());
    }
}
