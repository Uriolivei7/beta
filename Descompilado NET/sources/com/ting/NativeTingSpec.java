package com.ting;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;

/* JADX INFO: loaded from: classes.dex */
public abstract class NativeTingSpec extends ReactContextBaseJavaModule implements TurboModule {
    public static final String NAME = "Ting";

    public NativeTingSpec(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public abstract void alert(ReadableMap readableMap);

    @ReactMethod
    public abstract void dismissAlert();

    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return "Ting";
    }

    @ReactMethod
    public abstract void setup(ReadableMap readableMap);

    @ReactMethod
    public abstract void toast(ReadableMap readableMap);
}
