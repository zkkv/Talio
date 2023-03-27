package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;

import java.io.IOException;
import java.util.List;

public class BoardOverviewService {
    private final ServerUtils serverUtils;

    @Inject
    public BoardOverviewService(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public CardList addCardList(CardList cardList) {
        return serverUtils.addCardListToBoard(cardList);
    }

    public Card addCard(Card card, long cardListId) {
        return serverUtils.addCardToCardList(card, cardListId);
    }

    public List<CardList> getCardLists() {
        return serverUtils.getAllCardLists();
    }

    public void removeCard(Card card, long cardListId) {
        serverUtils.removeCardFromList(card, cardListId);
    }

    public void removeCardList(CardList cardList) {
        serverUtils.removeCardListFromBoard(cardList);
    }

    public Card addCardAtIndex(Card card, long cardListId, int index) {
        return serverUtils.addCardToCardListWithIndex(card, cardListId, index);
    }

    public List<Card> getCards(long cardListId) {
        return serverUtils.getCardsOfCardList(cardListId);
    }

    public CardList getCardList(long cardListId){
        return serverUtils.getCardList(cardListId);
    }

    public CardList updateCardListTitle(long cardListId,String title){
        return serverUtils.updateCardListTitle(cardListId,title);
    }

    public Card updateCardTitle(long cardId,String title){
        return serverUtils.updateCardTitle(cardId,title);
    }

    public void setServerAddress(String ip){
        serverUtils.setServer(ip);
    }

    public void testServerConnection() throws IOException {
        serverUtils.testConnection();
    }

    public void closeServerConnection(){
        serverUtils.closeConnection();
    }
}
