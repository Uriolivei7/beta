package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public final class RetryableMountingLayerException extends RuntimeException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RetryableMountingLayerException(String str) {
        super(str);
        D2.h.f(str, "msg");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RetryableMountingLayerException(Throwable th) {
        super(th);
        D2.h.f(th, "e");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RetryableMountingLayerException(String str, Throwable th) {
        super(str, th);
        D2.h.f(str, "msg");
    }
}
