package com.facebook.yoga;

/* JADX INFO: loaded from: classes.dex */
public enum p {
    UNDEFINED(0),
    EXACTLY(1),
    AT_MOST(2);


    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f8358b;

    p(int i3) {
        this.f8358b = i3;
    }

    public static p b(int i3) {
        if (i3 == 0) {
            return UNDEFINED;
        }
        if (i3 == 1) {
            return EXACTLY;
        }
        if (i3 == 2) {
            return AT_MOST;
        }
        throw new IllegalArgumentException("Unknown enum value: " + i3);
    }
}
