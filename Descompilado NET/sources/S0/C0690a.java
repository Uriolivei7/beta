package s0;

import D2.h;
import android.graphics.drawable.Animatable;
import q0.C0647c;

/* JADX INFO: renamed from: s0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0690a extends C0647c {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final InterfaceC0691b f10587c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f10588d = -1;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private long f10589e = -1;

    public C0690a(InterfaceC0691b interfaceC0691b) {
        this.f10587c = interfaceC0691b;
    }

    @Override // q0.C0647c, q0.InterfaceC0648d
    public void j(String str, Object obj) {
        h.f(str, "id");
        this.f10588d = System.currentTimeMillis();
    }

    @Override // q0.C0647c, q0.InterfaceC0648d
    public void k(String str, Object obj, Animatable animatable) {
        h.f(str, "id");
        long jCurrentTimeMillis = System.currentTimeMillis();
        this.f10589e = jCurrentTimeMillis;
        InterfaceC0691b interfaceC0691b = this.f10587c;
        if (interfaceC0691b != null) {
            interfaceC0691b.a(jCurrentTimeMillis - this.f10588d);
        }
    }
}
