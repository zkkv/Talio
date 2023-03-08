package server.api;

import commons.Card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class CardControllerTest {

    private TestCardRepository repo;

    private CardController sut;

    @BeforeEach
    public void setup(){
        repo = new TestCardRepository();
        sut = new CardController(repo);
    }


    @Test
    public void databaseIsUsed() {
        sut.add(getCard("title"));
        repo.calledMethods.contains("save");
    }

    private static Card getCard(String title) {
        return new Card(title);
    }
}
