package com.swmansion.gesturehandler.react;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.InterfaceC0462w0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import n2.C0625d;

/* JADX INFO: loaded from: classes.dex */
public final class j {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final a f8632g = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ReactContext f8633a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final n2.i f8634b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0625d f8635c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final ViewGroup f8636d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f8637e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f8638f;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final ViewGroup b(ViewGroup viewGroup) {
            UiThreadUtil.assertOnUiThread();
            ViewParent parent = viewGroup;
            while (parent != null && !(parent instanceof InterfaceC0462w0)) {
                parent = parent.getParent();
            }
            if (parent != null) {
                return (ViewGroup) parent;
            }
            throw new IllegalStateException(("View " + viewGroup + " has not been mounted under ReactRootView").toString());
        }

        private a() {
        }
    }

    public final class b extends C0625d {
        public b() {
        }

        private final void S0(MotionEvent motionEvent) {
            n2.i iVarN;
            if (Q() == 0 && (!j.this.f8637e || (iVarN = N()) == null || !iVarN.v())) {
                n();
                j.this.f8637e = false;
            }
            if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 10) {
                z();
            }
        }

        @Override // n2.C0625d
        protected void g0() {
            j.this.f8637e = true;
            long jUptimeMillis = SystemClock.uptimeMillis();
            MotionEvent motionEventObtain = MotionEvent.obtain(jUptimeMillis, jUptimeMillis, 3, 0.0f, 0.0f, 0);
            motionEventObtain.setAction(3);
            if (j.this.f() instanceof InterfaceC0462w0) {
                InterfaceC0462w0 interfaceC0462w0 = (InterfaceC0462w0) j.this.f();
                ViewGroup viewGroupF = j.this.f();
                D2.h.c(motionEventObtain);
                interfaceC0462w0.c(viewGroupF, motionEventObtain);
            }
            motionEventObtain.recycle();
        }

        @Override // n2.C0625d
        protected void h0(MotionEvent motionEvent, MotionEvent motionEvent2) {
            D2.h.f(motionEvent, "event");
            D2.h.f(motionEvent2, "sourceEvent");
            S0(motionEvent);
        }

        @Override // n2.C0625d
        protected void i0(MotionEvent motionEvent, MotionEvent motionEvent2) {
            D2.h.f(motionEvent, "event");
            D2.h.f(motionEvent2, "sourceEvent");
            S0(motionEvent);
        }
    }

    public j(ReactContext reactContext, ViewGroup viewGroup) {
        D2.h.f(reactContext, "context");
        D2.h.f(viewGroup, "wrappedView");
        this.f8633a = reactContext;
        UiThreadUtil.assertOnUiThread();
        int id = viewGroup.getId();
        if (id < 1) {
            throw new IllegalStateException(("Expect view tag to be set for " + viewGroup).toString());
        }
        D2.h.d(reactContext, "null cannot be cast to non-null type com.facebook.react.uimanager.ThemedReactContext");
        NativeModule nativeModule = ((B0) reactContext).b().getNativeModule((Class<NativeModule>) RNGestureHandlerModule.class);
        D2.h.c(nativeModule);
        RNGestureHandlerModule rNGestureHandlerModule = (RNGestureHandlerModule) nativeModule;
        h registry = rNGestureHandlerModule.getRegistry();
        ViewGroup viewGroupB = f8632g.b(viewGroup);
        this.f8636d = viewGroupB;
        Log.i("ReactNative", "[GESTURE HANDLER] Initialize gesture handler for root view " + viewGroupB);
        n2.i iVar = new n2.i(viewGroup, registry, new n());
        iVar.F(0.1f);
        this.f8634b = iVar;
        b bVar = new b();
        bVar.G0(-id);
        this.f8635c = bVar;
        registry.j(bVar);
        registry.c(bVar.R(), id, 3);
        rNGestureHandlerModule.registerRootHelper(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void h(j jVar) {
        jVar.k();
    }

    private final void k() {
        C0625d c0625d = this.f8635c;
        if (c0625d == null || c0625d.Q() != 2) {
            return;
        }
        c0625d.i();
        c0625d.z();
    }

    public final void d(View view) {
        D2.h.f(view, "view");
        n2.i iVar = this.f8634b;
        if (iVar != null) {
            iVar.f(view);
        }
    }

    public final boolean e(MotionEvent motionEvent) {
        D2.h.f(motionEvent, "ev");
        this.f8638f = true;
        n2.i iVar = this.f8634b;
        D2.h.c(iVar);
        iVar.B(motionEvent);
        this.f8638f = false;
        return this.f8637e;
    }

    public final ViewGroup f() {
        return this.f8636d;
    }

    public final void g(int i3, boolean z3) {
        if (z3) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.swmansion.gesturehandler.react.i
                @Override // java.lang.Runnable
                public final void run() {
                    j.h(this.f8631b);
                }
            });
        }
    }

    public final void i() {
        if (this.f8634b == null || this.f8638f) {
            return;
        }
        k();
    }

    public final void j() {
        Log.i("ReactNative", "[GESTURE HANDLER] Tearing down gesture handler registered for root view " + this.f8636d);
        ReactContext reactContext = this.f8633a;
        D2.h.d(reactContext, "null cannot be cast to non-null type com.facebook.react.uimanager.ThemedReactContext");
        NativeModule nativeModule = ((B0) reactContext).b().getNativeModule((Class<NativeModule>) RNGestureHandlerModule.class);
        D2.h.c(nativeModule);
        RNGestureHandlerModule rNGestureHandlerModule = (RNGestureHandlerModule) nativeModule;
        h registry = rNGestureHandlerModule.getRegistry();
        C0625d c0625d = this.f8635c;
        D2.h.c(c0625d);
        registry.g(c0625d.R());
        rNGestureHandlerModule.unregisterRootHelper(this);
    }
}
