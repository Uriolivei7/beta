package M2;

import M2.x;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class y extends C {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final x f1256g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final x f1257h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final x f1258i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final x f1259j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static final x f1260k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final byte[] f1261l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static final byte[] f1262m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static final byte[] f1263n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public static final b f1264o = new b(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final x f1265b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private long f1266c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final b3.l f1267d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final x f1268e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final List f1269f;

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final b3.l f1270a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private x f1271b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final List f1272c;

        /* JADX WARN: Multi-variable type inference failed */
        public a() {
            this(null, 1, 0 == true ? 1 : 0);
        }

        public final a a(t tVar, C c4) {
            D2.h.f(c4, "body");
            b(c.f1273c.a(tVar, c4));
            return this;
        }

        public final a b(c cVar) {
            D2.h.f(cVar, "part");
            this.f1272c.add(cVar);
            return this;
        }

        public final y c() {
            if (this.f1272c.isEmpty()) {
                throw new IllegalStateException("Multipart body must have at least one part.");
            }
            return new y(this.f1270a, this.f1271b, N2.c.R(this.f1272c));
        }

        public final a d(x xVar) {
            D2.h.f(xVar, "type");
            if (D2.h.b(xVar.g(), "multipart")) {
                this.f1271b = xVar;
                return this;
            }
            throw new IllegalArgumentException(("multipart != " + xVar).toString());
        }

        public a(String str) {
            D2.h.f(str, "boundary");
            this.f1270a = b3.l.f5639f.e(str);
            this.f1271b = y.f1256g;
            this.f1272c = new ArrayList();
        }

        /* JADX WARN: Illegal instructions before constructor call */
        public /* synthetic */ a(String str, int i3, DefaultConstructorMarker defaultConstructorMarker) {
            if ((i3 & 1) != 0) {
                str = UUID.randomUUID().toString();
                D2.h.e(str, "UUID.randomUUID().toString()");
            }
            this(str);
        }
    }

    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public static final class c {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final a f1273c = new a(null);

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final t f1274a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final C f1275b;

        public static final class a {
            private a() {
            }

            public final c a(t tVar, C c4) {
                D2.h.f(c4, "body");
                DefaultConstructorMarker defaultConstructorMarker = null;
                if (!((tVar != null ? tVar.a("Content-Type") : null) == null)) {
                    throw new IllegalArgumentException("Unexpected header: Content-Type");
                }
                if ((tVar != null ? tVar.a("Content-Length") : null) == null) {
                    return new c(tVar, c4, defaultConstructorMarker);
                }
                throw new IllegalArgumentException("Unexpected header: Content-Length");
            }

            public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }

        private c(t tVar, C c4) {
            this.f1274a = tVar;
            this.f1275b = c4;
        }

        public final C a() {
            return this.f1275b;
        }

        public final t b() {
            return this.f1274a;
        }

        public /* synthetic */ c(t tVar, C c4, DefaultConstructorMarker defaultConstructorMarker) {
            this(tVar, c4);
        }
    }

    static {
        x.a aVar = x.f1251g;
        f1256g = aVar.b("multipart/mixed");
        f1257h = aVar.b("multipart/alternative");
        f1258i = aVar.b("multipart/digest");
        f1259j = aVar.b("multipart/parallel");
        f1260k = aVar.b("multipart/form-data");
        f1261l = new byte[]{(byte) 58, (byte) 32};
        f1262m = new byte[]{(byte) 13, (byte) 10};
        byte b4 = (byte) 45;
        f1263n = new byte[]{b4, b4};
    }

    public y(b3.l lVar, x xVar, List<c> list) {
        D2.h.f(lVar, "boundaryByteString");
        D2.h.f(xVar, "type");
        D2.h.f(list, "parts");
        this.f1267d = lVar;
        this.f1268e = xVar;
        this.f1269f = list;
        this.f1265b = x.f1251g.b(xVar + "; boundary=" + i());
        this.f1266c = -1L;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final long j(b3.j jVar, boolean z3) throws EOFException {
        b3.i iVar;
        if (z3) {
            jVar = new b3.i();
            iVar = jVar;
        } else {
            iVar = 0;
        }
        int size = this.f1269f.size();
        long j3 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            c cVar = (c) this.f1269f.get(i3);
            t tVarB = cVar.b();
            C cA = cVar.a();
            D2.h.c(jVar);
            jVar.R(f1263n);
            jVar.u(this.f1267d);
            jVar.R(f1262m);
            if (tVarB != null) {
                int size2 = tVarB.size();
                for (int i4 = 0; i4 < size2; i4++) {
                    jVar.h0(tVarB.b(i4)).R(f1261l).h0(tVarB.h(i4)).R(f1262m);
                }
            }
            x xVarB = cA.b();
            if (xVarB != null) {
                jVar.h0("Content-Type: ").h0(xVarB.toString()).R(f1262m);
            }
            long jA = cA.a();
            if (jA != -1) {
                jVar.h0("Content-Length: ").i0(jA).R(f1262m);
            } else if (z3) {
                D2.h.c(iVar);
                iVar.v();
                return -1L;
            }
            byte[] bArr = f1262m;
            jVar.R(bArr);
            if (z3) {
                j3 += jA;
            } else {
                cA.h(jVar);
            }
            jVar.R(bArr);
        }
        D2.h.c(jVar);
        byte[] bArr2 = f1263n;
        jVar.R(bArr2);
        jVar.u(this.f1267d);
        jVar.R(bArr2);
        jVar.R(f1262m);
        if (!z3) {
            return j3;
        }
        D2.h.c(iVar);
        long jF0 = j3 + iVar.F0();
        iVar.v();
        return jF0;
    }

    @Override // M2.C
    public long a() throws EOFException {
        long j3 = this.f1266c;
        if (j3 != -1) {
            return j3;
        }
        long j4 = j(null, true);
        this.f1266c = j4;
        return j4;
    }

    @Override // M2.C
    public x b() {
        return this.f1265b;
    }

    @Override // M2.C
    public void h(b3.j jVar) throws EOFException {
        D2.h.f(jVar, "sink");
        j(jVar, false);
    }

    public final String i() {
        return this.f1267d.z();
    }
}
