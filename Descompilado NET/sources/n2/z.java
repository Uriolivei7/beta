package n2;

import android.view.MotionEvent;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0686i;

/* JADX INFO: loaded from: classes.dex */
public final class z {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final a f10148f = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final double f10149a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final double f10150b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final double f10151c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final double f10152d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final double f10153e;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final C0686i b(double d4, double d5) {
            double dAtan;
            double dAtan2;
            if (d4 < 1.0E-9d) {
                dAtan = 1.5707963267948966d;
                double d6 = (d5 < 1.0E-9d || Math.abs(d5 - 6.283185307179586d) < 1.0E-9d) ? 1.5707963267948966d : 0.0d;
                double d7 = d5 - 1.5707963267948966d;
                double d8 = Math.abs(d7) < 1.0E-9d ? 1.5707963267948966d : 0.0d;
                double d9 = d5 - 3.141592653589793d;
                dAtan2 = -1.5707963267948966d;
                if (Math.abs(d9) < 1.0E-9d) {
                    d6 = -1.5707963267948966d;
                }
                double d10 = d5 - 4.71238898038469d;
                if (Math.abs(d10) < 1.0E-9d) {
                    d8 = -1.5707963267948966d;
                }
                if (d5 > 1.0E-9d && Math.abs(d7) < 1.0E-9d) {
                    d8 = 1.5707963267948966d;
                    d6 = 1.5707963267948966d;
                }
                if (Math.abs(d7) > 1.0E-9d && Math.abs(d9) < 1.0E-9d) {
                    d8 = 1.5707963267948966d;
                    d6 = -1.5707963267948966d;
                }
                if (Math.abs(d9) > 1.0E-9d && Math.abs(d10) < 1.0E-9d) {
                    d8 = -1.5707963267948966d;
                    d6 = -1.5707963267948966d;
                }
                if (Math.abs(d10) <= 1.0E-9d || Math.abs(d5 - 6.283185307179586d) >= 1.0E-9d) {
                    dAtan2 = d8;
                    dAtan = d6;
                }
            } else {
                double dTan = Math.tan(d4);
                dAtan = Math.atan(Math.cos(d5) / dTan);
                dAtan2 = Math.atan(Math.sin(d5) / dTan);
            }
            return new C0686i(Double.valueOf(Math.rint(dAtan * 57.29577951308232d)), Double.valueOf(Math.rint(dAtan2 * 57.29577951308232d)));
        }

        public final z a(MotionEvent motionEvent) {
            D2.h.f(motionEvent, "event");
            double axisValue = 1.5707963267948966d - ((double) motionEvent.getAxisValue(25));
            double pressure = motionEvent.getPressure(0);
            double orientation = (((double) motionEvent.getOrientation(0)) + 1.5707963267948966d) % 6.283185307179586d;
            if (orientation != 0.0d && Math.signum(orientation) != Math.signum(6.283185307179586d)) {
                orientation += 6.283185307179586d;
            }
            double d4 = orientation;
            C0686i c0686iB = b(axisValue, d4);
            return new z(((Number) c0686iB.c()).doubleValue(), ((Number) c0686iB.d()).doubleValue(), axisValue, d4, pressure);
        }

        private a() {
        }
    }

    public z() {
        this(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 31, null);
    }

    public final double a() {
        return this.f10153e;
    }

    public final ReadableMap b() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putDouble("tiltX", this.f10149a);
        writableMapCreateMap.putDouble("tiltY", this.f10150b);
        writableMapCreateMap.putDouble("altitudeAngle", this.f10151c);
        writableMapCreateMap.putDouble("azimuthAngle", this.f10152d);
        writableMapCreateMap.putDouble("pressure", this.f10153e);
        D2.h.c(writableMapCreateMap);
        return writableMapCreateMap;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof z)) {
            return false;
        }
        z zVar = (z) obj;
        return Double.compare(this.f10149a, zVar.f10149a) == 0 && Double.compare(this.f10150b, zVar.f10150b) == 0 && Double.compare(this.f10151c, zVar.f10151c) == 0 && Double.compare(this.f10152d, zVar.f10152d) == 0 && Double.compare(this.f10153e, zVar.f10153e) == 0;
    }

    public int hashCode() {
        return (((((((Double.hashCode(this.f10149a) * 31) + Double.hashCode(this.f10150b)) * 31) + Double.hashCode(this.f10151c)) * 31) + Double.hashCode(this.f10152d)) * 31) + Double.hashCode(this.f10153e);
    }

    public String toString() {
        return "StylusData(tiltX=" + this.f10149a + ", tiltY=" + this.f10150b + ", altitudeAngle=" + this.f10151c + ", azimuthAngle=" + this.f10152d + ", pressure=" + this.f10153e + ")";
    }

    public z(double d4, double d5, double d6, double d7, double d8) {
        this.f10149a = d4;
        this.f10150b = d5;
        this.f10151c = d6;
        this.f10152d = d7;
        this.f10153e = d8;
    }

    public /* synthetic */ z(double d4, double d5, double d6, double d7, double d8, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 0.0d : d4, (i3 & 2) != 0 ? 0.0d : d5, (i3 & 4) != 0 ? 0.0d : d6, (i3 & 8) == 0 ? d7 : 0.0d, (i3 & 16) != 0 ? -1.0d : d8);
    }
}
