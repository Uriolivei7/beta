package com.facebook.react.views.textinput;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

/* JADX INFO: loaded from: classes.dex */
class n extends P1.d {
    @Deprecated
    public n(int i3) {
        this(-1, i3);
    }

    @Override // P1.d
    public boolean a() {
        return false;
    }

    @Override // P1.d
    protected WritableMap j() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putInt("target", o());
        return writableMapCreateMap;
    }

    @Override // P1.d
    public String k() {
        return "topBlur";
    }

    public n(int i3, int i4) {
        super(i3, i4);
    }
}
