package com.facebook.react.internal.interop;

import D2.h;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import s1.C0692a;

/* JADX INFO: loaded from: classes.dex */
public final class InteropEventEmitter implements RCTEventEmitter {
    private EventDispatcher eventDispatcherOverride;
    private final ReactContext reactContext;

    public InteropEventEmitter(ReactContext reactContext) {
        h.f(reactContext, "reactContext");
        this.reactContext = reactContext;
    }

    public final void overrideEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcherOverride = eventDispatcher;
    }

    @Override // com.facebook.react.uimanager.events.RCTEventEmitter
    public void receiveEvent(int i3, String str, WritableMap writableMap) {
        h.f(str, "eventName");
        EventDispatcher eventDispatcherC = this.eventDispatcherOverride;
        if (eventDispatcherC == null) {
            eventDispatcherC = H0.c(this.reactContext, i3);
        }
        int iE = H0.e(this.reactContext);
        if (eventDispatcherC != null) {
            eventDispatcherC.b(new C0692a(str, writableMap, iE, i3));
        }
    }

    @Override // com.facebook.react.uimanager.events.RCTEventEmitter
    public void receiveTouches(String str, WritableArray writableArray, WritableArray writableArray2) {
        h.f(str, "eventName");
        h.f(writableArray, "touches");
        h.f(writableArray2, "changedIndices");
        throw new UnsupportedOperationException("EventEmitter#receiveTouches is not supported by the Fabric Interop Layer");
    }
}
