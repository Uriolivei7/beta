package R1;

import com.facebook.react.uimanager.C0429f0;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final float f2052a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final float f2053b;

    /* JADX WARN: Illegal instructions before constructor call */
    public k() {
        float f3 = 0.0f;
        this(f3, f3, 3, null);
    }

    public final float a() {
        return this.f2052a;
    }

    public final float b() {
        return this.f2053b;
    }

    public final k c() {
        return new k(C0429f0.h(this.f2052a), C0429f0.h(this.f2053b));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof k)) {
            return false;
        }
        k kVar = (k) obj;
        return Float.compare(this.f2052a, kVar.f2052a) == 0 && Float.compare(this.f2053b, kVar.f2053b) == 0;
    }

    public int hashCode() {
        return (Float.hashCode(this.f2052a) * 31) + Float.hashCode(this.f2053b);
    }

    public String toString() {
        return "CornerRadii(horizontal=" + this.f2052a + ", vertical=" + this.f2053b + ")";
    }

    public k(float f3, float f4) {
        this.f2052a = f3;
        this.f2053b = f4;
    }

    public /* synthetic */ k(float f3, float f4, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 0.0f : f3, (i3 & 2) != 0 ? 0.0f : f4);
    }
}
