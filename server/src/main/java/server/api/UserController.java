package server.api;

import commons.Board;
import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.generators.SequenceGenerator;
import server.services.BoardService;
import server.services.UserService;

//TODO join board
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private final BoardService boardService;


    public UserController(UserService userService, BoardService boardService) {
        this.userService = userService;
        this.boardService = boardService;
    }

    @PostMapping("/{name}")
    public ResponseEntity<User> createUser(@PathVariable("name") String name) {

        return ResponseEntity.ok(userService.createUser(name));
    }

    @PutMapping("/{name}/board/{key}")
    public ResponseEntity<User> addBoardToUser(@PathVariable("name") String name,
                                               @PathVariable("key") String key){
        Board board = boardService.getBoardByKey(key);

        User user = userService.getUser(name);
        user.getJoinedBoards().add(board);
        user = userService.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{name}")
    public ResponseEntity<User> getUser(@PathVariable("name") String name){
        return ResponseEntity.ok(userService.getUser(name));
    }

    /**
     * Returns a response string with an admin password
     *
     * @return      Response string with the admin password
     * @author      Kirill Zhankov
     */
    @GetMapping("/admin")
    public ResponseEntity<String> getPassword(){
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        String pass = sequenceGenerator.generate();
        System.out.println("Admin password: "+pass);
        return ResponseEntity.ok(pass);
    }
}