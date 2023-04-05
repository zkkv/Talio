package server.services;

import commons.Card;
import commons.Tag;
import org.springframework.stereotype.Service;
import server.database.CardRepository;

import java.util.List;

@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card getCard(long cardId) {
        return cardRepository.findById(cardId).get();
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public boolean exists(long cardId) {
        return cardRepository.existsById(cardId);
    }

    public void delete(long cardId) {
        cardRepository.deleteById(cardId);
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public List<Tag> getTags(long cardId){
        return cardRepository.findTagsById(cardId);
    }

}
