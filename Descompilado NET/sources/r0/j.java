package R0;

import android.graphics.Bitmap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public class j extends u {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f1966c = new a(null);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    @Override // R0.u, R0.A
    /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
    public Bitmap get(int i3) {
        Bitmap bitmap = (Bitmap) super.get(i3);
        if (bitmap == null || !g(bitmap)) {
            return null;
        }
        bitmap.eraseColor(0);
        return bitmap;
    }

    @Override // R0.A
    /* JADX INFO: renamed from: f, reason: merged with bridge method [inline-methods] */
    public int a(Bitmap bitmap) {
        D2.h.f(bitmap, "bitmap");
        return Z0.e.j(bitmap);
    }

    protected final boolean g(Bitmap bitmap) {
        if (bitmap == null) {
            return false;
        }
        if (bitmap.isRecycled()) {
            Y.a.N("BitmapPoolBackend", "Cannot reuse a recycled bitmap: %s", bitmap);
            return false;
        }
        if (bitmap.isMutable()) {
            return true;
        }
        Y.a.N("BitmapPoolBackend", "Cannot reuse an immutable bitmap: %s", bitmap);
        return false;
    }

    @Override // R0.u, R0.A
    /* JADX INFO: renamed from: h, reason: merged with bridge method [inline-methods] */
    public void c(Bitmap bitmap) {
        D2.h.f(bitmap, "bitmap");
        if (g(bitmap)) {
            super.c(bitmap);
        }
    }
}
