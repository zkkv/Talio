package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubTaskTest {

    @Test
    public void checkConstructor() {
        var subTask = new SubTask(true,"asd");
        assertEquals("asd",subTask.getName());
    }

    @Test
    public void equalsHashCode() {
        var subTask1 = new SubTask(true,"asd");
        var subTask2 = new SubTask(true,"asd");
        assertEquals(subTask1, subTask2);
        assertEquals(subTask1.hashCode(), subTask2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var subTask1 = new SubTask(true,"asdd");
        var subTask2 = new SubTask(true,"asd");
        assertNotEquals(subTask1, subTask2);
        assertNotEquals(subTask1.hashCode(), subTask2.hashCode());
    }

    @Test
    public void hasToString() {
        var subTask = new SubTask(true,"asd").toString();
        assertTrue(subTask.contains(SubTask.class.getSimpleName()));
        assertTrue(subTask.contains("\n"));
        assertTrue(subTask.contains("asd"));
    }

    @Test
    public void testEquals(){
        var subTask1 = new SubTask(false,"asd");
        var subTask2 = new SubTask(false,"asd");

        assertEquals(subTask1,subTask2);
    }

    @Test
    public void testGetId(){
        var subTask = new SubTask();
        long id = 1L;
        subTask.setId(id);

        assertEquals(id,subTask.getId());
    }
}
