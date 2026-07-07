package com.facebook.imagepipeline.memory;

import R0.E;
import R0.F;
import X.k;
import X.m;
import X.p;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class a implements a0.f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Class f5917a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final a0.d f5918b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final E f5919c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final SparseArray f5920d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    final Set f5921e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f5922f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    final C0092a f5923g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    final C0092a f5924h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final F f5925i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f5926j;

    /* JADX INFO: renamed from: com.facebook.imagepipeline.memory.a$a, reason: collision with other inner class name */
    static class C0092a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        int f5927a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        int f5928b;

        C0092a() {
        }

        public void a(int i3) {
            int i4;
            int i5 = this.f5928b;
            if (i5 < i3 || (i4 = this.f5927a) <= 0) {
                Y.a.N("com.facebook.imagepipeline.memory.BasePool.Counter", "Unexpected decrement of %d. Current numBytes = %d, count = %d", Integer.valueOf(i3), Integer.valueOf(this.f5928b), Integer.valueOf(this.f5927a));
            } else {
                this.f5927a = i4 - 1;
                this.f5928b = i5 - i3;
            }
        }

        public void b(int i3) {
            this.f5927a++;
            this.f5928b += i3;
        }
    }

    public static class b extends RuntimeException {
        public b(Object obj) {
            super("Invalid size: " + obj.toString());
        }
    }

    public static class c extends RuntimeException {
        public c(int i3, int i4, int i5, int i6) {
            super("Pool hard cap violation? Hard cap = " + i3 + " Used size = " + i4 + " Free size = " + i5 + " Request size = " + i6);
        }
    }

    public a(a0.d dVar, E e4, F f3) {
        this.f5917a = getClass();
        this.f5918b = (a0.d) k.g(dVar);
        E e5 = (E) k.g(e4);
        this.f5919c = e5;
        this.f5925i = (F) k.g(f3);
        this.f5920d = new SparseArray();
        if (e5.f1951f) {
            q();
        } else {
            u(new SparseIntArray(0));
        }
        this.f5921e = m.b();
        this.f5924h = new C0092a();
        this.f5923g = new C0092a();
    }

    private synchronized void h() {
        try {
            k.i(!s() || this.f5924h.f5928b == 0);
        } catch (Throwable th) {
            throw th;
        }
    }

    private void i(SparseIntArray sparseIntArray) {
        this.f5920d.clear();
        for (int i3 = 0; i3 < sparseIntArray.size(); i3++) {
            int iKeyAt = sparseIntArray.keyAt(i3);
            this.f5920d.put(iKeyAt, new com.facebook.imagepipeline.memory.b(o(iKeyAt), sparseIntArray.valueAt(i3), 0, this.f5919c.f1951f));
        }
    }

    private synchronized com.facebook.imagepipeline.memory.b l(int i3) {
        return (com.facebook.imagepipeline.memory.b) this.f5920d.get(i3);
    }

    private synchronized void q() {
        try {
            SparseIntArray sparseIntArray = this.f5919c.f1948c;
            if (sparseIntArray != null) {
                i(sparseIntArray);
                this.f5922f = false;
            } else {
                this.f5922f = true;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void u(SparseIntArray sparseIntArray) {
        try {
            k.g(sparseIntArray);
            this.f5920d.clear();
            SparseIntArray sparseIntArray2 = this.f5919c.f1948c;
            if (sparseIntArray2 != null) {
                for (int i3 = 0; i3 < sparseIntArray2.size(); i3++) {
                    int iKeyAt = sparseIntArray2.keyAt(i3);
                    this.f5920d.put(iKeyAt, new com.facebook.imagepipeline.memory.b(o(iKeyAt), sparseIntArray2.valueAt(i3), sparseIntArray.get(iKeyAt, 0), this.f5919c.f1951f));
                }
                this.f5922f = false;
            } else {
                this.f5922f = true;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void v() {
        if (Y.a.w(2)) {
            Y.a.B(this.f5917a, "Used = (%d, %d); Free = (%d, %d)", Integer.valueOf(this.f5923g.f5927a), Integer.valueOf(this.f5923g.f5928b), Integer.valueOf(this.f5924h.f5927a), Integer.valueOf(this.f5924h.f5928b));
        }
    }

    @Override // a0.f, b0.InterfaceC0313h
    public void a(Object obj) {
        k.g(obj);
        int iN = n(obj);
        int iO = o(iN);
        synchronized (this) {
            try {
                com.facebook.imagepipeline.memory.b bVarL = l(iN);
                if (!this.f5921e.remove(obj)) {
                    Y.a.k(this.f5917a, "release (free, value unrecognized) (object, size) = (%x, %s)", Integer.valueOf(System.identityHashCode(obj)), Integer.valueOf(iN));
                    j(obj);
                    this.f5925i.c(iO);
                } else if (bVarL == null || bVarL.f() || s() || !t(obj)) {
                    if (bVarL != null) {
                        bVarL.b();
                    }
                    if (Y.a.w(2)) {
                        Y.a.z(this.f5917a, "release (free) (object, size) = (%x, %s)", Integer.valueOf(System.identityHashCode(obj)), Integer.valueOf(iN));
                    }
                    j(obj);
                    this.f5923g.a(iO);
                    this.f5925i.c(iO);
                } else {
                    bVarL.h(obj);
                    this.f5924h.b(iO);
                    this.f5923g.a(iO);
                    this.f5925i.e(iO);
                    if (Y.a.w(2)) {
                        Y.a.z(this.f5917a, "release (reuse) (object, size) = (%x, %s)", Integer.valueOf(System.identityHashCode(obj)), Integer.valueOf(iN));
                    }
                }
                v();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected abstract Object f(int i3);

    synchronized boolean g(int i3) {
        if (this.f5926j) {
            return true;
        }
        E e4 = this.f5919c;
        int i4 = e4.f1946a;
        int i5 = this.f5923g.f5928b;
        if (i3 > i4 - i5) {
            this.f5925i.d();
            return false;
        }
        int i6 = e4.f1947b;
        if (i3 > i6 - (i5 + this.f5924h.f5928b)) {
            x(i6 - i3);
        }
        if (i3 <= i4 - (this.f5923g.f5928b + this.f5924h.f5928b)) {
            return true;
        }
        this.f5925i.d();
        return false;
    }

    @Override // a0.f
    public Object get(int i3) throws Throwable {
        Object objF;
        Object objP;
        h();
        int iM = m(i3);
        synchronized (this) {
            try {
                com.facebook.imagepipeline.memory.b bVarK = k(iM);
                if (bVarK != null && (objP = p(bVarK)) != null) {
                    k.i(this.f5921e.add(objP));
                    int iN = n(objP);
                    int iO = o(iN);
                    this.f5923g.b(iO);
                    this.f5924h.a(iO);
                    this.f5925i.b(iO);
                    v();
                    if (Y.a.w(2)) {
                        Y.a.z(this.f5917a, "get (reuse) (object, size) = (%x, %s)", Integer.valueOf(System.identityHashCode(objP)), Integer.valueOf(iN));
                    }
                    return objP;
                }
                int iO2 = o(iM);
                if (!g(iO2)) {
                    throw new c(this.f5919c.f1946a, this.f5923g.f5928b, this.f5924h.f5928b, iO2);
                }
                this.f5923g.b(iO2);
                if (bVarK != null) {
                    bVarK.e();
                }
                try {
                    objF = f(iM);
                } catch (Throwable th) {
                    synchronized (this) {
                        try {
                            this.f5923g.a(iO2);
                            com.facebook.imagepipeline.memory.b bVarK2 = k(iM);
                            if (bVarK2 != null) {
                                bVarK2.b();
                            }
                            p.c(th);
                            objF = null;
                        } finally {
                        }
                    }
                }
                synchronized (this) {
                    try {
                        k.i(this.f5921e.add(objF));
                        y();
                        this.f5925i.a(iO2);
                        v();
                        if (Y.a.w(2)) {
                            Y.a.z(this.f5917a, "get (alloc) (object, size) = (%x, %s)", Integer.valueOf(System.identityHashCode(objF)), Integer.valueOf(iM));
                        }
                    } finally {
                    }
                }
                return objF;
            } finally {
            }
        }
    }

    protected abstract void j(Object obj);

    synchronized com.facebook.imagepipeline.memory.b k(int i3) {
        try {
            com.facebook.imagepipeline.memory.b bVar = (com.facebook.imagepipeline.memory.b) this.f5920d.get(i3);
            if (bVar == null && this.f5922f) {
                if (Y.a.w(2)) {
                    Y.a.y(this.f5917a, "creating new bucket %s", Integer.valueOf(i3));
                }
                com.facebook.imagepipeline.memory.b bVarW = w(i3);
                this.f5920d.put(i3, bVarW);
                return bVarW;
            }
            return bVar;
        } finally {
        }
    }

    protected abstract int m(int i3);

    protected abstract int n(Object obj);

    protected abstract int o(int i3);

    protected synchronized Object p(com.facebook.imagepipeline.memory.b bVar) {
        return bVar.c();
    }

    protected void r() {
        this.f5918b.a(this);
        this.f5925i.f(this);
    }

    synchronized boolean s() {
        boolean z3;
        z3 = this.f5923g.f5928b + this.f5924h.f5928b > this.f5919c.f1947b;
        if (z3) {
            this.f5925i.g();
        }
        return z3;
    }

    protected boolean t(Object obj) {
        k.g(obj);
        return true;
    }

    com.facebook.imagepipeline.memory.b w(int i3) {
        return new com.facebook.imagepipeline.memory.b(o(i3), Integer.MAX_VALUE, 0, this.f5919c.f1951f);
    }

    synchronized void x(int i3) {
        try {
            int i4 = this.f5923g.f5928b;
            int i5 = this.f5924h.f5928b;
            int iMin = Math.min((i4 + i5) - i3, i5);
            if (iMin <= 0) {
                return;
            }
            if (Y.a.w(2)) {
                Y.a.A(this.f5917a, "trimToSize: TargetSize = %d; Initial Size = %d; Bytes to free = %d", Integer.valueOf(i3), Integer.valueOf(this.f5923g.f5928b + this.f5924h.f5928b), Integer.valueOf(iMin));
            }
            v();
            for (int i6 = 0; i6 < this.f5920d.size() && iMin > 0; i6++) {
                com.facebook.imagepipeline.memory.b bVar = (com.facebook.imagepipeline.memory.b) k.g((com.facebook.imagepipeline.memory.b) this.f5920d.valueAt(i6));
                while (iMin > 0) {
                    Object objG = bVar.g();
                    if (objG == null) {
                        break;
                    }
                    j(objG);
                    int i7 = bVar.f5929a;
                    iMin -= i7;
                    this.f5924h.a(i7);
                }
            }
            v();
            if (Y.a.w(2)) {
                Y.a.z(this.f5917a, "trimToSize: TargetSize = %d; Final Size = %d", Integer.valueOf(i3), Integer.valueOf(this.f5923g.f5928b + this.f5924h.f5928b));
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    synchronized void y() {
        if (s()) {
            x(this.f5919c.f1947b);
        }
    }

    public a(a0.d dVar, E e4, F f3, boolean z3) {
        this(dVar, e4, f3);
        this.f5926j = z3;
    }
}
