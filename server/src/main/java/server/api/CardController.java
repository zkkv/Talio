package server.api;

import java.util.List;

import commons.Card;
import commons.CardList;
import commons.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.CardRepository;

@RestController
@RequestMapping("/api/card")
public class CardController {
    private final CardRepository repo;

    public CardController(CardRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Card> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Card> add(@RequestBody Card card) {
        if (card.title == null){
            return ResponseEntity.badRequest().build();
        }

        Card saved = repo.save(card);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update-title")
    public ResponseEntity<Card> updateTitle(@RequestBody Pair<Card,String> request){
        Card card = request.getLeft();
        card.title = request.getRight();
        return ResponseEntity.ok(repo.save(card));
    }
}
