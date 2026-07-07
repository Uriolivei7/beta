package W2;

import X2.k;
import X2.l;
import X2.m;
import X2.n;
import android.os.Build;
import android.security.NetworkSecurityPolicy;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509TrustManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class b extends j {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final boolean f2703f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final a f2704g = new a(null);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final List f2705d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final X2.j f2706e;

    public static final class a {
        private a() {
        }

        public final j a() {
            if (b()) {
                return new b();
            }
            return null;
        }

        public final boolean b() {
            return b.f2703f;
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX INFO: renamed from: W2.b$b, reason: collision with other inner class name */
    public static final class C0043b implements Z2.e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final X509TrustManager f2707a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Method f2708b;

        public C0043b(X509TrustManager x509TrustManager, Method method) {
            D2.h.f(x509TrustManager, "trustManager");
            D2.h.f(method, "findByIssuerAndSignatureMethod");
            this.f2707a = x509TrustManager;
            this.f2708b = method;
        }

        @Override // Z2.e
        public X509Certificate a(X509Certificate x509Certificate) {
            D2.h.f(x509Certificate, "cert");
            try {
                Object objInvoke = this.f2708b.invoke(this.f2707a, x509Certificate);
                if (objInvoke != null) {
                    return ((TrustAnchor) objInvoke).getTrustedCert();
                }
                throw new NullPointerException("null cannot be cast to non-null type java.security.cert.TrustAnchor");
            } catch (IllegalAccessException e4) {
                throw new AssertionError("unable to get issues and signature", e4);
            } catch (InvocationTargetException unused) {
                return null;
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof C0043b)) {
                return false;
            }
            C0043b c0043b = (C0043b) obj;
            return D2.h.b(this.f2707a, c0043b.f2707a) && D2.h.b(this.f2708b, c0043b.f2708b);
        }

        public int hashCode() {
            X509TrustManager x509TrustManager = this.f2707a;
            int iHashCode = (x509TrustManager != null ? x509TrustManager.hashCode() : 0) * 31;
            Method method = this.f2708b;
            return iHashCode + (method != null ? method.hashCode() : 0);
        }

        public String toString() {
            return "CustomTrustRootIndex(trustManager=" + this.f2707a + ", findByIssuerAndSignatureMethod=" + this.f2708b + ")";
        }
    }

    static {
        boolean z3 = false;
        if (j.f2732c.h() && Build.VERSION.SDK_INT < 30) {
            z3 = true;
        }
        f2703f = z3;
    }

    public b() {
        List listK = AbstractC0717n.k(n.a.b(n.f2785j, null, 1, null), new l(X2.h.f2768g.d()), new l(k.f2782b.a()), new l(X2.i.f2776b.a()));
        ArrayList arrayList = new ArrayList();
        for (Object obj : listK) {
            if (((m) obj).b()) {
                arrayList.add(obj);
            }
        }
        this.f2705d = arrayList;
        this.f2706e = X2.j.f2777d.a();
    }

    @Override // W2.j
    public Z2.c c(X509TrustManager x509TrustManager) {
        D2.h.f(x509TrustManager, "trustManager");
        X2.d dVarA = X2.d.f2760d.a(x509TrustManager);
        return dVarA != null ? dVarA : super.c(x509TrustManager);
    }

    @Override // W2.j
    public Z2.e d(X509TrustManager x509TrustManager) {
        D2.h.f(x509TrustManager, "trustManager");
        try {
            Method declaredMethod = x509TrustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", X509Certificate.class);
            D2.h.e(declaredMethod, "method");
            declaredMethod.setAccessible(true);
            return new C0043b(x509TrustManager, declaredMethod);
        } catch (NoSuchMethodException unused) {
            return super.d(x509TrustManager);
        }
    }

    @Override // W2.j
    public void e(SSLSocket sSLSocket, String str, List list) {
        Object next;
        D2.h.f(sSLSocket, "sslSocket");
        D2.h.f(list, "protocols");
        Iterator it = this.f2705d.iterator();
        while (true) {
            if (!it.hasNext()) {
                next = null;
                break;
            } else {
                next = it.next();
                if (((m) next).a(sSLSocket)) {
                    break;
                }
            }
        }
        m mVar = (m) next;
        if (mVar != null) {
            mVar.d(sSLSocket, str, list);
        }
    }

    @Override // W2.j
    public void f(Socket socket, InetSocketAddress inetSocketAddress, int i3) throws IOException {
        D2.h.f(socket, "socket");
        D2.h.f(inetSocketAddress, "address");
        try {
            socket.connect(inetSocketAddress, i3);
        } catch (ClassCastException e4) {
            if (Build.VERSION.SDK_INT != 26) {
                throw e4;
            }
            throw new IOException("Exception in connect", e4);
        }
    }

    @Override // W2.j
    public String h(SSLSocket sSLSocket) {
        Object next;
        D2.h.f(sSLSocket, "sslSocket");
        Iterator it = this.f2705d.iterator();
        while (true) {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
            if (((m) next).a(sSLSocket)) {
                break;
            }
        }
        m mVar = (m) next;
        if (mVar != null) {
            return mVar.c(sSLSocket);
        }
        return null;
    }

    @Override // W2.j
    public Object i(String str) {
        D2.h.f(str, "closer");
        return this.f2706e.a(str);
    }

    @Override // W2.j
    public boolean j(String str) {
        D2.h.f(str, "hostname");
        return NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted(str);
    }

    @Override // W2.j
    public void m(String str, Object obj) {
        D2.h.f(str, "message");
        if (this.f2706e.b(obj)) {
            return;
        }
        j.l(this, str, 5, null, 4, null);
    }
}
