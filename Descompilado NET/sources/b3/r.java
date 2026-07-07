package b3;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/* JADX INFO: loaded from: classes.dex */
public final class r implements F {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f5654b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f5655c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final k f5656d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Inflater f5657e;

    public r(k kVar, Inflater inflater) {
        D2.h.f(kVar, "source");
        D2.h.f(inflater, "inflater");
        this.f5656d = kVar;
        this.f5657e = inflater;
    }

    private final void o() {
        int i3 = this.f5654b;
        if (i3 == 0) {
            return;
        }
        int remaining = i3 - this.f5657e.getRemaining();
        this.f5654b -= remaining;
        this.f5656d.s(remaining);
    }

    public final long a(i iVar, long j3) throws IOException {
        D2.h.f(iVar, "sink");
        if (!(j3 >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + j3).toString());
        }
        if (this.f5655c) {
            throw new IllegalStateException("closed");
        }
        if (j3 == 0) {
            return 0L;
        }
        try {
            A aI0 = iVar.I0(1);
            int iMin = (int) Math.min(j3, 8192 - aI0.f5592c);
            i();
            int iInflate = this.f5657e.inflate(aI0.f5590a, aI0.f5592c, iMin);
            o();
            if (iInflate > 0) {
                aI0.f5592c += iInflate;
                long j4 = iInflate;
                iVar.E0(iVar.F0() + j4);
                return j4;
            }
            if (aI0.f5591b == aI0.f5592c) {
                iVar.f5627b = aI0.b();
                B.b(aI0);
            }
            return 0L;
        } catch (DataFormatException e4) {
            throw new IOException(e4);
        }
    }

    @Override // b3.F, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.f5655c) {
            return;
        }
        this.f5657e.end();
        this.f5655c = true;
        this.f5656d.close();
    }

    @Override // b3.F
    public G f() {
        return this.f5656d.f();
    }

    public final boolean i() {
        if (!this.f5657e.needsInput()) {
            return false;
        }
        if (this.f5656d.J()) {
            return true;
        }
        A a4 = this.f5656d.e().f5627b;
        D2.h.c(a4);
        int i3 = a4.f5592c;
        int i4 = a4.f5591b;
        int i5 = i3 - i4;
        this.f5654b = i5;
        this.f5657e.setInput(a4.f5590a, i4, i5);
        return false;
    }

    @Override // b3.F
    public long x(i iVar, long j3) throws IOException {
        D2.h.f(iVar, "sink");
        do {
            long jA = a(iVar, j3);
            if (jA > 0) {
                return jA;
            }
            if (this.f5657e.finished() || this.f5657e.needsDictionary()) {
                return -1L;
            }
        } while (!this.f5656d.J());
        throw new EOFException("source exhausted prematurely");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public r(F f3, Inflater inflater) {
        this(t.d(f3), inflater);
        D2.h.f(f3, "source");
        D2.h.f(inflater, "inflater");
    }
}
