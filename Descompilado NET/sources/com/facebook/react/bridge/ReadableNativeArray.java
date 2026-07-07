package com.facebook.react.bridge;

import java.util.ArrayList;
import java.util.Arrays;
import kotlin.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.AbstractC0681d;
import r2.C0685h;
import r2.EnumC0684g;
import s2.AbstractC0711h;

/* JADX INFO: loaded from: classes.dex */
public class ReadableNativeArray extends NativeArray implements ReadableArray {
    private static final Companion Companion = new Companion(null);
    private static int jniPassCounter;
    private final Lazy localArray$delegate;
    private final Lazy localTypeArray$delegate;

    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int getJNIPassCounter() {
            return ReadableNativeArray.jniPassCounter;
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

    protected ReadableNativeArray() {
        EnumC0684g enumC0684g = EnumC0684g.f10565b;
        this.localArray$delegate = AbstractC0681d.b(enumC0684g, new C2.a() { // from class: com.facebook.react.bridge.p
            @Override // C2.a
            public final Object a() {
                return ReadableNativeArray.localArray_delegate$lambda$0(this.f6506b);
            }
        });
        this.localTypeArray$delegate = AbstractC0681d.b(enumC0684g, new C2.a() { // from class: com.facebook.react.bridge.q
            @Override // C2.a
            public final Object a() {
                return ReadableNativeArray.localTypeArray_delegate$lambda$1(this.f6507b);
            }
        });
    }

    public static final int getJNIPassCounter() {
        return Companion.getJNIPassCounter();
    }

    private final Object[] getLocalArray() {
        return (Object[]) this.localArray$delegate.getValue();
    }

    private final ReadableType[] getLocalTypeArray() {
        Object value = this.localTypeArray$delegate.getValue();
        D2.h.e(value, "getValue(...)");
        return (ReadableType[]) value;
    }

    private final native Object[] importArray();

    private final native Object[] importTypeArray();

    /* JADX INFO: Access modifiers changed from: private */
    public static final Object[] localArray_delegate$lambda$0(ReadableNativeArray readableNativeArray) {
        jniPassCounter++;
        return readableNativeArray.importArray();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ReadableType[] localTypeArray_delegate$lambda$1(ReadableNativeArray readableNativeArray) {
        jniPassCounter++;
        Object[] objArrImportTypeArray = readableNativeArray.importTypeArray();
        return (ReadableType[]) Arrays.copyOf(objArrImportTypeArray, objArrImportTypeArray.length, ReadableType[].class);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ReadableNativeArray) {
            return AbstractC0711h.c(getLocalArray(), ((ReadableNativeArray) obj).getLocalArray());
        }
        return false;
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public boolean getBoolean(int i3) {
        Object obj = getLocalArray()[i3];
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Boolean");
        return ((Boolean) obj).booleanValue();
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public double getDouble(int i3) {
        Object obj = getLocalArray()[i3];
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Double");
        return ((Double) obj).doubleValue();
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public Dynamic getDynamic(int i3) {
        DynamicFromArray dynamicFromArrayCreate = DynamicFromArray.create(this, i3);
        D2.h.e(dynamicFromArrayCreate, "create(...)");
        return dynamicFromArrayCreate;
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public int getInt(int i3) {
        Object obj = getLocalArray()[i3];
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Double");
        return (int) ((Double) obj).doubleValue();
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public long getLong(int i3) {
        Object obj = getLocalArray()[i3];
        D2.h.d(obj, "null cannot be cast to non-null type kotlin.Long");
        return ((Long) obj).longValue();
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public String getString(int i3) {
        return (String) getLocalArray()[i3];
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public ReadableType getType(int i3) {
        return getLocalTypeArray()[i3];
    }

    public int hashCode() {
        return getLocalArray().hashCode();
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public boolean isNull(int i3) {
        return getLocalArray()[i3] == null;
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public int size() {
        return getLocalArray().length;
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public ArrayList<Object> toArrayList() {
        ArrayList<Object> arrayList = new ArrayList<>();
        int size = size();
        for (int i3 = 0; i3 < size; i3++) {
            switch (WhenMappings.$EnumSwitchMapping$0[getType(i3).ordinal()]) {
                case 1:
                    arrayList.add(null);
                    break;
                case 2:
                    arrayList.add(Boolean.valueOf(getBoolean(i3)));
                    break;
                case 3:
                    arrayList.add(Double.valueOf(getDouble(i3)));
                    break;
                case 4:
                    arrayList.add(getString(i3));
                    break;
                case 5:
                    ReadableNativeMap map = getMap(i3);
                    arrayList.add(map != null ? map.toHashMap() : null);
                    break;
                case 6:
                    ReadableNativeArray array = getArray(i3);
                    arrayList.add(array != null ? array.toArrayList() : null);
                    break;
                default:
                    throw new C0685h();
            }
        }
        return arrayList;
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public ReadableNativeArray getArray(int i3) {
        return (ReadableNativeArray) getLocalArray()[i3];
    }

    @Override // com.facebook.react.bridge.ReadableArray
    public ReadableNativeMap getMap(int i3) {
        return (ReadableNativeMap) getLocalArray()[i3];
    }
}
