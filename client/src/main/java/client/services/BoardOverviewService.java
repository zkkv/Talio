package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import commons.User;
import commons.SubTask;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class BoardOverviewService {
    private final ServerUtils serverUtils;

    @Inject
    public BoardOverviewService(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public CardList addCardList(CardList cardList,Board board) {
        return serverUtils.addCardListToBoard(cardList,board);
    }

    public Card addCard(Card card, long cardListId,Board board) {
        return serverUtils.addCardToCardList(card, cardListId,board);
    }

    public List<CardList> getCardLists(Board board) {
        return serverUtils.getAllCardLists(board);
    }
    public SubTask addSubTask(SubTask subTask, long cardId){
        return serverUtils.addSubTaskToCard(subTask,cardId);
    }

    public Board updateBoardTitle(Board board,String title){
        return serverUtils.updateBoardTitle(board,title);
    }

    public void removeBoard(Board board){
        serverUtils.removeBoard(board);
    }

    public Board getBoardByKey(String key){
        return serverUtils.getBoardByKey(key);
    }

    public User addBoardToUser(String key,String userName){
        return serverUtils.addBoardToUser(key,userName);
    }

    public List<Board> getUserBoards(String userName){
        return serverUtils.getUserBoards(userName);
    }

    public User getUser(String userName){
        return serverUtils.getUser(userName);
    }

    public User createUser(String userName){
        return serverUtils.createUser(userName);
    }


    public void removeCard(Card card, long cardListId,Board board) {
        serverUtils.removeCardFromList(card, cardListId,board);
    }

    public void removeCardList(CardList cardList,Board board) {
        serverUtils.removeCardListFromBoard(cardList,board);
    }

    public Card addCardAtIndex(Card card, long cardListId, int index,Board board) {
        return serverUtils.addCardToCardListWithIndex(card, cardListId, index,board);
    }

    public List<Card> getCards(long cardListId) {
        return serverUtils.getCardsOfCardList(cardListId);
    }

    public CardList getCardList(long cardListId) {
        return serverUtils.getCardList(cardListId);
    }

    public Board getBoard(long boardId) {
        return serverUtils.getBoard(boardId);
    }

    public Board createBoard(String title,String userName) {
        return serverUtils.createBoard(title,userName);
    }

    public List<Board> getAllBoards() {
        return serverUtils.getAllBoards();
    }

    public CardList updateCardListTitle(long cardListId, String title,Board board) {
        return serverUtils.updateCardListTitle(cardListId, title,board);
    }

    public Card updateCardTitle(long cardId, String title,Board board) {
        return serverUtils.updateCardTitle(cardId, title,board);
    }

    public void setServerAddress(String ip) {
        serverUtils.setServer(ip);
    }
    public SubTask updateTitleSubTask(long taskId, String title){
        return serverUtils.updateTitleSubTask(taskId,title);
    }
    public SubTask updateCheckboxTask(long taskId, boolean isChecked){
        return serverUtils.updateIsChecked(taskId,isChecked);
    }
    public void removeSubTask(SubTask subTask, long cardId) {
        serverUtils.removeSubTask(subTask,cardId);
    }

    public void testServerConnection() throws IOException {
        serverUtils.testConnection();
    }

    public void closeServerConnection() {
        serverUtils.closeConnection();
    }
    public <T> void registerForUpdates(String dest, Class<T> type, Consumer<T> consumer){
        serverUtils.registerForMessages(dest,type,consumer);
    }
}