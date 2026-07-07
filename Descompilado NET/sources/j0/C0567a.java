package j0;

import D2.h;
import i0.C0559a;

/* JADX INFO: renamed from: j0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0567a implements InterfaceC0568b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0567a f9544a = new C0567a();

    private C0567a() {
    }

    @Override // j0.InterfaceC0568b
    public boolean a(C0559a c0559a) {
        h.f(c0559a, "tag");
        return false;
    }

    @Override // j0.InterfaceC0568b
    public void b(C0559a c0559a, String str, Object... objArr) {
        h.f(c0559a, "tag");
        h.f(str, "message");
        h.f(objArr, "args");
    }

    @Override // j0.InterfaceC0568b
    public void c(C0559a c0559a, String str) {
        h.f(c0559a, "tag");
        h.f(str, "message");
    }
}
