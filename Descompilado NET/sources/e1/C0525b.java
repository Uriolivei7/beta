package e1;

/* JADX INFO: renamed from: e1.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0525b implements q.e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Object[] f9347a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f9348b;

    public C0525b(int i3) {
        this.f9347a = new Object[i3];
    }

    @Override // q.e
    public synchronized boolean a(Object obj) {
        D2.h.f(obj, "instance");
        int i3 = this.f9348b;
        Object[] objArr = this.f9347a;
        if (i3 == objArr.length) {
            return false;
        }
        objArr[i3] = obj;
        this.f9348b = i3 + 1;
        return true;
    }

    @Override // q.e
    public synchronized Object b() {
        int i3 = this.f9348b;
        if (i3 == 0) {
            return null;
        }
        int i4 = i3 - 1;
        this.f9348b = i4;
        Object obj = this.f9347a[i4];
        D2.h.d(obj, "null cannot be cast to non-null type T of com.facebook.react.common.ClearableSynchronizedPool");
        this.f9347a[i4] = null;
        return obj;
    }

    public final synchronized void c() {
        try {
            int i3 = this.f9348b;
            for (int i4 = 0; i4 < i3; i4++) {
                this.f9347a[i4] = null;
            }
            this.f9348b = 0;
        } catch (Throwable th) {
            throw th;
        }
    }
}
