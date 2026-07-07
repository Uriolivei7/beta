package M2;

import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import kotlin.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.AbstractC0681d;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class s {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f1216e = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Lazy f1217a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final G f1218b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0198i f1219c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final List f1220d;

    public static final class a {

        /* JADX INFO: renamed from: M2.s$a$a, reason: collision with other inner class name */
        static final class C0021a extends D2.i implements C2.a {

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            final /* synthetic */ List f1221c;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C0021a(List list) {
                super(0);
                this.f1221c = list;
            }

            @Override // C2.a
            /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
            public final List a() {
                return this.f1221c;
            }
        }

        static final class b extends D2.i implements C2.a {

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            final /* synthetic */ List f1222c;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            b(List list) {
                super(0);
                this.f1222c = list;
            }

            @Override // C2.a
            /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
            public final List a() {
                return this.f1222c;
            }
        }

        private a() {
        }

        private final List c(Certificate[] certificateArr) {
            return certificateArr != null ? N2.c.t((Certificate[]) Arrays.copyOf(certificateArr, certificateArr.length)) : AbstractC0717n.g();
        }

        public final s a(G g3, C0198i c0198i, List list, List list2) {
            D2.h.f(g3, "tlsVersion");
            D2.h.f(c0198i, "cipherSuite");
            D2.h.f(list, "peerCertificates");
            D2.h.f(list2, "localCertificates");
            return new s(g3, c0198i, N2.c.R(list2), new C0021a(N2.c.R(list)));
        }

        public final s b(SSLSession sSLSession) throws IOException {
            List listG;
            D2.h.f(sSLSession, "$this$handshake");
            String cipherSuite = sSLSession.getCipherSuite();
            if (cipherSuite == null) {
                throw new IllegalStateException("cipherSuite == null");
            }
            int iHashCode = cipherSuite.hashCode();
            if (iHashCode == 1019404634 ? cipherSuite.equals("TLS_NULL_WITH_NULL_NULL") : iHashCode == 1208658923 && cipherSuite.equals("SSL_NULL_WITH_NULL_NULL")) {
                throw new IOException("cipherSuite == " + cipherSuite);
            }
            C0198i c0198iB = C0198i.f1147s1.b(cipherSuite);
            String protocol = sSLSession.getProtocol();
            if (protocol == null) {
                throw new IllegalStateException("tlsVersion == null");
            }
            if (D2.h.b("NONE", protocol)) {
                throw new IOException("tlsVersion == NONE");
            }
            G gA = G.f955i.a(protocol);
            try {
                listG = c(sSLSession.getPeerCertificates());
            } catch (SSLPeerUnverifiedException unused) {
                listG = AbstractC0717n.g();
            }
            return new s(gA, c0198iB, c(sSLSession.getLocalCertificates()), new b(listG));
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static final class b extends D2.i implements C2.a {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ C2.a f1223c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(C2.a aVar) {
            super(0);
            this.f1223c = aVar;
        }

        @Override // C2.a
        /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
        public final List a() {
            try {
                return (List) this.f1223c.a();
            } catch (SSLPeerUnverifiedException unused) {
                return AbstractC0717n.g();
            }
        }
    }

    public s(G g3, C0198i c0198i, List<? extends Certificate> list, C2.a aVar) {
        D2.h.f(g3, "tlsVersion");
        D2.h.f(c0198i, "cipherSuite");
        D2.h.f(list, "localCertificates");
        D2.h.f(aVar, "peerCertificatesFn");
        this.f1218b = g3;
        this.f1219c = c0198i;
        this.f1220d = list;
        this.f1217a = AbstractC0681d.a(new b(aVar));
    }

    private final String b(Certificate certificate) {
        if (certificate instanceof X509Certificate) {
            return ((X509Certificate) certificate).getSubjectDN().toString();
        }
        String type = certificate.getType();
        D2.h.e(type, "type");
        return type;
    }

    public final C0198i a() {
        return this.f1219c;
    }

    public final List c() {
        return this.f1220d;
    }

    public final List d() {
        return (List) this.f1217a.getValue();
    }

    public final G e() {
        return this.f1218b;
    }

    public boolean equals(Object obj) {
        if (obj instanceof s) {
            s sVar = (s) obj;
            if (sVar.f1218b == this.f1218b && D2.h.b(sVar.f1219c, this.f1219c) && D2.h.b(sVar.d(), d()) && D2.h.b(sVar.f1220d, this.f1220d)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((((((527 + this.f1218b.hashCode()) * 31) + this.f1219c.hashCode()) * 31) + d().hashCode()) * 31) + this.f1220d.hashCode();
    }

    public String toString() {
        List listD = d();
        ArrayList arrayList = new ArrayList(AbstractC0717n.q(listD, 10));
        Iterator it = listD.iterator();
        while (it.hasNext()) {
            arrayList.add(b((Certificate) it.next()));
        }
        String string = arrayList.toString();
        StringBuilder sb = new StringBuilder();
        sb.append("Handshake{");
        sb.append("tlsVersion=");
        sb.append(this.f1218b);
        sb.append(' ');
        sb.append("cipherSuite=");
        sb.append(this.f1219c);
        sb.append(' ');
        sb.append("peerCertificates=");
        sb.append(string);
        sb.append(' ');
        sb.append("localCertificates=");
        List list = this.f1220d;
        ArrayList arrayList2 = new ArrayList(AbstractC0717n.q(list, 10));
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            arrayList2.add(b((Certificate) it2.next()));
        }
        sb.append(arrayList2);
        sb.append('}');
        return sb.toString();
    }
}
