package com.facebook.yoga;

/* JADX INFO: loaded from: classes.dex */
public class q {
    public static long a(float f3, float f4) {
        return ((long) Float.floatToRawIntBits(f4)) | (((long) Float.floatToRawIntBits(f3)) << 32);
    }

    public static long b(int i3, int i4) {
        return a(i3, i4);
    }
}
