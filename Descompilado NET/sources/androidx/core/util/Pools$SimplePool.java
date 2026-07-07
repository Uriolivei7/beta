package androidx.core.util;

import D2.h;
import q.e;

/* JADX INFO: loaded from: classes.dex */
public class Pools$SimplePool implements e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Object[] f4538a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f4539b;

    public Pools$SimplePool(int i3) {
        if (i3 <= 0) {
            throw new IllegalArgumentException("The max pool size must be > 0");
        }
        this.f4538a = new Object[i3];
    }

    private final boolean c(Object obj) {
        int i3 = this.f4539b;
        for (int i4 = 0; i4 < i3; i4++) {
            if (this.f4538a[i4] == obj) {
                return true;
            }
        }
        return false;
    }

    @Override // q.e
    public boolean a(Object obj) {
        h.f(obj, "instance");
        if (c(obj)) {
            throw new IllegalStateException("Already in the pool!");
        }
        int i3 = this.f4539b;
        Object[] objArr = this.f4538a;
        if (i3 >= objArr.length) {
            return false;
        }
        objArr[i3] = obj;
        this.f4539b = i3 + 1;
        return true;
    }

    @Override // q.e
    public Object b() {
        int i3 = this.f4539b;
        if (i3 <= 0) {
            return null;
        }
        int i4 = i3 - 1;
        Object obj = this.f4538a[i4];
        h.d(obj, "null cannot be cast to non-null type T of androidx.core.util.Pools.SimplePool");
        this.f4538a[i4] = null;
        this.f4539b--;
        return obj;
    }
}
