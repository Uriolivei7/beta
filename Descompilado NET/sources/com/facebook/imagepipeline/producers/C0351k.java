package com.facebook.imagepipeline.producers;

import H0.C0166d;
import J0.InterfaceC0169c;
import U0.b;
import b0.AbstractC0306a;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.k, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0351k implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final H0.x f6159a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final X.n f6160b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final H0.k f6161c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final e0 f6162d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final C0166d f6163e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final C0166d f6164f;

    /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.k$a */
    private static class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final f0 f6165c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final H0.x f6166d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final X.n f6167e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final H0.k f6168f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final C0166d f6169g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private final C0166d f6170h;

        public a(InterfaceC0354n interfaceC0354n, f0 f0Var, H0.x xVar, X.n nVar, H0.k kVar, C0166d c0166d, C0166d c0166d2) {
            super(interfaceC0354n);
            this.f6165c = f0Var;
            this.f6166d = xVar;
            this.f6167e = nVar;
            this.f6168f = kVar;
            this.f6169g = c0166d;
            this.f6170h = c0166d2;
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
        public void i(AbstractC0306a abstractC0306a, int i3) {
            try {
                if (V0.b.d()) {
                    V0.b.a("BitmapProbeProducer#onNewResultImpl");
                }
                if (!AbstractC0343c.f(i3) && abstractC0306a != null && !AbstractC0343c.m(i3, 8)) {
                    U0.b bVarX = this.f6165c.X();
                    R.d dVarC = this.f6168f.c(bVarX, this.f6165c.i());
                    String str = (String) this.f6165c.y("origin");
                    if (str != null && str.equals("memory_bitmap")) {
                        if (this.f6165c.e0().G().D() && !this.f6169g.b(dVarC)) {
                            this.f6166d.c(dVarC);
                            this.f6169g.a(dVarC);
                        }
                        if (this.f6165c.e0().G().B() && !this.f6170h.b(dVarC)) {
                            boolean z3 = bVarX.c() == b.EnumC0034b.SMALL;
                            InterfaceC0169c interfaceC0169c = (InterfaceC0169c) this.f6167e.get();
                            (z3 ? interfaceC0169c.c() : interfaceC0169c.a()).f(dVarC);
                            this.f6170h.a(dVarC);
                        }
                    }
                    p().d(abstractC0306a, i3);
                    if (V0.b.d()) {
                        V0.b.b();
                        return;
                    }
                    return;
                }
                p().d(abstractC0306a, i3);
                if (V0.b.d()) {
                    V0.b.b();
                }
            } catch (Throwable th) {
                if (V0.b.d()) {
                    V0.b.b();
                }
                throw th;
            }
        }
    }

    public C0351k(H0.x xVar, X.n nVar, H0.k kVar, C0166d c0166d, C0166d c0166d2, e0 e0Var) {
        this.f6159a = xVar;
        this.f6160b = nVar;
        this.f6161c = kVar;
        this.f6163e = c0166d;
        this.f6164f = c0166d2;
        this.f6162d = e0Var;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        try {
            if (V0.b.d()) {
                V0.b.a("BitmapProbeProducer#produceResults");
            }
            h0 h0VarP = f0Var.P();
            h0VarP.g(f0Var, c());
            a aVar = new a(interfaceC0354n, f0Var, this.f6159a, this.f6160b, this.f6161c, this.f6163e, this.f6164f);
            h0VarP.d(f0Var, "BitmapProbeProducer", null);
            if (V0.b.d()) {
                V0.b.a("mInputProducer.produceResult");
            }
            this.f6162d.b(aVar, f0Var);
            if (V0.b.d()) {
                V0.b.b();
            }
            if (V0.b.d()) {
                V0.b.b();
            }
        } catch (Throwable th) {
            if (V0.b.d()) {
                V0.b.b();
            }
            throw th;
        }
    }

    protected String c() {
        return "BitmapProbeProducer";
    }
}
