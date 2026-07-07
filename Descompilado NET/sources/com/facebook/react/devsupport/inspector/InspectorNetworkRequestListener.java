package com.facebook.react.devsupport.inspector;

import D2.h;
import com.facebook.jni.HybridData;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class InspectorNetworkRequestListener {
    private final HybridData mHybridData;

    public InspectorNetworkRequestListener(HybridData hybridData) {
        h.f(hybridData, "mHybridData");
        this.mHybridData = hybridData;
    }

    public final native void onCompletion();

    public final native void onData(String str);

    public final native void onError(String str);

    public final native void onHeaders(int i3, Map<String, String> map);
}
