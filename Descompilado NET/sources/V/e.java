package V;

import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public abstract class e implements Runnable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected final AtomicInteger f2672b = new AtomicInteger(0);

    public void a() {
        if (this.f2672b.compareAndSet(0, 2)) {
            d();
        }
    }

    protected abstract Object c();

    @Override // java.lang.Runnable
    public final void run() {
        if (this.f2672b.compareAndSet(0, 1)) {
            try {
                Object objC = c();
                this.f2672b.set(3);
                try {
                    f(objC);
                } finally {
                    b(objC);
                }
            } catch (Exception e4) {
                this.f2672b.set(4);
                e(e4);
            }
        }
    }

    protected void d() {
    }

    protected void b(Object obj) {
    }

    protected void e(Exception exc) {
    }

    protected void f(Object obj) {
    }
}
