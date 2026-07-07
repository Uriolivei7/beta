package com.facebook.react.runtime;

import com.facebook.jni.HybridData;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.soloader.SoLoader;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class JSTimerExecutor implements B1.c {
    private static final a Companion = new a(null);
    private final HybridData mHybridData;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    static {
        SoLoader.t("rninstance");
    }

    public JSTimerExecutor(HybridData hybridData) {
        D2.h.f(hybridData, "mHybridData");
        this.mHybridData = hybridData;
    }

    private final native void callTimers(WritableNativeArray writableNativeArray);

    @Override // B1.c
    public void callIdleCallbacks(double d4) {
    }

    @Override // B1.c
    public void callTimers(WritableArray writableArray) {
        D2.h.f(writableArray, "timerIDs");
        callTimers((WritableNativeArray) writableArray);
    }

    @Override // B1.c
    public void emitTimeDriftWarning(String str) {
        D2.h.f(str, "warningMessage");
    }
}
