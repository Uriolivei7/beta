package P2;

import D2.h;
import K2.o;
import M2.B;
import M2.C0193d;
import M2.D;
import M2.t;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f1718c = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final B f1719a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final D f1720b;

    public static final class a {
        private a() {
        }

        /* JADX WARN: Removed duplicated region for block: B:24:0x003b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final boolean a(M2.D r5, M2.B r6) {
            /*
                r4 = this;
                java.lang.String r0 = "response"
                D2.h.f(r5, r0)
                java.lang.String r0 = "request"
                D2.h.f(r6, r0)
                int r0 = r5.A()
                r1 = 200(0xc8, float:2.8E-43)
                r2 = 0
                if (r0 == r1) goto L65
                r1 = 410(0x19a, float:5.75E-43)
                if (r0 == r1) goto L65
                r1 = 414(0x19e, float:5.8E-43)
                if (r0 == r1) goto L65
                r1 = 501(0x1f5, float:7.02E-43)
                if (r0 == r1) goto L65
                r1 = 203(0xcb, float:2.84E-43)
                if (r0 == r1) goto L65
                r1 = 204(0xcc, float:2.86E-43)
                if (r0 == r1) goto L65
                r1 = 307(0x133, float:4.3E-43)
                if (r0 == r1) goto L3b
                r1 = 308(0x134, float:4.32E-43)
                if (r0 == r1) goto L65
                r1 = 404(0x194, float:5.66E-43)
                if (r0 == r1) goto L65
                r1 = 405(0x195, float:5.68E-43)
                if (r0 == r1) goto L65
                switch(r0) {
                    case 300: goto L65;
                    case 301: goto L65;
                    case 302: goto L3b;
                    default: goto L3a;
                }
            L3a:
                return r2
            L3b:
                java.lang.String r0 = "Expires"
                r1 = 2
                r3 = 0
                java.lang.String r0 = M2.D.c0(r5, r0, r3, r1, r3)
                if (r0 != 0) goto L65
                M2.d r0 = r5.v()
                int r0 = r0.c()
                r1 = -1
                if (r0 != r1) goto L65
                M2.d r0 = r5.v()
                boolean r0 = r0.b()
                if (r0 != 0) goto L65
                M2.d r0 = r5.v()
                boolean r0 = r0.a()
                if (r0 != 0) goto L65
                return r2
            L65:
                M2.d r5 = r5.v()
                boolean r5 = r5.h()
                if (r5 != 0) goto L7a
                M2.d r5 = r6.b()
                boolean r5 = r5.h()
                if (r5 != 0) goto L7a
                r2 = 1
            L7a:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: P2.c.a.a(M2.D, M2.B):boolean");
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private Date f1721a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f1722b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private Date f1723c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private String f1724d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private Date f1725e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private long f1726f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private long f1727g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private String f1728h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private int f1729i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private final long f1730j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private final B f1731k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        private final D f1732l;

        public b(long j3, B b4, D d4) {
            h.f(b4, "request");
            this.f1730j = j3;
            this.f1731k = b4;
            this.f1732l = d4;
            this.f1729i = -1;
            if (d4 != null) {
                this.f1726f = d4.z0();
                this.f1727g = d4.x0();
                t tVarD0 = d4.d0();
                int size = tVarD0.size();
                for (int i3 = 0; i3 < size; i3++) {
                    String strB = tVarD0.b(i3);
                    String strH = tVarD0.h(i3);
                    if (o.n(strB, "Date", true)) {
                        this.f1721a = S2.c.a(strH);
                        this.f1722b = strH;
                    } else if (o.n(strB, "Expires", true)) {
                        this.f1725e = S2.c.a(strH);
                    } else if (o.n(strB, "Last-Modified", true)) {
                        this.f1723c = S2.c.a(strH);
                        this.f1724d = strH;
                    } else if (o.n(strB, "ETag", true)) {
                        this.f1728h = strH;
                    } else if (o.n(strB, "Age", true)) {
                        this.f1729i = N2.c.U(strH, -1);
                    }
                }
            }
        }

        private final long a() {
            Date date = this.f1721a;
            long jMax = date != null ? Math.max(0L, this.f1727g - date.getTime()) : 0L;
            int i3 = this.f1729i;
            if (i3 != -1) {
                jMax = Math.max(jMax, TimeUnit.SECONDS.toMillis(i3));
            }
            long j3 = this.f1727g;
            return jMax + (j3 - this.f1726f) + (this.f1730j - j3);
        }

        private final c c() {
            String str;
            if (this.f1732l == null) {
                return new c(this.f1731k, null);
            }
            if (this.f1731k.g() && this.f1732l.P() == null) {
                return new c(this.f1731k, null);
            }
            if (!c.f1718c.a(this.f1732l, this.f1731k)) {
                return new c(this.f1731k, null);
            }
            C0193d c0193dB = this.f1731k.b();
            if (c0193dB.g() || e(this.f1731k)) {
                return new c(this.f1731k, null);
            }
            C0193d c0193dV = this.f1732l.v();
            long jA = a();
            long jD = d();
            if (c0193dB.c() != -1) {
                jD = Math.min(jD, TimeUnit.SECONDS.toMillis(c0193dB.c()));
            }
            long millis = 0;
            long millis2 = c0193dB.e() != -1 ? TimeUnit.SECONDS.toMillis(c0193dB.e()) : 0L;
            if (!c0193dV.f() && c0193dB.d() != -1) {
                millis = TimeUnit.SECONDS.toMillis(c0193dB.d());
            }
            if (!c0193dV.g()) {
                long j3 = millis2 + jA;
                if (j3 < millis + jD) {
                    D.a aVarU0 = this.f1732l.u0();
                    if (j3 >= jD) {
                        aVarU0.a("Warning", "110 HttpURLConnection \"Response is stale\"");
                    }
                    if (jA > 86400000 && f()) {
                        aVarU0.a("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
                    }
                    return new c(null, aVarU0.c());
                }
            }
            String str2 = this.f1728h;
            if (str2 != null) {
                str = "If-None-Match";
            } else {
                if (this.f1723c != null) {
                    str2 = this.f1724d;
                } else {
                    if (this.f1721a == null) {
                        return new c(this.f1731k, null);
                    }
                    str2 = this.f1722b;
                }
                str = "If-Modified-Since";
            }
            t.a aVarE = this.f1731k.e().e();
            h.c(str2);
            aVarE.c(str, str2);
            return new c(this.f1731k.i().f(aVarE.e()).b(), this.f1732l);
        }

        private final long d() {
            D d4 = this.f1732l;
            h.c(d4);
            if (d4.v().c() != -1) {
                return TimeUnit.SECONDS.toMillis(r0.c());
            }
            Date date = this.f1725e;
            if (date != null) {
                Date date2 = this.f1721a;
                long time = date.getTime() - (date2 != null ? date2.getTime() : this.f1727g);
                if (time > 0) {
                    return time;
                }
                return 0L;
            }
            if (this.f1723c == null || this.f1732l.y0().l().m() != null) {
                return 0L;
            }
            Date date3 = this.f1721a;
            long time2 = date3 != null ? date3.getTime() : this.f1726f;
            Date date4 = this.f1723c;
            h.c(date4);
            long time3 = time2 - date4.getTime();
            if (time3 > 0) {
                return time3 / ((long) 10);
            }
            return 0L;
        }

        private final boolean e(B b4) {
            return (b4.d("If-Modified-Since") == null && b4.d("If-None-Match") == null) ? false : true;
        }

        private final boolean f() {
            D d4 = this.f1732l;
            h.c(d4);
            return d4.v().c() == -1 && this.f1725e == null;
        }

        public final c b() {
            c cVarC = c();
            return (cVarC.b() == null || !this.f1731k.b().i()) ? cVarC : new c(null, null);
        }
    }

    public c(B b4, D d4) {
        this.f1719a = b4;
        this.f1720b = d4;
    }

    public final D a() {
        return this.f1720b;
    }

    public final B b() {
        return this.f1719a;
    }
}
