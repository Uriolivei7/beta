package R0;

import a0.InterfaceC0207a;
import com.facebook.imagepipeline.memory.AshmemMemoryChunkPool;
import com.facebook.imagepipeline.memory.BufferMemoryChunkPool;
import com.facebook.imagepipeline.memory.NativeMemoryChunkPool;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes.dex */
public class D {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final B f1938a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private com.facebook.imagepipeline.memory.f f1939b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private i f1940c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private com.facebook.imagepipeline.memory.f f1941d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private com.facebook.imagepipeline.memory.f f1942e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private a0.i f1943f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private a0.l f1944g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private InterfaceC0207a f1945h;

    public D(B b4) {
        this.f1938a = (B) X.k.g(b4);
    }

    private com.facebook.imagepipeline.memory.f a() {
        if (this.f1939b == null) {
            try {
                this.f1939b = (com.facebook.imagepipeline.memory.f) AshmemMemoryChunkPool.class.getConstructor(a0.d.class, E.class, F.class).newInstance(this.f1938a.i(), this.f1938a.g(), this.f1938a.h());
            } catch (ClassNotFoundException unused) {
                this.f1939b = null;
            } catch (IllegalAccessException unused2) {
                this.f1939b = null;
            } catch (InstantiationException unused3) {
                this.f1939b = null;
            } catch (NoSuchMethodException unused4) {
                this.f1939b = null;
            } catch (InvocationTargetException unused5) {
                this.f1939b = null;
            }
        }
        return this.f1939b;
    }

    private com.facebook.imagepipeline.memory.f e(int i3) {
        if (i3 == 0) {
            return f();
        }
        if (i3 == 1) {
            return c();
        }
        if (i3 == 2) {
            return a();
        }
        throw new IllegalArgumentException("Invalid MemoryChunkType");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0047  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public R0.i b() {
        /*
            Method dump skipped, instruction units count: 222
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: R0.D.b():R0.i");
    }

    public com.facebook.imagepipeline.memory.f c() {
        if (this.f1941d == null) {
            try {
                this.f1941d = (com.facebook.imagepipeline.memory.f) BufferMemoryChunkPool.class.getConstructor(a0.d.class, E.class, F.class).newInstance(this.f1938a.i(), this.f1938a.g(), this.f1938a.h());
            } catch (ClassNotFoundException unused) {
                this.f1941d = null;
            } catch (IllegalAccessException unused2) {
                this.f1941d = null;
            } catch (InstantiationException unused3) {
                this.f1941d = null;
            } catch (NoSuchMethodException unused4) {
                this.f1941d = null;
            } catch (InvocationTargetException unused5) {
                this.f1941d = null;
            }
        }
        return this.f1941d;
    }

    public int d() {
        return this.f1938a.f().f1952g;
    }

    public com.facebook.imagepipeline.memory.f f() {
        if (this.f1942e == null) {
            try {
                this.f1942e = (com.facebook.imagepipeline.memory.f) NativeMemoryChunkPool.class.getConstructor(a0.d.class, E.class, F.class).newInstance(this.f1938a.i(), this.f1938a.g(), this.f1938a.h());
            } catch (ClassNotFoundException e4) {
                Y.a.n("PoolFactory", "", e4);
                this.f1942e = null;
            } catch (IllegalAccessException e5) {
                Y.a.n("PoolFactory", "", e5);
                this.f1942e = null;
            } catch (InstantiationException e6) {
                Y.a.n("PoolFactory", "", e6);
                this.f1942e = null;
            } catch (NoSuchMethodException e7) {
                Y.a.n("PoolFactory", "", e7);
                this.f1942e = null;
            } catch (InvocationTargetException e8) {
                Y.a.n("PoolFactory", "", e8);
                this.f1942e = null;
            }
        }
        return this.f1942e;
    }

    public a0.i g(int i3) {
        if (this.f1943f == null) {
            com.facebook.imagepipeline.memory.f fVarE = e(i3);
            X.k.h(fVarE, "failed to get pool for chunk type: " + i3);
            this.f1943f = new y(fVarE, h());
        }
        return this.f1943f;
    }

    public a0.l h() {
        if (this.f1944g == null) {
            this.f1944g = new a0.l(i());
        }
        return this.f1944g;
    }

    public InterfaceC0207a i() {
        if (this.f1945h == null) {
            this.f1945h = new com.facebook.imagepipeline.memory.e(this.f1938a.i(), this.f1938a.j(), this.f1938a.k());
        }
        return this.f1945h;
    }
}
