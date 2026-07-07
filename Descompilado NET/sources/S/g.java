package S;

import R.a;
import R.c;
import S.f;
import c0.C0327a;
import e0.InterfaceC0522a;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class g implements k, U.a {

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private static final Class f2254r = g.class;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private static final long f2255s = TimeUnit.HOURS.toMillis(2);

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private static final long f2256t = TimeUnit.MINUTES.toMillis(30);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final long f2257a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final long f2258b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final CountDownLatch f2259c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f2260d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final R.c f2261e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    final Set f2262f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private long f2263g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final long f2264h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final C0327a f2265i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final f f2266j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final j f2267k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final R.a f2268l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final boolean f2269m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final b f2270n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final InterfaceC0522a f2271o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final Object f2272p = new Object();

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private boolean f2273q;

    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (g.this.f2272p) {
                g.this.p();
            }
            g.this.f2273q = true;
            g.this.f2259c.countDown();
        }
    }

    static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean f2275a = false;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private long f2276b = -1;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private long f2277c = -1;

        b() {
        }

        public synchronized long a() {
            return this.f2277c;
        }

        public synchronized long b() {
            return this.f2276b;
        }

        public synchronized void c(long j3, long j4) {
            if (this.f2275a) {
                this.f2276b += j3;
                this.f2277c += j4;
            }
        }

        public synchronized boolean d() {
            return this.f2275a;
        }

        public synchronized void e() {
            this.f2275a = false;
            this.f2277c = -1L;
            this.f2276b = -1L;
        }

        public synchronized void f(long j3, long j4) {
            this.f2277c = j4;
            this.f2276b = j3;
            this.f2275a = true;
        }
    }

    public static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final long f2278a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final long f2279b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final long f2280c;

        public c(long j3, long j4, long j5) {
            this.f2278a = j3;
            this.f2279b = j4;
            this.f2280c = j5;
        }
    }

    public g(f fVar, j jVar, c cVar, R.c cVar2, R.a aVar, U.b bVar, Executor executor, boolean z3) {
        this.f2257a = cVar.f2279b;
        long j3 = cVar.f2280c;
        this.f2258b = j3;
        this.f2260d = j3;
        this.f2265i = C0327a.d();
        this.f2266j = fVar;
        this.f2267k = jVar;
        this.f2263g = -1L;
        this.f2261e = cVar2;
        this.f2264h = cVar.f2278a;
        this.f2268l = aVar;
        this.f2270n = new b();
        this.f2271o = e0.d.a();
        this.f2269m = z3;
        this.f2262f = new HashSet();
        if (bVar != null) {
            bVar.a(this);
        }
        if (!z3) {
            this.f2259c = new CountDownLatch(0);
        } else {
            this.f2259c = new CountDownLatch(1);
            executor.execute(new a());
        }
    }

    private Q.a l(f.b bVar, R.d dVar, String str) {
        Q.a aVarC;
        synchronized (this.f2272p) {
            aVarC = bVar.c(dVar);
            this.f2262f.add(str);
            this.f2270n.c(aVarC.size(), 1L);
        }
        return aVarC;
    }

    private void m(long j3, c.a aVar) throws IOException {
        try {
            Collection<f.a> collectionN = n(this.f2266j.b());
            long jB = this.f2270n.b();
            long j4 = jB - j3;
            int i3 = 0;
            long j5 = 0;
            for (f.a aVar2 : collectionN) {
                if (j5 > j4) {
                    break;
                }
                long jE = this.f2266j.e(aVar2);
                this.f2262f.remove(aVar2.getId());
                if (jE > 0) {
                    i3++;
                    j5 += jE;
                    l lVarE = l.a().j(aVar2.getId()).g(aVar).i(jE).f(jB - j5).e(j3);
                    R.c cVar = this.f2261e;
                    if (cVar != null) {
                        cVar.a(lVarE);
                    }
                    lVarE.b();
                }
            }
            this.f2270n.c(-j5, -i3);
            this.f2266j.d();
        } catch (IOException e4) {
            this.f2268l.a(a.EnumC0028a.EVICTION, f2254r, "evictAboveSize: " + e4.getMessage(), e4);
            throw e4;
        }
    }

    private Collection n(Collection collection) {
        long jNow = this.f2271o.now() + f2255s;
        ArrayList arrayList = new ArrayList(collection.size());
        ArrayList arrayList2 = new ArrayList(collection.size());
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            f.a aVar = (f.a) it.next();
            if (aVar.a() > jNow) {
                arrayList.add(aVar);
            } else {
                arrayList2.add(aVar);
            }
        }
        Collections.sort(arrayList2, this.f2267k.get());
        arrayList.addAll(arrayList2);
        return arrayList;
    }

    private void o() {
        synchronized (this.f2272p) {
            try {
                boolean zP = p();
                s();
                long jB = this.f2270n.b();
                if (jB > this.f2260d && !zP) {
                    this.f2270n.e();
                    p();
                }
                long j3 = this.f2260d;
                if (jB > j3) {
                    m((j3 * 9) / 10, c.a.CACHE_FULL);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean p() {
        long jNow = this.f2271o.now();
        if (this.f2270n.d()) {
            long j3 = this.f2263g;
            if (j3 != -1 && jNow - j3 <= f2256t) {
                return false;
            }
        }
        return q();
    }

    private boolean q() {
        long j3;
        long jNow = this.f2271o.now();
        long j4 = f2255s + jNow;
        Set hashSet = (this.f2269m && this.f2262f.isEmpty()) ? this.f2262f : this.f2269m ? new HashSet() : null;
        try {
            long jI = 0;
            long jMax = -1;
            int i3 = 0;
            boolean z3 = false;
            int i4 = 0;
            int i5 = 0;
            for (f.a aVar : this.f2266j.b()) {
                i4++;
                jI += aVar.i();
                if (aVar.a() > j4) {
                    i5++;
                    i3 = (int) (((long) i3) + aVar.i());
                    j3 = j4;
                    jMax = Math.max(aVar.a() - jNow, jMax);
                    z3 = true;
                } else {
                    j3 = j4;
                    if (this.f2269m) {
                        X.k.g(hashSet);
                        hashSet.add(aVar.getId());
                    }
                }
                j4 = j3;
            }
            if (z3) {
                this.f2268l.a(a.EnumC0028a.READ_INVALID_ENTRY, f2254r, "Future timestamp found in " + i5 + " files , with a total size of " + i3 + " bytes, and a maximum time delta of " + jMax + "ms", null);
            }
            long j5 = i4;
            if (this.f2270n.a() != j5 || this.f2270n.b() != jI) {
                if (this.f2269m && this.f2262f != hashSet) {
                    X.k.g(hashSet);
                    this.f2262f.clear();
                    this.f2262f.addAll(hashSet);
                }
                this.f2270n.f(jI, j5);
            }
            this.f2263g = jNow;
            return true;
        } catch (IOException e4) {
            this.f2268l.a(a.EnumC0028a.GENERIC_IO, f2254r, "calcFileCacheSize: " + e4.getMessage(), e4);
            return false;
        }
    }

    private f.b r(String str, R.d dVar) {
        o();
        return this.f2266j.f(str, dVar);
    }

    private void s() {
        if (this.f2265i.f(this.f2266j.c() ? C0327a.EnumC0088a.EXTERNAL : C0327a.EnumC0088a.INTERNAL, this.f2258b - this.f2270n.b())) {
            this.f2260d = this.f2257a;
        } else {
            this.f2260d = this.f2258b;
        }
    }

    @Override // S.k
    public void a() {
        synchronized (this.f2272p) {
            try {
                this.f2266j.a();
                this.f2262f.clear();
                R.c cVar = this.f2261e;
                if (cVar != null) {
                    cVar.e();
                }
            } catch (IOException | NullPointerException e4) {
                this.f2268l.a(a.EnumC0028a.EVICTION, f2254r, "clearAll: " + e4.getMessage(), e4);
            }
            this.f2270n.e();
        }
    }

    @Override // S.k
    public boolean b(R.d dVar) {
        synchronized (this.f2272p) {
            try {
                List listB = R.e.b(dVar);
                for (int i3 = 0; i3 < listB.size(); i3++) {
                    if (this.f2262f.contains((String) listB.get(i3))) {
                        return true;
                    }
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // S.k
    public Q.a c(R.d dVar, R.j jVar) {
        String strA;
        l lVarD = l.a().d(dVar);
        R.c cVar = this.f2261e;
        if (cVar != null) {
            cVar.h(lVarD);
        }
        synchronized (this.f2272p) {
            strA = R.e.a(dVar);
        }
        lVarD.j(strA);
        try {
            try {
                f.b bVarR = r(strA, dVar);
                try {
                    bVarR.b(jVar, dVar);
                    Q.a aVarL = l(bVarR, dVar, strA);
                    lVarD.i(aVarL.size()).f(this.f2270n.b());
                    R.c cVar2 = this.f2261e;
                    if (cVar2 != null) {
                        cVar2.b(lVarD);
                    }
                    return aVarL;
                } finally {
                    if (!bVarR.a()) {
                        Y.a.i(f2254r, "Failed to delete temp file");
                    }
                }
            } catch (IOException e4) {
                lVarD.h(e4);
                R.c cVar3 = this.f2261e;
                if (cVar3 != null) {
                    cVar3.f(lVarD);
                }
                Y.a.j(f2254r, "Failed inserting a file into the cache", e4);
                throw e4;
            }
        } finally {
            lVarD.b();
        }
    }

    @Override // S.k
    public Q.a d(R.d dVar) {
        Q.a aVarJ;
        l lVarD = l.a().d(dVar);
        try {
            synchronized (this.f2272p) {
                try {
                    List listB = R.e.b(dVar);
                    String str = null;
                    aVarJ = null;
                    for (int i3 = 0; i3 < listB.size(); i3++) {
                        str = (String) listB.get(i3);
                        lVarD.j(str);
                        aVarJ = this.f2266j.j(str, dVar);
                        if (aVarJ != null) {
                            break;
                        }
                    }
                    if (aVarJ == null) {
                        R.c cVar = this.f2261e;
                        if (cVar != null) {
                            cVar.d(lVarD);
                        }
                        this.f2262f.remove(str);
                    } else {
                        X.k.g(str);
                        R.c cVar2 = this.f2261e;
                        if (cVar2 != null) {
                            cVar2.g(lVarD);
                        }
                        this.f2262f.add(str);
                    }
                } finally {
                }
            }
            return aVarJ;
        } catch (IOException e4) {
            this.f2268l.a(a.EnumC0028a.GENERIC_IO, f2254r, "getResource", e4);
            lVarD.h(e4);
            R.c cVar3 = this.f2261e;
            if (cVar3 != null) {
                cVar3.c(lVarD);
            }
            return null;
        } finally {
            lVarD.b();
        }
    }

    @Override // S.k
    public boolean e(R.d dVar) throws Throwable {
        String str;
        IOException e4;
        String str2 = null;
        try {
            try {
                synchronized (this.f2272p) {
                    try {
                        List listB = R.e.b(dVar);
                        int i3 = 0;
                        while (i3 < listB.size()) {
                            String str3 = (String) listB.get(i3);
                            if (this.f2266j.g(str3, dVar)) {
                                this.f2262f.add(str3);
                                return true;
                            }
                            i3++;
                            str2 = str3;
                        }
                        return false;
                    } catch (Throwable th) {
                        str = str2;
                        th = th;
                        try {
                            throw th;
                        } catch (IOException e5) {
                            e4 = e5;
                            l lVarH = l.a().d(dVar).j(str).h(e4);
                            R.c cVar = this.f2261e;
                            if (cVar != null) {
                                cVar.c(lVarH);
                            }
                            lVarH.b();
                            return false;
                        }
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e6) {
            str = null;
            e4 = e6;
        }
    }

    @Override // S.k
    public boolean f(R.d dVar) {
        synchronized (this.f2272p) {
            if (b(dVar)) {
                return true;
            }
            try {
                List listB = R.e.b(dVar);
                for (int i3 = 0; i3 < listB.size(); i3++) {
                    String str = (String) listB.get(i3);
                    if (this.f2266j.i(str, dVar)) {
                        this.f2262f.add(str);
                        return true;
                    }
                }
                return false;
            } catch (IOException unused) {
                return false;
            }
        }
    }

    @Override // S.k
    public void g(R.d dVar) {
        synchronized (this.f2272p) {
            try {
                List listB = R.e.b(dVar);
                for (int i3 = 0; i3 < listB.size(); i3++) {
                    String str = (String) listB.get(i3);
                    this.f2266j.h(str);
                    this.f2262f.remove(str);
                }
            } catch (IOException e4) {
                this.f2268l.a(a.EnumC0028a.DELETE_FILE, f2254r, "delete: " + e4.getMessage(), e4);
            }
        }
    }
}
