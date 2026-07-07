package R0;

import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class u implements A {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Set f1989a = new HashSet();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final k f1990b = new k();

    private Object d(Object obj) {
        if (obj != null) {
            synchronized (this) {
                this.f1989a.remove(obj);
            }
        }
        return obj;
    }

    @Override // R0.A
    public Object b() {
        return d(this.f1990b.f());
    }

    @Override // R0.A
    public void c(Object obj) {
        boolean zAdd;
        synchronized (this) {
            zAdd = this.f1989a.add(obj);
        }
        if (zAdd) {
            this.f1990b.e(a(obj), obj);
        }
    }

    @Override // R0.A
    public Object get(int i3) {
        return d(this.f1990b.a(i3));
    }
}
