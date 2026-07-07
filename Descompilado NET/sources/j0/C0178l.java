package J0;

import S.g;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* JADX INFO: renamed from: J0.l, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0178l implements InterfaceC0183q {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private InterfaceC0179m f597a;

    public C0178l(InterfaceC0179m interfaceC0179m) {
        this.f597a = interfaceC0179m;
    }

    private static S.g b(S.d dVar, S.f fVar) {
        return c(dVar, fVar, Executors.newSingleThreadExecutor());
    }

    private static S.g c(S.d dVar, S.f fVar, Executor executor) {
        return new S.g(fVar, dVar.h(), new g.c(dVar.k(), dVar.j(), dVar.f()), dVar.e(), dVar.d(), dVar.g(), executor, dVar.i());
    }

    @Override // J0.InterfaceC0183q
    public S.k a(S.d dVar) {
        return b(dVar, this.f597a.a(dVar));
    }
}
