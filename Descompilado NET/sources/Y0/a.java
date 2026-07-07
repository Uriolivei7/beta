package Y0;

import D2.h;
import O0.d;
import O0.f;
import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes.dex */
public final class a implements N0.a {
    @Override // N0.a
    public boolean a(d dVar) {
        h.f(dVar, "image");
        return dVar instanceof f;
    }

    @Override // N0.a
    public Drawable b(d dVar) {
        h.f(dVar, "image");
        f fVar = dVar instanceof f ? (f) dVar : null;
        if (fVar != null) {
            return fVar.f0();
        }
        return null;
    }
}
