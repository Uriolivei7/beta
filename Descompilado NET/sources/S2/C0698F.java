package s2;

import java.util.Collections;
import java.util.Map;
import r2.C0686i;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: s2.F, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0698F extends C0697E {
    public static Map a(Map map) {
        D2.h.f(map, "builder");
        return ((t2.c) map).l();
    }

    public static Map b() {
        return new t2.c();
    }

    public static int c(int i3) {
        if (i3 < 0) {
            return i3;
        }
        if (i3 < 3) {
            return i3 + 1;
        }
        if (i3 < 1073741824) {
            return (int) ((i3 / 0.75f) + 1.0f);
        }
        return Integer.MAX_VALUE;
    }

    public static Map d(C0686i c0686i) {
        D2.h.f(c0686i, "pair");
        Map mapSingletonMap = Collections.singletonMap(c0686i.c(), c0686i.d());
        D2.h.e(mapSingletonMap, "singletonMap(...)");
        return mapSingletonMap;
    }

    public static final Map e(Map map) {
        D2.h.f(map, "<this>");
        Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();
        Map mapSingletonMap = Collections.singletonMap(entry.getKey(), entry.getValue());
        D2.h.e(mapSingletonMap, "with(...)");
        return mapSingletonMap;
    }
}
