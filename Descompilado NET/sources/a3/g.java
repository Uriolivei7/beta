package a3;

import b3.i;
import b3.k;
import b3.l;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class g implements Closeable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f2938b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f2939c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f2940d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f2941e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f2942f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f2943g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final i f2944h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final i f2945i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private c f2946j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final byte[] f2947k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final i.a f2948l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final boolean f2949m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final k f2950n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final a f2951o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final boolean f2952p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final boolean f2953q;

    public interface a {
        void c(l lVar);

        void d(l lVar);

        void e(String str);

        void g(l lVar);

        void h(int i3, String str);
    }

    public g(boolean z3, k kVar, a aVar, boolean z4, boolean z5) {
        D2.h.f(kVar, "source");
        D2.h.f(aVar, "frameCallback");
        this.f2949m = z3;
        this.f2950n = kVar;
        this.f2951o = aVar;
        this.f2952p = z4;
        this.f2953q = z5;
        this.f2944h = new i();
        this.f2945i = new i();
        this.f2947k = z3 ? null : new byte[4];
        this.f2948l = z3 ? null : new i.a();
    }

    private final void i() throws ProtocolException, EOFException {
        short sY;
        String strO;
        long j3 = this.f2940d;
        if (j3 > 0) {
            this.f2950n.j(this.f2944h, j3);
            if (!this.f2949m) {
                i iVar = this.f2944h;
                i.a aVar = this.f2948l;
                D2.h.c(aVar);
                iVar.x0(aVar);
                this.f2948l.o(0L);
                f fVar = f.f2937a;
                i.a aVar2 = this.f2948l;
                byte[] bArr = this.f2947k;
                D2.h.c(bArr);
                fVar.b(aVar2, bArr);
                this.f2948l.close();
            }
        }
        switch (this.f2939c) {
            case 8:
                long jF0 = this.f2944h.F0();
                if (jF0 == 1) {
                    throw new ProtocolException("Malformed close payload length of 1.");
                }
                if (jF0 != 0) {
                    sY = this.f2944h.Y();
                    strO = this.f2944h.O();
                    String strA = f.f2937a.a(sY);
                    if (strA != null) {
                        throw new ProtocolException(strA);
                    }
                } else {
                    sY = 1005;
                    strO = "";
                }
                this.f2951o.h(sY, strO);
                this.f2938b = true;
                return;
            case 9:
                this.f2951o.c(this.f2944h.z0());
                return;
            case 10:
                this.f2951o.d(this.f2944h.z0());
                return;
            default:
                throw new ProtocolException("Unknown control opcode: " + N2.c.N(this.f2939c));
        }
    }

    private final void o() throws IOException {
        boolean z3;
        if (this.f2938b) {
            throw new IOException("closed");
        }
        long jH = this.f2950n.f().h();
        this.f2950n.f().b();
        try {
            int iB = N2.c.b(this.f2950n.r0(), 255);
            this.f2950n.f().g(jH, TimeUnit.NANOSECONDS);
            int i3 = iB & 15;
            this.f2939c = i3;
            boolean z4 = (iB & 128) != 0;
            this.f2941e = z4;
            boolean z5 = (iB & 8) != 0;
            this.f2942f = z5;
            if (z5 && !z4) {
                throw new ProtocolException("Control frames must be final.");
            }
            boolean z6 = (iB & 64) != 0;
            if (i3 == 1 || i3 == 2) {
                if (!z6) {
                    z3 = false;
                } else {
                    if (!this.f2952p) {
                        throw new ProtocolException("Unexpected rsv1 flag");
                    }
                    z3 = true;
                }
                this.f2943g = z3;
            } else if (z6) {
                throw new ProtocolException("Unexpected rsv1 flag");
            }
            if ((iB & 32) != 0) {
                throw new ProtocolException("Unexpected rsv2 flag");
            }
            if ((iB & 16) != 0) {
                throw new ProtocolException("Unexpected rsv3 flag");
            }
            int iB2 = N2.c.b(this.f2950n.r0(), 255);
            boolean z7 = (iB2 & 128) != 0;
            if (z7 == this.f2949m) {
                throw new ProtocolException(this.f2949m ? "Server-sent frames must not be masked." : "Client-sent frames must be masked.");
            }
            long j3 = iB2 & 127;
            this.f2940d = j3;
            if (j3 == 126) {
                this.f2940d = N2.c.c(this.f2950n.Y(), 65535);
            } else if (j3 == 127) {
                long jF = this.f2950n.F();
                this.f2940d = jF;
                if (jF < 0) {
                    throw new ProtocolException("Frame length 0x" + N2.c.O(this.f2940d) + " > 0x7FFFFFFFFFFFFFFF");
                }
            }
            if (this.f2942f && this.f2940d > 125) {
                throw new ProtocolException("Control frame must be less than 125B.");
            }
            if (z7) {
                k kVar = this.f2950n;
                byte[] bArr = this.f2947k;
                D2.h.c(bArr);
                kVar.m(bArr);
            }
        } catch (Throwable th) {
            this.f2950n.f().g(jH, TimeUnit.NANOSECONDS);
            throw th;
        }
    }

    private final void q() throws IOException {
        while (!this.f2938b) {
            long j3 = this.f2940d;
            if (j3 > 0) {
                this.f2950n.j(this.f2945i, j3);
                if (!this.f2949m) {
                    i iVar = this.f2945i;
                    i.a aVar = this.f2948l;
                    D2.h.c(aVar);
                    iVar.x0(aVar);
                    this.f2948l.o(this.f2945i.F0() - this.f2940d);
                    f fVar = f.f2937a;
                    i.a aVar2 = this.f2948l;
                    byte[] bArr = this.f2947k;
                    D2.h.c(bArr);
                    fVar.b(aVar2, bArr);
                    this.f2948l.close();
                }
            }
            if (this.f2941e) {
                return;
            }
            y();
            if (this.f2939c != 0) {
                throw new ProtocolException("Expected continuation opcode. Got: " + N2.c.N(this.f2939c));
            }
        }
        throw new IOException("closed");
    }

    private final void v() throws IOException {
        int i3 = this.f2939c;
        if (i3 != 1 && i3 != 2) {
            throw new ProtocolException("Unknown opcode: " + N2.c.N(i3));
        }
        q();
        if (this.f2943g) {
            c cVar = this.f2946j;
            if (cVar == null) {
                cVar = new c(this.f2953q);
                this.f2946j = cVar;
            }
            cVar.a(this.f2945i);
        }
        if (i3 == 1) {
            this.f2951o.e(this.f2945i.O());
        } else {
            this.f2951o.g(this.f2945i.z0());
        }
    }

    private final void y() throws IOException {
        while (!this.f2938b) {
            o();
            if (!this.f2942f) {
                return;
            } else {
                i();
            }
        }
    }

    public final void a() {
        o();
        if (this.f2942f) {
            i();
        } else {
            v();
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        c cVar = this.f2946j;
        if (cVar != null) {
            cVar.close();
        }
    }
}
