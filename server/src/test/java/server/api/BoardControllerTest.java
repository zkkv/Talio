package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class BoardControllerTest {

    private TestBoardRepository repo;

    private BoardController sut;

    @BeforeEach
    public void setup(){
        repo = new TestBoardRepository();
        sut = new BoardController(repo);
    }


    @Test
    public void databaseIsUsed() {
        sut.getBoard();
        repo.calledMethods.contains("save");
    }

}
