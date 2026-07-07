package b0;

import X.k;
import b0.AbstractC0306a;

/* JADX INFO: renamed from: b0.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0312g extends AbstractC0306a {
    private C0312g(C0314i c0314i, AbstractC0306a.c cVar, Throwable th) {
        super(c0314i, cVar, th);
    }

    @Override // b0.AbstractC0306a
    /* JADX INFO: renamed from: y */
    public AbstractC0306a clone() {
        k.i(a0());
        return new C0312g(this.f5577c, this.f5578d, this.f5579e);
    }

    C0312g(Object obj, InterfaceC0313h interfaceC0313h, AbstractC0306a.c cVar, Throwable th) {
        super(obj, interfaceC0313h, cVar, th, false);
    }
}
