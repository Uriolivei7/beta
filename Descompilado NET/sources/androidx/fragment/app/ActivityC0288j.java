package androidx.fragment.app;

import a.InterfaceC0206b;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedDispatcher;
import androidx.core.view.InterfaceC0278z;
import androidx.lifecycle.AbstractC0299g;
import androidx.savedstate.a;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import q.InterfaceC0643a;

/* JADX INFO: renamed from: androidx.fragment.app.j, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class ActivityC0288j extends ComponentActivity {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    boolean f5148A;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    final C0292n f5149w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    final androidx.lifecycle.m f5150x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    boolean f5151y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    boolean f5152z;

    /* JADX INFO: renamed from: androidx.fragment.app.j$a */
    class a extends p implements androidx.core.content.c, androidx.core.content.d, androidx.core.app.j, androidx.core.app.k, androidx.lifecycle.H, androidx.activity.o, androidx.activity.result.f, G.d, B, InterfaceC0278z {
        public a() {
            super(ActivityC0288j.this);
        }

        public void A() {
            ActivityC0288j.this.invalidateOptionsMenu();
        }

        @Override // androidx.fragment.app.p
        /* JADX INFO: renamed from: B, reason: merged with bridge method [inline-methods] */
        public ActivityC0288j w() {
            return ActivityC0288j.this;
        }

        @Override // androidx.activity.o
        public OnBackPressedDispatcher a() {
            return ActivityC0288j.this.a();
        }

        @Override // G.d
        public androidx.savedstate.a b() {
            return ActivityC0288j.this.b();
        }

        @Override // androidx.fragment.app.B
        public void c(x xVar, Fragment fragment) {
            ActivityC0288j.this.b0(fragment);
        }

        @Override // androidx.core.view.InterfaceC0278z
        public void d(androidx.core.view.C c4) {
            ActivityC0288j.this.d(c4);
        }

        @Override // androidx.fragment.app.p, androidx.fragment.app.AbstractC0290l
        public View f(int i3) {
            return ActivityC0288j.this.findViewById(i3);
        }

        @Override // androidx.core.app.j
        public void g(InterfaceC0643a interfaceC0643a) {
            ActivityC0288j.this.g(interfaceC0643a);
        }

        @Override // androidx.fragment.app.p, androidx.fragment.app.AbstractC0290l
        public boolean h() {
            Window window = ActivityC0288j.this.getWindow();
            return (window == null || window.peekDecorView() == null) ? false : true;
        }

        @Override // androidx.core.content.c
        public void i(InterfaceC0643a interfaceC0643a) {
            ActivityC0288j.this.i(interfaceC0643a);
        }

        @Override // androidx.core.app.k
        public void l(InterfaceC0643a interfaceC0643a) {
            ActivityC0288j.this.l(interfaceC0643a);
        }

        @Override // androidx.core.view.InterfaceC0278z
        public void n(androidx.core.view.C c4) {
            ActivityC0288j.this.n(c4);
        }

        @Override // androidx.activity.result.f
        public androidx.activity.result.e o() {
            return ActivityC0288j.this.o();
        }

        @Override // androidx.fragment.app.p
        public void p(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            ActivityC0288j.this.dump(str, fileDescriptor, printWriter, strArr);
        }

        @Override // androidx.core.app.j
        public void q(InterfaceC0643a interfaceC0643a) {
            ActivityC0288j.this.q(interfaceC0643a);
        }

        @Override // androidx.core.content.c
        public void r(InterfaceC0643a interfaceC0643a) {
            ActivityC0288j.this.r(interfaceC0643a);
        }

        @Override // androidx.lifecycle.H
        public androidx.lifecycle.G s() {
            return ActivityC0288j.this.s();
        }

        @Override // androidx.lifecycle.l
        public AbstractC0299g t() {
            return ActivityC0288j.this.f5150x;
        }

        @Override // androidx.core.app.k
        public void u(InterfaceC0643a interfaceC0643a) {
            ActivityC0288j.this.u(interfaceC0643a);
        }

        @Override // androidx.core.content.d
        public void v(InterfaceC0643a interfaceC0643a) {
            ActivityC0288j.this.v(interfaceC0643a);
        }

        @Override // androidx.core.content.d
        public void x(InterfaceC0643a interfaceC0643a) {
            ActivityC0288j.this.x(interfaceC0643a);
        }

        @Override // androidx.fragment.app.p
        public LayoutInflater y() {
            return ActivityC0288j.this.getLayoutInflater().cloneInContext(ActivityC0288j.this);
        }

        @Override // androidx.fragment.app.p
        public void z() {
            A();
        }
    }

    public ActivityC0288j() {
        this.f5149w = C0292n.b(new a());
        this.f5150x = new androidx.lifecycle.m(this);
        this.f5148A = true;
        U();
    }

    private void U() {
        b().h("android:support:lifecycle", new a.c() { // from class: androidx.fragment.app.f
            @Override // androidx.savedstate.a.c
            public final Bundle a() {
                return this.f5144a.V();
            }
        });
        r(new InterfaceC0643a() { // from class: androidx.fragment.app.g
            @Override // q.InterfaceC0643a
            public final void a(Object obj) {
                this.f5145a.W((Configuration) obj);
            }
        });
        F(new InterfaceC0643a() { // from class: androidx.fragment.app.h
            @Override // q.InterfaceC0643a
            public final void a(Object obj) {
                this.f5146a.X((Intent) obj);
            }
        });
        E(new InterfaceC0206b() { // from class: androidx.fragment.app.i
            @Override // a.InterfaceC0206b
            public final void a(Context context) {
                this.f5147a.Y(context);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Bundle V() {
        Z();
        this.f5150x.h(AbstractC0299g.a.ON_STOP);
        return new Bundle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void W(Configuration configuration) {
        this.f5149w.m();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void X(Intent intent) {
        this.f5149w.m();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void Y(Context context) {
        this.f5149w.a(null);
    }

    private static boolean a0(x xVar, AbstractC0299g.b bVar) {
        boolean zA0 = false;
        for (Fragment fragment : xVar.s0()) {
            if (fragment != null) {
                if (fragment.z() != null) {
                    zA0 |= a0(fragment.o(), bVar);
                }
                J j3 = fragment.f4931U;
                if (j3 != null && j3.t().b().b(AbstractC0299g.b.STARTED)) {
                    fragment.f4931U.h(bVar);
                    zA0 = true;
                }
                if (fragment.f4930T.b().b(AbstractC0299g.b.STARTED)) {
                    fragment.f4930T.m(bVar);
                    zA0 = true;
                }
            }
        }
        return zA0;
    }

    final View S(View view, String str, Context context, AttributeSet attributeSet) {
        return this.f5149w.n(view, str, context, attributeSet);
    }

    public x T() {
        return this.f5149w.l();
    }

    void Z() {
        while (a0(T(), AbstractC0299g.b.CREATED)) {
        }
    }

    public void b0(Fragment fragment) {
    }

    protected void c0() {
        this.f5150x.h(AbstractC0299g.a.ON_RESUME);
        this.f5149w.h();
    }

    @Override // android.app.Activity
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        if (y(strArr)) {
            printWriter.print(str);
            printWriter.print("Local FragmentActivity ");
            printWriter.print(Integer.toHexString(System.identityHashCode(this)));
            printWriter.println(" State:");
            String str2 = str + "  ";
            printWriter.print(str2);
            printWriter.print("mCreated=");
            printWriter.print(this.f5151y);
            printWriter.print(" mResumed=");
            printWriter.print(this.f5152z);
            printWriter.print(" mStopped=");
            printWriter.print(this.f5148A);
            if (getApplication() != null) {
                androidx.loader.app.a.b(this).a(str2, fileDescriptor, printWriter, strArr);
            }
            this.f5149w.l().W(str, fileDescriptor, printWriter, strArr);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i3, int i4, Intent intent) {
        this.f5149w.m();
        super.onActivityResult(i3, i4, intent);
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.f, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f5150x.h(AbstractC0299g.a.ON_CREATE);
        this.f5149w.e();
    }

    @Override // android.app.Activity, android.view.LayoutInflater.Factory2
    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        View viewS = S(view, str, context, attributeSet);
        return viewS == null ? super.onCreateView(view, str, context, attributeSet) : viewS;
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.f5149w.f();
        this.f5150x.h(AbstractC0299g.a.ON_DESTROY);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public boolean onMenuItemSelected(int i3, MenuItem menuItem) {
        if (super.onMenuItemSelected(i3, menuItem)) {
            return true;
        }
        if (i3 == 6) {
            return this.f5149w.d(menuItem);
        }
        return false;
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.f5152z = false;
        this.f5149w.g();
        this.f5150x.h(AbstractC0299g.a.ON_PAUSE);
    }

    @Override // android.app.Activity
    protected void onPostResume() {
        super.onPostResume();
        c0();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i3, String[] strArr, int[] iArr) {
        this.f5149w.m();
        super.onRequestPermissionsResult(i3, strArr, iArr);
    }

    @Override // android.app.Activity
    protected void onResume() {
        this.f5149w.m();
        super.onResume();
        this.f5152z = true;
        this.f5149w.k();
    }

    @Override // android.app.Activity
    protected void onStart() {
        this.f5149w.m();
        super.onStart();
        this.f5148A = false;
        if (!this.f5151y) {
            this.f5151y = true;
            this.f5149w.c();
        }
        this.f5149w.k();
        this.f5150x.h(AbstractC0299g.a.ON_START);
        this.f5149w.i();
    }

    @Override // android.app.Activity
    public void onStateNotSaved() {
        this.f5149w.m();
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        this.f5148A = true;
        Z();
        this.f5149w.j();
        this.f5150x.h(AbstractC0299g.a.ON_STOP);
    }

    @Override // android.app.Activity, android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        View viewS = S(null, str, context, attributeSet);
        return viewS == null ? super.onCreateView(str, context, attributeSet) : viewS;
    }

    public ActivityC0288j(int i3) {
        super(i3);
        this.f5149w = C0292n.b(new a());
        this.f5150x = new androidx.lifecycle.m(this);
        this.f5148A = true;
        U();
    }
}
