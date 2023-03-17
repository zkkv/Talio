package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class ListMenuCtrl {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;


    @Inject
    public ListMenuCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
}
