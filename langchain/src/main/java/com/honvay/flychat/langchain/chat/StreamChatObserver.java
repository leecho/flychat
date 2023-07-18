package com.honvay.flychat.langchain.chat;

public interface StreamChatObserver {

    void onResult(String result);

    void onError(Throwable throwable);

    void onComplete();
}
