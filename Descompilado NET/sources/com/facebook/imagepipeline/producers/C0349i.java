package com.facebook.imagepipeline.producers;

import U0.b;
import b0.AbstractC0306a;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.i, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0349i implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final H0.x f6145a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final H0.k f6146b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final e0 f6147c;

    /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.i$a */
    class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ R.d f6148c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ boolean f6149d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(InterfaceC0354n interfaceC0354n, R.d dVar, boolean z3) {
            super(interfaceC0354n);
            this.f6148c = dVar;
            this.f6149d = z3;
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
        public void i(AbstractC0306a abstractC0306a, int i3) {
            AbstractC0306a abstractC0306a2;
            try {
                if (V0.b.d()) {
                    V0.b.a("BitmapMemoryCacheProducer#onNewResultImpl");
                }
                boolean zE = AbstractC0343c.e(i3);
                if (abstractC0306a == null) {
                    if (zE) {
                        p().d(null, i3);
                    }
                    if (V0.b.d()) {
                        V0.b.b();
                        return;
                    }
                    return;
                }
                if (!((O0.d) abstractC0306a.P()).l0() && !AbstractC0343c.n(i3, 8)) {
                    if (!zE && (abstractC0306a2 = C0349i.this.f6145a.get(this.f6148c)) != null) {
                        try {
                            O0.o oVarL = ((O0.d) abstractC0306a.P()).l();
                            O0.o oVarL2 = ((O0.d) abstractC0306a2.P()).l();
                            if (oVarL2.a() || oVarL2.c() >= oVarL.c()) {
                                p().d(abstractC0306a2, i3);
                                if (V0.b.d()) {
                                    V0.b.b();
                                    return;
                                }
                                return;
                            }
                        } finally {
                            AbstractC0306a.D(abstractC0306a2);
                        }
                    }
                    AbstractC0306a abstractC0306aB = this.f6149d ? C0349i.this.f6145a.b(this.f6148c, abstractC0306a) : null;
                    if (zE) {
                        try {
                            p().c(1.0f);
                        } catch (Throwable th) {
                            AbstractC0306a.D(abstractC0306aB);
                            throw th;
                        }
                    }
                    InterfaceC0354n interfaceC0354nP = p();
                    if (abstractC0306aB != null) {
                        abstractC0306a = abstractC0306aB;
                    }
                    interfaceC0354nP.d(abstractC0306a, i3);
                    AbstractC0306a.D(abstractC0306aB);
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
            } catch (Throwable th2) {
                if (V0.b.d()) {
                    V0.b.b();
                }
                throw th2;
            }
        }
    }

    public C0349i(H0.x xVar, H0.k kVar, e0 e0Var) {
        this.f6145a = xVar;
        this.f6146b = kVar;
        this.f6147c = e0Var;
    }

    private static void f(O0.k kVar, f0 f0Var) {
        f0Var.q(kVar.a());
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        try {
            if (V0.b.d()) {
                V0.b.a("BitmapMemoryCacheProducer#produceResults");
            }
            h0 h0VarP = f0Var.P();
            h0VarP.g(f0Var, e());
            R.d dVarB = this.f6146b.b(f0Var.X(), f0Var.i());
            AbstractC0306a abstractC0306a = f0Var.X().y(1) ? this.f6145a.get(dVarB) : null;
            if (abstractC0306a != null) {
                f((O0.k) abstractC0306a.P(), f0Var);
                boolean zA = ((O0.d) abstractC0306a.P()).l().a();
                if (zA) {
                    h0VarP.d(f0Var, e(), h0VarP.j(f0Var, e()) ? X.g.of("cached_value_found", "true") : null);
                    h0VarP.e(f0Var, e(), true);
                    f0Var.n0("memory_bitmap", d());
                    interfaceC0354n.c(1.0f);
                }
                interfaceC0354n.d(abstractC0306a, AbstractC0343c.l(zA));
                abstractC0306a.close();
                if (zA) {
                    if (V0.b.d()) {
                        V0.b.b();
                        return;
                    }
                    return;
                }
            }
            if (f0Var.d0().b() >= b.c.BITMAP_MEMORY_CACHE.b()) {
                h0VarP.d(f0Var, e(), h0VarP.j(f0Var, e()) ? X.g.of("cached_value_found", "false") : null);
                h0VarP.e(f0Var, e(), false);
                f0Var.n0("memory_bitmap", d());
                interfaceC0354n.d(null, 1);
                if (V0.b.d()) {
                    V0.b.b();
                    return;
                }
                return;
            }
            InterfaceC0354n interfaceC0354nG = g(interfaceC0354n, dVarB, f0Var.X().y(2));
            h0VarP.d(f0Var, e(), h0VarP.j(f0Var, e()) ? X.g.of("cached_value_found", "false") : null);
            if (V0.b.d()) {
                V0.b.a("mInputProducer.produceResult");
            }
            this.f6147c.b(interfaceC0354nG, f0Var);
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

    protected String d() {
        return "pipe_bg";
    }

    protected String e() {
        return "BitmapMemoryCacheProducer";
    }

    protected InterfaceC0354n g(InterfaceC0354n interfaceC0354n, R.d dVar, boolean z3) {
        return new a(interfaceC0354n, dVar, z3);
    }
}
