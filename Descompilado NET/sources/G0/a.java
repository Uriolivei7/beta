package G0;

import D2.h;
import J0.C0167a;
import R0.i;
import Z0.e;
import android.graphics.Bitmap;
import b0.AbstractC0306a;

/* JADX INFO: loaded from: classes.dex */
public final class a extends b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final i f253a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0167a f254b;

    public a(i iVar, C0167a c0167a) {
        h.f(iVar, "bitmapPool");
        h.f(c0167a, "closeableReferenceFactory");
        this.f253a = iVar;
        this.f254b = c0167a;
    }

    @Override // G0.b
    public AbstractC0306a d(int i3, int i4, Bitmap.Config config) {
        h.f(config, "bitmapConfig");
        Bitmap bitmap = (Bitmap) this.f253a.get(e.i(i3, i4, config));
        if (bitmap.getAllocationByteCount() < i3 * i4 * e.h(config)) {
            throw new IllegalStateException("Check failed.");
        }
        bitmap.reconfigure(i3, i4, config);
        AbstractC0306a abstractC0306aC = this.f254b.c(bitmap, this.f253a);
        h.e(abstractC0306aC, "create(...)");
        return abstractC0306aC;
    }
}
