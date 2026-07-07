package s2;

import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class N extends M {
    public static Set b() {
        return C0694B.f10593b;
    }

    public static final Set c(Set set) {
        D2.h.f(set, "<this>");
        int size = set.size();
        return size != 0 ? size != 1 ? set : M.a(set.iterator().next()) : L.b();
    }
}
