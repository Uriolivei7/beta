package c2;

import android.content.Context;

/* JADX INFO: renamed from: c2.f, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0334f implements InterfaceC0337i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f5693a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0329a f5694b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f5695c;

    public C0334f(Context context, int i3) {
        this.f5693a = context;
        this.f5695c = i3;
        C0329a c0329a = new C0329a(5);
        this.f5694b = c0329a;
        c0329a.a(context.getApplicationInfo().sourceDir);
    }

    @Override // c2.InterfaceC0337i
    public InterfaceC0336h get() {
        return new C0333e(new C0335g(this.f5693a, this.f5694b), new C0330b(this.f5693a, this.f5694b), new C0340l(), new C0331c(this.f5693a), new C0338j(this.f5695c), new C0332d(), new C0339k(), new C0340l());
    }
}
