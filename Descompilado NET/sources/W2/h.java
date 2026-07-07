package W2;

import K2.o;
import java.util.List;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public class h extends j {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final boolean f2725d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f2726e = new a(0 == true ? 1 : 0);

    public static final class a {
        private a() {
        }

        public final h a() {
            if (b()) {
                return new h();
            }
            return null;
        }

        public final boolean b() {
            return h.f2725d;
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static {
        String property = System.getProperty("java.specification.version");
        Integer numJ = property != null ? o.j(property) : null;
        boolean z3 = true;
        if (numJ == null) {
            try {
                SSLSocket.class.getMethod("getApplicationProtocol", new Class[0]);
            } catch (NoSuchMethodException unused) {
                z3 = false;
            }
        } else if (numJ.intValue() < 9) {
            z3 = false;
        }
        f2725d = z3;
    }

    @Override // W2.j
    public void e(SSLSocket sSLSocket, String str, List list) {
        D2.h.f(sSLSocket, "sslSocket");
        D2.h.f(list, "protocols");
        SSLParameters sSLParameters = sSLSocket.getSSLParameters();
        List listB = j.f2732c.b(list);
        D2.h.e(sSLParameters, "sslParameters");
        Object[] array = listB.toArray(new String[0]);
        if (array == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        sSLParameters.setApplicationProtocols((String[]) array);
        sSLSocket.setSSLParameters(sSLParameters);
    }

    @Override // W2.j
    public String h(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        try {
            String applicationProtocol = sSLSocket.getApplicationProtocol();
            if (applicationProtocol == null) {
                return null;
            }
            if (applicationProtocol.hashCode() == 0) {
                if (applicationProtocol.equals("")) {
                    return null;
                }
            }
            return applicationProtocol;
        } catch (UnsupportedOperationException unused) {
            return null;
        }
    }
}
