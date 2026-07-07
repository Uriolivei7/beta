package com.reactnativecommunity.webview;

import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.WritableMap;

/* JADX INFO: loaded from: classes.dex */
public interface RNCWebViewMessagingModule extends JavaScriptModule {
    void onMessage(WritableMap writableMap);

    void onShouldStartLoadWithRequest(WritableMap writableMap);
}
