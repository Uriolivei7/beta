package androidx.activity;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public abstract class m {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f3034a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final CopyOnWriteArrayList f3035b = new CopyOnWriteArrayList();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private C2.a f3036c;

    public m(boolean z3) {
        this.f3034a = z3;
    }

    public final void a(a aVar) {
        D2.h.f(aVar, "cancellable");
        this.f3035b.add(aVar);
    }

    public abstract void b();

    public final boolean c() {
        return this.f3034a;
    }

    public final void d() {
        Iterator it = this.f3035b.iterator();
        while (it.hasNext()) {
            ((a) it.next()).cancel();
        }
    }

    public final void e(a aVar) {
        D2.h.f(aVar, "cancellable");
        this.f3035b.remove(aVar);
    }

    public final void f(boolean z3) {
        this.f3034a = z3;
        C2.a aVar = this.f3036c;
        if (aVar != null) {
            aVar.a();
        }
    }

    public final void g(C2.a aVar) {
        this.f3036c = aVar;
    }
}
