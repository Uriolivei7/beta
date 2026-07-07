package T0;

import R.d;
import R.i;
import X.k;
import android.graphics.Bitmap;
import com.facebook.imagepipeline.nativecode.NativeBlurFilter;

/* JADX INFO: loaded from: classes.dex */
public class a extends U0.a {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f2342c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f2343d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private d f2344e;

    public a(int i3) {
        this(3, i3);
    }

    @Override // U0.a, U0.d
    public d b() {
        if (this.f2344e == null) {
            this.f2344e = new i(String.format(null, "i%dr%d", Integer.valueOf(this.f2342c), Integer.valueOf(this.f2343d)));
        }
        return this.f2344e;
    }

    @Override // U0.a
    public void d(Bitmap bitmap) {
        NativeBlurFilter.a(bitmap, this.f2342c, this.f2343d);
    }

    public a(int i3, int i4) {
        k.b(Boolean.valueOf(i3 > 0));
        k.b(Boolean.valueOf(i4 > 0));
        this.f2342c = i3;
        this.f2343d = i4;
    }
}
