package com.facebook.react.devsupport;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import androidx.core.view.C0264n0;
import d1.AbstractC0509q;

/* JADX INFO: loaded from: classes.dex */
public final class P extends Dialog {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final View f6653b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public P(Activity activity, View view) {
        super(activity, AbstractC0509q.f9301b);
        D2.h.f(activity, "context");
        this.f6653b = view;
        requestWindowFeature(1);
        if (view != null) {
            setContentView(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final C0264n0 c(int i3, View view, C0264n0 c0264n0) {
        D2.h.f(view, "view");
        D2.h.f(c0264n0, "windowInsets");
        androidx.core.graphics.b bVarF = c0264n0.f(i3);
        D2.h.e(bVarF, "getInsets(...)");
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        D2.h.d(layoutParams, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        ((FrameLayout.LayoutParams) layoutParams).setMargins(bVarF.f4472a, bVarF.f4473b, bVarF.f4474c, bVarF.f4475d);
        return C0264n0.f4625b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final C0264n0 d(C2.p pVar, View view, C0264n0 c0264n0) {
        D2.h.f(view, "p0");
        D2.h.f(c0264n0, "p1");
        return (C0264n0) pVar.b(view, c0264n0);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(-16777216));
        }
        View view = this.f6653b;
        if (view != null) {
            final int iE = C0264n0.m.e() | C0264n0.m.a();
            final C2.p pVar = new C2.p() { // from class: com.facebook.react.devsupport.N
                @Override // C2.p
                public final Object b(Object obj, Object obj2) {
                    return P.c(iE, (View) obj, (C0264n0) obj2);
                }
            };
            androidx.core.view.Z.i0(view, new androidx.core.view.I() { // from class: com.facebook.react.devsupport.O
                @Override // androidx.core.view.I
                public final C0264n0 a(View view2, C0264n0 c0264n0) {
                    return P.d(pVar, view2, c0264n0);
                }
            });
        }
    }
}
