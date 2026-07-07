package androidx.lifecycle;

import androidx.lifecycle.AbstractC0299g;
import j.C0566c;
import java.util.Map;
import k.C0581b;

/* JADX INFO: loaded from: classes.dex */
public abstract class LiveData {

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    static final Object f5286k = new Object();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final Object f5287a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private C0581b f5288b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    int f5289c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f5290d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private volatile Object f5291e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    volatile Object f5292f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f5293g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f5294h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f5295i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final Runnable f5296j;

    class LifecycleBoundObserver extends androidx.lifecycle.LiveData.c implements InterfaceC0302j {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final l f5297e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        final /* synthetic */ LiveData f5298f;

        @Override // androidx.lifecycle.InterfaceC0302j
        public void d(l lVar, AbstractC0299g.a aVar) {
            AbstractC0299g.b bVarB = this.f5297e.t().b();
            if (bVarB == AbstractC0299g.b.DESTROYED) {
                this.f5298f.h(this.f5301a);
                return;
            }
            AbstractC0299g.b bVar = null;
            while (bVar != bVarB) {
                h(j());
                bVar = bVarB;
                bVarB = this.f5297e.t().b();
            }
        }

        void i() {
            this.f5297e.t().c(this);
        }

        boolean j() {
            return this.f5297e.t().b().b(AbstractC0299g.b.STARTED);
        }
    }

    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Object obj;
            synchronized (LiveData.this.f5287a) {
                obj = LiveData.this.f5292f;
                LiveData.this.f5292f = LiveData.f5286k;
            }
            LiveData.this.i(obj);
        }
    }

    private class b extends c {
        b(q qVar) {
            super(qVar);
        }

        @Override // androidx.lifecycle.LiveData.c
        boolean j() {
            return true;
        }
    }

    private abstract class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final q f5301a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        boolean f5302b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        int f5303c = -1;

        c(q qVar) {
            this.f5301a = qVar;
        }

        void h(boolean z3) {
            if (z3 == this.f5302b) {
                return;
            }
            this.f5302b = z3;
            LiveData.this.b(z3 ? 1 : -1);
            if (this.f5302b) {
                LiveData.this.d(this);
            }
        }

        void i() {
        }

        abstract boolean j();
    }

    public LiveData(Object obj) {
        this.f5287a = new Object();
        this.f5288b = new C0581b();
        this.f5289c = 0;
        this.f5292f = f5286k;
        this.f5296j = new a();
        this.f5291e = obj;
        this.f5293g = 0;
    }

    static void a(String str) {
        if (C0566c.f().b()) {
            return;
        }
        throw new IllegalStateException("Cannot invoke " + str + " on a background thread");
    }

    private void c(c cVar) {
        if (cVar.f5302b) {
            if (!cVar.j()) {
                cVar.h(false);
                return;
            }
            int i3 = cVar.f5303c;
            int i4 = this.f5293g;
            if (i3 >= i4) {
                return;
            }
            cVar.f5303c = i4;
            cVar.f5301a.a(this.f5291e);
        }
    }

    void b(int i3) {
        int i4 = this.f5289c;
        this.f5289c = i3 + i4;
        if (this.f5290d) {
            return;
        }
        this.f5290d = true;
        while (true) {
            try {
                int i5 = this.f5289c;
                if (i4 == i5) {
                    this.f5290d = false;
                    return;
                }
                boolean z3 = i4 == 0 && i5 > 0;
                boolean z4 = i4 > 0 && i5 == 0;
                if (z3) {
                    f();
                } else if (z4) {
                    g();
                }
                i4 = i5;
            } catch (Throwable th) {
                this.f5290d = false;
                throw th;
            }
        }
    }

    void d(c cVar) {
        if (this.f5294h) {
            this.f5295i = true;
            return;
        }
        this.f5294h = true;
        do {
            this.f5295i = false;
            if (cVar != null) {
                c(cVar);
                cVar = null;
            } else {
                C0581b.d dVarE = this.f5288b.e();
                while (dVarE.hasNext()) {
                    c((c) ((Map.Entry) dVarE.next()).getValue());
                    if (this.f5295i) {
                        break;
                    }
                }
            }
        } while (this.f5295i);
        this.f5294h = false;
    }

    public void e(q qVar) {
        a("observeForever");
        b bVar = new b(qVar);
        c cVar = (c) this.f5288b.i(qVar, bVar);
        if (cVar instanceof LifecycleBoundObserver) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (cVar != null) {
            return;
        }
        bVar.h(true);
    }

    protected void f() {
    }

    protected void g() {
    }

    public void h(q qVar) {
        a("removeObserver");
        c cVar = (c) this.f5288b.j(qVar);
        if (cVar == null) {
            return;
        }
        cVar.i();
        cVar.h(false);
    }

    protected void i(Object obj) {
        a("setValue");
        this.f5293g++;
        this.f5291e = obj;
        d(null);
    }

    public LiveData() {
        this.f5287a = new Object();
        this.f5288b = new C0581b();
        this.f5289c = 0;
        Object obj = f5286k;
        this.f5292f = obj;
        this.f5296j = new a();
        this.f5291e = obj;
        this.f5293g = -1;
    }
}
