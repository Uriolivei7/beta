package R1;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import com.facebook.react.bridge.ColorPropConverter;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.W;
import com.facebook.react.uimanager.X;
import java.util.ArrayList;
import java.util.List;
import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;
import r2.C0686i;
import s2.AbstractC0717n;
import w2.AbstractC0764a;

/* JADX INFO: loaded from: classes.dex */
public final class m {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ReadableArray f2060a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Context f2061b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final a f2062c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final ArrayList f2063d;

    private static abstract class a {

        /* JADX INFO: renamed from: R1.m$a$a, reason: collision with other inner class name */
        public static final class C0030a extends a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            private final double f2064a;

            public C0030a(double d4) {
                super(null);
                this.f2064a = d4;
            }

            public final double a() {
                return this.f2064a;
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof C0030a) && Double.compare(this.f2064a, ((C0030a) obj).f2064a) == 0;
            }

            public int hashCode() {
                return Double.hashCode(this.f2064a);
            }

            public String toString() {
                return "Angle(value=" + this.f2064a + ")";
            }
        }

        public static final class b extends a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            private final c f2065a;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public b(c cVar) {
                super(null);
                D2.h.f(cVar, "value");
                this.f2065a = cVar;
            }

            public final c a() {
                return this.f2065a;
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof b) && this.f2065a == ((b) obj).f2065a;
            }

            public int hashCode() {
                return this.f2065a.hashCode();
            }

            public String toString() {
                return "Keyword(value=" + this.f2065a + ")";
            }
        }

        /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
        /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
        public static final class c {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static final c f2066b = new c("TO_TOP_RIGHT", 0);

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            public static final c f2067c = new c("TO_BOTTOM_RIGHT", 1);

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            public static final c f2068d = new c("TO_TOP_LEFT", 2);

            /* JADX INFO: renamed from: e, reason: collision with root package name */
            public static final c f2069e = new c("TO_BOTTOM_LEFT", 3);

            /* JADX INFO: renamed from: f, reason: collision with root package name */
            private static final /* synthetic */ c[] f2070f;

            /* JADX INFO: renamed from: g, reason: collision with root package name */
            private static final /* synthetic */ EnumEntries f2071g;

            static {
                c[] cVarArrA = a();
                f2070f = cVarArrA;
                f2071g = AbstractC0764a.a(cVarArrA);
            }

            private c(String str, int i3) {
            }

            private static final /* synthetic */ c[] a() {
                return new c[]{f2066b, f2067c, f2068d, f2069e};
            }

            public static c valueOf(String str) {
                return (c) Enum.valueOf(c.class, str);
            }

            public static c[] values() {
                return (c[]) f2070f.clone();
            }
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f2072a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final /* synthetic */ int[] f2073b;

        static {
            int[] iArr = new int[a.c.values().length];
            try {
                iArr[a.c.f2066b.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[a.c.f2067c.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[a.c.f2068d.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[a.c.f2069e.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            f2072a = iArr;
            int[] iArr2 = new int[X.values().length];
            try {
                iArr2[X.f7408b.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr2[X.f7409c.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            f2073b = iArr2;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public m(ReadableMap readableMap, ReadableArray readableArray, Context context) {
        a.c cVar;
        a bVar;
        D2.h.f(readableMap, "directionMap");
        D2.h.f(readableArray, "colorStopsArray");
        D2.h.f(context, "context");
        this.f2060a = readableArray;
        this.f2061b = context;
        String string = readableMap.getString("type");
        if (!D2.h.b(string, "angle")) {
            if (!D2.h.b(string, "keyword")) {
                throw new IllegalArgumentException("Invalid direction type: " + string);
            }
            String string2 = readableMap.getString("value");
            if (string2 != null) {
                switch (string2.hashCode()) {
                    case -1849920841:
                        if (string2.equals("to bottom left")) {
                            cVar = a.c.f2069e;
                            bVar = new a.b(cVar);
                        }
                        break;
                    case -1507310228:
                        if (string2.equals("to bottom right")) {
                            cVar = a.c.f2067c;
                            bVar = new a.b(cVar);
                        }
                        break;
                    case -1359525897:
                        if (string2.equals("to top left")) {
                            cVar = a.c.f2068d;
                            bVar = new a.b(cVar);
                        }
                        break;
                    case 810031148:
                        if (string2.equals("to top right")) {
                            cVar = a.c.f2066b;
                            bVar = new a.b(cVar);
                        }
                        break;
                }
            }
            throw new IllegalArgumentException("Invalid linear gradient direction keyword: " + readableMap.getString("value"));
        }
        bVar = new a.C0030a(readableMap.getDouble("value"));
        this.f2062c = bVar;
        ArrayList arrayList = new ArrayList(readableArray.size());
        int size = readableArray.size();
        for (int i3 = 0; i3 < size; i3++) {
            ReadableMap map = this.f2060a.getMap(i3);
            if (map != null) {
                arrayList.add(new i((!map.hasKey("color") || map.isNull("color")) ? null : map.getType("color") == ReadableType.Map ? ColorPropConverter.getColor(map.getMap("color"), this.f2061b) : Integer.valueOf(map.getInt("color")), W.f7404c.a(map.getDynamic("position"))));
            }
        }
        this.f2063d = arrayList;
    }

    private final C0686i a(double d4, float f3, float f4) {
        double d5 = 360;
        double d6 = d4 % d5;
        if (d6 < 0.0d) {
            d6 += d5;
        }
        if (d6 == 0.0d) {
            return new C0686i(new float[]{0.0f, f3}, new float[]{0.0f, 0.0f});
        }
        if (d6 == 90.0d) {
            return new C0686i(new float[]{0.0f, 0.0f}, new float[]{f4, 0.0f});
        }
        if (d6 == 180.0d) {
            return new C0686i(new float[]{0.0f, 0.0f}, new float[]{0.0f, f3});
        }
        if (d6 == 270.0d) {
            return new C0686i(new float[]{f4, 0.0f}, new float[]{0.0f, 0.0f});
        }
        float fTan = (float) Math.tan(Math.toRadians(((double) 90) - d6));
        float f5 = (-1) / fTan;
        float f6 = 2;
        float f7 = f3 / f6;
        float f8 = f4 / f6;
        float[] fArr = d6 < 90.0d ? new float[]{f8, f7} : d6 < 180.0d ? new float[]{f8, -f7} : d6 < 270.0d ? new float[]{-f8, -f7} : new float[]{-f8, f7};
        float f9 = fArr[1] - (fArr[0] * f5);
        float f10 = f9 / (fTan - f5);
        float f11 = (f5 * f10) + f9;
        return new C0686i(new float[]{f8 - f10, f7 + f11}, new float[]{f8 + f10, f7 - f11});
    }

    private final double b(a.c cVar, double d4, double d5) {
        double degrees;
        double d6;
        int i3;
        int i4 = b.f2072a[cVar.ordinal()];
        if (i4 == 1) {
            return ((double) 90) - Math.toDegrees(Math.atan(d4 / d5));
        }
        if (i4 != 2) {
            if (i4 == 3) {
                degrees = Math.toDegrees(Math.atan(d4 / d5));
                i3 = 270;
            } else {
                if (i4 != 4) {
                    throw new C0685h();
                }
                degrees = Math.toDegrees(Math.atan(d5 / d4));
                i3 = 180;
            }
            d6 = i3;
        } else {
            degrees = Math.toDegrees(Math.atan(d4 / d5));
            d6 = 90;
        }
        return degrees + d6;
    }

    private final q[] c(ArrayList arrayList, float f3) {
        Float fB;
        int size = arrayList.size();
        q[] qVarArr = new q[size];
        int i3 = 0;
        for (int i4 = 0; i4 < size; i4++) {
            qVarArr[i4] = new q(null, null, 3, null);
        }
        Float f4 = f(((i) arrayList.get(0)).b(), f3);
        float fFloatValue = f4 != null ? f4.floatValue() : 0.0f;
        int size2 = arrayList.size();
        int i5 = 0;
        boolean z3 = false;
        while (i5 < size2) {
            Object obj = arrayList.get(i5);
            D2.h.e(obj, "get(...)");
            i iVar = (i) obj;
            Float f5 = f(iVar.b(), f3);
            if (f5 == null) {
                f5 = i5 == 0 ? Float.valueOf(0.0f) : i5 == arrayList.size() - 1 ? Float.valueOf(1.0f) : null;
            }
            if (f5 != null) {
                fFloatValue = Math.max(f5.floatValue(), fFloatValue);
                qVarArr[i5] = new q(iVar.a(), Float.valueOf(fFloatValue));
            } else {
                z3 = true;
            }
            i5++;
        }
        if (z3) {
            for (int i6 = 1; i6 < size; i6++) {
                Float fB2 = qVarArr[i6].b();
                if (fB2 != null) {
                    int i7 = i6 - i3;
                    int i8 = i7 - 1;
                    if (i8 > 0 && (fB = qVarArr[i3].b()) != null) {
                        float fFloatValue2 = (fB2.floatValue() - fB.floatValue()) / i7;
                        if (1 <= i8) {
                            int i9 = 1;
                            while (true) {
                                int i10 = i3 + i9;
                                qVarArr[i10] = new q(((i) arrayList.get(i10)).a(), Float.valueOf(fB.floatValue() + (i9 * fFloatValue2)));
                                if (i9 == i8) {
                                    break;
                                }
                                i9++;
                            }
                        }
                    }
                    i3 = i6;
                }
            }
        }
        return qVarArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x007e A[PHI: r6
      0x007e: PHI (r6v2 int) = (r6v1 int), (r6v1 int), (r6v1 int), (r6v1 int), (r6v1 int), (r6v1 int), (r6v1 int), (r6v5 int) binds: [B:5:0x0013, B:8:0x0018, B:11:0x003d, B:12:0x003f, B:13:0x0041, B:24:0x0099, B:21:0x0089, B:17:0x0079] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final java.util.List e(R1.q[] r23) {
        /*
            Method dump skipped, instruction units count: 468
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: R1.m.e(R1.q[]):java.util.List");
    }

    private final Float f(W w3, float f3) {
        if (w3 == null) {
            return null;
        }
        int i3 = b.f2073b[w3.a().ordinal()];
        if (i3 == 1) {
            return Float.valueOf(C0429f0.h(w3.b(0.0f)) / f3);
        }
        if (i3 == 2) {
            return Float.valueOf(w3.b(1.0f));
        }
        throw new C0685h();
    }

    public final Shader d(float f3, float f4) {
        double dB;
        a aVar = this.f2062c;
        if (aVar instanceof a.C0030a) {
            dB = ((a.C0030a) aVar).a();
        } else {
            if (!(aVar instanceof a.b)) {
                throw new C0685h();
            }
            dB = b(((a.b) aVar).a(), f3, f4);
        }
        C0686i c0686iA = a(dB, f4, f3);
        float[] fArr = (float[]) c0686iA.a();
        float[] fArr2 = (float[]) c0686iA.b();
        float f5 = fArr2[0] - fArr[0];
        float f6 = fArr2[1] - fArr[1];
        List listE = e(c(this.f2063d, (float) Math.sqrt((f5 * f5) + (f6 * f6))));
        int[] iArr = new int[listE.size()];
        float[] fArr3 = new float[listE.size()];
        int i3 = 0;
        for (Object obj : listE) {
            int i4 = i3 + 1;
            if (i3 < 0) {
                AbstractC0717n.p();
            }
            q qVar = (q) obj;
            Integer numA = qVar.a();
            if (numA != null && qVar.b() != null) {
                iArr[i3] = numA.intValue();
                fArr3[i3] = qVar.b().floatValue();
            }
            i3 = i4;
        }
        return new LinearGradient(fArr[0], fArr[1], fArr2[0], fArr2[1], iArr, fArr3, Shader.TileMode.CLAMP);
    }
}
