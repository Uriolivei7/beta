package X2;

import K2.o;
import X2.l;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.net.ssl.SSLSocket;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public class h implements m {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final l.a f2767f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final a f2768g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Method f2769a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Method f2770b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Method f2771c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Method f2772d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Class f2773e;

    public static final class a {

        /* JADX INFO: renamed from: X2.h$a$a, reason: collision with other inner class name */
        public static final class C0046a implements l.a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ String f2774a;

            C0046a(String str) {
                this.f2774a = str;
            }

            @Override // X2.l.a
            public boolean a(SSLSocket sSLSocket) {
                D2.h.f(sSLSocket, "sslSocket");
                String name = sSLSocket.getClass().getName();
                D2.h.e(name, "sslSocket.javaClass.name");
                return o.z(name, this.f2774a + '.', false, 2, null);
            }

            @Override // X2.l.a
            public m b(SSLSocket sSLSocket) {
                D2.h.f(sSLSocket, "sslSocket");
                return h.f2768g.b(sSLSocket.getClass());
            }
        }

        private a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final h b(Class cls) {
            Class superclass = cls;
            while (superclass != null && !D2.h.b(superclass.getSimpleName(), "OpenSSLSocketImpl")) {
                superclass = superclass.getSuperclass();
                if (superclass == null) {
                    throw new AssertionError("No OpenSSLSocketImpl superclass of socket of type " + cls);
                }
            }
            D2.h.c(superclass);
            return new h(superclass);
        }

        public final l.a c(String str) {
            D2.h.f(str, "packageName");
            return new C0046a(str);
        }

        public final l.a d() {
            return h.f2767f;
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        a aVar = new a(null);
        f2768g = aVar;
        f2767f = aVar.c("com.google.android.gms.org.conscrypt");
    }

    public h(Class<? super SSLSocket> cls) throws NoSuchMethodException {
        D2.h.f(cls, "sslSocketClass");
        this.f2773e = cls;
        Method declaredMethod = cls.getDeclaredMethod("setUseSessionTickets", Boolean.TYPE);
        D2.h.e(declaredMethod, "sslSocketClass.getDeclar…:class.javaPrimitiveType)");
        this.f2769a = declaredMethod;
        this.f2770b = cls.getMethod("setHostname", String.class);
        this.f2771c = cls.getMethod("getAlpnSelectedProtocol", new Class[0]);
        this.f2772d = cls.getMethod("setAlpnProtocols", byte[].class);
    }

    @Override // X2.m
    public boolean a(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        return this.f2773e.isInstance(sSLSocket);
    }

    @Override // X2.m
    public boolean b() {
        return W2.b.f2704g.b();
    }

    @Override // X2.m
    public String c(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        if (!a(sSLSocket)) {
            return null;
        }
        try {
            byte[] bArr = (byte[]) this.f2771c.invoke(sSLSocket, new Object[0]);
            if (bArr == null) {
                return null;
            }
            Charset charset = StandardCharsets.UTF_8;
            D2.h.e(charset, "StandardCharsets.UTF_8");
            return new String(bArr, charset);
        } catch (IllegalAccessException e4) {
            throw new AssertionError(e4);
        } catch (NullPointerException e5) {
            if (D2.h.b(e5.getMessage(), "ssl == null")) {
                return null;
            }
            throw e5;
        } catch (InvocationTargetException e6) {
            throw new AssertionError(e6);
        }
    }

    @Override // X2.m
    public void d(SSLSocket sSLSocket, String str, List list) {
        D2.h.f(sSLSocket, "sslSocket");
        D2.h.f(list, "protocols");
        if (a(sSLSocket)) {
            try {
                this.f2769a.invoke(sSLSocket, Boolean.TRUE);
                if (str != null) {
                    this.f2770b.invoke(sSLSocket, str);
                }
                this.f2772d.invoke(sSLSocket, W2.j.f2732c.c(list));
            } catch (IllegalAccessException e4) {
                throw new AssertionError(e4);
            } catch (InvocationTargetException e5) {
                throw new AssertionError(e5);
            }
        }
    }
}
