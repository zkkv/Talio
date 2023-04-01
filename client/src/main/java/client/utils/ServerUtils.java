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
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import com.google.inject.Singleton;
import commons.*;
import jakarta.ws.rs.core.Response;
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
    private HttpURLConnection connection;

    private StompSession session;


    public void setServer(String ip) {
        this.server ="http://"+ ip;
        session = connect("ws://"+ip+"/websocket");
    }

    public void testConnection() throws IOException {
        var url = new URL(server + "/api/quotes");
        connection = (HttpURLConnection) url.openConnection();
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

    public Card addCardToCardList(Card card, long cardListID,Board board) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/card-lists/" + cardListID + "/cards/board/"+board.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Card addCardToCardListWithIndex(Card card, long cardListID, int index,Board board) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/card-lists/" + cardListID +
                "/cards/"+index+"/board/"+board.getId())
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

    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        System.out.println("Inside registerForMessages    ");
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
            System.out.println("Inside try connect");
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
     * board with provided {@code boardId} using POST request.
     *
     * @param tag       Tag to be added
     * @param boardId   id of the board to tag list of which tag should be add
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
}