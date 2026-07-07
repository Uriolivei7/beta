package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public class ReactNoCrashSoftException extends RuntimeException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ReactNoCrashSoftException(String str) {
        super(str);
        D2.h.f(str, "m");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ReactNoCrashSoftException(Throwable th) {
        super(th);
        D2.h.f(th, "e");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ReactNoCrashSoftException(String str, Throwable th) {
        super(str, th);
        D2.h.f(str, "m");
    }
}
