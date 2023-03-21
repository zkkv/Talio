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

import commons.Board;
import commons.Card;
import commons.CardList;
import commons.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/card-lists")
public class CardListController {

    private final CardListRepository repo;
    private final CardRepository cardRepo;

    public CardListController(CardListRepository repo, CardRepository cardRepo) {
        this.repo = repo;
        this.cardRepo = cardRepo;
    }

    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<CardList>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<CardList> add(@RequestBody CardList cardList) {
        if (cardList.cards == null) {
            return ResponseEntity.badRequest().build();
        }

        CardList saved = repo.save(cardList);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<List<Card>> getCards(@PathVariable("id") long id) {
        return ResponseEntity.ok(repo.findById(id).get().cards);
    }

    @PostMapping("/{id}/cards")
    public ResponseEntity<Card> addCard(@RequestBody Card card, @PathVariable("id") long id) {
        var saved = cardRepo.save(card);
        CardList cardList = repo.findById(id).get();

        cardList.cards.add(saved);

        repo.save(cardList);
        return ResponseEntity.ok(card);
    }

    @PutMapping("/update-title")
    public ResponseEntity<CardList> updateTitle(@RequestBody Pair<CardList,String> request){
        CardList cardList = request.getLeft();
        cardList.title = request.getRight();
        return ResponseEntity.ok(repo.save(cardList));
    }

    @DeleteMapping("/remove-card-list/{listId}/remove-card/{cardId}")
    public ResponseEntity<Card> removeCard(@PathVariable(name = "listId") long listId, @PathVariable(name = "cardId") long cardId) {
        if(listId < 0 || !repo.existsById(listId)){
            return ResponseEntity.notFound().build();
        }
        if(cardId < 0 || !cardRepo.existsById(cardId)){
            return ResponseEntity.notFound().build();
        }
        CardList list = repo.findById(listId).get();
        Card card = cardRepo.findById(cardId).get();
        list.cards.remove(card);
        repo.save(list);
        cardRepo.deleteById(cardId);
        return ResponseEntity.ok(card);
    }
}