package I0;

import X.i;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;

/* JADX INFO: loaded from: classes.dex */
public class d {

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static final d f389m = b().a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f390a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f391b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final boolean f392c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final boolean f393d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final boolean f394e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final boolean f395f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final boolean f396g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final Bitmap.Config f397h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final Bitmap.Config f398i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final M0.c f399j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final ColorSpace f400k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final boolean f401l;

    public d(e eVar) {
        this.f390a = eVar.l();
        this.f391b = eVar.k();
        this.f392c = eVar.h();
        this.f393d = eVar.n();
        this.f394e = eVar.m();
        this.f395f = eVar.g();
        this.f396g = eVar.j();
        this.f397h = eVar.c();
        this.f398i = eVar.b();
        this.f399j = eVar.f();
        eVar.d();
        this.f400k = eVar.e();
        this.f401l = eVar.i();
    }

    public static d a() {
        return f389m;
    }

    public static e b() {
        return new e();
    }

    protected i.a c() {
        return X.i.b(this).a("minDecodeIntervalMs", this.f390a).a("maxDimensionPx", this.f391b).c("decodePreviewFrame", this.f392c).c("useLastFrameForPreview", this.f393d).c("useEncodedImageForPreview", this.f394e).c("decodeAllFrames", this.f395f).c("forceStaticImage", this.f396g).b("bitmapConfigName", this.f397h.name()).b("animatedBitmapConfigName", this.f398i.name()).b("customImageDecoder", this.f399j).b("bitmapTransformation", null).b("colorSpace", this.f400k);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        d dVar = (d) obj;
        if (this.f390a != dVar.f390a || this.f391b != dVar.f391b || this.f392c != dVar.f392c || this.f393d != dVar.f393d || this.f394e != dVar.f394e || this.f395f != dVar.f395f || this.f396g != dVar.f396g) {
            return false;
        }
        boolean z3 = this.f401l;
        if (z3 || this.f397h == dVar.f397h) {
            return (z3 || this.f398i == dVar.f398i) && this.f399j == dVar.f399j && this.f400k == dVar.f400k;
        }
        return false;
    }

    public int hashCode() {
        int iOrdinal = (((((((((((this.f390a * 31) + this.f391b) * 31) + (this.f392c ? 1 : 0)) * 31) + (this.f393d ? 1 : 0)) * 31) + (this.f394e ? 1 : 0)) * 31) + (this.f395f ? 1 : 0)) * 31) + (this.f396g ? 1 : 0);
        if (!this.f401l) {
            iOrdinal = (iOrdinal * 31) + this.f397h.ordinal();
        }
        if (!this.f401l) {
            int i3 = iOrdinal * 31;
            Bitmap.Config config = this.f398i;
            iOrdinal = i3 + (config != null ? config.ordinal() : 0);
        }
        int i4 = iOrdinal * 31;
        M0.c cVar = this.f399j;
        int iHashCode = (i4 + (cVar != null ? cVar.hashCode() : 0)) * 961;
        ColorSpace colorSpace = this.f400k;
        return iHashCode + (colorSpace != null ? colorSpace.hashCode() : 0);
    }

    public String toString() {
        return "ImageDecodeOptions{" + c().toString() + "}";
    }
}
