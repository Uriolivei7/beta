package com.facebook.react.animated;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX INFO: loaded from: classes.dex */
public final class k extends w {

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public static final a f6417q = new a(null);

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private static final Pattern f6418r;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final double[] f6419i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private Object f6420j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private b f6421k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private String f6422l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final String f6423m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final String f6424n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private w f6425o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private Object f6426p;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final int d(double d4, double[] dArr) {
            int i3 = 1;
            while (i3 < dArr.length - 1 && dArr[i3] < d4) {
                i3++;
            }
            return i3 - 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final double[] e(ReadableArray readableArray) {
            if (readableArray == null) {
                return new double[0];
            }
            int size = readableArray.size();
            double[] dArr = new double[size];
            for (int i3 = 0; i3 < size; i3++) {
                dArr[i3] = readableArray.getDouble(i3);
            }
            return dArr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int[] f(ReadableArray readableArray) {
            if (readableArray == null) {
                return new int[0];
            }
            int size = readableArray.size();
            int[] iArr = new int[size];
            for (int i3 = 0; i3 < size; i3++) {
                iArr[i3] = readableArray.getInt(i3);
            }
            return iArr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final double[][] g(ReadableArray readableArray) {
            int size = readableArray.size();
            double[][] dArr = new double[size][];
            Pattern pattern = k.f6418r;
            String string = readableArray.getString(0);
            if (string == null) {
                string = "";
            }
            Matcher matcher = pattern.matcher(string);
            ArrayList arrayList = new ArrayList();
            while (matcher.find()) {
                String strGroup = matcher.group();
                D2.h.e(strGroup, "group(...)");
                arrayList.add(Double.valueOf(Double.parseDouble(strGroup)));
            }
            int size2 = arrayList.size();
            double[] dArr2 = new double[size2];
            int size3 = arrayList.size();
            for (int i3 = 0; i3 < size3; i3++) {
                dArr2[i3] = ((Number) arrayList.get(i3)).doubleValue();
            }
            dArr[0] = dArr2;
            for (int i4 = 1; i4 < size; i4++) {
                double[] dArr3 = new double[size2];
                Pattern pattern2 = k.f6418r;
                String string2 = readableArray.getString(i4);
                if (string2 == null) {
                    string2 = "";
                }
                Matcher matcher2 = pattern2.matcher(string2);
                for (int i5 = 0; matcher2.find() && i5 < size2; i5++) {
                    String strGroup2 = matcher2.group();
                    D2.h.e(strGroup2, "group(...)");
                    dArr3[i5] = Double.parseDouble(strGroup2);
                }
                dArr[i4] = dArr3;
            }
            return dArr;
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x0038, code lost:
        
            if (r23.equals("extend") != false) goto L20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x007a, code lost:
        
            if (r24.equals("extend") != false) goto L39;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final double h(double r13, double r15, double r17, double r19, double r21, java.lang.String r23, java.lang.String r24) {
            /*
                r12 = this;
                r0 = r23
                r1 = r24
                int r2 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
                java.lang.String r3 = "Invalid extrapolation type "
                java.lang.String r4 = "extend"
                java.lang.String r5 = "identity"
                java.lang.String r6 = "clamp"
                r7 = 94742715(0x5a5a8bb, float:1.5578507E-35)
                r8 = -135761730(0xfffffffff7e870be, float:-9.428903E33)
                r9 = -1289044198(0xffffffffb32abf1a, float:-3.9755015E-8)
                if (r2 >= 0) goto L55
                if (r0 == 0) goto L3b
                int r10 = r23.hashCode()
                if (r10 == r9) goto L34
                if (r10 == r8) goto L2d
                if (r10 != r7) goto L3b
                boolean r10 = r0.equals(r6)
                if (r10 == 0) goto L3b
                r10 = r15
                goto L56
            L2d:
                boolean r1 = r0.equals(r5)
                if (r1 == 0) goto L3b
                return r13
            L34:
                boolean r10 = r0.equals(r4)
                if (r10 == 0) goto L3b
                goto L55
            L3b:
                com.facebook.react.bridge.JSApplicationIllegalArgumentException r1 = new com.facebook.react.bridge.JSApplicationIllegalArgumentException
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                r2.append(r3)
                r2.append(r0)
                java.lang.String r0 = "for left extrapolation"
                r2.append(r0)
                java.lang.String r0 = r2.toString()
                r1.<init>(r0)
                throw r1
            L55:
                r10 = r13
            L56:
                int r0 = (r10 > r17 ? 1 : (r10 == r17 ? 0 : -1))
                if (r0 <= 0) goto L97
                if (r1 == 0) goto L7d
                int r0 = r24.hashCode()
                if (r0 == r9) goto L76
                if (r0 == r8) goto L6f
                if (r0 != r7) goto L7d
                boolean r0 = r1.equals(r6)
                if (r0 == 0) goto L7d
                r10 = r17
                goto L97
            L6f:
                boolean r0 = r1.equals(r5)
                if (r0 == 0) goto L7d
                return r10
            L76:
                boolean r0 = r1.equals(r4)
                if (r0 == 0) goto L7d
                goto L97
            L7d:
                com.facebook.react.bridge.JSApplicationIllegalArgumentException r0 = new com.facebook.react.bridge.JSApplicationIllegalArgumentException
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                r2.append(r3)
                r2.append(r1)
                java.lang.String r1 = "for right extrapolation"
                r2.append(r1)
                java.lang.String r1 = r2.toString()
                r0.<init>(r1)
                throw r0
            L97:
                int r0 = (r19 > r21 ? 1 : (r19 == r21 ? 0 : -1))
                if (r0 != 0) goto L9c
                return r19
            L9c:
                int r0 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1))
                if (r0 != 0) goto La8
                if (r2 > 0) goto La5
                r0 = r19
                goto Lb1
            La5:
                r0 = r21
                goto Lb1
            La8:
                double r0 = r21 - r19
                double r10 = r10 - r15
                double r0 = r0 * r10
                double r2 = r17 - r15
                double r0 = r0 / r2
                double r0 = r19 + r0
            Lb1:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.animated.k.a.h(double, double, double, double, double, java.lang.String, java.lang.String):double");
        }

        public final double i(double d4, double[] dArr, double[] dArr2, String str, String str2) {
            D2.h.f(dArr, "inputRange");
            D2.h.f(dArr2, "outputRange");
            int iD = d(d4, dArr);
            int i3 = iD + 1;
            return h(d4, dArr[iD], dArr[i3], dArr2[iD], dArr2[i3], str, str2);
        }

        public final int j(double d4, double[] dArr, int[] iArr) {
            D2.h.f(dArr, "inputRange");
            D2.h.f(iArr, "outputRange");
            int iD = d(d4, dArr);
            int i3 = iArr[iD];
            int i4 = iD + 1;
            int i5 = iArr[i4];
            if (i3 == i5) {
                return i3;
            }
            double d5 = dArr[iD];
            double d6 = dArr[i4];
            return d5 == d6 ? d4 <= d5 ? i3 : i5 : androidx.core.graphics.a.b(i3, i5, (float) ((d4 - d5) / (d6 - d5)));
        }

        public final String k(String str, double d4, double[] dArr, double[][] dArr2, String str2, String str3) {
            double[] dArr3 = dArr;
            D2.h.f(str, "pattern");
            D2.h.f(dArr3, "inputRange");
            D2.h.f(dArr2, "outputRange");
            int iD = d(d4, dArr3);
            StringBuffer stringBuffer = new StringBuffer(str.length());
            Matcher matcher = k.f6418r.matcher(str);
            int i3 = 0;
            while (matcher.find()) {
                double[] dArr4 = dArr2[iD];
                if (i3 >= dArr4.length) {
                    break;
                }
                int i4 = iD + 1;
                int i5 = i3;
                StringBuffer stringBuffer2 = stringBuffer;
                double dH = h(d4, dArr3[iD], dArr3[i4], dArr4[i3], dArr2[i4][i3], str2, str3);
                int i6 = (int) dH;
                matcher.appendReplacement(stringBuffer2, ((double) i6) == dH ? String.valueOf(i6) : String.valueOf(dH));
                i3 = i5 + 1;
                stringBuffer = stringBuffer2;
                dArr3 = dArr;
            }
            StringBuffer stringBuffer3 = stringBuffer;
            matcher.appendTail(stringBuffer3);
            String string = stringBuffer3.toString();
            D2.h.e(string, "toString(...)");
            return string;
        }

        private a() {
        }
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    private static final class b {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final b f6427b = new b("Number", 0);

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final b f6428c = new b("Color", 1);

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public static final b f6429d = new b("String", 2);

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private static final /* synthetic */ b[] f6430e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private static final /* synthetic */ EnumEntries f6431f;

        static {
            b[] bVarArrA = a();
            f6430e = bVarArrA;
            f6431f = AbstractC0764a.a(bVarArrA);
        }

        private b(String str, int i3) {
        }

        private static final /* synthetic */ b[] a() {
            return new b[]{f6427b, f6428c, f6429d};
        }

        public static b valueOf(String str) {
            return (b) Enum.valueOf(b.class, str);
        }

        public static b[] values() {
            return (b[]) f6430e.clone();
        }
    }

    public /* synthetic */ class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f6432a;

        static {
            int[] iArr = new int[b.values().length];
            try {
                iArr[b.f6427b.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[b.f6428c.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[b.f6429d.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f6432a = iArr;
        }
    }

    static {
        Pattern patternCompile = Pattern.compile("[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?");
        D2.h.e(patternCompile, "compile(...)");
        f6418r = patternCompile;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public k(ReadableMap readableMap) {
        super(null, 1, null);
        D2.h.f(readableMap, "config");
        a aVar = f6417q;
        this.f6419i = aVar.e(readableMap.getArray("inputRange"));
        this.f6423m = readableMap.getString("extrapolateLeft");
        this.f6424n = readableMap.getString("extrapolateRight");
        ReadableArray array = readableMap.getArray("outputRange");
        if (D2.h.b("color", readableMap.getString("outputType"))) {
            this.f6421k = b.f6428c;
            this.f6420j = aVar.f(array);
            return;
        }
        if ((array != null ? array.getType(0) : null) != ReadableType.String) {
            this.f6421k = b.f6427b;
            this.f6420j = aVar.e(array);
        } else {
            this.f6421k = b.f6429d;
            this.f6420j = aVar.g(array);
            this.f6422l = array.getString(0);
        }
    }

    @Override // com.facebook.react.animated.b
    public void c(com.facebook.react.animated.b bVar) {
        D2.h.f(bVar, "parent");
        if (this.f6425o != null) {
            throw new IllegalStateException("Parent already attached");
        }
        if (!(bVar instanceof w)) {
            throw new IllegalArgumentException("Parent is of an invalid type");
        }
        this.f6425o = (w) bVar;
    }

    @Override // com.facebook.react.animated.b
    public void d(com.facebook.react.animated.b bVar) {
        D2.h.f(bVar, "parent");
        if (bVar != this.f6425o) {
            throw new IllegalArgumentException("Invalid parent node provided");
        }
        this.f6425o = null;
    }

    @Override // com.facebook.react.animated.w, com.facebook.react.animated.b
    public String e() {
        return "InterpolationAnimatedNode[" + this.f6381d + "] super: {super.prettyPrint()}";
    }

    @Override // com.facebook.react.animated.b
    public void h() {
        String str;
        w wVar = this.f6425o;
        if (wVar != null) {
            double dL = wVar.l();
            b bVar = this.f6421k;
            int i3 = bVar == null ? -1 : c.f6432a[bVar.ordinal()];
            if (i3 == 1) {
                a aVar = f6417q;
                double[] dArr = this.f6419i;
                Object obj = this.f6420j;
                D2.h.d(obj, "null cannot be cast to non-null type kotlin.DoubleArray");
                this.f6495f = aVar.i(dL, dArr, (double[]) obj, this.f6423m, this.f6424n);
                return;
            }
            if (i3 == 2) {
                a aVar2 = f6417q;
                double[] dArr2 = this.f6419i;
                Object obj2 = this.f6420j;
                D2.h.d(obj2, "null cannot be cast to non-null type kotlin.IntArray");
                this.f6426p = Integer.valueOf(aVar2.j(dL, dArr2, (int[]) obj2));
                return;
            }
            if (i3 == 3 && (str = this.f6422l) != null) {
                a aVar3 = f6417q;
                double[] dArr3 = this.f6419i;
                Object obj3 = this.f6420j;
                D2.h.d(obj3, "null cannot be cast to non-null type kotlin.Array<kotlin.DoubleArray>");
                this.f6426p = aVar3.k(str, dL, dArr3, (double[][]) obj3, this.f6423m, this.f6424n);
            }
        }
    }

    @Override // com.facebook.react.animated.w
    public Object k() {
        return this.f6426p;
    }
}
