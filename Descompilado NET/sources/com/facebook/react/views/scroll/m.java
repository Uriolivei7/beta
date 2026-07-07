package com.facebook.react.views.scroll;

import android.view.MotionEvent;
import android.view.VelocityTracker;

/* JADX INFO: loaded from: classes.dex */
public final class m {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private VelocityTracker f7896a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private float f7897b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private float f7898c;

    public final void a(MotionEvent motionEvent) {
        D2.h.f(motionEvent, "ev");
        if (this.f7896a == null) {
            this.f7896a = VelocityTracker.obtain();
        }
        VelocityTracker velocityTracker = this.f7896a;
        if (velocityTracker != null) {
            velocityTracker.addMovement(motionEvent);
            int action = motionEvent.getAction() & 255;
            if (action == 1 || action == 3) {
                velocityTracker.computeCurrentVelocity(1);
                this.f7897b = velocityTracker.getXVelocity();
                this.f7898c = velocityTracker.getYVelocity();
                velocityTracker.recycle();
                this.f7896a = null;
            }
        }
    }

    public final float b() {
        return this.f7897b;
    }

    public final float c() {
        return this.f7898c;
    }
}
