package com.facebook.react.views.swiperefresh;

import D2.h;
import P1.m;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.C0429f0;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class a extends androidx.swiperefreshlayout.widget.c {

    /* JADX INFO: renamed from: c0, reason: collision with root package name */
    private static final C0117a f7899c0 = new C0117a(null);

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private boolean f7900S;

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    private boolean f7901T;

    /* JADX INFO: renamed from: U, reason: collision with root package name */
    private float f7902U;

    /* JADX INFO: renamed from: V, reason: collision with root package name */
    private final int f7903V;

    /* JADX INFO: renamed from: W, reason: collision with root package name */
    private float f7904W;

    /* JADX INFO: renamed from: a0, reason: collision with root package name */
    private boolean f7905a0;

    /* JADX INFO: renamed from: b0, reason: collision with root package name */
    private boolean f7906b0;

    /* JADX INFO: renamed from: com.facebook.react.views.swiperefresh.a$a, reason: collision with other inner class name */
    private static final class C0117a {
        public /* synthetic */ C0117a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private C0117a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(ReactContext reactContext) {
        super(reactContext);
        h.f(reactContext, "reactContext");
        this.f7903V = ViewConfiguration.get(reactContext).getScaledTouchSlop();
    }

    private final boolean B(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.f7904W = motionEvent.getX();
            this.f7905a0 = false;
        } else if (action == 2) {
            float fAbs = Math.abs(motionEvent.getX() - this.f7904W);
            if (this.f7905a0 || fAbs > this.f7903V) {
                this.f7905a0 = true;
                return false;
            }
        }
        return true;
    }

    @Override // androidx.swiperefreshlayout.widget.c
    public boolean d() {
        View childAt = getChildAt(0);
        return childAt != null ? childAt.canScrollVertically(-1) : super.d();
    }

    @Override // androidx.swiperefreshlayout.widget.c, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        h.f(motionEvent, "ev");
        if (!B(motionEvent) || !super.onInterceptTouchEvent(motionEvent)) {
            return false;
        }
        m.b(this, motionEvent);
        this.f7906b0 = true;
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return true;
    }

    @Override // androidx.swiperefreshlayout.widget.c, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        super.onLayout(z3, i3, i4, i5, i6);
        if (this.f7900S) {
            return;
        }
        this.f7900S = true;
        setProgressViewOffset(this.f7902U);
        setRefreshing(this.f7901T);
    }

    @Override // androidx.swiperefreshlayout.widget.c, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        h.f(motionEvent, "ev");
        if (motionEvent.getActionMasked() == 1 && this.f7906b0) {
            m.a(this, motionEvent);
            this.f7906b0 = false;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // androidx.swiperefreshlayout.widget.c, android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z3) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z3);
        }
    }

    public final void setProgressViewOffset(float f3) {
        this.f7902U = f3;
        if (this.f7900S) {
            int progressCircleDiameter = getProgressCircleDiameter();
            s(false, Math.round(C0429f0.h(f3)) - progressCircleDiameter, Math.round(C0429f0.h(f3 + 64.0f)) - progressCircleDiameter);
        }
    }

    @Override // androidx.swiperefreshlayout.widget.c
    public void setRefreshing(boolean z3) {
        this.f7901T = z3;
        if (this.f7900S) {
            super.setRefreshing(z3);
        }
    }
}
