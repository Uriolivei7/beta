package O0;

import android.graphics.Bitmap;
import b0.AbstractC0306a;
import b0.InterfaceC0313h;

/* JADX INFO: loaded from: classes.dex */
class h extends b {
    protected h(AbstractC0306a abstractC0306a, o oVar, int i3, int i4) {
        super(abstractC0306a, oVar, i3, i4);
    }

    protected void finalize() throws Throwable {
        if (b()) {
            return;
        }
        Y.a.K("DefaultCloseableStaticBitmap", "finalize: %s %x still open.", getClass().getSimpleName(), Integer.valueOf(System.identityHashCode(this)));
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    protected h(Bitmap bitmap, InterfaceC0313h interfaceC0313h, o oVar, int i3, int i4) {
        super(bitmap, interfaceC0313h, oVar, i3, i4);
    }
}
