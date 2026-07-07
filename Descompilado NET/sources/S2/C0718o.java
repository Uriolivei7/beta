package s2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: s2.o, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0718o {
    public static final Object[] a(Object[] objArr, boolean z3) {
        D2.h.f(objArr, "<this>");
        if (z3 && D2.h.b(objArr.getClass(), Object[].class)) {
            return objArr;
        }
        Object[] objArrCopyOf = Arrays.copyOf(objArr, objArr.length, Object[].class);
        D2.h.e(objArrCopyOf, "copyOf(...)");
        return objArrCopyOf;
    }

    public static List b(Object obj) {
        List listSingletonList = Collections.singletonList(obj);
        D2.h.e(listSingletonList, "singletonList(...)");
        return listSingletonList;
    }

    public static final Object[] c(int i3, Object[] objArr) {
        D2.h.f(objArr, "array");
        if (i3 < objArr.length) {
            objArr[i3] = null;
        }
        return objArr;
    }
}
