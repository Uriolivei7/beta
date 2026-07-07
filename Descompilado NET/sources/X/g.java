package X;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class g extends HashMap {
    private g(Map map) {
        super(map);
    }

    public static g b(Map map) {
        return new g(map);
    }

    public static Map of(Object obj, Object obj2) {
        HashMap map = new HashMap(1);
        map.put(obj, obj2);
        return Collections.unmodifiableMap(map);
    }

    public static Map of(Object obj, Object obj2, Object obj3, Object obj4) {
        HashMap map = new HashMap(2);
        map.put(obj, obj2);
        map.put(obj3, obj4);
        return Collections.unmodifiableMap(map);
    }
}
