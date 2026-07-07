package com.facebook.jni;

import a2.AbstractC0211a;

/* JADX INFO: loaded from: classes.dex */
public class ThreadScopeSupport {
    static {
        AbstractC0211a.d("fbjni");
    }

    private static void runStdFunction(long j3) {
        runStdFunctionImpl(j3);
    }

    private static native void runStdFunctionImpl(long j3);
}
