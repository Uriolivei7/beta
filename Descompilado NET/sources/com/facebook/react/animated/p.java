package com.facebook.react.animated;

import com.facebook.react.bridge.JavaOnlyArray;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;

/* JADX INFO: loaded from: classes.dex */
public final class p extends com.facebook.react.animated.b {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final a f6450h = new a(null);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final o f6451f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final JavaOnlyMap f6452g;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f6453a;

        static {
            int[] iArr = new int[ReadableType.values().length];
            try {
                iArr[ReadableType.Null.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[ReadableType.Boolean.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[ReadableType.Number.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[ReadableType.String.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[ReadableType.Map.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[ReadableType.Array.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            f6453a = iArr;
        }
    }

    public p(ReadableMap readableMap, o oVar) {
        D2.h.f(readableMap, "config");
        D2.h.f(oVar, "nativeAnimatedNodesManager");
        this.f6451f = oVar;
        this.f6452g = JavaOnlyMap.Companion.deepClone(readableMap);
    }

    private final JavaOnlyArray j(ReadableArray readableArray) {
        if (readableArray == null) {
            return null;
        }
        JavaOnlyArray javaOnlyArray = new JavaOnlyArray();
        int size = readableArray.size();
        for (int i3 = 0; i3 < size; i3++) {
            switch (b.f6453a[readableArray.getType(i3).ordinal()]) {
                case 1:
                    javaOnlyArray.pushNull();
                    break;
                case 2:
                    javaOnlyArray.pushBoolean(readableArray.getBoolean(i3));
                    break;
                case 3:
                    javaOnlyArray.pushDouble(readableArray.getDouble(i3));
                    break;
                case 4:
                    javaOnlyArray.pushString(readableArray.getString(i3));
                    break;
                case 5:
                    ReadableMap map = readableArray.getMap(i3);
                    if (map != null && map.hasKey("nodeTag") && map.getType("nodeTag") == ReadableType.Number) {
                        com.facebook.react.animated.b bVarL = this.f6451f.l(map.getInt("nodeTag"));
                        if (bVarL == null) {
                            throw new IllegalArgumentException("Mapped value node does not exist");
                        }
                        if (bVarL instanceof w) {
                            w wVar = (w) bVarL;
                            Object objK = wVar.k();
                            if (objK instanceof Integer) {
                                javaOnlyArray.pushInt(((Number) objK).intValue());
                            } else if (objK instanceof String) {
                                javaOnlyArray.pushString((String) objK);
                            } else {
                                javaOnlyArray.pushDouble(wVar.l());
                            }
                        } else if (bVarL instanceof f) {
                            javaOnlyArray.pushInt(((f) bVarL).i());
                        }
                    } else {
                        javaOnlyArray.pushMap(k(readableArray.getMap(i3)));
                    }
                    break;
                case 6:
                    javaOnlyArray.pushArray(j(readableArray.getArray(i3)));
                    break;
                default:
                    throw new C0685h();
            }
        }
        return javaOnlyArray;
    }

    private final JavaOnlyMap k(ReadableMap readableMap) {
        if (readableMap == null) {
            return null;
        }
        JavaOnlyMap javaOnlyMap = new JavaOnlyMap();
        ReadableMapKeySetIterator readableMapKeySetIteratorKeySetIterator = readableMap.keySetIterator();
        while (readableMapKeySetIteratorKeySetIterator.hasNextKey()) {
            String strNextKey = readableMapKeySetIteratorKeySetIterator.nextKey();
            switch (b.f6453a[readableMap.getType(strNextKey).ordinal()]) {
                case 1:
                    javaOnlyMap.putNull(strNextKey);
                    break;
                case 2:
                    javaOnlyMap.putBoolean(strNextKey, readableMap.getBoolean(strNextKey));
                    break;
                case 3:
                    javaOnlyMap.putDouble(strNextKey, readableMap.getDouble(strNextKey));
                    break;
                case 4:
                    javaOnlyMap.putString(strNextKey, readableMap.getString(strNextKey));
                    break;
                case 5:
                    ReadableMap map = readableMap.getMap(strNextKey);
                    if (map != null && map.hasKey("nodeTag") && map.getType("nodeTag") == ReadableType.Number) {
                        com.facebook.react.animated.b bVarL = this.f6451f.l(map.getInt("nodeTag"));
                        if (bVarL == null) {
                            throw new IllegalArgumentException("Mapped value node does not exist");
                        }
                        if (bVarL instanceof w) {
                            w wVar = (w) bVarL;
                            Object objK = wVar.k();
                            if (objK instanceof Integer) {
                                javaOnlyMap.putInt(strNextKey, ((Number) objK).intValue());
                            } else if (!(objK instanceof String)) {
                                javaOnlyMap.putDouble(strNextKey, wVar.l());
                            } else {
                                javaOnlyMap.putString(strNextKey, (String) objK);
                            }
                        } else if (bVarL instanceof f) {
                            javaOnlyMap.putInt(strNextKey, ((f) bVarL).i());
                        }
                    } else {
                        javaOnlyMap.putMap(strNextKey, k(map));
                    }
                    break;
                case 6:
                    javaOnlyMap.putArray(strNextKey, j(readableMap.getArray(strNextKey)));
                    break;
                default:
                    throw new C0685h();
            }
        }
        return javaOnlyMap;
    }

    @Override // com.facebook.react.animated.b
    public String e() {
        return "ObjectAnimatedNode[" + this.f6381d + "]: mConfig: " + this.f6452g;
    }

    public final void i(String str, JavaOnlyMap javaOnlyMap) {
        D2.h.f(str, "propKey");
        D2.h.f(javaOnlyMap, "propsMap");
        ReadableType type = this.f6452g.getType("value");
        if (type == ReadableType.Map) {
            javaOnlyMap.putMap(str, k(this.f6452g.getMap("value")));
        } else {
            if (type != ReadableType.Array) {
                throw new IllegalArgumentException("Invalid value type for ObjectAnimatedNode");
            }
            javaOnlyMap.putArray(str, j(this.f6452g.getArray("value")));
        }
    }
}
