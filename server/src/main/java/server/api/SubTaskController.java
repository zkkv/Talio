package server.api;

import commons.SubTask;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.CardService;
import server.services.SubTaskService;

import java.util.List;

@RestController
@RequestMapping("/api/subTask")
public class SubTaskController {
    private final CardService cardService;
    private final SubTaskService subTaskService;

    public SubTaskController(CardService cardService, SubTaskService subTaskService) {
        this.cardService = cardService;
        this.subTaskService = subTaskService;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<SubTask>> getAll() {
        return ResponseEntity.ok(subTaskService.getAllSubTasks());
    }


    @GetMapping("/{id}")
    public ResponseEntity<SubTask> getById(@PathVariable("id") long id) {
        if (id < 0 || !subTaskService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(subTaskService.getSubTask(id));
    }


    @PutMapping("/update-titleTask/{id}")
    public ResponseEntity<SubTask> updateTitleSubTask(@RequestBody String title,
                                                      @PathVariable("id") long id) {
        SubTask subTask = subTaskService.getSubTask(id);
        subTask.setName(title);
        return ResponseEntity.ok(subTaskService.save(subTask));
    }

    @PutMapping("/update-checkbox-Task/{id}")
    public ResponseEntity<SubTask> updateIsChecked(@RequestBody boolean isChecked,
                                                   @PathVariable("id") long id) {
        SubTask subTask = subTaskService.getSubTask(id);
        subTask.setChecked(isChecked);
        return ResponseEntity.ok(subTaskService.save(subTask));
    }
}
