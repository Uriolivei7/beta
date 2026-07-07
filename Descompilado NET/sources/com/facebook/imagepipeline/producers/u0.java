package com.facebook.imagepipeline.producers;

/* JADX INFO: loaded from: classes.dex */
public class u0 implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final v0[] f6255a;

    private class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final f0 f6256c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final int f6257d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final I0.g f6258e;

        public a(InterfaceC0354n interfaceC0354n, f0 f0Var, int i3) {
            super(interfaceC0354n);
            this.f6256c = f0Var;
            this.f6257d = i3;
            this.f6258e = f0Var.X().r();
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0360u, com.facebook.imagepipeline.producers.AbstractC0343c
        protected void h(Throwable th) {
            if (u0.this.e(this.f6257d + 1, p(), this.f6256c)) {
                return;
            }
            p().a(th);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
        public void i(O0.j jVar, int i3) {
            if (jVar != null && (AbstractC0343c.f(i3) || w0.c(jVar, this.f6258e))) {
                p().d(jVar, i3);
            } else if (AbstractC0343c.e(i3)) {
                O0.j.o(jVar);
                if (u0.this.e(this.f6257d + 1, p(), this.f6256c)) {
                    return;
                }
                p().d(null, 1);
            }
        }
    }

    public u0(v0... v0VarArr) {
        v0[] v0VarArr2 = (v0[]) X.k.g(v0VarArr);
        this.f6255a = v0VarArr2;
        X.k.e(0, v0VarArr2.length);
    }

    private int d(int i3, I0.g gVar) {
        while (true) {
            v0[] v0VarArr = this.f6255a;
            if (i3 >= v0VarArr.length) {
                return -1;
            }
            if (v0VarArr[i3].a(gVar)) {
                return i3;
            }
            i3++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean e(int i3, InterfaceC0354n interfaceC0354n, f0 f0Var) {
        int iD = d(i3, f0Var.X().r());
        if (iD == -1) {
            return false;
        }
        this.f6255a[iD].b(new a(interfaceC0354n, f0Var, iD), f0Var);
        return true;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        if (f0Var.X().r() == null) {
            interfaceC0354n.d(null, 1);
        } else {
            if (e(0, interfaceC0354n, f0Var)) {
                return;
            }
            interfaceC0354n.d(null, 1);
        }
    }
}
