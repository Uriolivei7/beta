package U2;

import U2.d;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class j implements Closeable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final b3.i f2649b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f2650c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f2651d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final d.b f2652e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final b3.j f2653f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final boolean f2654g;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final a f2648i = new a(null);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final Logger f2647h = Logger.getLogger(e.class.getName());

    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public j(b3.j jVar, boolean z3) {
        D2.h.f(jVar, "sink");
        this.f2653f = jVar;
        this.f2654g = z3;
        b3.i iVar = new b3.i();
        this.f2649b = iVar;
        this.f2650c = 16384;
        this.f2652e = new d.b(0, false, iVar, 3, null);
    }

    private final void d0(int i3, long j3) {
        while (j3 > 0) {
            long jMin = Math.min(this.f2650c, j3);
            j3 -= jMin;
            v(i3, (int) jMin, 9, j3 == 0 ? 4 : 0);
            this.f2653f.Q(this.f2649b, jMin);
        }
    }

    public final int A() {
        return this.f2650c;
    }

    public final synchronized void D(boolean z3, int i3, int i4) {
        if (this.f2651d) {
            throw new IOException("closed");
        }
        v(0, 8, 6, z3 ? 1 : 0);
        this.f2653f.E(i3);
        this.f2653f.E(i4);
        this.f2653f.flush();
    }

    public final synchronized void P(int i3, int i4, List list) {
        D2.h.f(list, "requestHeaders");
        if (this.f2651d) {
            throw new IOException("closed");
        }
        this.f2652e.g(list);
        long jF0 = this.f2649b.F0();
        int iMin = (int) Math.min(((long) this.f2650c) - 4, jF0);
        long j3 = iMin;
        v(i3, iMin + 4, 5, jF0 == j3 ? 4 : 0);
        this.f2653f.E(i4 & Integer.MAX_VALUE);
        this.f2653f.Q(this.f2649b, j3);
        if (jF0 > j3) {
            d0(i3, jF0 - j3);
        }
    }

    public final synchronized void X(int i3, b bVar) {
        D2.h.f(bVar, "errorCode");
        if (this.f2651d) {
            throw new IOException("closed");
        }
        if (!(bVar.a() != -1)) {
            throw new IllegalArgumentException("Failed requirement.");
        }
        v(i3, 4, 3, 0);
        this.f2653f.E(bVar.a());
        this.f2653f.flush();
    }

    public final synchronized void a(m mVar) {
        try {
            D2.h.f(mVar, "peerSettings");
            if (this.f2651d) {
                throw new IOException("closed");
            }
            this.f2650c = mVar.e(this.f2650c);
            if (mVar.b() != -1) {
                this.f2652e.e(mVar.b());
            }
            v(0, 0, 4, 1);
            this.f2653f.flush();
        } catch (Throwable th) {
            throw th;
        }
    }

    public final synchronized void a0(m mVar) {
        try {
            D2.h.f(mVar, "settings");
            if (this.f2651d) {
                throw new IOException("closed");
            }
            int i3 = 0;
            v(0, mVar.i() * 6, 4, 0);
            while (i3 < 10) {
                if (mVar.f(i3)) {
                    this.f2653f.w(i3 != 4 ? i3 != 7 ? i3 : 4 : 3);
                    this.f2653f.E(mVar.a(i3));
                }
                i3++;
            }
            this.f2653f.flush();
        } catch (Throwable th) {
            throw th;
        }
    }

    public final synchronized void c0(int i3, long j3) {
        if (this.f2651d) {
            throw new IOException("closed");
        }
        if (!(j3 != 0 && j3 <= 2147483647L)) {
            throw new IllegalArgumentException(("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: " + j3).toString());
        }
        v(i3, 4, 8, 0);
        this.f2653f.E((int) j3);
        this.f2653f.flush();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        this.f2651d = true;
        this.f2653f.close();
    }

    public final synchronized void flush() {
        if (this.f2651d) {
            throw new IOException("closed");
        }
        this.f2653f.flush();
    }

    public final synchronized void i() {
        try {
            if (this.f2651d) {
                throw new IOException("closed");
            }
            if (this.f2654g) {
                Logger logger = f2647h;
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(N2.c.q(">> CONNECTION " + e.f2485a.k(), new Object[0]));
                }
                this.f2653f.u(e.f2485a);
                this.f2653f.flush();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public final synchronized void o(boolean z3, int i3, b3.i iVar, int i4) {
        if (this.f2651d) {
            throw new IOException("closed");
        }
        q(i3, z3 ? 1 : 0, iVar, i4);
    }

    public final void q(int i3, int i4, b3.i iVar, int i5) {
        v(i3, i5, 0, i4);
        if (i5 > 0) {
            b3.j jVar = this.f2653f;
            D2.h.c(iVar);
            jVar.Q(iVar, i5);
        }
    }

    public final void v(int i3, int i4, int i5, int i6) {
        Logger logger = f2647h;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(e.f2489e.c(false, i3, i4, i5, i6));
        }
        if (!(i4 <= this.f2650c)) {
            throw new IllegalArgumentException(("FRAME_SIZE_ERROR length > " + this.f2650c + ": " + i4).toString());
        }
        if (!((((int) 2147483648L) & i3) == 0)) {
            throw new IllegalArgumentException(("reserved bit set: " + i3).toString());
        }
        N2.c.Y(this.f2653f, i4);
        this.f2653f.L(i5 & 255);
        this.f2653f.L(i6 & 255);
        this.f2653f.E(i3 & Integer.MAX_VALUE);
    }

    public final synchronized void y(int i3, b bVar, byte[] bArr) {
        try {
            D2.h.f(bVar, "errorCode");
            D2.h.f(bArr, "debugData");
            if (this.f2651d) {
                throw new IOException("closed");
            }
            boolean z3 = true;
            if (!(bVar.a() != -1)) {
                throw new IllegalArgumentException("errorCode.httpCode == -1");
            }
            v(0, bArr.length + 8, 7, 0);
            this.f2653f.E(i3);
            this.f2653f.E(bVar.a());
            if (bArr.length != 0) {
                z3 = false;
            }
            if (!z3) {
                this.f2653f.R(bArr);
            }
            this.f2653f.flush();
        } finally {
        }
    }

    public final synchronized void z(boolean z3, int i3, List list) {
        D2.h.f(list, "headerBlock");
        if (this.f2651d) {
            throw new IOException("closed");
        }
        this.f2652e.g(list);
        long jF0 = this.f2649b.F0();
        long jMin = Math.min(this.f2650c, jF0);
        int i4 = jF0 == jMin ? 4 : 0;
        if (z3) {
            i4 |= 1;
        }
        v(i3, (int) jMin, 1, i4);
        this.f2653f.Q(this.f2649b, jMin);
        if (jF0 > jMin) {
            d0(i3, jF0 - jMin);
        }
    }
}
