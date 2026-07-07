package com.facebook.imagepipeline.producers;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0341a implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final e0 f6103a;

    /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.a$a, reason: collision with other inner class name */
    private static class C0094a extends AbstractC0360u {
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
        public void i(O0.j jVar, int i3) {
            if (jVar == null) {
                p().d(null, i3);
                return;
            }
            if (!O0.j.u0(jVar)) {
                jVar.x0();
            }
            p().d(jVar, i3);
        }

        private C0094a(InterfaceC0354n interfaceC0354n) {
            super(interfaceC0354n);
        }
    }

    public C0341a(e0 e0Var) {
        this.f6103a = e0Var;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        this.f6103a.b(new C0094a(interfaceC0354n), f0Var);
    }
}
