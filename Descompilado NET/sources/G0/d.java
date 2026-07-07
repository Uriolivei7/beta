package G0;

import android.graphics.Bitmap;
import b0.InterfaceC0313h;

/* JADX INFO: loaded from: classes.dex */
public class d implements InterfaceC0313h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static d f256a;

    private d() {
    }

    public static d b() {
        if (f256a == null) {
            f256a = new d();
        }
        return f256a;
    }

    @Override // b0.InterfaceC0313h
    /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
    public void a(Bitmap bitmap) {
        bitmap.recycle();
    }
}
