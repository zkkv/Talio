package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    Tag tag1;
    Tag tag2;
    List<Card> cards;

    @BeforeEach
    void setUp() {
        Card c1 = new Card("card1",new ArrayList<>(),new ArrayList<>());
        Card c2 = new Card("card2",new ArrayList<>(),new ArrayList<>());

        cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
    }

    @Test
    void testEqualsBothNull() {
        tag1 = new Tag();
        tag2 = new Tag();
        assertEquals(tag1, tag2);
    }

    @Test
    void testEqualsBothEqual() {
        tag1 = new Tag("name", 1, 2, 3, cards);
        tag2 = new Tag("name", 1, 2, 3, cards);
        assertEquals(tag1, tag2);
    }

    @Test
    void testNotEquals() {
        tag1 = new Tag("name1", 1, 2, 3, cards);
        tag2 = new Tag("name2", 1, 2, 3, cards);
        assertNotEquals(tag1, tag2);
    }

    @Test
    void testHashCode() {
        tag1 = new Tag("name", 1, 2, 3, cards);
        tag2 = new Tag("name", 1, 2, 3, cards);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }


    @Test
    void setId() {
        tag1 = new Tag("name", 1, 2, 3, cards);
        tag1.setId(42L);
        assertEquals(42L, tag1.getId());
    }

    @Test
    void getRed() {
        tag1 = new Tag("name", 1, 2, 3, cards);
        assertEquals(1, tag1.getRed());
    }

    @Test
    void getGreen() {
        tag1 = new Tag("name", 1, 2, 3, cards);
        assertEquals(2, tag1.getGreen());
    }

    @Test
    void getBlue() {
        tag1 = new Tag("name", 1, 2, 3, cards);
        assertEquals(3, tag1.getBlue());
    }

    @Test
    void getTitle() {
        tag1 = new Tag("name", 1, 2, 3, cards);
        assertEquals("name", tag1.getTitle());
    }

    @Test
    public void hasToString() {
        var tag = new Tag("asd",123,123,123,new ArrayList<>()).toString();
        assertTrue(tag.contains(Tag.class.getSimpleName()));
        assertTrue(tag.contains("\n"));
        assertTrue(tag.contains("asd"));
    }
}
