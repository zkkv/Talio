package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    public void checkConstructor() {
        Card card = new Card("card",new ArrayList<>(),new ArrayList<>());
        assertEquals("card", card.getTitle());
    }

    @Test
    void testEquals() {
        Card card1 = new Card("card",new ArrayList<>(),new ArrayList<>());
        Card card2 = new Card("card",new ArrayList<>(),new ArrayList<>());
        assertEquals(card1, card2);
    }

    @Test
    void testNotEquals() {
        Card card1 = new Card("card1",new ArrayList<>(),new ArrayList<>());
        Card card2 = new Card("card2",new ArrayList<>(),new ArrayList<>());
        assertNotEquals(card1, card2);
    }

    @Test
    void testEqualsHashCode() {
        Card card1 = new Card("card",new ArrayList<>(),new ArrayList<>());
        Card card2 = new Card("card",new ArrayList<>(),new ArrayList<>());
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void testNotEqualsHashCode() {
        Card card1 = new Card("card1",new ArrayList<>(),new ArrayList<>());
        Card card2 = new Card("card2",new ArrayList<>(),new ArrayList<>());
        assertNotEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    public void testHasDescription(){
        Card card = new Card();
        String desc = "asdfgh";
        card.setDescription(desc);

        assertTrue(card.hasDescription());
    }

    @Test
    public void testGetDesc(){
        Card card = new Card();
        String desc = "asdfgh";
        card.setDescription(desc);

        assertEquals(desc,card.getDescription());
    }

    @Test
    public void testSetDesc(){
        Card card = new Card();
        String desc = "asdfgh";
        card.setDescription(desc);

        assertEquals(desc,card.getDescription());
    }

}
