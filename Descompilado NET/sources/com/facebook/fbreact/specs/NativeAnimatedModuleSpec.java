package com.facebook.fbreact.specs;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;

/* JADX INFO: loaded from: classes.dex */
public abstract class NativeAnimatedModuleSpec extends ReactContextBaseJavaModule implements TurboModule {
    public static final String NAME = "NativeAnimatedModule";

    public NativeAnimatedModuleSpec(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public abstract void addAnimatedEventToView(double d4, String str, ReadableMap readableMap);

    @ReactMethod
    public abstract void addListener(String str);

    @ReactMethod
    public abstract void connectAnimatedNodeToView(double d4, double d5);

    @ReactMethod
    public abstract void connectAnimatedNodes(double d4, double d5);

    @ReactMethod
    public abstract void createAnimatedNode(double d4, ReadableMap readableMap);

    @ReactMethod
    public abstract void disconnectAnimatedNodeFromView(double d4, double d5);

    @ReactMethod
    public abstract void disconnectAnimatedNodes(double d4, double d5);

    @ReactMethod
    public abstract void dropAnimatedNode(double d4);

    @ReactMethod
    public abstract void extractAnimatedNodeOffset(double d4);

    @ReactMethod
    public abstract void finishOperationBatch();

    @ReactMethod
    public abstract void flattenAnimatedNodeOffset(double d4);

    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public abstract void getValue(double d4, Callback callback);

    @ReactMethod
    public void queueAndExecuteBatchedOperations(ReadableArray readableArray) {
    }

    @ReactMethod
    public abstract void removeAnimatedEventFromView(double d4, String str, double d5);

    @ReactMethod
    public abstract void removeListeners(double d4);

    @ReactMethod
    public abstract void restoreDefaultValues(double d4);

    @ReactMethod
    public abstract void setAnimatedNodeOffset(double d4, double d5);

    @ReactMethod
    public abstract void setAnimatedNodeValue(double d4, double d5);

    @ReactMethod
    public abstract void startAnimatingNode(double d4, double d5, ReadableMap readableMap, Callback callback);

    @ReactMethod
    public abstract void startListeningToAnimatedNodeValue(double d4);

    @ReactMethod
    public abstract void startOperationBatch();

    @ReactMethod
    public abstract void stopAnimation(double d4);

    @ReactMethod
    public abstract void stopListeningToAnimatedNodeValue(double d4);

    @ReactMethod
    public void updateAnimatedNodeConfig(double d4, ReadableMap readableMap) {
    }
}
