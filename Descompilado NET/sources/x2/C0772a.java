package x2;

import D2.h;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import s2.AbstractC0711h;

/* JADX INFO: renamed from: x2.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0772a {

    /* JADX INFO: renamed from: x2.a$a, reason: collision with other inner class name */
    private static final class C0156a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final C0156a f10955a = new C0156a();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final Method f10956b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final Method f10957c;

        static {
            Method method;
            Method method2;
            Method[] methods = Throwable.class.getMethods();
            h.c(methods);
            int length = methods.length;
            int i3 = 0;
            int i4 = 0;
            while (true) {
                method = null;
                if (i4 >= length) {
                    method2 = null;
                    break;
                }
                method2 = methods[i4];
                if (h.b(method2.getName(), "addSuppressed")) {
                    Class<?>[] parameterTypes = method2.getParameterTypes();
                    h.e(parameterTypes, "getParameterTypes(...)");
                    if (h.b(AbstractC0711h.A(parameterTypes), Throwable.class)) {
                        break;
                    }
                }
                i4++;
            }
            f10956b = method2;
            int length2 = methods.length;
            while (true) {
                if (i3 >= length2) {
                    break;
                }
                Method method3 = methods[i3];
                if (h.b(method3.getName(), "getSuppressed")) {
                    method = method3;
                    break;
                }
                i3++;
            }
            f10957c = method;
        }

        private C0156a() {
        }
    }

    public void a(Throwable th, Throwable th2) throws IllegalAccessException, InvocationTargetException {
        h.f(th, "cause");
        h.f(th2, "exception");
        Method method = C0156a.f10956b;
        if (method != null) {
            method.invoke(th, th2);
        }
    }
}
