package H2;

import D2.h;
import H2.a;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class f extends e {
    public static float b(float f3, float f4) {
        return f3 < f4 ? f4 : f3;
    }

    public static int c(int i3, int i4) {
        return i3 < i4 ? i4 : i3;
    }

    public static float d(float f3, float f4) {
        return f3 > f4 ? f4 : f3;
    }

    public static int e(int i3, int i4) {
        return i3 > i4 ? i4 : i3;
    }

    public static int f(int i3, int i4, int i5) {
        if (i4 <= i5) {
            return i3 < i4 ? i4 : i3 > i5 ? i5 : i3;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + i5 + " is less than minimum " + i4 + '.');
    }

    public static a g(int i3, int i4) {
        return a.f368e.a(i3, i4, -1);
    }

    public static a h(a aVar, int i3) {
        h.f(aVar, "<this>");
        e.a(i3 > 0, Integer.valueOf(i3));
        a.C0006a c0006a = a.f368e;
        int iA = aVar.a();
        int iB = aVar.b();
        if (aVar.c() <= 0) {
            i3 = -i3;
        }
        return c0006a.a(iA, iB, i3);
    }

    public static c i(int i3, int i4) {
        return i4 <= Integer.MIN_VALUE ? c.f376f.a() : new c(i3, i4 - 1);
    }
}
