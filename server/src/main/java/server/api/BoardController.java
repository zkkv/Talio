package server.api;

import commons.Board;
import commons.CardList;
import commons.Tag;
import commons.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.generators.SequenceGenerator;
import server.services.BoardService;
import server.services.CardListService;
import server.services.TagService;
import server.services.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CardListService cardListService;

    private final UserService userService;
    private final TagService tagService;

    // Listeners (for long-polling) get populated when a client sends a request and waits.
    // Once the Tag is found or the server times out, the consumer gets removed from the map.
    private Map<Object, Consumer<Tag>> listeners = new HashMap<>();

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
     * Adds tag to the board with specified {@code boardId}. Notifies all listeners subscribed
     * for the updates about the added tag.
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

        // [1] This sets the ResponseEntity in getTagUpdates() to OK with tag as a body. See [2].
        listeners.forEach((key, listener) -> listener.accept(tag));

        board.getTags().add(saved);
        board = boardService.save(board);
        simpMessagingTemplate.convertAndSend("/topic/board/" + boardId, board);
        return ResponseEntity.ok(saved);
    }

    /**
     * Gets all tags of the board with {@code boardId} from its repository.
     *
     * @param boardId   id of the board from which tags need to be retrieved
     * @return          ResponseEntity containing a list of all tags of the board
     * @author          Kirill Zhankov
     */
    @GetMapping("/{id}/tags")
    public ResponseEntity<List<Tag>> getAllTags(@PathVariable("id") long boardId) {
        Board board = boardService.getBoard(boardId);
        return ResponseEntity.ok(board.getTags());
    }


    /**
     * Returns response with the added tag or, in case of a timeout, 204 NO CONTENT.
     * Populates listeners with responses which are set to 200 OK once {@link #addTag(Tag, long)}
     * is called.
     *
     * @param boardId   board to get updates from
     * @return          200 OK response with the tag or 204 NO CONTENT in case of a timeout
     * @author          Kirill Zhankov
     */
    @GetMapping("/{id}/tags/updates")
    public DeferredResult<ResponseEntity<Tag>> getTagUpdates(@PathVariable("id") long boardId) {
        long timeout = 5000L;
        // No content (204) response will be sent back in case of a timeout.
        var timeoutResult = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        // DeferredResult makes the method asynchronous (i.e. non-blocking).
        var res = new DeferredResult<ResponseEntity<Tag>>(timeout, timeoutResult);

        // The key is made to be an Object to avoid duplicating keys (collisions). That is because
        // two objects of class Object are equal iff they refer to the same memory address.
        var key = new Object();
        // [2] The lambda expression is executed when the tag gets added in addTag(). See [1].
        listeners.put(key, tag -> res.setResult(ResponseEntity.ok(tag)));

        // Once we've got the response, we can remove the listener from the map.
        res.onCompletion(() -> listeners.remove(key));
        return res;
    }
}