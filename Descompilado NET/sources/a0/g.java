package a0;

import b0.InterfaceC0313h;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class g extends InputStream {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final InputStream f2854b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final byte[] f2855c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final InterfaceC0313h f2856d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f2857e = 0;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f2858f = 0;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f2859g = false;

    public g(InputStream inputStream, byte[] bArr, InterfaceC0313h interfaceC0313h) {
        this.f2854b = (InputStream) X.k.g(inputStream);
        this.f2855c = (byte[]) X.k.g(bArr);
        this.f2856d = (InterfaceC0313h) X.k.g(interfaceC0313h);
    }

    private boolean a() throws IOException {
        if (this.f2858f < this.f2857e) {
            return true;
        }
        int i3 = this.f2854b.read(this.f2855c);
        if (i3 <= 0) {
            return false;
        }
        this.f2857e = i3;
        this.f2858f = 0;
        return true;
    }

    private void i() throws IOException {
        if (this.f2859g) {
            throw new IOException("stream already closed");
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        X.k.i(this.f2858f <= this.f2857e);
        i();
        return (this.f2857e - this.f2858f) + this.f2854b.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.f2859g) {
            return;
        }
        this.f2859g = true;
        this.f2856d.a(this.f2855c);
        super.close();
    }

    protected void finalize() throws Throwable {
        if (!this.f2859g) {
            Y.a.m("PooledByteInputStream", "Finalized without closing");
            close();
        }
        super.finalize();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        X.k.i(this.f2858f <= this.f2857e);
        i();
        if (!a()) {
            return -1;
        }
        byte[] bArr = this.f2855c;
        int i3 = this.f2858f;
        this.f2858f = i3 + 1;
        return bArr[i3] & 255;
    }

    @Override // java.io.InputStream
    public long skip(long j3) throws IOException {
        X.k.i(this.f2858f <= this.f2857e);
        i();
        int i3 = this.f2857e;
        int i4 = this.f2858f;
        long j4 = i3 - i4;
        if (j4 >= j3) {
            this.f2858f = (int) (((long) i4) + j3);
            return j3;
        }
        this.f2858f = i3;
        return j4 + this.f2854b.skip(j3 - j4);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i3, int i4) throws IOException {
        X.k.i(this.f2858f <= this.f2857e);
        i();
        if (!a()) {
            return -1;
        }
        int iMin = Math.min(this.f2857e - this.f2858f, i4);
        System.arraycopy(this.f2855c, this.f2858f, bArr, i3, iMin);
        this.f2858f += iMin;
        return iMin;
    }
}
