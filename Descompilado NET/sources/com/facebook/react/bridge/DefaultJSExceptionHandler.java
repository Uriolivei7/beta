package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultJSExceptionHandler implements JSExceptionHandler {
    @Override // com.facebook.react.bridge.JSExceptionHandler
    public void handleException(Exception exc) {
        D2.h.f(exc, "e");
        if (!(exc instanceof RuntimeException)) {
            throw new RuntimeException(exc);
        }
    }
}
