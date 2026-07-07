package com.swmansion.gesturehandler;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;

/* JADX INFO: loaded from: classes.dex */
public abstract class NativeRNGestureHandlerModuleSpec extends ReactContextBaseJavaModule implements TurboModule {
    public static final String NAME = "RNGestureHandlerModule";

    public NativeRNGestureHandlerModuleSpec(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public abstract void attachGestureHandler(double d4, double d5, double d6);

    @ReactMethod
    public abstract void createGestureHandler(String str, double d4, ReadableMap readableMap);

    @ReactMethod
    public abstract void dropGestureHandler(double d4);

    @ReactMethod
    public abstract void flushOperations();

    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return "RNGestureHandlerModule";
    }

    @ReactMethod
    public abstract void handleClearJSResponder();

    @ReactMethod
    public abstract void handleSetJSResponder(double d4, boolean z3);

    @ReactMethod(isBlockingSynchronousMethod = true)
    public abstract boolean install();

    @ReactMethod
    public abstract void updateGestureHandler(double d4, ReadableMap readableMap);
}
