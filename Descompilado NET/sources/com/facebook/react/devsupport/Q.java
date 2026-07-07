package com.facebook.react.devsupport;

import a1.C0210a;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes.dex */
public final class Q implements e1.j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final k1.e f6654a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private View f6655b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private P f6656c;

    public Q(k1.e eVar) {
        D2.h.f(eVar, "devSupportManager");
        this.f6654a = eVar;
    }

    @Override // e1.j
    public boolean a() {
        P p3 = this.f6656c;
        if (p3 != null) {
            return p3.isShowing();
        }
        return false;
    }

    @Override // e1.j
    public void b() {
        if (a() || !e()) {
            return;
        }
        Activity activityI = this.f6654a.i();
        if (activityI == null || activityI.isFinishing()) {
            T1.c.a("Unable to launch logbox because react activity is not available, here is the error that logbox would've displayed: ");
            return;
        }
        P p3 = new P(activityI, this.f6655b);
        this.f6656c = p3;
        p3.setCancelable(false);
        p3.show();
    }

    @Override // e1.j
    public void c() {
        P p3;
        if (a() && (p3 = this.f6656c) != null) {
            p3.dismiss();
        }
        View view = this.f6655b;
        ViewGroup viewGroup = (ViewGroup) (view != null ? view.getParent() : null);
        if (viewGroup != null) {
            viewGroup.removeView(this.f6655b);
        }
        this.f6656c = null;
    }

    @Override // e1.j
    public void d() {
        View view = this.f6655b;
        if (view != null) {
            this.f6654a.b(view);
            this.f6655b = null;
        }
    }

    @Override // e1.j
    public boolean e() {
        return this.f6655b != null;
    }

    @Override // e1.j
    public void f(String str) {
        D2.h.f(str, "appKey");
        C0210a.b(D2.h.b(str, "LogBox"), "This surface manager can only create LogBox React application");
        View viewA = this.f6654a.a("LogBox");
        this.f6655b = viewA;
        if (viewA == null) {
            T1.c.a("Unable to launch logbox because react was unable to create the root view");
        }
    }
}
