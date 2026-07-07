package H0;

import b0.AbstractC0306a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class C {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Class f260b = C.class;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Map f261a = new HashMap();

    private C() {
    }

    public static C d() {
        return new C();
    }

    private synchronized void e() {
        Y.a.y(f260b, "Count = %d", Integer.valueOf(this.f261a.size()));
    }

    public void a() {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.f261a.values());
            this.f261a.clear();
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            O0.j jVar = (O0.j) arrayList.get(i3);
            if (jVar != null) {
                jVar.close();
            }
        }
    }

    public synchronized boolean b(R.d dVar) {
        X.k.g(dVar);
        if (!this.f261a.containsKey(dVar)) {
            return false;
        }
        O0.j jVar = (O0.j) this.f261a.get(dVar);
        synchronized (jVar) {
            if (O0.j.w0(jVar)) {
                return true;
            }
            this.f261a.remove(dVar);
            Y.a.G(f260b, "Found closed reference %d for key %s (%d)", Integer.valueOf(System.identityHashCode(jVar)), dVar.c(), Integer.valueOf(System.identityHashCode(dVar)));
            return false;
        }
    }

    public synchronized O0.j c(R.d dVar) {
        X.k.g(dVar);
        O0.j jVarI = (O0.j) this.f261a.get(dVar);
        if (jVarI != null) {
            synchronized (jVarI) {
                if (!O0.j.w0(jVarI)) {
                    this.f261a.remove(dVar);
                    Y.a.G(f260b, "Found closed reference %d for key %s (%d)", Integer.valueOf(System.identityHashCode(jVarI)), dVar.c(), Integer.valueOf(System.identityHashCode(dVar)));
                    return null;
                }
                jVarI = O0.j.i(jVarI);
            }
        }
        return jVarI;
    }

    public synchronized void f(R.d dVar, O0.j jVar) {
        X.k.g(dVar);
        X.k.b(Boolean.valueOf(O0.j.w0(jVar)));
        O0.j.o((O0.j) this.f261a.put(dVar, O0.j.i(jVar)));
        e();
    }

    public boolean g(R.d dVar) {
        O0.j jVar;
        X.k.g(dVar);
        synchronized (this) {
            jVar = (O0.j) this.f261a.remove(dVar);
        }
        if (jVar == null) {
            return false;
        }
        try {
            return jVar.v0();
        } finally {
            jVar.close();
        }
    }

    public synchronized boolean h(R.d dVar, O0.j jVar) {
        X.k.g(dVar);
        X.k.g(jVar);
        X.k.b(Boolean.valueOf(O0.j.w0(jVar)));
        O0.j jVar2 = (O0.j) this.f261a.get(dVar);
        if (jVar2 == null) {
            return false;
        }
        AbstractC0306a abstractC0306aV = jVar2.v();
        AbstractC0306a abstractC0306aV2 = jVar.v();
        if (abstractC0306aV != null && abstractC0306aV2 != null) {
            try {
                if (abstractC0306aV.P() == abstractC0306aV2.P()) {
                    this.f261a.remove(dVar);
                    AbstractC0306a.D(abstractC0306aV2);
                    AbstractC0306a.D(abstractC0306aV);
                    O0.j.o(jVar2);
                    e();
                    return true;
                }
            } finally {
                AbstractC0306a.D(abstractC0306aV2);
                AbstractC0306a.D(abstractC0306aV);
                O0.j.o(jVar2);
            }
        }
        return false;
    }
}
