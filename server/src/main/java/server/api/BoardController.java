package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardRepository br;

    public BoardController(BoardRepository br) {
        this.br = br;
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

    @GetMapping("/board/")
    public ResponseEntity<Board> getBoard() {
        Board saved = null;
        if (this.br.findAll().isEmpty()) {
            Board board = new Board(new ArrayList<>(),new ArrayList<>());
            return ResponseEntity.ok(br.saveAndFlush(board));
        }
        saved = br.findAll().stream().findFirst().get();
        return ResponseEntity.ok(saved);
    }


}
