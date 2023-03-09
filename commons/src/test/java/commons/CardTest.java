package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    public void checkConstructor() {
        Card card = new Card("card");
        assertEquals("card", card.title);
    }

    @Test
    void testEquals() {
        Card card1 = new Card("card");
        Card card2 = new Card("card");
        assertEquals(card1, card2);
    }

    @Test
    void testNotEquals() {
        Card card1 = new Card("card1");
        Card card2 = new Card("card2");
        assertNotEquals(card1, card2);
    }

    @Test
    void testEqualsHashCode() {
        Card card1 = new Card("card");
        Card card2 = new Card("card");
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void testNotEqualsHashCode() {
        Card card1 = new Card("card1");
        Card card2 = new Card("card2");
        assertNotEquals(card1.hashCode(), card2.hashCode());
    }

//    @Test
//    void testToString() {
//        Card card = new Card("card");
//        assertEquals("", card.toString());
//    }
}