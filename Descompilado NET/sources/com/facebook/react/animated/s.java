package com.facebook.react.animated;

import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import java.util.Map;
import s2.AbstractC0696D;

/* JADX INFO: loaded from: classes.dex */
public final class s extends b {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final o f6478f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final Map f6479g;

    public s(ReadableMap readableMap, o oVar) {
        D2.h.f(readableMap, "config");
        D2.h.f(oVar, "nativeAnimatedNodesManager");
        this.f6478f = oVar;
        ReadableMap map = readableMap.getMap("style");
        ReadableMapKeySetIterator readableMapKeySetIteratorKeySetIterator = map != null ? map.keySetIterator() : null;
        Map mapB = AbstractC0696D.b();
        while (readableMapKeySetIteratorKeySetIterator != null && readableMapKeySetIteratorKeySetIterator.hasNextKey()) {
            String strNextKey = readableMapKeySetIteratorKeySetIterator.nextKey();
            mapB.put(strNextKey, Integer.valueOf(map.getInt(strNextKey)));
        }
        this.f6479g = AbstractC0696D.a(mapB);
    }

    @Override // com.facebook.react.animated.b
    public String e() {
        return "StyleAnimatedNode[" + this.f6381d + "] mPropMapping: " + this.f6479g;
    }

    public final void i(JavaOnlyMap javaOnlyMap) {
        D2.h.f(javaOnlyMap, "propsMap");
        for (Map.Entry entry : this.f6479g.entrySet()) {
            String str = (String) entry.getKey();
            b bVarL = this.f6478f.l(((Number) entry.getValue()).intValue());
            if (bVarL == null) {
                throw new IllegalArgumentException("Mapped style node does not exist");
            }
            if (bVarL instanceof v) {
                ((v) bVarL).i(javaOnlyMap);
            } else if (bVarL instanceof w) {
                w wVar = (w) bVarL;
                Object objK = wVar.k();
                if (objK instanceof Integer) {
                    javaOnlyMap.putInt(str, ((Number) objK).intValue());
                } else if (objK instanceof String) {
                    javaOnlyMap.putString(str, (String) objK);
                } else {
                    javaOnlyMap.putDouble(str, wVar.l());
                }
            } else if (bVarL instanceof f) {
                javaOnlyMap.putInt(str, ((f) bVarL).i());
            } else {
                if (!(bVarL instanceof p)) {
                    throw new IllegalArgumentException("Unsupported type of node used in property node " + bVarL.getClass());
                }
                ((p) bVarL).i(str, javaOnlyMap);
            }
        }
    }
}
