package com.facebook.react.uimanager;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReadableType;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class W {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f7404c = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final float f7405a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final X f7406b;

    public static final class a {

        /* JADX INFO: renamed from: com.facebook.react.uimanager.W$a$a, reason: collision with other inner class name */
        public /* synthetic */ class C0110a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public static final /* synthetic */ int[] f7407a;

            static {
                int[] iArr = new int[ReadableType.values().length];
                try {
                    iArr[ReadableType.Number.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[ReadableType.String.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                f7407a = iArr;
            }
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final W a(Dynamic dynamic) {
            D2.h.f(dynamic, "dynamic");
            int i3 = C0110a.f7407a[dynamic.getType().ordinal()];
            if (i3 == 1) {
                double dAsDouble = dynamic.asDouble();
                if (dAsDouble >= 0.0d) {
                    return new W((float) dAsDouble, X.f7408b);
                }
                return null;
            }
            if (i3 != 2) {
                Y.a.I("ReactNative", "Unsupported type for radius property: " + dynamic.getType());
                return null;
            }
            String strAsString = dynamic.asString();
            if (!K2.o.m(strAsString, "%", false, 2, null)) {
                Y.a.I("ReactNative", "Invalid string value: " + strAsString);
                return null;
            }
            try {
                String strSubstring = strAsString.substring(0, strAsString.length() - 1);
                D2.h.e(strSubstring, "substring(...)");
                float f3 = Float.parseFloat(strSubstring);
                if (f3 >= 0.0f) {
                    return new W(f3, X.f7409c);
                }
                return null;
            } catch (NumberFormatException unused) {
                Y.a.I("ReactNative", "Invalid percentage format: " + strAsString);
                return null;
            }
        }

        private a() {
        }
    }

    public W(float f3, X x3) {
        D2.h.f(x3, "type");
        this.f7405a = f3;
        this.f7406b = x3;
    }

    public final X a() {
        return this.f7406b;
    }

    public final float b(float f3) {
        return this.f7406b == X.f7409c ? (this.f7405a / 100) * f3 : this.f7405a;
    }

    public final R1.k c(float f3, float f4) {
        if (this.f7406b != X.f7409c) {
            float f5 = this.f7405a;
            return new R1.k(f5, f5);
        }
        float f6 = this.f7405a;
        float f7 = 100;
        return new R1.k((f6 / f7) * f3, (f6 / f7) * f4);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof W)) {
            return false;
        }
        W w3 = (W) obj;
        return Float.compare(this.f7405a, w3.f7405a) == 0 && this.f7406b == w3.f7406b;
    }

    public int hashCode() {
        return (Float.hashCode(this.f7405a) * 31) + this.f7406b.hashCode();
    }

    public String toString() {
        return "LengthPercentage(value=" + this.f7405a + ", type=" + this.f7406b + ")";
    }

    public W() {
        this(0.0f, X.f7408b);
    }
}
