package com.facebook.imagepipeline.producers;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.u, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0360u extends AbstractC0343c {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final InterfaceC0354n f6254b;

    public AbstractC0360u(InterfaceC0354n interfaceC0354n) {
        D2.h.f(interfaceC0354n, "consumer");
        this.f6254b = interfaceC0354n;
    }

    @Override // com.facebook.imagepipeline.producers.AbstractC0343c
    protected void g() {
        this.f6254b.b();
    }

    @Override // com.facebook.imagepipeline.producers.AbstractC0343c
    protected void h(Throwable th) {
        D2.h.f(th, "t");
        this.f6254b.a(th);
    }

    @Override // com.facebook.imagepipeline.producers.AbstractC0343c
    protected void j(float f3) {
        this.f6254b.c(f3);
    }

    public final InterfaceC0354n p() {
        return this.f6254b;
    }
}
