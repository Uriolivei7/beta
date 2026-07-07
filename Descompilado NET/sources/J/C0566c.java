package j;

import java.util.concurrent.Executor;

/* JADX INFO: renamed from: j.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0566c extends e {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static volatile C0566c f9534c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final Executor f9535d = new Executor() { // from class: j.a
        @Override // java.util.concurrent.Executor
        public final void execute(Runnable runnable) {
            C0566c.g(runnable);
        }
    };

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final Executor f9536e = new Executor() { // from class: j.b
        @Override // java.util.concurrent.Executor
        public final void execute(Runnable runnable) {
            C0566c.h(runnable);
        }
    };

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private e f9537a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final e f9538b;

    private C0566c() {
        d dVar = new d();
        this.f9538b = dVar;
        this.f9537a = dVar;
    }

    public static C0566c f() {
        if (f9534c != null) {
            return f9534c;
        }
        synchronized (C0566c.class) {
            try {
                if (f9534c == null) {
                    f9534c = new C0566c();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return f9534c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void g(Runnable runnable) {
        f().c(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void h(Runnable runnable) {
        f().a(runnable);
    }

    @Override // j.e
    public void a(Runnable runnable) {
        this.f9537a.a(runnable);
    }

    @Override // j.e
    public boolean b() {
        return this.f9537a.b();
    }

    @Override // j.e
    public void c(Runnable runnable) {
        this.f9537a.c(runnable);
    }
}
