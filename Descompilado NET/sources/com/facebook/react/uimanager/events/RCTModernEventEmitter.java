package com.facebook.react.uimanager.events;

import P1.q;
import com.facebook.react.bridge.WritableMap;

/* JADX INFO: loaded from: classes.dex */
public interface RCTModernEventEmitter extends RCTEventEmitter {
    void receiveEvent(int i3, int i4, String str, WritableMap writableMap);

    void receiveEvent(int i3, int i4, String str, boolean z3, int i5, WritableMap writableMap, int i6);

    void receiveTouches(q qVar);
}
