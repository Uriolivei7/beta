package com.facebook.react.animated;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import g1.C0542a;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class j extends e {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final a f6409l = new a(null);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private long f6410e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private double[] f6411f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private double f6412g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private double f6413h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f6414i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f6415j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f6416k;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public j(ReadableMap readableMap) {
        D2.h.f(readableMap, "config");
        this.f6410e = -1L;
        this.f6411f = new double[0];
        this.f6414i = 1;
        this.f6415j = 1;
        a(readableMap);
    }

    @Override // com.facebook.react.animated.e
    public void a(ReadableMap readableMap) {
        int size;
        D2.h.f(readableMap, "config");
        ReadableArray array = readableMap.getArray("frames");
        if (array != null && this.f6411f.length != (size = array.size())) {
            double[] dArr = new double[size];
            for (int i3 = 0; i3 < size; i3++) {
                dArr[i3] = array.getDouble(i3);
            }
            this.f6411f = dArr;
        }
        this.f6412g = (readableMap.hasKey("toValue") && readableMap.getType("toValue") == ReadableType.Number) ? readableMap.getDouble("toValue") : 0.0d;
        int i4 = (readableMap.hasKey("iterations") && readableMap.getType("iterations") == ReadableType.Number) ? readableMap.getInt("iterations") : 1;
        this.f6414i = i4;
        this.f6415j = 1;
        this.f6382a = i4 == 0;
        this.f6410e = -1L;
    }

    @Override // com.facebook.react.animated.e
    public void b(long j3) {
        double d4;
        w wVar = this.f6383b;
        if (wVar == null) {
            throw new IllegalArgumentException("Animated value should not be null");
        }
        if (this.f6410e < 0) {
            this.f6410e = j3;
            if (this.f6415j == 1) {
                this.f6413h = wVar.f6495f;
            }
        }
        int iRound = (int) Math.round(((j3 - this.f6410e) / ((long) 1000000)) / 16.666666666666668d);
        if (iRound < 0) {
            String str = "Calculated frame index should never be lower than 0. Called with frameTimeNanos " + j3 + " and mStartFrameTimeNanos " + this.f6410e;
            if (C0542a.f9423b) {
                throw new IllegalStateException(str.toString());
            }
            if (this.f6416k < 100) {
                Y.a.I("ReactNative", str);
                this.f6416k++;
                return;
            }
            return;
        }
        if (this.f6382a) {
            return;
        }
        double[] dArr = this.f6411f;
        if (iRound >= dArr.length - 1) {
            int i3 = this.f6414i;
            if (i3 == -1 || this.f6415j < i3) {
                double d5 = this.f6413h;
                d4 = d5 + (dArr[dArr.length - 1] * (this.f6412g - d5));
                this.f6410e = -1L;
                this.f6415j++;
            } else {
                d4 = this.f6412g;
                this.f6382a = true;
            }
        } else {
            double d6 = this.f6413h;
            d4 = d6 + (dArr[iRound] * (this.f6412g - d6));
        }
        wVar.f6495f = d4;
    }
}
