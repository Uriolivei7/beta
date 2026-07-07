package b3;

import java.io.EOFException;

/* JADX INFO: renamed from: b3.h, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
final class C0325h implements D {
    @Override // b3.D
    public void Q(i iVar, long j3) throws EOFException {
        D2.h.f(iVar, "source");
        iVar.s(j3);
    }

    @Override // b3.D
    public G f() {
        return G.f5605d;
    }

    @Override // b3.D, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // b3.D, java.io.Flushable
    public void flush() {
    }
}
