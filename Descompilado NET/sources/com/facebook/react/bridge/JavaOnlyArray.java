package com.facebook.react.bridge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class JavaOnlyArray implements ReadableArray, WritableArray {
    public static final Companion Companion = new Companion(null);
    private final List<Object> backingList;

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

        public final JavaOnlyArray deepClone(ReadableArray readableArray) {
            JavaOnlyArray javaOnlyArray = new JavaOnlyArray();
            if (readableArray == null) {
                return javaOnlyArray;
            }
            int size = readableArray.size();
            for (int i3 = 0; i3 < size; i3++) {
                switch (WhenMappings.$EnumSwitchMapping$0[readableArray.getType(i3).ordinal()]) {
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
                        javaOnlyArray.pushMap(JavaOnlyMap.Companion.deepClone(readableArray.getMap(i3)));
                        break;
                    case 6:
                        javaOnlyArray.pushArray(JavaOnlyArray.Companion.deepClone(readableArray.getArray(i3)));
                        break;
                    default:
                        throw new C0685h();
                }
            }
            return javaOnlyArray;
        }

        public final JavaOnlyArray from(List<?> list) {
            D2.h.f(list, "list");
            return new JavaOnlyArray(list, (DefaultConstructorMarker) null);
        }

        public final JavaOnlyArray of(Object... objArr) {
            D2.h.f(objArr, "values");
            return new JavaOnlyArray(Arrays.copyOf(objArr, objArr.length), (DefaultConstructorMarker) null);
        }

        private Companion() {
        }
    }

    public /* synthetic */ JavaOnlyArray(List list, DefaultConstructorMarker defaultConstructorMarker) {
        this((List<?>) list);
    }

    public static final JavaOnlyArray deepClone(ReadableArray readableArray) {
        return Companion.deepClone(readableArray);
    }

    public static final JavaOnlyArray from(List<?> list) {
        return Companion.from(list);
    }

    public static final JavaOnlyArray of(Object... objArr) {
        return Companion.of(objArr);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !D2.h.b(JavaOnlyArray.class, obj.getClass())) {
            return false;
        }
        return D2.h.b(this.backingList, ((JavaOnlyArray) obj).backingList);
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public ReadableArray getArray(int i3) {
        return (ReadableArray) this.backingList.get(i3);
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public boolean getBoolean(int i3) {
        Object obj = this.backingList.get(i3);
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Boolean");
        return ((Boolean) obj).booleanValue();
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public double getDouble(int i3) {
        Object obj = this.backingList.get(i3);
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Number");
        return ((Number) obj).doubleValue();
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public Dynamic getDynamic(int i3) {
        DynamicFromArray dynamicFromArrayCreate = DynamicFromArray.create(this, i3);
        D2.h.e(dynamicFromArrayCreate, "create(...)");
        return dynamicFromArrayCreate;
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public int getInt(int i3) {
        Object obj = this.backingList.get(i3);
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Number");
        return ((Number) obj).intValue();
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public long getLong(int i3) {
        Object obj = this.backingList.get(i3);
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Number");
        return ((Number) obj).longValue();
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public ReadableMap getMap(int i3) {
        return (ReadableMap) this.backingList.get(i3);
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public String getString(int i3) {
        return (String) this.backingList.get(i3);
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public ReadableType getType(int i3) {
        Object obj = this.backingList.get(i3);
        if (obj == null) {
            return ReadableType.Null;
        }
        if (obj instanceof Boolean) {
            return ReadableType.Boolean;
        }
        if ((obj instanceof Double) || (obj instanceof Float) || (obj instanceof Integer) || (obj instanceof Long)) {
            return ReadableType.Number;
        }
        if (obj instanceof String) {
            return ReadableType.String;
        }
        if (obj instanceof ReadableArray) {
            return ReadableType.Array;
        }
        if (obj instanceof ReadableMap) {
            return ReadableType.Map;
        }
        throw new IllegalStateException("Invalid type " + obj.getClass() + ")");
    }

    public int hashCode() {
        return this.backingList.hashCode();
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public boolean isNull(int i3) {
        return this.backingList.get(i3) == null;
    }

    @Override // com.facebook.react.bridge.WritableArray
    public void pushArray(ReadableArray readableArray) {
        this.backingList.add(readableArray);
    }

    @Override // com.facebook.react.bridge.WritableArray
    public void pushBoolean(boolean z3) {
        this.backingList.add(Boolean.valueOf(z3));
    }

    @Override // com.facebook.react.bridge.WritableArray
    public void pushDouble(double d4) {
        this.backingList.add(Double.valueOf(d4));
    }

    @Override // com.facebook.react.bridge.WritableArray
    public void pushInt(int i3) {
        this.backingList.add(Double.valueOf(i3));
    }

    @Override // com.facebook.react.bridge.WritableArray
    public void pushLong(long j3) {
        this.backingList.add(Double.valueOf(j3));
    }

    @Override // com.facebook.react.bridge.WritableArray
    public void pushMap(ReadableMap readableMap) {
        this.backingList.add(readableMap);
    }

    @Override // com.facebook.react.bridge.WritableArray
    public void pushNull() {
        this.backingList.add(null);
    }

    @Override // com.facebook.react.bridge.WritableArray
    public void pushString(String str) {
        this.backingList.add(str);
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public int size() {
        return this.backingList.size();
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public ArrayList<Object> toArrayList() {
        return new ArrayList<>(this.backingList);
    }

    public String toString() {
        return this.backingList.toString();
    }

    public /* synthetic */ JavaOnlyArray(Object[] objArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(objArr);
    }

    private JavaOnlyArray(Object... objArr) {
        this.backingList = AbstractC0717n.l(Arrays.copyOf(objArr, objArr.length));
    }

    private JavaOnlyArray(List<?> list) {
        this.backingList = new ArrayList(list);
    }

    public JavaOnlyArray() {
        this.backingList = new ArrayList();
    }
}
