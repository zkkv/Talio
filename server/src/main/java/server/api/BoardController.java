package server.api;

import commons.Board;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;
import server.services.CardListService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CardListService cardListService;


    public BoardController(BoardService boardService, CardListService cardListService,
                           SimpMessagingTemplate simpMessagingTemplate) {
        this.boardService = boardService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.cardListService = cardListService;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Board>> getAll() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !boardService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boardService.getBoard(id));
    }

    //TODO create new board issue
    @PostMapping("/create")
    public ResponseEntity<Board> createBoard(@RequestBody String name) {
        Board board = new Board(new ArrayList<>(), name);
        board.getCardLists().add(new CardList(new ArrayList<>(), "TO DO"));
        board.getCardLists().add(new CardList(new ArrayList<>(), "DOING"));
        board.getCardLists().add(new CardList(new ArrayList<>(), "DONE"));
        board = boardService.save(board);
        return ResponseEntity.ok(board);
    }

    @PostMapping("/{id}/add-card-list")
    public ResponseEntity<CardList> addCardList(@RequestBody CardList cardList,
                                                @PathVariable("id") long boardId) {
        var saved = cardListService.save(cardList);//We first save the card list to update the id
        Board board = boardService.getBoard(boardId);
        board.getCardLists().add(cardList);
        boardService.save(board);
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId, board);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}/card-lists")
    public ResponseEntity<List<CardList>> getAllCardLists(@PathVariable("id") long boardId) {
        Board board = boardService.getBoard(boardId);
        return ResponseEntity.ok(board.getCardLists());
    }

    @DeleteMapping("/remove-card-list/{id}/board/{boardId}")
    public ResponseEntity<CardList> removeCardList(@PathVariable(name = "id") long cardListId,
                                                   @PathVariable("boardId") long boardId) {
        if (cardListId < 0 || !cardListService.exists(cardListId)) {
            return ResponseEntity.notFound().build();
        }
        Board board = boardService.getBoard(boardId);
        CardList list = cardListService.getCardList(cardListId);
        board.getCardLists().remove(list);
        boardService.save(board);
        cardListService.delete(cardListId);
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId,list);
        return ResponseEntity.ok(list);
    }
}
