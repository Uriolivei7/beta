package s2;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class u extends t {
    public static boolean t(Collection collection, Iterable iterable) {
        D2.h.f(collection, "<this>");
        D2.h.f(iterable, "elements");
        if (iterable instanceof Collection) {
            return collection.addAll((Collection) iterable);
        }
        Iterator it = iterable.iterator();
        boolean z3 = false;
        while (it.hasNext()) {
            if (collection.add(it.next())) {
                z3 = true;
            }
        }
        return z3;
    }

    public static boolean u(Collection collection, Object[] objArr) {
        D2.h.f(collection, "<this>");
        D2.h.f(objArr, "elements");
        return collection.addAll(AbstractC0711h.d(objArr));
    }

    private static final boolean v(Iterable iterable, C2.l lVar, boolean z3) {
        Iterator it = iterable.iterator();
        boolean z4 = false;
        while (it.hasNext()) {
            if (((Boolean) lVar.d(it.next())).booleanValue() == z3) {
                it.remove();
                z4 = true;
            }
        }
        return z4;
    }

    private static final boolean w(List list, C2.l lVar, boolean z3) {
        int i3;
        if (!(list instanceof RandomAccess)) {
            D2.h.d(list, "null cannot be cast to non-null type kotlin.collections.MutableIterable<T of kotlin.collections.CollectionsKt__MutableCollectionsKt.filterInPlace>");
            return v(D2.v.a(list), lVar, z3);
        }
        int i4 = AbstractC0717n.i(list);
        if (i4 >= 0) {
            int i5 = 0;
            i3 = 0;
            while (true) {
                Object obj = list.get(i5);
                if (((Boolean) lVar.d(obj)).booleanValue() != z3) {
                    if (i3 != i5) {
                        list.set(i3, obj);
                    }
                    i3++;
                }
                if (i5 == i4) {
                    break;
                }
                i5++;
            }
        } else {
            i3 = 0;
        }
        if (i3 >= list.size()) {
            return false;
        }
        int i6 = AbstractC0717n.i(list);
        if (i3 > i6) {
            return true;
        }
        while (true) {
            list.remove(i6);
            if (i6 == i3) {
                return true;
            }
            i6--;
        }
    }

    public static boolean x(List list, C2.l lVar) {
        D2.h.f(list, "<this>");
        D2.h.f(lVar, "predicate");
        return w(list, lVar, true);
    }
}
