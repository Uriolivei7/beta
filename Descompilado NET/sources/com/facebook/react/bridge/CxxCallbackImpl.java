package com.facebook.react.bridge;

import com.facebook.jni.HybridClassBase;

/* JADX INFO: loaded from: classes.dex */
public final class CxxCallbackImpl extends HybridClassBase implements Callback {
    private CxxCallbackImpl() {
    }

    private final native void nativeInvoke(NativeArray nativeArray);

    @Override // com.facebook.react.bridge.Callback
    public void invoke(Object... objArr) {
        D2.h.f(objArr, "args");
        WritableNativeArray writableNativeArrayFromJavaArgs = Arguments.fromJavaArgs(objArr);
        D2.h.e(writableNativeArrayFromJavaArgs, "fromJavaArgs(...)");
        nativeInvoke(writableNativeArrayFromJavaArgs);
    }
}
