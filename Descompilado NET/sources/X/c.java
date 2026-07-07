package X;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
public class c extends FilterOutputStream {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private long f2734b;

    public c(OutputStream outputStream) {
        super(outputStream);
        this.f2734b = 0L;
    }

    public long a() {
        return this.f2734b;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        ((FilterOutputStream) this).out.close();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i3, int i4) throws IOException {
        ((FilterOutputStream) this).out.write(bArr, i3, i4);
        this.f2734b += (long) i4;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i3) throws IOException {
        ((FilterOutputStream) this).out.write(i3);
        this.f2734b++;
    }
}
