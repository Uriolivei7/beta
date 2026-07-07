package F;

import D2.h;
import F.a;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class d extends a {
    /* JADX WARN: Multi-variable type inference failed */
    public d() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    @Override // F.a
    public Object a(a.b bVar) {
        h.f(bVar, "key");
        return b().get(bVar);
    }

    public final void c(a.b bVar, Object obj) {
        h.f(bVar, "key");
        b().put(bVar, obj);
    }

    public d(a aVar) {
        h.f(aVar, "initialExtras");
        b().putAll(aVar.b());
    }

    public /* synthetic */ d(a aVar, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? a.C0003a.f211b : aVar);
    }
}
