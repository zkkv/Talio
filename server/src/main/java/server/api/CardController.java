package server.api;

import java.util.List;

import commons.Board;
import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import server.services.BoardService;
import server.services.CardService;

@RestController
@RequestMapping("/api/card")
public class CardController {
    private final CardService cardService;

    private final BoardService boardService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public CardController(CardService cardService, BoardService boardService, SimpMessagingTemplate simpMessagingTemplate) {
        this.cardService = cardService;
        this.boardService = boardService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping(path = { "", "/" })
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
    public ResponseEntity<Card> updateTitle(@RequestBody String title,@PathVariable("id") long id,
                                            @PathVariable("boardId") long boardId){
        Card card = cardService.getCard(id);
        card.setTitle(title);
        card = cardService.save(card);
        Board board = boardService.getBoard(boardId);
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId,board);
        return ResponseEntity.ok(card);
    }
}