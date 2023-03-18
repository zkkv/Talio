package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class BoardControllerTest {

    private TestBoardRepository repo;

    private BoardController sut;

    @BeforeEach
    public void setup(){
        repo = new TestBoardRepository();
//        sut = new BoardController(repo, cr);
    }


    @Test
    public void databaseIsUsed() {
        sut.getOrCreateBoard();
        repo.calledMethods.contains("save");
    }
}
