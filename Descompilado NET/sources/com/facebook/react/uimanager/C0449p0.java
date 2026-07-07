package com.facebook.react.uimanager;

/* JADX INFO: renamed from: com.facebook.react.uimanager.p0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0449p0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0449p0 f7604a = new C0449p0();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static int f7605b = 1;

    private C0449p0() {
    }

    public static final synchronized int a() {
        int i3;
        i3 = f7605b;
        f7605b = i3 + 10;
        return i3;
    }
}
