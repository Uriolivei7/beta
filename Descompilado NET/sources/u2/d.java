package U2;

import b3.F;
import b3.t;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0711h;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final c[] f2464a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Map f2465b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final d f2466c;

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final List f2467a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final b3.k f2468b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public c[] f2469c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f2470d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public int f2471e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public int f2472f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final int f2473g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private int f2474h;

        public a(F f3, int i3) {
            this(f3, i3, 0, 4, null);
        }

        private final void a() {
            int i3 = this.f2474h;
            int i4 = this.f2472f;
            if (i3 < i4) {
                if (i3 == 0) {
                    b();
                } else {
                    d(i4 - i3);
                }
            }
        }

        private final void b() {
            AbstractC0711h.k(this.f2469c, null, 0, 0, 6, null);
            this.f2470d = this.f2469c.length - 1;
            this.f2471e = 0;
            this.f2472f = 0;
        }

        private final int c(int i3) {
            return this.f2470d + 1 + i3;
        }

        private final int d(int i3) {
            int i4;
            int i5 = 0;
            if (i3 > 0) {
                int length = this.f2469c.length;
                while (true) {
                    length--;
                    i4 = this.f2470d;
                    if (length < i4 || i3 <= 0) {
                        break;
                    }
                    c cVar = this.f2469c[length];
                    D2.h.c(cVar);
                    int i6 = cVar.f2461a;
                    i3 -= i6;
                    this.f2472f -= i6;
                    this.f2471e--;
                    i5++;
                }
                c[] cVarArr = this.f2469c;
                System.arraycopy(cVarArr, i4 + 1, cVarArr, i4 + 1 + i5, this.f2471e);
                this.f2470d += i5;
            }
            return i5;
        }

        private final b3.l f(int i3) throws IOException {
            if (h(i3)) {
                return d.f2466c.c()[i3].f2462b;
            }
            int iC = c(i3 - d.f2466c.c().length);
            if (iC >= 0) {
                c[] cVarArr = this.f2469c;
                if (iC < cVarArr.length) {
                    c cVar = cVarArr[iC];
                    D2.h.c(cVar);
                    return cVar.f2462b;
                }
            }
            throw new IOException("Header index too large " + (i3 + 1));
        }

        private final void g(int i3, c cVar) {
            this.f2467a.add(cVar);
            int i4 = cVar.f2461a;
            if (i3 != -1) {
                c cVar2 = this.f2469c[c(i3)];
                D2.h.c(cVar2);
                i4 -= cVar2.f2461a;
            }
            int i5 = this.f2474h;
            if (i4 > i5) {
                b();
                return;
            }
            int iD = d((this.f2472f + i4) - i5);
            if (i3 == -1) {
                int i6 = this.f2471e + 1;
                c[] cVarArr = this.f2469c;
                if (i6 > cVarArr.length) {
                    c[] cVarArr2 = new c[cVarArr.length * 2];
                    System.arraycopy(cVarArr, 0, cVarArr2, cVarArr.length, cVarArr.length);
                    this.f2470d = this.f2469c.length - 1;
                    this.f2469c = cVarArr2;
                }
                int i7 = this.f2470d;
                this.f2470d = i7 - 1;
                this.f2469c[i7] = cVar;
                this.f2471e++;
            } else {
                this.f2469c[i3 + c(i3) + iD] = cVar;
            }
            this.f2472f += i4;
        }

        private final boolean h(int i3) {
            return i3 >= 0 && i3 <= d.f2466c.c().length - 1;
        }

        private final int i() {
            return N2.c.b(this.f2468b.r0(), 255);
        }

        private final void l(int i3) throws IOException {
            if (h(i3)) {
                this.f2467a.add(d.f2466c.c()[i3]);
                return;
            }
            int iC = c(i3 - d.f2466c.c().length);
            if (iC >= 0) {
                c[] cVarArr = this.f2469c;
                if (iC < cVarArr.length) {
                    List list = this.f2467a;
                    c cVar = cVarArr[iC];
                    D2.h.c(cVar);
                    list.add(cVar);
                    return;
                }
            }
            throw new IOException("Header index too large " + (i3 + 1));
        }

        private final void n(int i3) {
            g(-1, new c(f(i3), j()));
        }

        private final void o() {
            g(-1, new c(d.f2466c.a(j()), j()));
        }

        private final void p(int i3) throws IOException {
            this.f2467a.add(new c(f(i3), j()));
        }

        private final void q() throws IOException {
            this.f2467a.add(new c(d.f2466c.a(j()), j()));
        }

        public final List e() {
            List listE0 = AbstractC0717n.e0(this.f2467a);
            this.f2467a.clear();
            return listE0;
        }

        public final b3.l j() {
            int i3 = i();
            boolean z3 = (i3 & 128) == 128;
            long jM = m(i3, 127);
            if (!z3) {
                return this.f2468b.p(jM);
            }
            b3.i iVar = new b3.i();
            k.f2658d.b(this.f2468b, jM, iVar);
            return iVar.z0();
        }

        public final void k() throws IOException {
            while (!this.f2468b.J()) {
                int iB = N2.c.b(this.f2468b.r0(), 255);
                if (iB == 128) {
                    throw new IOException("index == 0");
                }
                if ((iB & 128) == 128) {
                    l(m(iB, 127) - 1);
                } else if (iB == 64) {
                    o();
                } else if ((iB & 64) == 64) {
                    n(m(iB, 63) - 1);
                } else if ((iB & 32) == 32) {
                    int iM = m(iB, 31);
                    this.f2474h = iM;
                    if (iM < 0 || iM > this.f2473g) {
                        throw new IOException("Invalid dynamic table size update " + this.f2474h);
                    }
                    a();
                } else if (iB == 16 || iB == 0) {
                    q();
                } else {
                    p(m(iB, 15) - 1);
                }
            }
        }

        public final int m(int i3, int i4) {
            int i5 = i3 & i4;
            if (i5 < i4) {
                return i5;
            }
            int i6 = 0;
            while (true) {
                int i7 = i();
                if ((i7 & 128) == 0) {
                    return i4 + (i7 << i6);
                }
                i4 += (i7 & 127) << i6;
                i6 += 7;
            }
        }

        public a(F f3, int i3, int i4) {
            D2.h.f(f3, "source");
            this.f2473g = i3;
            this.f2474h = i4;
            this.f2467a = new ArrayList();
            this.f2468b = t.d(f3);
            this.f2469c = new c[8];
            this.f2470d = r2.length - 1;
        }

        public /* synthetic */ a(F f3, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
            this(f3, i3, (i5 & 4) != 0 ? i3 : i4);
        }
    }

    public static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f2475a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private boolean f2476b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public int f2477c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public c[] f2478d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f2479e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public int f2480f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public int f2481g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public int f2482h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private final boolean f2483i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private final b3.i f2484j;

        public b(int i3, b3.i iVar) {
            this(i3, false, iVar, 2, null);
        }

        private final void a() {
            int i3 = this.f2477c;
            int i4 = this.f2481g;
            if (i3 < i4) {
                if (i3 == 0) {
                    b();
                } else {
                    c(i4 - i3);
                }
            }
        }

        private final void b() {
            AbstractC0711h.k(this.f2478d, null, 0, 0, 6, null);
            this.f2479e = this.f2478d.length - 1;
            this.f2480f = 0;
            this.f2481g = 0;
        }

        private final int c(int i3) {
            int i4;
            int i5 = 0;
            if (i3 > 0) {
                int length = this.f2478d.length;
                while (true) {
                    length--;
                    i4 = this.f2479e;
                    if (length < i4 || i3 <= 0) {
                        break;
                    }
                    c cVar = this.f2478d[length];
                    D2.h.c(cVar);
                    i3 -= cVar.f2461a;
                    int i6 = this.f2481g;
                    c cVar2 = this.f2478d[length];
                    D2.h.c(cVar2);
                    this.f2481g = i6 - cVar2.f2461a;
                    this.f2480f--;
                    i5++;
                }
                c[] cVarArr = this.f2478d;
                System.arraycopy(cVarArr, i4 + 1, cVarArr, i4 + 1 + i5, this.f2480f);
                c[] cVarArr2 = this.f2478d;
                int i7 = this.f2479e;
                Arrays.fill(cVarArr2, i7 + 1, i7 + 1 + i5, (Object) null);
                this.f2479e += i5;
            }
            return i5;
        }

        private final void d(c cVar) {
            int i3 = cVar.f2461a;
            int i4 = this.f2477c;
            if (i3 > i4) {
                b();
                return;
            }
            c((this.f2481g + i3) - i4);
            int i5 = this.f2480f + 1;
            c[] cVarArr = this.f2478d;
            if (i5 > cVarArr.length) {
                c[] cVarArr2 = new c[cVarArr.length * 2];
                System.arraycopy(cVarArr, 0, cVarArr2, cVarArr.length, cVarArr.length);
                this.f2479e = this.f2478d.length - 1;
                this.f2478d = cVarArr2;
            }
            int i6 = this.f2479e;
            this.f2479e = i6 - 1;
            this.f2478d[i6] = cVar;
            this.f2480f++;
            this.f2481g += i3;
        }

        public final void e(int i3) {
            this.f2482h = i3;
            int iMin = Math.min(i3, 16384);
            int i4 = this.f2477c;
            if (i4 == iMin) {
                return;
            }
            if (iMin < i4) {
                this.f2475a = Math.min(this.f2475a, iMin);
            }
            this.f2476b = true;
            this.f2477c = iMin;
            a();
        }

        public final void f(b3.l lVar) {
            D2.h.f(lVar, "data");
            if (this.f2483i) {
                k kVar = k.f2658d;
                if (kVar.d(lVar) < lVar.v()) {
                    b3.i iVar = new b3.i();
                    kVar.c(lVar, iVar);
                    b3.l lVarZ0 = iVar.z0();
                    h(lVarZ0.v(), 127, 128);
                    this.f2484j.u(lVarZ0);
                    return;
                }
            }
            h(lVar.v(), 127, 0);
            this.f2484j.u(lVar);
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x0077  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final void g(java.util.List r13) {
            /*
                Method dump skipped, instruction units count: 264
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: U2.d.b.g(java.util.List):void");
        }

        public final void h(int i3, int i4, int i5) {
            if (i3 < i4) {
                this.f2484j.L(i3 | i5);
                return;
            }
            this.f2484j.L(i5 | i4);
            int i6 = i3 - i4;
            while (i6 >= 128) {
                this.f2484j.L(128 | (i6 & 127));
                i6 >>>= 7;
            }
            this.f2484j.L(i6);
        }

        public b(b3.i iVar) {
            this(0, false, iVar, 3, null);
        }

        public b(int i3, boolean z3, b3.i iVar) {
            D2.h.f(iVar, "out");
            this.f2482h = i3;
            this.f2483i = z3;
            this.f2484j = iVar;
            this.f2475a = Integer.MAX_VALUE;
            this.f2477c = i3;
            this.f2478d = new c[8];
            this.f2479e = r2.length - 1;
        }

        public /* synthetic */ b(int i3, boolean z3, b3.i iVar, int i4, DefaultConstructorMarker defaultConstructorMarker) {
            this((i4 & 1) != 0 ? 4096 : i3, (i4 & 2) != 0 ? true : z3, iVar);
        }
    }

    static {
        d dVar = new d();
        f2466c = dVar;
        c cVar = new c(c.f2459i, "");
        b3.l lVar = c.f2456f;
        c cVar2 = new c(lVar, "GET");
        c cVar3 = new c(lVar, "POST");
        b3.l lVar2 = c.f2457g;
        c cVar4 = new c(lVar2, "/");
        c cVar5 = new c(lVar2, "/index.html");
        b3.l lVar3 = c.f2458h;
        c cVar6 = new c(lVar3, "http");
        c cVar7 = new c(lVar3, "https");
        b3.l lVar4 = c.f2455e;
        f2464a = new c[]{cVar, cVar2, cVar3, cVar4, cVar5, cVar6, cVar7, new c(lVar4, "200"), new c(lVar4, "204"), new c(lVar4, "206"), new c(lVar4, "304"), new c(lVar4, "400"), new c(lVar4, "404"), new c(lVar4, "500"), new c("accept-charset", ""), new c("accept-encoding", "gzip, deflate"), new c("accept-language", ""), new c("accept-ranges", ""), new c("accept", ""), new c("access-control-allow-origin", ""), new c("age", ""), new c("allow", ""), new c("authorization", ""), new c("cache-control", ""), new c("content-disposition", ""), new c("content-encoding", ""), new c("content-language", ""), new c("content-length", ""), new c("content-location", ""), new c("content-range", ""), new c("content-type", ""), new c("cookie", ""), new c("date", ""), new c("etag", ""), new c("expect", ""), new c("expires", ""), new c("from", ""), new c("host", ""), new c("if-match", ""), new c("if-modified-since", ""), new c("if-none-match", ""), new c("if-range", ""), new c("if-unmodified-since", ""), new c("last-modified", ""), new c("link", ""), new c("location", ""), new c("max-forwards", ""), new c("proxy-authenticate", ""), new c("proxy-authorization", ""), new c("range", ""), new c("referer", ""), new c("refresh", ""), new c("retry-after", ""), new c("server", ""), new c("set-cookie", ""), new c("strict-transport-security", ""), new c("transfer-encoding", ""), new c("user-agent", ""), new c("vary", ""), new c("via", ""), new c("www-authenticate", "")};
        f2465b = dVar.d();
    }

    private d() {
    }

    private final Map d() {
        c[] cVarArr = f2464a;
        LinkedHashMap linkedHashMap = new LinkedHashMap(cVarArr.length);
        int length = cVarArr.length;
        for (int i3 = 0; i3 < length; i3++) {
            c[] cVarArr2 = f2464a;
            if (!linkedHashMap.containsKey(cVarArr2[i3].f2462b)) {
                linkedHashMap.put(cVarArr2[i3].f2462b, Integer.valueOf(i3));
            }
        }
        Map mapUnmodifiableMap = Collections.unmodifiableMap(linkedHashMap);
        D2.h.e(mapUnmodifiableMap, "Collections.unmodifiableMap(result)");
        return mapUnmodifiableMap;
    }

    public final b3.l a(b3.l lVar) throws IOException {
        D2.h.f(lVar, "name");
        int iV = lVar.v();
        for (int i3 = 0; i3 < iV; i3++) {
            byte b4 = (byte) 65;
            byte b5 = (byte) 90;
            byte bF = lVar.f(i3);
            if (b4 <= bF && b5 >= bF) {
                throw new IOException("PROTOCOL_ERROR response malformed: mixed case name: " + lVar.z());
            }
        }
        return lVar;
    }

    public final Map b() {
        return f2465b;
    }

    public final c[] c() {
        return f2464a;
    }
}
