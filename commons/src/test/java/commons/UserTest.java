package commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class UserTest {
    @Test
    public void checkConstructor() {
        var user = new User("asd",new ArrayList<>());
        assertEquals("asd",user.getUserName());
    }

    @Test
    public void equalsHashCode() {
        var user1 = new User("asd",new ArrayList<>());
        var user2 = new User("asd",new ArrayList<>());
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var user1 = new User("asd",new ArrayList<>());
        var user2 = new User("asdd",new ArrayList<>());
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void hasToString() {
        var user = new User("asd",new ArrayList<>()).toString();
        assertTrue(user.contains(User.class.getSimpleName()));
        assertTrue(user.contains("\n"));
        assertTrue(user.contains("asd"));
    }

    @Test
    public void testEquals(){
        var user1 = new User("asd",new ArrayList<>());
        var user2 = new User("asd",new ArrayList<>());

        assertEquals(user1,user2);
    }

    @Test
    public void testSetBoards(){
        var user1 = new User("asd",new ArrayList<>());
        List<Board> boards = new ArrayList<>();
        user1.setJoinedBoards(boards);
        assertEquals(boards,user1.getJoinedBoards());
    }

    @Test
    public void testSetId(){
        var user = new User();
        user.setId(1L);

        assertEquals(1L,user.getId());
    }
}