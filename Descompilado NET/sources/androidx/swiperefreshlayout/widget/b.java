package androidx.swiperefreshlayout.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import q.g;

/* JADX INFO: loaded from: classes.dex */
public class b extends Drawable implements Animatable {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final Interpolator f5467h = new LinearInterpolator();

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final Interpolator f5468i = new D.a();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final int[] f5469j = {-16777216};

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final c f5470b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private float f5471c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Resources f5472d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Animator f5473e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    float f5474f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    boolean f5475g;

    class a implements ValueAnimator.AnimatorUpdateListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ c f5476a;

        a(c cVar) {
            this.f5476a = cVar;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            b.this.n(fFloatValue, this.f5476a);
            b.this.b(fFloatValue, this.f5476a, false);
            b.this.invalidateSelf();
        }
    }

    /* JADX INFO: renamed from: androidx.swiperefreshlayout.widget.b$b, reason: collision with other inner class name */
    class C0085b implements Animator.AnimatorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ c f5478a;

        C0085b(c cVar) {
            this.f5478a = cVar;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            b.this.b(1.0f, this.f5478a, true);
            this.f5478a.A();
            this.f5478a.l();
            b bVar = b.this;
            if (!bVar.f5475g) {
                bVar.f5474f += 1.0f;
                return;
            }
            bVar.f5475g = false;
            animator.cancel();
            animator.setDuration(1332L);
            animator.start();
            this.f5478a.x(false);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            b.this.f5474f = 0.0f;
        }
    }

    private static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final RectF f5480a = new RectF();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final Paint f5481b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final Paint f5482c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final Paint f5483d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        float f5484e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        float f5485f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        float f5486g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        float f5487h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        int[] f5488i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        int f5489j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        float f5490k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        float f5491l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        float f5492m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        boolean f5493n;

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        Path f5494o;

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        float f5495p;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        float f5496q;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        int f5497r;

        /* JADX INFO: renamed from: s, reason: collision with root package name */
        int f5498s;

        /* JADX INFO: renamed from: t, reason: collision with root package name */
        int f5499t;

        /* JADX INFO: renamed from: u, reason: collision with root package name */
        int f5500u;

        c() {
            Paint paint = new Paint();
            this.f5481b = paint;
            Paint paint2 = new Paint();
            this.f5482c = paint2;
            Paint paint3 = new Paint();
            this.f5483d = paint3;
            this.f5484e = 0.0f;
            this.f5485f = 0.0f;
            this.f5486g = 0.0f;
            this.f5487h = 5.0f;
            this.f5495p = 1.0f;
            this.f5499t = 255;
            paint.setStrokeCap(Paint.Cap.SQUARE);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint2.setStyle(Paint.Style.FILL);
            paint2.setAntiAlias(true);
            paint3.setColor(0);
        }

        void A() {
            this.f5490k = this.f5484e;
            this.f5491l = this.f5485f;
            this.f5492m = this.f5486g;
        }

        void a(Canvas canvas, Rect rect) {
            RectF rectF = this.f5480a;
            float f3 = this.f5496q;
            float fMin = (this.f5487h / 2.0f) + f3;
            if (f3 <= 0.0f) {
                fMin = (Math.min(rect.width(), rect.height()) / 2.0f) - Math.max((this.f5497r * this.f5495p) / 2.0f, this.f5487h / 2.0f);
            }
            rectF.set(rect.centerX() - fMin, rect.centerY() - fMin, rect.centerX() + fMin, rect.centerY() + fMin);
            float f4 = this.f5484e;
            float f5 = this.f5486g;
            float f6 = (f4 + f5) * 360.0f;
            float f7 = ((this.f5485f + f5) * 360.0f) - f6;
            this.f5481b.setColor(this.f5500u);
            this.f5481b.setAlpha(this.f5499t);
            float f8 = this.f5487h / 2.0f;
            rectF.inset(f8, f8);
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2.0f, this.f5483d);
            float f9 = -f8;
            rectF.inset(f9, f9);
            canvas.drawArc(rectF, f6, f7, false, this.f5481b);
            b(canvas, f6, f7, rectF);
        }

        void b(Canvas canvas, float f3, float f4, RectF rectF) {
            if (this.f5493n) {
                Path path = this.f5494o;
                if (path == null) {
                    Path path2 = new Path();
                    this.f5494o = path2;
                    path2.setFillType(Path.FillType.EVEN_ODD);
                } else {
                    path.reset();
                }
                float fMin = Math.min(rectF.width(), rectF.height()) / 2.0f;
                float f5 = (this.f5497r * this.f5495p) / 2.0f;
                this.f5494o.moveTo(0.0f, 0.0f);
                this.f5494o.lineTo(this.f5497r * this.f5495p, 0.0f);
                Path path3 = this.f5494o;
                float f6 = this.f5497r;
                float f7 = this.f5495p;
                path3.lineTo((f6 * f7) / 2.0f, this.f5498s * f7);
                this.f5494o.offset((fMin + rectF.centerX()) - f5, rectF.centerY() + (this.f5487h / 2.0f));
                this.f5494o.close();
                this.f5482c.setColor(this.f5500u);
                this.f5482c.setAlpha(this.f5499t);
                canvas.save();
                canvas.rotate(f3 + f4, rectF.centerX(), rectF.centerY());
                canvas.drawPath(this.f5494o, this.f5482c);
                canvas.restore();
            }
        }

        int c() {
            return this.f5499t;
        }

        float d() {
            return this.f5485f;
        }

        int e() {
            return this.f5488i[f()];
        }

        int f() {
            return (this.f5489j + 1) % this.f5488i.length;
        }

        float g() {
            return this.f5484e;
        }

        int h() {
            return this.f5488i[this.f5489j];
        }

        float i() {
            return this.f5491l;
        }

        float j() {
            return this.f5492m;
        }

        float k() {
            return this.f5490k;
        }

        void l() {
            t(f());
        }

        void m() {
            this.f5490k = 0.0f;
            this.f5491l = 0.0f;
            this.f5492m = 0.0f;
            y(0.0f);
            v(0.0f);
            w(0.0f);
        }

        void n(int i3) {
            this.f5499t = i3;
        }

        void o(float f3, float f4) {
            this.f5497r = (int) f3;
            this.f5498s = (int) f4;
        }

        void p(float f3) {
            if (f3 != this.f5495p) {
                this.f5495p = f3;
            }
        }

        void q(float f3) {
            this.f5496q = f3;
        }

        void r(int i3) {
            this.f5500u = i3;
        }

        void s(ColorFilter colorFilter) {
            this.f5481b.setColorFilter(colorFilter);
        }

        void t(int i3) {
            this.f5489j = i3;
            this.f5500u = this.f5488i[i3];
        }

        void u(int[] iArr) {
            this.f5488i = iArr;
            t(0);
        }

        void v(float f3) {
            this.f5485f = f3;
        }

        void w(float f3) {
            this.f5486g = f3;
        }

        void x(boolean z3) {
            if (this.f5493n != z3) {
                this.f5493n = z3;
            }
        }

        void y(float f3) {
            this.f5484e = f3;
        }

        void z(float f3) {
            this.f5487h = f3;
            this.f5481b.setStrokeWidth(f3);
        }
    }

    public b(Context context) {
        this.f5472d = ((Context) g.g(context)).getResources();
        c cVar = new c();
        this.f5470b = cVar;
        cVar.u(f5469j);
        k(2.5f);
        m();
    }

    private void a(float f3, c cVar) {
        n(f3, cVar);
        float fFloor = (float) (Math.floor(cVar.j() / 0.8f) + 1.0d);
        cVar.y(cVar.k() + (((cVar.i() - 0.01f) - cVar.k()) * f3));
        cVar.v(cVar.i());
        cVar.w(cVar.j() + ((fFloor - cVar.j()) * f3));
    }

    private int c(float f3, int i3, int i4) {
        return ((((i3 >> 24) & 255) + ((int) ((((i4 >> 24) & 255) - r0) * f3))) << 24) | ((((i3 >> 16) & 255) + ((int) ((((i4 >> 16) & 255) - r1) * f3))) << 16) | ((((i3 >> 8) & 255) + ((int) ((((i4 >> 8) & 255) - r2) * f3))) << 8) | ((i3 & 255) + ((int) (f3 * ((i4 & 255) - r8))));
    }

    private void h(float f3) {
        this.f5471c = f3;
    }

    private void i(float f3, float f4, float f5, float f6) {
        c cVar = this.f5470b;
        float f7 = this.f5472d.getDisplayMetrics().density;
        cVar.z(f4 * f7);
        cVar.q(f3 * f7);
        cVar.t(0);
        cVar.o(f5 * f7, f6 * f7);
    }

    private void m() {
        c cVar = this.f5470b;
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimatorOfFloat.addUpdateListener(new a(cVar));
        valueAnimatorOfFloat.setRepeatCount(-1);
        valueAnimatorOfFloat.setRepeatMode(1);
        valueAnimatorOfFloat.setInterpolator(f5467h);
        valueAnimatorOfFloat.addListener(new C0085b(cVar));
        this.f5473e = valueAnimatorOfFloat;
    }

    void b(float f3, c cVar, boolean z3) {
        float interpolation;
        float interpolation2;
        if (this.f5475g) {
            a(f3, cVar);
            return;
        }
        if (f3 != 1.0f || z3) {
            float fJ = cVar.j();
            if (f3 < 0.5f) {
                interpolation = cVar.k();
                interpolation2 = (f5468i.getInterpolation(f3 / 0.5f) * 0.79f) + 0.01f + interpolation;
            } else {
                float fK = cVar.k() + 0.79f;
                interpolation = fK - (((1.0f - f5468i.getInterpolation((f3 - 0.5f) / 0.5f)) * 0.79f) + 0.01f);
                interpolation2 = fK;
            }
            float f4 = fJ + (0.20999998f * f3);
            float f5 = (f3 + this.f5474f) * 216.0f;
            cVar.y(interpolation);
            cVar.v(interpolation2);
            cVar.w(f4);
            h(f5);
        }
    }

    public void d(boolean z3) {
        this.f5470b.x(z3);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.save();
        canvas.rotate(this.f5471c, bounds.exactCenterX(), bounds.exactCenterY());
        this.f5470b.a(canvas, bounds);
        canvas.restore();
    }

    public void e(float f3) {
        this.f5470b.p(f3);
        invalidateSelf();
    }

    public void f(int... iArr) {
        this.f5470b.u(iArr);
        this.f5470b.t(0);
        invalidateSelf();
    }

    public void g(float f3) {
        this.f5470b.w(f3);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f5470b.c();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Animatable
    public boolean isRunning() {
        return this.f5473e.isRunning();
    }

    public void j(float f3, float f4) {
        this.f5470b.y(f3);
        this.f5470b.v(f4);
        invalidateSelf();
    }

    public void k(float f3) {
        this.f5470b.z(f3);
        invalidateSelf();
    }

    public void l(int i3) {
        if (i3 == 0) {
            i(11.0f, 3.0f, 12.0f, 6.0f);
        } else {
            i(7.5f, 2.5f, 10.0f, 5.0f);
        }
        invalidateSelf();
    }

    void n(float f3, c cVar) {
        if (f3 > 0.75f) {
            cVar.r(c((f3 - 0.75f) / 0.25f, cVar.h(), cVar.e()));
        } else {
            cVar.r(cVar.h());
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.f5470b.n(i3);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f5470b.s(colorFilter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Animatable
    public void start() {
        this.f5473e.cancel();
        this.f5470b.A();
        if (this.f5470b.d() != this.f5470b.g()) {
            this.f5475g = true;
            this.f5473e.setDuration(666L);
            this.f5473e.start();
        } else {
            this.f5470b.t(0);
            this.f5470b.m();
            this.f5473e.setDuration(1332L);
            this.f5473e.start();
        }
    }

    @Override // android.graphics.drawable.Animatable
    public void stop() {
        this.f5473e.cancel();
        h(0.0f);
        this.f5470b.x(false);
        this.f5470b.t(0);
        this.f5470b.m();
        invalidateSelf();
    }
}
