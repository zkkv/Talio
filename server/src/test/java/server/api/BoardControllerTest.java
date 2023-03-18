package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class BoardControllerTest {

    private TestBoardRepository repo;
    private TestCardListRepository cr;

    private BoardController sut;

    @BeforeEach
    public void setup(){
        repo = new TestBoardRepository();
        cr = new TestCardListRepository();
        sut = new BoardController(repo, cr);
    }


    @Test
    public void databaseIsUsed() {
        sut.getOrCreateBoard();
        repo.calledMethods.contains("save");
    }
}
