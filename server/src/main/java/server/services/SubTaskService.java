package server.services;

import commons.SubTask;
import org.springframework.stereotype.Service;
import server.database.SubTaskRepository;

import java.util.List;

@Service
public class SubTaskService {
    private final SubTaskRepository subTaskRepository;

    public SubTaskService(SubTaskRepository subTaskRepository) {
        this.subTaskRepository = subTaskRepository;
    }

    public SubTask getSubTask(long subTaskId) {
        return subTaskRepository.findById(subTaskId).get();
    }

    public List<SubTask> getAllSubTasks() {
        return subTaskRepository.findAll();
    }

    public boolean exists(long subTaskId) {
        return subTaskRepository.existsById(subTaskId);
    }

    public void delete(long subTaskId) {
        subTaskRepository.deleteById(subTaskId);
    }

    public SubTask save(SubTask subTask) {
        return subTaskRepository.save(subTask);
    }
}
