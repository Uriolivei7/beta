package F;

import D2.h;
import androidx.lifecycle.D;
import androidx.lifecycle.E;

/* JADX INFO: loaded from: classes.dex */
public final class b implements E.b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final f[] f212b;

    public b(f... fVarArr) {
        h.f(fVarArr, "initializers");
        this.f212b = fVarArr;
    }

    @Override // androidx.lifecycle.E.b
    public D b(Class cls, a aVar) {
        h.f(cls, "modelClass");
        h.f(aVar, "extras");
        D d4 = null;
        for (f fVar : this.f212b) {
            if (h.b(fVar.a(), cls)) {
                Object objD = fVar.b().d(aVar);
                d4 = objD instanceof D ? (D) objD : null;
            }
        }
        if (d4 != null) {
            return d4;
        }
        throw new IllegalArgumentException("No initializer set for given class " + cls.getName());
    }
}
