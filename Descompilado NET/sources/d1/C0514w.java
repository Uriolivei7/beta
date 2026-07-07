package d1;

import a1.C0210a;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import com.facebook.react.bridge.Callback;
import d2.C0518a;
import r1.C0670b;

/* JADX INFO: renamed from: d1.w, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0514w {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Activity f9314a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f9315b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private B1.g f9316c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Callback f9317d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private C0517z f9318e;

    /* JADX INFO: renamed from: d1.w$a */
    class a extends C0517z {
        a(Activity activity, N n3, String str, Bundle bundle, boolean z3) {
            super(activity, n3, str, bundle, z3);
        }

        @Override // d1.C0517z
        protected a0 a() {
            a0 a0VarD = C0514w.this.d();
            return a0VarD == null ? super.a() : a0VarD;
        }
    }

    @Deprecated
    public C0514w(Activity activity, String str) {
        this.f9314a = activity;
        this.f9315b = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void m() {
        String strG = g();
        Bundle bundleC = c();
        if (Build.VERSION.SDK_INT >= 26 && l()) {
            this.f9314a.getWindow().setColorMode(1);
        }
        if (C0670b.c()) {
            this.f9318e = new C0517z(h(), i(), strG, bundleC);
        } else {
            this.f9318e = new a(h(), j(), strG, bundleC, k());
        }
        if (strG != null) {
            o(strG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void n(int i3, String[] strArr, int[] iArr, Object[] objArr) {
        B1.g gVar = this.f9316c;
        if (gVar == null || !gVar.onRequestPermissionsResult(i3, strArr, iArr)) {
            return;
        }
        this.f9316c = null;
    }

    public void A() {
        this.f9318e.m();
        Callback callback = this.f9317d;
        if (callback != null) {
            callback.invoke(new Object[0]);
            this.f9317d = null;
        }
    }

    public void B() {
        C0517z c0517z = this.f9318e;
        if (c0517z != null) {
            c0517z.q();
        }
    }

    public void C(boolean z3) {
        this.f9318e.r(z3);
    }

    public void D(String[] strArr, int i3, B1.g gVar) {
        this.f9316c = gVar;
        h().requestPermissions(strArr, i3);
    }

    protected Bundle c() {
        return f();
    }

    protected a0 d() {
        return null;
    }

    protected Context e() {
        return (Context) C0210a.c(this.f9314a);
    }

    protected Bundle f() {
        return null;
    }

    public String g() {
        return this.f9315b;
    }

    protected Activity h() {
        return (Activity) e();
    }

    public InterfaceC0491A i() {
        return ((InterfaceC0516y) h().getApplication()).b();
    }

    protected N j() {
        return ((InterfaceC0516y) h().getApplication()).a();
    }

    protected boolean k() {
        return C0670b.f();
    }

    protected boolean l() {
        return false;
    }

    protected void o(String str) {
        this.f9318e.g(str);
        h().setContentView(this.f9318e.e());
    }

    public void p(int i3, int i4, Intent intent) {
        this.f9318e.h(i3, i4, intent, true);
    }

    public boolean q() {
        return this.f9318e.i();
    }

    public void r(Configuration configuration) {
        this.f9318e.j(configuration);
    }

    public void s(Bundle bundle) {
        C0518a.o(0L, "ReactActivityDelegate.onCreate::init", new Runnable() { // from class: d1.u
            @Override // java.lang.Runnable
            public final void run() {
                this.f9309b.m();
            }
        });
    }

    public void t() {
        this.f9318e.k();
    }

    public boolean u(int i3, KeyEvent keyEvent) {
        return this.f9318e.n(i3, keyEvent);
    }

    public boolean v(int i3, KeyEvent keyEvent) {
        return this.f9318e.o(i3);
    }

    public boolean w(int i3, KeyEvent keyEvent) {
        return this.f9318e.s(i3, keyEvent);
    }

    public boolean x(Intent intent) {
        return this.f9318e.p(intent);
    }

    public void y() {
        this.f9318e.l();
    }

    public void z(final int i3, final String[] strArr, final int[] iArr) {
        this.f9317d = new Callback() { // from class: d1.v
            @Override // com.facebook.react.bridge.Callback
            public final void invoke(Object[] objArr) {
                this.f9310b.n(i3, strArr, iArr, objArr);
            }
        };
    }

    public C0514w(AbstractActivityC0510s abstractActivityC0510s, String str) {
        this.f9314a = abstractActivityC0510s;
        this.f9315b = str;
    }
}
