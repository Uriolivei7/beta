package n2;

import android.view.MotionEvent;

/* JADX INFO: loaded from: classes.dex */
public final class k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final k f10028a = new k();

    private k() {
    }

    public final double a(double d4) {
        return Math.cos(Math.toRadians(d4 / 2.0d));
    }

    public final float b(MotionEvent motionEvent, boolean z3) {
        D2.h.f(motionEvent, "event");
        int actionIndex = motionEvent.getActionMasked() == 6 ? motionEvent.getActionIndex() : -1;
        if (!z3) {
            int pointerCount = motionEvent.getPointerCount();
            int i3 = pointerCount - 1;
            if (i3 == actionIndex) {
                i3 = pointerCount - 2;
            }
            return motionEvent.getX(i3);
        }
        int pointerCount2 = motionEvent.getPointerCount();
        float x3 = 0.0f;
        int i4 = 0;
        for (int i5 = 0; i5 < pointerCount2; i5++) {
            if (i5 != actionIndex) {
                x3 += motionEvent.getX(i5);
                i4++;
            }
        }
        return x3 / i4;
    }

    public final float c(MotionEvent motionEvent, boolean z3) {
        D2.h.f(motionEvent, "event");
        int actionIndex = motionEvent.getActionMasked() == 6 ? motionEvent.getActionIndex() : -1;
        if (!z3) {
            int pointerCount = motionEvent.getPointerCount();
            int i3 = pointerCount - 1;
            if (i3 == actionIndex) {
                i3 = pointerCount - 2;
            }
            return motionEvent.getY(i3);
        }
        int pointerCount2 = motionEvent.getPointerCount();
        float y3 = 0.0f;
        int i4 = 0;
        for (int i5 = 0; i5 < pointerCount2; i5++) {
            if (i5 != actionIndex) {
                y3 += motionEvent.getY(i5);
                i4++;
            }
        }
        return y3 / i4;
    }
}
