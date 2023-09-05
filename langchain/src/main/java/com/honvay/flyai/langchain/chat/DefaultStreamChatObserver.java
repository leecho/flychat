package com.honvay.flyai.langchain.chat;

import java.util.function.Consumer;

public class DefaultStreamChatObserver implements StreamChatObserver {

    private final Consumer<String> resultHandler;

    private final Consumer<Void> completeHandler;

    private final Consumer<Throwable> errorHandler;

    public DefaultStreamChatObserver(Consumer<String> resultHandler,
                                     Consumer<Void> completeHandler,
                                     Consumer<Throwable> errorHandler) {
        this.resultHandler = resultHandler;
        this.completeHandler = completeHandler;
        this.errorHandler = errorHandler;
    }


    @Override
    public void onResult(String result) {
        resultHandler.accept(result);
    }

    @Override
    public void onError(Throwable throwable) {
        errorHandler.accept(throwable);
    }

    @Override
    public void onComplete() {
        completeHandler.accept(null);
    }
}
