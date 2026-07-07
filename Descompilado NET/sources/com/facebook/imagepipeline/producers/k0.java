package com.facebook.imagepipeline.producers;

import b0.AbstractC0306a;

/* JADX INFO: loaded from: classes.dex */
public final class k0 implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final e0 f6171a;

    private final class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ k0 f6172c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(k0 k0Var, InterfaceC0354n interfaceC0354n) {
            super(interfaceC0354n);
            D2.h.f(interfaceC0354n, "consumer");
            this.f6172c = k0Var;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
        public void i(O0.j jVar, int i3) {
            AbstractC0306a abstractC0306aV = null;
            try {
                if (O0.j.w0(jVar) && jVar != null) {
                    abstractC0306aV = jVar.v();
                }
                p().d(abstractC0306aV, i3);
                AbstractC0306a.D(abstractC0306aV);
            } catch (Throwable th) {
                AbstractC0306a.D(abstractC0306aV);
                throw th;
            }
        }
    }

    public k0(e0 e0Var) {
        D2.h.f(e0Var, "inputProducer");
        this.f6171a = e0Var;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        D2.h.f(interfaceC0354n, "consumer");
        D2.h.f(f0Var, "context");
        this.f6171a.b(new a(this, interfaceC0354n), f0Var);
    }
}
