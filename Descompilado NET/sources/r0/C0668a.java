package r0;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import s0.InterfaceC0691b;
import t0.r;

/* JADX INFO: renamed from: r0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0668a extends Drawable implements InterfaceC0691b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f10490b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f10491c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f10492d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f10493e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f10494f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private String f10495g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private r f10496h;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f10498j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f10499k;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private int f10505q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f10506r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private int f10507s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private int f10508t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private int f10509u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private long f10510v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private String f10511w;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private HashMap f10497i = new HashMap();

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f10500l = 80;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final Paint f10501m = new Paint(1);

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final Matrix f10502n = new Matrix();

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final Rect f10503o = new Rect();

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final RectF f10504p = new RectF();

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private int f10512x = -1;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private int f10513y = 0;

    public C0668a() {
        i();
    }

    private void c(Canvas canvas, String str, Object obj) {
        e(canvas, str, String.valueOf(obj), -1);
    }

    private void d(Canvas canvas, String str, String str2) {
        e(canvas, str, str2, -1);
    }

    private void e(Canvas canvas, String str, String str2, int i3) {
        String str3 = str + ": ";
        float fMeasureText = this.f10501m.measureText(str3);
        float fMeasureText2 = this.f10501m.measureText(str2);
        this.f10501m.setColor(1711276032);
        int i4 = this.f10508t;
        int i5 = this.f10509u;
        canvas.drawRect(i4 - 4, i5 + 8, i4 + fMeasureText + fMeasureText2 + 4.0f, i5 + this.f10507s + 8, this.f10501m);
        this.f10501m.setColor(-1);
        canvas.drawText(str3, this.f10508t, this.f10509u, this.f10501m);
        this.f10501m.setColor(i3);
        canvas.drawText(str2, this.f10508t + fMeasureText, this.f10509u, this.f10501m);
        this.f10509u += this.f10507s;
    }

    private static String g(String str, Object... objArr) {
        return objArr == null ? str : String.format(Locale.US, str, objArr);
    }

    private void h(Rect rect, int i3, int i4) {
        int iMin = Math.min(40, Math.max(10, Math.min(rect.width() / i4, rect.height() / i3)));
        this.f10501m.setTextSize(iMin);
        int i5 = iMin + 8;
        this.f10507s = i5;
        int i6 = this.f10500l;
        if (i6 == 80) {
            this.f10507s = i5 * (-1);
        }
        this.f10505q = rect.left + 10;
        this.f10506r = i6 == 80 ? rect.bottom - 10 : rect.top + 20;
    }

    @Override // s0.InterfaceC0691b
    public void a(long j3) {
        this.f10510v = j3;
        invalidateSelf();
    }

    public void b(String str, String str2) {
        this.f10497i.put(str, str2);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        this.f10501m.setStyle(Paint.Style.STROKE);
        this.f10501m.setStrokeWidth(2.0f);
        this.f10501m.setColor(-26624);
        canvas.drawRect(bounds.left, bounds.top, bounds.right, bounds.bottom, this.f10501m);
        Paint paint = this.f10501m;
        Paint.Style style = Paint.Style.FILL;
        paint.setStyle(style);
        this.f10501m.setColor(this.f10513y);
        canvas.drawRect(bounds.left, bounds.top, bounds.right, bounds.bottom, this.f10501m);
        this.f10501m.setStyle(style);
        this.f10501m.setStrokeWidth(0.0f);
        this.f10501m.setColor(-1);
        this.f10508t = this.f10505q;
        this.f10509u = this.f10506r;
        String str = this.f10491c;
        if (str != null) {
            d(canvas, "IDs", g("%s, %s", this.f10490b, str));
        } else {
            d(canvas, "ID", this.f10490b);
        }
        d(canvas, "D", g("%dx%d", Integer.valueOf(bounds.width()), Integer.valueOf(bounds.height())));
        if (bounds.height() > 0) {
            c(canvas, "DAR", Float.valueOf(bounds.width() / bounds.height()));
        }
        e(canvas, "I", g("%dx%d", Integer.valueOf(this.f10492d), Integer.valueOf(this.f10493e)), f(this.f10492d, this.f10493e, this.f10496h));
        int i3 = this.f10493e;
        if (i3 > 0) {
            c(canvas, "IAR", Float.valueOf(this.f10492d / i3));
        }
        d(canvas, "I", g("%d KiB", Integer.valueOf(this.f10494f / 1024)));
        String str2 = this.f10495g;
        if (str2 != null) {
            d(canvas, "i format", str2);
        }
        int i4 = this.f10498j;
        if (i4 > 0) {
            d(canvas, "anim", g("f %d, l %d", Integer.valueOf(i4), Integer.valueOf(this.f10499k)));
        }
        r rVar = this.f10496h;
        if (rVar != null) {
            c(canvas, "scale", rVar);
        }
        long j3 = this.f10510v;
        if (j3 >= 0) {
            d(canvas, "t", g("%d ms", Long.valueOf(j3)));
        }
        String str3 = this.f10511w;
        if (str3 != null) {
            e(canvas, "origin", str3, this.f10512x);
        }
        for (Map.Entry entry : this.f10497i.entrySet()) {
            d(canvas, (String) entry.getKey(), (String) entry.getValue());
        }
    }

    int f(int i3, int i4, r rVar) {
        int iWidth = getBounds().width();
        int iHeight = getBounds().height();
        if (iWidth > 0 && iHeight > 0 && i3 > 0 && i4 > 0) {
            if (rVar != null) {
                Rect rect = this.f10503o;
                rect.top = 0;
                rect.left = 0;
                rect.right = iWidth;
                rect.bottom = iHeight;
                this.f10502n.reset();
                rVar.a(this.f10502n, this.f10503o, i3, i4, 0.0f, 0.0f);
                RectF rectF = this.f10504p;
                rectF.top = 0.0f;
                rectF.left = 0.0f;
                rectF.right = i3;
                rectF.bottom = i4;
                this.f10502n.mapRect(rectF);
                int iWidth2 = (int) this.f10504p.width();
                int iHeight2 = (int) this.f10504p.height();
                iWidth = Math.min(iWidth, iWidth2);
                iHeight = Math.min(iHeight, iHeight2);
            }
            float f3 = iWidth;
            float f4 = f3 * 0.1f;
            float f5 = f3 * 0.5f;
            float f6 = iHeight;
            float f7 = 0.1f * f6;
            float f8 = f6 * 0.5f;
            int iAbs = Math.abs(i3 - iWidth);
            int iAbs2 = Math.abs(i4 - iHeight);
            float f9 = iAbs;
            if (f9 < f4 && iAbs2 < f7) {
                return -16711936;
            }
            if (f9 < f5 && iAbs2 < f8) {
                return -256;
            }
        }
        return -65536;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public void i() {
        this.f10492d = -1;
        this.f10493e = -1;
        this.f10494f = -1;
        this.f10497i = new HashMap();
        this.f10498j = -1;
        this.f10499k = -1;
        this.f10495g = null;
        j(null);
        this.f10510v = -1L;
        this.f10511w = null;
        this.f10512x = -1;
        invalidateSelf();
    }

    public void j(String str) {
        if (str == null) {
            str = "none";
        }
        this.f10490b = str;
        invalidateSelf();
    }

    public void k(int i3, int i4) {
        this.f10492d = i3;
        this.f10493e = i4;
        invalidateSelf();
    }

    public void l(int i3) {
        this.f10494f = i3;
    }

    public void m(r rVar) {
        this.f10496h = rVar;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        h(rect, 9, 8);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }
}
