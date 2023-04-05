package server.api;

import commons.Board;
import commons.Card;
import commons.SubTask;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;
import server.services.CardService;
import server.services.SubTaskService;
import server.services.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/card")
public class CardController {
    private final CardService cardService;
    private final SubTaskService subTaskService;
    private final TagService tagService;
    private final BoardService boardService;

    private final SimpMessagingTemplate simpMessagingTemplate;


    public CardController(CardService cardService, BoardService boardService,
                          SimpMessagingTemplate simpMessagingTemplate,
                          SubTaskService subTaskService,
                          TagService tagService) {
        this.cardService = cardService;
        this.boardService = boardService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.subTaskService = subTaskService;
        this.tagService = tagService;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Card>> getAll() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (id < 0 || !cardService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardService.getCard(id));
    }

    @PutMapping("/update-title/{id}/board/{boardId}")
    public ResponseEntity<Card> updateTitle(@RequestBody String title, @PathVariable("id") long id,
                                            @PathVariable("boardId") long boardId) {
        Card card = cardService.getCard(id);
        card.setTitle(title);
        card = cardService.save(card);
        Board board = boardService.getBoard(boardId);
        simpMessagingTemplate.convertAndSend("/topic/board/" + boardId, board);
        return ResponseEntity.ok(card);
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<SubTask> addSubTask(@RequestBody SubTask subTask,
                                              @PathVariable("id") long id) {
        var saved = subTaskService.save(subTask);
        Card card = cardService.getCard(id);
        card.getTasks().add(saved);
        subTaskService.save(subTask);
        cardService.save(card);
        return ResponseEntity.ok(subTask);
    }

    @DeleteMapping("/remove-card/{cardId}/remove-task/{taskId}")
    public ResponseEntity<SubTask> removeSubTask(@PathVariable(name = "cardId") long cardId,
                                                 @PathVariable(name = "taskId") long taskId) {
        if (cardId < 0 || !cardService.exists(cardId) ||
                taskId < 0 || !subTaskService.exists(taskId)) {
            return ResponseEntity.notFound().build();
        }
        Card card = cardService.getCard(cardId);
        SubTask subTask = subTaskService.getSubTask(taskId);
        card.getTasks().remove(subTask);
        cardService.save(card);
        subTaskService.delete(taskId);
        return ResponseEntity.ok(subTask);
    }
    @PutMapping("/update-description/{id}/board/{boardId}")
    public ResponseEntity<Card> updateDescription(@RequestBody String description,
                                                  @PathVariable("id") long id,
                                                  @PathVariable("boardId") long boardId) {
        Card card = cardService.getCard(id);
        card.setDescription(description);
        card = cardService.save(card);
        Board board = boardService.getBoard((boardId));
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId, board);
        return ResponseEntity.ok(card);
    }

    @PostMapping("/{id}/add-tag")
    public ResponseEntity<Tag> addTag(@RequestBody Tag tag, @PathVariable("id") long cardId) {

        tag = tagService.save(tag);
        Card card = cardService.getCard(cardId);
        card.getTags().add(tag);
        card = cardService.save(card);

        return ResponseEntity.ok(tag);
    }

    @DeleteMapping("/{id}/remove-tag/{tagId}")
    public ResponseEntity<Tag> removeTag(@PathVariable("tagId") long tagId,
                                         @PathVariable("id") long cardId){
        Tag tag = tagService.getTag(tagId);
        tag = tagService.save(tag);
        Card card = cardService.getCard(cardId);

        card.getTags().remove(tag);

        card = cardService.save(card);
        return ResponseEntity.ok(tag);
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<List<Tag>> getAllTags(@PathVariable("id") long cardId) {
        List<Tag> tags = cardService.getCard(cardId).getTags();
        return ResponseEntity.ok(tags);
    }

    @PutMapping("/update-subTasks/{id}/board/{boardId}")
    public ResponseEntity<Card> updateSubTasks(@RequestBody List<SubTask> subtasks,
                                               @PathVariable("id") long id,
                                               @PathVariable("boardId") long boardId){
        Card card = cardService.getCard(id);
        card.setTasks(subtasks);
        card = cardService.save(card);
        Board board = boardService.getBoard(boardId);
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId,board);
        return ResponseEntity.ok(card);
    }
}