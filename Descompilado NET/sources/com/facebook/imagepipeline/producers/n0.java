package com.facebook.imagepipeline.producers;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class n0 extends V.e {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final InterfaceC0354n f6193c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final h0 f6194d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final f0 f6195e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final String f6196f;

    public n0(InterfaceC0354n interfaceC0354n, h0 h0Var, f0 f0Var, String str) {
        D2.h.f(interfaceC0354n, "consumer");
        D2.h.f(h0Var, "producerListener");
        D2.h.f(f0Var, "producerContext");
        D2.h.f(str, "producerName");
        this.f6193c = interfaceC0354n;
        this.f6194d = h0Var;
        this.f6195e = f0Var;
        this.f6196f = str;
        h0Var.g(f0Var, str);
    }

    @Override // V.e
    protected void d() {
        h0 h0Var = this.f6194d;
        f0 f0Var = this.f6195e;
        String str = this.f6196f;
        h0Var.f(f0Var, str, h0Var.j(f0Var, str) ? g() : null);
        this.f6193c.b();
    }

    @Override // V.e
    protected void e(Exception exc) {
        D2.h.f(exc, "e");
        h0 h0Var = this.f6194d;
        f0 f0Var = this.f6195e;
        String str = this.f6196f;
        h0Var.i(f0Var, str, exc, h0Var.j(f0Var, str) ? h(exc) : null);
        this.f6193c.a(exc);
    }

    @Override // V.e
    protected void f(Object obj) {
        h0 h0Var = this.f6194d;
        f0 f0Var = this.f6195e;
        String str = this.f6196f;
        h0Var.d(f0Var, str, h0Var.j(f0Var, str) ? i(obj) : null);
        this.f6193c.d(obj, 1);
    }

    protected Map g() {
        return null;
    }

    protected Map h(Exception exc) {
        return null;
    }

    protected Map i(Object obj) {
        return null;
    }
}
