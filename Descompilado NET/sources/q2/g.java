package q2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import q2.r;

/* JADX INFO: loaded from: classes.dex */
public final class g implements InterfaceC0654b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final InterfaceC0653a f10399b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private C0656d f10400c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Bitmap f10401d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    final View f10402e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f10403f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final ViewGroup f10404g;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f10409l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private Drawable f10410m;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private float f10398a = 16.0f;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final int[] f10405h = new int[2];

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final int[] f10406i = new int[2];

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final ViewTreeObserver.OnPreDrawListener f10407j = new a();

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f10408k = true;

    class a implements ViewTreeObserver.OnPreDrawListener {
        a() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            g.this.k();
            return true;
        }
    }

    public g(View view, ViewGroup viewGroup, int i3, InterfaceC0653a interfaceC0653a) {
        this.f10404g = viewGroup;
        this.f10402e = view;
        this.f10403f = i3;
        this.f10399b = interfaceC0653a;
        if (interfaceC0653a instanceof p) {
            ((p) interfaceC0653a).f(view.getContext());
        }
        i(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    private void h() {
        this.f10401d = this.f10399b.e(this.f10401d, this.f10398a);
        if (this.f10399b.b()) {
            return;
        }
        this.f10400c.setBitmap(this.f10401d);
    }

    private void j() {
        this.f10404g.getLocationOnScreen(this.f10405h);
        this.f10402e.getLocationOnScreen(this.f10406i);
        int[] iArr = this.f10406i;
        int i3 = iArr[0];
        int[] iArr2 = this.f10405h;
        int i4 = i3 - iArr2[0];
        int i5 = iArr[1] - iArr2[1];
        float height = this.f10402e.getHeight() / this.f10401d.getHeight();
        float width = this.f10402e.getWidth() / this.f10401d.getWidth();
        this.f10400c.translate((-i4) / width, (-i5) / height);
        this.f10400c.scale(1.0f / width, 1.0f / height);
    }

    @Override // q2.InterfaceC0657e
    public InterfaceC0657e a(Drawable drawable) {
        this.f10410m = drawable;
        return this;
    }

    @Override // q2.InterfaceC0654b
    public void b() {
        i(this.f10402e.getMeasuredWidth(), this.f10402e.getMeasuredHeight());
    }

    @Override // q2.InterfaceC0654b
    public boolean c(Canvas canvas) {
        if (this.f10408k && this.f10409l) {
            if (canvas instanceof C0656d) {
                return false;
            }
            float width = this.f10402e.getWidth() / this.f10401d.getWidth();
            canvas.save();
            canvas.scale(width, this.f10402e.getHeight() / this.f10401d.getHeight());
            this.f10399b.d(canvas, this.f10401d);
            canvas.restore();
            int i3 = this.f10403f;
            if (i3 != 0) {
                canvas.drawColor(i3);
            }
        }
        return true;
    }

    @Override // q2.InterfaceC0657e
    public InterfaceC0657e d(boolean z3) {
        this.f10408k = z3;
        f(z3);
        this.f10402e.invalidate();
        return this;
    }

    @Override // q2.InterfaceC0654b
    public void destroy() {
        f(false);
        this.f10399b.destroy();
        this.f10409l = false;
    }

    @Override // q2.InterfaceC0657e
    public InterfaceC0657e e(float f3) {
        this.f10398a = f3;
        return this;
    }

    @Override // q2.InterfaceC0657e
    public InterfaceC0657e f(boolean z3) {
        this.f10404g.getViewTreeObserver().removeOnPreDrawListener(this.f10407j);
        if (z3) {
            this.f10404g.getViewTreeObserver().addOnPreDrawListener(this.f10407j);
        }
        return this;
    }

    @Override // q2.InterfaceC0657e
    public InterfaceC0657e g(int i3) {
        if (this.f10403f != i3) {
            this.f10403f = i3;
            this.f10402e.invalidate();
        }
        return this;
    }

    void i(int i3, int i4) {
        f(true);
        r rVar = new r(this.f10399b.c());
        if (rVar.b(i3, i4)) {
            this.f10402e.setWillNotDraw(true);
            return;
        }
        this.f10402e.setWillNotDraw(false);
        r.a aVarD = rVar.d(i3, i4);
        this.f10401d = Bitmap.createBitmap(aVarD.f10427a, aVarD.f10428b, this.f10399b.a());
        this.f10400c = new C0656d(this.f10401d);
        this.f10409l = true;
        k();
    }

    void k() {
        if (this.f10408k && this.f10409l) {
            Drawable drawable = this.f10410m;
            if (drawable == null) {
                this.f10401d.eraseColor(0);
            } else {
                drawable.draw(this.f10400c);
            }
            this.f10400c.save();
            j();
            this.f10404g.draw(this.f10400c);
            this.f10400c.restore();
            h();
        }
    }
}
