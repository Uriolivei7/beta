package androidx.activity;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.InterfaceC0302j;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class ImmLeaksCleaner implements InterfaceC0302j {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static int f2996b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Field f2997c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static Field f2998d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static Field f2999e;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Activity f3000a;

    private static void h() {
        try {
            f2996b = 2;
            Field declaredField = InputMethodManager.class.getDeclaredField("mServedView");
            f2998d = declaredField;
            declaredField.setAccessible(true);
            Field declaredField2 = InputMethodManager.class.getDeclaredField("mNextServedView");
            f2999e = declaredField2;
            declaredField2.setAccessible(true);
            Field declaredField3 = InputMethodManager.class.getDeclaredField("mH");
            f2997c = declaredField3;
            declaredField3.setAccessible(true);
            f2996b = 1;
        } catch (NoSuchFieldException unused) {
        }
    }

    @Override // androidx.lifecycle.InterfaceC0302j
    public void d(androidx.lifecycle.l lVar, AbstractC0299g.a aVar) {
        if (aVar != AbstractC0299g.a.ON_DESTROY) {
            return;
        }
        if (f2996b == 0) {
            h();
        }
        if (f2996b == 1) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.f3000a.getSystemService("input_method");
            try {
                Object obj = f2997c.get(inputMethodManager);
                if (obj == null) {
                    return;
                }
                synchronized (obj) {
                    try {
                        try {
                            try {
                                View view = (View) f2998d.get(inputMethodManager);
                                if (view == null) {
                                    return;
                                }
                                if (view.isAttachedToWindow()) {
                                    return;
                                }
                                try {
                                    f2999e.set(inputMethodManager, null);
                                    inputMethodManager.isActive();
                                } catch (IllegalAccessException unused) {
                                }
                            } catch (ClassCastException unused2) {
                            }
                        } catch (IllegalAccessException unused3) {
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            } catch (IllegalAccessException unused4) {
            }
        }
    }
}
