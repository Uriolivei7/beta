package I0;

import D2.u;
import f0.C0531b;
import java.util.Arrays;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f420e = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f421a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f422b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final float f423c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final float f424d;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public g(int i3, int i4) {
        this(i3, i4, 0.0f, 0.0f, 12, null);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof g) {
            g gVar = (g) obj;
            if (this.f421a == gVar.f421a && this.f422b == gVar.f422b) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return C0531b.a(this.f421a, this.f422b);
    }

    public String toString() {
        u uVar = u.f192a;
        String str = String.format(null, "%dx%d", Arrays.copyOf(new Object[]{Integer.valueOf(this.f421a), Integer.valueOf(this.f422b)}, 2));
        D2.h.e(str, "format(...)");
        return str;
    }

    public g(int i3, int i4, float f3) {
        this(i3, i4, f3, 0.0f, 8, null);
    }

    public g(int i3, int i4, float f3, float f4) {
        this.f421a = i3;
        this.f422b = i4;
        this.f423c = f3;
        this.f424d = f4;
        if (i3 <= 0) {
            throw new IllegalStateException("Check failed.");
        }
        if (i4 <= 0) {
            throw new IllegalStateException("Check failed.");
        }
    }

    public /* synthetic */ g(int i3, int i4, float f3, float f4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(i3, i4, (i5 & 4) != 0 ? 2048.0f : f3, (i5 & 8) != 0 ? 0.6666667f : f4);
    }
}
