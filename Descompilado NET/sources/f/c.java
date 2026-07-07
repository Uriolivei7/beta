package F;

import C2.l;
import D2.h;
import androidx.lifecycle.E;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final List f213a = new ArrayList();

    public final void a(I2.b bVar, l lVar) {
        h.f(bVar, "clazz");
        h.f(lVar, "initializer");
        this.f213a.add(new f(B2.a.a(bVar), lVar));
    }

    public final E.b b() {
        f[] fVarArr = (f[]) this.f213a.toArray(new f[0]);
        return new b((f[]) Arrays.copyOf(fVarArr, fVarArr.length));
    }
}
