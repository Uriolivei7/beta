package i1;

import D2.h;
import M2.InterfaceC0194e;
import M2.p;
import M2.z;

/* JADX INFO: renamed from: i1.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0560a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0560a f9524a = new C0560a();

    private C0560a() {
    }

    public static final void a(z zVar, Object obj) {
        h.f(zVar, "client");
        h.f(obj, "tag");
        p pVarC = zVar.c();
        for (InterfaceC0194e interfaceC0194e : pVarC.j()) {
            if (h.b(obj, interfaceC0194e.i().j())) {
                interfaceC0194e.cancel();
                return;
            }
        }
        for (InterfaceC0194e interfaceC0194e2 : pVarC.k()) {
            if (h.b(obj, interfaceC0194e2.i().j())) {
                interfaceC0194e2.cancel();
                return;
            }
        }
    }
}
