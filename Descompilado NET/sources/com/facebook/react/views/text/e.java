package com.facebook.react.views.text;

import com.facebook.react.uimanager.C0452r0;

/* JADX INFO: loaded from: classes.dex */
public class e extends C0452r0 {

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private String f7949y = null;

    @Override // com.facebook.react.uimanager.C0452r0, com.facebook.react.uimanager.InterfaceC0451q0
    public boolean R() {
        return true;
    }

    @L1.a(name = "text")
    public void setText(String str) {
        this.f7949y = str;
        y0();
    }

    @Override // com.facebook.react.uimanager.C0452r0
    public String toString() {
        return v() + " [text: " + this.f7949y + "]";
    }

    public String v1() {
        return this.f7949y;
    }
}
