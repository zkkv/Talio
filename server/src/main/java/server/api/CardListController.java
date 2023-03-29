/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api;

import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.CardListService;
import server.services.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/card-lists")
public class CardListController {

    private final CardListService cardListService;
    private final CardService cardService;

    private final SimpMessagingTemplate simpMessagingTemplate;
    public CardListController(CardListService cardListService,
                              CardService cardService,
                              SimpMessagingTemplate simpMessagingTemplate) {
        this.cardListService = cardListService;
        this.cardService = cardService;
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

    @PostMapping("/{id}/cards")
    public ResponseEntity<Card> addCard(@RequestBody Card card, @PathVariable("id") long id) {
        var saved = cardService.save(card);
        CardList cardList = cardListService.getCardList(id);

        cardList.getCards().add(saved);

        cardListService.save(cardList);
        simpMessagingTemplate.convertAndSend("/topic/board",card);
        return ResponseEntity.ok(card);
    }

    @PostMapping("/{id}/cards/{index}")
    public ResponseEntity<Card> addCardAtIndex(@RequestBody Card card,
                                               @PathVariable("id") long id,
                                               @PathVariable("index") int index) {
        var saved = cardService.save(card);
        CardList cardList = cardListService.getCardList(id);
        cardList.getCards().add(index, saved);
        cardListService.save(cardList);
        simpMessagingTemplate.convertAndSend("/topic/board",saved);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update-title/{id}")
    public ResponseEntity<CardList> updateTitle(@RequestBody String title,
                                                @PathVariable("id") long id){
        CardList cardList = cardListService.getCardList(id);
        cardList.setTitle(title);
        cardList= cardListService.save(cardList);
        simpMessagingTemplate.convertAndSend("/topic/board",cardList);
        return ResponseEntity.ok(cardList);
    }

    @DeleteMapping("/remove-card-list/{listId}/remove-card/{cardId}")
    public ResponseEntity<Card> removeCard(@PathVariable(name = "listId") long listId,
                                           @PathVariable(name = "cardId") long cardId) {
        if(listId < 0 || !cardListService.exists(listId)||
                cardId < 0 || !cardService.exists(cardId)){
            return ResponseEntity.notFound().build();
        }
        CardList list = cardListService.getCardList(listId);
        Card card = cardService.getCard(cardId);
        list.getCards().remove(card);
        cardListService.save(list);
        cardService.delete(cardId);
        simpMessagingTemplate.convertAndSend("/topic/board",card);
        return ResponseEntity.ok(card);
    }
}