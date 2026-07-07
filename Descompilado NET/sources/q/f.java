package q;

import androidx.core.util.Pools$SimplePool;

/* JADX INFO: loaded from: classes.dex */
public class f extends Pools$SimplePool {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Object f10333c;

    public f(int i3) {
        super(i3);
        this.f10333c = new Object();
    }

    @Override // androidx.core.util.Pools$SimplePool, q.e
    public boolean a(Object obj) {
        boolean zA;
        D2.h.f(obj, "instance");
        synchronized (this.f10333c) {
            zA = super.a(obj);
        }
        return zA;
    }

    @Override // androidx.core.util.Pools$SimplePool, q.e
    public Object b() {
        Object objB;
        synchronized (this.f10333c) {
            objB = super.b();
        }
        return objB;
    }
}
