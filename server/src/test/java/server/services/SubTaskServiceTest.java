package server.services;

import commons.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.SubTaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class SubTaskServiceTest {
    @Mock
    private SubTaskRepository subTaskRepository;

    @InjectMocks
    private SubTaskService subTaskService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSubtask(){
        SubTask subTask = new SubTask();
        subTask.setId(1L);

        when(subTaskRepository.findById(1L)).thenReturn(Optional.of(subTask));

        SubTask actual = subTaskService.getSubTask(1L);

        assertEquals(actual,subTask);
    }

    @Test
    public void testGetAllSubtasks(){
        List<SubTask> subTasks = new ArrayList<>();
        SubTask subTask1 = new SubTask();
        subTask1.setId(1L);

        SubTask subTask2 = new SubTask();
        subTask2.setId(2L);
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        when(subTaskRepository.findAll()).thenReturn(subTasks);

        List<SubTask> actual = subTaskService.getAllSubTasks();

        assertEquals(subTasks,actual);
    }

    @Test
    public void testExists(){
        SubTask subTask = new SubTask();
        subTask.setId(1L);

        when(subTaskRepository.existsById(1L)).thenReturn(true);

        boolean actual = subTaskService.exists(1L);

        assertTrue(actual);
    }

    @Test
    public void testDelete() {
        doNothing().when(subTaskRepository).deleteById(anyLong());

        subTaskService.delete(1L);

        verify(subTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSave() {
        SubTask subTask = new SubTask();

        SubTask subTaskExpected = new SubTask();
        subTaskExpected.setId(1L);

        when(subTaskRepository.save(subTask)).thenReturn(subTaskExpected);

        SubTask actual = subTaskService.save(subTask);

        assertEquals(subTaskExpected, actual);
    }
}
