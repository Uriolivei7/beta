package g2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.TypedValue;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class h implements View.OnTouchListener {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private f2.h f9434b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private View f9435c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f9436d = true;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Rect f9437e = new Rect();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f9438f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f9439g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f9440h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f9441i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f9442j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f9443k;

    class a implements View.OnLayoutChangeListener {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ float f9444b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f9445c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ float f9446d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ long f9447e;

        a(float f3, int i3, float f4, long j3) {
            this.f9444b = f3;
            this.f9445c = i3;
            this.f9446d = f4;
            this.f9447e = j3;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void c() {
            h.this.s();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void d(float f3, int i3, float f4, View view) {
            h.this.u();
            float f5 = h.this.f9438f * f3;
            float f6 = i3 / 2.0f;
            h.this.w(Math.max((int) (f5 - f6), 0), Math.max((int) ((h.this.f9439g * f4) - f6), 0));
            view.post(new Runnable() { // from class: g2.g
                @Override // java.lang.Runnable
                public final void run() {
                    this.f9433b.c();
                }
            });
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(final View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            view.removeOnLayoutChangeListener(this);
            final float f3 = this.f9444b;
            final int i11 = this.f9445c;
            final float f4 = this.f9446d;
            view.postDelayed(new Runnable() { // from class: g2.f
                @Override // java.lang.Runnable
                public final void run() {
                    this.f9428b.d(f3, i11, f4, view);
                }
            }, this.f9447e);
        }
    }

    public static Rect i(Window window) {
        if (Build.VERSION.SDK_INT < 28) {
            return null;
        }
        View decorView = window != null ? window.getDecorView() : null;
        WindowInsets rootWindowInsets = decorView != null ? decorView.getRootWindowInsets() : null;
        DisplayCutout displayCutout = rootWindowInsets != null ? rootWindowInsets.getDisplayCutout() : null;
        if (displayCutout != null) {
            return new Rect(displayCutout.getSafeInsetLeft(), displayCutout.getSafeInsetTop(), displayCutout.getSafeInsetRight(), displayCutout.getSafeInsetBottom());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p() {
        u();
        t();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void q() {
        u();
        t();
    }

    public View e() {
        return this.f9435c;
    }

    public f2.h f() {
        return this.f9434b;
    }

    protected float g() {
        return TypedValue.applyDimension(1, 1.0f, Resources.getSystem().getDisplayMetrics());
    }

    public Rect h() {
        Window window;
        Context contextH = this.f9434b.h();
        if ((contextH instanceof Activity) && (window = ((Activity) contextH).getWindow()) != null) {
            return i(window);
        }
        return null;
    }

    public int j() {
        return this.f9439g;
    }

    public int k() {
        return this.f9443k;
    }

    public int l() {
        return this.f9442j;
    }

    public int m() {
        return this.f9438f;
    }

    public boolean n() {
        return this.f9436d;
    }

    public boolean o() {
        return true;
    }

    public void r() {
        if (!o()) {
            f().r(new Runnable() { // from class: g2.d
                @Override // java.lang.Runnable
                public final void run() {
                    this.f9426b.p();
                }
            }, 100L);
            return;
        }
        int width = e().getWidth();
        int height = e().getHeight();
        int i3 = this.f9440h - this.f9442j;
        int i4 = this.f9441i - this.f9443k;
        float fG = g();
        float f3 = i3;
        float f4 = 1.0f;
        float f5 = f3 <= fG ? 0.0f : ((float) Math.abs(this.f9438f - (i3 + width))) < fG ? 1.0f : (f3 + (width / 2.0f)) / this.f9438f;
        float f6 = i4;
        if (f6 <= fG) {
            f4 = 0.0f;
        } else if (Math.abs(this.f9439g - (i4 + height)) >= fG) {
            f4 = (f6 + (height / 2.0f)) / this.f9439g;
        }
        View viewE = e();
        if (viewE == null) {
            return;
        }
        viewE.addOnLayoutChangeListener(new a(f5, width, f4, 100L));
    }

    protected void s() {
        u();
        t();
    }

    public void t() {
        View viewE = e();
        if (viewE == null) {
            return;
        }
        int[] iArr = new int[2];
        viewE.getLocationOnScreen(iArr);
        this.f9440h = iArr[0];
        this.f9441i = iArr[1];
    }

    public void u() {
        View viewE = e();
        if (viewE == null) {
            return;
        }
        viewE.getWindowVisibleDisplayFrame(this.f9437e);
        Rect rect = this.f9437e;
        int i3 = rect.right;
        int i4 = rect.left;
        this.f9438f = i3 - i4;
        int i5 = rect.bottom;
        int i6 = rect.top;
        this.f9439g = i5 - i6;
        this.f9442j = i4;
        this.f9443k = i6;
    }

    public void v(f2.h hVar) {
        this.f9434b = hVar;
        View viewI = hVar.i();
        this.f9435c = viewI;
        viewI.setOnTouchListener(this);
        this.f9435c.post(new Runnable() { // from class: g2.e
            @Override // java.lang.Runnable
            public final void run() {
                this.f9427b.q();
            }
        });
    }

    public void w(float f3, float f4) {
        x(f3, f4, n());
    }

    public void x(float f3, float f4, boolean z3) {
        y((int) f3, (int) f4, z3);
    }

    public void y(int i3, int i4, boolean z3) {
        if (z3) {
            z(i3, i4);
            return;
        }
        Rect rectH = h();
        if (rectH == null) {
            z(i3, i4);
            return;
        }
        if (rectH.left > 0 && rectH.right > 0 && rectH.top > 0 && rectH.bottom > 0) {
            z(i3, i4);
            return;
        }
        int iK = this.f9434b.k();
        int iJ = this.f9434b.j();
        int iM = m();
        int iJ2 = j();
        if (i3 < rectH.left - l()) {
            i3 = rectH.left - l();
        } else {
            int i5 = rectH.right;
            if (i3 > (iM - i5) - iK) {
                i3 = (iM - i5) - iK;
            }
        }
        if (i4 < rectH.top - k()) {
            i4 = rectH.top - k();
        } else {
            int i6 = rectH.bottom;
            if (i4 > (iJ2 - i6) - iJ) {
                i4 = (iJ2 - i6) - iJ;
            }
        }
        z(i3, i4);
    }

    public void z(int i3, int i4) {
        WindowManager.LayoutParams layoutParamsL = this.f9434b.l();
        if (layoutParamsL == null) {
            return;
        }
        if (layoutParamsL.gravity == 8388659 && layoutParamsL.x == i3 && layoutParamsL.y == i4) {
            return;
        }
        layoutParamsL.x = i3;
        layoutParamsL.y = i4;
        layoutParamsL.gravity = 8388659;
        this.f9434b.K();
        t();
    }
}
