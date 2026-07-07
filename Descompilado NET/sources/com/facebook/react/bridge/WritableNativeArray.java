package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public final class WritableNativeArray extends ReadableNativeArray implements WritableArray {
    public WritableNativeArray() {
        initHybrid();
    }

    private final native void initHybrid();

    private final native void pushNativeArray(ReadableNativeArray readableNativeArray);

    private final native void pushNativeMap(ReadableNativeMap readableNativeMap);

    @Override // com.facebook.react.bridge.WritableArray
    public void pushArray(ReadableArray readableArray) {
        if (readableArray != null && !(readableArray instanceof ReadableNativeArray)) {
            throw new IllegalStateException("Illegal type provided");
        }
        pushNativeArray((ReadableNativeArray) readableArray);
    }

    @Override // com.facebook.react.bridge.WritableArray
    public native void pushBoolean(boolean z3);

    @Override // com.facebook.react.bridge.WritableArray
    public native void pushDouble(double d4);

    @Override // com.facebook.react.bridge.WritableArray
    public native void pushInt(int i3);

    @Override // com.facebook.react.bridge.WritableArray
    public native void pushLong(long j3);

    @Override // com.facebook.react.bridge.WritableArray
    public void pushMap(ReadableMap readableMap) {
        if (readableMap != null && !(readableMap instanceof ReadableNativeMap)) {
            throw new IllegalStateException("Illegal type provided");
        }
        pushNativeMap((ReadableNativeMap) readableMap);
    }

    @Override // com.facebook.react.bridge.WritableArray
    public native void pushNull();

    @Override // com.facebook.react.bridge.WritableArray
    public native void pushString(String str);
}
