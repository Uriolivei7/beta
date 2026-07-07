package com.reactnativecommunity.blurview;

import android.R;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.react.uimanager.B0;
import java.util.Objects;
import q2.C0655c;

/* JADX INFO: loaded from: classes.dex */
abstract class a {
    public static C0655c a(B0 b02) {
        C0655c c0655c = new C0655c(b02);
        Activity currentActivity = b02.getCurrentActivity();
        Objects.requireNonNull(currentActivity);
        View decorView = currentActivity.getWindow().getDecorView();
        c0655c.f((ViewGroup) decorView.findViewById(R.id.content)).a(decorView.getBackground()).e(10.0f);
        return c0655c;
    }

    public static void b(C0655c c0655c, boolean z3) {
        c0655c.b(z3);
        c0655c.invalidate();
    }

    public static void c(C0655c c0655c, boolean z3) {
        c0655c.c(z3);
    }

    public static void d(C0655c c0655c, int i3) {
        c0655c.e(i3);
        c0655c.invalidate();
    }

    public static void e(C0655c c0655c, int i3) {
        c0655c.d(i3);
        c0655c.invalidate();
    }
}
