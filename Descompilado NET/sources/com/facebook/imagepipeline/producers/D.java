package com.facebook.imagepipeline.producers;

import android.net.Uri;

/* JADX INFO: loaded from: classes.dex */
public class D {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final InterfaceC0354n f5975a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final f0 f5976b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private long f5977c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f5978d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private I0.b f5979e;

    public D(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        this.f5975a = interfaceC0354n;
        this.f5976b = f0Var;
    }

    public InterfaceC0354n a() {
        return this.f5975a;
    }

    public f0 b() {
        return this.f5976b;
    }

    public long c() {
        return this.f5977c;
    }

    public h0 d() {
        return this.f5976b.P();
    }

    public int e() {
        return this.f5978d;
    }

    public I0.b f() {
        return this.f5979e;
    }

    public Uri g() {
        return this.f5976b.X().v();
    }

    public void h(long j3) {
        this.f5977c = j3;
    }

    public void i(int i3) {
        this.f5978d = i3;
    }

    public void j(I0.b bVar) {
        this.f5979e = bVar;
    }
}
