package com.facebook.react.animated;

import com.facebook.react.bridge.JSApplicationCausedNativeException;
import com.facebook.react.bridge.ReadableMap;

/* JADX INFO: loaded from: classes.dex */
public final class h extends w {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final o f6402i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final int f6403j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final double f6404k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final double f6405l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private double f6406m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h(ReadableMap readableMap, o oVar) {
        super(null, 1, null);
        D2.h.f(readableMap, "config");
        D2.h.f(oVar, "nativeAnimatedNodesManager");
        this.f6402i = oVar;
        this.f6403j = readableMap.getInt("input");
        this.f6404k = readableMap.getDouble("min");
        this.f6405l = readableMap.getDouble("max");
        this.f6495f = this.f6406m;
    }

    private final double o() {
        b bVarL = this.f6402i.l(this.f6403j);
        if (bVarL == null || !(bVarL instanceof w)) {
            throw new JSApplicationCausedNativeException("Illegal node ID set as an input for Animated.DiffClamp node");
        }
        return ((w) bVarL).l();
    }

    @Override // com.facebook.react.animated.w, com.facebook.react.animated.b
    public String e() {
        return "DiffClampAnimatedNode[" + this.f6381d + "]: InputNodeTag: " + this.f6403j + " min: " + this.f6404k + " max: " + this.f6405l + " lastValue: " + this.f6406m + " super: " + super.e();
    }

    @Override // com.facebook.react.animated.b
    public void h() {
        double dO = o();
        double d4 = dO - this.f6406m;
        this.f6406m = dO;
        this.f6495f = Math.min(Math.max(this.f6495f + d4, this.f6404k), this.f6405l);
    }
}
