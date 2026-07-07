package com.facebook.imagepipeline.producers;

import J0.InterfaceC0169c;
import U0.b;
import com.facebook.imagepipeline.producers.C0361v;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.x, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0363x implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final X.n f6271a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final H0.k f6272b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final e0 f6273c;

    /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.x$a */
    private static class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final f0 f6274c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final X.n f6275d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final H0.k f6276e;

        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
        public void i(O0.j jVar, int i3) {
            this.f6274c.P().g(this.f6274c, "DiskCacheWriteProducer");
            if (AbstractC0343c.f(i3) || jVar == null || AbstractC0343c.m(i3, 10) || jVar.D() == D0.c.f151d) {
                this.f6274c.P().d(this.f6274c, "DiskCacheWriteProducer", null);
                p().d(jVar, i3);
                return;
            }
            U0.b bVarX = this.f6274c.X();
            R.d dVarC = this.f6276e.c(bVarX, this.f6274c.i());
            InterfaceC0169c interfaceC0169c = (InterfaceC0169c) this.f6275d.get();
            H0.j jVarA = C0361v.a(bVarX, interfaceC0169c.c(), interfaceC0169c.a(), interfaceC0169c.b());
            if (jVarA != null) {
                jVarA.p(dVarC, jVar);
                this.f6274c.P().d(this.f6274c, "DiskCacheWriteProducer", null);
                p().d(jVar, i3);
                return;
            }
            this.f6274c.P().i(this.f6274c, "DiskCacheWriteProducer", new C0361v.a("Got no disk cache for CacheChoice: " + Integer.valueOf(bVarX.c().ordinal()).toString()), null);
            p().d(jVar, i3);
        }

        private a(InterfaceC0354n interfaceC0354n, f0 f0Var, X.n nVar, H0.k kVar) {
            super(interfaceC0354n);
            this.f6274c = f0Var;
            this.f6275d = nVar;
            this.f6276e = kVar;
        }
    }

    public C0363x(X.n nVar, H0.k kVar, e0 e0Var) {
        this.f6271a = nVar;
        this.f6272b = kVar;
        this.f6273c = e0Var;
    }

    private void c(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        if (f0Var.d0().b() >= b.c.DISK_CACHE.b()) {
            f0Var.n0("disk", "nil-result_write");
            interfaceC0354n.d(null, 1);
        } else {
            if (f0Var.X().y(32)) {
                interfaceC0354n = new a(interfaceC0354n, f0Var, this.f6271a, this.f6272b);
            }
            this.f6273c.b(interfaceC0354n, f0Var);
        }
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        c(interfaceC0354n, f0Var);
    }
}
