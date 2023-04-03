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
package client;

import client.scenes.*;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CreateBoardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(JoinBoardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(UserPageCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardSettingsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ClientConnectCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StartPageCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddTaskCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ListMenuCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminLoginCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CardDetailsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(TagsListCtrl.class).in(Scopes.SINGLETON);
        binder.bind(TagDetailsCtrl.class).in(Scopes.SINGLETON);
    }
}