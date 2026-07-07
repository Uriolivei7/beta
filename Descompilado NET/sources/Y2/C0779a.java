package y2;

import D2.h;
import java.lang.reflect.InvocationTargetException;
import x2.C0772a;

/* JADX INFO: renamed from: y2.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0779a extends C0772a {

    /* JADX INFO: renamed from: y2.a$a, reason: collision with other inner class name */
    private static final class C0160a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final C0160a f11019a = new C0160a();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final Integer f11020b;

        static {
            Object obj;
            Integer num = null;
            try {
                obj = Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
            } catch (Throwable unused) {
            }
            Integer num2 = obj instanceof Integer ? (Integer) obj : null;
            if (num2 != null && num2.intValue() > 0) {
                num = num2;
            }
            f11020b = num;
        }

        private C0160a() {
        }
    }

    private final boolean b(int i3) {
        Integer num = C0160a.f11020b;
        return num == null || num.intValue() >= i3;
    }

    @Override // x2.C0772a
    public void a(Throwable th, Throwable th2) throws IllegalAccessException, InvocationTargetException {
        h.f(th, "cause");
        h.f(th2, "exception");
        if (b(19)) {
            th.addSuppressed(th2);
        } else {
            super.a(th, th2);
        }
    }
}
