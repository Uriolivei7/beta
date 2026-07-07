package I0;

import D2.u;
import f0.C0531b;
import java.util.Arrays;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f425c = new a(null);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final h f426d = new h(-1, false);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final h f427e = new h(-2, false);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final h f428f = new h(-1, true);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f429a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final boolean f430b;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final h a() {
            return h.f426d;
        }

        public final h b() {
            return h.f428f;
        }

        public final h c() {
            return h.f427e;
        }

        private a() {
        }
    }

    public /* synthetic */ h(int i3, boolean z3, DefaultConstructorMarker defaultConstructorMarker) {
        this(i3, z3);
    }

    public static final h d() {
        return f425c.a();
    }

    public static final h e() {
        return f425c.b();
    }

    public static final h g() {
        return f425c.c();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof h)) {
            return false;
        }
        h hVar = (h) obj;
        return this.f429a == hVar.f429a && this.f430b == hVar.f430b;
    }

    public final boolean f() {
        return this.f430b;
    }

    public final int h() {
        if (j()) {
            throw new IllegalStateException("Rotation is set to use EXIF");
        }
        return this.f429a;
    }

    public int hashCode() {
        return C0531b.b(Integer.valueOf(this.f429a), Boolean.valueOf(this.f430b));
    }

    public final boolean i() {
        return this.f429a != -2;
    }

    public final boolean j() {
        return this.f429a == -1;
    }

    public String toString() {
        u uVar = u.f192a;
        String str = String.format(null, "%d defer:%b", Arrays.copyOf(new Object[]{Integer.valueOf(this.f429a), Boolean.valueOf(this.f430b)}, 2));
        D2.h.e(str, "format(...)");
        return str;
    }

    private h(int i3, boolean z3) {
        this.f429a = i3;
        this.f430b = z3;
    }
}
