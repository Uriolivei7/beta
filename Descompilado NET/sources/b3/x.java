package b3;

import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
final class x implements D {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final OutputStream f5664b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final G f5665c;

    public x(OutputStream outputStream, G g3) {
        D2.h.f(outputStream, "out");
        D2.h.f(g3, "timeout");
        this.f5664b = outputStream;
        this.f5665c = g3;
    }

    @Override // b3.D
    public void Q(i iVar, long j3) throws IOException {
        D2.h.f(iVar, "source");
        AbstractC0323f.b(iVar.F0(), 0L, j3);
        while (j3 > 0) {
            this.f5665c.f();
            A a4 = iVar.f5627b;
            D2.h.c(a4);
            int iMin = (int) Math.min(j3, a4.f5592c - a4.f5591b);
            this.f5664b.write(a4.f5590a, a4.f5591b, iMin);
            a4.f5591b += iMin;
            long j4 = iMin;
            j3 -= j4;
            iVar.E0(iVar.F0() - j4);
            if (a4.f5591b == a4.f5592c) {
                iVar.f5627b = a4.b();
                B.b(a4);
            }
        }
    }

    @Override // b3.D, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.f5664b.close();
    }

    @Override // b3.D
    public G f() {
        return this.f5665c;
    }

    @Override // b3.D, java.io.Flushable
    public void flush() throws IOException {
        this.f5664b.flush();
    }

    public String toString() {
        return "sink(" + this.f5664b + ')';
    }
}
