package R0;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public final class r implements i {
    @Override // a0.f
    /* JADX INFO: renamed from: f, reason: merged with bridge method [inline-methods] */
    public Bitmap get(int i3) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(1, (int) Math.ceil(((double) i3) / 2.0d), Bitmap.Config.RGB_565);
        D2.h.e(bitmapCreateBitmap, "createBitmap(...)");
        return bitmapCreateBitmap;
    }

    @Override // a0.f, b0.InterfaceC0313h
    /* JADX INFO: renamed from: g, reason: merged with bridge method [inline-methods] */
    public void a(Bitmap bitmap) {
        D2.h.f(bitmap, "value");
        bitmap.recycle();
    }
}
