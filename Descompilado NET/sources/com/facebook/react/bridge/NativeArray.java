package com.facebook.react.bridge;

import com.facebook.jni.HybridClassBase;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public abstract class NativeArray extends HybridClassBase implements NativeArrayInterface {
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

    protected NativeArray() {
    }

    @Override // com.facebook.react.bridge.NativeArrayInterface
    public native String toString();
}
