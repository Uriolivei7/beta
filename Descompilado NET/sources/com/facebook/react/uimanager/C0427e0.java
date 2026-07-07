package com.facebook.react.uimanager;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

/* JADX INFO: renamed from: com.facebook.react.uimanager.e0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0427e0 extends P1.d {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final q.f f7471l = new q.f(20);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f7472h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f7473i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f7474j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f7475k;

    private C0427e0() {
    }

    public static C0427e0 v(int i3, int i4, int i5, int i6, int i7, int i8) {
        C0427e0 c0427e0 = (C0427e0) f7471l.b();
        if (c0427e0 == null) {
            c0427e0 = new C0427e0();
        }
        c0427e0.u(i3, i4, i5, i6, i7, i8);
        return c0427e0;
    }

    @Override // P1.d
    protected WritableMap j() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putDouble("x", C0429f0.f(this.f7472h));
        writableMapCreateMap.putDouble("y", C0429f0.f(this.f7473i));
        writableMapCreateMap.putDouble("width", C0429f0.f(this.f7474j));
        writableMapCreateMap.putDouble("height", C0429f0.f(this.f7475k));
        WritableMap writableMapCreateMap2 = Arguments.createMap();
        writableMapCreateMap2.putMap("layout", writableMapCreateMap);
        writableMapCreateMap2.putInt("target", o());
        return writableMapCreateMap2;
    }

    @Override // P1.d
    public String k() {
        return "topLayout";
    }

    @Override // P1.d
    public void t() {
        f7471l.a(this);
    }

    protected void u(int i3, int i4, int i5, int i6, int i7, int i8) {
        super.q(i3, i4);
        this.f7472h = i5;
        this.f7473i = i6;
        this.f7474j = i7;
        this.f7475k = i8;
    }
}
