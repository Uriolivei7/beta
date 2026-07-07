package K0;

import D2.h;
import Q0.d;
import X.k;
import com.facebook.imagepipeline.producers.AbstractC0343c;
import com.facebook.imagepipeline.producers.InterfaceC0354n;
import com.facebook.imagepipeline.producers.e0;
import com.facebook.imagepipeline.producers.f0;
import com.facebook.imagepipeline.producers.m0;
import h0.AbstractC0546a;
import java.util.Map;
import r2.r;

/* JADX INFO: loaded from: classes.dex */
public abstract class a extends AbstractC0546a {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final m0 f808h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final d f809i;

    /* JADX INFO: renamed from: K0.a$a, reason: collision with other inner class name */
    public static final class C0010a extends AbstractC0343c {
        C0010a() {
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        protected void g() {
            a.this.E();
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        protected void h(Throwable th) {
            h.f(th, "throwable");
            a.this.F(th);
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        protected void i(Object obj, int i3) {
            a aVar = a.this;
            aVar.G(obj, i3, aVar.D());
        }

        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        protected void j(float f3) {
            a.this.t(f3);
        }
    }

    protected a(e0 e0Var, m0 m0Var, d dVar) {
        h.f(e0Var, "producer");
        h.f(m0Var, "settableProducerContext");
        h.f(dVar, "requestListener");
        this.f808h = m0Var;
        this.f809i = dVar;
        if (!V0.b.d()) {
            p(m0Var.a());
            if (V0.b.d()) {
                V0.b.a("AbstractProducerToDataSourceAdapter()->onRequestStart");
                try {
                    dVar.c(m0Var);
                    r rVar = r.f10584a;
                } finally {
                }
            } else {
                dVar.c(m0Var);
            }
            if (!V0.b.d()) {
                e0Var.b(B(), m0Var);
                return;
            }
            V0.b.a("AbstractProducerToDataSourceAdapter()->produceResult");
            try {
                e0Var.b(B(), m0Var);
                r rVar2 = r.f10584a;
                return;
            } finally {
            }
        }
        V0.b.a("AbstractProducerToDataSourceAdapter()");
        try {
            p(m0Var.a());
            if (V0.b.d()) {
                V0.b.a("AbstractProducerToDataSourceAdapter()->onRequestStart");
                try {
                    dVar.c(m0Var);
                    r rVar3 = r.f10584a;
                    V0.b.b();
                } finally {
                }
            } else {
                dVar.c(m0Var);
            }
            if (V0.b.d()) {
                V0.b.a("AbstractProducerToDataSourceAdapter()->produceResult");
                try {
                    e0Var.b(B(), m0Var);
                    r rVar4 = r.f10584a;
                    V0.b.b();
                } finally {
                }
            } else {
                e0Var.b(B(), m0Var);
            }
            r rVar5 = r.f10584a;
        } catch (Throwable th) {
            throw th;
        }
    }

    private final InterfaceC0354n B() {
        return new C0010a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final synchronized void E() {
        k.i(l());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void F(Throwable th) {
        if (super.r(th, C(this.f808h))) {
            this.f809i.k(this.f808h, th);
        }
    }

    protected final Map C(f0 f0Var) {
        h.f(f0Var, "producerContext");
        return f0Var.a();
    }

    public final m0 D() {
        return this.f808h;
    }

    protected void G(Object obj, int i3, f0 f0Var) {
        h.f(f0Var, "producerContext");
        boolean zE = AbstractC0343c.e(i3);
        if (super.v(obj, zE, C(f0Var)) && zE) {
            this.f809i.h(this.f808h);
        }
    }

    @Override // h0.AbstractC0546a, h0.InterfaceC0548c
    public boolean close() {
        if (!super.close()) {
            return false;
        }
        if (super.e()) {
            return true;
        }
        this.f809i.a(this.f808h);
        this.f808h.j();
        return true;
    }
}
