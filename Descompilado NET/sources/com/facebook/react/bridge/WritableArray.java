package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public interface WritableArray extends ReadableArray {
    void pushArray(ReadableArray readableArray);

    void pushBoolean(boolean z3);

    void pushDouble(double d4);

    void pushInt(int i3);

    void pushLong(long j3);

    void pushMap(ReadableMap readableMap);

    void pushNull();

    void pushString(String str);
}
