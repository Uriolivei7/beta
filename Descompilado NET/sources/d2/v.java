package D2;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class v {
    public static Iterable a(Object obj) {
        if ((obj instanceof E2.a) && !(obj instanceof E2.b)) {
            g(obj, "kotlin.collections.MutableIterable");
        }
        return c(obj);
    }

    public static List b(Object obj) {
        if ((obj instanceof E2.a) && !(obj instanceof E2.c)) {
            g(obj, "kotlin.collections.MutableList");
        }
        return d(obj);
    }

    public static Iterable c(Object obj) {
        try {
            return (Iterable) obj;
        } catch (ClassCastException e4) {
            throw f(e4);
        }
    }

    public static List d(Object obj) {
        try {
            return (List) obj;
        } catch (ClassCastException e4) {
            throw f(e4);
        }
    }

    private static Throwable e(Throwable th) {
        return h.k(th, v.class.getName());
    }

    public static ClassCastException f(ClassCastException classCastException) {
        throw ((ClassCastException) e(classCastException));
    }

    public static void g(Object obj, String str) {
        h((obj == null ? "null" : obj.getClass().getName()) + " cannot be cast to " + str);
    }

    public static void h(String str) {
        throw f(new ClassCastException(str));
    }
}
