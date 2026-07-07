package com.facebook.react.modules.appregistry;

import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.WritableMap;

/* JADX INFO: loaded from: classes.dex */
public interface AppRegistry extends JavaScriptModule {
    void runApplication(String str, WritableMap writableMap);

    void startHeadlessTask(int i3, String str, WritableMap writableMap);

    void unmountApplicationComponentAtRootTag(int i3);
}
