package com.facebook.react.views.view;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import androidx.core.view.AbstractC0262m0;
import androidx.core.view.M0;
import androidx.core.view.Z;

/* JADX INFO: loaded from: classes.dex */
public abstract class p {
    public static final void b(Window window, boolean z3) {
        D2.h.f(window, "<this>");
        if (z3) {
            window.getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.facebook.react.views.view.o
                @Override // android.view.View.OnApplyWindowInsetsListener
                public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    return p.c(view, windowInsets);
                }
            });
        } else {
            window.getDecorView().setOnApplyWindowInsetsListener(null);
        }
        Z.U(window.getDecorView());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final WindowInsets c(View view, WindowInsets windowInsets) {
        D2.h.f(view, "v");
        D2.h.f(windowInsets, "insets");
        WindowInsets windowInsetsOnApplyWindowInsets = view.onApplyWindowInsets(windowInsets);
        return windowInsetsOnApplyWindowInsets.replaceSystemWindowInsets(windowInsetsOnApplyWindowInsets.getSystemWindowInsetLeft(), 0, windowInsetsOnApplyWindowInsets.getSystemWindowInsetRight(), windowInsetsOnApplyWindowInsets.getSystemWindowInsetBottom());
    }

    public static final void d(Window window, boolean z3) {
        D2.h.f(window, "<this>");
        if (z3) {
            f(window);
        } else {
            g(window);
        }
    }

    public static final void e(Window window, boolean z3) {
        D2.h.f(window, "<this>");
        AbstractC0262m0.b(window, !z3);
        if (z3) {
            boolean z4 = (window.getContext().getResources().getConfiguration().uiMode & 48) == 32;
            int i3 = Build.VERSION.SDK_INT;
            if (i3 >= 29) {
                window.setStatusBarContrastEnforced(false);
                window.setNavigationBarContrastEnforced(true);
            }
            window.setStatusBarColor(0);
            window.setNavigationBarColor(i3 < 29 ? (i3 < 27 || z4) ? Color.argb(128, 27, 27, 27) : Color.argb(230, 255, 255, 255) : 0);
            new M0(window, window.getDecorView()).c(!z4);
            if (i3 >= 28) {
                window.getAttributes().layoutInDisplayCutoutMode = i3 >= 30 ? 3 : 1;
            }
        }
    }

    private static final void f(Window window) {
        if (Build.VERSION.SDK_INT >= 30) {
            window.getAttributes().layoutInDisplayCutoutMode = 1;
            window.setDecorFitsSystemWindows(false);
        }
        window.addFlags(1024);
        window.clearFlags(2048);
    }

    private static final void g(Window window) {
        if (Build.VERSION.SDK_INT >= 30) {
            window.getAttributes().layoutInDisplayCutoutMode = 0;
            window.setDecorFitsSystemWindows(true);
        }
        window.addFlags(2048);
        window.clearFlags(1024);
    }
}
