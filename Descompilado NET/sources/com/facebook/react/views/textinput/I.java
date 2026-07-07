package com.facebook.react.views.textinput;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

/* JADX INFO: loaded from: classes.dex */
class I extends P1.d {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private String f8073h;

    @Deprecated
    public I(int i3, String str) {
        this(-1, i3, str);
    }

    @Override // P1.d
    public boolean a() {
        return false;
    }

    @Override // P1.d
    protected WritableMap j() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putInt("target", o());
        writableMapCreateMap.putString("text", this.f8073h);
        return writableMapCreateMap;
    }

    @Override // P1.d
    public String k() {
        return "topSubmitEditing";
    }

    public I(int i3, int i4, String str) {
        super(i3, i4);
        this.f8073h = str;
    }
}
