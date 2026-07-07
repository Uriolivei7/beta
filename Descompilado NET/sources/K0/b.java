package K0;

import Q0.d;
import b0.AbstractC0306a;
import com.facebook.imagepipeline.producers.e0;
import com.facebook.imagepipeline.producers.f0;
import com.facebook.imagepipeline.producers.m0;
import h0.InterfaceC0548c;

/* JADX INFO: loaded from: classes.dex */
public class b extends a {
    private b(e0 e0Var, m0 m0Var, d dVar) {
        super(e0Var, m0Var, dVar);
    }

    public static InterfaceC0548c I(e0 e0Var, m0 m0Var, d dVar) {
        if (V0.b.d()) {
            V0.b.a("CloseableProducerToDataSourceAdapter#create");
        }
        b bVar = new b(e0Var, m0Var, dVar);
        if (V0.b.d()) {
            V0.b.b();
        }
        return bVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // h0.AbstractC0546a
    /* JADX INFO: renamed from: H, reason: merged with bridge method [inline-methods] */
    public void i(AbstractC0306a abstractC0306a) {
        AbstractC0306a.D(abstractC0306a);
    }

    @Override // h0.AbstractC0546a, h0.InterfaceC0548c
    /* JADX INFO: renamed from: J, reason: merged with bridge method [inline-methods] */
    public AbstractC0306a b() {
        return AbstractC0306a.A((AbstractC0306a) super.b());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // K0.a
    /* JADX INFO: renamed from: K, reason: merged with bridge method [inline-methods] */
    public void G(AbstractC0306a abstractC0306a, int i3, f0 f0Var) {
        super.G(AbstractC0306a.A(abstractC0306a), i3, f0Var);
    }
}
