package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public interface WritableMap extends ReadableMap {
    WritableMap copy();

    void merge(ReadableMap readableMap);

    void putArray(String str, ReadableArray readableArray);

    void putBoolean(String str, boolean z3);

    void putDouble(String str, double d4);

    void putInt(String str, int i3);

    void putLong(String str, long j3);

    void putMap(String str, ReadableMap readableMap);

    void putNull(String str);

    void putString(String str, String str2);
}
