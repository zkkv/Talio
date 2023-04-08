package server.api;

import commons.Card;
import commons.SubTask;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;
import server.services.CardService;
import server.services.SubTaskService;

import java.util.List;

@RestController
@RequestMapping("/api/subTask")
public class SubTaskController {
    private final CardService cardService;
    private final SubTaskService subTaskService;

    private final BoardService boardService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public SubTaskController(CardService cardService, SubTaskService subTaskService,
                             BoardService boardService,
                             SimpMessagingTemplate simpMessagingTemplate) {
        this.cardService = cardService;
        this.subTaskService = subTaskService;
        this.boardService = boardService;
        this.simpMessagingTemplate = simpMessagingTemplate;
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


    @PutMapping("/update-titleTask/{id}/card/{cardId}/board/{boardId}")
    public ResponseEntity<SubTask> updateTitleSubTask(@RequestBody String title,
                                                      @PathVariable("id") long id,
                                                      @PathVariable("cardId") long cardId,
                                                      @PathVariable("boardId") long boardId) {
        SubTask subTask = subTaskService.getSubTask(id);
        subTask.setName(title);
        Card card = cardService.getCard(cardId);
        card = cardService.save(card);
        simpMessagingTemplate.convertAndSend("/topic/board/"+
            boardId,boardService.getBoard(boardId));
        return ResponseEntity.ok(subTaskService.save(subTask));
    }

    @PutMapping("/update-checkbox-Task/{id}/card/{cardId}/board/{boardId}")
    public ResponseEntity<SubTask> updateIsChecked(@RequestBody boolean isChecked,
                                                   @PathVariable("id") long id,
                                                   @PathVariable("cardId") long cardId,
                                                   @PathVariable("boardId") long boardId) {
        SubTask subTask = subTaskService.getSubTask(id);
        subTask.setChecked(isChecked);
        subTask = subTaskService.save(subTask);
        Card card = cardService.getCard(cardId);
        card = cardService.save(card);
        simpMessagingTemplate.convertAndSend("/topic/board/"+
            boardId,boardService.getBoard(boardId));
        return ResponseEntity.ok(subTask);
    }
}