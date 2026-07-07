package com.facebook.imagepipeline.producers;

import U0.b;
import b0.AbstractC0306a;

/* JADX INFO: loaded from: classes.dex */
public class A implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final H0.x f5957a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final H0.k f5958b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final e0 f5959c;

    private static class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final H0.x f5960c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final R.d f5961d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final boolean f5962e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final boolean f5963f;

        public a(InterfaceC0354n interfaceC0354n, H0.x xVar, R.d dVar, boolean z3, boolean z4) {
            super(interfaceC0354n);
            this.f5960c = xVar;
            this.f5961d = dVar;
            this.f5962e = z3;
            this.f5963f = z4;
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
        public void i(O0.j jVar, int i3) {
            try {
                if (V0.b.d()) {
                    V0.b.a("EncodedMemoryCacheProducer#onNewResultImpl");
                }
                if (!AbstractC0343c.f(i3) && jVar != null && !AbstractC0343c.m(i3, 10) && jVar.D() != D0.c.f151d) {
                    AbstractC0306a abstractC0306aV = jVar.v();
                    if (abstractC0306aV != null) {
                        try {
                            AbstractC0306a abstractC0306aB = (this.f5963f && this.f5962e) ? this.f5960c.b(this.f5961d, abstractC0306aV) : null;
                            if (abstractC0306aB != null) {
                                try {
                                    O0.j jVar2 = new O0.j(abstractC0306aB);
                                    jVar2.q(jVar);
                                    try {
                                        p().c(1.0f);
                                        p().d(jVar2, i3);
                                        if (V0.b.d()) {
                                            V0.b.b();
                                            return;
                                        }
                                        return;
                                    } finally {
                                        O0.j.o(jVar2);
                                    }
                                } finally {
                                    AbstractC0306a.D(abstractC0306aB);
                                }
                            }
                        } finally {
                            AbstractC0306a.D(abstractC0306aV);
                        }
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

    public A(H0.x xVar, H0.k kVar, e0 e0Var) {
        this.f5957a = xVar;
        this.f5958b = kVar;
        this.f5959c = e0Var;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        try {
            if (V0.b.d()) {
                V0.b.a("EncodedMemoryCacheProducer#produceResults");
            }
            h0 h0VarP = f0Var.P();
            h0VarP.g(f0Var, "EncodedMemoryCacheProducer");
            R.d dVarC = this.f5958b.c(f0Var.X(), f0Var.i());
            AbstractC0306a abstractC0306a = f0Var.X().y(4) ? this.f5957a.get(dVarC) : null;
            try {
                if (abstractC0306a != null) {
                    O0.j jVar = new O0.j(abstractC0306a);
                    try {
                        h0VarP.d(f0Var, "EncodedMemoryCacheProducer", h0VarP.j(f0Var, "EncodedMemoryCacheProducer") ? X.g.of("cached_value_found", "true") : null);
                        h0VarP.e(f0Var, "EncodedMemoryCacheProducer", true);
                        f0Var.D("memory_encoded");
                        interfaceC0354n.c(1.0f);
                        interfaceC0354n.d(jVar, 1);
                        O0.j.o(jVar);
                        if (V0.b.d()) {
                            V0.b.b();
                            return;
                        }
                        return;
                    } catch (Throwable th) {
                        O0.j.o(jVar);
                        throw th;
                    }
                }
                if (f0Var.d0().b() < b.c.ENCODED_MEMORY_CACHE.b()) {
                    a aVar = new a(interfaceC0354n, this.f5957a, dVarC, f0Var.X().y(8), f0Var.e0().G().C());
                    h0VarP.d(f0Var, "EncodedMemoryCacheProducer", h0VarP.j(f0Var, "EncodedMemoryCacheProducer") ? X.g.of("cached_value_found", "false") : null);
                    this.f5959c.b(aVar, f0Var);
                    if (V0.b.d()) {
                        V0.b.b();
                        return;
                    }
                    return;
                }
                h0VarP.d(f0Var, "EncodedMemoryCacheProducer", h0VarP.j(f0Var, "EncodedMemoryCacheProducer") ? X.g.of("cached_value_found", "false") : null);
                h0VarP.e(f0Var, "EncodedMemoryCacheProducer", false);
                f0Var.n0("memory_encoded", "nil-result");
                interfaceC0354n.d(null, 1);
                if (V0.b.d()) {
                    V0.b.b();
                }
            } finally {
                AbstractC0306a.D(abstractC0306a);
            }
        } catch (Throwable th2) {
            if (V0.b.d()) {
                V0.b.b();
            }
            throw th2;
        }
    }
}
