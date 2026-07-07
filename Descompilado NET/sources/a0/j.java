package a0;

import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class j extends InputStream {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final h f2860b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    int f2861c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    int f2862d;

    public j(h hVar) {
        X.k.b(Boolean.valueOf(!hVar.b()));
        this.f2860b = (h) X.k.g(hVar);
        this.f2861c = 0;
        this.f2862d = 0;
    }

    @Override // java.io.InputStream
    public int available() {
        return this.f2860b.size() - this.f2861c;
    }

    @Override // java.io.InputStream
    public void mark(int i3) {
        this.f2862d = this.f2861c;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.InputStream
    public int read() {
        if (available() <= 0) {
            return -1;
        }
        h hVar = this.f2860b;
        int i3 = this.f2861c;
        this.f2861c = i3 + 1;
        return hVar.g(i3) & 255;
    }

    @Override // java.io.InputStream
    public void reset() {
        this.f2861c = this.f2862d;
    }

    @Override // java.io.InputStream
    public long skip(long j3) {
        X.k.b(Boolean.valueOf(j3 >= 0));
        int iMin = Math.min((int) j3, available());
        this.f2861c += iMin;
        return iMin;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i3, int i4) {
        if (i3 >= 0 && i4 >= 0 && i3 + i4 <= bArr.length) {
            int iAvailable = available();
            if (iAvailable <= 0) {
                return -1;
            }
            if (i4 <= 0) {
                return 0;
            }
            int iMin = Math.min(iAvailable, i4);
            this.f2860b.c(this.f2861c, bArr, i3, iMin);
            this.f2861c += iMin;
            return iMin;
        }
        throw new ArrayIndexOutOfBoundsException("length=" + bArr.length + "; regionStart=" + i3 + "; regionLength=" + i4);
    }
}
