package com.facebook.react.bridge;

import com.facebook.jni.HybridClassBase;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public abstract class NativeMap extends HybridClassBase {
    private static final Companion Companion = new Companion(null);

    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    static {
        ReactBridge.staticInit();
    }

    public native String toString();
}
