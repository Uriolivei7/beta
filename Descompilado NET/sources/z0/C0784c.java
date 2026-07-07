package z0;

/* JADX INFO: renamed from: z0.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0784c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f11043a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f11044b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f11045c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f11046d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final int f11047e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final int f11048f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final String f11049g;

    public C0784c(int i3, int i4, int i5, int i6, int i7, int i8, String str) {
        D2.h.f(str, "scaleType");
        this.f11043a = i3;
        this.f11044b = i4;
        this.f11045c = i5;
        this.f11046d = i6;
        this.f11047e = i7;
        this.f11048f = i8;
        this.f11049g = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!D2.h.b(C0784c.class, obj != null ? obj.getClass() : null)) {
            return false;
        }
        D2.h.d(obj, "null cannot be cast to non-null type com.facebook.fresco.ui.common.DimensionsInfo");
        C0784c c0784c = (C0784c) obj;
        return this.f11043a == c0784c.f11043a && this.f11044b == c0784c.f11044b && this.f11045c == c0784c.f11045c && this.f11046d == c0784c.f11046d && this.f11047e == c0784c.f11047e && this.f11048f == c0784c.f11048f && D2.h.b(this.f11049g, c0784c.f11049g);
    }

    public int hashCode() {
        return (((((((((((this.f11043a * 31) + this.f11044b) * 31) + this.f11045c) * 31) + this.f11046d) * 31) + this.f11047e) * 31) + this.f11048f) * 31) + this.f11049g.hashCode();
    }

    public String toString() {
        return "DimensionsInfo(viewportWidth=" + this.f11043a + ", viewportHeight=" + this.f11044b + ", encodedImageWidth=" + this.f11045c + ", encodedImageHeight=" + this.f11046d + ", decodedImageWidth=" + this.f11047e + ", decodedImageHeight=" + this.f11048f + ", scaleType=" + this.f11049g + ")";
    }
}
