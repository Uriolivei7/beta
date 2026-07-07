package com.facebook.imagepipeline.producers;

import b0.AbstractC0306a;
import java.util.Map;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class c0 implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final e0 f6112a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final G0.b f6113b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Executor f6114c;

    private class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final h0 f6115c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final f0 f6116d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final U0.d f6117e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f6118f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private AbstractC0306a f6119g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private int f6120h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private boolean f6121i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private boolean f6122j;

        /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.c0$a$a, reason: collision with other inner class name */
        class C0095a extends C0346f {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ c0 f6124a;

            C0095a(c0 c0Var) {
                this.f6124a = c0Var;
            }

            @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
            public void a() {
                a.this.C();
            }
        }

        class b implements Runnable {
            b() {
            }

            @Override // java.lang.Runnable
            public void run() {
                AbstractC0306a abstractC0306a;
                int i3;
                synchronized (a.this) {
                    abstractC0306a = a.this.f6119g;
                    i3 = a.this.f6120h;
                    a.this.f6119g = null;
                    a.this.f6121i = false;
                }
                if (AbstractC0306a.c0(abstractC0306a)) {
                    try {
                        a.this.z(abstractC0306a, i3);
                    } finally {
                        AbstractC0306a.D(abstractC0306a);
                    }
                }
                a.this.x();
            }
        }

        public a(InterfaceC0354n interfaceC0354n, h0 h0Var, U0.d dVar, f0 f0Var) {
            super(interfaceC0354n);
            this.f6119g = null;
            this.f6120h = 0;
            this.f6121i = false;
            this.f6122j = false;
            this.f6115c = h0Var;
            this.f6117e = dVar;
            this.f6116d = f0Var;
            f0Var.a0(new C0095a(c0.this));
        }

        private Map A(h0 h0Var, f0 f0Var, U0.d dVar) {
            if (h0Var.j(f0Var, "PostprocessorProducer")) {
                return X.g.of("Postprocessor", dVar.getName());
            }
            return null;
        }

        private synchronized boolean B() {
            return this.f6118f;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void C() {
            if (y()) {
                p().b();
            }
        }

        private void D(Throwable th) {
            if (y()) {
                p().a(th);
            }
        }

        private void E(AbstractC0306a abstractC0306a, int i3) {
            boolean zE = AbstractC0343c.e(i3);
            if ((zE || B()) && !(zE && y())) {
                return;
            }
            p().d(abstractC0306a, i3);
        }

        private AbstractC0306a G(O0.d dVar) {
            O0.e eVar = (O0.e) dVar;
            AbstractC0306a abstractC0306aA = this.f6117e.a(eVar.C(), c0.this.f6113b);
            try {
                O0.e eVarI = O0.e.I(abstractC0306aA, dVar.l(), eVar.N(), eVar.s0());
                eVarI.q(eVar.a());
                return AbstractC0306a.d0(eVarI);
            } finally {
                AbstractC0306a.D(abstractC0306aA);
            }
        }

        private synchronized boolean H() {
            if (this.f6118f || !this.f6121i || this.f6122j || !AbstractC0306a.c0(this.f6119g)) {
                return false;
            }
            this.f6122j = true;
            return true;
        }

        private boolean I(O0.d dVar) {
            return dVar instanceof O0.e;
        }

        private void J() {
            c0.this.f6114c.execute(new b());
        }

        private void K(AbstractC0306a abstractC0306a, int i3) {
            synchronized (this) {
                try {
                    if (this.f6118f) {
                        return;
                    }
                    AbstractC0306a abstractC0306a2 = this.f6119g;
                    this.f6119g = AbstractC0306a.A(abstractC0306a);
                    this.f6120h = i3;
                    this.f6121i = true;
                    boolean zH = H();
                    AbstractC0306a.D(abstractC0306a2);
                    if (zH) {
                        J();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void x() {
            boolean zH;
            synchronized (this) {
                this.f6122j = false;
                zH = H();
            }
            if (zH) {
                J();
            }
        }

        private boolean y() {
            synchronized (this) {
                try {
                    if (this.f6118f) {
                        return false;
                    }
                    AbstractC0306a abstractC0306a = this.f6119g;
                    this.f6119g = null;
                    this.f6118f = true;
                    AbstractC0306a.D(abstractC0306a);
                    return true;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void z(AbstractC0306a abstractC0306a, int i3) {
            X.k.b(Boolean.valueOf(AbstractC0306a.c0(abstractC0306a)));
            if (!I((O0.d) abstractC0306a.P())) {
                E(abstractC0306a, i3);
                return;
            }
            this.f6115c.g(this.f6116d, "PostprocessorProducer");
            try {
                try {
                    AbstractC0306a abstractC0306aG = G((O0.d) abstractC0306a.P());
                    h0 h0Var = this.f6115c;
                    f0 f0Var = this.f6116d;
                    h0Var.d(f0Var, "PostprocessorProducer", A(h0Var, f0Var, this.f6117e));
                    E(abstractC0306aG, i3);
                    AbstractC0306a.D(abstractC0306aG);
                } catch (Exception e4) {
                    h0 h0Var2 = this.f6115c;
                    f0 f0Var2 = this.f6116d;
                    h0Var2.i(f0Var2, "PostprocessorProducer", e4, A(h0Var2, f0Var2, this.f6117e));
                    D(e4);
                    AbstractC0306a.D(null);
                }
            } catch (Throwable th) {
                AbstractC0306a.D(null);
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: F, reason: merged with bridge method [inline-methods] */
        public void i(AbstractC0306a abstractC0306a, int i3) {
            if (AbstractC0306a.c0(abstractC0306a)) {
                K(abstractC0306a, i3);
            } else if (AbstractC0343c.e(i3)) {
                E(null, i3);
            }
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0360u, com.facebook.imagepipeline.producers.AbstractC0343c
        protected void g() {
            C();
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0360u, com.facebook.imagepipeline.producers.AbstractC0343c
        protected void h(Throwable th) {
            D(th);
        }
    }

    class b extends AbstractC0360u {
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
        public void i(AbstractC0306a abstractC0306a, int i3) {
            if (AbstractC0343c.f(i3)) {
                return;
            }
            p().d(abstractC0306a, i3);
        }

        private b(a aVar) {
            super(aVar);
        }
    }

    public c0(e0 e0Var, G0.b bVar, Executor executor) {
        this.f6112a = (e0) X.k.g(e0Var);
        this.f6113b = bVar;
        this.f6114c = (Executor) X.k.g(executor);
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        h0 h0VarP = f0Var.P();
        U0.d dVarL = f0Var.X().l();
        X.k.g(dVarL);
        this.f6112a.b(new b(new a(interfaceC0354n, h0VarP, dVarL, f0Var)), f0Var);
    }
}
