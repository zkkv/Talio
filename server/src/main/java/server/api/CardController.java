package server.api;

import java.util.List;

import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import server.services.CardService;

@RestController
@RequestMapping("/api/card")
public class CardController {
    private final CardService cardService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public CardController(CardService cardService, SimpMessagingTemplate simpMessagingTemplate) {
        this.cardService = cardService;
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

    @PutMapping("/update-title/{id}")
    public ResponseEntity<Card> updateTitle(@RequestBody String title,@PathVariable("id") long id){
        Card card = cardService.getCard(id);
        card.setTitle(title);
        card = cardService.save(card);
        simpMessagingTemplate.convertAndSend("/topic/board",card);
        return ResponseEntity.ok(card);
    }
}
