package com.swmansion.gesturehandler.react;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.uimanager.InterfaceC0462w0;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class k extends com.facebook.react.views.view.g {

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    public static final a f8640v = new a(null);

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private boolean f8641t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private j f8642u;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean b(ViewGroup viewGroup) {
            UiThreadUtil.assertOnUiThread();
            for (ViewParent parent = viewGroup.getParent(); parent != null; parent = parent.getParent()) {
                if ((parent instanceof c) || (parent instanceof k)) {
                    return true;
                }
                if (parent instanceof InterfaceC0462w0) {
                    return false;
                }
            }
            return false;
        }

        private a() {
        }
    }

    public k(Context context) {
        super(context);
    }

    public final void F(View view) {
        D2.h.f(view, "view");
        j jVar = this.f8642u;
        if (jVar != null) {
            jVar.d(view);
        }
    }

    public final void G() {
        j jVar = this.f8642u;
        if (jVar != null) {
            jVar.j();
        }
    }

    @Override // com.facebook.react.views.view.g, android.view.View
    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        D2.h.f(motionEvent, "event");
        if (this.f8641t) {
            j jVar = this.f8642u;
            D2.h.c(jVar);
            if (jVar.e(motionEvent)) {
                return true;
            }
        }
        return super.dispatchGenericMotionEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        D2.h.f(motionEvent, "ev");
        if (this.f8641t) {
            j jVar = this.f8642u;
            D2.h.c(jVar);
            if (jVar.e(motionEvent)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // com.facebook.react.views.view.g, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        boolean zB = f8640v.b(this);
        this.f8641t = !zB;
        if (zB) {
            Log.i("ReactNative", "[GESTURE HANDLER] Gesture handler is already enabled for a parent view");
        }
        if (this.f8641t && this.f8642u == null) {
            Context context = getContext();
            D2.h.d(context, "null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
            this.f8642u = new j((ReactContext) context, this);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z3) {
        if (this.f8641t) {
            j jVar = this.f8642u;
            D2.h.c(jVar);
            jVar.i();
        }
        super.requestDisallowInterceptTouchEvent(z3);
    }
}
