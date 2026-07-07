package S;

import X.n;
import android.content.Context;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2229a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f2230b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final n f2231c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final long f2232d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final long f2233e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final long f2234f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final j f2235g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final R.a f2236h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final R.c f2237i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final U.b f2238j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final Context f2239k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final boolean f2240l;

    class a implements n {
        a() {
        }

        @Override // X.n
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public File get() {
            X.k.g(d.this.f2239k);
            return d.this.f2239k.getApplicationContext().getCacheDir();
        }
    }

    public static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f2242a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f2243b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private n f2244c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private long f2245d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private long f2246e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private long f2247f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private j f2248g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private R.a f2249h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private R.c f2250i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private U.b f2251j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private boolean f2252k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        private final Context f2253l;

        public d n() {
            return new d(this);
        }

        private b(Context context) {
            this.f2242a = 1;
            this.f2243b = "image_cache";
            this.f2245d = 41943040L;
            this.f2246e = 10485760L;
            this.f2247f = 2097152L;
            this.f2248g = new c();
            this.f2253l = context;
        }
    }

    protected d(b bVar) {
        Context context = bVar.f2253l;
        this.f2239k = context;
        X.k.j((bVar.f2244c == null && context == null) ? false : true, "Either a non-null context or a base directory path or supplier must be provided.");
        if (bVar.f2244c == null && context != null) {
            bVar.f2244c = new a();
        }
        this.f2229a = bVar.f2242a;
        this.f2230b = (String) X.k.g(bVar.f2243b);
        this.f2231c = (n) X.k.g(bVar.f2244c);
        this.f2232d = bVar.f2245d;
        this.f2233e = bVar.f2246e;
        this.f2234f = bVar.f2247f;
        this.f2235g = (j) X.k.g(bVar.f2248g);
        this.f2236h = bVar.f2249h == null ? R.g.b() : bVar.f2249h;
        this.f2237i = bVar.f2250i == null ? R.h.i() : bVar.f2250i;
        this.f2238j = bVar.f2251j == null ? U.c.b() : bVar.f2251j;
        this.f2240l = bVar.f2252k;
    }

    public static b m(Context context) {
        return new b(context);
    }

    public String b() {
        return this.f2230b;
    }

    public n c() {
        return this.f2231c;
    }

    public R.a d() {
        return this.f2236h;
    }

    public R.c e() {
        return this.f2237i;
    }

    public long f() {
        return this.f2232d;
    }

    public U.b g() {
        return this.f2238j;
    }

    public j h() {
        return this.f2235g;
    }

    public boolean i() {
        return this.f2240l;
    }

    public long j() {
        return this.f2233e;
    }

    public long k() {
        return this.f2234f;
    }

    public int l() {
        return this.f2229a;
    }
}
