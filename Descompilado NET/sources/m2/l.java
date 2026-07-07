package M2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.net.ssl.SSLSocket;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;
import u2.AbstractC0746a;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final C0198i[] f1164e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final C0198i[] f1165f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final l f1166g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final l f1167h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final l f1168i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final l f1169j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static final b f1170k = new b(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final boolean f1171a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final boolean f1172b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String[] f1173c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String[] f1174d;

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean f1175a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String[] f1176b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private String[] f1177c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f1178d;

        public a(boolean z3) {
            this.f1175a = z3;
        }

        public final l a() {
            return new l(this.f1175a, this.f1178d, this.f1176b, this.f1177c);
        }

        public final a b(C0198i... c0198iArr) {
            D2.h.f(c0198iArr, "cipherSuites");
            if (!this.f1175a) {
                throw new IllegalArgumentException("no cipher suites for cleartext connections");
            }
            ArrayList arrayList = new ArrayList(c0198iArr.length);
            for (C0198i c0198i : c0198iArr) {
                arrayList.add(c0198i.c());
            }
            Object[] array = arrayList.toArray(new String[0]);
            if (array == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            String[] strArr = (String[]) array;
            return c((String[]) Arrays.copyOf(strArr, strArr.length));
        }

        public final a c(String... strArr) throws CloneNotSupportedException {
            D2.h.f(strArr, "cipherSuites");
            if (!this.f1175a) {
                throw new IllegalArgumentException("no cipher suites for cleartext connections");
            }
            if (strArr.length == 0) {
                throw new IllegalArgumentException("At least one cipher suite is required");
            }
            Object objClone = strArr.clone();
            if (objClone == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
            }
            this.f1176b = (String[]) objClone;
            return this;
        }

        public final a d(boolean z3) {
            if (!this.f1175a) {
                throw new IllegalArgumentException("no TLS extensions for cleartext connections");
            }
            this.f1178d = z3;
            return this;
        }

        public final a e(G... gArr) {
            D2.h.f(gArr, "tlsVersions");
            if (!this.f1175a) {
                throw new IllegalArgumentException("no TLS versions for cleartext connections");
            }
            ArrayList arrayList = new ArrayList(gArr.length);
            for (G g3 : gArr) {
                arrayList.add(g3.a());
            }
            Object[] array = arrayList.toArray(new String[0]);
            if (array == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            String[] strArr = (String[]) array;
            return f((String[]) Arrays.copyOf(strArr, strArr.length));
        }

        public final a f(String... strArr) throws CloneNotSupportedException {
            D2.h.f(strArr, "tlsVersions");
            if (!this.f1175a) {
                throw new IllegalArgumentException("no TLS versions for cleartext connections");
            }
            if (strArr.length == 0) {
                throw new IllegalArgumentException("At least one TLS version is required");
            }
            Object objClone = strArr.clone();
            if (objClone == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
            }
            this.f1177c = (String[]) objClone;
            return this;
        }

        public a(l lVar) {
            D2.h.f(lVar, "connectionSpec");
            this.f1175a = lVar.f();
            this.f1176b = lVar.f1173c;
            this.f1177c = lVar.f1174d;
            this.f1178d = lVar.h();
        }
    }

    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        C0198i c0198i = C0198i.f1132n1;
        C0198i c0198i2 = C0198i.f1135o1;
        C0198i c0198i3 = C0198i.f1138p1;
        C0198i c0198i4 = C0198i.f1091Z0;
        C0198i c0198i5 = C0198i.f1102d1;
        C0198i c0198i6 = C0198i.f1093a1;
        C0198i c0198i7 = C0198i.f1105e1;
        C0198i c0198i8 = C0198i.f1123k1;
        C0198i c0198i9 = C0198i.f1120j1;
        C0198i[] c0198iArr = {c0198i, c0198i2, c0198i3, c0198i4, c0198i5, c0198i6, c0198i7, c0198i8, c0198i9};
        f1164e = c0198iArr;
        C0198i[] c0198iArr2 = {c0198i, c0198i2, c0198i3, c0198i4, c0198i5, c0198i6, c0198i7, c0198i8, c0198i9, C0198i.f1061K0, C0198i.f1063L0, C0198i.f1116i0, C0198i.f1119j0, C0198i.f1052G, C0198i.f1060K, C0198i.f1121k};
        f1165f = c0198iArr2;
        a aVarB = new a(true).b((C0198i[]) Arrays.copyOf(c0198iArr, c0198iArr.length));
        G g3 = G.TLS_1_3;
        G g4 = G.TLS_1_2;
        f1166g = aVarB.e(g3, g4).d(true).a();
        f1167h = new a(true).b((C0198i[]) Arrays.copyOf(c0198iArr2, c0198iArr2.length)).e(g3, g4).d(true).a();
        f1168i = new a(true).b((C0198i[]) Arrays.copyOf(c0198iArr2, c0198iArr2.length)).e(g3, g4, G.TLS_1_1, G.TLS_1_0).d(true).a();
        f1169j = new a(false).a();
    }

    public l(boolean z3, boolean z4, String[] strArr, String[] strArr2) {
        this.f1171a = z3;
        this.f1172b = z4;
        this.f1173c = strArr;
        this.f1174d = strArr2;
    }

    private final l g(SSLSocket sSLSocket, boolean z3) throws CloneNotSupportedException {
        String[] enabledCipherSuites;
        String[] enabledProtocols;
        if (this.f1173c != null) {
            String[] enabledCipherSuites2 = sSLSocket.getEnabledCipherSuites();
            D2.h.e(enabledCipherSuites2, "sslSocket.enabledCipherSuites");
            enabledCipherSuites = N2.c.B(enabledCipherSuites2, this.f1173c, C0198i.f1147s1.c());
        } else {
            enabledCipherSuites = sSLSocket.getEnabledCipherSuites();
        }
        if (this.f1174d != null) {
            String[] enabledProtocols2 = sSLSocket.getEnabledProtocols();
            D2.h.e(enabledProtocols2, "sslSocket.enabledProtocols");
            enabledProtocols = N2.c.B(enabledProtocols2, this.f1174d, AbstractC0746a.b());
        } else {
            enabledProtocols = sSLSocket.getEnabledProtocols();
        }
        String[] supportedCipherSuites = sSLSocket.getSupportedCipherSuites();
        D2.h.e(supportedCipherSuites, "supportedCipherSuites");
        int iU = N2.c.u(supportedCipherSuites, "TLS_FALLBACK_SCSV", C0198i.f1147s1.c());
        if (z3 && iU != -1) {
            D2.h.e(enabledCipherSuites, "cipherSuitesIntersection");
            String str = supportedCipherSuites[iU];
            D2.h.e(str, "supportedCipherSuites[indexOfFallbackScsv]");
            enabledCipherSuites = N2.c.l(enabledCipherSuites, str);
        }
        a aVar = new a(this);
        D2.h.e(enabledCipherSuites, "cipherSuitesIntersection");
        a aVarC = aVar.c((String[]) Arrays.copyOf(enabledCipherSuites, enabledCipherSuites.length));
        D2.h.e(enabledProtocols, "tlsVersionsIntersection");
        return aVarC.f((String[]) Arrays.copyOf(enabledProtocols, enabledProtocols.length)).a();
    }

    public final void c(SSLSocket sSLSocket, boolean z3) throws CloneNotSupportedException {
        D2.h.f(sSLSocket, "sslSocket");
        l lVarG = g(sSLSocket, z3);
        if (lVarG.i() != null) {
            sSLSocket.setEnabledProtocols(lVarG.f1174d);
        }
        if (lVarG.d() != null) {
            sSLSocket.setEnabledCipherSuites(lVarG.f1173c);
        }
    }

    public final List d() {
        String[] strArr = this.f1173c;
        if (strArr == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            arrayList.add(C0198i.f1147s1.b(str));
        }
        return AbstractC0717n.e0(arrayList);
    }

    public final boolean e(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "socket");
        if (!this.f1171a) {
            return false;
        }
        String[] strArr = this.f1174d;
        if (strArr != null && !N2.c.r(strArr, sSLSocket.getEnabledProtocols(), AbstractC0746a.b())) {
            return false;
        }
        String[] strArr2 = this.f1173c;
        return strArr2 == null || N2.c.r(strArr2, sSLSocket.getEnabledCipherSuites(), C0198i.f1147s1.c());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof l)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        boolean z3 = this.f1171a;
        l lVar = (l) obj;
        if (z3 != lVar.f1171a) {
            return false;
        }
        return !z3 || (Arrays.equals(this.f1173c, lVar.f1173c) && Arrays.equals(this.f1174d, lVar.f1174d) && this.f1172b == lVar.f1172b);
    }

    public final boolean f() {
        return this.f1171a;
    }

    public final boolean h() {
        return this.f1172b;
    }

    public int hashCode() {
        if (!this.f1171a) {
            return 17;
        }
        String[] strArr = this.f1173c;
        int iHashCode = (527 + (strArr != null ? Arrays.hashCode(strArr) : 0)) * 31;
        String[] strArr2 = this.f1174d;
        return ((iHashCode + (strArr2 != null ? Arrays.hashCode(strArr2) : 0)) * 31) + (!this.f1172b ? 1 : 0);
    }

    public final List i() {
        String[] strArr = this.f1174d;
        if (strArr == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            arrayList.add(G.f955i.a(str));
        }
        return AbstractC0717n.e0(arrayList);
    }

    public String toString() {
        if (!this.f1171a) {
            return "ConnectionSpec()";
        }
        return "ConnectionSpec(cipherSuites=" + Objects.toString(d(), "[all enabled]") + ", tlsVersions=" + Objects.toString(i(), "[all enabled]") + ", supportsTlsExtensions=" + this.f1172b + ')';
    }
}
