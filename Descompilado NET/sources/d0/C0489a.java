package d0;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: renamed from: d0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0489a extends FilterInputStream {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f9061b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f9062c;

    public C0489a(InputStream inputStream, int i3) {
        super(inputStream);
        inputStream.getClass();
        if (i3 < 0) {
            throw new IllegalArgumentException("limit must be >= 0");
        }
        this.f9061b = i3;
        this.f9062c = -1;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() {
        return Math.min(((FilterInputStream) this).in.available(), this.f9061b);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void mark(int i3) {
        if (((FilterInputStream) this).in.markSupported()) {
            ((FilterInputStream) this).in.mark(i3);
            this.f9062c = this.f9061b;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.f9061b == 0) {
            return -1;
        }
        int i3 = ((FilterInputStream) this).in.read();
        if (i3 != -1) {
            this.f9061b--;
        }
        return i3;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void reset() throws IOException {
        if (!((FilterInputStream) this).in.markSupported()) {
            throw new IOException("mark is not supported");
        }
        if (this.f9062c == -1) {
            throw new IOException("mark not set");
        }
        ((FilterInputStream) this).in.reset();
        this.f9061b = this.f9062c;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j3) throws IOException {
        long jSkip = ((FilterInputStream) this).in.skip(Math.min(j3, this.f9061b));
        this.f9061b = (int) (((long) this.f9061b) - jSkip);
        return jSkip;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i3, int i4) throws IOException {
        int i5 = this.f9061b;
        if (i5 == 0) {
            return -1;
        }
        int i6 = ((FilterInputStream) this).in.read(bArr, i3, Math.min(i4, i5));
        if (i6 > 0) {
            this.f9061b -= i6;
        }
        return i6;
    }
}
