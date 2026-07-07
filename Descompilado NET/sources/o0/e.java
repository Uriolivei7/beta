package O0;

import android.graphics.Bitmap;
import b0.AbstractC0306a;
import b0.InterfaceC0313h;

/* JADX INFO: loaded from: classes.dex */
public interface e extends c {
    static e I(AbstractC0306a abstractC0306a, o oVar, int i3, int i4) {
        return b.w0() ? new b(abstractC0306a, oVar, i3, i4) : new h(abstractC0306a, oVar, i3, i4);
    }

    static e k0(Bitmap bitmap, InterfaceC0313h interfaceC0313h, o oVar, int i3) {
        return m0(bitmap, interfaceC0313h, oVar, i3, 0);
    }

    static e m0(Bitmap bitmap, InterfaceC0313h interfaceC0313h, o oVar, int i3, int i4) {
        return b.w0() ? new b(bitmap, interfaceC0313h, oVar, i3, i4) : new h(bitmap, interfaceC0313h, oVar, i3, i4);
    }

    int N();

    int s0();
}
