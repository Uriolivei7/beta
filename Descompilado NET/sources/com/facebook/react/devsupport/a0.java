package com.facebook.react.devsupport;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.facebook.react.bridge.UiThreadUtil;
import d1.AbstractC0504l;
import d1.AbstractC0505m;
import d1.AbstractC0507o;
import d1.AbstractC0509q;
import k1.e;

/* JADX INFO: loaded from: classes.dex */
public final class a0 implements k1.h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final q.i f6671a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Dialog f6672b;

    public a0(q.i iVar) {
        D2.h.f(iVar, "contextSupplier");
        this.f6671a = iVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void f(a0 a0Var) {
        Dialog dialog = a0Var.f6672b;
        if (dialog != null) {
            dialog.dismiss();
        }
        a0Var.f6672b = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void g(a0 a0Var, String str, final e.a aVar) {
        Dialog dialog = a0Var.f6672b;
        if (dialog != null) {
            dialog.dismiss();
        }
        Context context = (Context) a0Var.f6671a.get();
        if (context == null) {
            return;
        }
        View viewInflate = LayoutInflater.from(context).inflate(AbstractC0507o.f9258d, (ViewGroup) null);
        D2.h.e(viewInflate, "inflate(...)");
        viewInflate.findViewById(AbstractC0505m.f9239l).setOnClickListener(new View.OnClickListener() { // from class: com.facebook.react.devsupport.Z
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                a0.h(aVar, view);
            }
        });
        ((TextView) viewInflate.findViewById(AbstractC0505m.f9240m)).setText(str);
        Dialog dialog2 = new Dialog(context, AbstractC0509q.f9300a);
        dialog2.setContentView(viewInflate);
        dialog2.setCancelable(false);
        a0Var.f6672b = dialog2;
        Window window = dialog2.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            D2.h.e(attributes, "getAttributes(...)");
            attributes.dimAmount = 0.2f;
            window.setAttributes(attributes);
            window.addFlags(2);
            window.setGravity(48);
            window.setElevation(0.0f);
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.setBackgroundDrawableResource(AbstractC0504l.f9222a);
        }
        Dialog dialog3 = a0Var.f6672b;
        if (dialog3 != null) {
            dialog3.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void h(e.a aVar, View view) {
        aVar.a();
    }

    @Override // k1.h
    public void d(final String str, final e.a aVar) {
        D2.h.f(str, "message");
        D2.h.f(aVar, "listener");
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.X
            @Override // java.lang.Runnable
            public final void run() {
                a0.g(this.f6666b, str, aVar);
            }
        });
    }

    @Override // k1.h
    public void e() {
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.Y
            @Override // java.lang.Runnable
            public final void run() {
                a0.f(this.f6669b);
            }
        });
    }
}
