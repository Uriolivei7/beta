package com.facebook.react.animated;

import com.facebook.react.bridge.JSApplicationCausedNativeException;
import com.facebook.react.bridge.ReadableMap;

/* JADX INFO: loaded from: classes.dex */
public final class l extends w {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final o f6433i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final int f6434j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final double f6435k;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public l(ReadableMap readableMap, o oVar) {
        super(null, 1, null);
        D2.h.f(readableMap, "config");
        D2.h.f(oVar, "nativeAnimatedNodesManager");
        this.f6433i = oVar;
        this.f6434j = readableMap.getInt("input");
        this.f6435k = readableMap.getDouble("modulus");
    }

    @Override // com.facebook.react.animated.w, com.facebook.react.animated.b
    public String e() {
        return "NativeAnimatedNodesManager[" + this.f6381d + "] inputNode: " + this.f6434j + " modulus: " + this.f6435k + " super: " + super.e();
    }

    @Override // com.facebook.react.animated.b
    public void h() {
        b bVarL = this.f6433i.l(this.f6434j);
        if (!(bVarL instanceof w)) {
            throw new JSApplicationCausedNativeException("Illegal node ID set as an input for Animated.modulus node");
        }
        double dL = ((w) bVarL).l();
        double d4 = this.f6435k;
        this.f6495f = ((dL % d4) + d4) % d4;
    }
}
