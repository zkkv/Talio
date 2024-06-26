package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

    private List<CardList> cardLists;

    private List<CardList> cardLists2;
    private List<Card> cards;
    private List<Card> cards2;

    @BeforeEach
    public void setup(){
        cardLists = new ArrayList<>();
        cardLists2 = new ArrayList<>();
        cards = new ArrayList<>();
        cards2 = new ArrayList<>();
        cards.add(new Card("t1",new ArrayList<>(),new ArrayList<>()));
        cards.add(new Card("t2",new ArrayList<>(),new ArrayList<>()));
        cards2.add(new Card("t3",new ArrayList<>(),new ArrayList<>()));
        cardLists.add(new CardList(cards,"l1"));
        cardLists2.add(new CardList(cards2,"l2"));
    }

    @Test
    public void checkConstructor() {
        var b = new Board(cardLists,"b1");
        assertEquals(cardLists, b.getCardLists());
    }

    @Test
    public void equalsHashCode() {
        var b1 = new Board(cardLists,"b1");
        var b2 = new Board(cardLists,"b1");
        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var b1 = new Board(cardLists,"b1");
        var b2 = new Board(cardLists2,"b2");
        assertNotEquals(b1, b2);
        assertNotEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    public void hasToString() {
        var actual = new Board(cardLists,"b1").toString();
        assertTrue(actual.contains(Board.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("cardLists"));
    }

    @Test
    public void testGetKey(){
        var b1 = new Board();
        String key = "asd";
        b1.setKey(key);

        assertEquals(key,b1.getKey());
    }
}