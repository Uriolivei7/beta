package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.bridge.ReadableArray;

/* JADX INFO: loaded from: classes.dex */
public final class e extends c {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f6852b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f6853c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String f6854d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final ReadableArray f6855e;

    public e(int i3, int i4, String str, ReadableArray readableArray) {
        D2.h.f(str, "commandId");
        this.f6852b = i3;
        this.f6853c = i4;
        this.f6854d = str;
        this.f6855e = readableArray;
    }

    @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
    public void execute(n1.d dVar) {
        D2.h.f(dVar, "mountingManager");
        dVar.p(this.f6852b, this.f6853c, this.f6854d, this.f6855e);
    }

    @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
    public int getSurfaceId() {
        return this.f6852b;
    }

    public String toString() {
        return "DispatchStringCommandMountItem [" + this.f6853c + "] " + this.f6854d;
    }
}
