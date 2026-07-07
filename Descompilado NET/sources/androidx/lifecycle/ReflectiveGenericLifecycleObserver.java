package androidx.lifecycle;

import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.C0294b;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
class ReflectiveGenericLifecycleObserver implements InterfaceC0302j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Object f5305a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0294b.a f5306b;

    ReflectiveGenericLifecycleObserver(Object obj) {
        this.f5305a = obj;
        this.f5306b = C0294b.f5312c.c(obj.getClass());
    }

    @Override // androidx.lifecycle.InterfaceC0302j
    public void d(l lVar, AbstractC0299g.a aVar) {
        this.f5306b.a(lVar, aVar, this.f5305a);
    }
}
