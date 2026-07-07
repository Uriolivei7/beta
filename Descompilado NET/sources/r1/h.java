package R1;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2042a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f2043b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f2044c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f2045d;

    public h() {
        this(0, 0, 0, 0, 15, null);
    }

    public final int a() {
        return this.f2045d;
    }

    public final int b() {
        return this.f2042a;
    }

    public final int c() {
        return this.f2044c;
    }

    public final int d() {
        return this.f2043b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof h)) {
            return false;
        }
        h hVar = (h) obj;
        return this.f2042a == hVar.f2042a && this.f2043b == hVar.f2043b && this.f2044c == hVar.f2044c && this.f2045d == hVar.f2045d;
    }

    public int hashCode() {
        return (((((Integer.hashCode(this.f2042a) * 31) + Integer.hashCode(this.f2043b)) * 31) + Integer.hashCode(this.f2044c)) * 31) + Integer.hashCode(this.f2045d);
    }

    public String toString() {
        return "ColorEdges(left=" + this.f2042a + ", top=" + this.f2043b + ", right=" + this.f2044c + ", bottom=" + this.f2045d + ")";
    }

    public h(int i3, int i4, int i5, int i6) {
        this.f2042a = i3;
        this.f2043b = i4;
        this.f2044c = i5;
        this.f2045d = i6;
    }

    public /* synthetic */ h(int i3, int i4, int i5, int i6, int i7, DefaultConstructorMarker defaultConstructorMarker) {
        this((i7 & 1) != 0 ? -16777216 : i3, (i7 & 2) != 0 ? -16777216 : i4, (i7 & 4) != 0 ? -16777216 : i5, (i7 & 8) != 0 ? -16777216 : i6);
    }
}
