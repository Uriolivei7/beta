package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public final class NoSuchKeyException extends RuntimeException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NoSuchKeyException(String str) {
        super(str);
        D2.h.f(str, "msg");
    }
}
