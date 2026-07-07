package com.facebook.imagepipeline.producers;

import J0.InterfaceC0169c;
import U0.b;
import com.facebook.imagepipeline.producers.C0361v;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.w, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0362w implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final X.n f6261a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final H0.k f6262b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final e0 f6263c;

    /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.w$a */
    class a implements O.d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ h0 f6264a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ f0 f6265b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ InterfaceC0354n f6266c;

        a(h0 h0Var, f0 f0Var, InterfaceC0354n interfaceC0354n) {
            this.f6264a = h0Var;
            this.f6265b = f0Var;
            this.f6266c = interfaceC0354n;
        }

        @Override // O.d
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public Void a(O.f fVar) {
            if (C0362w.f(fVar)) {
                this.f6264a.f(this.f6265b, "DiskCacheProducer", null);
                this.f6266c.b();
            } else if (fVar.n()) {
                this.f6264a.i(this.f6265b, "DiskCacheProducer", fVar.i(), null);
                C0362w.this.f6263c.b(this.f6266c, this.f6265b);
            } else {
                O0.j jVar = (O0.j) fVar.j();
                if (jVar != null) {
                    h0 h0Var = this.f6264a;
                    f0 f0Var = this.f6265b;
                    h0Var.d(f0Var, "DiskCacheProducer", C0362w.e(h0Var, f0Var, true, jVar.c0()));
                    this.f6264a.e(this.f6265b, "DiskCacheProducer", true);
                    this.f6265b.D("disk");
                    this.f6266c.c(1.0f);
                    this.f6266c.d(jVar, 1);
                    jVar.close();
                } else {
                    h0 h0Var2 = this.f6264a;
                    f0 f0Var2 = this.f6265b;
                    h0Var2.d(f0Var2, "DiskCacheProducer", C0362w.e(h0Var2, f0Var2, false, 0));
                    C0362w.this.f6263c.b(this.f6266c, this.f6265b);
                }
            }
            return null;
        }
    }

    /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.w$b */
    class b extends C0346f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ AtomicBoolean f6268a;

        b(AtomicBoolean atomicBoolean) {
            this.f6268a = atomicBoolean;
        }

        @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
        public void a() {
            this.f6268a.set(true);
        }
    }

    public C0362w(X.n nVar, H0.k kVar, e0 e0Var) {
        this.f6261a = nVar;
        this.f6262b = kVar;
        this.f6263c = e0Var;
    }

    static Map e(h0 h0Var, f0 f0Var, boolean z3, int i3) {
        if (h0Var.j(f0Var, "DiskCacheProducer")) {
            return z3 ? X.g.of("cached_value_found", String.valueOf(z3), "encodedImageSize", String.valueOf(i3)) : X.g.of("cached_value_found", String.valueOf(z3));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean f(O.f fVar) {
        return fVar.l() || (fVar.n() && (fVar.i() instanceof CancellationException));
    }

    private void g(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        if (f0Var.d0().b() < b.c.DISK_CACHE.b()) {
            this.f6263c.b(interfaceC0354n, f0Var);
        } else {
            f0Var.n0("disk", "nil-result_read");
            interfaceC0354n.d(null, 1);
        }
    }

    private O.d h(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        return new a(f0Var.P(), f0Var, interfaceC0354n);
    }

    private void i(AtomicBoolean atomicBoolean, f0 f0Var) {
        f0Var.a0(new b(atomicBoolean));
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        U0.b bVarX = f0Var.X();
        if (!f0Var.X().y(16)) {
            g(interfaceC0354n, f0Var);
            return;
        }
        f0Var.P().g(f0Var, "DiskCacheProducer");
        R.d dVarC = this.f6262b.c(bVarX, f0Var.i());
        InterfaceC0169c interfaceC0169c = (InterfaceC0169c) this.f6261a.get();
        H0.j jVarA = C0361v.a(bVarX, interfaceC0169c.c(), interfaceC0169c.a(), interfaceC0169c.b());
        if (jVarA != null) {
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            jVarA.m(dVarC, atomicBoolean).e(h(interfaceC0354n, f0Var));
            i(atomicBoolean, f0Var);
        } else {
            f0Var.P().i(f0Var, "DiskCacheProducer", new C0361v.a("Got no disk cache for CacheChoice: " + Integer.valueOf(bVarX.c().ordinal()).toString()), null);
            g(interfaceC0354n, f0Var);
        }
    }
}
