package com.facebook.react.fabric.events;

import D2.h;
import com.facebook.jni.HybridClassBase;
import com.facebook.react.bridge.NativeMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.fabric.c;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class EventEmitterWrapper extends HybridClassBase {
    private static final a Companion = new a(null);

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    static {
        c.a();
    }

    private EventEmitterWrapper() {
    }

    private final native void dispatchEvent(String str, NativeMap nativeMap, int i3);

    private final native void dispatchEventSynchronously(String str, NativeMap nativeMap);

    private final native void dispatchUniqueEvent(String str, NativeMap nativeMap);

    public final synchronized void destroy() {
        if (isValid()) {
            resetNative();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final synchronized void dispatch(String str, WritableMap writableMap, int i3) {
        h.f(str, "eventName");
        if (isValid()) {
            dispatchEvent(str, (NativeMap) writableMap, i3);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final synchronized void dispatchEventSynchronously(String str, WritableMap writableMap) {
        h.f(str, "eventName");
        if (isValid()) {
            UiThreadUtil.assertOnUiThread();
            dispatchEventSynchronously(str, (NativeMap) writableMap);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final synchronized void dispatchUnique(String str, WritableMap writableMap) {
        h.f(str, "eventName");
        if (isValid()) {
            dispatchUniqueEvent(str, (NativeMap) writableMap);
        }
    }
}
