package com.facebook.yoga;

/* JADX INFO: loaded from: classes.dex */
public enum k {
    NONE(0),
    STRETCH_FLEX_BASIS(1),
    ABSOLUTE_POSITION_WITHOUT_INSETS_EXCLUDES_PADDING(2),
    ABSOLUTE_PERCENT_AGAINST_INNER_SIZE(4),
    ALL(Integer.MAX_VALUE),
    CLASSIC(2147483646);


    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f8334b;

    k(int i3) {
        this.f8334b = i3;
    }

    public int b() {
        return this.f8334b;
    }
}
