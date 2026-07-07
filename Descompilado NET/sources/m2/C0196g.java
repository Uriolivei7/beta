package M2;

import b3.l;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLPeerUnverifiedException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: renamed from: M2.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0196g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Set f1029a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Z2.c f1030b;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final b f1028d = new b(null);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final C0196g f1027c = new a().a();

    /* JADX INFO: renamed from: M2.g$a */
    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final List f1031a = new ArrayList();

        /* JADX WARN: Multi-variable type inference failed */
        public final C0196g a() {
            return new C0196g(AbstractC0717n.h0(this.f1031a), null, 2, 0 == true ? 1 : 0);
        }
    }

    /* JADX INFO: renamed from: M2.g$b */
    public static final class b {
        private b() {
        }

        public final String a(Certificate certificate) {
            D2.h.f(certificate, "certificate");
            if (!(certificate instanceof X509Certificate)) {
                throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
            }
            return "sha256/" + c((X509Certificate) certificate).a();
        }

        public final b3.l b(X509Certificate x509Certificate) {
            D2.h.f(x509Certificate, "$this$sha1Hash");
            l.a aVar = b3.l.f5639f;
            PublicKey publicKey = x509Certificate.getPublicKey();
            D2.h.e(publicKey, "publicKey");
            byte[] encoded = publicKey.getEncoded();
            D2.h.e(encoded, "publicKey.encoded");
            return l.a.h(aVar, encoded, 0, 0, 3, null).t();
        }

        public final b3.l c(X509Certificate x509Certificate) {
            D2.h.f(x509Certificate, "$this$sha256Hash");
            l.a aVar = b3.l.f5639f;
            PublicKey publicKey = x509Certificate.getPublicKey();
            D2.h.e(publicKey, "publicKey");
            byte[] encoded = publicKey.getEncoded();
            D2.h.e(encoded, "publicKey.encoded");
            return l.a.h(aVar, encoded, 0, 0, 3, null).u();
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX INFO: renamed from: M2.g$c */
    public static final class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final String f1032a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final String f1033b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final b3.l f1034c;

        public c(String str, String str2) {
            D2.h.f(str, "pattern");
            D2.h.f(str2, "pin");
            if (!((K2.o.z(str, "*.", false, 2, null) && K2.o.O(str, "*", 1, false, 4, null) == -1) || (K2.o.z(str, "**.", false, 2, null) && K2.o.O(str, "*", 2, false, 4, null) == -1) || K2.o.O(str, "*", 0, false, 6, null) == -1)) {
                throw new IllegalArgumentException(("Unexpected pattern: " + str).toString());
            }
            String strE = N2.a.e(str);
            if (strE == null) {
                throw new IllegalArgumentException("Invalid pattern: " + str);
            }
            this.f1032a = strE;
            if (K2.o.z(str2, "sha1/", false, 2, null)) {
                this.f1033b = "sha1";
                l.a aVar = b3.l.f5639f;
                String strSubstring = str2.substring(5);
                D2.h.e(strSubstring, "(this as java.lang.String).substring(startIndex)");
                b3.l lVarB = aVar.b(strSubstring);
                if (lVarB != null) {
                    this.f1034c = lVarB;
                    return;
                }
                throw new IllegalArgumentException("Invalid pin hash: " + str2);
            }
            if (!K2.o.z(str2, "sha256/", false, 2, null)) {
                throw new IllegalArgumentException("pins must start with 'sha256/' or 'sha1/': " + str2);
            }
            this.f1033b = "sha256";
            l.a aVar2 = b3.l.f5639f;
            String strSubstring2 = str2.substring(7);
            D2.h.e(strSubstring2, "(this as java.lang.String).substring(startIndex)");
            b3.l lVarB2 = aVar2.b(strSubstring2);
            if (lVarB2 != null) {
                this.f1034c = lVarB2;
                return;
            }
            throw new IllegalArgumentException("Invalid pin hash: " + str2);
        }

        public final b3.l a() {
            return this.f1034c;
        }

        public final String b() {
            return this.f1033b;
        }

        public final boolean c(String str) {
            D2.h.f(str, "hostname");
            if (K2.o.z(this.f1032a, "**.", false, 2, null)) {
                int length = this.f1032a.length() - 3;
                int length2 = str.length() - length;
                if (!K2.o.q(str, str.length() - length, this.f1032a, 3, length, false, 16, null)) {
                    return false;
                }
                if (length2 != 0 && str.charAt(length2 - 1) != '.') {
                    return false;
                }
            } else {
                if (!K2.o.z(this.f1032a, "*.", false, 2, null)) {
                    return D2.h.b(str, this.f1032a);
                }
                int length3 = this.f1032a.length() - 1;
                int length4 = str.length() - length3;
                if (!K2.o.q(str, str.length() - length3, this.f1032a, 1, length3, false, 16, null) || K2.o.T(str, '.', length4 - 1, false, 4, null) != -1) {
                    return false;
                }
            }
            return true;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof c)) {
                return false;
            }
            c cVar = (c) obj;
            return D2.h.b(this.f1032a, cVar.f1032a) && D2.h.b(this.f1033b, cVar.f1033b) && D2.h.b(this.f1034c, cVar.f1034c);
        }

        public int hashCode() {
            return (((this.f1032a.hashCode() * 31) + this.f1033b.hashCode()) * 31) + this.f1034c.hashCode();
        }

        public String toString() {
            return this.f1033b + '/' + this.f1034c.a();
        }
    }

    /* JADX INFO: renamed from: M2.g$d */
    static final class d extends D2.i implements C2.a {

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ List f1036d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f1037e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(List list, String str) {
            super(0);
            this.f1036d = list;
            this.f1037e = str;
        }

        @Override // C2.a
        /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
        public final List a() {
            List<Certificate> listA;
            Z2.c cVarD = C0196g.this.d();
            if (cVarD == null || (listA = cVarD.a(this.f1036d, this.f1037e)) == null) {
                listA = this.f1036d;
            }
            ArrayList arrayList = new ArrayList(AbstractC0717n.q(listA, 10));
            for (Certificate certificate : listA) {
                if (certificate == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.security.cert.X509Certificate");
                }
                arrayList.add((X509Certificate) certificate);
            }
            return arrayList;
        }
    }

    public C0196g(Set<c> set, Z2.c cVar) {
        D2.h.f(set, "pins");
        this.f1029a = set;
        this.f1030b = cVar;
    }

    public final void a(String str, List list) {
        D2.h.f(str, "hostname");
        D2.h.f(list, "peerCertificates");
        b(str, new d(list, str));
    }

    public final void b(String str, C2.a aVar) throws SSLPeerUnverifiedException {
        D2.h.f(str, "hostname");
        D2.h.f(aVar, "cleanedPeerCertificatesFn");
        List<c> listC = c(str);
        if (listC.isEmpty()) {
            return;
        }
        List<X509Certificate> list = (List) aVar.a();
        for (X509Certificate x509Certificate : list) {
            b3.l lVarC = null;
            b3.l lVarB = null;
            for (c cVar : listC) {
                String strB = cVar.b();
                int iHashCode = strB.hashCode();
                if (iHashCode != -903629273) {
                    if (iHashCode != 3528965 || !strB.equals("sha1")) {
                        throw new AssertionError("unsupported hashAlgorithm: " + cVar.b());
                    }
                    if (lVarB == null) {
                        lVarB = f1028d.b(x509Certificate);
                    }
                    if (D2.h.b(cVar.a(), lVarB)) {
                        return;
                    }
                } else {
                    if (!strB.equals("sha256")) {
                        throw new AssertionError("unsupported hashAlgorithm: " + cVar.b());
                    }
                    if (lVarC == null) {
                        lVarC = f1028d.c(x509Certificate);
                    }
                    if (D2.h.b(cVar.a(), lVarC)) {
                        return;
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Certificate pinning failure!");
        sb.append("\n  Peer certificate chain:");
        for (X509Certificate x509Certificate2 : list) {
            sb.append("\n    ");
            sb.append(f1028d.a(x509Certificate2));
            sb.append(": ");
            Principal subjectDN = x509Certificate2.getSubjectDN();
            D2.h.e(subjectDN, "element.subjectDN");
            sb.append(subjectDN.getName());
        }
        sb.append("\n  Pinned certificates for ");
        sb.append(str);
        sb.append(":");
        for (c cVar2 : listC) {
            sb.append("\n    ");
            sb.append(cVar2);
        }
        String string = sb.toString();
        D2.h.e(string, "StringBuilder().apply(builderAction).toString()");
        throw new SSLPeerUnverifiedException(string);
    }

    public final List c(String str) {
        D2.h.f(str, "hostname");
        Set set = this.f1029a;
        List listG = AbstractC0717n.g();
        for (Object obj : set) {
            if (((c) obj).c(str)) {
                if (listG.isEmpty()) {
                    listG = new ArrayList();
                }
                D2.v.b(listG).add(obj);
            }
        }
        return listG;
    }

    public final Z2.c d() {
        return this.f1030b;
    }

    public final C0196g e(Z2.c cVar) {
        D2.h.f(cVar, "certificateChainCleaner");
        return D2.h.b(this.f1030b, cVar) ? this : new C0196g(this.f1029a, cVar);
    }

    public boolean equals(Object obj) {
        if (obj instanceof C0196g) {
            C0196g c0196g = (C0196g) obj;
            if (D2.h.b(c0196g.f1029a, this.f1029a) && D2.h.b(c0196g.f1030b, this.f1030b)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int iHashCode = (1517 + this.f1029a.hashCode()) * 41;
        Z2.c cVar = this.f1030b;
        return iHashCode + (cVar != null ? cVar.hashCode() : 0);
    }

    public /* synthetic */ C0196g(Set set, Z2.c cVar, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(set, (i3 & 2) != 0 ? null : cVar);
    }
}
