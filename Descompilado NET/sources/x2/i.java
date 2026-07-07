package X2;

import X2.l;
import java.util.List;
import javax.net.ssl.SSLSocket;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.bouncycastle.jsse.BCSSLParameters;
import org.bouncycastle.jsse.BCSSLSocket;

/* JADX INFO: loaded from: classes.dex */
public final class i implements m {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final b f2776b = new b(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final l.a f2775a = new a();

    public static final class a implements l.a {
        a() {
        }

        @Override // X2.l.a
        public boolean a(SSLSocket sSLSocket) {
            D2.h.f(sSLSocket, "sslSocket");
            W2.c.f2710f.b();
            return false;
        }

        @Override // X2.l.a
        public m b(SSLSocket sSLSocket) {
            D2.h.f(sSLSocket, "sslSocket");
            return new i();
        }
    }

    public static final class b {
        private b() {
        }

        public final l.a a() {
            return i.f2775a;
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Override // X2.m
    public boolean a(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        return false;
    }

    @Override // X2.m
    public boolean b() {
        return W2.c.f2710f.b();
    }

    @Override // X2.m
    public String c(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        String applicationProtocol = ((BCSSLSocket) sSLSocket).getApplicationProtocol();
        if (applicationProtocol == null || (applicationProtocol.hashCode() == 0 && applicationProtocol.equals(""))) {
            return null;
        }
        return applicationProtocol;
    }

    @Override // X2.m
    public void d(SSLSocket sSLSocket, String str, List list) {
        D2.h.f(sSLSocket, "sslSocket");
        D2.h.f(list, "protocols");
        if (a(sSLSocket)) {
            BCSSLSocket bCSSLSocket = (BCSSLSocket) sSLSocket;
            BCSSLParameters parameters = bCSSLSocket.getParameters();
            D2.h.e(parameters, "sslParameters");
            Object[] array = W2.j.f2732c.b(list).toArray(new String[0]);
            if (array == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            parameters.setApplicationProtocols((String[]) array);
            bCSSLSocket.setParameters(parameters);
        }
    }
}
