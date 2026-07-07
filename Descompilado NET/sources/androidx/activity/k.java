package androidx.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Executor f3026a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C2.a f3027b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Object f3028c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f3029d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f3030e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f3031f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final List f3032g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Runnable f3033h;

    public k(Executor executor, C2.a aVar) {
        D2.h.f(executor, "executor");
        D2.h.f(aVar, "reportFullyDrawn");
        this.f3026a = executor;
        this.f3027b = aVar;
        this.f3028c = new Object();
        this.f3032g = new ArrayList();
        this.f3033h = new Runnable() { // from class: androidx.activity.j
            @Override // java.lang.Runnable
            public final void run() {
                k.d(this.f3025b);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void d(k kVar) {
        D2.h.f(kVar, "this$0");
        synchronized (kVar.f3028c) {
            try {
                kVar.f3030e = false;
                if (kVar.f3029d == 0 && !kVar.f3031f) {
                    kVar.f3027b.a();
                    kVar.b();
                }
                r2.r rVar = r2.r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final void b() {
        synchronized (this.f3028c) {
            try {
                this.f3031f = true;
                Iterator it = this.f3032g.iterator();
                while (it.hasNext()) {
                    ((C2.a) it.next()).a();
                }
                this.f3032g.clear();
                r2.r rVar = r2.r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final boolean c() {
        boolean z3;
        synchronized (this.f3028c) {
            z3 = this.f3031f;
        }
        return z3;
    }
}
