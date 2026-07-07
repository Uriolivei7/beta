package m0;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import t0.i;

/* JADX INFO: renamed from: m0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0598a implements N0.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Resources f9796a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final N0.a f9797b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final N0.a f9798c;

    public C0598a(Resources resources, N0.a aVar, N0.a aVar2) {
        this.f9796a = resources;
        this.f9797b = aVar;
        this.f9798c = aVar2;
    }

    private static boolean c(O0.e eVar) {
        return (eVar.s0() == 1 || eVar.s0() == 0) ? false : true;
    }

    private static boolean d(O0.e eVar) {
        return (eVar.N() == 0 || eVar.N() == -1) ? false : true;
    }

    @Override // N0.a
    public boolean a(O0.d dVar) {
        return true;
    }

    @Override // N0.a
    public Drawable b(O0.d dVar) {
        try {
            if (V0.b.d()) {
                V0.b.a("DefaultDrawableFactory#createDrawable");
            }
            if (dVar instanceof O0.e) {
                O0.e eVar = (O0.e) dVar;
                BitmapDrawable bitmapDrawable = new BitmapDrawable(this.f9796a, eVar.C());
                if (!d(eVar) && !c(eVar)) {
                    if (V0.b.d()) {
                        V0.b.b();
                    }
                    return bitmapDrawable;
                }
                i iVar = new i(bitmapDrawable, eVar.N(), eVar.s0());
                if (V0.b.d()) {
                    V0.b.b();
                }
                return iVar;
            }
            N0.a aVar = this.f9797b;
            if (aVar != null && aVar.a(dVar)) {
                Drawable drawableB = this.f9797b.b(dVar);
                if (V0.b.d()) {
                    V0.b.b();
                }
                return drawableB;
            }
            N0.a aVar2 = this.f9798c;
            if (aVar2 == null || !aVar2.a(dVar)) {
                if (!V0.b.d()) {
                    return null;
                }
                V0.b.b();
                return null;
            }
            Drawable drawableB2 = this.f9798c.b(dVar);
            if (V0.b.d()) {
                V0.b.b();
            }
            return drawableB2;
        } catch (Throwable th) {
            if (V0.b.d()) {
                V0.b.b();
            }
            throw th;
        }
    }

    public C0598a(Resources resources, N0.a aVar) {
        this(resources, aVar, null);
    }
}
