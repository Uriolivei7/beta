package com.facebook.imagepipeline.producers;

import b0.AbstractC0306a;

/* JADX INFO: loaded from: classes.dex */
public class b0 implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final H0.x f6104a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final H0.k f6105b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final e0 f6106c;

    public static class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final R.d f6107c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final boolean f6108d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final H0.x f6109e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final boolean f6110f;

        public a(InterfaceC0354n interfaceC0354n, R.d dVar, boolean z3, H0.x xVar, boolean z4) {
            super(interfaceC0354n);
            this.f6107c = dVar;
            this.f6108d = z3;
            this.f6109e = xVar;
            this.f6110f = z4;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
        public void i(AbstractC0306a abstractC0306a, int i3) {
            if (abstractC0306a == null) {
                if (AbstractC0343c.e(i3)) {
                    p().d(null, i3);
                }
            } else if (!AbstractC0343c.f(i3) || this.f6108d) {
                AbstractC0306a abstractC0306aB = this.f6110f ? this.f6109e.b(this.f6107c, abstractC0306a) : null;
                try {
                    p().c(1.0f);
                    InterfaceC0354n interfaceC0354nP = p();
                    if (abstractC0306aB != null) {
                        abstractC0306a = abstractC0306aB;
                    }
                    interfaceC0354nP.d(abstractC0306a, i3);
                } finally {
                    AbstractC0306a.D(abstractC0306aB);
                }
            }
        }
    }

    public b0(H0.x xVar, H0.k kVar, e0 e0Var) {
        this.f6104a = xVar;
        this.f6105b = kVar;
        this.f6106c = e0Var;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        h0 h0VarP = f0Var.P();
        U0.b bVarX = f0Var.X();
        Object objI = f0Var.i();
        U0.d dVarL = bVarX.l();
        if (dVarL == null || dVarL.b() == null) {
            this.f6106c.b(interfaceC0354n, f0Var);
            return;
        }
        h0VarP.g(f0Var, c());
        R.d dVarA = this.f6105b.a(bVarX, objI);
        AbstractC0306a abstractC0306a = f0Var.X().y(1) ? this.f6104a.get(dVarA) : null;
        if (abstractC0306a == null) {
            a aVar = new a(interfaceC0354n, dVarA, false, this.f6104a, f0Var.X().y(2));
            h0VarP.d(f0Var, c(), h0VarP.j(f0Var, c()) ? X.g.of("cached_value_found", "false") : null);
            this.f6106c.b(aVar, f0Var);
        } else {
            h0VarP.d(f0Var, c(), h0VarP.j(f0Var, c()) ? X.g.of("cached_value_found", "true") : null);
            h0VarP.e(f0Var, "PostprocessedBitmapMemoryCacheProducer", true);
            f0Var.n0("memory_bitmap", "postprocessed");
            interfaceC0354n.c(1.0f);
            interfaceC0354n.d(abstractC0306a, 1);
            abstractC0306a.close();
        }
    }

    protected String c() {
        return "PostprocessedBitmapMemoryCacheProducer";
    }
}
