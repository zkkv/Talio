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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.google.inject.Singleton;
import commons.*;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Singleton
public class ServerUtils {
    private String server;
    private String ws;
    private HttpURLConnection connection;

    private StompSession session;

    // This executor service allows for the client to wait for poll response using another thread
    // and not block the rest of the application.
    private final ExecutorService EXEC = Executors.newCachedThreadPool();
    // See BoardController.
    private Map<Object, Consumer<Tag>> listeners = new HashMap<>();

    public void setServer(String ip) {
        this.server = "http://" + ip;
        this.ws = "ws://" + ip + "/websocket";
    }

    public void testConnection() throws IOException, RuntimeException {
        var url = new URL(server + "/api/quotes");
        connection = (HttpURLConnection) url.openConnection();
        session = connect(ws);
    }

    public void closeConnection() {
        connection.disconnect();
        server = null;
    }


    public Board createBoard(String title,String userName){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/boards/create/"+userName) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(title,APPLICATION_JSON),Board.class);
    }

    public Board getBoard(long boardId){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/boards/"+boardId) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<Board>(){});
    }

    public List<Board> getAllBoards(){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/boards") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<List<Board>>(){});
    }

    public CardList addCardListToBoard(CardList cardList,Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/boards/"+board.getId()+"/add-card-list") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }

    public Board updateBoardTitle(Board board,String title){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/boards/update-title/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(title, APPLICATION_JSON), Board.class);
    }

    public Response removeBoard(Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/boards/remove-board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .delete();
    }

    public User addBoardToUser(String boardKey,String userName){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/users/"+userName+"/board/"+boardKey) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(boardKey,APPLICATION_JSON),User.class);
    }

    public List<Board> getUserBoards(String userName){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/boards/user/"+userName) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<List<Board>>() {});
    }

    public User getUser(String userName){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/users/"+userName) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<User>() {});
    }

    public User createUser(String userName){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/users/"+userName) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(userName,APPLICATION_JSON), User.class);
    }

    public Board getBoardByKey(String key){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/boards/key/"+key) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<Board>(){});
    }
    public User removeBoardForUser(String userName,Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/users/"+userName) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(board, APPLICATION_JSON), User.class);
    }


    public Response removeCardListFromBoard(CardList cardList,Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/boards/remove-card-list/" + cardList.getId()+
                "/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .delete();
    }

    public List<CardList> getAllCardLists(Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/boards/"+board.getId()+"/card-lists") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<List<CardList>>() {});
    }

    public CardList getCardList(long id){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/card-lists/"+id) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<CardList>() {});
    }

    public CardList updateCardListTitle(long cardListId,String title,Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/card-lists/update-title/"+
                cardListId+"/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(title,APPLICATION_JSON), CardList.class);
    }

    public Card updateCardTitle(long cardId,String title,Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/card/update-title/"+cardId+"/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(title,APPLICATION_JSON), Card.class);
    }

    public Card updateCardDescription(long cardId, String description, Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/card/update-description/"
                +cardId+"/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(description,APPLICATION_JSON), Card.class);
    }

    public Card addCardToCardList(Card card, long cardListID,Board board) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/card-lists/" + cardListID + "/cards/board/"+board.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Card addCardToCardListWithIndex(Card card, long cardId,
                                           long cardListID, int index,Board board) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/card-lists/" + cardListID +
                "/cards/"+cardId+"/"+index+"/board/"+board.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public List<Card> getCardsOfCardList(long cardListID) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/card-lists/" + cardListID + "/cards")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Card>>() {});
    }

    public Response removeCardFromList(Card card,long cardListId,Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/card-lists/remove-card-list/" + cardListId
                + "/remove-card/" + card.getId()+"/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .delete();
    }

    public Response removeCardFromListWhenDragged(Card card,long cardListId,Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/card-lists/remove-card-list/" + cardListId
                + "/remove-card-from-list/" + card.getId()+"/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .delete();
    }

    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    public void send(String dest, Object o) {
        session.send(dest, o);
    }

    public StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            //System.out.println("Inside try connect");
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();

        }
        catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new IllegalArgumentException();
    }


    /**
     * Returns a string with an admin password obtained by a GET request.
     *
     * @return      string with the admin password
     * @author      Kirill Zhankov
     */
    public String getAdminPassword() {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/users/admin") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<String>(){});
    }


    /**
     * Adds the provided {@code tag} to the tag list of the
     * board with provided {@code boardId} using POST request and returns the tag.
     *
     * @param tag       Tag to be added
     * @param boardId   id of the board to tag list of which tag should be added
     * @return          Added tag
     * @author          Kirill Zhankov
     */
    public Tag addTagToBoard(Tag tag, long boardId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/boards/" + boardId + "/add-tag")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }


    /**
     * A method to change the name of the tag
     *
     * @param tagId the id of the tag which name is updated
     * @param title the new name
     * @param board the baord in which the tag is switched
     * @return the tag with the new name
     */
    public Tag updateTagName(long tagId,String title,Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/tag/update-name/"+
                tagId+"/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(title,APPLICATION_JSON), Tag.class);
    }

    /**
     * A method to remove a tag
     *
     * @param tagId the id of the tag which is removed
     * @param board the board from which the tag is removed
     * @return the response containing if the tag has been deleted successfully
     */
    public Response removeTag(long tagId,Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/tag/remove/"+
                tagId+"/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .delete();
    }

    /**
     * Returns a list of all tags of a board with {@code boardId}.
     *
     * @param boardId   id of the board to get the tags from
     * @return          list of tags of the board
     * @author          Kirill Zhankov
     */
    public List<Tag> getAllTags(long boardId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/boards/" + boardId + "/tags")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Tag>>() {});
    }

    /**
     * A new method to get the tags of the cards from
     * @param cardId the card from which to get all the tags
     * @return the list of the tags of the card
     */
    public List<Tag> getAllTagsFromCard(long cardId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/card/" + cardId + "/tags")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Tag>>() {});
    }

    /**
     * A method do add the tag to the card
     * @param tag the tag to add
     * @param cardId the id of the card to which we add
     * @return the tag which we add
     */
    public Tag addTagToCard(Tag tag, long cardId,Board board) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/card/" + cardId + "/add-tag/board/"+board.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * A method to remove the tag from the card
     * @param tagId the id of the tag which we add
     * @param cardId cardId the id of the card from which we remove
     * @return if the tag has been removed from the card successfully
     */
    public Response removeTagFromCard(long tagId, long cardId,Board board) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/card/" + cardId + "/remove-tag/"+
                tagId+"/board/"+board.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();
    }

    /**
     * Long-polls the server on a dedicated thread for any tag updates on
     * board with {@code boardId}.
     * In case of a 200 OK, executes the consumer function.
     * In case of 204 CONTENT resends the request.
     *
     * @param boardId   id of the board for which updates should be tracked
     * @param consumer  consumer function which is executed once 200 OK response is received
     * @see             client.scenes.TagsListCtrl#registerForTagUpdates(Board)
     * @author          Kirill Zhankov
     */
    public void registerForTagUpdates(long boardId, Consumer<Tag> consumer) {

        // See EXEC description
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                Response res = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("api/boards/" + boardId + "/tags/updates")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(Response.class);

                var key = new Object();
                listeners.put(key, consumer);

                // If 204 NO CONTENT, just poll again and wait...
                if (res.getStatus() == 204) {
                    continue;
                }

                // ...otherwise, tag has been returned, and we can draw it.
                // See TagListCtrl.registerForTagUpdates() to better understand.
                Tag tag = res.readEntity(Tag.class);
                listeners.remove(key);

                Platform.runLater(() -> {
                    consumer.accept(tag);
                });
            }
        });
    }


    /**
     * Stops the polling.
     *
     * @author  Kirill Zhankov
     * @see     client.Main
     */
    public void stopPolling() {
        EXEC.shutdownNow();
    }


    public SubTask addSubTaskToCard(SubTask subTask, long cardId, Board board) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/card/" + cardId + "/tasks/board/"+board.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(subTask, APPLICATION_JSON), SubTask.class);
    }


    public SubTask updateTitleSubTask(long taskId, String title, Board board, Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/subTask/update-titleTask/" + taskId+"/card/"
                +card.getId()+"/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(title, APPLICATION_JSON), SubTask.class);
    }


    public SubTask updateIsChecked(long taskId, boolean checked, Board board, Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/subTask/update-checkbox-Task/" +
                taskId+"/card/"+card.getId()+"/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(checked, APPLICATION_JSON), SubTask.class);
    }


    public Response removeSubTask(SubTask subTask, long cardId,Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/card/remove-card/" + cardId
                + "/remove-task/" + subTask.getId()+"/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .delete();
    }

    public Card updateCardSubTasks(long cardId,List<SubTask> subtasks,Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/card/update-subTasks/"+cardId+"/board/"+board.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(subtasks,APPLICATION_JSON), Card.class);
    }
}