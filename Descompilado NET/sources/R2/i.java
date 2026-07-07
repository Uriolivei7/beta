package R2;

import M2.F;
import java.util.LinkedHashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Set f2191a = new LinkedHashSet();

    public final synchronized void a(F f3) {
        D2.h.f(f3, "route");
        this.f2191a.remove(f3);
    }

    public final synchronized void b(F f3) {
        D2.h.f(f3, "failedRoute");
        this.f2191a.add(f3);
    }

    public final synchronized boolean c(F f3) {
        D2.h.f(f3, "route");
        return this.f2191a.contains(f3);
    }
}
