package com.facebook.react.bridge;

import a1.C0210a;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.AbstractC0681d;
import r2.C0685h;
import r2.EnumC0684g;

/* JADX INFO: loaded from: classes.dex */
public class ReadableNativeMap extends NativeMap implements ReadableMap {
    private static final Companion Companion = new Companion(null);
    private static int jniPassCounter;
    private final Lazy keys$delegate;
    private final Lazy localMap$delegate;
    private final Lazy localTypeMap$delegate;

    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int getJNIPassCounter() {
            return ReadableNativeMap.jniPassCounter;
        }

        private Companion() {
        }
    }

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

    protected ReadableNativeMap() {
        EnumC0684g enumC0684g = EnumC0684g.f10565b;
        this.keys$delegate = AbstractC0681d.b(enumC0684g, new C2.a() { // from class: com.facebook.react.bridge.r
            @Override // C2.a
            public final Object a() {
                return ReadableNativeMap.keys_delegate$lambda$1(this.f6512b);
            }
        });
        this.localMap$delegate = AbstractC0681d.b(enumC0684g, new C2.a() { // from class: com.facebook.react.bridge.s
            @Override // C2.a
            public final Object a() {
                return ReadableNativeMap.localMap_delegate$lambda$2(this.f6513b);
            }
        });
        this.localTypeMap$delegate = AbstractC0681d.b(enumC0684g, new C2.a() { // from class: com.facebook.react.bridge.t
            @Override // C2.a
            public final Object a() {
                return ReadableNativeMap.localTypeMap_delegate$lambda$3(this.f6514b);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final /* synthetic */ <T> T checkInstance(String str, Object obj, Class<T> cls) {
        D2.h.i(2, "T");
        if (obj != 0) {
            return obj;
        }
        throw new UnexpectedNativeTypeException("Value for " + str + " cannot be cast from " + (obj != 0 ? obj.getClass().getSimpleName() : "NULL") + " to " + cls.getSimpleName());
    }

    public static final int getJNIPassCounter() {
        return Companion.getJNIPassCounter();
    }

    private final String[] getKeys() {
        return (String[]) this.keys$delegate.getValue();
    }

    private final HashMap<String, Object> getLocalMap() {
        return (HashMap) this.localMap$delegate.getValue();
    }

    private final HashMap<String, ReadableType> getLocalTypeMap() {
        return (HashMap) this.localTypeMap$delegate.getValue();
    }

    private final Object getNullableValue(String str) {
        return getLocalMap().get(str);
    }

    private final Object getValue(String str) {
        if (!hasKey(str)) {
            throw new NoSuchKeyException(str);
        }
        Object objC = C0210a.c(getLocalMap().get(str));
        D2.h.e(objC, "assertNotNull(...)");
        return objC;
    }

    private final native String[] importKeys();

    private final native Object[] importTypes();

    private final native Object[] importValues();

    /* JADX INFO: Access modifiers changed from: private */
    public static final String[] keys_delegate$lambda$1(ReadableNativeMap readableNativeMap) {
        String[] strArrImportKeys = readableNativeMap.importKeys();
        jniPassCounter++;
        return strArrImportKeys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final HashMap localMap_delegate$lambda$2(ReadableNativeMap readableNativeMap) {
        int length = readableNativeMap.getKeys().length;
        HashMap map = new HashMap(length);
        Object[] objArrImportValues = readableNativeMap.importValues();
        jniPassCounter++;
        for (int i3 = 0; i3 < length; i3++) {
            map.put(readableNativeMap.getKeys()[i3], objArrImportValues[i3]);
        }
        return map;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final HashMap localTypeMap_delegate$lambda$3(ReadableNativeMap readableNativeMap) {
        int length = readableNativeMap.getKeys().length;
        HashMap map = new HashMap(length);
        Object[] objArrImportTypes = readableNativeMap.importTypes();
        jniPassCounter++;
        for (int i3 = 0; i3 < length; i3++) {
            String str = readableNativeMap.getKeys()[i3];
            Object obj = objArrImportTypes[i3];
            D2.h.d(obj, "null cannot be cast to non-null type com.facebook.react.bridge.ReadableType");
            map.put(str, (ReadableType) obj);
        }
        return map;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ReadableNativeMap) {
            return D2.h.b(getLocalMap(), ((ReadableNativeMap) obj).getLocalMap());
        }
        return false;
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public ReadableArray getArray(String str) {
        D2.h.f(str, "name");
        Object nullableValue = getNullableValue(str);
        ReadableArray readableArray = null;
        if (nullableValue != null) {
            readableArray = (ReadableArray) (nullableValue instanceof ReadableArray ? nullableValue : null);
            if (readableArray == null) {
                throw new UnexpectedNativeTypeException("Value for " + str + " cannot be cast from " + nullableValue.getClass().getSimpleName() + " to " + ReadableArray.class.getSimpleName());
            }
        }
        return readableArray;
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public boolean getBoolean(String str) {
        D2.h.f(str, "name");
        Class cls = Boolean.TYPE;
        Object value = getValue(str);
        Boolean bool = (Boolean) (!(value instanceof Boolean) ? null : value);
        if (bool != null) {
            return bool.booleanValue();
        }
        throw new UnexpectedNativeTypeException("Value for " + str + " cannot be cast from " + (value != null ? value.getClass().getSimpleName() : "NULL") + " to " + cls.getSimpleName());
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public double getDouble(String str) {
        D2.h.f(str, "name");
        Class cls = Double.TYPE;
        Object value = getValue(str);
        Double d4 = (Double) (!(value instanceof Double) ? null : value);
        if (d4 != null) {
            return d4.doubleValue();
        }
        throw new UnexpectedNativeTypeException("Value for " + str + " cannot be cast from " + (value != null ? value.getClass().getSimpleName() : "NULL") + " to " + cls.getSimpleName());
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
        ReadableNativeMap$entryIterator$1$1 readableNativeMap$entryIterator$1$1;
        synchronized (this) {
            String[] keys = getKeys();
            Object[] objArrImportValues = importValues();
            jniPassCounter++;
            readableNativeMap$entryIterator$1$1 = new ReadableNativeMap$entryIterator$1$1(keys, objArrImportValues);
        }
        return readableNativeMap$entryIterator$1$1;
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public int getInt(String str) {
        D2.h.f(str, "name");
        Class cls = Double.TYPE;
        Object value = getValue(str);
        Double d4 = (Double) (!(value instanceof Double) ? null : value);
        if (d4 != null) {
            return (int) d4.doubleValue();
        }
        throw new UnexpectedNativeTypeException("Value for " + str + " cannot be cast from " + (value != null ? value.getClass().getSimpleName() : "NULL") + " to " + cls.getSimpleName());
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public long getLong(String str) {
        D2.h.f(str, "name");
        Class cls = Long.TYPE;
        Object value = getValue(str);
        Long l3 = (Long) (!(value instanceof Long) ? null : value);
        if (l3 != null) {
            return l3.longValue();
        }
        throw new UnexpectedNativeTypeException("Value for " + str + " cannot be cast from " + (value != null ? value.getClass().getSimpleName() : "NULL") + " to " + cls.getSimpleName());
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public String getString(String str) {
        D2.h.f(str, "name");
        Object nullableValue = getNullableValue(str);
        String str2 = null;
        if (nullableValue != null) {
            str2 = (String) (nullableValue instanceof String ? nullableValue : null);
            if (str2 == null) {
                throw new UnexpectedNativeTypeException("Value for " + str + " cannot be cast from " + nullableValue.getClass().getSimpleName() + " to " + String.class.getSimpleName());
            }
        }
        return str2;
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public ReadableType getType(String str) {
        D2.h.f(str, "name");
        ReadableType readableType = getLocalTypeMap().get(str);
        if (readableType != null) {
            return readableType;
        }
        throw new NoSuchKeyException(str);
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public boolean hasKey(String str) {
        D2.h.f(str, "name");
        return getLocalMap().containsKey(str);
    }

    public int hashCode() {
        return getLocalMap().hashCode();
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public boolean isNull(String str) {
        D2.h.f(str, "name");
        if (getLocalMap().containsKey(str)) {
            return getLocalMap().get(str) == null;
        }
        throw new NoSuchKeyException(str);
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public ReadableMapKeySetIterator keySetIterator() {
        final String[] keys = getKeys();
        return new ReadableMapKeySetIterator() { // from class: com.facebook.react.bridge.ReadableNativeMap.keySetIterator.1
            private int currentIndex;

            public final int getCurrentIndex() {
                return this.currentIndex;
            }

            @Override // com.facebook.react.bridge.ReadableMapKeySetIterator
            public boolean hasNextKey() {
                return this.currentIndex < keys.length;
            }

            @Override // com.facebook.react.bridge.ReadableMapKeySetIterator
            public String nextKey() {
                String[] strArr = keys;
                int i3 = this.currentIndex;
                this.currentIndex = i3 + 1;
                return strArr[i3];
            }

            public final void setCurrentIndex(int i3) {
                this.currentIndex = i3;
            }
        };
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>(getLocalMap());
        for (String str : map.keySet()) {
            D2.h.d(str, "null cannot be cast to non-null type kotlin.String");
            String str2 = str;
            switch (WhenMappings.$EnumSwitchMapping$0[getType(str2).ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                    break;
                case 5:
                    map.put(str2, ((ReadableNativeMap) C0210a.c(getMap(str2))).toHashMap());
                    break;
                case 6:
                    map.put(str2, ((ReadableArray) C0210a.c(getArray(str2))).toArrayList());
                    break;
                default:
                    throw new C0685h();
            }
        }
        return map;
    }

    private final /* synthetic */ <T> T getNullableValue(String str, Class<T> cls) {
        T t3 = (T) getNullableValue(str);
        if (t3 == null) {
            return null;
        }
        D2.h.i(2, "T");
        return t3;
    }

    @Override // com.facebook.react.bridge.ReadableMap
    public ReadableNativeMap getMap(String str) {
        D2.h.f(str, "name");
        Object nullableValue = getNullableValue(str);
        ReadableNativeMap readableNativeMap = null;
        if (nullableValue != null) {
            readableNativeMap = (ReadableNativeMap) (nullableValue instanceof ReadableNativeMap ? nullableValue : null);
            if (readableNativeMap == null) {
                throw new UnexpectedNativeTypeException("Value for " + str + " cannot be cast from " + nullableValue.getClass().getSimpleName() + " to " + ReadableNativeMap.class.getSimpleName());
            }
        }
        return readableNativeMap;
    }

    private final /* synthetic */ <T> T getValue(String str, Class<T> cls) {
        T t3 = (T) getValue(str);
        D2.h.i(2, "T");
        if (t3 != null) {
            return t3;
        }
        throw new UnexpectedNativeTypeException("Value for " + str + " cannot be cast from " + (t3 != null ? t3.getClass().getSimpleName() : "NULL") + " to " + cls.getSimpleName());
    }
}
