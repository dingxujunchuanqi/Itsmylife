package com.sinoautodiagnoseos.entity.Push;

import java.util.List;

/**
 * Created by Lanye on 2017/3/6.
 */

public class PushSend {
    public List<Msg> Messages;

    public void setMessages(List<Msg> messages) {
        Messages = messages;
    }

    public List<Msg> getMessages() {

        return Messages;
    }
}
