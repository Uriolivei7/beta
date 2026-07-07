package com.facebook.react.devsupport;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import androidx.core.view.C0264n0;
import com.facebook.fbreact.specs.NativeRedBoxSpec;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.devsupport.i0;
import d1.AbstractC0509q;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class i0 implements e1.j {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f6724e = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final k1.e f6725a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final K f6726b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Dialog f6727c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private g0 f6728d;

    public static final class a {

        /* JADX INFO: renamed from: com.facebook.react.devsupport.i0$a$a, reason: collision with other inner class name */
        public static final class C0104a implements LifecycleEventListener {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ Runnable f6729b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            final /* synthetic */ ReactContext f6730c;

            C0104a(Runnable runnable, ReactContext reactContext) {
                this.f6729b = runnable;
                this.f6730c = reactContext;
            }

            @Override // com.facebook.react.bridge.LifecycleEventListener
            public void onHostDestroy() {
            }

            @Override // com.facebook.react.bridge.LifecycleEventListener
            public void onHostPause() {
            }

            @Override // com.facebook.react.bridge.LifecycleEventListener
            public void onHostResume() {
                this.f6729b.run();
                this.f6730c.removeLifecycleEventListener(this);
            }
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void b(ReactContext reactContext, Runnable runnable) {
            reactContext.addLifecycleEventListener(new C0104a(runnable, reactContext));
        }

        private a() {
        }
    }

    public static final class b extends Dialog {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ i0 f6731b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(Activity activity, i0 i0Var, int i3) {
            super(activity, i3);
            this.f6731b = i0Var;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final C0264n0 b(int i3, View view, C0264n0 c0264n0) {
            D2.h.f(view, "view");
            D2.h.f(c0264n0, "windowInsetsCompat");
            androidx.core.graphics.b bVarF = c0264n0.f(i3);
            D2.h.e(bVarF, "getInsets(...)");
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            D2.h.d(layoutParams, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
            ((FrameLayout.LayoutParams) layoutParams).setMargins(bVarF.f4472a, bVarF.f4473b, bVarF.f4474c, bVarF.f4475d);
            return C0264n0.f4625b;
        }

        @Override // android.app.Dialog
        protected void onCreate(Bundle bundle) {
            Window window = getWindow();
            if (window == null) {
                throw new IllegalStateException("Required value was null.");
            }
            window.setBackgroundDrawable(new ColorDrawable(-16777216));
            final int iE = C0264n0.m.e() | C0264n0.m.a();
            g0 g0Var = this.f6731b.f6728d;
            if (g0Var == null) {
                throw new IllegalStateException("Required value was null.");
            }
            androidx.core.view.Z.i0(g0Var, new androidx.core.view.I() { // from class: com.facebook.react.devsupport.j0
                @Override // androidx.core.view.I
                public final C0264n0 a(View view, C0264n0 c0264n0) {
                    return i0.b.b(iE, view, c0264n0);
                }
            });
        }

        @Override // android.app.Dialog, android.view.KeyEvent.Callback
        public boolean onKeyUp(int i3, KeyEvent keyEvent) {
            D2.h.f(keyEvent, "event");
            if (i3 == 82) {
                this.f6731b.f6725a.x();
                return true;
            }
            if (this.f6731b.f6726b.b(i3, getCurrentFocus())) {
                this.f6731b.f6725a.s();
            }
            return super.onKeyUp(i3, keyEvent);
        }
    }

    public i0(k1.e eVar) {
        D2.h.f(eVar, "devSupportManager");
        this.f6725a = eVar;
        this.f6726b = new K();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void k(i0 i0Var) {
        i0Var.b();
    }

    @Override // e1.j
    public boolean a() {
        Dialog dialog = this.f6727c;
        return dialog != null && dialog.isShowing();
    }

    @Override // e1.j
    public void b() {
        String strL = this.f6725a.l();
        Activity activityI = this.f6725a.i();
        if (activityI == null || activityI.isFinishing()) {
            ReactContext reactContextC = this.f6725a.C();
            if (reactContextC != null) {
                f6724e.b(reactContextC, new Runnable() { // from class: com.facebook.react.devsupport.h0
                    @Override // java.lang.Runnable
                    public final void run() {
                        i0.k(this.f6722b);
                    }
                });
                return;
            }
            if (strL == null) {
                strL = "N/A";
            }
            Y.a.m("ReactNative", "Unable to launch redbox because react activity and react context is not available, here is the error that redbox would've displayed: " + strL);
            return;
        }
        g0 g0Var = this.f6728d;
        if ((g0Var != null ? g0Var.getContext() : null) != activityI) {
            f(NativeRedBoxSpec.NAME);
        }
        g0 g0Var2 = this.f6728d;
        if (g0Var2 != null) {
            g0Var2.g();
        }
        if (this.f6727c == null) {
            b bVar = new b(activityI, this, AbstractC0509q.f9302c);
            bVar.requestWindowFeature(1);
            g0 g0Var3 = this.f6728d;
            if (g0Var3 == null) {
                throw new IllegalStateException("Required value was null.");
            }
            bVar.setContentView(g0Var3);
            this.f6727c = bVar;
        }
        Dialog dialog = this.f6727c;
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override // e1.j
    public void c() {
        try {
            Dialog dialog = this.f6727c;
            if (dialog != null) {
                dialog.dismiss();
            }
        } catch (IllegalArgumentException e4) {
            Y.a.n("ReactNative", "RedBoxDialogSurfaceDelegate: error while dismissing dialog: ", e4);
        }
        d();
        this.f6727c = null;
    }

    @Override // e1.j
    public void d() {
        this.f6728d = null;
    }

    @Override // e1.j
    public boolean e() {
        return this.f6728d != null;
    }

    @Override // e1.j
    public void f(String str) {
        D2.h.f(str, "appKey");
        this.f6725a.t();
        Activity activityI = this.f6725a.i();
        if (activityI != null && !activityI.isFinishing()) {
            g0 g0Var = new g0(activityI, this.f6725a, null);
            g0Var.d();
            this.f6728d = g0Var;
            return;
        }
        String strL = this.f6725a.l();
        if (strL == null) {
            strL = "N/A";
        }
        Y.a.m("ReactNative", "Unable to launch redbox because react activity is not available, here is the error that redbox would've displayed: " + strL);
    }
}
