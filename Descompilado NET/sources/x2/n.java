package X2;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class n extends h {

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final a f2785j = new a(null);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Class f2786h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final Class f2787i;

    public static final class a {
        private a() {
        }

        public static /* synthetic */ m b(a aVar, String str, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                str = "com.android.org.conscrypt";
            }
            return aVar.a(str);
        }

        public final m a(String str) {
            D2.h.f(str, "packageName");
            try {
                Class<?> cls = Class.forName(str + ".OpenSSLSocketImpl");
                Class<?> cls2 = Class.forName(str + ".OpenSSLSocketFactoryImpl");
                Class<?> cls3 = Class.forName(str + ".SSLParametersImpl");
                D2.h.e(cls3, "paramsClass");
                return new n(cls, cls2, cls3);
            } catch (Exception e4) {
                W2.j.f2732c.g().k("unable to load android socket classes", 5, e4);
                return null;
            }
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public n(Class<? super SSLSocket> cls, Class<? super SSLSocketFactory> cls2, Class<?> cls3) {
        super(cls);
        D2.h.f(cls, "sslSocketClass");
        D2.h.f(cls2, "sslSocketFactoryClass");
        D2.h.f(cls3, "paramClass");
        this.f2786h = cls2;
        this.f2787i = cls3;
    }
}
