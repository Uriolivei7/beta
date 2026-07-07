package com.facebook.imagepipeline.producers;

import b0.AbstractC0306a;
import com.facebook.imagepipeline.producers.H;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class l0 implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Executor f6177a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final a0.i f6178b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final e0 f6179c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final boolean f6180d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final W0.d f6181e;

    private class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final boolean f6182c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final W0.d f6183d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final f0 f6184e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f6185f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final H f6186g;

        /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.l0$a$a, reason: collision with other inner class name */
        class C0096a implements H.d {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ l0 f6188a;

            C0096a(l0 l0Var) {
                this.f6188a = l0Var;
            }

            @Override // com.facebook.imagepipeline.producers.H.d
            public void a(O0.j jVar, int i3) throws Throwable {
                if (jVar == null) {
                    a.this.p().d(null, i3);
                } else {
                    a aVar = a.this;
                    aVar.w(jVar, i3, (W0.c) X.k.g(aVar.f6183d.createImageTranscoder(jVar.D(), a.this.f6182c)));
                }
            }
        }

        class b extends C0346f {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ l0 f6190a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ InterfaceC0354n f6191b;

            b(l0 l0Var, InterfaceC0354n interfaceC0354n) {
                this.f6190a = l0Var;
                this.f6191b = interfaceC0354n;
            }

            @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
            public void a() {
                a.this.f6186g.c();
                a.this.f6185f = true;
                this.f6191b.b();
            }

            @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
            public void b() {
                if (a.this.f6184e.c0()) {
                    a.this.f6186g.h();
                }
            }
        }

        a(InterfaceC0354n interfaceC0354n, f0 f0Var, boolean z3, W0.d dVar) {
            super(interfaceC0354n);
            this.f6185f = false;
            this.f6184e = f0Var;
            Boolean boolS = f0Var.X().s();
            this.f6182c = boolS != null ? boolS.booleanValue() : z3;
            this.f6183d = dVar;
            this.f6186g = new H(l0.this.f6177a, new C0096a(l0.this), 100);
            f0Var.a0(new b(l0.this, interfaceC0354n));
        }

        private O0.j A(O0.j jVar) {
            I0.h hVarT = this.f6184e.X().t();
            return (hVarT.j() || !hVarT.i()) ? jVar : y(jVar, hVarT.h());
        }

        private O0.j B(O0.j jVar) {
            return (this.f6184e.X().t().f() || jVar.N() == 0 || jVar.N() == -1) ? jVar : y(jVar, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void w(O0.j jVar, int i3, W0.c cVar) throws Throwable {
            this.f6184e.P().g(this.f6184e, "ResizeAndRotateProducer");
            U0.b bVarX = this.f6184e.X();
            a0.k kVarB = l0.this.f6178b.b();
            try {
                W0.b bVarC = cVar.c(jVar, kVarB, bVarX.t(), bVarX.r(), null, 85, jVar.z());
                if (bVarC.a() == 2) {
                    throw new RuntimeException("Error while transcoding the image");
                }
                Map mapZ = z(jVar, bVarX.r(), bVarC, cVar.a());
                AbstractC0306a abstractC0306aD0 = AbstractC0306a.d0(kVarB.a());
                try {
                    O0.j jVar2 = new O0.j(abstractC0306aD0);
                    jVar2.E0(D0.b.f135b);
                    try {
                        jVar2.x0();
                        this.f6184e.P().d(this.f6184e, "ResizeAndRotateProducer", mapZ);
                        if (bVarC.a() != 1) {
                            i3 |= 16;
                        }
                        p().d(jVar2, i3);
                    } finally {
                        O0.j.o(jVar2);
                    }
                } finally {
                    AbstractC0306a.D(abstractC0306aD0);
                }
            } catch (Exception e4) {
                this.f6184e.P().i(this.f6184e, "ResizeAndRotateProducer", e4, null);
                if (AbstractC0343c.e(i3)) {
                    p().a(e4);
                }
            } finally {
                kVarB.close();
            }
        }

        private void x(O0.j jVar, int i3, D0.c cVar) {
            p().d((cVar == D0.b.f135b || cVar == D0.b.f145l) ? B(jVar) : A(jVar), i3);
        }

        private O0.j y(O0.j jVar, int i3) {
            O0.j jVarI = O0.j.i(jVar);
            if (jVarI != null) {
                jVarI.F0(i3);
            }
            return jVarI;
        }

        private Map z(O0.j jVar, I0.g gVar, W0.b bVar, String str) {
            String str2;
            if (!this.f6184e.P().j(this.f6184e, "ResizeAndRotateProducer")) {
                return null;
            }
            String str3 = jVar.h() + "x" + jVar.d();
            if (gVar != null) {
                str2 = gVar.f421a + "x" + gVar.f422b;
            } else {
                str2 = "Unspecified";
            }
            HashMap map = new HashMap();
            map.put("Image format", String.valueOf(jVar.D()));
            map.put("Original size", str3);
            map.put("Requested size", str2);
            map.put("queueTime", String.valueOf(this.f6186g.f()));
            map.put("Transcoder id", str);
            map.put("Transcoding result", String.valueOf(bVar));
            return X.g.b(map);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: C, reason: merged with bridge method [inline-methods] */
        public void i(O0.j jVar, int i3) {
            if (this.f6185f) {
                return;
            }
            boolean zE = AbstractC0343c.e(i3);
            if (jVar == null) {
                if (zE) {
                    p().d(null, 1);
                    return;
                }
                return;
            }
            D0.c cVarD = jVar.D();
            f0.e eVarH = l0.h(this.f6184e.X(), jVar, (W0.c) X.k.g(this.f6183d.createImageTranscoder(cVarD, this.f6182c)));
            if (zE || eVarH != f0.e.UNSET) {
                if (eVarH != f0.e.YES) {
                    x(jVar, i3, cVarD);
                } else if (this.f6186g.k(jVar, i3)) {
                    if (zE || this.f6184e.c0()) {
                        this.f6186g.h();
                    }
                }
            }
        }
    }

    public l0(Executor executor, a0.i iVar, e0 e0Var, boolean z3, W0.d dVar) {
        this.f6177a = (Executor) X.k.g(executor);
        this.f6178b = (a0.i) X.k.g(iVar);
        this.f6179c = (e0) X.k.g(e0Var);
        this.f6181e = (W0.d) X.k.g(dVar);
        this.f6180d = z3;
    }

    private static boolean f(I0.h hVar, O0.j jVar) {
        return !hVar.f() && (W0.e.e(hVar, jVar) != 0 || g(hVar, jVar));
    }

    private static boolean g(I0.h hVar, O0.j jVar) {
        if (hVar.i() && !hVar.f()) {
            return W0.e.f2683b.contains(Integer.valueOf(jVar.s0()));
        }
        jVar.C0(0);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static f0.e h(U0.b bVar, O0.j jVar, W0.c cVar) {
        if (jVar == null || jVar.D() == D0.c.f151d) {
            return f0.e.UNSET;
        }
        if (cVar.b(jVar.D())) {
            return f0.e.c(f(bVar.t(), jVar) || cVar.d(jVar, bVar.t(), bVar.r()));
        }
        return f0.e.NO;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        this.f6179c.b(new a(interfaceC0354n, f0Var, this.f6180d, this.f6181e), f0Var);
    }
}
