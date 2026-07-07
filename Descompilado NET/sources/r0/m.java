package R0;

import android.util.Log;
import java.io.Closeable;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class m implements v, Closeable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private ByteBuffer f1974b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f1975c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final long f1976d = System.identityHashCode(this);

    public m(int i3) {
        this.f1974b = ByteBuffer.allocateDirect(i3);
        this.f1975c = i3;
    }

    private void a(int i3, v vVar, int i4, int i5) {
        if (!(vVar instanceof m)) {
            throw new IllegalArgumentException("Cannot copy two incompatible MemoryChunks");
        }
        X.k.i(!b());
        X.k.i(!vVar.b());
        X.k.g(this.f1974b);
        w.b(i3, vVar.i(), i4, i5, this.f1975c);
        this.f1974b.position(i3);
        ByteBuffer byteBuffer = (ByteBuffer) X.k.g(vVar.q());
        byteBuffer.position(i4);
        byte[] bArr = new byte[i5];
        this.f1974b.get(bArr, 0, i5);
        byteBuffer.put(bArr, 0, i5);
    }

    @Override // R0.v
    public synchronized boolean b() {
        return this.f1974b == null;
    }

    @Override // R0.v
    public synchronized int c(int i3, byte[] bArr, int i4, int i5) {
        int iA;
        X.k.g(bArr);
        X.k.i(!b());
        X.k.g(this.f1974b);
        iA = w.a(i3, i5, this.f1975c);
        w.b(i3, bArr.length, i4, iA, this.f1975c);
        this.f1974b.position(i3);
        this.f1974b.get(bArr, i4, iA);
        return iA;
    }

    @Override // R0.v, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        this.f1974b = null;
    }

    @Override // R0.v
    public synchronized byte g(int i3) {
        X.k.i(!b());
        X.k.b(Boolean.valueOf(i3 >= 0));
        X.k.b(Boolean.valueOf(i3 < this.f1975c));
        X.k.g(this.f1974b);
        return this.f1974b.get(i3);
    }

    @Override // R0.v
    public int i() {
        return this.f1975c;
    }

    @Override // R0.v
    public long o() {
        return this.f1976d;
    }

    @Override // R0.v
    public synchronized ByteBuffer q() {
        return this.f1974b;
    }

    @Override // R0.v
    public synchronized int v(int i3, byte[] bArr, int i4, int i5) {
        int iA;
        X.k.g(bArr);
        X.k.i(!b());
        X.k.g(this.f1974b);
        iA = w.a(i3, i5, this.f1975c);
        w.b(i3, bArr.length, i4, iA, this.f1975c);
        this.f1974b.position(i3);
        this.f1974b.put(bArr, i4, iA);
        return iA;
    }

    @Override // R0.v
    public void y(int i3, v vVar, int i4, int i5) {
        X.k.g(vVar);
        if (vVar.o() == o()) {
            Log.w("BufferMemoryChunk", "Copying from BufferMemoryChunk " + Long.toHexString(o()) + " to BufferMemoryChunk " + Long.toHexString(vVar.o()) + " which are the same ");
            X.k.b(Boolean.FALSE);
        }
        if (vVar.o() < o()) {
            synchronized (vVar) {
                synchronized (this) {
                    a(i3, vVar, i4, i5);
                }
            }
        } else {
            synchronized (this) {
                synchronized (vVar) {
                    a(i3, vVar, i4, i5);
                }
            }
        }
    }

    @Override // R0.v
    public long z() {
        throw new UnsupportedOperationException("Cannot get the pointer of a BufferMemoryChunk");
    }
}
