package androidx.fragment.app;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.E;
import androidx.lifecycle.InterfaceC0298f;

/* JADX INFO: loaded from: classes.dex */
class J implements InterfaceC0298f, G.d, androidx.lifecycle.H {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Fragment f5020b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final androidx.lifecycle.G f5021c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private E.b f5022d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private androidx.lifecycle.m f5023e = null;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private G.c f5024f = null;

    J(Fragment fragment, androidx.lifecycle.G g3) {
        this.f5020b = fragment;
        this.f5021c = g3;
    }

    @Override // G.d
    public androidx.savedstate.a b() {
        d();
        return this.f5024f.b();
    }

    void c(AbstractC0299g.a aVar) {
        this.f5023e.h(aVar);
    }

    void d() {
        if (this.f5023e == null) {
            this.f5023e = new androidx.lifecycle.m(this);
            G.c cVarA = G.c.a(this);
            this.f5024f = cVarA;
            cVarA.c();
            androidx.lifecycle.y.c(this);
        }
    }

    boolean e() {
        return this.f5023e != null;
    }

    void f(Bundle bundle) {
        this.f5024f.d(bundle);
    }

    void g(Bundle bundle) {
        this.f5024f.e(bundle);
    }

    void h(AbstractC0299g.b bVar) {
        this.f5023e.m(bVar);
    }

    @Override // androidx.lifecycle.InterfaceC0298f
    public E.b j() {
        Application application;
        E.b bVarJ = this.f5020b.j();
        if (!bVarJ.equals(this.f5020b.f4933W)) {
            this.f5022d = bVarJ;
            return bVarJ;
        }
        if (this.f5022d == null) {
            Context applicationContext = this.f5020b.m1().getApplicationContext();
            while (true) {
                if (!(applicationContext instanceof ContextWrapper)) {
                    application = null;
                    break;
                }
                if (applicationContext instanceof Application) {
                    application = (Application) applicationContext;
                    break;
                }
                applicationContext = ((ContextWrapper) applicationContext).getBaseContext();
            }
            this.f5022d = new androidx.lifecycle.B(application, this, this.f5020b.n());
        }
        return this.f5022d;
    }

    @Override // androidx.lifecycle.InterfaceC0298f
    public F.a k() {
        Application application;
        Context applicationContext = this.f5020b.m1().getApplicationContext();
        while (true) {
            if (!(applicationContext instanceof ContextWrapper)) {
                application = null;
                break;
            }
            if (applicationContext instanceof Application) {
                application = (Application) applicationContext;
                break;
            }
            applicationContext = ((ContextWrapper) applicationContext).getBaseContext();
        }
        F.d dVar = new F.d();
        if (application != null) {
            dVar.c(E.a.f5272h, application);
        }
        dVar.c(androidx.lifecycle.y.f5368a, this);
        dVar.c(androidx.lifecycle.y.f5369b, this);
        if (this.f5020b.n() != null) {
            dVar.c(androidx.lifecycle.y.f5370c, this.f5020b.n());
        }
        return dVar;
    }

    @Override // androidx.lifecycle.H
    public androidx.lifecycle.G s() {
        d();
        return this.f5021c;
    }

    @Override // androidx.lifecycle.l
    public AbstractC0299g t() {
        d();
        return this.f5023e;
    }
}
