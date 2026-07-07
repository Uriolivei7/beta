package com.facebook.react.uimanager;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

/* JADX INFO: loaded from: classes.dex */
public class E0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static ThreadLocal f7240a = new a();

    class a extends ThreadLocal {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public double[] initialValue() {
            return new double[16];
        }
    }

    static /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f7241a;

        static {
            int[] iArr = new int[ReadableType.values().length];
            f7241a = iArr;
            try {
                iArr[ReadableType.Number.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f7241a[ReadableType.String.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private static double a(ReadableMap readableMap, String str) {
        double d4;
        boolean z3 = true;
        if (readableMap.getType(str) == ReadableType.String) {
            String string = readableMap.getString(str);
            if (string.endsWith("rad")) {
                string = string.substring(0, string.length() - 3);
            } else if (string.endsWith("deg")) {
                string = string.substring(0, string.length() - 3);
                z3 = false;
            }
            d4 = Float.parseFloat(string);
        } else {
            d4 = readableMap.getDouble(str);
        }
        return z3 ? d4 : Y.l(d4);
    }

    private static float[] b(float f3, float f4, ReadableArray readableArray, boolean z3) {
        if (readableArray == null) {
            return null;
        }
        if (f4 == 0.0f && f3 == 0.0f) {
            return null;
        }
        float f5 = f3 / 2.0f;
        float f6 = f4 / 2.0f;
        float[] fArr = new float[3];
        fArr[0] = f5;
        fArr[1] = f6;
        fArr[2] = 0.0f;
        int i3 = 0;
        while (i3 < readableArray.size() && i3 < 3) {
            int i4 = b.f7241a[readableArray.getType(i3).ordinal()];
            if (i4 == 1) {
                fArr[i3] = (float) readableArray.getDouble(i3);
            } else if (i4 == 2 && z3) {
                String string = readableArray.getString(i3);
                if (string.endsWith("%")) {
                    fArr[i3] = ((i3 == 0 ? f3 : f4) * Float.parseFloat(string.substring(0, string.length() - 1))) / 100.0f;
                }
            }
            i3++;
        }
        return new float[]{(-f5) + fArr[0], (-f6) + fArr[1], fArr[2]};
    }

    private static double c(String str, double d4) {
        try {
            return str.endsWith("%") ? (Double.parseDouble(str.substring(0, str.length() - 1)) * d4) / 100.0d : Double.parseDouble(str);
        } catch (NumberFormatException unused) {
            Y.a.I("ReactNative", "Invalid translate value: " + str);
            return 0.0d;
        }
    }

    public static void d(ReadableArray readableArray, double[] dArr, float f3, float f4, ReadableArray readableArray2, boolean z3) {
        int i3;
        int i4;
        int i5;
        double[] dArr2 = (double[]) f7240a.get();
        Y.r(dArr);
        float[] fArrB = b(f3, f4, readableArray2, z3);
        int i6 = 1;
        if (fArrB != null) {
            Y.r(dArr2);
            Y.j(dArr2, fArrB[0], fArrB[1], fArrB[2]);
            Y.p(dArr, dArr, dArr2);
        }
        int i7 = 16;
        if (readableArray.size() == 16 && readableArray.getType(0) == ReadableType.Number) {
            Y.r(dArr2);
            for (int i8 = 0; i8 < readableArray.size(); i8++) {
                dArr2[i8] = readableArray.getDouble(i8);
            }
            Y.p(dArr, dArr, dArr2);
        } else {
            int size = readableArray.size();
            int i9 = 0;
            while (i9 < size) {
                ReadableMap map = readableArray.getMap(i9);
                String strNextKey = map.keySetIterator().nextKey();
                Y.r(dArr2);
                if ("matrix".equals(strNextKey)) {
                    ReadableArray array = map.getArray(strNextKey);
                    for (int i10 = 0; i10 < i7; i10++) {
                        dArr2[i10] = array.getDouble(i10);
                    }
                } else if ("perspective".equals(strNextKey)) {
                    Y.a(dArr2, map.getDouble(strNextKey));
                } else if ("rotateX".equals(strNextKey)) {
                    Y.b(dArr2, a(map, strNextKey));
                } else if ("rotateY".equals(strNextKey)) {
                    Y.c(dArr2, a(map, strNextKey));
                } else {
                    if ("rotate".equals(strNextKey) || "rotateZ".equals(strNextKey)) {
                        i3 = i9;
                        i4 = i7;
                        i5 = size;
                        Y.d(dArr2, a(map, strNextKey));
                    } else if ("scale".equals(strNextKey)) {
                        double d4 = map.getDouble(strNextKey);
                        Y.e(dArr2, d4);
                        Y.f(dArr2, d4);
                    } else if ("scaleX".equals(strNextKey)) {
                        Y.e(dArr2, map.getDouble(strNextKey));
                    } else if ("scaleY".equals(strNextKey)) {
                        Y.f(dArr2, map.getDouble(strNextKey));
                    } else {
                        int i11 = size;
                        if ("translate".equals(strNextKey)) {
                            ReadableArray array2 = map.getArray(strNextKey);
                            ReadableType type = array2.getType(0);
                            ReadableType readableType = ReadableType.String;
                            double dC = (type == readableType && z3) ? c(array2.getString(0), f3) : array2.getDouble(0);
                            i3 = i9;
                            double dC2 = (array2.getType(i6) == readableType && z3) ? c(array2.getString(i6), f4) : array2.getDouble(i6);
                            i5 = i11;
                            i4 = 16;
                            Y.j(dArr2, dC, dC2, array2.size() > 2 ? array2.getDouble(2) : 0.0d);
                        } else {
                            i3 = i9;
                            i5 = i11;
                            i4 = 16;
                            if ("translateX".equals(strNextKey)) {
                                Y.i(dArr2, (map.getType(strNextKey) == ReadableType.String && z3) ? c(map.getString(strNextKey), f3) : map.getDouble(strNextKey), 0.0d);
                            } else if ("translateY".equals(strNextKey)) {
                                Y.i(dArr2, 0.0d, (map.getType(strNextKey) == ReadableType.String && z3) ? c(map.getString(strNextKey), f4) : map.getDouble(strNextKey));
                            } else if ("skewX".equals(strNextKey)) {
                                Y.g(dArr2, a(map, strNextKey));
                            } else if ("skewY".equals(strNextKey)) {
                                Y.h(dArr2, a(map, strNextKey));
                            } else {
                                Y.a.I("ReactNative", "Unsupported transform type: " + strNextKey);
                            }
                        }
                    }
                    Y.p(dArr, dArr, dArr2);
                    i9 = i3 + 1;
                    i7 = i4;
                    size = i5;
                    i6 = 1;
                }
                i3 = i9;
                i4 = i7;
                i5 = size;
                Y.p(dArr, dArr, dArr2);
                i9 = i3 + 1;
                i7 = i4;
                size = i5;
                i6 = 1;
            }
        }
        if (fArrB != null) {
            Y.r(dArr2);
            Y.j(dArr2, -fArrB[0], -fArrB[1], -fArrB[2]);
            Y.p(dArr, dArr, dArr2);
        }
    }
}
