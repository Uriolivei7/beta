package com.facebook.react.views.scroll;

import android.os.SystemClock;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final a f7761f = new a(null);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private float f7764c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private float f7765d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f7762a = Integer.MIN_VALUE;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f7763b = Integer.MIN_VALUE;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private long f7766e = -11;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public final float a() {
        return this.f7764c;
    }

    public final float b() {
        return this.f7765d;
    }

    public final boolean c(int i3, int i4) {
        long jUptimeMillis = SystemClock.uptimeMillis();
        long j3 = this.f7766e;
        boolean z3 = (jUptimeMillis - j3 <= 10 && this.f7762a == i3 && this.f7763b == i4) ? false : true;
        if (jUptimeMillis - j3 != 0) {
            this.f7764c = (i3 - this.f7762a) / (jUptimeMillis - j3);
            this.f7765d = (i4 - this.f7763b) / (jUptimeMillis - j3);
        }
        this.f7766e = jUptimeMillis;
        this.f7762a = i3;
        this.f7763b = i4;
        return z3;
    }
}
