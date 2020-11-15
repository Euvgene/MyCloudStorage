package client;

import common.AbstractMessage;

public interface Callback {
    void callback(AbstractMessage msg);
}
