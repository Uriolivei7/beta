package R0;

import android.os.SharedMemory;
import android.system.ErrnoException;
import android.util.Log;
import java.io.Closeable;
import java.nio.ByteBuffer;

/* JADX INFO: renamed from: R0.f, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0204f implements v, Closeable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private SharedMemory f1953b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private ByteBuffer f1954c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final long f1955d;

    public C0204f(int i3) {
        X.k.b(Boolean.valueOf(i3 > 0));
        try {
            SharedMemory sharedMemoryCreate = SharedMemory.create("AshmemMemoryChunk", i3);
            this.f1953b = sharedMemoryCreate;
            this.f1954c = sharedMemoryCreate.mapReadWrite();
            this.f1955d = System.identityHashCode(this);
        } catch (ErrnoException e4) {
            throw new RuntimeException("Fail to create AshmemMemory", e4);
        }
    }

    private void a(int i3, v vVar, int i4, int i5) {
        if (!(vVar instanceof C0204f)) {
            throw new IllegalArgumentException("Cannot copy two incompatible MemoryChunks");
        }
        X.k.i(!b());
        X.k.i(!vVar.b());
        X.k.g(this.f1954c);
        X.k.g(vVar.q());
        w.b(i3, vVar.i(), i4, i5, i());
        this.f1954c.position(i3);
        vVar.q().position(i4);
        byte[] bArr = new byte[i5];
        this.f1954c.get(bArr, 0, i5);
        vVar.q().put(bArr, 0, i5);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x000e  */
    @Override // R0.v
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized boolean b() {
        /*
            r1 = this;
            monitor-enter(r1)
            java.nio.ByteBuffer r0 = r1.f1954c     // Catch: java.lang.Throwable -> Lc
            if (r0 == 0) goto Le
            android.os.SharedMemory r0 = r1.f1953b     // Catch: java.lang.Throwable -> Lc
            if (r0 != 0) goto La
            goto Le
        La:
            r0 = 0
            goto Lf
        Lc:
            r0 = move-exception
            goto L11
        Le:
            r0 = 1
        Lf:
            monitor-exit(r1)
            return r0
        L11:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> Lc
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: R0.C0204f.b():boolean");
    }

    @Override // R0.v
    public synchronized int c(int i3, byte[] bArr, int i4, int i5) {
        int iA;
        X.k.g(bArr);
        X.k.g(this.f1954c);
        iA = w.a(i3, i5, i());
        w.b(i3, bArr.length, i4, iA, i());
        this.f1954c.position(i3);
        this.f1954c.get(bArr, i4, iA);
        return iA;
    }

    @Override // R0.v, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        try {
            if (!b()) {
                SharedMemory sharedMemory = this.f1953b;
                if (sharedMemory != null) {
                    sharedMemory.close();
                }
                ByteBuffer byteBuffer = this.f1954c;
                if (byteBuffer != null) {
                    SharedMemory.unmap(byteBuffer);
                }
                this.f1954c = null;
                this.f1953b = null;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // R0.v
    public synchronized byte g(int i3) {
        X.k.i(!b());
        X.k.b(Boolean.valueOf(i3 >= 0));
        X.k.b(Boolean.valueOf(i3 < i()));
        X.k.g(this.f1954c);
        return this.f1954c.get(i3);
    }

    @Override // R0.v
    public int i() {
        X.k.g(this.f1953b);
        return this.f1953b.getSize();
    }

    @Override // R0.v
    public long o() {
        return this.f1955d;
    }

    @Override // R0.v
    public ByteBuffer q() {
        return this.f1954c;
    }

    @Override // R0.v
    public synchronized int v(int i3, byte[] bArr, int i4, int i5) {
        int iA;
        X.k.g(bArr);
        X.k.g(this.f1954c);
        iA = w.a(i3, i5, i());
        w.b(i3, bArr.length, i4, iA, i());
        this.f1954c.position(i3);
        this.f1954c.put(bArr, i4, iA);
        return iA;
    }

    @Override // R0.v
    public void y(int i3, v vVar, int i4, int i5) {
        X.k.g(vVar);
        if (vVar.o() == o()) {
            Log.w("AshmemMemoryChunk", "Copying from AshmemMemoryChunk " + Long.toHexString(o()) + " to AshmemMemoryChunk " + Long.toHexString(vVar.o()) + " which are the same ");
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
        throw new UnsupportedOperationException("Cannot get the pointer of an  AshmemMemoryChunk");
    }

    public C0204f() {
        this.f1953b = null;
        this.f1954c = null;
        this.f1955d = System.identityHashCode(this);
    }
}
