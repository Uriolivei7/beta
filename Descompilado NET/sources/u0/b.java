package U0;

import I0.f;
import I0.g;
import I0.h;
import J0.EnumC0180n;
import X.e;
import X.i;
import X.k;
import android.net.Uri;
import android.os.Build;
import b1.C0315a;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    public static final e f2380A = new a();

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private static boolean f2381y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private static boolean f2382z;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f2383a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final EnumC0034b f2384b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Uri f2385c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f2386d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private File f2387e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final boolean f2388f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final boolean f2389g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final boolean f2390h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final I0.d f2391i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final g f2392j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final h f2393k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final I0.b f2394l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final f f2395m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final c f2396n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    protected int f2397o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final boolean f2398p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final boolean f2399q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final Boolean f2400r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final d f2401s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final Q0.e f2402t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private final Boolean f2403u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private final EnumC0180n f2404v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private final String f2405w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private final int f2406x;

    class a implements e {
        a() {
        }

        @Override // X.e
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public Uri a(b bVar) {
            if (bVar != null) {
                return bVar.v();
            }
            return null;
        }
    }

    /* JADX INFO: renamed from: U0.b$b, reason: collision with other inner class name */
    public enum EnumC0034b {
        SMALL,
        DEFAULT,
        DYNAMIC
    }

    public enum c {
        FULL_FETCH(1),
        DISK_CACHE(2),
        ENCODED_MEMORY_CACHE(3),
        BITMAP_MEMORY_CACHE(4);


        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f2416b;

        c(int i3) {
            this.f2416b = i3;
        }

        public static c a(c cVar, c cVar2) {
            return cVar.b() > cVar2.b() ? cVar : cVar2;
        }

        public int b() {
            return this.f2416b;
        }
    }

    protected b(U0.c cVar) {
        this.f2384b = cVar.d();
        Uri uriR = cVar.r();
        this.f2385c = uriR;
        this.f2386d = x(uriR);
        this.f2388f = cVar.w();
        this.f2389g = cVar.u();
        this.f2390h = cVar.j();
        this.f2391i = cVar.i();
        this.f2392j = cVar.o();
        this.f2393k = cVar.q() == null ? h.d() : cVar.q();
        this.f2394l = cVar.c();
        this.f2395m = cVar.n();
        this.f2396n = cVar.k();
        boolean zT = cVar.t();
        this.f2398p = zT;
        int iE = cVar.e();
        this.f2397o = zT ? iE : iE | 48;
        this.f2399q = cVar.v();
        this.f2400r = cVar.S();
        this.f2401s = cVar.l();
        this.f2402t = cVar.m();
        this.f2403u = cVar.p();
        this.f2404v = cVar.h();
        this.f2406x = cVar.f();
        this.f2405w = cVar.g();
    }

    public static b a(Uri uri) {
        if (uri == null) {
            return null;
        }
        return U0.c.x(uri).a();
    }

    private static int x(Uri uri) {
        if (uri == null) {
            return -1;
        }
        if (f0.f.o(uri)) {
            return 0;
        }
        if (uri.getPath() != null && f0.f.m(uri)) {
            return Z.a.c(Z.a.b(uri.getPath())) ? 2 : 3;
        }
        if (f0.f.l(uri)) {
            return 4;
        }
        if (f0.f.i(uri)) {
            return 5;
        }
        if (f0.f.n(uri)) {
            return 6;
        }
        if (f0.f.h(uri)) {
            return 7;
        }
        return f0.f.p(uri) ? 8 : -1;
    }

    public I0.b b() {
        return this.f2394l;
    }

    public EnumC0034b c() {
        return this.f2384b;
    }

    public int d() {
        return this.f2397o;
    }

    public int e() {
        return this.f2406x;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof b)) {
            return false;
        }
        b bVar = (b) obj;
        if (f2381y) {
            int i3 = this.f2383a;
            int i4 = bVar.f2383a;
            if (i3 != 0 && i4 != 0 && i3 != i4) {
                return false;
            }
        }
        if (this.f2389g != bVar.f2389g || this.f2398p != bVar.f2398p || this.f2399q != bVar.f2399q || !i.a(this.f2385c, bVar.f2385c) || !i.a(this.f2384b, bVar.f2384b) || !i.a(this.f2405w, bVar.f2405w) || !i.a(this.f2387e, bVar.f2387e) || !i.a(this.f2394l, bVar.f2394l) || !i.a(this.f2391i, bVar.f2391i) || !i.a(this.f2392j, bVar.f2392j) || !i.a(this.f2395m, bVar.f2395m) || !i.a(this.f2396n, bVar.f2396n) || !i.a(Integer.valueOf(this.f2397o), Integer.valueOf(bVar.f2397o)) || !i.a(this.f2400r, bVar.f2400r) || !i.a(this.f2403u, bVar.f2403u) || !i.a(this.f2404v, bVar.f2404v) || !i.a(this.f2393k, bVar.f2393k) || this.f2390h != bVar.f2390h) {
            return false;
        }
        d dVar = this.f2401s;
        R.d dVarB = dVar != null ? dVar.b() : null;
        d dVar2 = bVar.f2401s;
        return i.a(dVarB, dVar2 != null ? dVar2.b() : null) && this.f2406x == bVar.f2406x;
    }

    public String f() {
        return this.f2405w;
    }

    public EnumC0180n g() {
        return this.f2404v;
    }

    public I0.d h() {
        return this.f2391i;
    }

    public int hashCode() {
        boolean z3 = f2382z;
        int iA = z3 ? this.f2383a : 0;
        if (iA == 0) {
            d dVar = this.f2401s;
            iA = C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(C0315a.a(0, this.f2384b), this.f2385c), Boolean.valueOf(this.f2389g)), this.f2394l), this.f2395m), this.f2396n), Integer.valueOf(this.f2397o)), Boolean.valueOf(this.f2398p)), Boolean.valueOf(this.f2399q)), this.f2391i), this.f2400r), this.f2392j), this.f2393k), dVar != null ? dVar.b() : null), this.f2403u), this.f2404v), Integer.valueOf(this.f2406x)), Boolean.valueOf(this.f2390h));
            if (z3) {
                this.f2383a = iA;
            }
        }
        return iA;
    }

    public boolean i() {
        return Build.VERSION.SDK_INT >= 29 && this.f2390h;
    }

    public boolean j() {
        return this.f2389g;
    }

    public c k() {
        return this.f2396n;
    }

    public d l() {
        return this.f2401s;
    }

    public int m() {
        g gVar = this.f2392j;
        if (gVar != null) {
            return gVar.f422b;
        }
        return 2048;
    }

    public int n() {
        g gVar = this.f2392j;
        if (gVar != null) {
            return gVar.f421a;
        }
        return 2048;
    }

    public f o() {
        return this.f2395m;
    }

    public boolean p() {
        return this.f2388f;
    }

    public Q0.e q() {
        return this.f2402t;
    }

    public g r() {
        return this.f2392j;
    }

    public Boolean s() {
        return this.f2403u;
    }

    public h t() {
        return this.f2393k;
    }

    public String toString() {
        return i.b(this).b("uri", this.f2385c).b("cacheChoice", this.f2384b).b("decodeOptions", this.f2391i).b("postprocessor", this.f2401s).b("priority", this.f2395m).b("resizeOptions", this.f2392j).b("rotationOptions", this.f2393k).b("bytesRange", this.f2394l).b("resizingAllowedOverride", this.f2403u).b("downsampleOverride", this.f2404v).c("progressiveRenderingEnabled", this.f2388f).c("localThumbnailPreviewsEnabled", this.f2389g).c("loadThumbnailOnly", this.f2390h).b("lowestPermittedRequestLevel", this.f2396n).a("cachesDisabled", this.f2397o).c("isDiskCacheEnabled", this.f2398p).c("isMemoryCacheEnabled", this.f2399q).b("decodePrefetches", this.f2400r).a("delayMs", this.f2406x).toString();
    }

    public synchronized File u() {
        try {
            if (this.f2387e == null) {
                k.g(this.f2385c.getPath());
                this.f2387e = new File(this.f2385c.getPath());
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.f2387e;
    }

    public Uri v() {
        return this.f2385c;
    }

    public int w() {
        return this.f2386d;
    }

    public boolean y(int i3) {
        return (i3 & d()) == 0;
    }

    public Boolean z() {
        return this.f2400r;
    }
}
