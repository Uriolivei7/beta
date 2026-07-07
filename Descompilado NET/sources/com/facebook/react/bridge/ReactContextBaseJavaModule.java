package com.facebook.react.bridge;

import android.app.Activity;

/* JADX INFO: loaded from: classes.dex */
public abstract class ReactContextBaseJavaModule extends BaseJavaModule {
    public ReactContextBaseJavaModule() {
        super(null);
    }

    protected final Activity getCurrentActivity() {
        return getReactApplicationContext().getCurrentActivity();
    }

    public ReactContextBaseJavaModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }
}
