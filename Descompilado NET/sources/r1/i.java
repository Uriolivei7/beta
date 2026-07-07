package R1;

import com.facebook.react.uimanager.W;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
final class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Integer f2046a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final W f2047b;

    /* JADX WARN: Multi-variable type inference failed */
    public i() {
        this(null, 0 == true ? 1 : 0, 3, 0 == true ? 1 : 0);
    }

    public final Integer a() {
        return this.f2046a;
    }

    public final W b() {
        return this.f2047b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof i)) {
            return false;
        }
        i iVar = (i) obj;
        return D2.h.b(this.f2046a, iVar.f2046a) && D2.h.b(this.f2047b, iVar.f2047b);
    }

    public int hashCode() {
        Integer num = this.f2046a;
        int iHashCode = (num == null ? 0 : num.hashCode()) * 31;
        W w3 = this.f2047b;
        return iHashCode + (w3 != null ? w3.hashCode() : 0);
    }

    public String toString() {
        return "ColorStop(color=" + this.f2046a + ", position=" + this.f2047b + ")";
    }

    public i(Integer num, W w3) {
        this.f2046a = num;
        this.f2047b = w3;
    }

    public /* synthetic */ i(Integer num, W w3, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? null : num, (i3 & 2) != 0 ? null : w3);
    }
}
