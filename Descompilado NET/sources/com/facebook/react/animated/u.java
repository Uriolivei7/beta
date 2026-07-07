package com.facebook.react.animated;

import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReadableMap;

/* JADX INFO: loaded from: classes.dex */
public final class u extends b {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final o f6482f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final JavaOnlyMap f6483g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final int f6484h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final int f6485i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final int f6486j;

    public u(ReadableMap readableMap, o oVar) {
        D2.h.f(readableMap, "config");
        D2.h.f(oVar, "nativeAnimatedNodesManager");
        this.f6482f = oVar;
        this.f6483g = JavaOnlyMap.Companion.deepClone(readableMap.getMap("animationConfig"));
        this.f6484h = readableMap.getInt("animationId");
        this.f6485i = readableMap.getInt("toValue");
        this.f6486j = readableMap.getInt("value");
    }

    @Override // com.facebook.react.animated.b
    public String e() {
        return "TrackingAnimatedNode[" + this.f6381d + "]: animationID: " + this.f6484h + " toValueNode: " + this.f6485i + " valueNode: " + this.f6486j + " animationConfig: " + this.f6483g;
    }

    @Override // com.facebook.react.animated.b
    public void h() {
        b bVarL = this.f6482f.l(this.f6485i);
        w wVar = bVarL instanceof w ? (w) bVarL : null;
        if (wVar != null) {
            this.f6483g.putDouble("toValue", wVar.l());
        } else {
            this.f6483g.putNull("toValue");
        }
        this.f6482f.x(this.f6484h, this.f6486j, this.f6483g, null);
    }
}
