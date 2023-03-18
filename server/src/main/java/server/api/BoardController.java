package server.api;

import commons.Board;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardListRepository;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardRepository br;
    private final CardListRepository cr;

    public BoardController(BoardRepository br, CardListRepository cr) {
        this.br = br;
        this.cr = cr;
    }

    @GetMapping(path = {"", "/"})
    public List<Board> getAll() {
        return br.findAll();
    }

    @GetMapping("/{key}")
    public ResponseEntity<Board> getById(@PathVariable("key") long key) {
        if (key < 0 || !br.existsById(key)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(br.findById(key).get());
    }

    private static boolean isNull(Object s) {
        return s == null;
    }

    @GetMapping("/create")
    public ResponseEntity<Board> getOrCreateBoard() {
        if(br.findAll().isEmpty()){
            Board board =new Board(new ArrayList<>());
            board.cardLists.add(new CardList(new ArrayList<>(), "TO DO"));
            board.cardLists.add(new CardList(new ArrayList<>(), "DOING"));
            board.cardLists.add(new CardList(new ArrayList<>(), "DONE"));
            Board board1 = br.save(board);
            return ResponseEntity.ok(board1);
        }
        return ResponseEntity.ok(br.findAll().get(0));
    }

    @PutMapping("/add-card-List")
    public ResponseEntity<CardList> addCardList(@RequestBody CardList cardList){
        var saved = cr.save(cardList);//We first save the card list to update the id
        Board board1 = br.findAll().get(0);
        board1.cardLists.add(cardList);
        br.save(board1);
        return ResponseEntity.ok(saved);
    }
}
