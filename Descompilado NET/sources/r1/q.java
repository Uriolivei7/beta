package R1;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
final class q {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Integer f2101a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Float f2102b;

    /* JADX WARN: Multi-variable type inference failed */
    public q() {
        this(null, 0 == true ? 1 : 0, 3, 0 == true ? 1 : 0);
    }

    public final Integer a() {
        return this.f2101a;
    }

    public final Float b() {
        return this.f2102b;
    }

    public final void c(Integer num) {
        this.f2101a = num;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof q)) {
            return false;
        }
        q qVar = (q) obj;
        return D2.h.b(this.f2101a, qVar.f2101a) && D2.h.b(this.f2102b, qVar.f2102b);
    }

    public int hashCode() {
        Integer num = this.f2101a;
        int iHashCode = (num == null ? 0 : num.hashCode()) * 31;
        Float f3 = this.f2102b;
        return iHashCode + (f3 != null ? f3.hashCode() : 0);
    }

    public String toString() {
        return "ProcessedColorStop(color=" + this.f2101a + ", position=" + this.f2102b + ")";
    }

    public q(Integer num, Float f3) {
        this.f2101a = num;
        this.f2102b = f3;
    }

    public /* synthetic */ q(Integer num, Float f3, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? null : num, (i3 & 2) != 0 ? null : f3);
    }
}
