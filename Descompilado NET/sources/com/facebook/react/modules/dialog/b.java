package com.facebook.react.modules.dialog;

import a1.C0210a;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.b;
import androidx.core.view.C0237a;
import androidx.core.view.Z;
import androidx.fragment.app.DialogInterfaceOnCancelListenerC0283e;
import com.facebook.react.modules.dialog.DialogModule;
import d.j;
import d1.AbstractC0505m;
import d1.AbstractC0507o;
import r.v;

/* JADX INFO: loaded from: classes.dex */
public class b extends DialogInterfaceOnCancelListenerC0283e implements DialogInterface.OnClickListener {

    /* JADX INFO: renamed from: t0, reason: collision with root package name */
    private final DialogModule.b f6975t0;

    class a extends C0237a {

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ TextView f6976d;

        a(TextView textView) {
            this.f6976d = textView;
        }

        @Override // androidx.core.view.C0237a
        public void g(View view, v vVar) {
            super.g(this.f6976d, vVar);
            vVar.x0(true);
        }
    }

    public b() {
        this.f6975t0 = null;
    }

    private static Dialog M1(Context context, Bundle bundle, DialogInterface.OnClickListener onClickListener) {
        b.a aVar = new b.a(context);
        if (bundle.containsKey("title")) {
            aVar.d(P1(context, (String) C0210a.c(bundle.getString("title"))));
        }
        if (bundle.containsKey("button_positive")) {
            aVar.k(bundle.getString("button_positive"), onClickListener);
        }
        if (bundle.containsKey("button_negative")) {
            aVar.h(bundle.getString("button_negative"), onClickListener);
        }
        if (bundle.containsKey("button_neutral")) {
            aVar.i(bundle.getString("button_neutral"), onClickListener);
        }
        if (bundle.containsKey("message")) {
            aVar.g(bundle.getString("message"));
        }
        if (bundle.containsKey("items")) {
            aVar.f(bundle.getCharSequenceArray("items"), onClickListener);
        }
        return aVar.a();
    }

    private static Dialog N1(Context context, Bundle bundle, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (bundle.containsKey("title")) {
            builder.setCustomTitle(P1(context, (String) C0210a.c(bundle.getString("title"))));
        }
        if (bundle.containsKey("button_positive")) {
            builder.setPositiveButton(bundle.getString("button_positive"), onClickListener);
        }
        if (bundle.containsKey("button_negative")) {
            builder.setNegativeButton(bundle.getString("button_negative"), onClickListener);
        }
        if (bundle.containsKey("button_neutral")) {
            builder.setNeutralButton(bundle.getString("button_neutral"), onClickListener);
        }
        if (bundle.containsKey("message")) {
            builder.setMessage(bundle.getString("message"));
        }
        if (bundle.containsKey("items")) {
            builder.setItems(bundle.getCharSequenceArray("items"), onClickListener);
        }
        return builder.create();
    }

    public static Dialog O1(Context context, Bundle bundle, DialogInterface.OnClickListener onClickListener) {
        return Q1(context) ? M1(context, bundle, onClickListener) : N1(context, bundle, onClickListener);
    }

    private static View P1(Context context, String str) {
        View viewInflate = LayoutInflater.from(context).inflate(AbstractC0507o.f9255a, (ViewGroup) null);
        TextView textView = (TextView) C0210a.c((TextView) viewInflate.findViewById(AbstractC0505m.f9238k));
        textView.setText(str);
        textView.setFocusable(true);
        if (Build.VERSION.SDK_INT >= 28) {
            textView.setAccessibilityHeading(true);
        } else {
            Z.X(textView, new a(textView));
        }
        return viewInflate;
    }

    private static boolean Q1(Context context) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(j.f9054y0);
        boolean zHasValue = typedArrayObtainStyledAttributes.hasValue(j.f8861D0);
        typedArrayObtainStyledAttributes.recycle();
        return zHasValue;
    }

    @Override // androidx.fragment.app.DialogInterfaceOnCancelListenerC0283e
    public Dialog F1(Bundle bundle) {
        return O1(k1(), l1(), this);
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i3) {
        DialogModule.b bVar = this.f6975t0;
        if (bVar != null) {
            bVar.onClick(dialogInterface, i3);
        }
    }

    @Override // androidx.fragment.app.DialogInterfaceOnCancelListenerC0283e, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        DialogModule.b bVar = this.f6975t0;
        if (bVar != null) {
            bVar.onDismiss(dialogInterface);
        }
    }

    public b(DialogModule.b bVar, Bundle bundle) {
        this.f6975t0 = bVar;
        s1(bundle);
    }
}
