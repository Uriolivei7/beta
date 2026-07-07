package com.facebook.yoga;

/* JADX INFO: loaded from: classes.dex */
public enum YogaLogLevel {
    ERROR(0),
    WARN(1),
    INFO(2),
    DEBUG(3),
    VERBOSE(4),
    FATAL(5);


    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f8278b;

    YogaLogLevel(int i3) {
        this.f8278b = i3;
    }

    public static YogaLogLevel fromInt(int i3) {
        if (i3 == 0) {
            return ERROR;
        }
        if (i3 == 1) {
            return WARN;
        }
        if (i3 == 2) {
            return INFO;
        }
        if (i3 == 3) {
            return DEBUG;
        }
        if (i3 == 4) {
            return VERBOSE;
        }
        if (i3 == 5) {
            return FATAL;
        }
        throw new IllegalArgumentException("Unknown enum value: " + i3);
    }
}
