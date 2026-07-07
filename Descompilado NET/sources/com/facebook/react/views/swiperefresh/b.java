package com.facebook.react.views.swiperefresh;

import P1.d;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

/* JADX INFO: loaded from: classes.dex */
public final class b extends d {
    public b(int i3) {
        this(-1, i3);
    }

    @Override // P1.d
    protected WritableMap j() {
        return Arguments.createMap();
    }

    @Override // P1.d
    public String k() {
        return "topRefresh";
    }

    public b(int i3, int i4) {
        super(i3, i4);
    }
}
