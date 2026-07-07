package com.facebook.imagepipeline.producers;

import a0.InterfaceC0207a;
import android.os.SystemClock;
import b0.AbstractC0306a;
import com.facebook.imagepipeline.producers.Y;
import java.io.InputStream;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class X implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected final a0.i f6080a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final InterfaceC0207a f6081b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Y f6082c;

    class a implements Y.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ D f6083a;

        a(D d4) {
            this.f6083a = d4;
        }

        @Override // com.facebook.imagepipeline.producers.Y.a
        public void a(Throwable th) {
            X.this.l(this.f6083a, th);
        }

        @Override // com.facebook.imagepipeline.producers.Y.a
        public void b() {
            X.this.k(this.f6083a);
        }

        @Override // com.facebook.imagepipeline.producers.Y.a
        public void c(InputStream inputStream, int i3) throws Throwable {
            if (V0.b.d()) {
                V0.b.a("NetworkFetcher->onResponse");
            }
            X.this.m(this.f6083a, inputStream, i3);
            if (V0.b.d()) {
                V0.b.b();
            }
        }
    }

    public X(a0.i iVar, InterfaceC0207a interfaceC0207a, Y y3) {
        this.f6080a = iVar;
        this.f6081b = interfaceC0207a;
        this.f6082c = y3;
    }

    protected static float e(int i3, int i4) {
        return i4 > 0 ? i3 / i4 : 1.0f - ((float) Math.exp(((double) (-i3)) / 50000.0d));
    }

    private Map f(D d4, int i3) {
        if (d4.d().j(d4.b(), "NetworkFetchProducer")) {
            return this.f6082c.e(d4, i3);
        }
        return null;
    }

    protected static void j(a0.k kVar, int i3, I0.b bVar, InterfaceC0354n interfaceC0354n, f0 f0Var) throws Throwable {
        O0.j jVar;
        AbstractC0306a abstractC0306aD0 = AbstractC0306a.d0(kVar.a());
        O0.j jVar2 = null;
        try {
            jVar = new O0.j(abstractC0306aD0);
        } catch (Throwable th) {
            th = th;
        }
        try {
            jVar.B0(bVar);
            jVar.x0();
            interfaceC0354n.d(jVar, i3);
            O0.j.o(jVar);
            AbstractC0306a.D(abstractC0306aD0);
        } catch (Throwable th2) {
            th = th2;
            jVar2 = jVar;
            O0.j.o(jVar2);
            AbstractC0306a.D(abstractC0306aD0);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k(D d4) {
        d4.d().f(d4.b(), "NetworkFetchProducer", null);
        d4.a().b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l(D d4, Throwable th) {
        d4.d().i(d4.b(), "NetworkFetchProducer", th, null);
        d4.d().e(d4.b(), "NetworkFetchProducer", false);
        d4.b().D("network");
        d4.a().a(th);
    }

    private boolean n(D d4, f0 f0Var) {
        M0.e eVarE = f0Var.e0().e();
        if (eVarE != null && eVarE.c() && d4.b().c0()) {
            return this.f6082c.d(d4);
        }
        return false;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        f0Var.P().g(f0Var, "NetworkFetchProducer");
        D dC = this.f6082c.c(interfaceC0354n, f0Var);
        this.f6082c.b(dC, new a(dC));
    }

    protected long g() {
        return SystemClock.uptimeMillis();
    }

    protected void h(a0.k kVar, D d4) throws Throwable {
        Map mapF = f(d4, kVar.size());
        h0 h0VarD = d4.d();
        h0VarD.d(d4.b(), "NetworkFetchProducer", mapF);
        h0VarD.e(d4.b(), "NetworkFetchProducer", true);
        d4.b().D("network");
        j(kVar, d4.e() | 1, d4.f(), d4.a(), d4.b());
    }

    protected void i(a0.k kVar, D d4) throws Throwable {
        if (n(d4, d4.b())) {
            long jG = g();
            if (jG - d4.c() >= 100) {
                d4.h(jG);
                d4.d().b(d4.b(), "NetworkFetchProducer", "intermediate_result");
                j(kVar, d4.e(), d4.f(), d4.a(), d4.b());
            }
        }
    }

    protected void m(D d4, InputStream inputStream, int i3) throws Throwable {
        a0.k kVarE = i3 > 0 ? this.f6080a.e(i3) : this.f6080a.b();
        byte[] bArr = (byte[]) this.f6081b.get(16384);
        while (true) {
            try {
                int i4 = inputStream.read(bArr);
                if (i4 < 0) {
                    this.f6082c.a(d4, kVarE.size());
                    h(kVarE, d4);
                    this.f6081b.a(bArr);
                    kVarE.close();
                    return;
                }
                if (i4 > 0) {
                    kVarE.write(bArr, 0, i4);
                    i(kVarE, d4);
                    d4.a().c(e(kVarE.size(), i3));
                }
            } catch (Throwable th) {
                this.f6081b.a(bArr);
                kVarE.close();
                throw th;
            }
        }
    }
}
