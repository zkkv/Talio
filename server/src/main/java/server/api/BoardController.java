package server.api;

import commons.Board;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;
import server.services.CardListService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;
    private final CardListService cardListService;

    public BoardController(BoardService boardService, CardListService cardListService) {
        this.boardService = boardService;
        this.cardListService = cardListService;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Board>> getAll() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    @GetMapping("/{key}")
    public ResponseEntity<Board> getById(@PathVariable("key") long key) {
        if (key < 0 || !boardService.exists(key)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boardService.getBoard(key));
    }

    @GetMapping("/create")
    public ResponseEntity<Board> getOrCreateBoard() {
        if(boardService.getAllBoards().isEmpty()){
            Board board =new Board(new ArrayList<>());
            board.getCardLists().add(new CardList(new ArrayList<>(), "TO DO"));
            board.getCardLists().add(new CardList(new ArrayList<>(), "DOING"));
            board.getCardLists().add(new CardList(new ArrayList<>(), "DONE"));
            Board board1 = boardService.save(board);
            return ResponseEntity.ok(board1);
        }
        return ResponseEntity.ok(boardService.getAllBoards().get(0));
    }

    @PutMapping("/add-card-list")
    public ResponseEntity<CardList> addCardList(@RequestBody CardList cardList){
        var saved = cardListService.save(cardList);//We first save the card list to update the id
        Board board1 = boardService.getAllBoards().get(0);
        board1.getCardLists().add(cardList);
        boardService.save(board1);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/remove-card-list/{id}")
    public ResponseEntity<CardList> removeCardList(@PathVariable(name = "id") long id) {
        if(id<0||!cardListService.exists(id)){
            return ResponseEntity.notFound().build();
        }
        Board board = boardService.getAllBoards().get(0);
        CardList list = cardListService.getCardList(id);
        board.getCardLists().remove(list);
        boardService.save(board);
        cardListService.delete(id);
        return ResponseEntity.ok(list);
    }
}
