package com.facebook.react.animated;

import com.facebook.react.bridge.JavaOnlyArray;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import java.util.ArrayList;
import java.util.List;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class v extends com.facebook.react.animated.b {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final o f6487f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final List f6488g;

    private final class a extends c {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f6489c;

        public a() {
            super();
        }

        public final int c() {
            return this.f6489c;
        }

        public final void d(int i3) {
            this.f6489c = i3;
        }
    }

    private final class b extends c {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private double f6491c;

        public b() {
            super();
        }

        public final double c() {
            return this.f6491c;
        }

        public final void d(double d4) {
            this.f6491c = d4;
        }
    }

    private class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f6493a;

        public c() {
        }

        public final String a() {
            return this.f6493a;
        }

        public final void b(String str) {
            this.f6493a = str;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v3, types: [com.facebook.react.animated.v$b, com.facebook.react.animated.v$c] */
    /* JADX WARN: Type inference failed for: r4v4, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r4v5, types: [com.facebook.react.animated.v$a, com.facebook.react.animated.v$c] */
    public v(ReadableMap readableMap, o oVar) {
        List listG;
        ?? bVar;
        D2.h.f(readableMap, "config");
        D2.h.f(oVar, "nativeAnimatedNodesManager");
        this.f6487f = oVar;
        ReadableArray array = readableMap.getArray("transforms");
        if (array == null) {
            listG = AbstractC0717n.g();
        } else {
            int size = array.size();
            ArrayList arrayList = new ArrayList(size);
            for (int i3 = 0; i3 < size; i3++) {
                ReadableMap map = array.getMap(i3);
                if (map == null) {
                    throw new IllegalStateException("Required value was null.");
                }
                String string = map.getString("property");
                if (D2.h.b(map.getString("type"), "animated")) {
                    bVar = new a();
                    bVar.b(string);
                    bVar.d(map.getInt("nodeTag"));
                } else {
                    bVar = new b();
                    bVar.b(string);
                    bVar.d(map.getDouble("value"));
                }
                arrayList.add(bVar);
            }
            listG = arrayList;
        }
        this.f6488g = listG;
    }

    @Override // com.facebook.react.animated.b
    public String e() {
        return "TransformAnimatedNode[" + this.f6381d + "]: transformConfigs: " + this.f6488g;
    }

    public final void i(JavaOnlyMap javaOnlyMap) {
        double dC;
        D2.h.f(javaOnlyMap, "propsMap");
        int size = this.f6488g.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i3 = 0; i3 < size; i3++) {
            c cVar = (c) this.f6488g.get(i3);
            if (cVar instanceof a) {
                com.facebook.react.animated.b bVarL = this.f6487f.l(((a) cVar).c());
                if (bVarL == null) {
                    throw new IllegalArgumentException("Mapped style node does not exist");
                }
                if (!(bVarL instanceof w)) {
                    throw new IllegalArgumentException("Unsupported type of node used as a transform child node " + bVarL.getClass());
                }
                dC = ((w) bVarL).l();
            } else {
                D2.h.d(cVar, "null cannot be cast to non-null type com.facebook.react.animated.TransformAnimatedNode.StaticTransformConfig");
                dC = ((b) cVar).c();
            }
            arrayList.add(JavaOnlyMap.Companion.of(cVar.a(), Double.valueOf(dC)));
        }
        javaOnlyMap.putArray("transform", JavaOnlyArray.Companion.from(arrayList));
    }
}
