package v0;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/* JADX INFO: renamed from: v0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0755a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    InterfaceC0149a f10886a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final float f10887b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    boolean f10888c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    boolean f10889d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    long f10890e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    float f10891f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    float f10892g;

    /* JADX INFO: renamed from: v0.a$a, reason: collision with other inner class name */
    public interface InterfaceC0149a {
        boolean f();
    }

    public C0755a(Context context) {
        this.f10887b = ViewConfiguration.get(context).getScaledTouchSlop();
        a();
    }

    public static C0755a c(Context context) {
        return new C0755a(context);
    }

    public void a() {
        this.f10886a = null;
        e();
    }

    public boolean b() {
        return this.f10888c;
    }

    public boolean d(MotionEvent motionEvent) {
        InterfaceC0149a interfaceC0149a;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.f10888c = true;
            this.f10889d = true;
            this.f10890e = motionEvent.getEventTime();
            this.f10891f = motionEvent.getX();
            this.f10892g = motionEvent.getY();
        } else if (action == 1) {
            this.f10888c = false;
            if (Math.abs(motionEvent.getX() - this.f10891f) > this.f10887b || Math.abs(motionEvent.getY() - this.f10892g) > this.f10887b) {
                this.f10889d = false;
            }
            if (this.f10889d && motionEvent.getEventTime() - this.f10890e <= ViewConfiguration.getLongPressTimeout() && (interfaceC0149a = this.f10886a) != null) {
                interfaceC0149a.f();
            }
            this.f10889d = false;
        } else if (action != 2) {
            if (action == 3) {
                this.f10888c = false;
                this.f10889d = false;
            }
        } else if (Math.abs(motionEvent.getX() - this.f10891f) > this.f10887b || Math.abs(motionEvent.getY() - this.f10892g) > this.f10887b) {
            this.f10889d = false;
        }
        return true;
    }

    public void e() {
        this.f10888c = false;
        this.f10889d = false;
    }

    public void f(InterfaceC0149a interfaceC0149a) {
        this.f10886a = interfaceC0149a;
    }
}
