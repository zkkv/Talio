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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;

import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class CardListControllerTest {

    private TestCardListRepository repo;

    private CardListController sut;

    @BeforeEach
    public void setup() {
        repo = new TestCardListRepository();
        sut = new CardListController(repo, null);
    }

    @Test
    public void cannotAddNullList() {
        var actual = sut.add(new CardList(null, "Title"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void databaseIsUsed() {
        sut.add(new CardList(new ArrayList<>(), "Title"));
        assertTrue(repo.calledMethods.contains("save"));
    }
}