package Q1;

import android.view.animation.Interpolator;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class n implements Interpolator {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f1855b = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final float f1856a;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final float a(ReadableMap readableMap) {
            D2.h.f(readableMap, "params");
            if (readableMap.getType("springDamping") == ReadableType.Number) {
                return (float) readableMap.getDouble("springDamping");
            }
            return 0.5f;
        }

        private a() {
        }
    }

    public n() {
        this(0.0f, 1, null);
    }

    public static final float a(ReadableMap readableMap) {
        return f1855b.a(readableMap);
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f3) {
        double dPow = Math.pow(2.0d, (-10) * f3);
        float f4 = this.f1856a;
        return (float) (((double) 1) + (dPow * Math.sin(((((double) (f3 - (f4 / 4))) * 3.141592653589793d) * ((double) 2)) / ((double) f4))));
    }

    public /* synthetic */ n(float f3, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 0.5f : f3);
    }

    public n(float f3) {
        this.f1856a = f3;
    }
}
