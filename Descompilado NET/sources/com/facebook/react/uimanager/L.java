package com.facebook.react.uimanager;

/* JADX INFO: loaded from: classes.dex */
public final class L {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final L f7256a = new L();

    private L() {
    }

    public static final boolean a(float f3, float f4) {
        if (Float.isNaN(f3) || Float.isNaN(f4)) {
            if (!Float.isNaN(f3) || !Float.isNaN(f4)) {
                return false;
            }
        } else if (Math.abs(f4 - f3) >= 1.0E-5f) {
            return false;
        }
        return true;
    }

    public static final boolean b(Float f3, Float f4) {
        if (f3 == null) {
            return f4 == null;
        }
        if (f4 == null) {
            return false;
        }
        return a(f3.floatValue(), f4.floatValue());
    }
}
