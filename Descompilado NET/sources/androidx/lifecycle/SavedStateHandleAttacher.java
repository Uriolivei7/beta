package androidx.lifecycle;

import androidx.lifecycle.AbstractC0299g;

/* JADX INFO: loaded from: classes.dex */
public final class SavedStateHandleAttacher implements InterfaceC0302j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final z f5307a;

    public SavedStateHandleAttacher(z zVar) {
        D2.h.f(zVar, "provider");
        this.f5307a = zVar;
    }

    @Override // androidx.lifecycle.InterfaceC0302j
    public void d(l lVar, AbstractC0299g.a aVar) {
        D2.h.f(lVar, "source");
        D2.h.f(aVar, "event");
        if (aVar == AbstractC0299g.a.ON_CREATE) {
            lVar.t().c(this);
            this.f5307a.d();
        } else {
            throw new IllegalStateException(("Next event must be ON_CREATE, it was " + aVar).toString());
        }
    }
}
