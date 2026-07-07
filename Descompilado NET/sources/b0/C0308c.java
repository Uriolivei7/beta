package b0;

import b0.AbstractC0306a;

/* JADX INFO: renamed from: b0.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0308c extends AbstractC0306a {
    C0308c(Object obj, InterfaceC0313h interfaceC0313h, AbstractC0306a.c cVar, Throwable th) {
        super(obj, interfaceC0313h, cVar, th, true);
    }

    protected void finalize() throws Throwable {
        try {
            synchronized (this) {
                if (this.f5576b) {
                    return;
                }
                Object objF = this.f5577c.f();
                Y.a.K("FinalizerCloseableReference", "Finalized without closing: %x %x (type = %s)", Integer.valueOf(System.identityHashCode(this)), Integer.valueOf(System.identityHashCode(this.f5577c)), objF == null ? null : objF.getClass().getName());
                this.f5577c.d();
            }
        } finally {
            super.finalize();
        }
    }

    @Override // b0.AbstractC0306a, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // b0.AbstractC0306a
    /* JADX INFO: renamed from: y */
    public AbstractC0306a clone() {
        return this;
    }
}
