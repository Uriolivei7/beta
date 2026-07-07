package com.facebook.imagepipeline.producers;

import J0.InterfaceC0169c;
import a0.InterfaceC0207a;
import android.net.Uri;
import b0.AbstractC0306a;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class Z implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final X.n f6085a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final H0.k f6086b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final a0.i f6087c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final InterfaceC0207a f6088d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final e0 f6089e;

    class a implements O.d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ h0 f6090a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ f0 f6091b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ InterfaceC0354n f6092c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ R.d f6093d;

        a(h0 h0Var, f0 f0Var, InterfaceC0354n interfaceC0354n, R.d dVar) {
            this.f6090a = h0Var;
            this.f6091b = f0Var;
            this.f6092c = interfaceC0354n;
            this.f6093d = dVar;
        }

        @Override // O.d
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public Void a(O.f fVar) {
            if (Z.g(fVar)) {
                this.f6090a.f(this.f6091b, "PartialDiskCacheProducer", null);
                this.f6092c.b();
            } else if (fVar.n()) {
                this.f6090a.i(this.f6091b, "PartialDiskCacheProducer", fVar.i(), null);
                Z.this.i(this.f6092c, this.f6091b, this.f6093d, null);
            } else {
                O0.j jVar = (O0.j) fVar.j();
                if (jVar != null) {
                    h0 h0Var = this.f6090a;
                    f0 f0Var = this.f6091b;
                    h0Var.d(f0Var, "PartialDiskCacheProducer", Z.f(h0Var, f0Var, true, jVar.c0()));
                    I0.b bVarG = I0.b.g(jVar.c0() - 1);
                    jVar.B0(bVarG);
                    int iC0 = jVar.c0();
                    U0.b bVarX = this.f6091b.X();
                    if (bVarG.c(bVarX.b())) {
                        this.f6091b.n0("disk", "partial");
                        this.f6090a.e(this.f6091b, "PartialDiskCacheProducer", true);
                        this.f6092c.d(jVar, 9);
                    } else {
                        this.f6092c.d(jVar, 8);
                        Z.this.i(this.f6092c, new m0(U0.c.b(bVarX).z(I0.b.d(iC0 - 1)).a(), this.f6091b), this.f6093d, jVar);
                    }
                } else {
                    h0 h0Var2 = this.f6090a;
                    f0 f0Var2 = this.f6091b;
                    h0Var2.d(f0Var2, "PartialDiskCacheProducer", Z.f(h0Var2, f0Var2, false, 0));
                    Z.this.i(this.f6092c, this.f6091b, this.f6093d, jVar);
                }
            }
            return null;
        }
    }

    class b extends C0346f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ AtomicBoolean f6095a;

        b(AtomicBoolean atomicBoolean) {
            this.f6095a = atomicBoolean;
        }

        @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
        public void a() {
            this.f6095a.set(true);
        }
    }

    private static class c extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final X.n f6097c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final R.d f6098d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final a0.i f6099e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final InterfaceC0207a f6100f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final O0.j f6101g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private final boolean f6102h;

        private void q(InputStream inputStream, OutputStream outputStream, int i3) throws IOException {
            byte[] bArr = (byte[]) this.f6100f.get(16384);
            int i4 = i3;
            while (i4 > 0) {
                try {
                    int i5 = inputStream.read(bArr, 0, Math.min(16384, i4));
                    if (i5 < 0) {
                        break;
                    } else if (i5 > 0) {
                        outputStream.write(bArr, 0, i5);
                        i4 -= i5;
                    }
                } finally {
                    this.f6100f.a(bArr);
                }
            }
            if (i4 > 0) {
                throw new IOException(String.format(null, "Failed to read %d bytes - finished %d short", Integer.valueOf(i3), Integer.valueOf(i4)));
            }
        }

        private a0.k r(O0.j jVar, O0.j jVar2) throws IOException {
            int i3 = ((I0.b) X.k.g(jVar2.y())).f387a;
            a0.k kVarE = this.f6099e.e(jVar2.c0() + i3);
            q(jVar.X(), kVarE, i3);
            q(jVar2.X(), kVarE, jVar2.c0());
            return kVarE;
        }

        private void t(a0.k kVar) throws Throwable {
            O0.j jVar;
            Throwable th;
            AbstractC0306a abstractC0306aD0 = AbstractC0306a.d0(kVar.a());
            try {
                jVar = new O0.j(abstractC0306aD0);
                try {
                    jVar.x0();
                    p().d(jVar, 1);
                    O0.j.o(jVar);
                    AbstractC0306a.D(abstractC0306aD0);
                } catch (Throwable th2) {
                    th = th2;
                    O0.j.o(jVar);
                    AbstractC0306a.D(abstractC0306aD0);
                    throw th;
                }
            } catch (Throwable th3) {
                jVar = null;
                th = th3;
            }
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: s, reason: merged with bridge method [inline-methods] */
        public void i(O0.j jVar, int i3) {
            if (AbstractC0343c.f(i3)) {
                return;
            }
            if (this.f6101g != null && jVar != null && jVar.y() != null) {
                try {
                    try {
                        t(r(this.f6101g, jVar));
                    } catch (IOException e4) {
                        Y.a.n("PartialDiskCacheProducer", "Error while merging image data", e4);
                        p().a(e4);
                    }
                    ((InterfaceC0169c) this.f6097c.get()).a().s(this.f6098d);
                    return;
                } finally {
                    jVar.close();
                    this.f6101g.close();
                }
            }
            if (!this.f6102h || !AbstractC0343c.n(i3, 8) || !AbstractC0343c.e(i3) || jVar == null || jVar.D() == D0.c.f151d) {
                p().d(jVar, i3);
            } else {
                ((InterfaceC0169c) this.f6097c.get()).a().p(this.f6098d, jVar);
                p().d(jVar, i3);
            }
        }

        private c(InterfaceC0354n interfaceC0354n, X.n nVar, R.d dVar, a0.i iVar, InterfaceC0207a interfaceC0207a, O0.j jVar, boolean z3) {
            super(interfaceC0354n);
            this.f6097c = nVar;
            this.f6098d = dVar;
            this.f6099e = iVar;
            this.f6100f = interfaceC0207a;
            this.f6101g = jVar;
            this.f6102h = z3;
        }
    }

    public Z(X.n nVar, H0.k kVar, a0.i iVar, InterfaceC0207a interfaceC0207a, e0 e0Var) {
        this.f6085a = nVar;
        this.f6086b = kVar;
        this.f6087c = iVar;
        this.f6088d = interfaceC0207a;
        this.f6089e = e0Var;
    }

    private static Uri e(U0.b bVar) {
        return bVar.v().buildUpon().appendQueryParameter("fresco_partial", "true").build();
    }

    static Map f(h0 h0Var, f0 f0Var, boolean z3, int i3) {
        if (h0Var.j(f0Var, "PartialDiskCacheProducer")) {
            return z3 ? X.g.of("cached_value_found", String.valueOf(z3), "encodedImageSize", String.valueOf(i3)) : X.g.of("cached_value_found", String.valueOf(z3));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean g(O.f fVar) {
        return fVar.l() || (fVar.n() && (fVar.i() instanceof CancellationException));
    }

    private O.d h(InterfaceC0354n interfaceC0354n, f0 f0Var, R.d dVar) {
        return new a(f0Var.P(), f0Var, interfaceC0354n, dVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i(InterfaceC0354n interfaceC0354n, f0 f0Var, R.d dVar, O0.j jVar) {
        this.f6089e.b(new c(interfaceC0354n, this.f6085a, dVar, this.f6087c, this.f6088d, jVar, f0Var.X().y(32)), f0Var);
    }

    private void j(AtomicBoolean atomicBoolean, f0 f0Var) {
        f0Var.a0(new b(atomicBoolean));
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        U0.b bVarX = f0Var.X();
        boolean zY = f0Var.X().y(16);
        boolean zY2 = f0Var.X().y(32);
        if (!zY && !zY2) {
            this.f6089e.b(interfaceC0354n, f0Var);
            return;
        }
        h0 h0VarP = f0Var.P();
        h0VarP.g(f0Var, "PartialDiskCacheProducer");
        R.d dVarD = this.f6086b.d(bVarX, e(bVarX), f0Var.i());
        if (!zY) {
            h0VarP.d(f0Var, "PartialDiskCacheProducer", f(h0VarP, f0Var, false, 0));
            i(interfaceC0354n, f0Var, dVarD, null);
        } else {
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            ((InterfaceC0169c) this.f6085a.get()).a().m(dVarD, atomicBoolean).e(h(interfaceC0354n, f0Var, dVarD));
            j(atomicBoolean, f0Var);
        }
    }
}
