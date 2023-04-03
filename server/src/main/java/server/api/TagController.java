package server.api;

import commons.Board;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import server.services.BoardService;
import server.services.TagService;

@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagService tagService;
    private final BoardService boardService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public TagController(TagService tagService, BoardService boardService,
                         SimpMessagingTemplate simpMessagingTemplate) {
        this.tagService = tagService;
        this.boardService = boardService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /**
     * A method to change the name of the tag
     *
     * @param id the id of the tag which name is updated
     * @param title the new name
     * @param boardId the baord in which the tag is switched
     * @return a response with the updated tag
     */
    @PutMapping("/update-name/{id}/board/{boardId}")
    public ResponseEntity<Tag> updateTitle(@RequestBody String title,
                                                @PathVariable("id") long id,
                                                @PathVariable("boardId") long boardId){
        Tag tag = tagService.getTag(id);
        tag.setTitle(title);
        tag = tagService.save(tag);
        Board board = boardService.getBoard(boardId);
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId+"/tag",board);
        return ResponseEntity.ok(tag);
    }

    /**
     * A method to remove a tag
     *
     * @param tagId the id of the tag which is removed
     * @param boardId the board from which the tag is removed
     * @return a response with the removed tag
     */
    @DeleteMapping("/remove/{tagId}/board/{boardId}")
    public ResponseEntity<Tag> removeTag(@PathVariable("boardId") long boardId,
                                         @PathVariable("tagId") long tagId){
        Tag tag = tagService.getTag(tagId);
        Board board = boardService.getBoard(boardId);

        board.getTags().remove(tag);

        board = boardService.save(board);
        tagService.removeTag(tagId);

        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId+"/tag",board);
        return ResponseEntity.ok(tag);
    }

}
