package androidx.core.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.core.view.Z;

/* JADX INFO: loaded from: classes.dex */
public abstract class a implements View.OnTouchListener {

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private static final int f4707s = ViewConfiguration.getTapTimeout();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final View f4710d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Runnable f4711e;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f4714h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f4715i;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f4719m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    boolean f4720n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    boolean f4721o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    boolean f4722p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private boolean f4723q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private boolean f4724r;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final C0069a f4708b = new C0069a();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Interpolator f4709c = new AccelerateInterpolator();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private float[] f4712f = {0.0f, 0.0f};

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private float[] f4713g = {Float.MAX_VALUE, Float.MAX_VALUE};

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private float[] f4716j = {0.0f, 0.0f};

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private float[] f4717k = {0.0f, 0.0f};

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private float[] f4718l = {Float.MAX_VALUE, Float.MAX_VALUE};

    /* JADX INFO: renamed from: androidx.core.widget.a$a, reason: collision with other inner class name */
    private static class C0069a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f4725a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f4726b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private float f4727c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private float f4728d;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private float f4734j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private int f4735k;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private long f4729e = Long.MIN_VALUE;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private long f4733i = -1;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private long f4730f = 0;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private int f4731g = 0;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private int f4732h = 0;

        C0069a() {
        }

        private float e(long j3) {
            if (j3 < this.f4729e) {
                return 0.0f;
            }
            long j4 = this.f4733i;
            if (j4 < 0 || j3 < j4) {
                return a.e((j3 - r0) / this.f4725a, 0.0f, 1.0f) * 0.5f;
            }
            float f3 = this.f4734j;
            return (1.0f - f3) + (f3 * a.e((j3 - j4) / this.f4735k, 0.0f, 1.0f));
        }

        private float g(float f3) {
            return ((-4.0f) * f3 * f3) + (f3 * 4.0f);
        }

        public void a() {
            if (this.f4730f == 0) {
                throw new RuntimeException("Cannot compute scroll delta before calling start()");
            }
            long jCurrentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            float fG = g(e(jCurrentAnimationTimeMillis));
            long j3 = jCurrentAnimationTimeMillis - this.f4730f;
            this.f4730f = jCurrentAnimationTimeMillis;
            float f3 = j3 * fG;
            this.f4731g = (int) (this.f4727c * f3);
            this.f4732h = (int) (f3 * this.f4728d);
        }

        public int b() {
            return this.f4731g;
        }

        public int c() {
            return this.f4732h;
        }

        public int d() {
            float f3 = this.f4727c;
            return (int) (f3 / Math.abs(f3));
        }

        public int f() {
            float f3 = this.f4728d;
            return (int) (f3 / Math.abs(f3));
        }

        public boolean h() {
            return this.f4733i > 0 && AnimationUtils.currentAnimationTimeMillis() > this.f4733i + ((long) this.f4735k);
        }

        public void i() {
            long jCurrentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.f4735k = a.f((int) (jCurrentAnimationTimeMillis - this.f4729e), 0, this.f4726b);
            this.f4734j = e(jCurrentAnimationTimeMillis);
            this.f4733i = jCurrentAnimationTimeMillis;
        }

        public void j(int i3) {
            this.f4726b = i3;
        }

        public void k(int i3) {
            this.f4725a = i3;
        }

        public void l(float f3, float f4) {
            this.f4727c = f3;
            this.f4728d = f4;
        }

        public void m() {
            long jCurrentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.f4729e = jCurrentAnimationTimeMillis;
            this.f4733i = -1L;
            this.f4730f = jCurrentAnimationTimeMillis;
            this.f4734j = 0.5f;
            this.f4731g = 0;
            this.f4732h = 0;
        }
    }

    private class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            a aVar = a.this;
            if (aVar.f4722p) {
                if (aVar.f4720n) {
                    aVar.f4720n = false;
                    aVar.f4708b.m();
                }
                C0069a c0069a = a.this.f4708b;
                if (c0069a.h() || !a.this.u()) {
                    a.this.f4722p = false;
                    return;
                }
                a aVar2 = a.this;
                if (aVar2.f4721o) {
                    aVar2.f4721o = false;
                    aVar2.c();
                }
                c0069a.a();
                a.this.j(c0069a.b(), c0069a.c());
                Z.S(a.this.f4710d, this);
            }
        }
    }

    public a(View view) {
        this.f4710d = view;
        float f3 = Resources.getSystem().getDisplayMetrics().density;
        float f4 = (int) ((1575.0f * f3) + 0.5f);
        o(f4, f4);
        float f5 = (int) ((f3 * 315.0f) + 0.5f);
        p(f5, f5);
        l(1);
        n(Float.MAX_VALUE, Float.MAX_VALUE);
        s(0.2f, 0.2f);
        t(1.0f, 1.0f);
        k(f4707s);
        r(500);
        q(500);
    }

    private float d(int i3, float f3, float f4, float f5) {
        float fH = h(this.f4712f[i3], f4, this.f4713g[i3], f3);
        if (fH == 0.0f) {
            return 0.0f;
        }
        float f6 = this.f4716j[i3];
        float f7 = this.f4717k[i3];
        float f8 = this.f4718l[i3];
        float f9 = f6 * f5;
        return fH > 0.0f ? e(fH * f9, f7, f8) : -e((-fH) * f9, f7, f8);
    }

    static float e(float f3, float f4, float f5) {
        return f3 > f5 ? f5 : f3 < f4 ? f4 : f3;
    }

    static int f(int i3, int i4, int i5) {
        return i3 > i5 ? i5 : i3 < i4 ? i4 : i3;
    }

    private float g(float f3, float f4) {
        if (f4 == 0.0f) {
            return 0.0f;
        }
        int i3 = this.f4714h;
        if (i3 == 0 || i3 == 1) {
            if (f3 < f4) {
                if (f3 >= 0.0f) {
                    return 1.0f - (f3 / f4);
                }
                if (this.f4722p && i3 == 1) {
                    return 1.0f;
                }
            }
        } else if (i3 == 2 && f3 < 0.0f) {
            return f3 / (-f4);
        }
        return 0.0f;
    }

    private float h(float f3, float f4, float f5, float f6) {
        float interpolation;
        float fE = e(f3 * f4, 0.0f, f5);
        float fG = g(f4 - f6, fE) - g(f6, fE);
        if (fG < 0.0f) {
            interpolation = -this.f4709c.getInterpolation(-fG);
        } else {
            if (fG <= 0.0f) {
                return 0.0f;
            }
            interpolation = this.f4709c.getInterpolation(fG);
        }
        return e(interpolation, -1.0f, 1.0f);
    }

    private void i() {
        if (this.f4720n) {
            this.f4722p = false;
        } else {
            this.f4708b.i();
        }
    }

    private void v() {
        int i3;
        if (this.f4711e == null) {
            this.f4711e = new b();
        }
        this.f4722p = true;
        this.f4720n = true;
        if (this.f4719m || (i3 = this.f4715i) <= 0) {
            this.f4711e.run();
        } else {
            Z.T(this.f4710d, this.f4711e, i3);
        }
        this.f4719m = true;
    }

    public abstract boolean a(int i3);

    public abstract boolean b(int i3);

    void c() {
        long jUptimeMillis = SystemClock.uptimeMillis();
        MotionEvent motionEventObtain = MotionEvent.obtain(jUptimeMillis, jUptimeMillis, 3, 0.0f, 0.0f, 0);
        this.f4710d.onTouchEvent(motionEventObtain);
        motionEventObtain.recycle();
    }

    public abstract void j(int i3, int i4);

    public a k(int i3) {
        this.f4715i = i3;
        return this;
    }

    public a l(int i3) {
        this.f4714h = i3;
        return this;
    }

    public a m(boolean z3) {
        if (this.f4723q && !z3) {
            i();
        }
        this.f4723q = z3;
        return this;
    }

    public a n(float f3, float f4) {
        float[] fArr = this.f4713g;
        fArr[0] = f3;
        fArr[1] = f4;
        return this;
    }

    public a o(float f3, float f4) {
        float[] fArr = this.f4718l;
        fArr[0] = f3 / 1000.0f;
        fArr[1] = f4 / 1000.0f;
        return this;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0016  */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouch(android.view.View r6, android.view.MotionEvent r7) {
        /*
            r5 = this;
            boolean r0 = r5.f4723q
            r1 = 0
            if (r0 != 0) goto L6
            return r1
        L6:
            int r0 = r7.getActionMasked()
            r2 = 1
            if (r0 == 0) goto L1a
            if (r0 == r2) goto L16
            r3 = 2
            if (r0 == r3) goto L1e
            r6 = 3
            if (r0 == r6) goto L16
            goto L58
        L16:
            r5.i()
            goto L58
        L1a:
            r5.f4721o = r2
            r5.f4719m = r1
        L1e:
            float r0 = r7.getX()
            int r3 = r6.getWidth()
            float r3 = (float) r3
            android.view.View r4 = r5.f4710d
            int r4 = r4.getWidth()
            float r4 = (float) r4
            float r0 = r5.d(r1, r0, r3, r4)
            float r7 = r7.getY()
            int r6 = r6.getHeight()
            float r6 = (float) r6
            android.view.View r3 = r5.f4710d
            int r3 = r3.getHeight()
            float r3 = (float) r3
            float r6 = r5.d(r2, r7, r6, r3)
            androidx.core.widget.a$a r7 = r5.f4708b
            r7.l(r0, r6)
            boolean r6 = r5.f4722p
            if (r6 != 0) goto L58
            boolean r6 = r5.u()
            if (r6 == 0) goto L58
            r5.v()
        L58:
            boolean r6 = r5.f4724r
            if (r6 == 0) goto L61
            boolean r6 = r5.f4722p
            if (r6 == 0) goto L61
            r1 = r2
        L61:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.widget.a.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    public a p(float f3, float f4) {
        float[] fArr = this.f4717k;
        fArr[0] = f3 / 1000.0f;
        fArr[1] = f4 / 1000.0f;
        return this;
    }

    public a q(int i3) {
        this.f4708b.j(i3);
        return this;
    }

    public a r(int i3) {
        this.f4708b.k(i3);
        return this;
    }

    public a s(float f3, float f4) {
        float[] fArr = this.f4712f;
        fArr[0] = f3;
        fArr[1] = f4;
        return this;
    }

    public a t(float f3, float f4) {
        float[] fArr = this.f4716j;
        fArr[0] = f3 / 1000.0f;
        fArr[1] = f4 / 1000.0f;
        return this;
    }

    boolean u() {
        C0069a c0069a = this.f4708b;
        int iF = c0069a.f();
        int iD = c0069a.d();
        return (iF != 0 && b(iF)) || (iD != 0 && a(iD));
    }
}
