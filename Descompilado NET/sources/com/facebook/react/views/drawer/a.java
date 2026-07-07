package com.facebook.react.views.drawer;

import D2.h;
import P1.m;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.C0237a;
import androidx.core.view.Z;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.C0433h0;
import d1.AbstractC0505m;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r.v;
import y.C0775a;

/* JADX INFO: loaded from: classes.dex */
public final class a extends C0775a {

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    public static final b f7659T = new b(null);

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private int f7660Q;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private int f7661R;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private boolean f7662S;

    /* JADX INFO: renamed from: com.facebook.react.views.drawer.a$a, reason: collision with other inner class name */
    public static final class C0112a extends C0237a {
        C0112a() {
        }

        @Override // androidx.core.view.C0237a
        public void f(View view, AccessibilityEvent accessibilityEvent) {
            h.f(view, "host");
            h.f(accessibilityEvent, "event");
            super.f(view, accessibilityEvent);
            Object tag = view.getTag(AbstractC0505m.f9234g);
            if (tag instanceof C0433h0.d) {
                accessibilityEvent.setClassName(C0433h0.d.e((C0433h0.d) tag));
            }
        }

        @Override // androidx.core.view.C0237a
        public void g(View view, v vVar) {
            h.f(view, "host");
            h.f(vVar, "info");
            super.g(view, vVar);
            C0433h0.d dVarD = C0433h0.d.d(view);
            if (dVarD != null) {
                vVar.p0(C0433h0.d.e(dVarD));
            }
        }
    }

    public static final class b {
        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private b() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(ReactContext reactContext) {
        super(reactContext);
        h.f(reactContext, "reactContext");
        this.f7660Q = 8388611;
        this.f7661R = -1;
        Z.X(this, new C0112a());
    }

    public final void V() {
        d(this.f7660Q);
    }

    public final void W() {
        I(this.f7660Q);
    }

    public final void X() {
        if (getChildCount() == 2) {
            View childAt = getChildAt(1);
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            h.d(layoutParams, "null cannot be cast to non-null type androidx.drawerlayout.widget.DrawerLayout.LayoutParams");
            C0775a.e eVar = (C0775a.e) layoutParams;
            eVar.f11003a = this.f7660Q;
            ((ViewGroup.MarginLayoutParams) eVar).width = this.f7661R;
            childAt.setLayoutParams(eVar);
            childAt.setClickable(true);
        }
    }

    @Override // y.C0775a, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        h.f(motionEvent, "ev");
        try {
            if (!super.onInterceptTouchEvent(motionEvent)) {
                return false;
            }
            m.b(this, motionEvent);
            this.f7662S = true;
            return true;
        } catch (IllegalArgumentException e4) {
            Y.a.J("ReactNative", "Error intercepting touch event.", e4);
            return false;
        }
    }

    @Override // y.C0775a, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        h.f(motionEvent, "ev");
        if (motionEvent.getActionMasked() == 1 && this.f7662S) {
            m.a(this, motionEvent);
            this.f7662S = false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public final void setDrawerPosition$ReactAndroid_release(int i3) {
        this.f7660Q = i3;
        X();
    }

    public final void setDrawerWidth$ReactAndroid_release(int i3) {
        this.f7661R = i3;
        X();
    }
}
