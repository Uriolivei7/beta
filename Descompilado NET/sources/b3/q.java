package b3;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Inflater;

/* JADX INFO: loaded from: classes.dex */
public final class q implements F {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private byte f5649b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final z f5650c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Inflater f5651d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final r f5652e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final CRC32 f5653f;

    public q(F f3) {
        D2.h.f(f3, "source");
        z zVar = new z(f3);
        this.f5650c = zVar;
        Inflater inflater = new Inflater(true);
        this.f5651d = inflater;
        this.f5652e = new r((k) zVar, inflater);
        this.f5653f = new CRC32();
    }

    private final void a(String str, int i3, int i4) throws IOException {
        if (i4 == i3) {
            return;
        }
        String str2 = String.format("%s: actual 0x%08x != expected 0x%08x", Arrays.copyOf(new Object[]{str, Integer.valueOf(i4), Integer.valueOf(i3)}, 3));
        D2.h.e(str2, "java.lang.String.format(this, *args)");
        throw new IOException(str2);
    }

    private final void i() throws IOException {
        this.f5650c.g0(10L);
        byte bA0 = this.f5650c.f5670b.a0(3L);
        boolean z3 = ((bA0 >> 1) & 1) == 1;
        if (z3) {
            q(this.f5650c.f5670b, 0L, 10L);
        }
        a("ID1ID2", 8075, this.f5650c.Y());
        this.f5650c.s(8L);
        if (((bA0 >> 2) & 1) == 1) {
            this.f5650c.g0(2L);
            if (z3) {
                q(this.f5650c.f5670b, 0L, 2L);
            }
            long jB0 = this.f5650c.f5670b.B0();
            this.f5650c.g0(jB0);
            if (z3) {
                q(this.f5650c.f5670b, 0L, jB0);
            }
            this.f5650c.s(jB0);
        }
        if (((bA0 >> 3) & 1) == 1) {
            long jA = this.f5650c.a((byte) 0);
            if (jA == -1) {
                throw new EOFException();
            }
            if (z3) {
                q(this.f5650c.f5670b, 0L, jA + 1);
            }
            this.f5650c.s(jA + 1);
        }
        if (((bA0 >> 4) & 1) == 1) {
            long jA2 = this.f5650c.a((byte) 0);
            if (jA2 == -1) {
                throw new EOFException();
            }
            if (z3) {
                q(this.f5650c.f5670b, 0L, jA2 + 1);
            }
            this.f5650c.s(jA2 + 1);
        }
        if (z3) {
            a("FHCRC", this.f5650c.q(), (short) this.f5653f.getValue());
            this.f5653f.reset();
        }
    }

    private final void o() throws IOException {
        a("CRC", this.f5650c.o(), (int) this.f5653f.getValue());
        a("ISIZE", this.f5650c.o(), (int) this.f5651d.getBytesWritten());
    }

    private final void q(i iVar, long j3, long j4) {
        A a4 = iVar.f5627b;
        D2.h.c(a4);
        while (true) {
            int i3 = a4.f5592c;
            int i4 = a4.f5591b;
            if (j3 < i3 - i4) {
                break;
            }
            j3 -= (long) (i3 - i4);
            a4 = a4.f5595f;
            D2.h.c(a4);
        }
        while (j4 > 0) {
            int i5 = (int) (((long) a4.f5591b) + j3);
            int iMin = (int) Math.min(a4.f5592c - i5, j4);
            this.f5653f.update(a4.f5590a, i5, iMin);
            j4 -= (long) iMin;
            a4 = a4.f5595f;
            D2.h.c(a4);
            j3 = 0;
        }
    }

    @Override // b3.F, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f5652e.close();
    }

    @Override // b3.F
    public G f() {
        return this.f5650c.f();
    }

    @Override // b3.F
    public long x(i iVar, long j3) throws IOException {
        D2.h.f(iVar, "sink");
        if (!(j3 >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + j3).toString());
        }
        if (j3 == 0) {
            return 0L;
        }
        if (this.f5649b == 0) {
            i();
            this.f5649b = (byte) 1;
        }
        if (this.f5649b == 1) {
            long jF0 = iVar.F0();
            long jX = this.f5652e.x(iVar, j3);
            if (jX != -1) {
                q(iVar, jF0, jX);
                return jX;
            }
            this.f5649b = (byte) 2;
        }
        if (this.f5649b == 2) {
            o();
            this.f5649b = (byte) 3;
            if (!this.f5650c.J()) {
                throw new IOException("gzip finished without exhausting source");
            }
        }
        return -1L;
    }
}
