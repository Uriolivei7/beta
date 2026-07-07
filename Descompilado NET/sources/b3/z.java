package b3;

import K2.AbstractC0189a;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes.dex */
public final class z implements k {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final i f5670b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public boolean f5671c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final F f5672d;

    public z(F f3) {
        D2.h.f(f3, "source");
        this.f5672d = f3;
        this.f5670b = new i();
    }

    @Override // b3.k
    public int B() throws EOFException {
        g0(4L);
        return this.f5670b.B();
    }

    @Override // b3.k
    public long F() throws EOFException {
        g0(8L);
        return this.f5670b.F();
    }

    @Override // b3.k
    public String G() {
        return W(Long.MAX_VALUE);
    }

    @Override // b3.k
    public byte[] H() {
        this.f5670b.T(this.f5672d);
        return this.f5670b.H();
    }

    @Override // b3.k
    public boolean J() {
        if (this.f5671c) {
            throw new IllegalStateException("closed");
        }
        return this.f5670b.J() && this.f5672d.x(this.f5670b, (long) 8192) == -1;
    }

    @Override // b3.k
    public int K(w wVar) throws EOFException {
        D2.h.f(wVar, "options");
        if (this.f5671c) {
            throw new IllegalStateException("closed");
        }
        while (true) {
            int iD = c3.a.d(this.f5670b, wVar, true);
            if (iD != -2) {
                if (iD != -1) {
                    this.f5670b.s(wVar.e()[iD].v());
                    return iD;
                }
            } else if (this.f5672d.x(this.f5670b, 8192) == -1) {
                break;
            }
        }
        return -1;
    }

    @Override // b3.k
    public byte[] M(long j3) throws EOFException {
        g0(j3);
        return this.f5670b.M(j3);
    }

    @Override // b3.k
    public String O() {
        this.f5670b.T(this.f5672d);
        return this.f5670b.O();
    }

    @Override // b3.k
    public long S(D d4) {
        D2.h.f(d4, "sink");
        long j3 = 0;
        while (this.f5672d.x(this.f5670b, 8192) != -1) {
            long jZ = this.f5670b.z();
            if (jZ > 0) {
                j3 += jZ;
                d4.Q(this.f5670b, jZ);
            }
        }
        if (this.f5670b.F0() <= 0) {
            return j3;
        }
        long jF0 = j3 + this.f5670b.F0();
        i iVar = this.f5670b;
        d4.Q(iVar, iVar.F0());
        return jF0;
    }

    @Override // b3.k
    public long V() throws EOFException {
        byte bA0;
        g0(1L);
        long j3 = 0;
        while (true) {
            long j4 = j3 + 1;
            if (!v(j4)) {
                break;
            }
            bA0 = this.f5670b.a0(j3);
            if ((bA0 < ((byte) 48) || bA0 > ((byte) 57)) && !(j3 == 0 && bA0 == ((byte) 45))) {
                break;
            }
            j3 = j4;
        }
        if (j3 == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected leading [0-9] or '-' character but was 0x");
            String string = Integer.toString(bA0, AbstractC0189a.a(AbstractC0189a.a(16)));
            D2.h.e(string, "java.lang.Integer.toStri…(this, checkRadix(radix))");
            sb.append(string);
            throw new NumberFormatException(sb.toString());
        }
        return this.f5670b.V();
    }

    @Override // b3.k
    public String W(long j3) throws EOFException {
        if (!(j3 >= 0)) {
            throw new IllegalArgumentException(("limit < 0: " + j3).toString());
        }
        long j4 = j3 == Long.MAX_VALUE ? Long.MAX_VALUE : j3 + 1;
        byte b4 = (byte) 10;
        long jI = i(b4, 0L, j4);
        if (jI != -1) {
            return c3.a.c(this.f5670b, jI);
        }
        if (j4 < Long.MAX_VALUE && v(j4) && this.f5670b.a0(j4 - 1) == ((byte) 13) && v(1 + j4) && this.f5670b.a0(j4) == b4) {
            return c3.a.c(this.f5670b, j4);
        }
        i iVar = new i();
        i iVar2 = this.f5670b;
        iVar2.D(iVar, 0L, Math.min(32, iVar2.F0()));
        throw new EOFException("\\n not found: limit=" + Math.min(this.f5670b.F0(), j3) + " content=" + iVar.z0().k() + "…");
    }

    @Override // b3.k
    public short Y() throws EOFException {
        g0(2L);
        return this.f5670b.Y();
    }

    public long a(byte b4) {
        return i(b4, 0L, Long.MAX_VALUE);
    }

    @Override // b3.F, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws EOFException {
        if (this.f5671c) {
            return;
        }
        this.f5671c = true;
        this.f5672d.close();
        this.f5670b.v();
    }

    @Override // b3.k, b3.j
    public i e() {
        return this.f5670b;
    }

    @Override // b3.F
    public G f() {
        return this.f5672d.f();
    }

    @Override // b3.k
    public void g0(long j3) throws EOFException {
        if (!v(j3)) {
            throw new EOFException();
        }
    }

    public long i(byte b4, long j3, long j4) {
        if (this.f5671c) {
            throw new IllegalStateException("closed");
        }
        if (!(0 <= j3 && j4 >= j3)) {
            throw new IllegalArgumentException(("fromIndex=" + j3 + " toIndex=" + j4).toString());
        }
        while (j3 < j4) {
            long jC0 = this.f5670b.c0(b4, j3, j4);
            if (jC0 != -1) {
                return jC0;
            }
            long jF0 = this.f5670b.F0();
            if (jF0 >= j4 || this.f5672d.x(this.f5670b, 8192) == -1) {
                return -1L;
            }
            j3 = Math.max(j3, jF0);
        }
        return -1L;
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        return !this.f5671c;
    }

    @Override // b3.k
    public void j(i iVar, long j3) throws EOFException {
        D2.h.f(iVar, "sink");
        try {
            g0(j3);
            this.f5670b.j(iVar, j3);
        } catch (EOFException e4) {
            iVar.T(this.f5670b);
            throw e4;
        }
    }

    @Override // b3.k
    public void m(byte[] bArr) throws EOFException {
        D2.h.f(bArr, "sink");
        try {
            g0(bArr.length);
            this.f5670b.m(bArr);
        } catch (EOFException e4) {
            int i3 = 0;
            while (this.f5670b.F0() > 0) {
                i iVar = this.f5670b;
                int iW0 = iVar.w0(bArr, i3, (int) iVar.F0());
                if (iW0 == -1) {
                    throw new AssertionError();
                }
                i3 += iW0;
            }
            throw e4;
        }
    }

    public int o() throws EOFException {
        g0(4L);
        return this.f5670b.A0();
    }

    @Override // b3.k
    public long o0() throws EOFException {
        byte bA0;
        g0(1L);
        int i3 = 0;
        while (true) {
            int i4 = i3 + 1;
            if (!v(i4)) {
                break;
            }
            bA0 = this.f5670b.a0(i3);
            if ((bA0 < ((byte) 48) || bA0 > ((byte) 57)) && ((bA0 < ((byte) 97) || bA0 > ((byte) 102)) && (bA0 < ((byte) 65) || bA0 > ((byte) 70)))) {
                break;
            }
            i3 = i4;
        }
        if (i3 == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected leading [0-9a-fA-F] character but was 0x");
            String string = Integer.toString(bA0, AbstractC0189a.a(AbstractC0189a.a(16)));
            D2.h.e(string, "java.lang.Integer.toStri…(this, checkRadix(radix))");
            sb.append(string);
            throw new NumberFormatException(sb.toString());
        }
        return this.f5670b.o0();
    }

    @Override // b3.k
    public l p(long j3) throws EOFException {
        g0(j3);
        return this.f5670b.p(j3);
    }

    @Override // b3.k
    public String p0(Charset charset) {
        D2.h.f(charset, "charset");
        this.f5670b.T(this.f5672d);
        return this.f5670b.p0(charset);
    }

    public short q() throws EOFException {
        g0(2L);
        return this.f5670b.B0();
    }

    @Override // b3.k
    public InputStream q0() {
        return new a();
    }

    @Override // b3.k
    public byte r0() throws EOFException {
        g0(1L);
        return this.f5670b.r0();
    }

    @Override // java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) {
        D2.h.f(byteBuffer, "sink");
        if (this.f5670b.F0() == 0 && this.f5672d.x(this.f5670b, 8192) == -1) {
            return -1;
        }
        return this.f5670b.read(byteBuffer);
    }

    @Override // b3.k
    public void s(long j3) throws EOFException {
        if (this.f5671c) {
            throw new IllegalStateException("closed");
        }
        while (j3 > 0) {
            if (this.f5670b.F0() == 0 && this.f5672d.x(this.f5670b, 8192) == -1) {
                throw new EOFException();
            }
            long jMin = Math.min(j3, this.f5670b.F0());
            this.f5670b.s(jMin);
            j3 -= jMin;
        }
    }

    public String toString() {
        return "buffer(" + this.f5672d + ')';
    }

    public boolean v(long j3) {
        if (!(j3 >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + j3).toString());
        }
        if (this.f5671c) {
            throw new IllegalStateException("closed");
        }
        while (this.f5670b.F0() < j3) {
            if (this.f5672d.x(this.f5670b, 8192) == -1) {
                return false;
            }
        }
        return true;
    }

    @Override // b3.F
    public long x(i iVar, long j3) {
        D2.h.f(iVar, "sink");
        if (!(j3 >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + j3).toString());
        }
        if (this.f5671c) {
            throw new IllegalStateException("closed");
        }
        if (this.f5670b.F0() == 0 && this.f5672d.x(this.f5670b, 8192) == -1) {
            return -1L;
        }
        return this.f5670b.x(iVar, Math.min(j3, this.f5670b.F0()));
    }

    public static final class a extends InputStream {
        a() {
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            z zVar = z.this;
            if (zVar.f5671c) {
                throw new IOException("closed");
            }
            return (int) Math.min(zVar.f5670b.F0(), Integer.MAX_VALUE);
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws EOFException {
            z.this.close();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            z zVar = z.this;
            if (zVar.f5671c) {
                throw new IOException("closed");
            }
            if (zVar.f5670b.F0() == 0) {
                z zVar2 = z.this;
                if (zVar2.f5672d.x(zVar2.f5670b, 8192) == -1) {
                    return -1;
                }
            }
            return z.this.f5670b.r0() & 255;
        }

        public String toString() {
            return z.this + ".inputStream()";
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i3, int i4) throws IOException {
            D2.h.f(bArr, "data");
            if (!z.this.f5671c) {
                AbstractC0323f.b(bArr.length, i3, i4);
                if (z.this.f5670b.F0() == 0) {
                    z zVar = z.this;
                    if (zVar.f5672d.x(zVar.f5670b, 8192) == -1) {
                        return -1;
                    }
                }
                return z.this.f5670b.w0(bArr, i3, i4);
            }
            throw new IOException("closed");
        }
    }
}
