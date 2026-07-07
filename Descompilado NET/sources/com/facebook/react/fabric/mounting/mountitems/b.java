package com.facebook.react.fabric.mounting.mountitems;

/* JADX INFO: loaded from: classes.dex */
public final class b implements MountItem {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f6845a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f6846b;

    public b(int i3, int i4) {
        this.f6845a = i3;
        this.f6846b = i4;
    }

    @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
    public void execute(n1.d dVar) {
        D2.h.f(dVar, "mountingManager");
        n1.g gVarF = dVar.f(this.f6845a);
        if (gVarF == null) {
            return;
        }
        gVarF.i(this.f6846b);
    }

    @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
    public int getSurfaceId() {
        return this.f6845a;
    }
}
