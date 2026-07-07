package com.facebook.react.uimanager;

import android.view.Choreographer;
import com.facebook.react.bridge.JSExceptionHandler;
import com.facebook.react.bridge.ReactContext;

/* JADX INFO: loaded from: classes.dex */
public abstract class M implements Choreographer.FrameCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final JSExceptionHandler f7257a;

    protected M(JSExceptionHandler jSExceptionHandler) {
        D2.h.f(jSExceptionHandler, "exceptionHandler");
        this.f7257a = jSExceptionHandler;
    }

    protected abstract void a(long j3);

    @Override // android.view.Choreographer.FrameCallback
    public void doFrame(long j3) {
        try {
            a(j3);
        } catch (RuntimeException e4) {
            this.f7257a.handleException(e4);
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    protected M(ReactContext reactContext) {
        D2.h.f(reactContext, "reactContext");
        JSExceptionHandler exceptionHandler = reactContext.getExceptionHandler();
        D2.h.e(exceptionHandler, "getExceptionHandler(...)");
        this(exceptionHandler);
    }
}
