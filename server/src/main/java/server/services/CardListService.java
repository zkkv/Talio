package server.services;

import commons.CardList;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;

import java.util.List;

@Service
public class CardListService {
    private final CardListRepository cardListRepository;

    public CardListService(CardListRepository cardListRepository) {
        this.cardListRepository = cardListRepository;
    }

    public CardList getCardList(long cardListId){
        return cardListRepository.findById(cardListId).get();
    }

    public List<CardList> getAllCardLists(){
        return cardListRepository.findAll();
    }

    public boolean exists(long cardListId){
        return cardListRepository.existsById(cardListId);
    }

    public void delete(long cardListId){
        cardListRepository.deleteById(cardListId);
    }

    public CardList save(CardList cardList){
        return cardListRepository.save(cardList);
    }
}
