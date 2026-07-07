package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.bridge.ReadableArray;

/* JADX INFO: loaded from: classes.dex */
public final class d extends c {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f6848b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f6849c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f6850d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final ReadableArray f6851e;

    public d(int i3, int i4, int i5, ReadableArray readableArray) {
        this.f6848b = i3;
        this.f6849c = i4;
        this.f6850d = i5;
        this.f6851e = readableArray;
    }

    @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
    public void execute(n1.d dVar) {
        D2.h.f(dVar, "mountingManager");
        dVar.o(this.f6848b, this.f6849c, this.f6850d, this.f6851e);
    }

    @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
    public int getSurfaceId() {
        return this.f6848b;
    }

    public String toString() {
        return "DispatchIntCommandMountItem [" + this.f6849c + "] " + this.f6850d;
    }
}
