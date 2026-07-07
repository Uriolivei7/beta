package com.facebook.react.bridge;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public interface ReadableArray {
    ReadableArray getArray(int i3);

    boolean getBoolean(int i3);

    double getDouble(int i3);

    Dynamic getDynamic(int i3);

    int getInt(int i3);

    long getLong(int i3);

    ReadableMap getMap(int i3);

    String getString(int i3);

    ReadableType getType(int i3);

    boolean isNull(int i3);

    int size();

    ArrayList<Object> toArrayList();
}
