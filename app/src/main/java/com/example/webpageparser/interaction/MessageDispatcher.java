package com.example.webpageparser.interaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MessageDispatcher {
    public static void dispatch(Handler messageHandler, String key, String messageText) {
        Message message = Message.obtain();
        message.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString(key, messageText);
        message.setData(bundle);
        messageHandler.sendMessage(message);
    }
}
