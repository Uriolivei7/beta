package R0;

import android.graphics.Bitmap;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class s implements i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Set f1983a;

    public s() {
        Set setB = X.m.b();
        D2.h.e(setB, "newIdentityHashSet(...)");
        this.f1983a = setB;
    }

    @Override // a0.f
    /* JADX INFO: renamed from: f, reason: merged with bridge method [inline-methods] */
    public Bitmap get(int i3) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(1, (int) Math.ceil(((double) i3) / 2.0d), Bitmap.Config.RGB_565);
        D2.h.e(bitmapCreateBitmap, "createBitmap(...)");
        this.f1983a.add(bitmapCreateBitmap);
        return bitmapCreateBitmap;
    }

    @Override // a0.f, b0.InterfaceC0313h
    /* JADX INFO: renamed from: g, reason: merged with bridge method [inline-methods] */
    public void a(Bitmap bitmap) {
        D2.h.f(bitmap, "value");
        this.f1983a.remove(bitmap);
        bitmap.recycle();
    }
}
