package com.facebook.react.views.debuggingoverlay;

import D2.h;
import android.graphics.RectF;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f7654a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final RectF f7655b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f7656c;

    public c(int i3, RectF rectF, int i4) {
        h.f(rectF, "rectangle");
        this.f7654a = i3;
        this.f7655b = rectF;
        this.f7656c = i4;
    }

    public final int a() {
        return this.f7656c;
    }

    public final int b() {
        return this.f7654a;
    }

    public final RectF c() {
        return this.f7655b;
    }
}
