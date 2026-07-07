package O;

/* JADX INFO: loaded from: classes.dex */
public class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final f f1448a = new f();

    public f a() {
        return this.f1448a;
    }

    public void b() {
        if (!e()) {
            throw new IllegalStateException("Cannot cancel a completed task.");
        }
    }

    public void c(Exception exc) {
        if (!f(exc)) {
            throw new IllegalStateException("Cannot set the error on a completed task.");
        }
    }

    public void d(Object obj) {
        if (!g(obj)) {
            throw new IllegalStateException("Cannot set the result of a completed task.");
        }
    }

    public boolean e() {
        return this.f1448a.p();
    }

    public boolean f(Exception exc) {
        return this.f1448a.q(exc);
    }

    public boolean g(Object obj) {
        return this.f1448a.r(obj);
    }
}
