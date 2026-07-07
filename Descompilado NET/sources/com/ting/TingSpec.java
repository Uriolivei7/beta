package com.ting;

import D2.h;
import com.facebook.react.bridge.ReactApplicationContext;

/* JADX INFO: loaded from: classes.dex */
public abstract class TingSpec extends NativeTingSpec {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TingSpec(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "context");
    }
}
