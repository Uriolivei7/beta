package W2;

import X2.k;
import X2.l;
import X2.m;
import android.os.Build;
import android.security.NetworkSecurityPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509TrustManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class a extends j {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final boolean f2700e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final C0042a f2701f = new C0042a(null);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final List f2702d;

    /* JADX INFO: renamed from: W2.a$a, reason: collision with other inner class name */
    public static final class C0042a {
        private C0042a() {
        }

        public final j a() {
            if (b()) {
                return new a();
            }
            return null;
        }

        public final boolean b() {
            return a.f2700e;
        }

        public /* synthetic */ C0042a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        f2700e = j.f2732c.h() && Build.VERSION.SDK_INT >= 29;
    }

    public a() {
        List listK = AbstractC0717n.k(X2.c.f2759a.a(), new l(X2.h.f2768g.d()), new l(k.f2782b.a()), new l(X2.i.f2776b.a()));
        ArrayList arrayList = new ArrayList();
        for (Object obj : listK) {
            if (((m) obj).b()) {
                arrayList.add(obj);
            }
        }
        this.f2702d = arrayList;
    }

    @Override // W2.j
    public Z2.c c(X509TrustManager x509TrustManager) {
        D2.h.f(x509TrustManager, "trustManager");
        X2.d dVarA = X2.d.f2760d.a(x509TrustManager);
        return dVarA != null ? dVarA : super.c(x509TrustManager);
    }

    @Override // W2.j
    public void e(SSLSocket sSLSocket, String str, List list) {
        Object next;
        D2.h.f(sSLSocket, "sslSocket");
        D2.h.f(list, "protocols");
        Iterator it = this.f2702d.iterator();
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
    public String h(SSLSocket sSLSocket) {
        Object next;
        D2.h.f(sSLSocket, "sslSocket");
        Iterator it = this.f2702d.iterator();
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
    public boolean j(String str) {
        D2.h.f(str, "hostname");
        return NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted(str);
    }
}
