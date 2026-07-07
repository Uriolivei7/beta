package com.facebook.imagepipeline.nativecode;

import X.k;
import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class NativeRoundingFilter {
    static {
        e.a();
    }

    private static native void nativeAddRoundedCornersFilter(Bitmap bitmap, int i3, int i4, int i5, int i6);

    private static native void nativeToCircleFastFilter(Bitmap bitmap, boolean z3);

    private static native void nativeToCircleFilter(Bitmap bitmap, boolean z3);

    private static native void nativeToCircleWithBorderFilter(Bitmap bitmap, int i3, int i4, boolean z3);

    public static void toCircle(Bitmap bitmap, boolean z3) {
        k.g(bitmap);
        if (bitmap.getWidth() < 3 || bitmap.getHeight() < 3) {
            return;
        }
        nativeToCircleFilter(bitmap, z3);
    }

    public static void toCircleFast(Bitmap bitmap, boolean z3) {
        k.g(bitmap);
        if (bitmap.getWidth() < 3 || bitmap.getHeight() < 3) {
            return;
        }
        nativeToCircleFastFilter(bitmap, z3);
    }
}
