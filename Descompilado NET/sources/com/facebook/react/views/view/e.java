package com.facebook.react.views.view;

import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final e f8165a = new e();

    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f8166a;

        static {
            int[] iArr = new int[com.facebook.yoga.p.values().length];
            try {
                iArr[com.facebook.yoga.p.EXACTLY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[com.facebook.yoga.p.AT_MOST.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            f8166a = iArr;
        }
    }

    private e() {
    }

    public static final int a(float f3, com.facebook.yoga.p pVar) {
        D2.h.f(pVar, "mode");
        int i3 = a.f8166a[pVar.ordinal()];
        return i3 != 1 ? i3 != 2 ? View.MeasureSpec.makeMeasureSpec(0, 0) : View.MeasureSpec.makeMeasureSpec((int) f3, Integer.MIN_VALUE) : View.MeasureSpec.makeMeasureSpec((int) f3, 1073741824);
    }
}
