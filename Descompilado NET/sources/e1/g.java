package e1;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class g implements SensorEventListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final a f9359a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f9360b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private float f9361c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private float f9362d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private float f9363e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private SensorManager f9364f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private long f9365g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f9366h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private long f9367i;

    public interface a {
        void a();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public g(a aVar) {
        this(aVar, 0, 2, null);
        D2.h.f(aVar, "shakeListener");
    }

    private final boolean a(float f3) {
        return Math.abs(f3) > 13.042845f;
    }

    private final void b(long j3) {
        if (this.f9366h >= this.f9360b * 8) {
            d();
            this.f9359a.a();
        }
        if (j3 - this.f9367i > h.f9369b) {
            d();
        }
    }

    private final void c(long j3) {
        this.f9367i = j3;
        this.f9366h++;
    }

    private final void d() {
        this.f9366h = 0;
        this.f9361c = 0.0f;
        this.f9362d = 0.0f;
        this.f9363e = 0.0f;
    }

    public final void e(SensorManager sensorManager) {
        D2.h.f(sensorManager, "manager");
        Sensor defaultSensor = sensorManager.getDefaultSensor(1);
        if (defaultSensor == null) {
            return;
        }
        this.f9364f = sensorManager;
        this.f9365g = -1L;
        sensorManager.registerListener(this, defaultSensor, 2);
        this.f9367i = 0L;
        d();
    }

    public final void f() {
        SensorManager sensorManager = this.f9364f;
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        this.f9364f = null;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i3) {
        D2.h.f(sensor, "sensor");
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        D2.h.f(sensorEvent, "sensorEvent");
        if (sensorEvent.timestamp - this.f9365g < h.f9368a) {
            return;
        }
        float[] fArr = sensorEvent.values;
        float f3 = fArr[0];
        float f4 = fArr[1];
        float f5 = fArr[2] - 9.80665f;
        this.f9365g = sensorEvent.timestamp;
        if (a(f3) && this.f9361c * f3 <= 0.0f) {
            c(sensorEvent.timestamp);
            this.f9361c = f3;
        } else if (a(f4) && this.f9362d * f4 <= 0.0f) {
            c(sensorEvent.timestamp);
            this.f9362d = f4;
        } else if (a(f5) && this.f9363e * f5 <= 0.0f) {
            c(sensorEvent.timestamp);
            this.f9363e = f5;
        }
        b(sensorEvent.timestamp);
    }

    public g(a aVar, int i3) {
        D2.h.f(aVar, "shakeListener");
        this.f9359a = aVar;
        this.f9360b = i3;
    }

    public /* synthetic */ g(a aVar, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(aVar, (i4 & 2) != 0 ? 1 : i3);
    }
}
