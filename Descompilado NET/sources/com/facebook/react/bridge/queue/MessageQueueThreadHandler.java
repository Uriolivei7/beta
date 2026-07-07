package com.facebook.react.bridge.queue;

import D2.h;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* JADX INFO: loaded from: classes.dex */
public final class MessageQueueThreadHandler extends Handler {
    private final QueueThreadExceptionHandler exceptionHandler;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MessageQueueThreadHandler(Looper looper, QueueThreadExceptionHandler queueThreadExceptionHandler) {
        super(looper);
        h.f(looper, "looper");
        h.f(queueThreadExceptionHandler, "exceptionHandler");
        this.exceptionHandler = queueThreadExceptionHandler;
    }

    @Override // android.os.Handler
    public void dispatchMessage(Message message) {
        h.f(message, "msg");
        try {
            super.dispatchMessage(message);
        } catch (Exception e4) {
            this.exceptionHandler.handleException(e4);
        }
    }
}
