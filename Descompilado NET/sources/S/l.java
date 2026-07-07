package S;

import R.c;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class l implements R.b {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final Object f2289i = new Object();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static l f2290j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static int f2291k;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private R.d f2292a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f2293b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private long f2294c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f2295d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private long f2296e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private IOException f2297f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private c.a f2298g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private l f2299h;

    private l() {
    }

    public static l a() {
        synchronized (f2289i) {
            try {
                l lVar = f2290j;
                if (lVar == null) {
                    return new l();
                }
                f2290j = lVar.f2299h;
                lVar.f2299h = null;
                f2291k--;
                return lVar;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void c() {
        this.f2292a = null;
        this.f2293b = null;
        this.f2294c = 0L;
        this.f2295d = 0L;
        this.f2296e = 0L;
        this.f2297f = null;
        this.f2298g = null;
    }

    public void b() {
        synchronized (f2289i) {
            try {
                if (f2291k < 5) {
                    c();
                    f2291k++;
                    l lVar = f2290j;
                    if (lVar != null) {
                        this.f2299h = lVar;
                    }
                    f2290j = this;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public l d(R.d dVar) {
        this.f2292a = dVar;
        return this;
    }

    public l e(long j3) {
        this.f2295d = j3;
        return this;
    }

    public l f(long j3) {
        this.f2296e = j3;
        return this;
    }

    public l g(c.a aVar) {
        this.f2298g = aVar;
        return this;
    }

    public l h(IOException iOException) {
        this.f2297f = iOException;
        return this;
    }

    public l i(long j3) {
        this.f2294c = j3;
        return this;
    }

    public l j(String str) {
        this.f2293b = str;
        return this;
    }
}
