package Z0;

import android.graphics.ColorSpace;
import r2.C0686i;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ColorSpace f2807a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0686i f2808b;

    public g(int i3, int i4, ColorSpace colorSpace) {
        this.f2807a = colorSpace;
        this.f2808b = (i3 == -1 || i4 == -1) ? null : new C0686i(Integer.valueOf(i3), Integer.valueOf(i4));
    }

    public final ColorSpace a() {
        return this.f2807a;
    }

    public final C0686i b() {
        return this.f2808b;
    }
}
