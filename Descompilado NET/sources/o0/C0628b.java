package o0;

import O0.l;
import e0.InterfaceC0523b;
import java.io.Closeable;
import t0.G;
import z0.C0782a;
import z0.C0791j;
import z0.EnumC0786e;
import z0.InterfaceC0783b;
import z0.InterfaceC0790i;
import z0.n;

/* JADX INFO: renamed from: o0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0628b extends C0782a implements Closeable, G {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final InterfaceC0523b f10156d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final C0791j f10157e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final InterfaceC0790i f10158f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private InterfaceC0790i f10159g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final boolean f10160h;

    public C0628b(InterfaceC0523b interfaceC0523b, C0791j c0791j, InterfaceC0790i interfaceC0790i) {
        this(interfaceC0523b, c0791j, interfaceC0790i, true);
    }

    private void P(C0791j c0791j, long j3) {
        c0791j.R(false);
        c0791j.L(j3);
        d0(c0791j, n.f11155g);
    }

    private void c0(C0791j c0791j, EnumC0786e enumC0786e) {
        c0791j.H(enumC0786e);
        this.f10158f.a(c0791j, enumC0786e);
        InterfaceC0790i interfaceC0790i = this.f10159g;
        if (interfaceC0790i != null) {
            interfaceC0790i.a(c0791j, enumC0786e);
        }
    }

    private void d0(C0791j c0791j, n nVar) {
        this.f10158f.b(c0791j, nVar);
        InterfaceC0790i interfaceC0790i = this.f10159g;
        if (interfaceC0790i != null) {
            interfaceC0790i.b(c0791j, nVar);
        }
    }

    @Override // z0.C0782a, z0.InterfaceC0783b
    /* JADX INFO: renamed from: A, reason: merged with bridge method [inline-methods] */
    public void z(String str, l lVar, InterfaceC0783b.a aVar) {
        long jNow = this.f10156d.now();
        C0791j c0791j = this.f10157e;
        c0791j.F(aVar);
        c0791j.A(jNow);
        c0791j.J(jNow);
        c0791j.B(str);
        c0791j.G(lVar);
        c0(c0791j, EnumC0786e.f11057h);
    }

    @Override // z0.C0782a, z0.InterfaceC0783b
    /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
    public void a(String str, l lVar) {
        long jNow = this.f10156d.now();
        C0791j c0791j = this.f10157e;
        c0791j.C(jNow);
        c0791j.B(str);
        c0791j.G(lVar);
        c0(c0791j, EnumC0786e.f11056g);
    }

    public void X(C0791j c0791j, long j3) {
        c0791j.R(true);
        c0791j.Q(j3);
        d0(c0791j, n.f11154f);
    }

    public void a0() {
        this.f10157e.w();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        a0();
    }

    @Override // t0.G
    public void i(boolean z3) {
        if (z3) {
            X(this.f10157e, this.f10156d.now());
        } else {
            P(this.f10157e, this.f10156d.now());
        }
    }

    @Override // z0.C0782a, z0.InterfaceC0783b
    public void o(String str, Object obj, InterfaceC0783b.a aVar) {
        long jNow = this.f10156d.now();
        C0791j c0791j = this.f10157e;
        c0791j.x();
        c0791j.D(jNow);
        c0791j.B(str);
        c0791j.y(obj);
        c0791j.F(aVar);
        c0(c0791j, EnumC0786e.f11055f);
        if (this.f10160h) {
            X(c0791j, jNow);
        }
    }

    @Override // z0.C0782a, z0.InterfaceC0783b
    public void v(String str, InterfaceC0783b.a aVar) {
        long jNow = this.f10156d.now();
        C0791j c0791j = this.f10157e;
        c0791j.F(aVar);
        c0791j.B(str);
        c0(c0791j, EnumC0786e.f11060k);
        if (this.f10160h) {
            P(c0791j, jNow);
        }
    }

    @Override // z0.C0782a, z0.InterfaceC0783b
    public void y(String str, Throwable th, InterfaceC0783b.a aVar) {
        long jNow = this.f10156d.now();
        C0791j c0791j = this.f10157e;
        c0791j.F(aVar);
        c0791j.z(jNow);
        c0791j.B(str);
        c0791j.E(th);
        c0(c0791j, EnumC0786e.f11058i);
        P(c0791j, jNow);
    }

    public C0628b(InterfaceC0523b interfaceC0523b, C0791j c0791j, InterfaceC0790i interfaceC0790i, boolean z3) {
        this.f10159g = null;
        this.f10156d = interfaceC0523b;
        this.f10157e = c0791j;
        this.f10158f = interfaceC0790i;
        this.f10160h = z3;
    }

    @Override // t0.G
    public void onDraw() {
    }
}
