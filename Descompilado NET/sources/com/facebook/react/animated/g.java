package com.facebook.react.animated;

import com.facebook.react.bridge.ReadableMap;

/* JADX INFO: loaded from: classes.dex */
public final class g extends e {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private double f6395e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private double f6396f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private long f6397g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private double f6398h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private double f6399i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f6400j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f6401k;

    public g(ReadableMap readableMap) {
        D2.h.f(readableMap, "config");
        this.f6397g = -1L;
        this.f6400j = 1;
        this.f6401k = 1;
        a(readableMap);
    }

    @Override // com.facebook.react.animated.e
    public void a(ReadableMap readableMap) {
        D2.h.f(readableMap, "config");
        this.f6395e = readableMap.getDouble("velocity");
        this.f6396f = readableMap.getDouble("deceleration");
        this.f6397g = -1L;
        this.f6398h = 0.0d;
        this.f6399i = 0.0d;
        int i3 = readableMap.hasKey("iterations") ? readableMap.getInt("iterations") : 1;
        this.f6400j = i3;
        this.f6401k = 1;
        this.f6382a = i3 == 0;
    }

    @Override // com.facebook.react.animated.e
    public void b(long j3) {
        w wVar = this.f6383b;
        if (wVar == null) {
            throw new IllegalArgumentException("Animated value should not be null");
        }
        long j4 = j3 / ((long) 1000000);
        if (this.f6397g == -1) {
            this.f6397g = j4 - ((long) 16);
            double d4 = this.f6398h;
            if (d4 == this.f6399i) {
                this.f6398h = wVar.f6495f;
            } else {
                wVar.f6495f = d4;
            }
            this.f6399i = wVar.f6495f;
        }
        double d5 = this.f6398h;
        double d6 = this.f6395e;
        double d7 = 1;
        double d8 = this.f6396f;
        double dExp = d5 + ((d6 / (d7 - d8)) * (d7 - Math.exp((-(d7 - d8)) * (j4 - this.f6397g))));
        if (Math.abs(this.f6399i - dExp) < 0.1d) {
            int i3 = this.f6400j;
            if (i3 != -1 && this.f6401k >= i3) {
                this.f6382a = true;
                return;
            } else {
                this.f6397g = -1L;
                this.f6401k++;
            }
        }
        this.f6399i = dExp;
        wVar.f6495f = dExp;
    }
}
