package com.facebook.imagepipeline.producers;

/* JADX INFO: loaded from: classes.dex */
public class o0 implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final e0 f6197a;

    class a extends AbstractC0360u {
        a(InterfaceC0354n interfaceC0354n) {
            super(interfaceC0354n);
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        protected void i(Object obj, int i3) {
            if (AbstractC0343c.e(i3)) {
                p().d(null, i3);
            }
        }
    }

    public o0(e0 e0Var) {
        this.f6197a = e0Var;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        this.f6197a.b(new a(interfaceC0354n), f0Var);
    }
}
