package com.facebook.yoga;

/* JADX INFO: loaded from: classes.dex */
public enum h {
    INHERIT(0),
    LTR(1),
    RTL(2);


    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f8310b;

    h(int i3) {
        this.f8310b = i3;
    }

    public static h b(int i3) {
        if (i3 == 0) {
            return INHERIT;
        }
        if (i3 == 1) {
            return LTR;
        }
        if (i3 == 2) {
            return RTL;
        }
        throw new IllegalArgumentException("Unknown enum value: " + i3);
    }

    public int c() {
        return this.f8310b;
    }
}
