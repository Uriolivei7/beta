package X;

/* JADX INFO: loaded from: classes.dex */
public abstract class k {
    private static String a(int i3, int i4, String str) {
        if (i3 < 0) {
            return k("%s (%s) must not be negative", str, Integer.valueOf(i3));
        }
        if (i4 >= 0) {
            return k("%s (%s) must be less than size (%s)", str, Integer.valueOf(i3), Integer.valueOf(i4));
        }
        throw new IllegalArgumentException("negative size: " + i4);
    }

    public static void b(Boolean bool) {
        if (bool != null && !bool.booleanValue()) {
            throw new IllegalArgumentException();
        }
    }

    public static void c(boolean z3, Object obj) {
        if (!z3) {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
    }

    public static void d(boolean z3, String str, Object... objArr) {
        if (!z3) {
            throw new IllegalArgumentException(k(str, objArr));
        }
    }

    public static int e(int i3, int i4) {
        return f(i3, i4, "index");
    }

    public static int f(int i3, int i4, String str) {
        if (i3 < 0 || i3 >= i4) {
            throw new IndexOutOfBoundsException(a(i3, i4, str));
        }
        return i3;
    }

    public static Object g(Object obj) {
        obj.getClass();
        return obj;
    }

    public static Object h(Object obj, Object obj2) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(String.valueOf(obj2));
    }

    public static void i(boolean z3) {
        if (!z3) {
            throw new IllegalStateException();
        }
    }

    public static void j(boolean z3, Object obj) {
        if (!z3) {
            throw new IllegalStateException(String.valueOf(obj));
        }
    }

    static String k(String str, Object... objArr) {
        int iIndexOf;
        String strValueOf = String.valueOf(str);
        StringBuilder sb = new StringBuilder(strValueOf.length() + (objArr.length * 16));
        int i3 = 0;
        int i4 = 0;
        while (i3 < objArr.length && (iIndexOf = strValueOf.indexOf("%s", i4)) != -1) {
            sb.append(strValueOf.substring(i4, iIndexOf));
            sb.append(objArr[i3]);
            i4 = iIndexOf + 2;
            i3++;
        }
        sb.append(strValueOf.substring(i4));
        if (i3 < objArr.length) {
            sb.append(" [");
            sb.append(objArr[i3]);
            for (int i5 = i3 + 1; i5 < objArr.length; i5++) {
                sb.append(", ");
                sb.append(objArr[i5]);
            }
            sb.append(']');
        }
        return sb.toString();
    }
}
