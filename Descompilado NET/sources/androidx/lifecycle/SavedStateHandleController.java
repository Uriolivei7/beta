package androidx.lifecycle;

import androidx.lifecycle.AbstractC0299g;

/* JADX INFO: loaded from: classes.dex */
public final class SavedStateHandleController implements InterfaceC0302j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f5308a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final x f5309b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f5310c;

    public SavedStateHandleController(String str, x xVar) {
        D2.h.f(str, "key");
        D2.h.f(xVar, "handle");
        this.f5308a = str;
        this.f5309b = xVar;
    }

    @Override // androidx.lifecycle.InterfaceC0302j
    public void d(l lVar, AbstractC0299g.a aVar) {
        D2.h.f(lVar, "source");
        D2.h.f(aVar, "event");
        if (aVar == AbstractC0299g.a.ON_DESTROY) {
            this.f5310c = false;
            lVar.t().c(this);
        }
    }

    public final void h(androidx.savedstate.a aVar, AbstractC0299g abstractC0299g) {
        D2.h.f(aVar, "registry");
        D2.h.f(abstractC0299g, "lifecycle");
        if (this.f5310c) {
            throw new IllegalStateException("Already attached to lifecycleOwner");
        }
        this.f5310c = true;
        abstractC0299g.a(this);
        aVar.h(this.f5308a, this.f5309b.c());
    }

    public final x i() {
        return this.f5309b;
    }

    public final boolean j() {
        return this.f5310c;
    }
}
