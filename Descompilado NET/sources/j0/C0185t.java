package J0;

import U0.b;
import android.net.Uri;
import android.os.StrictMode;
import com.facebook.imagepipeline.producers.e0;
import com.facebook.imagepipeline.producers.m0;
import com.facebook.imagepipeline.producers.q0;
import h0.AbstractC0549d;
import h0.InterfaceC0548c;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;

/* JADX INFO: renamed from: J0.t, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0185t {

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public static final a f604n = new a(null);

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final CancellationException f605o = new CancellationException("Prefetching is not enabled");

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private static final CancellationException f606p = new CancellationException("ImageRequest is null");

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private static final CancellationException f607q = new CancellationException("Modified URL is null");

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final W f608a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final X.n f609b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final X.n f610c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Q0.e f611d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Q0.d f612e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final H0.x f613f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final H0.x f614g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final H0.k f615h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final q0 f616i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final X.n f617j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final AtomicLong f618k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final X.n f619l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final InterfaceC0187v f620m;

    /* JADX INFO: renamed from: J0.t$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX INFO: renamed from: J0.t$b */
    public /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f621a;

        static {
            int[] iArr = new int[b.EnumC0034b.values().length];
            try {
                iArr[b.EnumC0034b.DEFAULT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[b.EnumC0034b.SMALL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[b.EnumC0034b.DYNAMIC.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f621a = iArr;
        }
    }

    public C0185t(W w3, Set<? extends Q0.e> set, Set<? extends Q0.d> set2, X.n nVar, H0.x xVar, H0.x xVar2, X.n nVar2, H0.k kVar, q0 q0Var, X.n nVar3, X.n nVar4, T.a aVar, InterfaceC0187v interfaceC0187v) {
        D2.h.f(w3, "producerSequenceFactory");
        D2.h.f(set, "requestListeners");
        D2.h.f(set2, "requestListener2s");
        D2.h.f(nVar, "isPrefetchEnabledSupplier");
        D2.h.f(xVar, "bitmapMemoryCache");
        D2.h.f(xVar2, "encodedMemoryCache");
        D2.h.f(nVar2, "diskCachesStoreSupplier");
        D2.h.f(kVar, "cacheKeyFactory");
        D2.h.f(q0Var, "threadHandoffProducerQueue");
        D2.h.f(nVar3, "suppressBitmapPrefetchingSupplier");
        D2.h.f(nVar4, "lazyDataSource");
        D2.h.f(interfaceC0187v, "config");
        this.f608a = w3;
        this.f609b = nVar;
        this.f610c = nVar2;
        this.f611d = new Q0.c((Set<Q0.e>) set);
        this.f612e = new Q0.b(set2);
        this.f618k = new AtomicLong();
        this.f613f = xVar;
        this.f614g = xVar2;
        this.f615h = kVar;
        this.f616i = q0Var;
        this.f617j = nVar3;
        this.f619l = nVar4;
        this.f620m = interfaceC0187v;
    }

    private final InterfaceC0548c A(e0 e0Var, U0.b bVar, b.c cVar, Object obj, Q0.e eVar, String str) {
        return B(e0Var, bVar, cVar, obj, eVar, str, null);
    }

    private final InterfaceC0548c B(e0 e0Var, U0.b bVar, b.c cVar, Object obj, Q0.e eVar, String str, Map map) {
        InterfaceC0548c interfaceC0548cB;
        if (!V0.b.d()) {
            com.facebook.imagepipeline.producers.G g3 = new com.facebook.imagepipeline.producers.G(q(bVar, eVar), this.f612e);
            try {
                b.c cVarA = b.c.a(bVar.k(), cVar);
                D2.h.e(cVarA, "getMax(...)");
                m0 m0Var = new m0(bVar, n(), str, g3, obj, cVarA, false, bVar.p() || !f0.f.o(bVar.v()), bVar.o(), this.f620m);
                m0Var.q(map);
                return K0.b.I(e0Var, m0Var, g3);
            } catch (Exception e4) {
                return AbstractC0549d.b(e4);
            }
        }
        V0.b.a("ImagePipeline#submitFetchRequest");
        try {
            com.facebook.imagepipeline.producers.G g4 = new com.facebook.imagepipeline.producers.G(q(bVar, eVar), this.f612e);
            try {
                b.c cVarA2 = b.c.a(bVar.k(), cVar);
                D2.h.e(cVarA2, "getMax(...)");
                m0 m0Var2 = new m0(bVar, n(), str, g4, obj, cVarA2, false, bVar.p() || !f0.f.o(bVar.v()), bVar.o(), this.f620m);
                m0Var2.q(map);
                interfaceC0548cB = K0.b.I(e0Var, m0Var2, g4);
            } catch (Exception e5) {
                interfaceC0548cB = AbstractC0549d.b(e5);
            }
            V0.b.b();
            return interfaceC0548cB;
        } catch (Throwable th) {
            V0.b.b();
            throw th;
        }
    }

    private final InterfaceC0548c C(e0 e0Var, U0.b bVar, b.c cVar, Object obj, I0.f fVar, Q0.e eVar) {
        U0.b bVarA = bVar;
        com.facebook.imagepipeline.producers.G g3 = new com.facebook.imagepipeline.producers.G(q(bVar, eVar), this.f612e);
        Uri uriV = bVar.v();
        D2.h.e(uriV, "getSourceUri(...)");
        Uri uriA = A0.b.f31b.a(uriV, obj);
        if (uriA == null) {
            InterfaceC0548c interfaceC0548cB = AbstractC0549d.b(f607q);
            D2.h.e(interfaceC0548cB, "immediateFailedDataSource(...)");
            return interfaceC0548cB;
        }
        if (!D2.h.b(uriV, uriA)) {
            bVarA = U0.c.b(bVar).R(uriA).a();
        }
        U0.b bVar2 = bVarA;
        try {
            b.c cVarA = b.c.a(bVar2.k(), cVar);
            D2.h.e(cVarA, "getMax(...)");
            String strN = n();
            x xVarG = this.f620m.G();
            return K0.c.f811j.a(e0Var, new m0(bVar2, strN, g3, obj, cVarA, true, xVarG != null && xVarG.b() && bVar2.p(), fVar, this.f620m), g3);
        } catch (Exception e4) {
            return AbstractC0549d.b(e4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean f(R.d dVar) {
        D2.h.f(dVar, "it");
        return true;
    }

    public static /* synthetic */ InterfaceC0548c m(C0185t c0185t, U0.b bVar, Object obj, b.c cVar, Q0.e eVar, String str, int i3, Object obj2) {
        return c0185t.l(bVar, obj, (i3 & 4) != 0 ? null : cVar, (i3 & 8) != 0 ? null : eVar, (i3 & 16) != 0 ? null : str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final boolean v(U0.b bVar) {
        Object obj = this.f610c.get();
        D2.h.e(obj, "get(...)");
        InterfaceC0169c interfaceC0169c = (InterfaceC0169c) obj;
        R.d dVarC = this.f615h.c(bVar, null);
        String strF = bVar.f();
        if (strF != null) {
            H0.j jVar = (H0.j) interfaceC0169c.b().get(strF);
            if (jVar == null) {
                return false;
            }
            D2.h.c(dVarC);
            return jVar.k(dVarC);
        }
        Iterator it = interfaceC0169c.b().entrySet().iterator();
        while (it.hasNext()) {
            H0.j jVar2 = (H0.j) ((Map.Entry) it.next()).getValue();
            D2.h.c(dVarC);
            if (jVar2.k(dVarC)) {
                return true;
            }
        }
        return false;
    }

    private final X.l w(final Uri uri) {
        return new X.l() { // from class: J0.r
            @Override // X.l
            public final boolean a(Object obj) {
                return C0185t.x(uri, (R.d) obj);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean x(Uri uri, R.d dVar) {
        D2.h.f(uri, "$uri");
        D2.h.f(dVar, "key");
        return dVar.b(uri);
    }

    public final void c() {
        e();
        d();
    }

    public final void d() {
        Object obj = this.f610c.get();
        D2.h.e(obj, "get(...)");
        InterfaceC0169c interfaceC0169c = (InterfaceC0169c) obj;
        interfaceC0169c.a().h();
        interfaceC0169c.c().h();
        Iterator it = interfaceC0169c.b().entrySet().iterator();
        while (it.hasNext()) {
            ((H0.j) ((Map.Entry) it.next()).getValue()).h();
        }
    }

    public final void e() {
        X.l lVar = new X.l() { // from class: J0.s
            @Override // X.l
            public final boolean a(Object obj) {
                return C0185t.f((R.d) obj);
            }
        };
        this.f613f.e(lVar);
        this.f614g.e(lVar);
    }

    public final void g(Uri uri) {
        D2.h.f(uri, "uri");
        j(uri);
        i(uri);
    }

    public final void h(U0.b bVar) {
        if (bVar == null) {
            return;
        }
        R.d dVarC = this.f615h.c(bVar, null);
        Object obj = this.f610c.get();
        D2.h.e(obj, "get(...)");
        InterfaceC0169c interfaceC0169c = (InterfaceC0169c) obj;
        H0.j jVarA = interfaceC0169c.a();
        D2.h.c(dVarC);
        jVarA.s(dVarC);
        interfaceC0169c.c().s(dVarC);
        Iterator it = interfaceC0169c.b().entrySet().iterator();
        while (it.hasNext()) {
            ((H0.j) ((Map.Entry) it.next()).getValue()).s(dVarC);
        }
    }

    public final void i(Uri uri) {
        U0.b bVarA = U0.b.a(uri);
        if (bVarA == null) {
            throw new IllegalStateException("Required value was null.");
        }
        h(bVarA);
    }

    public final void j(Uri uri) {
        D2.h.f(uri, "uri");
        X.l lVarW = w(uri);
        this.f613f.e(lVarW);
        this.f614g.e(lVarW);
    }

    public final InterfaceC0548c k(U0.b bVar, Object obj) {
        return m(this, bVar, obj, null, null, null, 24, null);
    }

    public final InterfaceC0548c l(U0.b bVar, Object obj, b.c cVar, Q0.e eVar, String str) {
        if (bVar == null) {
            InterfaceC0548c interfaceC0548cB = AbstractC0549d.b(new NullPointerException());
            D2.h.e(interfaceC0548cB, "immediateFailedDataSource(...)");
            return interfaceC0548cB;
        }
        try {
            e0 e0VarE = this.f608a.E(bVar);
            if (cVar == null) {
                cVar = b.c.FULL_FETCH;
            }
            return A(e0VarE, bVar, cVar, obj, eVar, str);
        } catch (Exception e4) {
            return AbstractC0549d.b(e4);
        }
    }

    public final String n() {
        return String.valueOf(this.f618k.getAndIncrement());
    }

    public final H0.x o() {
        return this.f613f;
    }

    public final H0.k p() {
        return this.f615h;
    }

    public final Q0.e q(U0.b bVar, Q0.e eVar) {
        if (bVar != null) {
            return eVar == null ? bVar.q() == null ? this.f611d : new Q0.c(this.f611d, bVar.q()) : bVar.q() == null ? new Q0.c(this.f611d, eVar) : new Q0.c(this.f611d, eVar, bVar.q());
        }
        throw new IllegalStateException("Required value was null.");
    }

    public final boolean r(Uri uri) {
        if (uri == null) {
            return false;
        }
        return this.f613f.d(w(uri));
    }

    public final boolean s(U0.b bVar) {
        boolean zK;
        D2.h.f(bVar, "imageRequest");
        Object obj = this.f610c.get();
        D2.h.e(obj, "get(...)");
        InterfaceC0169c interfaceC0169c = (InterfaceC0169c) obj;
        R.d dVarC = this.f615h.c(bVar, null);
        b.EnumC0034b enumC0034bC = bVar.c();
        D2.h.e(enumC0034bC, "getCacheChoice(...)");
        StrictMode.ThreadPolicy threadPolicyAllowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            int i3 = b.f621a[enumC0034bC.ordinal()];
            if (i3 == 1) {
                H0.j jVarA = interfaceC0169c.a();
                D2.h.c(dVarC);
                zK = jVarA.k(dVarC);
            } else if (i3 == 2) {
                H0.j jVarC = interfaceC0169c.c();
                D2.h.c(dVarC);
                zK = jVarC.k(dVarC);
            } else {
                if (i3 != 3) {
                    throw new C0685h();
                }
                zK = v(bVar);
            }
            StrictMode.setThreadPolicy(threadPolicyAllowThreadDiskReads);
            return zK;
        } catch (Throwable th) {
            StrictMode.setThreadPolicy(threadPolicyAllowThreadDiskReads);
            throw th;
        }
    }

    public final boolean t(Uri uri) {
        return u(uri, b.EnumC0034b.SMALL) || u(uri, b.EnumC0034b.DEFAULT) || u(uri, b.EnumC0034b.DYNAMIC);
    }

    public final boolean u(Uri uri, b.EnumC0034b enumC0034b) {
        U0.b bVarA = U0.c.x(uri).A(enumC0034b).a();
        D2.h.c(bVarA);
        return s(bVarA);
    }

    public final InterfaceC0548c y(U0.b bVar, Object obj) {
        return z(bVar, obj, I0.f.f416d, null);
    }

    public final InterfaceC0548c z(U0.b bVar, Object obj, I0.f fVar, Q0.e eVar) {
        D2.h.f(fVar, "priority");
        if (!((Boolean) this.f609b.get()).booleanValue()) {
            InterfaceC0548c interfaceC0548cB = AbstractC0549d.b(f605o);
            D2.h.e(interfaceC0548cB, "immediateFailedDataSource(...)");
            return interfaceC0548cB;
        }
        if (bVar == null) {
            InterfaceC0548c interfaceC0548cB2 = AbstractC0549d.b(new NullPointerException("imageRequest is null"));
            D2.h.c(interfaceC0548cB2);
            return interfaceC0548cB2;
        }
        try {
            return C(this.f608a.G(bVar), bVar, b.c.FULL_FETCH, obj, fVar, eVar);
        } catch (Exception e4) {
            return AbstractC0549d.b(e4);
        }
    }
}
