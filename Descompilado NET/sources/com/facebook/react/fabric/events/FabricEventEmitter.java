package com.facebook.react.fabric.events;

import D2.h;
import P1.q;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.fabric.FabricUIManager;
import com.facebook.react.uimanager.events.RCTModernEventEmitter;
import d2.C0518a;

/* JADX INFO: loaded from: classes.dex */
public final class FabricEventEmitter implements RCTModernEventEmitter {
    private final FabricUIManager uiManager;

    public FabricEventEmitter(FabricUIManager fabricUIManager) {
        h.f(fabricUIManager, "uiManager");
        this.uiManager = fabricUIManager;
    }

    @Override // com.facebook.react.uimanager.events.RCTEventEmitter
    public void receiveEvent(int i3, String str, WritableMap writableMap) {
        h.f(str, "eventName");
        receiveEvent(-1, i3, str, writableMap);
    }

    @Override // com.facebook.react.uimanager.events.RCTEventEmitter
    public void receiveTouches(String str, WritableArray writableArray, WritableArray writableArray2) {
        h.f(str, "eventName");
        h.f(writableArray, "touches");
        h.f(writableArray2, "changedIndices");
        throw new UnsupportedOperationException("EventEmitter#receiveTouches is not supported by Fabric");
    }

    @Override // com.facebook.react.uimanager.events.RCTModernEventEmitter
    public void receiveEvent(int i3, int i4, String str, WritableMap writableMap) {
        h.f(str, "eventName");
        receiveEvent(i3, i4, str, false, 0, writableMap, 2);
    }

    @Override // com.facebook.react.uimanager.events.RCTModernEventEmitter
    public void receiveTouches(q qVar) {
        h.f(qVar, "event");
        throw new UnsupportedOperationException("EventEmitter#receiveTouches is not supported by Fabric");
    }

    @Override // com.facebook.react.uimanager.events.RCTModernEventEmitter
    public void receiveEvent(int i3, int i4, String str, boolean z3, int i5, WritableMap writableMap, int i6) {
        h.f(str, "eventName");
        C0518a.c(0L, "FabricEventEmitter.receiveEvent('" + str + "')");
        try {
            this.uiManager.receiveEvent(i3, i4, str, z3, writableMap, i6);
        } finally {
            C0518a.i(0L);
        }
    }
}
