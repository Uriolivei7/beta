package com.facebook.react.views.safeareaview;

import D2.h;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.C0264n0;
import androidx.core.view.I;
import androidx.core.view.Z;
import com.facebook.react.bridge.GuardedRunnable;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.A0;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.UIManagerModule;

/* JADX INFO: loaded from: classes.dex */
public final class b extends ViewGroup {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final B0 f7748b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private A0 f7749c;

    public static final class a extends GuardedRunnable {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ androidx.core.graphics.b f7751c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(androidx.core.graphics.b bVar, B0 b02) {
            super(b02);
            this.f7751c = bVar;
        }

        @Override // com.facebook.react.bridge.GuardedRunnable
        public void runGuarded() {
            UIManagerModule uIManagerModule = (UIManagerModule) b.this.getReactContext().b().getNativeModule(UIManagerModule.class);
            if (uIManagerModule != null) {
                int id = b.this.getId();
                androidx.core.graphics.b bVar = this.f7751c;
                uIManagerModule.updateInsetsPadding(id, bVar.f4473b, bVar.f4472a, bVar.f4475d, bVar.f4474c);
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public b(B0 b02) {
        super(b02);
        h.f(b02, "reactContext");
        this.f7748b = b02;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final C0264n0 b(b bVar, View view, C0264n0 c0264n0) {
        h.f(view, "<unused var>");
        h.f(c0264n0, "windowInsets");
        androidx.core.graphics.b bVarF = c0264n0.f(C0264n0.m.e() | C0264n0.m.a());
        h.e(bVarF, "getInsets(...)");
        bVar.c(bVarF);
        return C0264n0.f4625b;
    }

    private final void c(androidx.core.graphics.b bVar) {
        A0 a02 = this.f7749c;
        if (a02 == null) {
            B0 b02 = this.f7748b;
            b02.runOnNativeModulesQueueThread(new a(bVar, b02));
            return;
        }
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        C0429f0 c0429f0 = C0429f0.f7476a;
        writableNativeMap.putDouble("left", c0429f0.d(bVar.f4472a));
        writableNativeMap.putDouble("top", c0429f0.d(bVar.f4473b));
        writableNativeMap.putDouble("bottom", c0429f0.d(bVar.f4475d));
        writableNativeMap.putDouble("right", c0429f0.d(bVar.f4474c));
        a02.b(writableNativeMap);
    }

    public final B0 getReactContext() {
        return this.f7748b;
    }

    public final A0 getStateWrapper$ReactAndroid_release() {
        return this.f7749c;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Z.i0(this, new I() { // from class: com.facebook.react.views.safeareaview.a
            @Override // androidx.core.view.I
            public final C0264n0 a(View view, C0264n0 c0264n0) {
                return b.b(this.f7747a, view, c0264n0);
            }
        });
        requestApplyInsets();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
    }

    public final void setStateWrapper$ReactAndroid_release(A0 a02) {
        this.f7749c = a02;
    }
}
