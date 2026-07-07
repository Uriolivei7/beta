package u0;

import X.k;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private a f10833a = a.BITMAP_ONLY;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f10834b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private float[] f10835c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f10836d = 0;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private float f10837e = 0.0f;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f10838f = 0;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private float f10839g = 0.0f;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f10840h = false;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f10841i = false;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f10842j = false;

    public enum a {
        OVERLAY_COLOR,
        BITMAP_ONLY
    }

    public static e a(float f3) {
        return new e().p(f3);
    }

    private float[] e() {
        if (this.f10835c == null) {
            this.f10835c = new float[8];
        }
        return this.f10835c;
    }

    public int b() {
        return this.f10838f;
    }

    public float c() {
        return this.f10837e;
    }

    public float[] d() {
        return this.f10835c;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        e eVar = (e) obj;
        if (this.f10834b == eVar.f10834b && this.f10836d == eVar.f10836d && Float.compare(eVar.f10837e, this.f10837e) == 0 && this.f10838f == eVar.f10838f && Float.compare(eVar.f10839g, this.f10839g) == 0 && this.f10833a == eVar.f10833a && this.f10840h == eVar.f10840h && this.f10841i == eVar.f10841i) {
            return Arrays.equals(this.f10835c, eVar.f10835c);
        }
        return false;
    }

    public int f() {
        return this.f10836d;
    }

    public float g() {
        return this.f10839g;
    }

    public boolean h() {
        return this.f10841i;
    }

    public int hashCode() {
        a aVar = this.f10833a;
        int iHashCode = (((aVar != null ? aVar.hashCode() : 0) * 31) + (this.f10834b ? 1 : 0)) * 31;
        float[] fArr = this.f10835c;
        int iHashCode2 = (((iHashCode + (fArr != null ? Arrays.hashCode(fArr) : 0)) * 31) + this.f10836d) * 31;
        float f3 = this.f10837e;
        int iFloatToIntBits = (((iHashCode2 + (f3 != 0.0f ? Float.floatToIntBits(f3) : 0)) * 31) + this.f10838f) * 31;
        float f4 = this.f10839g;
        return ((((iFloatToIntBits + (f4 != 0.0f ? Float.floatToIntBits(f4) : 0)) * 31) + (this.f10840h ? 1 : 0)) * 31) + (this.f10841i ? 1 : 0);
    }

    public boolean i() {
        return this.f10842j;
    }

    public boolean j() {
        return this.f10834b;
    }

    public a k() {
        return this.f10833a;
    }

    public boolean l() {
        return this.f10840h;
    }

    public e m(int i3) {
        this.f10838f = i3;
        return this;
    }

    public e n(float f3) {
        k.c(f3 >= 0.0f, "the border width cannot be < 0");
        this.f10837e = f3;
        return this;
    }

    public e o(float f3, float f4, float f5, float f6) {
        float[] fArrE = e();
        fArrE[1] = f3;
        fArrE[0] = f3;
        fArrE[3] = f4;
        fArrE[2] = f4;
        fArrE[5] = f5;
        fArrE[4] = f5;
        fArrE[7] = f6;
        fArrE[6] = f6;
        return this;
    }

    public e p(float f3) {
        Arrays.fill(e(), f3);
        return this;
    }

    public e q(int i3) {
        this.f10836d = i3;
        this.f10833a = a.OVERLAY_COLOR;
        return this;
    }

    public e r(float f3) {
        k.c(f3 >= 0.0f, "the padding cannot be < 0");
        this.f10839g = f3;
        return this;
    }

    public e s(boolean z3) {
        this.f10841i = z3;
        return this;
    }

    public e t(boolean z3) {
        this.f10834b = z3;
        return this;
    }

    public e u(a aVar) {
        this.f10833a = aVar;
        return this;
    }
}
