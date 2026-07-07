package W0;

import D2.u;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2681a;

    public b(int i3) {
        this.f2681a = i3;
    }

    public final int a() {
        return this.f2681a;
    }

    public String toString() {
        u uVar = u.f192a;
        String str = String.format(null, "Status: %d", Arrays.copyOf(new Object[]{Integer.valueOf(this.f2681a)}, 1));
        D2.h.e(str, "format(...)");
        return str;
    }
}
