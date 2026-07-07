package o0;

import e0.InterfaceC0523b;
import z0.C0791j;

/* JADX INFO: renamed from: o0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0627a extends Q0.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final InterfaceC0523b f10154a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0791j f10155b;

    public C0627a(InterfaceC0523b interfaceC0523b, C0791j c0791j) {
        this.f10154a = interfaceC0523b;
        this.f10155b = c0791j;
    }

    @Override // Q0.a, Q0.e
    public void a(U0.b bVar, Object obj, String str, boolean z3) {
        this.f10155b.K(this.f10154a.now());
        this.f10155b.I(bVar);
        this.f10155b.y(obj);
        this.f10155b.P(str);
        this.f10155b.O(z3);
    }

    @Override // Q0.a, Q0.e
    public void b(U0.b bVar, String str, boolean z3) {
        this.f10155b.J(this.f10154a.now());
        this.f10155b.I(bVar);
        this.f10155b.P(str);
        this.f10155b.O(z3);
    }

    @Override // Q0.a, Q0.e
    public void i(String str) {
        this.f10155b.J(this.f10154a.now());
        this.f10155b.P(str);
    }

    @Override // Q0.a, Q0.e
    public void k(U0.b bVar, String str, Throwable th, boolean z3) {
        this.f10155b.J(this.f10154a.now());
        this.f10155b.I(bVar);
        this.f10155b.P(str);
        this.f10155b.O(z3);
    }
}
