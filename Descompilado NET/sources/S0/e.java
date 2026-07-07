package S0;

import R0.i;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/* JADX INFO: loaded from: classes.dex */
public final class e extends c {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final h f2308h;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public e(i iVar, q.e eVar, h hVar) {
        super(iVar, eVar, hVar);
        D2.h.f(iVar, "bitmapPool");
        D2.h.f(eVar, "decodeBuffers");
        D2.h.f(hVar, "platformDecoderOptions");
        this.f2308h = hVar;
    }

    @Override // S0.c
    public int d(int i3, int i4, BitmapFactory.Options options) {
        D2.h.f(options, "options");
        Bitmap.Config config = options.outConfig;
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        return Z0.e.i(i3, i4, config);
    }
}
