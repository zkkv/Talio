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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.inject.Singleton;
import commons.*;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

@Singleton
public class ServerUtils {
    private String server;
    private HttpURLConnection connection;


    public void setServer(String server) {
        this.server ="http://"+ server;
    }

    public void testConnection() throws IOException {
        var url = new URL(server + "/api/quotes");
        connection = (HttpURLConnection) url.openConnection();
    }

    public void closeConnection() {
        connection.disconnect();
        server = null;
    }

    public Board getOrCreateBoard(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/create") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Board>(){});
    }

    public CardList addCardListToBoard(CardList cardList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/add-card-list") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }

    public Response removeCardListFromBoard(CardList cardList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/remove-card-list/" + cardList.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    public List<CardList> getAllCardLists(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/card-lists") //
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

    public CardList updateCardListTitle(long cardListId,String title){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/card-lists/update-title/"+cardListId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(title,APPLICATION_JSON), CardList.class);
    }

    public Card updateCardTitle(long cardId,String title){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/card/update-title/"+cardId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(title,APPLICATION_JSON), Card.class);
    }

    public Card addCardToCardList(Card card, long cardListID) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/card-lists/" + cardListID + "/cards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Card addCardToCardListWithIndex(Card card, long cardListID, int index) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/card-lists/" + cardListID + "/cards/"+index)
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

    public Response removeCardFromList(Card card,long cardListId) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/card-lists/remove-card-list/" + cardListId
                + "/remove-card/" + card.getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .delete();
    }
}