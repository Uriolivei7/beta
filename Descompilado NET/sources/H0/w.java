package H0;

import H0.n;
import H0.x;
import android.os.SystemClock;
import b0.AbstractC0306a;
import b0.InterfaceC0313h;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
public class w implements n, x {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final m f317a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final m f318b;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final D f320d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final x.a f321e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final X.n f322f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    protected y f323g;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final boolean f325i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final boolean f326j;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final Map f319c = new WeakHashMap();

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private long f324h = SystemClock.uptimeMillis();

    class a implements D {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ D f327a;

        a(D d4) {
            this.f327a = d4;
        }

        @Override // H0.D
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public int a(n.a aVar) {
            return w.this.f325i ? aVar.f307f : this.f327a.a(aVar.f303b.P());
        }
    }

    class b implements InterfaceC0313h {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ n.a f329a;

        b(n.a aVar) {
            this.f329a = aVar;
        }

        @Override // b0.InterfaceC0313h
        public void a(Object obj) {
            w.this.y(this.f329a);
        }
    }

    public w(D d4, x.a aVar, X.n nVar, n.b bVar, boolean z3, boolean z4) {
        this.f320d = d4;
        this.f317a = new m(A(d4));
        this.f318b = new m(A(d4));
        this.f321e = aVar;
        this.f322f = nVar;
        this.f323g = (y) X.k.h((y) nVar.get(), "mMemoryCacheParamsSupplier returned null");
        this.f325i = z3;
        this.f326j = z4;
    }

    private D A(D d4) {
        return new a(d4);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0021  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private synchronized boolean i(int r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            H0.y r0 = r3.f323g     // Catch: java.lang.Throwable -> L1f
            int r0 = r0.f335e     // Catch: java.lang.Throwable -> L1f
            if (r4 > r0) goto L21
            int r0 = r3.k()     // Catch: java.lang.Throwable -> L1f
            H0.y r1 = r3.f323g     // Catch: java.lang.Throwable -> L1f
            int r1 = r1.f332b     // Catch: java.lang.Throwable -> L1f
            r2 = 1
            int r1 = r1 - r2
            if (r0 > r1) goto L21
            int r0 = r3.l()     // Catch: java.lang.Throwable -> L1f
            H0.y r1 = r3.f323g     // Catch: java.lang.Throwable -> L1f
            int r1 = r1.f331a     // Catch: java.lang.Throwable -> L1f
            int r1 = r1 - r4
            if (r0 > r1) goto L21
            goto L22
        L1f:
            r4 = move-exception
            goto L24
        L21:
            r2 = 0
        L22:
            monitor-exit(r3)
            return r2
        L24:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L1f
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: H0.w.i(int):boolean");
    }

    private synchronized void j(n.a aVar) {
        X.k.g(aVar);
        X.k.i(aVar.f304c > 0);
        aVar.f304c--;
    }

    private synchronized void m(n.a aVar) {
        X.k.g(aVar);
        X.k.i(!aVar.f305d);
        aVar.f304c++;
    }

    private synchronized void n(n.a aVar) {
        X.k.g(aVar);
        X.k.i(!aVar.f305d);
        aVar.f305d = true;
    }

    private synchronized void o(ArrayList arrayList) {
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                n((n.a) it.next());
            }
        }
    }

    private synchronized boolean p(n.a aVar) {
        if (aVar.f305d || aVar.f304c != 0) {
            return false;
        }
        this.f317a.g(aVar.f302a, aVar);
        return true;
    }

    private void q(ArrayList arrayList) {
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                AbstractC0306a.D(x((n.a) it.next()));
            }
        }
    }

    private void u(ArrayList arrayList) {
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                t((n.a) it.next());
            }
        }
    }

    private synchronized void v() {
        if (this.f324h + this.f323g.f336f > SystemClock.uptimeMillis()) {
            return;
        }
        this.f324h = SystemClock.uptimeMillis();
        this.f323g = (y) X.k.h((y) this.f322f.get(), "mMemoryCacheParamsSupplier returned null");
    }

    private synchronized AbstractC0306a w(n.a aVar) {
        m(aVar);
        return AbstractC0306a.n0(aVar.f303b.P(), new b(aVar));
    }

    private synchronized AbstractC0306a x(n.a aVar) {
        X.k.g(aVar);
        return (aVar.f305d && aVar.f304c == 0) ? aVar.f303b : null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void y(n.a aVar) {
        boolean zP;
        AbstractC0306a abstractC0306aX;
        X.k.g(aVar);
        synchronized (this) {
            j(aVar);
            zP = p(aVar);
            abstractC0306aX = x(aVar);
        }
        AbstractC0306a.D(abstractC0306aX);
        if (!zP) {
            aVar = null;
        }
        s(aVar);
        v();
        r();
    }

    private synchronized ArrayList z(int i3, int i4) {
        int iMax = Math.max(i3, 0);
        int iMax2 = Math.max(i4, 0);
        if (this.f317a.b() <= iMax && this.f317a.e() <= iMax2) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        while (true) {
            if (this.f317a.b() <= iMax && this.f317a.e() <= iMax2) {
                break;
            }
            Object objC = this.f317a.c();
            if (objC != null) {
                this.f317a.h(objC);
                arrayList.add((n.a) this.f318b.h(objC));
            } else {
                if (!this.f326j) {
                    throw new IllegalStateException(String.format("key is null, but exclusiveEntries count: %d, size: %d", Integer.valueOf(this.f317a.b()), Integer.valueOf(this.f317a.e())));
                }
                this.f317a.j();
            }
        }
        return arrayList;
    }

    @Override // H0.x
    public AbstractC0306a b(Object obj, AbstractC0306a abstractC0306a) {
        return h(obj, abstractC0306a, null);
    }

    @Override // H0.x
    public void c(Object obj) {
        X.k.g(obj);
        synchronized (this) {
            try {
                n.a aVar = (n.a) this.f317a.h(obj);
                if (aVar != null) {
                    this.f317a.g(obj, aVar);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // H0.x
    public synchronized boolean d(X.l lVar) {
        return !this.f318b.d(lVar).isEmpty();
    }

    @Override // H0.x
    public int e(X.l lVar) {
        ArrayList arrayListI;
        ArrayList arrayListI2;
        synchronized (this) {
            arrayListI = this.f317a.i(lVar);
            arrayListI2 = this.f318b.i(lVar);
            o(arrayListI2);
        }
        q(arrayListI2);
        u(arrayListI);
        v();
        r();
        return arrayListI2.size();
    }

    @Override // H0.x
    public AbstractC0306a get(Object obj) {
        n.a aVar;
        AbstractC0306a abstractC0306aW;
        X.k.g(obj);
        synchronized (this) {
            try {
                aVar = (n.a) this.f317a.h(obj);
                n.a aVar2 = (n.a) this.f318b.a(obj);
                abstractC0306aW = aVar2 != null ? w(aVar2) : null;
            } catch (Throwable th) {
                throw th;
            }
        }
        t(aVar);
        v();
        r();
        return abstractC0306aW;
    }

    public AbstractC0306a h(Object obj, AbstractC0306a abstractC0306a, n.b bVar) {
        n.a aVar;
        AbstractC0306a abstractC0306aW;
        AbstractC0306a abstractC0306aX;
        X.k.g(obj);
        X.k.g(abstractC0306a);
        v();
        synchronized (this) {
            try {
                aVar = (n.a) this.f317a.h(obj);
                n.a aVar2 = (n.a) this.f318b.h(obj);
                abstractC0306aW = null;
                if (aVar2 != null) {
                    n(aVar2);
                    abstractC0306aX = x(aVar2);
                } else {
                    abstractC0306aX = null;
                }
                int iA = this.f320d.a(abstractC0306a.P());
                if (i(iA)) {
                    n.a aVarA = this.f325i ? n.a.a(obj, abstractC0306a, iA, bVar) : n.a.b(obj, abstractC0306a, bVar);
                    this.f318b.g(obj, aVarA);
                    abstractC0306aW = w(aVarA);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        AbstractC0306a.D(abstractC0306aX);
        t(aVar);
        r();
        return abstractC0306aW;
    }

    public synchronized int k() {
        return this.f318b.b() - this.f317a.b();
    }

    public synchronized int l() {
        return this.f318b.e() - this.f317a.e();
    }

    public void r() {
        ArrayList arrayListZ;
        synchronized (this) {
            y yVar = this.f323g;
            int iMin = Math.min(yVar.f334d, yVar.f332b - k());
            y yVar2 = this.f323g;
            arrayListZ = z(iMin, Math.min(yVar2.f333c, yVar2.f331a - l()));
            o(arrayListZ);
        }
        q(arrayListZ);
        u(arrayListZ);
    }

    private static void s(n.a aVar) {
    }

    private static void t(n.a aVar) {
    }
}
