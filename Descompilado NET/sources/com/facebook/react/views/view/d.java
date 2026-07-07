package com.facebook.react.views.view;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final d f8164a = new d();

    private d() {
    }

    private final int a(double d4) {
        return Math.max(0, Math.min(255, F2.a.b(d4)));
    }

    public static final int b(double d4, double d5, double d6, double d7) {
        d dVar = f8164a;
        return (dVar.a(d4) << 16) | (dVar.a(d7 * ((double) 255)) << 24) | (dVar.a(d5) << 8) | dVar.a(d6);
    }
}
