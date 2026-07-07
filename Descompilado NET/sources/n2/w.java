package n2;

import android.view.MotionEvent;

/* JADX INFO: loaded from: classes.dex */
public final class w {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final a f10105a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private long f10106b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private long f10107c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private double f10108d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private double f10109e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private float f10110f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private float f10111g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f10112h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final int[] f10113i = new int[2];

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f10114j;

    public interface a {
        boolean a(w wVar);

        boolean b(w wVar);

        void c(w wVar);
    }

    public w(a aVar) {
        this.f10105a = aVar;
    }

    private final void a() {
        if (this.f10112h) {
            this.f10114j = false;
            this.f10112h = false;
            a aVar = this.f10105a;
            if (aVar != null) {
                aVar.c(this);
            }
        }
    }

    private final void g() {
        if (this.f10114j) {
            return;
        }
        this.f10114j = true;
    }

    private final void h(double d4) {
        if (this.f10114j) {
            this.f10108d = d4;
            this.f10114j = false;
        }
    }

    private final void i(MotionEvent motionEvent) {
        this.f10107c = this.f10106b;
        this.f10106b = motionEvent.getEventTime();
        int iFindPointerIndex = motionEvent.findPointerIndex(this.f10113i[0]);
        int iFindPointerIndex2 = motionEvent.findPointerIndex(this.f10113i[1]);
        if (iFindPointerIndex == -1 || iFindPointerIndex2 == -1) {
            return;
        }
        float x3 = motionEvent.getX(iFindPointerIndex);
        float y3 = motionEvent.getY(iFindPointerIndex);
        float x4 = motionEvent.getX(iFindPointerIndex2);
        float y4 = motionEvent.getY(iFindPointerIndex2);
        this.f10110f = (x3 + x4) * 0.5f;
        this.f10111g = (y3 + y4) * 0.5f;
        double d4 = -Math.atan2(y4 - y3, x4 - x3);
        h(d4);
        double d5 = Double.isNaN(this.f10108d) ? 0.0d : this.f10108d - d4;
        this.f10109e = d5;
        this.f10108d = d4;
        if (d5 > 3.141592653589793d) {
            this.f10109e = d5 - 3.141592653589793d;
        } else if (d5 < -3.141592653589793d) {
            this.f10109e = d5 + 3.141592653589793d;
        }
        double d6 = this.f10109e;
        if (d6 > 1.5707963267948966d) {
            this.f10109e = d6 - 3.141592653589793d;
        } else if (d6 < -1.5707963267948966d) {
            this.f10109e = d6 + 3.141592653589793d;
        }
    }

    public final float b() {
        return this.f10110f;
    }

    public final float c() {
        return this.f10111g;
    }

    public final double d() {
        return this.f10109e;
    }

    public final long e() {
        return this.f10106b - this.f10107c;
    }

    public final boolean f(MotionEvent motionEvent) {
        a aVar;
        D2.h.f(motionEvent, "event");
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.f10112h = false;
            this.f10113i[0] = motionEvent.getPointerId(motionEvent.getActionIndex());
            this.f10113i[1] = -1;
        } else if (actionMasked == 1) {
            a();
        } else if (actionMasked != 2) {
            if (actionMasked == 5) {
                if (!this.f10112h || this.f10114j) {
                    this.f10113i[1] = motionEvent.getPointerId(motionEvent.getActionIndex());
                    i(motionEvent);
                }
                if (!this.f10112h) {
                    this.f10112h = true;
                    this.f10107c = motionEvent.getEventTime();
                    this.f10108d = Double.NaN;
                    a aVar2 = this.f10105a;
                    if (aVar2 != null) {
                        aVar2.b(this);
                    }
                }
            } else if (actionMasked == 6 && this.f10112h) {
                int pointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                int[] iArr = this.f10113i;
                if (pointerId == iArr[0]) {
                    iArr[0] = iArr[1];
                    iArr[1] = -1;
                    g();
                } else if (pointerId == iArr[1]) {
                    iArr[1] = -1;
                    g();
                }
            }
        } else if (this.f10112h) {
            i(motionEvent);
            if (!this.f10114j && (aVar = this.f10105a) != null) {
                aVar.a(this);
            }
        }
        return true;
    }
}
