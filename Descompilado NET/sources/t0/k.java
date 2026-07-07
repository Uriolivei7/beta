package t0;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class k extends n {

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private static boolean f10671K = false;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private final Paint f10672E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private final Paint f10673F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private final Bitmap f10674G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private WeakReference f10675H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private boolean f10676I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private RectF f10677J;

    public k(Resources resources, Bitmap bitmap, Paint paint, boolean z3) {
        super(new BitmapDrawable(resources, bitmap));
        Paint paint2 = new Paint();
        this.f10672E = paint2;
        Paint paint3 = new Paint(1);
        this.f10673F = paint3;
        this.f10677J = null;
        this.f10674G = bitmap;
        if (paint != null) {
            paint2.set(paint);
        }
        paint2.setFlags(1);
        paint3.setStyle(Paint.Style.STROKE);
        this.f10676I = z3;
    }

    public static boolean k() {
        return f10671K;
    }

    private void l() {
        Shader shader;
        WeakReference weakReference = this.f10675H;
        if (weakReference == null || weakReference.get() != this.f10674G) {
            this.f10675H = new WeakReference(this.f10674G);
            if (this.f10674G != null) {
                Paint paint = this.f10672E;
                Bitmap bitmap = this.f10674G;
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                paint.setShader(new BitmapShader(bitmap, tileMode, tileMode));
                this.f10723g = true;
            }
        }
        if (this.f10723g && (shader = this.f10672E.getShader()) != null) {
            shader.setLocalMatrix(this.f10741y);
            this.f10723g = false;
        }
        this.f10672E.setFilterBitmap(b());
    }

    @Override // t0.n, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (V0.b.d()) {
            V0.b.a("RoundedBitmapDrawable#draw");
        }
        if (!e()) {
            super.draw(canvas);
            if (V0.b.d()) {
                V0.b.b();
                return;
            }
            return;
        }
        j();
        g();
        l();
        int iSave = canvas.save();
        canvas.concat(this.f10738v);
        if (this.f10676I || this.f10677J == null) {
            canvas.drawPath(this.f10722f, this.f10672E);
        } else {
            int iSave2 = canvas.save();
            canvas.clipRect(this.f10677J);
            canvas.drawPath(this.f10722f, this.f10672E);
            canvas.restoreToCount(iSave2);
        }
        float f3 = this.f10721e;
        if (f3 > 0.0f) {
            this.f10673F.setStrokeWidth(f3);
            this.f10673F.setColor(C0725e.c(this.f10724h, this.f10672E.getAlpha()));
            canvas.drawPath(this.f10725i, this.f10673F);
        }
        canvas.restoreToCount(iSave);
        if (V0.b.d()) {
            V0.b.b();
        }
    }

    @Override // t0.n
    boolean e() {
        return super.e() && this.f10674G != null;
    }

    @Override // t0.n, t0.j
    public void f(boolean z3) {
        this.f10676I = z3;
    }

    @Override // t0.n
    protected void j() {
        super.j();
        if (this.f10676I) {
            return;
        }
        if (this.f10677J == null) {
            this.f10677J = new RectF();
        }
        this.f10741y.mapRect(this.f10677J, this.f10731o);
    }

    @Override // t0.n, android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        super.setAlpha(i3);
        if (i3 != this.f10672E.getAlpha()) {
            this.f10672E.setAlpha(i3);
            super.setAlpha(i3);
            invalidateSelf();
        }
    }

    @Override // t0.n, android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
        this.f10672E.setColorFilter(colorFilter);
    }

    public k(Resources resources, Bitmap bitmap, Paint paint) {
        this(resources, bitmap, paint, f10671K);
    }

    public k(Resources resources, Bitmap bitmap) {
        this(resources, bitmap, null);
    }
}
