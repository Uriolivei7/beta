package S0;

import R0.i;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(i iVar, q.e eVar, h hVar) {
        super(iVar, eVar, hVar);
        D2.h.f(iVar, "bitmapPool");
        D2.h.f(eVar, "decodeBuffers");
        D2.h.f(hVar, "platformDecoderOptions");
    }

    @Override // S0.c
    public int d(int i3, int i4, BitmapFactory.Options options) {
        D2.h.f(options, "options");
        Bitmap.Config config = options.inPreferredConfig;
        if (config != null) {
            return Z0.e.i(i3, i4, config);
        }
        throw new IllegalStateException("Required value was null.");
    }
}
