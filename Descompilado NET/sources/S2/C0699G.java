package s2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import r2.C0686i;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: s2.G, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0699G extends C0698F {
    public static Map f() {
        C0693A c0693a = C0693A.f10592b;
        D2.h.d(c0693a, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.MapsKt__MapsKt.emptyMap, V of kotlin.collections.MapsKt__MapsKt.emptyMap>");
        return c0693a;
    }

    public static HashMap g(C0686i... c0686iArr) {
        D2.h.f(c0686iArr, "pairs");
        HashMap map = new HashMap(AbstractC0696D.c(c0686iArr.length));
        l(map, c0686iArr);
        return map;
    }

    public static Map h(C0686i... c0686iArr) {
        D2.h.f(c0686iArr, "pairs");
        return c0686iArr.length > 0 ? p(c0686iArr, new LinkedHashMap(AbstractC0696D.c(c0686iArr.length))) : AbstractC0696D.f();
    }

    public static Map i(C0686i... c0686iArr) {
        D2.h.f(c0686iArr, "pairs");
        LinkedHashMap linkedHashMap = new LinkedHashMap(AbstractC0696D.c(c0686iArr.length));
        l(linkedHashMap, c0686iArr);
        return linkedHashMap;
    }

    public static final Map j(Map map) {
        D2.h.f(map, "<this>");
        int size = map.size();
        return size != 0 ? size != 1 ? map : C0698F.e(map) : AbstractC0696D.f();
    }

    public static final void k(Map map, Iterable iterable) {
        D2.h.f(map, "<this>");
        D2.h.f(iterable, "pairs");
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            C0686i c0686i = (C0686i) it.next();
            map.put(c0686i.a(), c0686i.b());
        }
    }

    public static final void l(Map map, C0686i[] c0686iArr) {
        D2.h.f(map, "<this>");
        D2.h.f(c0686iArr, "pairs");
        for (C0686i c0686i : c0686iArr) {
            map.put(c0686i.a(), c0686i.b());
        }
    }

    public static Map m(Iterable iterable) {
        D2.h.f(iterable, "<this>");
        if (!(iterable instanceof Collection)) {
            return j(n(iterable, new LinkedHashMap()));
        }
        Collection collection = (Collection) iterable;
        int size = collection.size();
        if (size == 0) {
            return AbstractC0696D.f();
        }
        if (size != 1) {
            return n(iterable, new LinkedHashMap(AbstractC0696D.c(collection.size())));
        }
        return AbstractC0696D.d((C0686i) (iterable instanceof List ? ((List) iterable).get(0) : iterable.iterator().next()));
    }

    public static final Map n(Iterable iterable, Map map) {
        D2.h.f(iterable, "<this>");
        D2.h.f(map, "destination");
        k(map, iterable);
        return map;
    }

    public static Map o(Map map) {
        D2.h.f(map, "<this>");
        int size = map.size();
        return size != 0 ? size != 1 ? AbstractC0696D.q(map) : C0698F.e(map) : AbstractC0696D.f();
    }

    public static final Map p(C0686i[] c0686iArr, Map map) {
        D2.h.f(c0686iArr, "<this>");
        D2.h.f(map, "destination");
        l(map, c0686iArr);
        return map;
    }

    public static Map q(Map map) {
        D2.h.f(map, "<this>");
        return new LinkedHashMap(map);
    }
}
