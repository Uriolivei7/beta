package M2;

import M2.t;
import java.io.Closeable;
import java.util.List;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class D implements Closeable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private C0193d f915b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final B f916c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final A f917d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final String f918e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final int f919f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final s f920g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final t f921h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final E f922i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final D f923j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final D f924k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final D f925l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final long f926m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final long f927n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final R2.c f928o;

    public D(B b4, A a4, String str, int i3, s sVar, t tVar, E e4, D d4, D d5, D d6, long j3, long j4, R2.c cVar) {
        D2.h.f(b4, "request");
        D2.h.f(a4, "protocol");
        D2.h.f(str, "message");
        D2.h.f(tVar, "headers");
        this.f916c = b4;
        this.f917d = a4;
        this.f918e = str;
        this.f919f = i3;
        this.f920g = sVar;
        this.f921h = tVar;
        this.f922i = e4;
        this.f923j = d4;
        this.f924k = d5;
        this.f925l = d6;
        this.f926m = j3;
        this.f927n = j4;
        this.f928o = cVar;
    }

    public static /* synthetic */ String c0(D d4, String str, String str2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            str2 = null;
        }
        return d4.a0(str, str2);
    }

    public final int A() {
        return this.f919f;
    }

    public final R2.c D() {
        return this.f928o;
    }

    public final s P() {
        return this.f920g;
    }

    public final String X(String str) {
        return c0(this, str, null, 2, null);
    }

    public final E a() {
        return this.f922i;
    }

    public final String a0(String str, String str2) {
        D2.h.f(str, "name");
        String strA = this.f921h.a(str);
        return strA != null ? strA : str2;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        E e4 = this.f922i;
        if (e4 == null) {
            throw new IllegalStateException("response is not eligible for a body and must not be closed");
        }
        e4.close();
    }

    public final t d0() {
        return this.f921h;
    }

    public final boolean e0() {
        int i3 = this.f919f;
        return 200 <= i3 && 299 >= i3;
    }

    public final int i() {
        return this.f919f;
    }

    public final String n0() {
        return this.f918e;
    }

    public final t o() {
        return this.f921h;
    }

    public final E q() {
        return this.f922i;
    }

    public final D t0() {
        return this.f923j;
    }

    public String toString() {
        return "Response{protocol=" + this.f917d + ", code=" + this.f919f + ", message=" + this.f918e + ", url=" + this.f916c.l() + '}';
    }

    public final a u0() {
        return new a(this);
    }

    public final C0193d v() {
        C0193d c0193d = this.f915b;
        if (c0193d != null) {
            return c0193d;
        }
        C0193d c0193dB = C0193d.f1005p.b(this.f921h);
        this.f915b = c0193dB;
        return c0193dB;
    }

    public final D v0() {
        return this.f925l;
    }

    public final A w0() {
        return this.f917d;
    }

    public final long x0() {
        return this.f927n;
    }

    public final D y() {
        return this.f924k;
    }

    public final B y0() {
        return this.f916c;
    }

    public final List z() {
        String str;
        t tVar = this.f921h;
        int i3 = this.f919f;
        if (i3 == 401) {
            str = "WWW-Authenticate";
        } else {
            if (i3 != 407) {
                return AbstractC0717n.g();
            }
            str = "Proxy-Authenticate";
        }
        return S2.e.a(tVar, str);
    }

    public final long z0() {
        return this.f926m;
    }

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private B f929a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private A f930b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f931c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private String f932d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private s f933e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private t.a f934f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private E f935g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private D f936h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private D f937i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private D f938j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private long f939k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        private long f940l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        private R2.c f941m;

        public a() {
            this.f931c = -1;
            this.f934f = new t.a();
        }

        private final void e(D d4) {
            if (d4 != null) {
                if (!(d4.q() == null)) {
                    throw new IllegalArgumentException("priorResponse.body != null");
                }
            }
        }

        private final void f(String str, D d4) {
            if (d4 != null) {
                if (!(d4.q() == null)) {
                    throw new IllegalArgumentException((str + ".body != null").toString());
                }
                if (!(d4.t0() == null)) {
                    throw new IllegalArgumentException((str + ".networkResponse != null").toString());
                }
                if (!(d4.y() == null)) {
                    throw new IllegalArgumentException((str + ".cacheResponse != null").toString());
                }
                if (d4.v0() == null) {
                    return;
                }
                throw new IllegalArgumentException((str + ".priorResponse != null").toString());
            }
        }

        public a a(String str, String str2) {
            D2.h.f(str, "name");
            D2.h.f(str2, "value");
            this.f934f.a(str, str2);
            return this;
        }

        public a b(E e4) {
            this.f935g = e4;
            return this;
        }

        public D c() {
            int i3 = this.f931c;
            if (!(i3 >= 0)) {
                throw new IllegalStateException(("code < 0: " + this.f931c).toString());
            }
            B b4 = this.f929a;
            if (b4 == null) {
                throw new IllegalStateException("request == null");
            }
            A a4 = this.f930b;
            if (a4 == null) {
                throw new IllegalStateException("protocol == null");
            }
            String str = this.f932d;
            if (str != null) {
                return new D(b4, a4, str, i3, this.f933e, this.f934f.e(), this.f935g, this.f936h, this.f937i, this.f938j, this.f939k, this.f940l, this.f941m);
            }
            throw new IllegalStateException("message == null");
        }

        public a d(D d4) {
            f("cacheResponse", d4);
            this.f937i = d4;
            return this;
        }

        public a g(int i3) {
            this.f931c = i3;
            return this;
        }

        public final int h() {
            return this.f931c;
        }

        public a i(s sVar) {
            this.f933e = sVar;
            return this;
        }

        public a j(String str, String str2) {
            D2.h.f(str, "name");
            D2.h.f(str2, "value");
            this.f934f.i(str, str2);
            return this;
        }

        public a k(t tVar) {
            D2.h.f(tVar, "headers");
            this.f934f = tVar.e();
            return this;
        }

        public final void l(R2.c cVar) {
            D2.h.f(cVar, "deferredTrailers");
            this.f941m = cVar;
        }

        public a m(String str) {
            D2.h.f(str, "message");
            this.f932d = str;
            return this;
        }

        public a n(D d4) {
            f("networkResponse", d4);
            this.f936h = d4;
            return this;
        }

        public a o(D d4) {
            e(d4);
            this.f938j = d4;
            return this;
        }

        public a p(A a4) {
            D2.h.f(a4, "protocol");
            this.f930b = a4;
            return this;
        }

        public a q(long j3) {
            this.f940l = j3;
            return this;
        }

        public a r(B b4) {
            D2.h.f(b4, "request");
            this.f929a = b4;
            return this;
        }

        public a s(long j3) {
            this.f939k = j3;
            return this;
        }

        public a(D d4) {
            D2.h.f(d4, "response");
            this.f931c = -1;
            this.f929a = d4.y0();
            this.f930b = d4.w0();
            this.f931c = d4.A();
            this.f932d = d4.n0();
            this.f933e = d4.P();
            this.f934f = d4.d0().e();
            this.f935g = d4.q();
            this.f936h = d4.t0();
            this.f937i = d4.y();
            this.f938j = d4.v0();
            this.f939k = d4.z0();
            this.f940l = d4.x0();
            this.f941m = d4.D();
        }
    }
}
