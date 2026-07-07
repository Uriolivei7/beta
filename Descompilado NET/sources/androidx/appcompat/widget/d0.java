package androidx.appcompat.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public abstract class d0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final ThreadLocal f4191a = new ThreadLocal();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    static final int[] f4192b = {-16842910};

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    static final int[] f4193c = {R.attr.state_focused};

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    static final int[] f4194d = {R.attr.state_activated};

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    static final int[] f4195e = {R.attr.state_pressed};

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    static final int[] f4196f = {R.attr.state_checked};

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    static final int[] f4197g = {R.attr.state_selected};

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    static final int[] f4198h = {-16842919, -16842908};

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    static final int[] f4199i = new int[0];

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final int[] f4200j = new int[1];

    public static void a(View view, Context context) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(d.j.f9054y0);
        try {
            if (!typedArrayObtainStyledAttributes.hasValue(d.j.f8861D0)) {
                Log.e("ThemeUtils", "View " + view.getClass() + " is an AppCompat widget that can only be used with a Theme.AppCompat theme (or descendant).");
            }
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    public static int b(Context context, int i3) {
        ColorStateList colorStateListE = e(context, i3);
        if (colorStateListE != null && colorStateListE.isStateful()) {
            return colorStateListE.getColorForState(f4192b, colorStateListE.getDefaultColor());
        }
        TypedValue typedValueF = f();
        context.getTheme().resolveAttribute(R.attr.disabledAlpha, typedValueF, true);
        return d(context, i3, typedValueF.getFloat());
    }

    public static int c(Context context, int i3) {
        int[] iArr = f4200j;
        iArr[0] = i3;
        h0 h0VarT = h0.t(context, null, iArr);
        try {
            return h0VarT.b(0, 0);
        } finally {
            h0VarT.w();
        }
    }

    static int d(Context context, int i3, float f3) {
        return androidx.core.graphics.a.g(c(context, i3), Math.round(Color.alpha(r0) * f3));
    }

    public static ColorStateList e(Context context, int i3) {
        int[] iArr = f4200j;
        iArr[0] = i3;
        h0 h0VarT = h0.t(context, null, iArr);
        try {
            return h0VarT.c(0);
        } finally {
            h0VarT.w();
        }
    }

    private static TypedValue f() {
        ThreadLocal threadLocal = f4191a;
        TypedValue typedValue = (TypedValue) threadLocal.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        threadLocal.set(typedValue2);
        return typedValue2;
    }
}
