package d1;

import a1.C0210a;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import com.facebook.react.devsupport.k0;
import p1.InterfaceC0636a;
import r1.C0670b;

/* JADX INFO: renamed from: d1.z, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0517z {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Activity f9323a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private a0 f9324b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f9325c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Bundle f9326d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private com.facebook.react.devsupport.K f9327e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private N f9328f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private InterfaceC0491A f9329g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private InterfaceC0636a f9330h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f9331i;

    @Deprecated
    public C0517z(Activity activity, N n3, String str, Bundle bundle) {
        this.f9331i = C0670b.f();
        this.f9323a = activity;
        this.f9325c = str;
        this.f9326d = bundle;
        this.f9327e = new com.facebook.react.devsupport.K();
        this.f9328f = n3;
    }

    private k1.e b() {
        InterfaceC0491A interfaceC0491A;
        if (C0670b.c() && (interfaceC0491A = this.f9329g) != null && interfaceC0491A.b() != null) {
            return this.f9329g.b();
        }
        if (!d().v() || d().o() == null) {
            return null;
        }
        return d().o().D();
    }

    private N d() {
        return this.f9328f;
    }

    protected a0 a() {
        a0 a0Var = new a0(this.f9323a);
        a0Var.setIsFabric(f());
        return a0Var;
    }

    public J c() {
        return d().o();
    }

    public a0 e() {
        if (!C0670b.c()) {
            return this.f9324b;
        }
        InterfaceC0636a interfaceC0636a = this.f9330h;
        if (interfaceC0636a != null) {
            return (a0) interfaceC0636a.a();
        }
        return null;
    }

    protected boolean f() {
        return this.f9331i;
    }

    public void g(String str) {
        if (C0670b.c()) {
            if (this.f9330h == null) {
                this.f9330h = this.f9329g.a(this.f9323a, str, this.f9326d);
            }
            this.f9330h.start();
        } else {
            if (this.f9324b != null) {
                throw new IllegalStateException("Cannot loadApp while app is already running.");
            }
            a0 a0VarA = a();
            this.f9324b = a0VarA;
            a0VarA.u(d().o(), str, this.f9326d);
        }
    }

    public void h(int i3, int i4, Intent intent, boolean z3) {
        if (C0670b.c()) {
            this.f9329g.onActivityResult(this.f9323a, i3, i4, intent);
        } else if (d().v() && z3) {
            d().o().W(this.f9323a, i3, i4, intent);
        }
    }

    public boolean i() {
        if (C0670b.c()) {
            this.f9329g.g();
            return true;
        }
        if (!d().v()) {
            return false;
        }
        d().o().X();
        return true;
    }

    public void j(Configuration configuration) {
        if (C0670b.c()) {
            this.f9329g.c((Context) C0210a.c(this.f9323a));
        } else if (d().v()) {
            c().Y((Context) C0210a.c(this.f9323a), configuration);
        }
    }

    public void k() {
        t();
        if (C0670b.c()) {
            this.f9329g.h(this.f9323a);
        } else if (d().v()) {
            d().o().a0(this.f9323a);
        }
    }

    public void l() {
        if (C0670b.c()) {
            this.f9329g.f(this.f9323a);
        } else if (d().v()) {
            d().o().c0(this.f9323a);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void m() {
        if (!(this.f9323a instanceof B1.a)) {
            throw new ClassCastException("Host Activity does not implement DefaultHardwareBackBtnHandler");
        }
        if (C0670b.c()) {
            InterfaceC0491A interfaceC0491A = this.f9329g;
            Activity activity = this.f9323a;
            interfaceC0491A.e(activity, (B1.a) activity);
        } else if (d().v()) {
            J jO = d().o();
            Activity activity2 = this.f9323a;
            jO.e0(activity2, (B1.a) activity2);
        }
    }

    public boolean n(int i3, KeyEvent keyEvent) {
        InterfaceC0491A interfaceC0491A;
        if (i3 != 90) {
            return false;
        }
        if ((!C0670b.c() || (interfaceC0491A = this.f9329g) == null || interfaceC0491A.b() == null) && !(d().v() && d().u())) {
            return false;
        }
        keyEvent.startTracking();
        return true;
    }

    public boolean o(int i3) {
        InterfaceC0491A interfaceC0491A;
        if (i3 != 90) {
            return false;
        }
        if (!C0670b.c() || (interfaceC0491A = this.f9329g) == null) {
            if (!d().v() || !d().u()) {
                return false;
            }
            d().o().r0();
            return true;
        }
        k1.e eVarB = interfaceC0491A.b();
        if (eVarB == null || (eVarB instanceof k0)) {
            return false;
        }
        eVarB.x();
        return true;
    }

    public boolean p(Intent intent) {
        if (C0670b.c()) {
            this.f9329g.onNewIntent(intent);
            return true;
        }
        if (!d().v()) {
            return false;
        }
        d().o().g0(intent);
        return true;
    }

    public void q() {
        if (C0670b.c()) {
            this.f9329g.d(this.f9323a);
        } else if (d().v()) {
            d().o().h0(this.f9323a);
        }
    }

    public void r(boolean z3) {
        if (C0670b.c()) {
            this.f9329g.onWindowFocusChange(z3);
        } else if (d().v()) {
            d().o().i0(z3);
        }
    }

    public boolean s(int i3, KeyEvent keyEvent) {
        k1.e eVarB = b();
        if (eVarB != null && !(eVarB instanceof k0)) {
            if (i3 == 82) {
                eVarB.x();
                return true;
            }
            if (((com.facebook.react.devsupport.K) C0210a.c(this.f9327e)).b(i3, this.f9323a.getCurrentFocus())) {
                eVarB.s();
                return true;
            }
        }
        return false;
    }

    public void t() {
        if (C0670b.c()) {
            InterfaceC0636a interfaceC0636a = this.f9330h;
            if (interfaceC0636a != null) {
                interfaceC0636a.stop();
                this.f9330h = null;
                return;
            }
            return;
        }
        a0 a0Var = this.f9324b;
        if (a0Var != null) {
            a0Var.v();
            this.f9324b = null;
        }
    }

    public C0517z(Activity activity, InterfaceC0491A interfaceC0491A, String str, Bundle bundle) {
        this.f9331i = C0670b.f();
        this.f9323a = activity;
        this.f9325c = str;
        this.f9326d = bundle;
        this.f9327e = new com.facebook.react.devsupport.K();
        this.f9329g = interfaceC0491A;
    }

    public C0517z(Activity activity, N n3, String str, Bundle bundle, boolean z3) {
        C0670b.f();
        this.f9331i = z3;
        this.f9323a = activity;
        this.f9325c = str;
        this.f9326d = bundle;
        this.f9327e = new com.facebook.react.devsupport.K();
        this.f9328f = n3;
    }
}
