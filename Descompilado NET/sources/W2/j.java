package W2;

import M2.A;
import M2.z;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public class j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static volatile j f2730a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Logger f2731b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f2732c;

    public static final class a {
        private a() {
        }

        private final j d() {
            X2.e.f2765c.b();
            j jVarA = W2.a.f2701f.a();
            if (jVarA != null) {
                return jVarA;
            }
            j jVarA2 = b.f2704g.a();
            D2.h.c(jVarA2);
            return jVarA2;
        }

        private final j e() {
            i iVarA;
            c cVarA;
            d dVarB;
            if (j() && (dVarB = d.f2713f.b()) != null) {
                return dVarB;
            }
            if (i() && (cVarA = c.f2710f.a()) != null) {
                return cVarA;
            }
            if (k() && (iVarA = i.f2728f.a()) != null) {
                return iVarA;
            }
            h hVarA = h.f2726e.a();
            if (hVarA != null) {
                return hVarA;
            }
            j jVarA = e.f2716i.a();
            return jVarA != null ? jVarA : new j();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final j f() {
            return h() ? d() : e();
        }

        private final boolean i() {
            Provider provider = Security.getProviders()[0];
            D2.h.e(provider, "Security.getProviders()[0]");
            return D2.h.b("BC", provider.getName());
        }

        private final boolean j() {
            Provider provider = Security.getProviders()[0];
            D2.h.e(provider, "Security.getProviders()[0]");
            return D2.h.b("Conscrypt", provider.getName());
        }

        private final boolean k() {
            Provider provider = Security.getProviders()[0];
            D2.h.e(provider, "Security.getProviders()[0]");
            return D2.h.b("OpenJSSE", provider.getName());
        }

        public final List b(List list) {
            D2.h.f(list, "protocols");
            ArrayList arrayList = new ArrayList();
            for (Object obj : list) {
                if (((A) obj) != A.HTTP_1_0) {
                    arrayList.add(obj);
                }
            }
            ArrayList arrayList2 = new ArrayList(AbstractC0717n.q(arrayList, 10));
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                arrayList2.add(((A) it.next()).toString());
            }
            return arrayList2;
        }

        public final byte[] c(List list) {
            D2.h.f(list, "protocols");
            b3.i iVar = new b3.i();
            for (String str : b(list)) {
                iVar.L(str.length());
                iVar.h0(str);
            }
            return iVar.H();
        }

        public final j g() {
            return j.f2730a;
        }

        public final boolean h() {
            return D2.h.b("Dalvik", System.getProperty("java.vm.name"));
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        a aVar = new a(null);
        f2732c = aVar;
        f2730a = aVar.f();
        f2731b = Logger.getLogger(z.class.getName());
    }

    public static /* synthetic */ void l(j jVar, String str, int i3, Throwable th, int i4, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: log");
        }
        if ((i4 & 2) != 0) {
            i3 = 4;
        }
        if ((i4 & 4) != 0) {
            th = null;
        }
        jVar.k(str, i3, th);
    }

    public void b(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
    }

    public Z2.c c(X509TrustManager x509TrustManager) {
        D2.h.f(x509TrustManager, "trustManager");
        return new Z2.a(d(x509TrustManager));
    }

    public Z2.e d(X509TrustManager x509TrustManager) {
        D2.h.f(x509TrustManager, "trustManager");
        X509Certificate[] acceptedIssuers = x509TrustManager.getAcceptedIssuers();
        D2.h.e(acceptedIssuers, "trustManager.acceptedIssuers");
        return new Z2.b((X509Certificate[]) Arrays.copyOf(acceptedIssuers, acceptedIssuers.length));
    }

    public void e(SSLSocket sSLSocket, String str, List list) {
        D2.h.f(sSLSocket, "sslSocket");
        D2.h.f(list, "protocols");
    }

    public void f(Socket socket, InetSocketAddress inetSocketAddress, int i3) throws IOException {
        D2.h.f(socket, "socket");
        D2.h.f(inetSocketAddress, "address");
        socket.connect(inetSocketAddress, i3);
    }

    public final String g() {
        return "OkHttp";
    }

    public String h(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        return null;
    }

    public Object i(String str) {
        D2.h.f(str, "closer");
        if (f2731b.isLoggable(Level.FINE)) {
            return new Throwable(str);
        }
        return null;
    }

    public boolean j(String str) {
        D2.h.f(str, "hostname");
        return true;
    }

    public void k(String str, int i3, Throwable th) {
        D2.h.f(str, "message");
        f2731b.log(i3 == 5 ? Level.WARNING : Level.INFO, str, th);
    }

    public void m(String str, Object obj) {
        D2.h.f(str, "message");
        if (obj == null) {
            str = str + " To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);";
        }
        k(str, 5, (Throwable) obj);
    }

    public SSLContext n() throws NoSuchAlgorithmException {
        SSLContext sSLContext = SSLContext.getInstance("TLS");
        D2.h.e(sSLContext, "SSLContext.getInstance(\"TLS\")");
        return sSLContext;
    }

    public SSLSocketFactory o(X509TrustManager x509TrustManager) {
        D2.h.f(x509TrustManager, "trustManager");
        try {
            SSLContext sSLContextN = n();
            sSLContextN.init(null, new TrustManager[]{x509TrustManager}, null);
            SSLSocketFactory socketFactory = sSLContextN.getSocketFactory();
            D2.h.e(socketFactory, "newSSLContext().apply {\n…ll)\n      }.socketFactory");
            return socketFactory;
        } catch (GeneralSecurityException e4) {
            throw new AssertionError("No System TLS: " + e4, e4);
        }
    }

    public X509TrustManager p() throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        D2.h.e(trustManagerFactory, "factory");
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        D2.h.c(trustManagers);
        if (trustManagers.length == 1 && (trustManagers[0] instanceof X509TrustManager)) {
            TrustManager trustManager = trustManagers[0];
            if (trustManager != null) {
                return (X509TrustManager) trustManager;
            }
            throw new NullPointerException("null cannot be cast to non-null type javax.net.ssl.X509TrustManager");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected default trust managers: ");
        String string = Arrays.toString(trustManagers);
        D2.h.e(string, "java.util.Arrays.toString(this)");
        sb.append(string);
        throw new IllegalStateException(sb.toString().toString());
    }

    public String toString() {
        String simpleName = getClass().getSimpleName();
        D2.h.e(simpleName, "javaClass.simpleName");
        return simpleName;
    }
}
