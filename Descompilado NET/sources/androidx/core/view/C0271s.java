package androidx.core.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/* JADX INFO: renamed from: androidx.core.view.s, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0271s {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f4658a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final InterfaceC0272t f4659b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final b f4660c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final a f4661d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private VelocityTracker f4662e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private float f4663f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f4664g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f4665h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f4666i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final int[] f4667j;

    /* JADX INFO: renamed from: androidx.core.view.s$a */
    interface a {
        float a(VelocityTracker velocityTracker, MotionEvent motionEvent, int i3);
    }

    /* JADX INFO: renamed from: androidx.core.view.s$b */
    interface b {
        void a(Context context, int[] iArr, MotionEvent motionEvent, int i3);
    }

    public C0271s(Context context, InterfaceC0272t interfaceC0272t) {
        this(context, interfaceC0272t, new b() { // from class: androidx.core.view.q
            @Override // androidx.core.view.C0271s.b
            public final void a(Context context2, int[] iArr, MotionEvent motionEvent, int i3) {
                C0271s.c(context2, iArr, motionEvent, i3);
            }
        }, new a() { // from class: androidx.core.view.r
            @Override // androidx.core.view.C0271s.a
            public final float a(VelocityTracker velocityTracker, MotionEvent motionEvent, int i3) {
                return C0271s.f(velocityTracker, motionEvent, i3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void c(Context context, int[] iArr, MotionEvent motionEvent, int i3) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        iArr[0] = AbstractC0244d0.g(context, viewConfiguration, motionEvent.getDeviceId(), i3, motionEvent.getSource());
        iArr[1] = AbstractC0244d0.f(context, viewConfiguration, motionEvent.getDeviceId(), i3, motionEvent.getSource());
    }

    private boolean d(MotionEvent motionEvent, int i3) {
        int source = motionEvent.getSource();
        int deviceId = motionEvent.getDeviceId();
        if (this.f4665h == source && this.f4666i == deviceId && this.f4664g == i3) {
            return false;
        }
        this.f4660c.a(this.f4658a, this.f4667j, motionEvent, i3);
        this.f4665h = source;
        this.f4666i = deviceId;
        this.f4664g = i3;
        return true;
    }

    private float e(MotionEvent motionEvent, int i3) {
        if (this.f4662e == null) {
            this.f4662e = VelocityTracker.obtain();
        }
        return this.f4661d.a(this.f4662e, motionEvent, i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float f(VelocityTracker velocityTracker, MotionEvent motionEvent, int i3) {
        W.a(velocityTracker, motionEvent);
        W.b(velocityTracker, 1000);
        return W.d(velocityTracker, i3);
    }

    public void g(MotionEvent motionEvent, int i3) {
        boolean zD = d(motionEvent, i3);
        if (this.f4667j[0] == Integer.MAX_VALUE) {
            VelocityTracker velocityTracker = this.f4662e;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.f4662e = null;
                return;
            }
            return;
        }
        float fE = e(motionEvent, i3) * this.f4659b.b();
        float fSignum = Math.signum(fE);
        if (zD || (fSignum != Math.signum(this.f4663f) && fSignum != 0.0f)) {
            this.f4659b.c();
        }
        float fAbs = Math.abs(fE);
        int[] iArr = this.f4667j;
        if (fAbs < iArr[0]) {
            return;
        }
        float fMax = Math.max(-r6, Math.min(fE, iArr[1]));
        this.f4663f = this.f4659b.a(fMax) ? fMax : 0.0f;
    }

    C0271s(Context context, InterfaceC0272t interfaceC0272t, b bVar, a aVar) {
        this.f4664g = -1;
        this.f4665h = -1;
        this.f4666i = -1;
        this.f4667j = new int[]{Integer.MAX_VALUE, 0};
        this.f4658a = context;
        this.f4659b = interfaceC0272t;
        this.f4660c = bVar;
        this.f4661d = aVar;
    }
}
