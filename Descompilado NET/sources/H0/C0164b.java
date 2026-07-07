package H0;

import android.net.Uri;
import com.facebook.common.time.RealtimeSinceBootClock;

/* JADX INFO: renamed from: H0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0164b implements R.d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f262a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final I0.g f263b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final I0.h f264c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final I0.d f265d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final R.d f266e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final String f267f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private Object f268g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final int f269h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final long f270i;

    public C0164b(String str, I0.g gVar, I0.h hVar, I0.d dVar, R.d dVar2, String str2) {
        D2.h.f(str, "sourceString");
        D2.h.f(hVar, "rotationOptions");
        D2.h.f(dVar, "imageDecodeOptions");
        this.f262a = str;
        this.f263b = gVar;
        this.f264c = hVar;
        this.f265d = dVar;
        this.f266e = dVar2;
        this.f267f = str2;
        this.f269h = (((((((((str.hashCode() * 31) + (gVar != null ? gVar.hashCode() : 0)) * 31) + hVar.hashCode()) * 31) + dVar.hashCode()) * 31) + (dVar2 != null ? dVar2.hashCode() : 0)) * 31) + (str2 != null ? str2.hashCode() : 0);
        this.f270i = RealtimeSinceBootClock.get().now();
    }

    @Override // R.d
    public boolean a() {
        return false;
    }

    @Override // R.d
    public boolean b(Uri uri) {
        D2.h.f(uri, "uri");
        String strC = c();
        String string = uri.toString();
        D2.h.e(string, "toString(...)");
        return K2.o.E(strC, string, false, 2, null);
    }

    @Override // R.d
    public String c() {
        return this.f262a;
    }

    public final void d(Object obj) {
        this.f268g = obj;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!D2.h.b(C0164b.class, obj != null ? obj.getClass() : null)) {
            return false;
        }
        D2.h.d(obj, "null cannot be cast to non-null type com.facebook.imagepipeline.cache.BitmapMemoryCacheKey");
        C0164b c0164b = (C0164b) obj;
        return D2.h.b(this.f262a, c0164b.f262a) && D2.h.b(this.f263b, c0164b.f263b) && D2.h.b(this.f264c, c0164b.f264c) && D2.h.b(this.f265d, c0164b.f265d) && D2.h.b(this.f266e, c0164b.f266e) && D2.h.b(this.f267f, c0164b.f267f);
    }

    public int hashCode() {
        return this.f269h;
    }

    public String toString() {
        return "BitmapMemoryCacheKey(sourceString=" + this.f262a + ", resizeOptions=" + this.f263b + ", rotationOptions=" + this.f264c + ", imageDecodeOptions=" + this.f265d + ", postprocessorCacheKey=" + this.f266e + ", postprocessorName=" + this.f267f + ")";
    }
}
