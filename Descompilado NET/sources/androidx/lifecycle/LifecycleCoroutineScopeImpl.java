package androidx.lifecycle;

import androidx.lifecycle.AbstractC0299g;
import v2.InterfaceC0757a;

/* JADX INFO: loaded from: classes.dex */
public final class LifecycleCoroutineScopeImpl extends AbstractC0300h implements InterfaceC0302j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final AbstractC0299g f5285a;

    public LifecycleCoroutineScopeImpl(AbstractC0299g abstractC0299g, InterfaceC0757a interfaceC0757a) {
        D2.h.f(abstractC0299g, "lifecycle");
        D2.h.f(interfaceC0757a, "coroutineContext");
        this.f5285a = abstractC0299g;
        if (i().b() == AbstractC0299g.b.DESTROYED) {
            h();
            L2.c.b(null, null, 1, null);
        }
    }

    @Override // androidx.lifecycle.InterfaceC0302j
    public void d(l lVar, AbstractC0299g.a aVar) {
        D2.h.f(lVar, "source");
        D2.h.f(aVar, "event");
        if (i().b().compareTo(AbstractC0299g.b.DESTROYED) <= 0) {
            i().c(this);
            h();
            L2.c.b(null, null, 1, null);
        }
    }

    public InterfaceC0757a h() {
        return null;
    }

    public AbstractC0299g i() {
        return this.f5285a;
    }
}
