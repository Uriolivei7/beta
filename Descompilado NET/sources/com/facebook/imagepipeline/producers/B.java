package com.facebook.imagepipeline.producers;

import H0.C0166d;
import J0.InterfaceC0169c;
import U0.b;

/* JADX INFO: loaded from: classes.dex */
public class B implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final X.n f5964a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final H0.k f5965b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final e0 f5966c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final C0166d f5967d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final C0166d f5968e;

    private static class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final f0 f5969c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final X.n f5970d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final H0.k f5971e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final C0166d f5972f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final C0166d f5973g;

        public a(InterfaceC0354n interfaceC0354n, f0 f0Var, X.n nVar, H0.k kVar, C0166d c0166d, C0166d c0166d2) {
            super(interfaceC0354n);
            this.f5969c = f0Var;
            this.f5970d = nVar;
            this.f5971e = kVar;
            this.f5972f = c0166d;
            this.f5973g = c0166d2;
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
        public void i(O0.j jVar, int i3) {
            try {
                if (V0.b.d()) {
                    V0.b.a("EncodedProbeProducer#onNewResultImpl");
                }
                if (!AbstractC0343c.f(i3) && jVar != null && !AbstractC0343c.m(i3, 10) && jVar.D() != D0.c.f151d) {
                    U0.b bVarX = this.f5969c.X();
                    R.d dVarC = this.f5971e.c(bVarX, this.f5969c.i());
                    this.f5972f.a(dVarC);
                    if ("memory_encoded".equals(this.f5969c.y("origin"))) {
                        if (!this.f5973g.b(dVarC)) {
                            boolean z3 = bVarX.c() == b.EnumC0034b.SMALL;
                            InterfaceC0169c interfaceC0169c = (InterfaceC0169c) this.f5970d.get();
                            (z3 ? interfaceC0169c.c() : interfaceC0169c.a()).f(dVarC);
                            this.f5973g.a(dVarC);
                        }
                    } else if ("disk".equals(this.f5969c.y("origin"))) {
                        this.f5973g.a(dVarC);
                    }
                    p().d(jVar, i3);
                    if (V0.b.d()) {
                        V0.b.b();
                        return;
                    }
                    return;
                }
                p().d(jVar, i3);
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

    public B(X.n nVar, H0.k kVar, C0166d c0166d, C0166d c0166d2, e0 e0Var) {
        this.f5964a = nVar;
        this.f5965b = kVar;
        this.f5967d = c0166d;
        this.f5968e = c0166d2;
        this.f5966c = e0Var;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        try {
            if (V0.b.d()) {
                V0.b.a("EncodedProbeProducer#produceResults");
            }
            h0 h0VarP = f0Var.P();
            h0VarP.g(f0Var, c());
            a aVar = new a(interfaceC0354n, f0Var, this.f5964a, this.f5965b, this.f5967d, this.f5968e);
            h0VarP.d(f0Var, "EncodedProbeProducer", null);
            if (V0.b.d()) {
                V0.b.a("mInputProducer.produceResult");
            }
            this.f5966c.b(aVar, f0Var);
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
        return "EncodedProbeProducer";
    }
}
