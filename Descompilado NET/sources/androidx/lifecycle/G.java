package androidx.lifecycle;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class G {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Map f5281a = new LinkedHashMap();

    public final void a() {
        Iterator it = this.f5281a.values().iterator();
        while (it.hasNext()) {
            ((D) it.next()).a();
        }
        this.f5281a.clear();
    }

    public final D b(String str) {
        D2.h.f(str, "key");
        return (D) this.f5281a.get(str);
    }

    public final Set c() {
        return new HashSet(this.f5281a.keySet());
    }

    public final void d(String str, D d4) {
        D2.h.f(str, "key");
        D2.h.f(d4, "viewModel");
        D d5 = (D) this.f5281a.put(str, d4);
        if (d5 != null) {
            d5.d();
        }
    }
}
