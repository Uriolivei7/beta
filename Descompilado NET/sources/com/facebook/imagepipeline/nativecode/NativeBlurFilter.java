package com.facebook.imagepipeline.nativecode;

import X.k;
import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class NativeBlurFilter {
    static {
        e.a();
    }

    public static void a(Bitmap bitmap, int i3, int i4) {
        k.g(bitmap);
        k.b(Boolean.valueOf(i3 > 0));
        k.b(Boolean.valueOf(i4 > 0));
        nativeIterativeBoxBlur(bitmap, i3, i4);
    }

    private static native void nativeIterativeBoxBlur(Bitmap bitmap, int i3, int i4);
}
