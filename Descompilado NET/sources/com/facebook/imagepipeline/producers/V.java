package com.facebook.imagepipeline.producers;

import android.util.Pair;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/* JADX INFO: loaded from: classes.dex */
public abstract class V implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final Map f6064a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final e0 f6065b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final boolean f6066c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String f6067d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final String f6068e;

    class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Object f6069a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final CopyOnWriteArraySet f6070b = X.m.a();

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private Closeable f6071c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private float f6072d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f6073e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private C0345e f6074f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private b f6075g;

        /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.V$a$a, reason: collision with other inner class name */
        class C0093a extends C0346f {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ Pair f6077a;

            C0093a(Pair pair) {
                this.f6077a = pair;
            }

            @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
            public void a() {
                boolean zRemove;
                List list;
                C0345e c0345e;
                List listT;
                List listR;
                synchronized (a.this) {
                    try {
                        zRemove = a.this.f6070b.remove(this.f6077a);
                        list = null;
                        if (!zRemove) {
                            c0345e = null;
                            listT = null;
                        } else if (a.this.f6070b.isEmpty()) {
                            c0345e = a.this.f6074f;
                            listT = null;
                        } else {
                            List listS = a.this.s();
                            listT = a.this.t();
                            listR = a.this.r();
                            c0345e = null;
                            list = listS;
                        }
                        listR = listT;
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                C0345e.f(list);
                C0345e.g(listT);
                C0345e.e(listR);
                if (c0345e != null) {
                    if (!V.this.f6066c || c0345e.v()) {
                        c0345e.j();
                    } else {
                        C0345e.g(c0345e.p(I0.f.f415c));
                    }
                }
                if (zRemove) {
                    ((InterfaceC0354n) this.f6077a.first).b();
                }
            }

            @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
            public void b() {
                C0345e.e(a.this.r());
            }

            @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
            public void c() {
                C0345e.g(a.this.t());
            }

            @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
            public void d() {
                C0345e.f(a.this.s());
            }
        }

        private class b extends AbstractC0343c {
            @Override // com.facebook.imagepipeline.producers.AbstractC0343c
            protected void g() {
                try {
                    if (V0.b.d()) {
                        V0.b.a("MultiplexProducer#onCancellation");
                    }
                    a.this.m(this);
                    if (V0.b.d()) {
                        V0.b.b();
                    }
                } catch (Throwable th) {
                    if (V0.b.d()) {
                        V0.b.b();
                    }
                    throw th;
                }
            }

            @Override // com.facebook.imagepipeline.producers.AbstractC0343c
            protected void h(Throwable th) {
                try {
                    if (V0.b.d()) {
                        V0.b.a("MultiplexProducer#onFailure");
                    }
                    a.this.n(this, th);
                    if (V0.b.d()) {
                        V0.b.b();
                    }
                } catch (Throwable th2) {
                    if (V0.b.d()) {
                        V0.b.b();
                    }
                    throw th2;
                }
            }

            @Override // com.facebook.imagepipeline.producers.AbstractC0343c
            protected void j(float f3) {
                try {
                    if (V0.b.d()) {
                        V0.b.a("MultiplexProducer#onProgressUpdate");
                    }
                    a.this.p(this, f3);
                    if (V0.b.d()) {
                        V0.b.b();
                    }
                } catch (Throwable th) {
                    if (V0.b.d()) {
                        V0.b.b();
                    }
                    throw th;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.facebook.imagepipeline.producers.AbstractC0343c
            /* JADX INFO: renamed from: p, reason: merged with bridge method [inline-methods] */
            public void i(Closeable closeable, int i3) {
                try {
                    if (V0.b.d()) {
                        V0.b.a("MultiplexProducer#onNewResult");
                    }
                    a.this.o(this, closeable, i3);
                    if (V0.b.d()) {
                        V0.b.b();
                    }
                } catch (Throwable th) {
                    if (V0.b.d()) {
                        V0.b.b();
                    }
                    throw th;
                }
            }

            private b() {
            }
        }

        public a(Object obj) {
            this.f6069a = obj;
        }

        private void g(Pair pair, f0 f0Var) {
            f0Var.a0(new C0093a(pair));
        }

        private void i(Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e4) {
                    throw new RuntimeException(e4);
                }
            }
        }

        private synchronized boolean j() {
            Iterator it = this.f6070b.iterator();
            while (it.hasNext()) {
                if (((f0) ((Pair) it.next()).second).c0()) {
                    return true;
                }
            }
            return false;
        }

        private synchronized boolean k() {
            Iterator it = this.f6070b.iterator();
            while (it.hasNext()) {
                if (!((f0) ((Pair) it.next()).second).v()) {
                    return false;
                }
            }
            return true;
        }

        private synchronized I0.f l() {
            I0.f fVarB;
            fVarB = I0.f.f415c;
            Iterator it = this.f6070b.iterator();
            while (it.hasNext()) {
                fVarB = I0.f.b(fVarB, ((f0) ((Pair) it.next()).second).o());
            }
            return fVarB;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void q(f0.e eVar) {
            synchronized (this) {
                try {
                    X.k.b(Boolean.valueOf(this.f6074f == null));
                    X.k.b(Boolean.valueOf(this.f6075g == null));
                    if (this.f6070b.isEmpty()) {
                        V.this.k(this.f6069a, this);
                        return;
                    }
                    f0 f0Var = (f0) ((Pair) this.f6070b.iterator().next()).second;
                    C0345e c0345e = new C0345e(f0Var.X(), f0Var.getId(), f0Var.P(), f0Var.i(), f0Var.d0(), k(), j(), l(), f0Var.e0());
                    this.f6074f = c0345e;
                    c0345e.q(f0Var.a());
                    if (eVar.b()) {
                        this.f6074f.A("started_as_prefetch", Boolean.valueOf(eVar.a()));
                    }
                    b bVar = new b();
                    this.f6075g = bVar;
                    V.this.f6065b.b(bVar, this.f6074f);
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized List r() {
            C0345e c0345e = this.f6074f;
            if (c0345e == null) {
                return null;
            }
            return c0345e.m(j());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized List s() {
            C0345e c0345e = this.f6074f;
            if (c0345e == null) {
                return null;
            }
            return c0345e.n(k());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized List t() {
            C0345e c0345e = this.f6074f;
            if (c0345e == null) {
                return null;
            }
            return c0345e.p(l());
        }

        public boolean h(InterfaceC0354n interfaceC0354n, f0 f0Var) {
            Pair pairCreate = Pair.create(interfaceC0354n, f0Var);
            synchronized (this) {
                try {
                    if (V.this.i(this.f6069a) != this) {
                        return false;
                    }
                    this.f6070b.add(pairCreate);
                    List listS = s();
                    List listT = t();
                    List listR = r();
                    Closeable closeableG = this.f6071c;
                    float f3 = this.f6072d;
                    int i3 = this.f6073e;
                    C0345e.f(listS);
                    C0345e.g(listT);
                    C0345e.e(listR);
                    synchronized (pairCreate) {
                        try {
                            synchronized (this) {
                                if (closeableG != this.f6071c) {
                                    closeableG = null;
                                } else if (closeableG != null) {
                                    closeableG = V.this.g(closeableG);
                                }
                            }
                            if (closeableG != null) {
                                if (f3 > 0.0f) {
                                    interfaceC0354n.c(f3);
                                }
                                interfaceC0354n.d(closeableG, i3);
                                i(closeableG);
                            }
                        } catch (Throwable th) {
                            throw th;
                        } finally {
                        }
                    }
                    g(pairCreate, f0Var);
                    return true;
                } finally {
                }
            }
        }

        public void m(b bVar) {
            synchronized (this) {
                try {
                    if (this.f6075g != bVar) {
                        return;
                    }
                    this.f6075g = null;
                    this.f6074f = null;
                    i(this.f6071c);
                    this.f6071c = null;
                    q(f0.e.UNSET);
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void n(b bVar, Throwable th) {
            synchronized (this) {
                try {
                    if (this.f6075g != bVar) {
                        return;
                    }
                    this.f6070b.clear();
                    V.this.k(this.f6069a, this);
                    i(this.f6071c);
                    this.f6071c = null;
                    for (Pair pair : this.f6070b) {
                        synchronized (pair) {
                            try {
                                ((f0) pair.second).P().i((f0) pair.second, V.this.f6067d, th, null);
                                C0345e c0345e = this.f6074f;
                                if (c0345e != null) {
                                    ((f0) pair.second).q(c0345e.a());
                                }
                                ((InterfaceC0354n) pair.first).a(th);
                            } finally {
                            }
                        }
                    }
                } finally {
                }
            }
        }

        public void o(b bVar, Closeable closeable, int i3) {
            synchronized (this) {
                try {
                    if (this.f6075g != bVar) {
                        return;
                    }
                    i(this.f6071c);
                    this.f6071c = null;
                    int size = this.f6070b.size();
                    if (AbstractC0343c.f(i3)) {
                        this.f6071c = V.this.g(closeable);
                        this.f6073e = i3;
                    } else {
                        this.f6070b.clear();
                        V.this.k(this.f6069a, this);
                    }
                    for (Pair pair : this.f6070b) {
                        synchronized (pair) {
                            try {
                                if (AbstractC0343c.e(i3)) {
                                    ((f0) pair.second).P().d((f0) pair.second, V.this.f6067d, null);
                                    C0345e c0345e = this.f6074f;
                                    if (c0345e != null) {
                                        ((f0) pair.second).q(c0345e.a());
                                    }
                                    ((f0) pair.second).A(V.this.f6068e, Integer.valueOf(size));
                                }
                                ((InterfaceC0354n) pair.first).d(closeable, i3);
                            } finally {
                            }
                        }
                    }
                } finally {
                }
            }
        }

        public void p(b bVar, float f3) {
            synchronized (this) {
                try {
                    if (this.f6075g != bVar) {
                        return;
                    }
                    this.f6072d = f3;
                    for (Pair pair : this.f6070b) {
                        synchronized (pair) {
                            ((InterfaceC0354n) pair.first).c(f3);
                        }
                    }
                } finally {
                }
            }
        }
    }

    protected V(e0 e0Var, String str, String str2) {
        this(e0Var, str, str2, false);
    }

    private synchronized a h(Object obj) {
        a aVar;
        aVar = new a(obj);
        this.f6064a.put(obj, aVar);
        return aVar;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        a aVarI;
        boolean z3;
        try {
            if (V0.b.d()) {
                V0.b.a("MultiplexProducer#produceResults");
            }
            f0Var.P().g(f0Var, this.f6067d);
            Object objJ = j(f0Var);
            do {
                synchronized (this) {
                    try {
                        aVarI = i(objJ);
                        if (aVarI == null) {
                            aVarI = h(objJ);
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                    } finally {
                    }
                }
            } while (!aVarI.h(interfaceC0354n, f0Var));
            if (z3) {
                aVarI.q(f0.e.c(f0Var.v()));
            }
            if (V0.b.d()) {
                V0.b.b();
            }
        } catch (Throwable th) {
            if (V0.b.d()) {
                V0.b.b();
            }
            throw th;
        }
    }

    protected abstract Closeable g(Closeable closeable);

    protected synchronized a i(Object obj) {
        return (a) this.f6064a.get(obj);
    }

    protected abstract Object j(f0 f0Var);

    protected synchronized void k(Object obj, a aVar) {
        if (this.f6064a.get(obj) == aVar) {
            this.f6064a.remove(obj);
        }
    }

    protected V(e0 e0Var, String str, String str2, boolean z3) {
        this.f6065b = e0Var;
        this.f6064a = new HashMap();
        this.f6066c = z3;
        this.f6067d = str;
        this.f6068e = str2;
    }
}
