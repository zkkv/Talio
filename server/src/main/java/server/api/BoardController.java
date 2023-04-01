package server.api;

import commons.Board;
import commons.CardList;
import commons.Tag;
import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.generators.SequenceGenerator;
import server.services.BoardService;
import server.services.CardListService;
import server.services.TagService;
import server.services.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CardListService cardListService;

    private final UserService userService;
    private final TagService tagService;

    public BoardController(BoardService boardService,
                           CardListService cardListService,
                           UserService userService,
                           SimpMessagingTemplate simpMessagingTemplate,
                           TagService tagService) {
        this.boardService = boardService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.cardListService = cardListService;
        this.userService = userService;
        this.tagService = tagService;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Board>> getAll() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Board>> getUserBoards(@PathVariable("username") String userName){
        List<Board> boards = userService.getUserBoards(userName);
        return ResponseEntity.ok(boards);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !boardService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boardService.getBoard(id));
    }

    @PostMapping("/create/{username}")
    public ResponseEntity<Board> createBoard(@RequestBody String name,
                                             @PathVariable("username") String userName) {
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        Board board = new Board(new ArrayList<>(), name);
        board.getCardLists().add(new CardList(new ArrayList<>(), "TO DO"));
        board.getCardLists().add(new CardList(new ArrayList<>(), "DOING"));
        board.getCardLists().add(new CardList(new ArrayList<>(), "DONE"));
        board.setKey(sequenceGenerator.generate());
        board = boardService.save(board);
        User user = userService.getUser(userName);
        user.getJoinedBoards().add(board);
        userService.save(user);
        return ResponseEntity.ok(board);
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<Board> getByKey(@PathVariable("key") String boardKey){
        Board board = boardService.getBoardByKey(boardKey);

        return ResponseEntity.ok(board);
    }

    @PostMapping("/{id}/add-card-list")
    public ResponseEntity<CardList> addCardList(@RequestBody CardList cardList,
                                                @PathVariable("id") long boardId) {
        var saved = cardListService.save(cardList);//We first save the card list to update the id
        Board board = boardService.getBoard(boardId);
        board.getCardLists().add(cardList);
        board = boardService.save(board);
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
        board = boardService.save(board);
        cardListService.delete(cardListId);
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId,board);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/update-title/{id}")
    public ResponseEntity<Board> updateTitle(@PathVariable("id") long boardId,
                                             @RequestBody String title){
        Board board = boardService.getBoard(boardId);
        board.setTitle(title);
        board = boardService.save(board);
        simpMessagingTemplate.convertAndSend("/topic/board/"+board.getId(),board);
        return ResponseEntity.ok(board);
    }


    @DeleteMapping("/remove-board/{id}")
    public ResponseEntity<Board> removeBoard(@PathVariable("id") long boardId){
        List<User> users = userService.getAllUsers();
        Board board = boardService.getBoard(boardId);
        users.stream().filter(user -> user.getJoinedBoards().contains(board))
            .forEach(user -> {
                user.getJoinedBoards().remove(board);
                userService.save(user);
            });

        boardService.delete(boardId);
        simpMessagingTemplate.convertAndSend("/topic/board/remove",board);
        return ResponseEntity.ok(board);
    }

    /**
     * Adds tag to the board with specified {@code boardId}.
     *
     * @param tag       Tag to be added
     * @param boardId   boardId
     * @return          ResponseEntity containing the added tag
     * @author          Kirill Zhankov
     */
    @PostMapping("/{id}/add-tag")
    public ResponseEntity<Tag> addTag(@RequestBody Tag tag, @PathVariable("id") long boardId) {
        Tag saved = tagService.save(tag);
        Board board = boardService.getBoard(boardId);

        var tags = board.getTags();
        if (tag == null) {
            tags.add(saved);
            board.setTags(tags);
        }
        boardService.save(board);

        simpMessagingTemplate.convertAndSend("/topic/board/" + boardId, board);
        return ResponseEntity.ok(saved);
    }
}