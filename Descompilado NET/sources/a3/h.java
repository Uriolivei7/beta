package a3;

import b3.i;
import b3.j;
import b3.l;
import java.io.Closeable;
import java.io.IOException;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public final class h implements Closeable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final i f2954b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final i f2955c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f2956d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private a f2957e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final byte[] f2958f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final i.a f2959g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final boolean f2960h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final j f2961i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final Random f2962j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final boolean f2963k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final boolean f2964l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final long f2965m;

    public h(boolean z3, j jVar, Random random, boolean z4, boolean z5, long j3) {
        D2.h.f(jVar, "sink");
        D2.h.f(random, "random");
        this.f2960h = z3;
        this.f2961i = jVar;
        this.f2962j = random;
        this.f2963k = z4;
        this.f2964l = z5;
        this.f2965m = j3;
        this.f2954b = new i();
        this.f2955c = jVar.e();
        this.f2958f = z3 ? new byte[4] : null;
        this.f2959g = z3 ? new i.a() : null;
    }

    private final void i(int i3, l lVar) throws IOException {
        if (this.f2956d) {
            throw new IOException("closed");
        }
        int iV = lVar.v();
        if (!(((long) iV) <= 125)) {
            throw new IllegalArgumentException("Payload size must be less than or equal to 125");
        }
        this.f2955c.L(i3 | 128);
        if (this.f2960h) {
            this.f2955c.L(iV | 128);
            Random random = this.f2962j;
            byte[] bArr = this.f2958f;
            D2.h.c(bArr);
            random.nextBytes(bArr);
            this.f2955c.R(this.f2958f);
            if (iV > 0) {
                long jF0 = this.f2955c.F0();
                this.f2955c.u(lVar);
                i iVar = this.f2955c;
                i.a aVar = this.f2959g;
                D2.h.c(aVar);
                iVar.x0(aVar);
                this.f2959g.o(jF0);
                f.f2937a.b(this.f2959g, this.f2958f);
                this.f2959g.close();
            }
        } else {
            this.f2955c.L(iV);
            this.f2955c.u(lVar);
        }
        this.f2961i.flush();
    }

    public final void a(int i3, l lVar) {
        l lVarZ0 = l.f5638e;
        if (i3 != 0 || lVar != null) {
            if (i3 != 0) {
                f.f2937a.c(i3);
            }
            i iVar = new i();
            iVar.w(i3);
            if (lVar != null) {
                iVar.u(lVar);
            }
            lVarZ0 = iVar.z0();
        }
        try {
            i(8, lVarZ0);
        } finally {
            this.f2956d = true;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws Throwable {
        a aVar = this.f2957e;
        if (aVar != null) {
            aVar.close();
        }
    }

    public final void o(int i3, l lVar) throws IOException {
        D2.h.f(lVar, "data");
        if (this.f2956d) {
            throw new IOException("closed");
        }
        this.f2954b.u(lVar);
        int i4 = i3 | 128;
        if (this.f2963k && lVar.v() >= this.f2965m) {
            a aVar = this.f2957e;
            if (aVar == null) {
                aVar = new a(this.f2964l);
                this.f2957e = aVar;
            }
            aVar.a(this.f2954b);
            i4 = i3 | 192;
        }
        long jF0 = this.f2954b.F0();
        this.f2955c.L(i4);
        int i5 = this.f2960h ? 128 : 0;
        if (jF0 <= 125) {
            this.f2955c.L(i5 | ((int) jF0));
        } else if (jF0 <= 65535) {
            this.f2955c.L(i5 | 126);
            this.f2955c.w((int) jF0);
        } else {
            this.f2955c.L(i5 | 127);
            this.f2955c.Q0(jF0);
        }
        if (this.f2960h) {
            Random random = this.f2962j;
            byte[] bArr = this.f2958f;
            D2.h.c(bArr);
            random.nextBytes(bArr);
            this.f2955c.R(this.f2958f);
            if (jF0 > 0) {
                i iVar = this.f2954b;
                i.a aVar2 = this.f2959g;
                D2.h.c(aVar2);
                iVar.x0(aVar2);
                this.f2959g.o(0L);
                f.f2937a.b(this.f2959g, this.f2958f);
                this.f2959g.close();
            }
        }
        this.f2955c.Q(this.f2954b, jF0);
        this.f2961i.t();
    }

    public final void q(l lVar) throws IOException {
        D2.h.f(lVar, "payload");
        i(9, lVar);
    }

    public final void v(l lVar) throws IOException {
        D2.h.f(lVar, "payload");
        i(10, lVar);
    }
}
