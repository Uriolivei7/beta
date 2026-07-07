package R0;

import a0.h;
import b0.AbstractC0306a;

/* JADX INFO: loaded from: classes.dex */
public class x implements a0.h {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f1992b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    AbstractC0306a f1993c;

    public x(AbstractC0306a abstractC0306a, int i3) {
        X.k.g(abstractC0306a);
        X.k.b(Boolean.valueOf(i3 >= 0 && i3 <= ((v) abstractC0306a.P()).i()));
        this.f1993c = abstractC0306a.clone();
        this.f1992b = i3;
    }

    synchronized void a() {
        if (b()) {
            throw new h.a();
        }
    }

    @Override // a0.h
    public synchronized boolean b() {
        return !AbstractC0306a.c0(this.f1993c);
    }

    @Override // a0.h
    public synchronized int c(int i3, byte[] bArr, int i4, int i5) {
        a();
        X.k.b(Boolean.valueOf(i3 + i5 <= this.f1992b));
        X.k.g(this.f1993c);
        return ((v) this.f1993c.P()).c(i3, bArr, i4, i5);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        AbstractC0306a.D(this.f1993c);
        this.f1993c = null;
    }

    @Override // a0.h
    public synchronized byte g(int i3) {
        a();
        X.k.b(Boolean.valueOf(i3 >= 0));
        X.k.b(Boolean.valueOf(i3 < this.f1992b));
        X.k.g(this.f1993c);
        return ((v) this.f1993c.P()).g(i3);
    }

    @Override // a0.h
    public synchronized int size() {
        a();
        return this.f1992b;
    }
}
