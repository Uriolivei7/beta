package androidx.activity;

import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public abstract class r {
    public static final void a(View view, o oVar) {
        D2.h.f(view, "<this>");
        D2.h.f(oVar, "onBackPressedDispatcherOwner");
        view.setTag(p.f3039b, oVar);
    }
}
