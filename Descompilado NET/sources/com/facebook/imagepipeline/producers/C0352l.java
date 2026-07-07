package com.facebook.imagepipeline.producers;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.l, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0352l implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final e0 f6173a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final e0 f6174b;

    /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.l$a */
    private class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private f0 f6175c;

        @Override // com.facebook.imagepipeline.producers.AbstractC0360u, com.facebook.imagepipeline.producers.AbstractC0343c
        protected void h(Throwable th) {
            C0352l.this.f6174b.b(p(), this.f6175c);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
        public void i(O0.j jVar, int i3) {
            U0.b bVarX = this.f6175c.X();
            boolean zE = AbstractC0343c.e(i3);
            boolean zC = w0.c(jVar, bVarX.r());
            if (jVar != null && (zC || bVarX.j())) {
                if (zE && zC) {
                    p().d(jVar, i3);
                } else {
                    p().d(jVar, AbstractC0343c.o(i3, 1));
                }
            }
            if (!zE || zC || bVarX.i()) {
                return;
            }
            O0.j.o(jVar);
            C0352l.this.f6174b.b(p(), this.f6175c);
        }

        private a(InterfaceC0354n interfaceC0354n, f0 f0Var) {
            super(interfaceC0354n);
            this.f6175c = f0Var;
        }
    }

    public C0352l(e0 e0Var, e0 e0Var2) {
        this.f6173a = e0Var;
        this.f6174b = e0Var2;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        this.f6173a.b(new a(interfaceC0354n, f0Var), f0Var);
    }
}
