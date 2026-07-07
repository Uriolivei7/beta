package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public final class ObjectAlreadyConsumedException extends RuntimeException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ObjectAlreadyConsumedException(String str) {
        super(str);
        D2.h.f(str, "detailMessage");
    }
}
