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
        cards.add(new Card("t1"));
        cards.add(new Card("t2"));
        cards2.add(new Card("t3"));
        cardLists.add(new CardList(cards,"l1"));
        cardLists2.add(new CardList(cards2,"l2"));
    }

    @Test
    public void checkConstructor() {
        var b = new Board(cardLists);
        assertEquals(cardLists, b.getCardLists());
    }

    @Test
    public void equalsHashCode() {
        var b1 = new Board(cardLists);
        var b2 = new Board(cardLists);
        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var b1 = new Board(cardLists);
        var b2 = new Board(cardLists2);
        assertNotEquals(b1, b2);
        assertNotEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    public void hasToString() {
        var actual = new Board(cardLists).toString();
        assertTrue(actual.contains(Board.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("cardLists"));
    }
}
