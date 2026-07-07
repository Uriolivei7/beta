package androidx.lifecycle;

import androidx.lifecycle.AbstractC0299g;

/* JADX INFO: loaded from: classes.dex */
public final class CompositeGeneratedAdaptersObserver implements InterfaceC0302j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final InterfaceC0297e[] f5260a;

    public CompositeGeneratedAdaptersObserver(InterfaceC0297e[] interfaceC0297eArr) {
        D2.h.f(interfaceC0297eArr, "generatedAdapters");
        this.f5260a = interfaceC0297eArr;
    }

    @Override // androidx.lifecycle.InterfaceC0302j
    public void d(l lVar, AbstractC0299g.a aVar) {
        D2.h.f(lVar, "source");
        D2.h.f(aVar, "event");
        new o();
        InterfaceC0297e[] interfaceC0297eArr = this.f5260a;
        if (interfaceC0297eArr.length > 0) {
            InterfaceC0297e interfaceC0297e = interfaceC0297eArr[0];
            throw null;
        }
        if (interfaceC0297eArr.length <= 0) {
            return;
        }
        InterfaceC0297e interfaceC0297e2 = interfaceC0297eArr[0];
        throw null;
    }
}
