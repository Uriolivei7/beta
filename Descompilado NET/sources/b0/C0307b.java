package b0;

import X.k;
import b0.AbstractC0306a;

/* JADX INFO: renamed from: b0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0307b extends AbstractC0306a {
    private C0307b(C0314i c0314i, AbstractC0306a.c cVar, Throwable th) {
        super(c0314i, cVar, th);
    }

    protected void finalize() throws Throwable {
        try {
            synchronized (this) {
                if (this.f5576b) {
                    super.finalize();
                    return;
                }
                Object objF = this.f5577c.f();
                Y.a.K("DefaultCloseableReference", "Finalized without closing: %x %x (type = %s)", Integer.valueOf(System.identityHashCode(this)), Integer.valueOf(System.identityHashCode(this.f5577c)), objF == null ? null : objF.getClass().getName());
                AbstractC0306a.c cVar = this.f5578d;
                if (cVar != null) {
                    cVar.b(this.f5577c, this.f5579e);
                }
                close();
                super.finalize();
            }
        } catch (Throwable th) {
            super.finalize();
            throw th;
        }
    }

    @Override // b0.AbstractC0306a
    /* JADX INFO: renamed from: y, reason: merged with bridge method [inline-methods] */
    public AbstractC0306a clone() {
        k.i(a0());
        return new C0307b(this.f5577c, this.f5578d, this.f5579e != null ? new Throwable() : null);
    }

    C0307b(Object obj, InterfaceC0313h interfaceC0313h, AbstractC0306a.c cVar, Throwable th) {
        super(obj, interfaceC0313h, cVar, th, true);
    }
}
