package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;
import server.services.CardListService;
import server.services.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/card-lists")
public class CardListController {

    private final CardListService cardListService;
    private final CardService cardService;

    private final BoardService boardService;

    private final SimpMessagingTemplate simpMessagingTemplate;
    public CardListController(CardListService cardListService,
                              CardService cardService,
                              BoardService boardService, SimpMessagingTemplate simpMessagingTemplate) {
        this.cardListService = cardListService;
        this.cardService = cardService;
        this.boardService = boardService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<CardList>> getAll() {
        return ResponseEntity.ok(cardListService.getAllCardLists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        if (id < 0 || !cardListService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardListService.getCardList(id));
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<List<Card>> getCards(@PathVariable("id") long id) {
        return ResponseEntity.ok(cardListService.getCardList(id).getCards());
    }

    @PostMapping("/{id}/cards/board/{boardId}")
    public ResponseEntity<Card> addCard(@RequestBody Card card,
                                        @PathVariable("id") long id,
                                        @PathVariable("boardId") long boardId) {
        var saved = cardService.save(card);
        CardList cardList = cardListService.getCardList(id);

        cardList.getCards().add(saved);

        cardListService.save(cardList);
        Board board = boardService.getBoard(boardId);

        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId,board);
        return ResponseEntity.ok(card);
    }

    @PostMapping("/{id}/cards/{index}/board/{boardId}")
    public ResponseEntity<Card> addCardAtIndex(@RequestBody Card card,
                                               @PathVariable("id") long id,
                                               @PathVariable("index") int index,
                                               @PathVariable("boardId") long boardId) {
        var saved = cardService.save(card);
        CardList cardList = cardListService.getCardList(id);
        cardList.getCards().add(index, saved);
        cardListService.save(cardList);
        Board board = boardService.getBoard(boardId);
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId,board);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update-title/{id}/board/{boardId}")
    public ResponseEntity<CardList> updateTitle(@RequestBody String title,
                                                @PathVariable("id") long id,
                                                @PathVariable("boardId") long boardId){
        CardList cardList = cardListService.getCardList(id);
        cardList.setTitle(title);
        cardList= cardListService.save(cardList);
        Board board = boardService.getBoard(boardId);
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId,board);
        return ResponseEntity.ok(cardList);
    }

    @DeleteMapping("/remove-card-list/{listId}/remove-card/{cardId}/board/{boardId}")
    public ResponseEntity<Card> removeCard(@PathVariable(name = "listId") long listId,
                                           @PathVariable(name = "cardId") long cardId,
                                           @PathVariable("boardId") long boardId) {
        if(listId < 0 || !cardListService.exists(listId)||
            cardId < 0 || !cardService.exists(cardId)){
            return ResponseEntity.notFound().build();
        }
        CardList list = cardListService.getCardList(listId);
        Card card = cardService.getCard(cardId);
        list.getCards().remove(card);
        cardListService.save(list);
        cardService.delete(cardId);
        Board board = boardService.getBoard(boardId);
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId,board);
        return ResponseEntity.ok(card);
    }
}
