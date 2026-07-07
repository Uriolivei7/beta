package U2;

import U2.d;
import b3.F;
import b3.G;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.chromium.support_lib_boundary.WebSettingsBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
public final class h implements Closeable {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final Logger f2607f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final a f2608g = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final b f2609b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final d.a f2610c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final b3.k f2611d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final boolean f2612e;

    public static final class a {
        private a() {
        }

        public final Logger a() {
            return h.f2607f;
        }

        public final int b(int i3, int i4, int i5) throws IOException {
            if ((i4 & 8) != 0) {
                i3--;
            }
            if (i5 <= i3) {
                return i3 - i5;
            }
            throw new IOException("PROTOCOL_ERROR padding " + i5 + " > remaining length " + i3);
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public interface c {
        void b(int i3, U2.b bVar);

        void c();

        void d(boolean z3, int i3, int i4);

        void e(boolean z3, int i3, b3.k kVar, int i4);

        void f(int i3, int i4, int i5, boolean z3);

        void g(boolean z3, m mVar);

        void h(boolean z3, int i3, int i4, List list);

        void i(int i3, long j3);

        void j(int i3, U2.b bVar, b3.l lVar);

        void k(int i3, int i4, List list);
    }

    static {
        Logger logger = Logger.getLogger(e.class.getName());
        D2.h.e(logger, "Logger.getLogger(Http2::class.java.name)");
        f2607f = logger;
    }

    public h(b3.k kVar, boolean z3) {
        D2.h.f(kVar, "source");
        this.f2611d = kVar;
        this.f2612e = z3;
        b bVar = new b(kVar);
        this.f2609b = bVar;
        this.f2610c = new d.a(bVar, 4096, 0, 4, null);
    }

    private final void A(c cVar, int i3, int i4, int i5) throws IOException {
        if (i3 != 8) {
            throw new IOException("TYPE_PING length != 8: " + i3);
        }
        if (i5 != 0) {
            throw new IOException("TYPE_PING streamId != 0");
        }
        cVar.d((i4 & 1) != 0, this.f2611d.B(), this.f2611d.B());
    }

    private final void D(c cVar, int i3) {
        int iB = this.f2611d.B();
        cVar.f(i3, iB & Integer.MAX_VALUE, N2.c.b(this.f2611d.r0(), 255) + 1, (((int) 2147483648L) & iB) != 0);
    }

    private final void P(c cVar, int i3, int i4, int i5) throws IOException {
        if (i3 == 5) {
            if (i5 == 0) {
                throw new IOException("TYPE_PRIORITY streamId == 0");
            }
            D(cVar, i5);
        } else {
            throw new IOException("TYPE_PRIORITY length: " + i3 + " != 5");
        }
    }

    private final void X(c cVar, int i3, int i4, int i5) throws IOException {
        if (i5 == 0) {
            throw new IOException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0");
        }
        int iB = (i4 & 8) != 0 ? N2.c.b(this.f2611d.r0(), 255) : 0;
        cVar.k(i5, this.f2611d.B() & Integer.MAX_VALUE, y(f2608g.b(i3 - 4, i4, iB), iB, i4, i5));
    }

    private final void a0(c cVar, int i3, int i4, int i5) throws IOException {
        if (i3 != 4) {
            throw new IOException("TYPE_RST_STREAM length: " + i3 + " != 4");
        }
        if (i5 == 0) {
            throw new IOException("TYPE_RST_STREAM streamId == 0");
        }
        int iB = this.f2611d.B();
        U2.b bVarA = U2.b.f2452r.a(iB);
        if (bVarA != null) {
            cVar.b(i5, bVarA);
            return;
        }
        throw new IOException("TYPE_RST_STREAM unexpected error code: " + iB);
    }

    private final void c0(c cVar, int i3, int i4, int i5) throws IOException {
        int iB;
        if (i5 != 0) {
            throw new IOException("TYPE_SETTINGS streamId != 0");
        }
        if ((i4 & 1) != 0) {
            if (i3 != 0) {
                throw new IOException("FRAME_SIZE_ERROR ack frame should be empty!");
            }
            cVar.c();
            return;
        }
        if (i3 % 6 != 0) {
            throw new IOException("TYPE_SETTINGS length % 6 != 0: " + i3);
        }
        m mVar = new m();
        H2.a aVarH = H2.d.h(H2.d.i(0, i3), 6);
        int iA = aVarH.a();
        int iB2 = aVarH.b();
        int iC = aVarH.c();
        if (iC < 0 ? iA >= iB2 : iA <= iB2) {
            while (true) {
                int iC2 = N2.c.c(this.f2611d.Y(), 65535);
                iB = this.f2611d.B();
                if (iC2 != 2) {
                    if (iC2 == 3) {
                        iC2 = 4;
                    } else if (iC2 != 4) {
                        if (iC2 == 5 && (iB < 16384 || iB > 16777215)) {
                            break;
                        }
                    } else {
                        if (iB < 0) {
                            throw new IOException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1");
                        }
                        iC2 = 7;
                    }
                } else if (iB != 0 && iB != 1) {
                    throw new IOException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1");
                }
                mVar.h(iC2, iB);
                if (iA == iB2) {
                    break;
                } else {
                    iA += iC;
                }
            }
            throw new IOException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: " + iB);
        }
        cVar.g(false, mVar);
    }

    private final void d0(c cVar, int i3, int i4, int i5) throws IOException {
        if (i3 != 4) {
            throw new IOException("TYPE_WINDOW_UPDATE length !=4: " + i3);
        }
        long jD = N2.c.d(this.f2611d.B(), 2147483647L);
        if (jD == 0) {
            throw new IOException("windowSizeIncrement was 0");
        }
        cVar.i(i5, jD);
    }

    private final void q(c cVar, int i3, int i4, int i5) throws IOException {
        if (i5 == 0) {
            throw new IOException("PROTOCOL_ERROR: TYPE_DATA streamId == 0");
        }
        boolean z3 = (i4 & 1) != 0;
        if ((i4 & 32) != 0) {
            throw new IOException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA");
        }
        int iB = (i4 & 8) != 0 ? N2.c.b(this.f2611d.r0(), 255) : 0;
        cVar.e(z3, i5, this.f2611d, f2608g.b(i3, i4, iB));
        this.f2611d.s(iB);
    }

    private final void v(c cVar, int i3, int i4, int i5) throws IOException {
        if (i3 < 8) {
            throw new IOException("TYPE_GOAWAY length < 8: " + i3);
        }
        if (i5 != 0) {
            throw new IOException("TYPE_GOAWAY streamId != 0");
        }
        int iB = this.f2611d.B();
        int iB2 = this.f2611d.B();
        int i6 = i3 - 8;
        U2.b bVarA = U2.b.f2452r.a(iB2);
        if (bVarA == null) {
            throw new IOException("TYPE_GOAWAY unexpected error code: " + iB2);
        }
        b3.l lVarP = b3.l.f5638e;
        if (i6 > 0) {
            lVarP = this.f2611d.p(i6);
        }
        cVar.j(iB, bVarA, lVarP);
    }

    private final List y(int i3, int i4, int i5, int i6) throws IOException {
        this.f2609b.q(i3);
        b bVar = this.f2609b;
        bVar.v(bVar.a());
        this.f2609b.y(i4);
        this.f2609b.o(i5);
        this.f2609b.z(i6);
        this.f2610c.k();
        return this.f2610c.e();
    }

    private final void z(c cVar, int i3, int i4, int i5) throws IOException {
        if (i5 == 0) {
            throw new IOException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0");
        }
        boolean z3 = (i4 & 1) != 0;
        int iB = (i4 & 8) != 0 ? N2.c.b(this.f2611d.r0(), 255) : 0;
        if ((i4 & 32) != 0) {
            D(cVar, i5);
            i3 -= 5;
        }
        cVar.h(z3, i5, -1, y(f2608g.b(i3, i4, iB), iB, i4, i5));
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f2611d.close();
    }

    public final boolean i(boolean z3, c cVar) throws IOException {
        D2.h.f(cVar, "handler");
        try {
            this.f2611d.g0(9L);
            int iH = N2.c.H(this.f2611d);
            if (iH > 16384) {
                throw new IOException("FRAME_SIZE_ERROR: " + iH);
            }
            int iB = N2.c.b(this.f2611d.r0(), 255);
            int iB2 = N2.c.b(this.f2611d.r0(), 255);
            int iB3 = this.f2611d.B() & Integer.MAX_VALUE;
            Logger logger = f2607f;
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(e.f2489e.c(true, iB3, iH, iB, iB2));
            }
            if (z3 && iB != 4) {
                throw new IOException("Expected a SETTINGS frame but was " + e.f2489e.b(iB));
            }
            switch (iB) {
                case WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY /* 0 */:
                    q(cVar, iH, iB2, iB3);
                    return true;
                case 1:
                    z(cVar, iH, iB2, iB3);
                    return true;
                case 2:
                    P(cVar, iH, iB2, iB3);
                    return true;
                case 3:
                    a0(cVar, iH, iB2, iB3);
                    return true;
                case 4:
                    c0(cVar, iH, iB2, iB3);
                    return true;
                case 5:
                    X(cVar, iH, iB2, iB3);
                    return true;
                case 6:
                    A(cVar, iH, iB2, iB3);
                    return true;
                case 7:
                    v(cVar, iH, iB2, iB3);
                    return true;
                case 8:
                    d0(cVar, iH, iB2, iB3);
                    return true;
                default:
                    this.f2611d.s(iH);
                    return true;
            }
        } catch (EOFException unused) {
            return false;
        }
    }

    public final void o(c cVar) throws IOException {
        D2.h.f(cVar, "handler");
        if (this.f2612e) {
            if (!i(true, cVar)) {
                throw new IOException("Required SETTINGS preface not received");
            }
            return;
        }
        b3.k kVar = this.f2611d;
        b3.l lVar = e.f2485a;
        b3.l lVarP = kVar.p(lVar.v());
        Logger logger = f2607f;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(N2.c.q("<< CONNECTION " + lVarP.k(), new Object[0]));
        }
        if (D2.h.b(lVar, lVarP)) {
            return;
        }
        throw new IOException("Expected a connection header but was " + lVarP.z());
    }

    public static final class b implements F {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f2613b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f2614c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f2615d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f2616e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private int f2617f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final b3.k f2618g;

        public b(b3.k kVar) {
            D2.h.f(kVar, "source");
            this.f2618g = kVar;
        }

        private final void i() throws IOException {
            int i3 = this.f2615d;
            int iH = N2.c.H(this.f2618g);
            this.f2616e = iH;
            this.f2613b = iH;
            int iB = N2.c.b(this.f2618g.r0(), 255);
            this.f2614c = N2.c.b(this.f2618g.r0(), 255);
            a aVar = h.f2608g;
            if (aVar.a().isLoggable(Level.FINE)) {
                aVar.a().fine(e.f2489e.c(true, this.f2615d, this.f2613b, iB, this.f2614c));
            }
            int iB2 = this.f2618g.B() & Integer.MAX_VALUE;
            this.f2615d = iB2;
            if (iB == 9) {
                if (iB2 != i3) {
                    throw new IOException("TYPE_CONTINUATION streamId changed");
                }
            } else {
                throw new IOException(iB + " != TYPE_CONTINUATION");
            }
        }

        public final int a() {
            return this.f2616e;
        }

        @Override // b3.F
        public G f() {
            return this.f2618g.f();
        }

        public final void o(int i3) {
            this.f2614c = i3;
        }

        public final void q(int i3) {
            this.f2616e = i3;
        }

        public final void v(int i3) {
            this.f2613b = i3;
        }

        @Override // b3.F
        public long x(b3.i iVar, long j3) throws IOException {
            D2.h.f(iVar, "sink");
            while (true) {
                int i3 = this.f2616e;
                if (i3 != 0) {
                    long jX = this.f2618g.x(iVar, Math.min(j3, i3));
                    if (jX == -1) {
                        return -1L;
                    }
                    this.f2616e -= (int) jX;
                    return jX;
                }
                this.f2618g.s(this.f2617f);
                this.f2617f = 0;
                if ((this.f2614c & 4) != 0) {
                    return -1L;
                }
                i();
            }
        }

        public final void y(int i3) {
            this.f2617f = i3;
        }

        public final void z(int i3) {
            this.f2615d = i3;
        }

        @Override // b3.F, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }
    }
}
