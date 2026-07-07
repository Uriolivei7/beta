package X2;

import java.lang.reflect.Method;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class j {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f2777d = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Method f2778a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Method f2779b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Method f2780c;

    public static final class a {
        private a() {
        }

        public final j a() {
            Method method;
            Method method2;
            Method method3;
            try {
                Class<?> cls = Class.forName("dalvik.system.CloseGuard");
                method = cls.getMethod("get", new Class[0]);
                method3 = cls.getMethod("open", String.class);
                method2 = cls.getMethod("warnIfOpen", new Class[0]);
            } catch (Exception unused) {
                method = null;
                method2 = null;
                method3 = null;
            }
            return new j(method, method3, method2);
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public j(Method method, Method method2, Method method3) {
        this.f2778a = method;
        this.f2779b = method2;
        this.f2780c = method3;
    }

    public final Object a(String str) {
        D2.h.f(str, "closer");
        Method method = this.f2778a;
        if (method != null) {
            try {
                Object objInvoke = method.invoke(null, new Object[0]);
                Method method2 = this.f2779b;
                D2.h.c(method2);
                method2.invoke(objInvoke, str);
                return objInvoke;
            } catch (Exception unused) {
            }
        }
        return null;
    }

    public final boolean b(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            Method method = this.f2780c;
            D2.h.c(method);
            method.invoke(obj, new Object[0]);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}
