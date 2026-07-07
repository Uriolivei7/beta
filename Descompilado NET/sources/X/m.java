package X;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* JADX INFO: loaded from: classes.dex */
public abstract class m {
    public static CopyOnWriteArraySet a() {
        return new CopyOnWriteArraySet();
    }

    public static Set b() {
        return c(new IdentityHashMap());
    }

    public static Set c(Map map) {
        return Collections.newSetFromMap(map);
    }
}
