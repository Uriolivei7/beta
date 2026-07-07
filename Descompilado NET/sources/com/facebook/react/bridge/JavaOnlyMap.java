package com.facebook.react.bridge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;
import x2.AbstractC0774c;

/* JADX INFO: loaded from: classes.dex */
public final class JavaOnlyMap implements ReadableMap, WritableMap {
    public static final Companion Companion = new Companion(null);
    private final Map<String, Object> backingMap;

    public static final class Companion {

        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

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
                $EnumSwitchMapping$0 = iArr;
            }
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final JavaOnlyMap deepClone(ReadableMap readableMap) {
            JavaOnlyMap javaOnlyMap = new JavaOnlyMap();
            if (readableMap == null) {
                return javaOnlyMap;
            }
            ReadableMapKeySetIterator readableMapKeySetIteratorKeySetIterator = readableMap.keySetIterator();
            while (readableMapKeySetIteratorKeySetIterator.hasNextKey()) {
                String strNextKey = readableMapKeySetIteratorKeySetIterator.nextKey();
                switch (WhenMappings.$EnumSwitchMapping$0[readableMap.getType(strNextKey).ordinal()]) {
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
                        javaOnlyMap.putMap(strNextKey, deepClone(readableMap.getMap(strNextKey)));
                        break;
                    case 6:
                        javaOnlyMap.putArray(strNextKey, JavaOnlyArray.Companion.deepClone(readableMap.getArray(strNextKey)));
                        break;
                    default:
                        throw new C0685h();
                }
            }
            return javaOnlyMap;
        }

        public final JavaOnlyMap from(Map<String, ? extends Object> map) {
            D2.h.f(map, "map");
            return new JavaOnlyMap(new Object[]{map}, null);
        }

        public final JavaOnlyMap of(Object... objArr) {
            D2.h.f(objArr, "keysAndValues");
            return new JavaOnlyMap(Arrays.copyOf(objArr, objArr.length), null);
        }

        private Companion() {
        }
    }

    public /* synthetic */ JavaOnlyMap(Object[] objArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(objArr);
    }

    public static final JavaOnlyMap deepClone(ReadableMap readableMap) {
        return Companion.deepClone(readableMap);
    }

    public static final JavaOnlyMap from(Map<String, ? extends Object> map) {
        return Companion.from(map);
    }

    public static final JavaOnlyMap of(Object... objArr) {
        return Companion.of(objArr);
    }

    @Override // com.facebook.react.bridge.WritableMap
    public WritableMap copy() {
        JavaOnlyMap javaOnlyMap = new JavaOnlyMap();
        javaOnlyMap.merge(this);
        return javaOnlyMap;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !D2.h.b(JavaOnlyMap.class, obj.getClass())) {
            return false;
        }
        return D2.h.b(this.backingMap, ((JavaOnlyMap) obj).backingMap);
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public ReadableArray getArray(String str) {
        D2.h.f(str, "name");
        return (ReadableArray) this.backingMap.get(str);
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public boolean getBoolean(String str) {
        D2.h.f(str, "name");
        Object obj = this.backingMap.get(str);
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Boolean");
        return ((Boolean) obj).booleanValue();
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public double getDouble(String str) {
        D2.h.f(str, "name");
        Object obj = this.backingMap.get(str);
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Number");
        return ((Number) obj).doubleValue();
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public Dynamic getDynamic(String str) {
        D2.h.f(str, "name");
        DynamicFromMap dynamicFromMapCreate = DynamicFromMap.create(this, str);
        D2.h.e(dynamicFromMapCreate, "create(...)");
        return dynamicFromMapCreate;
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public Iterator<Map.Entry<String, Object>> getEntryIterator() {
        return this.backingMap.entrySet().iterator();
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public int getInt(String str) {
        D2.h.f(str, "name");
        Object obj = this.backingMap.get(str);
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Number");
        return ((Number) obj).intValue();
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public long getLong(String str) {
        D2.h.f(str, "name");
        Object obj = this.backingMap.get(str);
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Number");
        return ((Number) obj).longValue();
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public ReadableMap getMap(String str) {
        D2.h.f(str, "name");
        return (ReadableMap) this.backingMap.get(str);
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public String getString(String str) {
        D2.h.f(str, "name");
        return (String) this.backingMap.get(str);
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public ReadableType getType(String str) {
        D2.h.f(str, "name");
        Object obj = this.backingMap.get(str);
        if (obj == null) {
            return ReadableType.Null;
        }
        if (obj instanceof Number) {
            return ReadableType.Number;
        }
        if (obj instanceof String) {
            return ReadableType.String;
        }
        if (obj instanceof Boolean) {
            return ReadableType.Boolean;
        }
        if (obj instanceof ReadableMap) {
            return ReadableType.Map;
        }
        if (obj instanceof ReadableArray) {
            return ReadableType.Array;
        }
        if (obj instanceof Dynamic) {
            return ((Dynamic) obj).getType();
        }
        throw new IllegalArgumentException("Invalid value " + obj + " for key " + str + " contained in JavaOnlyMap");
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public boolean hasKey(String str) {
        D2.h.f(str, "name");
        return this.backingMap.containsKey(str);
    }

    public int hashCode() {
        return this.backingMap.hashCode();
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public boolean isNull(String str) {
        D2.h.f(str, "name");
        return this.backingMap.get(str) == null;
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public ReadableMapKeySetIterator keySetIterator() {
        return new ReadableMapKeySetIterator(this) { // from class: com.facebook.react.bridge.JavaOnlyMap.keySetIterator.1
            private final Iterator<Map.Entry<String, Object>> iterator;

            {
                this.iterator = this.backingMap.entrySet().iterator();
            }

            @Override // com.facebook.react.bridge.ReadableMapKeySetIterator
            public boolean hasNextKey() {
                return this.iterator.hasNext();
            }

            @Override // com.facebook.react.bridge.ReadableMapKeySetIterator
            public String nextKey() {
                return this.iterator.next().getKey();
            }
        };
    }

    @Override // com.facebook.react.bridge.WritableMap
    public void merge(ReadableMap readableMap) {
        D2.h.f(readableMap, "source");
        this.backingMap.putAll(((JavaOnlyMap) readableMap).backingMap);
    }

    @Override // com.facebook.react.bridge.WritableMap
    public void putArray(String str, ReadableArray readableArray) {
        D2.h.f(str, "key");
        this.backingMap.put(str, readableArray);
    }

    @Override // com.facebook.react.bridge.WritableMap
    public void putBoolean(String str, boolean z3) {
        D2.h.f(str, "key");
        this.backingMap.put(str, Boolean.valueOf(z3));
    }

    @Override // com.facebook.react.bridge.WritableMap
    public void putDouble(String str, double d4) {
        D2.h.f(str, "key");
        this.backingMap.put(str, Double.valueOf(d4));
    }

    @Override // com.facebook.react.bridge.WritableMap
    public void putInt(String str, int i3) {
        D2.h.f(str, "key");
        this.backingMap.put(str, Double.valueOf(i3));
    }

    @Override // com.facebook.react.bridge.WritableMap
    public void putLong(String str, long j3) {
        D2.h.f(str, "key");
        this.backingMap.put(str, Double.valueOf(j3));
    }

    @Override // com.facebook.react.bridge.WritableMap
    public void putMap(String str, ReadableMap readableMap) {
        D2.h.f(str, "key");
        this.backingMap.put(str, readableMap);
    }

    @Override // com.facebook.react.bridge.WritableMap
    public void putNull(String str) {
        D2.h.f(str, "key");
        this.backingMap.put(str, null);
    }

    @Override // com.facebook.react.bridge.WritableMap
    public void putString(String str, String str2) {
        D2.h.f(str, "key");
        this.backingMap.put(str, str2);
    }

    public final void remove(String str) {
        D2.h.f(str, "key");
        this.backingMap.remove(str);
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public HashMap<String, Object> toHashMap() {
        return new HashMap<>(this.backingMap);
    }

    public String toString() {
        return this.backingMap.toString();
    }

    public JavaOnlyMap() {
        this.backingMap = new HashMap();
    }

    private JavaOnlyMap(Object... objArr) {
        this();
        if (objArr.length % 2 == 0) {
            int i3 = 0;
            int iB = AbstractC0774c.b(0, objArr.length - 1, 2);
            if (iB < 0) {
                return;
            }
            while (true) {
                Object objValueOf = objArr[i3 + 1];
                objValueOf = objValueOf instanceof Number ? Double.valueOf(((Number) objValueOf).doubleValue()) : objValueOf;
                Map<String, Object> map = this.backingMap;
                Object obj = objArr[i3];
                D2.h.d(obj, "null cannot be cast to non-null type kotlin.String");
                map.put((String) obj, objValueOf);
                if (i3 == iB) {
                    return;
                } else {
                    i3 += 2;
                }
            }
        } else {
            throw new IllegalArgumentException("You must provide the same number of keys and values");
        }
    }
}
