package W2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLSocket;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class e extends j {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final b f2716i = new b(null);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Method f2717d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Method f2718e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Method f2719f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final Class f2720g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Class f2721h;

    private static final class a implements InvocationHandler {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean f2722a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f2723b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final List f2724c;

        public a(List<String> list) {
            D2.h.f(list, "protocols");
            this.f2724c = list;
        }

        public final String a() {
            return this.f2723b;
        }

        public final boolean b() {
            return this.f2722a;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) {
            D2.h.f(obj, "proxy");
            D2.h.f(method, "method");
            if (objArr == null) {
                objArr = new Object[0];
            }
            String name = method.getName();
            Class<?> returnType = method.getReturnType();
            if (D2.h.b(name, "supports") && D2.h.b(Boolean.TYPE, returnType)) {
                return Boolean.TRUE;
            }
            if (D2.h.b(name, "unsupported") && D2.h.b(Void.TYPE, returnType)) {
                this.f2722a = true;
                return null;
            }
            if (D2.h.b(name, "protocols") && objArr.length == 0) {
                return this.f2724c;
            }
            if ((D2.h.b(name, "selectProtocol") || D2.h.b(name, "select")) && D2.h.b(String.class, returnType) && objArr.length == 1) {
                Object obj2 = objArr[0];
                if (obj2 instanceof List) {
                    if (obj2 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.List<*>");
                    }
                    List list = (List) obj2;
                    int size = list.size();
                    if (size >= 0) {
                        int i3 = 0;
                        while (true) {
                            Object obj3 = list.get(i3);
                            if (obj3 == null) {
                                throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
                            }
                            String str = (String) obj3;
                            if (!this.f2724c.contains(str)) {
                                if (i3 == size) {
                                    break;
                                }
                                i3++;
                            } else {
                                this.f2723b = str;
                                return str;
                            }
                        }
                    }
                    String str2 = (String) this.f2724c.get(0);
                    this.f2723b = str2;
                    return str2;
                }
            }
            if ((!D2.h.b(name, "protocolSelected") && !D2.h.b(name, "selected")) || objArr.length != 1) {
                return method.invoke(this, Arrays.copyOf(objArr, objArr.length));
            }
            Object obj4 = objArr[0];
            if (obj4 == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
            }
            this.f2723b = (String) obj4;
            return null;
        }
    }

    public static final class b {
        private b() {
        }

        public final j a() {
            String property = System.getProperty("java.specification.version", "unknown");
            try {
                D2.h.e(property, "jvmVersion");
                if (Integer.parseInt(property) >= 9) {
                    return null;
                }
            } catch (NumberFormatException unused) {
            }
            try {
                Class<?> cls = Class.forName("org.eclipse.jetty.alpn.ALPN", true, null);
                Class<?> cls2 = Class.forName("org.eclipse.jetty.alpn.ALPN$Provider", true, null);
                Class<?> cls3 = Class.forName("org.eclipse.jetty.alpn.ALPN$ClientProvider", true, null);
                Class<?> cls4 = Class.forName("org.eclipse.jetty.alpn.ALPN$ServerProvider", true, null);
                Method method = cls.getMethod("put", SSLSocket.class, cls2);
                Method method2 = cls.getMethod("get", SSLSocket.class);
                Method method3 = cls.getMethod("remove", SSLSocket.class);
                D2.h.e(method, "putMethod");
                D2.h.e(method2, "getMethod");
                D2.h.e(method3, "removeMethod");
                D2.h.e(cls3, "clientProviderClass");
                D2.h.e(cls4, "serverProviderClass");
                return new e(method, method2, method3, cls3, cls4);
            } catch (ClassNotFoundException | NoSuchMethodException unused2) {
                return null;
            }
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public e(Method method, Method method2, Method method3, Class<?> cls, Class<?> cls2) {
        D2.h.f(method, "putMethod");
        D2.h.f(method2, "getMethod");
        D2.h.f(method3, "removeMethod");
        D2.h.f(cls, "clientProviderClass");
        D2.h.f(cls2, "serverProviderClass");
        this.f2717d = method;
        this.f2718e = method2;
        this.f2719f = method3;
        this.f2720g = cls;
        this.f2721h = cls2;
    }

    @Override // W2.j
    public void b(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        try {
            this.f2719f.invoke(null, sSLSocket);
        } catch (IllegalAccessException e4) {
            throw new AssertionError("failed to remove ALPN", e4);
        } catch (InvocationTargetException e5) {
            throw new AssertionError("failed to remove ALPN", e5);
        }
    }

    @Override // W2.j
    public void e(SSLSocket sSLSocket, String str, List list) {
        D2.h.f(sSLSocket, "sslSocket");
        D2.h.f(list, "protocols");
        try {
            this.f2717d.invoke(null, sSLSocket, Proxy.newProxyInstance(j.class.getClassLoader(), new Class[]{this.f2720g, this.f2721h}, new a(j.f2732c.b(list))));
        } catch (IllegalAccessException e4) {
            throw new AssertionError("failed to set ALPN", e4);
        } catch (InvocationTargetException e5) {
            throw new AssertionError("failed to set ALPN", e5);
        }
    }

    @Override // W2.j
    public String h(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        try {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(this.f2718e.invoke(null, sSLSocket));
            if (invocationHandler == null) {
                throw new NullPointerException("null cannot be cast to non-null type okhttp3.internal.platform.Jdk8WithJettyBootPlatform.AlpnProvider");
            }
            a aVar = (a) invocationHandler;
            if (!aVar.b() && aVar.a() == null) {
                j.l(this, "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?", 0, null, 6, null);
                return null;
            }
            if (aVar.b()) {
                return null;
            }
            return aVar.a();
        } catch (IllegalAccessException e4) {
            throw new AssertionError("failed to get ALPN selected protocol", e4);
        } catch (InvocationTargetException e5) {
            throw new AssertionError("failed to get ALPN selected protocol", e5);
        }
    }
}
