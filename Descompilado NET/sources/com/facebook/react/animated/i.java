package com.facebook.react.animated;

import com.facebook.react.bridge.JSApplicationCausedNativeException;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

/* JADX INFO: loaded from: classes.dex */
public final class i extends w {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final o f6407i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final int[] f6408j;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public i(ReadableMap readableMap, o oVar) {
        int[] iArr;
        super(null, 1, null);
        D2.h.f(readableMap, "config");
        D2.h.f(oVar, "nativeAnimatedNodesManager");
        this.f6407i = oVar;
        ReadableArray array = readableMap.getArray("input");
        if (array == null) {
            iArr = new int[0];
        } else {
            int size = array.size();
            int[] iArr2 = new int[size];
            for (int i3 = 0; i3 < size; i3++) {
                iArr2[i3] = array.getInt(i3);
            }
            iArr = iArr2;
        }
        this.f6408j = iArr;
    }

    @Override // com.facebook.react.animated.w, com.facebook.react.animated.b
    public String e() {
        return "DivisionAnimatedNode[" + this.f6381d + "]: input nodes: " + this.f6408j + " - super: " + super.e();
    }

    @Override // com.facebook.react.animated.b
    public void h() {
        int[] iArr = this.f6408j;
        int length = iArr.length;
        int i3 = 0;
        int i4 = 0;
        while (i3 < length) {
            int i5 = i4 + 1;
            b bVarL = this.f6407i.l(iArr[i3]);
            if (bVarL == null || !(bVarL instanceof w)) {
                throw new JSApplicationCausedNativeException("Illegal node ID set as an input for Animated.divide node with Animated ID " + this.f6381d);
            }
            double d4 = ((w) bVarL).f6495f;
            if (i4 == 0) {
                this.f6495f = d4;
            } else {
                if (d4 == 0.0d) {
                    throw new JSApplicationCausedNativeException("Detected a division by zero in Animated.divide node with Animated ID " + this.f6381d);
                }
                this.f6495f /= d4;
            }
            i3++;
            i4 = i5;
        }
    }
}
