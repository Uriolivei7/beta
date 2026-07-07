package com.facebook.react.uimanager;

/* JADX INFO: loaded from: classes.dex */
public final class T {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final T f7383a = new T();

    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f7384a;

        static {
            int[] iArr = new int[com.facebook.yoga.h.values().length];
            try {
                iArr[com.facebook.yoga.h.LTR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[com.facebook.yoga.h.RTL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            f7384a = iArr;
        }
    }

    private T() {
    }

    public static final int a(com.facebook.yoga.h hVar) {
        D2.h.f(hVar, "direction");
        int i3 = a.f7384a[hVar.ordinal()];
        if (i3 != 1) {
            return i3 != 2 ? 2 : 1;
        }
        return 0;
    }
}
