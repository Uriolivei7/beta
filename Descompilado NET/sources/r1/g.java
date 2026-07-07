package R1;

import android.content.Context;
import com.facebook.react.bridge.ColorPropConverter;
import com.facebook.react.bridge.JSApplicationCausedNativeException;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final a f2034g = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final float f2035a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final float f2036b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Integer f2037c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Float f2038d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Float f2039e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Boolean f2040f;

    public static final class a {

        /* JADX INFO: renamed from: R1.g$a$a, reason: collision with other inner class name */
        public /* synthetic */ class C0029a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public static final /* synthetic */ int[] f2041a;

            static {
                int[] iArr = new int[ReadableType.values().length];
                try {
                    iArr[ReadableType.Number.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[ReadableType.Map.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                f2041a = iArr;
            }
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final g a(ReadableMap readableMap, Context context) {
            Integer num;
            Integer numValueOf;
            D2.h.f(context, "context");
            if (readableMap == null || !readableMap.hasKey("offsetX") || !readableMap.hasKey("offsetY")) {
                return null;
            }
            float f3 = (float) readableMap.getDouble("offsetX");
            float f4 = (float) readableMap.getDouble("offsetY");
            if (readableMap.hasKey("color")) {
                ReadableType type = readableMap.getType("color");
                int i3 = C0029a.f2041a[type.ordinal()];
                if (i3 == 1) {
                    numValueOf = Integer.valueOf(readableMap.getInt("color"));
                } else {
                    if (i3 != 2) {
                        throw new JSApplicationCausedNativeException("Unsupported color type " + type);
                    }
                    numValueOf = ColorPropConverter.getColor(readableMap.getMap("color"), context);
                }
                num = numValueOf;
            } else {
                num = null;
            }
            return new g(f3, f4, num, readableMap.hasKey("blurRadius") ? Float.valueOf((float) readableMap.getDouble("blurRadius")) : null, readableMap.hasKey("spreadDistance") ? Float.valueOf((float) readableMap.getDouble("spreadDistance")) : null, readableMap.hasKey("inset") ? Boolean.valueOf(readableMap.getBoolean("inset")) : null);
        }

        private a() {
        }
    }

    public g(float f3, float f4, Integer num, Float f5, Float f6, Boolean bool) {
        this.f2035a = f3;
        this.f2036b = f4;
        this.f2037c = num;
        this.f2038d = f5;
        this.f2039e = f6;
        this.f2040f = bool;
    }

    public final Float a() {
        return this.f2038d;
    }

    public final Integer b() {
        return this.f2037c;
    }

    public final Boolean c() {
        return this.f2040f;
    }

    public final float d() {
        return this.f2035a;
    }

    public final float e() {
        return this.f2036b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof g)) {
            return false;
        }
        g gVar = (g) obj;
        return Float.compare(this.f2035a, gVar.f2035a) == 0 && Float.compare(this.f2036b, gVar.f2036b) == 0 && D2.h.b(this.f2037c, gVar.f2037c) && D2.h.b(this.f2038d, gVar.f2038d) && D2.h.b(this.f2039e, gVar.f2039e) && D2.h.b(this.f2040f, gVar.f2040f);
    }

    public final Float f() {
        return this.f2039e;
    }

    public int hashCode() {
        int iHashCode = ((Float.hashCode(this.f2035a) * 31) + Float.hashCode(this.f2036b)) * 31;
        Integer num = this.f2037c;
        int iHashCode2 = (iHashCode + (num == null ? 0 : num.hashCode())) * 31;
        Float f3 = this.f2038d;
        int iHashCode3 = (iHashCode2 + (f3 == null ? 0 : f3.hashCode())) * 31;
        Float f4 = this.f2039e;
        int iHashCode4 = (iHashCode3 + (f4 == null ? 0 : f4.hashCode())) * 31;
        Boolean bool = this.f2040f;
        return iHashCode4 + (bool != null ? bool.hashCode() : 0);
    }

    public String toString() {
        return "BoxShadow(offsetX=" + this.f2035a + ", offsetY=" + this.f2036b + ", color=" + this.f2037c + ", blurRadius=" + this.f2038d + ", spreadDistance=" + this.f2039e + ", inset=" + this.f2040f + ")";
    }

    public /* synthetic */ g(float f3, float f4, Integer num, Float f5, Float f6, Boolean bool, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(f3, f4, (i3 & 4) != 0 ? null : num, (i3 & 8) != 0 ? null : f5, (i3 & 16) != 0 ? null : f6, (i3 & 32) != 0 ? null : bool);
    }
}
