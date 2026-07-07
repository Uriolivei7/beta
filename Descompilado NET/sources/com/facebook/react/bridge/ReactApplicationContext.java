package com.facebook.react.bridge;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public abstract class ReactApplicationContext extends ReactContext {
    public ReactApplicationContext(Context context) {
        super(context.getApplicationContext());
    }
}
