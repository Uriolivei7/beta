package com.facebook.react.devsupport;

import android.app.Activity;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.facebook.react.bridge.UiThreadUtil;
import d1.AbstractC0507o;
import java.util.Arrays;
import java.util.Locale;
import k1.InterfaceC0585c;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: com.facebook.react.devsupport.h, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0375h implements InterfaceC0585c {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f6717d = new a(null);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static boolean f6718e = true;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final c0 f6719a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private TextView f6720b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private PopupWindow f6721c;

    /* JADX INFO: renamed from: com.facebook.react.devsupport.h$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public C0375h(c0 c0Var) {
        D2.h.f(c0Var, "reactInstanceDevHelper");
        this.f6719a = c0Var;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void g(C0375h c0375h) {
        c0375h.h();
    }

    private final void h() {
        PopupWindow popupWindow = this.f6721c;
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            this.f6721c = null;
            this.f6720b = null;
        }
    }

    private final void i(String str) {
        PopupWindow popupWindow = this.f6721c;
        if (popupWindow == null || !popupWindow.isShowing()) {
            Activity activityI = this.f6719a.i();
            if (activityI == null) {
                Y.a.m("ReactNative", "Unable to display loading message because react activity isn't available");
                return;
            }
            try {
                Rect rect = new Rect();
                activityI.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int i3 = rect.top;
                Object systemService = activityI.getSystemService("layout_inflater");
                D2.h.d(systemService, "null cannot be cast to non-null type android.view.LayoutInflater");
                View viewInflate = ((LayoutInflater) systemService).inflate(AbstractC0507o.f9256b, (ViewGroup) null);
                D2.h.d(viewInflate, "null cannot be cast to non-null type android.widget.TextView");
                TextView textView = (TextView) viewInflate;
                textView.setText(str);
                PopupWindow popupWindow2 = new PopupWindow(textView, -1, -2);
                popupWindow2.setTouchable(false);
                popupWindow2.showAtLocation(activityI.getWindow().getDecorView(), 0, 0, i3);
                this.f6720b = textView;
                this.f6721c = popupWindow2;
            } catch (WindowManager.BadTokenException unused) {
                Y.a.m("ReactNative", "Unable to display loading message because react activity isn't active, message: " + str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void j(C0375h c0375h, String str) {
        c0375h.i(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void k(Integer num, Integer num2, C0375h c0375h, String str) {
        String str2;
        if (num == null || num2 == null || num2.intValue() <= 0) {
            str2 = "";
        } else {
            D2.u uVar = D2.u.f192a;
            str2 = String.format(Locale.getDefault(), " %.1f%%", Arrays.copyOf(new Object[]{Float.valueOf((num.intValue() / num2.intValue()) * 100)}, 1));
            D2.h.e(str2, "format(...)");
        }
        TextView textView = c0375h.f6720b;
        if (textView != null) {
            if (str == null) {
                str = "Loading";
            }
            textView.setText(str + str2 + "…");
        }
    }

    @Override // k1.InterfaceC0585c
    public void a(final String str) {
        D2.h.f(str, "message");
        if (f6718e) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.f
                @Override // java.lang.Runnable
                public final void run() {
                    C0375h.j(this.f6697b, str);
                }
            });
        }
    }

    @Override // k1.InterfaceC0585c
    public void b(final String str, final Integer num, final Integer num2) {
        if (f6718e) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.g
                @Override // java.lang.Runnable
                public final void run() {
                    C0375h.k(num, num2, this, str);
                }
            });
        }
    }

    @Override // k1.InterfaceC0585c
    public void c() {
        if (f6718e) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.e
                @Override // java.lang.Runnable
                public final void run() {
                    C0375h.g(this.f6695b);
                }
            });
        }
    }
}
