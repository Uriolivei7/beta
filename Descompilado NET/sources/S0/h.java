package S0;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final boolean f2310a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final boolean f2311b;

    /* JADX WARN: Illegal instructions before constructor call */
    public h() {
        boolean z3 = false;
        this(z3, z3, 3, null);
    }

    public final boolean a() {
        return this.f2310a;
    }

    public final boolean b() {
        return this.f2311b;
    }

    public h(boolean z3, boolean z4) {
        this.f2310a = z3;
        this.f2311b = z4;
    }

    public /* synthetic */ h(boolean z3, boolean z4, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? false : z3, (i3 & 2) != 0 ? false : z4);
    }
}
