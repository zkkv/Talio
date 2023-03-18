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
import commons.Board;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {
    private static String server;
    private static HttpURLConnection connection;

    public static void setServer(String server) {
        ServerUtils.server = server;
    }

    public static void testConnection() throws IOException {
        var url = new URL(server + "/api/quotes");
        connection = (HttpURLConnection) url.openConnection();
    }

    public static void closeConnection() {
        connection.disconnect();
        server = null;
    }

    /*public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }*/

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
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
    public Response removeCardListToBoard(CardList cardList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/card-list/removeCardList/" + cardList.getId()) //
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

    public Card addCardToCardList(Card card, long cardListID) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/card-lists/" + cardListID + "/cards")
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
}