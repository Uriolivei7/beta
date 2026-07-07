package O1;

import D2.s;
import R1.n;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.C0468z0;
import com.facebook.react.uimanager.L;
import r2.C0685h;
import r2.r;
import u2.AbstractC0746a;

/* JADX INFO: loaded from: classes.dex */
public final class c extends Drawable {

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    static final /* synthetic */ I2.f[] f1496z = {s.c(new D2.k(c.class, "borderStyle", "getBorderStyle()Lcom/facebook/react/uimanager/style/BorderStyle;", 0))};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f1497a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0468z0 f1498b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private R1.e f1499c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private R1.c f1500d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final G2.b f1501e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Integer[] f1502f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private R1.h f1503g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private R1.j f1504h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f1505i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final float f1506j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private Path f1507k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final Paint f1508l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f1509m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private Path f1510n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private Path f1511o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private Path f1512p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private Path f1513q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private Path f1514r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private PointF f1515s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private PointF f1516t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private PointF f1517u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private PointF f1518v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private RectF f1519w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private RectF f1520x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private RectF f1521y;

    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f1522a;

        static {
            int[] iArr = new int[R1.f.values().length];
            try {
                iArr[R1.f.f2029c.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[R1.f.f2030d.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[R1.f.f2031e.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f1522a = iArr;
        }
    }

    public static final class b extends G2.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ c f1523b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(Object obj, c cVar) {
            super(obj);
            this.f1523b = cVar;
        }

        @Override // G2.a
        protected void c(I2.f fVar, Object obj, Object obj2) {
            D2.h.f(fVar, "property");
            if (D2.h.b(obj, obj2)) {
                return;
            }
            this.f1523b.f1509m = true;
            this.f1523b.invalidateSelf();
        }
    }

    public c(Context context, C0468z0 c0468z0, R1.e eVar, R1.c cVar, R1.f fVar) {
        D2.h.f(context, "context");
        this.f1497a = context;
        this.f1498b = c0468z0;
        this.f1499c = eVar;
        this.f1500d = cVar;
        this.f1501e = m(fVar);
        this.f1503g = new R1.h(0, 0, 0, 0, 15, null);
        this.f1505i = 255;
        this.f1506j = 0.8f;
        this.f1508l = new Paint(1);
        this.f1509m = true;
    }

    private final RectF b() {
        RectF rectFA;
        R1.c cVar = this.f1500d;
        if (cVar == null || (rectFA = cVar.a(getLayoutDirection(), this.f1497a)) == null) {
            return new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        }
        return new RectF(Float.isNaN(rectFA.left) ? 0.0f : C0429f0.f7476a.b(rectFA.left), Float.isNaN(rectFA.top) ? 0.0f : C0429f0.f7476a.b(rectFA.top), Float.isNaN(rectFA.right) ? 0.0f : C0429f0.f7476a.b(rectFA.right), Float.isNaN(rectFA.bottom) ? 0.0f : C0429f0.f7476a.b(rectFA.bottom));
    }

    private final void c(Canvas canvas, int i3, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        if (i3 == 0) {
            return;
        }
        if (this.f1507k == null) {
            this.f1507k = new Path();
        }
        this.f1508l.setColor(n(i3, this.f1505i));
        Path path = this.f1507k;
        if (path != null) {
            path.reset();
        }
        Path path2 = this.f1507k;
        if (path2 != null) {
            path2.moveTo(f3, f4);
        }
        Path path3 = this.f1507k;
        if (path3 != null) {
            path3.lineTo(f5, f6);
        }
        Path path4 = this.f1507k;
        if (path4 != null) {
            path4.lineTo(f7, f8);
        }
        Path path5 = this.f1507k;
        if (path5 != null) {
            path5.lineTo(f9, f10);
        }
        Path path6 = this.f1507k;
        if (path6 != null) {
            path6.lineTo(f3, f4);
        }
        Path path7 = this.f1507k;
        if (path7 != null) {
            canvas.drawPath(path7, this.f1508l);
        }
    }

    private final void d(Canvas canvas) {
        RectF rectFB = b();
        int iC = F2.a.c(rectFB.left);
        int iC2 = F2.a.c(rectFB.top);
        int iC3 = F2.a.c(rectFB.right);
        int iC4 = F2.a.c(rectFB.bottom);
        if (iC > 0 || iC3 > 0 || iC2 > 0 || iC4 > 0) {
            Rect bounds = getBounds();
            D2.h.e(bounds, "getBounds(...)");
            int i3 = bounds.left;
            int i4 = bounds.top;
            int iF = f(iC, iC2, iC3, iC4, this.f1503g.b(), this.f1503g.d(), this.f1503g.c(), this.f1503g.a());
            if (iF == 0) {
                this.f1508l.setAntiAlias(false);
                int iWidth = bounds.width();
                int iHeight = bounds.height();
                if (iC > 0) {
                    float f3 = i3;
                    float f4 = i4;
                    float f5 = i3 + iC;
                    c(canvas, this.f1503g.b(), f3, f4, f5, i4 + iC2, f5, r0 - iC4, f3, i4 + iHeight);
                }
                if (iC2 > 0) {
                    float f6 = i3;
                    float f7 = i4;
                    float f8 = i3 + iC;
                    float f9 = i4 + iC2;
                    c(canvas, this.f1503g.d(), f6, f7, f8, f9, r0 - iC3, f9, i3 + iWidth, f7);
                }
                if (iC3 > 0) {
                    int i5 = i3 + iWidth;
                    float f10 = i5;
                    float f11 = i5 - iC3;
                    c(canvas, this.f1503g.c(), f10, i4, f10, i4 + iHeight, f11, r7 - iC4, f11, i4 + iC2);
                }
                if (iC4 > 0) {
                    int i6 = i4 + iHeight;
                    float f12 = i6;
                    float f13 = i6 - iC4;
                    int iA = this.f1503g.a();
                    c(canvas, iA, i3, f12, i3 + iWidth, f12, r8 - iC3, f13, i3 + iC, f13);
                }
                this.f1508l.setAntiAlias(true);
                return;
            }
            if (Color.alpha(iF) != 0) {
                int i7 = bounds.right;
                int i8 = bounds.bottom;
                this.f1508l.setColor(n(iF, this.f1505i));
                this.f1508l.setStyle(Paint.Style.STROKE);
                Path path = new Path();
                this.f1510n = path;
                if (iC > 0) {
                    path.reset();
                    int iC5 = F2.a.c(rectFB.left);
                    v(iC5);
                    this.f1508l.setStrokeWidth(iC5);
                    Path path2 = this.f1510n;
                    if (path2 != null) {
                        path2.moveTo(i3 + (iC5 / 2), i4);
                    }
                    Path path3 = this.f1510n;
                    if (path3 != null) {
                        path3.lineTo(i3 + (iC5 / 2), i8);
                    }
                    Path path4 = this.f1510n;
                    if (path4 != null) {
                        canvas.drawPath(path4, this.f1508l);
                    }
                }
                if (iC2 > 0) {
                    Path path5 = this.f1510n;
                    if (path5 != null) {
                        path5.reset();
                    }
                    int iC6 = F2.a.c(rectFB.top);
                    v(iC6);
                    this.f1508l.setStrokeWidth(iC6);
                    Path path6 = this.f1510n;
                    if (path6 != null) {
                        path6.moveTo(i3, i4 + (iC6 / 2));
                    }
                    Path path7 = this.f1510n;
                    if (path7 != null) {
                        path7.lineTo(i7, i4 + (iC6 / 2));
                    }
                    Path path8 = this.f1510n;
                    if (path8 != null) {
                        canvas.drawPath(path8, this.f1508l);
                    }
                }
                if (iC3 > 0) {
                    Path path9 = this.f1510n;
                    if (path9 != null) {
                        path9.reset();
                    }
                    int iC7 = F2.a.c(rectFB.right);
                    v(iC7);
                    this.f1508l.setStrokeWidth(iC7);
                    Path path10 = this.f1510n;
                    if (path10 != null) {
                        path10.moveTo(i7 - (iC7 / 2), i4);
                    }
                    Path path11 = this.f1510n;
                    if (path11 != null) {
                        path11.lineTo(i7 - (iC7 / 2), i8);
                    }
                    Path path12 = this.f1510n;
                    if (path12 != null) {
                        canvas.drawPath(path12, this.f1508l);
                    }
                }
                if (iC4 > 0) {
                    Path path13 = this.f1510n;
                    if (path13 != null) {
                        path13.reset();
                    }
                    int iC8 = F2.a.c(rectFB.bottom);
                    v(iC8);
                    this.f1508l.setStrokeWidth(iC8);
                    Path path14 = this.f1510n;
                    if (path14 != null) {
                        path14.moveTo(i3, i8 - (iC8 / 2));
                    }
                    Path path15 = this.f1510n;
                    if (path15 != null) {
                        path15.lineTo(i7, i8 - (iC8 / 2));
                    }
                    Path path16 = this.f1510n;
                    if (path16 != null) {
                        canvas.drawPath(path16, this.f1508l);
                    }
                }
            }
        }
    }

    private final void e(Canvas canvas) {
        PointF pointF;
        PointF pointF2;
        PointF pointF3;
        PointF pointF4;
        float f3;
        float f4;
        float f5;
        PointF pointF5;
        PointF pointF6;
        R1.k kVarC;
        R1.k kVarC2;
        R1.k kVarC3;
        R1.k kVarC4;
        t();
        canvas.save();
        Path path = this.f1513q;
        if (path == null) {
            throw new IllegalStateException("Required value was null.");
        }
        canvas.clipPath(path);
        RectF rectFB = b();
        float fB = 0.0f;
        if (rectFB.top > 0.0f || rectFB.bottom > 0.0f || rectFB.left > 0.0f || rectFB.right > 0.0f) {
            float fJ = j();
            int iG = g(n.f2075c);
            if (rectFB.top != fJ || rectFB.bottom != fJ || rectFB.left != fJ || rectFB.right != fJ || this.f1503g.b() != iG || this.f1503g.d() != iG || this.f1503g.c() != iG || this.f1503g.a() != iG) {
                this.f1508l.setStyle(Paint.Style.FILL);
                if (Build.VERSION.SDK_INT >= 26) {
                    Path path2 = this.f1514r;
                    if (path2 == null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    canvas.clipOutPath(path2);
                } else {
                    Path path3 = this.f1514r;
                    if (path3 == null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    canvas.clipPath(path3, Region.Op.DIFFERENCE);
                }
                RectF rectF = this.f1520x;
                if (rectF == null) {
                    throw new IllegalStateException("Required value was null.");
                }
                float f6 = rectF.left;
                float f7 = rectF.right;
                float f8 = rectF.top;
                float f9 = rectF.bottom;
                PointF pointF7 = this.f1517u;
                if (pointF7 == null) {
                    throw new IllegalStateException("Required value was null.");
                }
                PointF pointF8 = this.f1518v;
                if (pointF8 == null) {
                    throw new IllegalStateException("Required value was null.");
                }
                PointF pointF9 = this.f1515s;
                if (pointF9 == null) {
                    throw new IllegalStateException("Required value was null.");
                }
                PointF pointF10 = this.f1516t;
                if (pointF10 == null) {
                    throw new IllegalStateException("Required value was null.");
                }
                if (rectFB.left > 0.0f) {
                    float f10 = this.f1506j;
                    pointF = pointF10;
                    pointF2 = pointF9;
                    pointF3 = pointF8;
                    pointF4 = pointF7;
                    f3 = f9;
                    f4 = f8;
                    f5 = f7;
                    c(canvas, this.f1503g.b(), f6, f8 - f10, pointF7.x, pointF7.y - f10, pointF9.x, pointF9.y + f10, f6, f9 + f10);
                } else {
                    pointF = pointF10;
                    pointF2 = pointF9;
                    pointF3 = pointF8;
                    pointF4 = pointF7;
                    f3 = f9;
                    f4 = f8;
                    f5 = f7;
                }
                if (rectFB.top > 0.0f) {
                    float f11 = this.f1506j;
                    PointF pointF11 = pointF4;
                    PointF pointF12 = pointF3;
                    pointF5 = pointF12;
                    c(canvas, this.f1503g.d(), f6 - f11, f4, pointF11.x - f11, pointF11.y, pointF12.x + f11, pointF12.y, f5 + f11, f4);
                } else {
                    pointF5 = pointF3;
                }
                if (rectFB.right > 0.0f) {
                    float f12 = this.f1506j;
                    PointF pointF13 = pointF5;
                    PointF pointF14 = pointF;
                    pointF6 = pointF14;
                    c(canvas, this.f1503g.c(), f5, f4 - f12, pointF13.x, pointF13.y - f12, pointF14.x, pointF14.y + f12, f5, f3 + f12);
                } else {
                    pointF6 = pointF;
                }
                if (rectFB.bottom > 0.0f) {
                    float f13 = this.f1506j;
                    PointF pointF15 = pointF2;
                    float f14 = pointF15.x - f13;
                    float f15 = pointF15.y;
                    PointF pointF16 = pointF6;
                    c(canvas, this.f1503g.a(), f6 - f13, f3, f14, f15, pointF16.x + f13, pointF16.y, f5 + f13, f3);
                }
            } else if (fJ > 0.0f) {
                this.f1508l.setColor(n(iG, this.f1505i));
                this.f1508l.setStyle(Paint.Style.STROKE);
                this.f1508l.setStrokeWidth(fJ);
                R1.j jVar = this.f1504h;
                if (jVar == null || !jVar.f()) {
                    Path path4 = this.f1512p;
                    if (path4 == null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    canvas.drawPath(path4, this.f1508l);
                } else {
                    RectF rectF2 = this.f1521y;
                    if (rectF2 != null) {
                        R1.j jVar2 = this.f1504h;
                        float fA = ((jVar2 == null || (kVarC3 = jVar2.c()) == null || (kVarC4 = kVarC3.c()) == null) ? 0.0f : kVarC4.a()) - (rectFB.left * 0.5f);
                        R1.j jVar3 = this.f1504h;
                        if (jVar3 != null && (kVarC = jVar3.c()) != null && (kVarC2 = kVarC.c()) != null) {
                            fB = kVarC2.b();
                        }
                        canvas.drawRoundRect(rectF2, fA, fB - (rectFB.top * 0.5f), this.f1508l);
                    }
                }
            }
        }
        canvas.restore();
    }

    private final int f(int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        int i11 = (i6 > 0 ? i10 : -1) & (i3 > 0 ? i7 : -1) & (i4 > 0 ? i8 : -1) & (i5 > 0 ? i9 : -1);
        if (i3 <= 0) {
            i7 = 0;
        }
        if (i4 <= 0) {
            i8 = 0;
        }
        int i12 = i7 | i8;
        if (i5 <= 0) {
            i9 = 0;
        }
        int i13 = i12 | i9;
        if (i6 <= 0) {
            i10 = 0;
        }
        if (i11 == (i13 | i10)) {
            return i11;
        }
        return 0;
    }

    private final void i(double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, PointF pointF) {
        double d12 = 2;
        double d13 = (d4 + d6) / d12;
        double d14 = (d5 + d7) / d12;
        double d15 = d8 - d13;
        double d16 = d9 - d14;
        double dAbs = Math.abs(d6 - d4) / d12;
        double dAbs2 = Math.abs(d7 - d5) / d12;
        double d17 = ((d11 - d14) - d16) / ((d10 - d13) - d15);
        double d18 = d16 - (d15 * d17);
        double d19 = dAbs2 * dAbs2;
        double d20 = dAbs * dAbs;
        double d21 = d19 + (d20 * d17 * d17);
        double d22 = d12 * dAbs * dAbs * d18 * d17;
        double d23 = d12 * d21;
        double dSqrt = ((-d22) / d23) - Math.sqrt(((-(d20 * ((d18 * d18) - d19))) / d21) + Math.pow(d22 / d23, 2.0d));
        double d24 = (d17 * dSqrt) + d18;
        double d25 = dSqrt + d13;
        double d26 = d24 + d14;
        if (Double.isNaN(d25) || Double.isNaN(d26)) {
            return;
        }
        pointF.x = (float) d25;
        pointF.y = (float) d26;
    }

    private final float j() {
        C0468z0 c0468z0 = this.f1498b;
        float fB = c0468z0 != null ? c0468z0.b(8) : Float.NaN;
        if (Float.isNaN(fB)) {
            return 0.0f;
        }
        return fB;
    }

    private final float k(float f3, float f4) {
        return H2.d.b(f3 - f4, 0.0f);
    }

    private final PathEffect l(R1.f fVar, float f3) {
        int i3 = a.f1522a[fVar.ordinal()];
        if (i3 == 1) {
            return null;
        }
        if (i3 == 2) {
            float f4 = f3 * 3;
            return new DashPathEffect(new float[]{f4, f4, f4, f4}, 0.0f);
        }
        if (i3 == 3) {
            return new DashPathEffect(new float[]{f3, f3, f3, f3}, 0.0f);
        }
        throw new C0685h();
    }

    private final G2.b m(Object obj) {
        return new b(obj, this);
    }

    private final int n(int i3, int i4) {
        if (i4 == 255) {
            return i3;
        }
        if (i4 == 0) {
            return i3 & 16777215;
        }
        return (i3 & 16777215) | ((((i3 >>> 24) * ((i4 + (i4 >> 7)) >> 7)) >> 8) << 24);
    }

    private final void t() {
        R1.j jVarD;
        R1.k kVar;
        R1.k kVar2;
        R1.k kVar3;
        R1.k kVar4;
        Path path;
        Path path2;
        Path path3;
        R1.k kVarB;
        R1.k kVarA;
        R1.k kVarD;
        R1.k kVarC;
        if (this.f1509m) {
            this.f1509m = false;
            Path path4 = this.f1514r;
            if (path4 == null) {
                path4 = new Path();
            }
            this.f1514r = path4;
            Path path5 = this.f1513q;
            if (path5 == null) {
                path5 = new Path();
            }
            this.f1513q = path5;
            this.f1511o = new Path();
            RectF rectF = this.f1519w;
            if (rectF == null) {
                rectF = new RectF();
            }
            this.f1519w = rectF;
            RectF rectF2 = this.f1520x;
            if (rectF2 == null) {
                rectF2 = new RectF();
            }
            this.f1520x = rectF2;
            RectF rectF3 = this.f1521y;
            if (rectF3 == null) {
                rectF3 = new RectF();
            }
            this.f1521y = rectF3;
            Path path6 = this.f1514r;
            if (path6 != null) {
                path6.reset();
                r rVar = r.f10584a;
            }
            Path path7 = this.f1513q;
            if (path7 != null) {
                path7.reset();
                r rVar2 = r.f10584a;
            }
            RectF rectF4 = this.f1519w;
            if (rectF4 != null) {
                rectF4.set(getBounds());
                r rVar3 = r.f10584a;
            }
            RectF rectF5 = this.f1520x;
            if (rectF5 != null) {
                rectF5.set(getBounds());
                r rVar4 = r.f10584a;
            }
            RectF rectF6 = this.f1521y;
            if (rectF6 != null) {
                rectF6.set(getBounds());
                r rVar5 = r.f10584a;
            }
            RectF rectFB = b();
            if (Color.alpha(this.f1503g.b()) != 0 || Color.alpha(this.f1503g.d()) != 0 || Color.alpha(this.f1503g.c()) != 0 || Color.alpha(this.f1503g.a()) != 0) {
                RectF rectF7 = this.f1519w;
                if (rectF7 != null) {
                    rectF7.top = rectF7 != null ? rectF7.top + rectFB.top : 0.0f;
                    r rVar6 = r.f10584a;
                }
                if (rectF7 != null) {
                    rectF7.bottom = rectF7 != null ? rectF7.bottom - rectFB.bottom : 0.0f;
                    r rVar7 = r.f10584a;
                }
                if (rectF7 != null) {
                    rectF7.left = rectF7 != null ? rectF7.left + rectFB.left : 0.0f;
                    r rVar8 = r.f10584a;
                }
                if (rectF7 != null) {
                    rectF7.right = rectF7 != null ? rectF7.right - rectFB.right : 0.0f;
                    r rVar9 = r.f10584a;
                }
            }
            RectF rectF8 = this.f1521y;
            if (rectF8 != null) {
                rectF8.top = rectF8 != null ? rectF8.top + (rectFB.top * 0.5f) : 0.0f;
                r rVar10 = r.f10584a;
            }
            if (rectF8 != null) {
                rectF8.bottom = rectF8 != null ? rectF8.bottom - (rectFB.bottom * 0.5f) : 0.0f;
                r rVar11 = r.f10584a;
            }
            if (rectF8 != null) {
                rectF8.left = rectF8 != null ? rectF8.left + (rectFB.left * 0.5f) : 0.0f;
                r rVar12 = r.f10584a;
            }
            if (rectF8 != null) {
                rectF8.right = rectF8 != null ? rectF8.right - (rectFB.right * 0.5f) : 0.0f;
                r rVar13 = r.f10584a;
            }
            R1.e eVar = this.f1499c;
            if (eVar != null) {
                int layoutDirection = getLayoutDirection();
                Context context = this.f1497a;
                RectF rectF9 = this.f1520x;
                float fD = rectF9 != null ? C0429f0.f7476a.d(rectF9.width()) : 0.0f;
                RectF rectF10 = this.f1520x;
                jVarD = eVar.d(layoutDirection, context, fD, rectF10 != null ? C0429f0.f7476a.d(rectF10.height()) : 0.0f);
            } else {
                jVarD = null;
            }
            this.f1504h = jVarD;
            if (jVarD == null || (kVarC = jVarD.c()) == null || (kVar = kVarC.c()) == null) {
                kVar = new R1.k(0.0f, 0.0f);
            }
            R1.j jVar = this.f1504h;
            if (jVar == null || (kVarD = jVar.d()) == null || (kVar2 = kVarD.c()) == null) {
                kVar2 = new R1.k(0.0f, 0.0f);
            }
            R1.j jVar2 = this.f1504h;
            if (jVar2 == null || (kVarA = jVar2.a()) == null || (kVar3 = kVarA.c()) == null) {
                kVar3 = new R1.k(0.0f, 0.0f);
            }
            R1.j jVar3 = this.f1504h;
            if (jVar3 == null || (kVarB = jVar3.b()) == null || (kVar4 = kVarB.c()) == null) {
                kVar4 = new R1.k(0.0f, 0.0f);
            }
            float fK = k(kVar.a(), rectFB.left);
            float fK2 = k(kVar.b(), rectFB.top);
            float fK3 = k(kVar2.a(), rectFB.right);
            float fK4 = k(kVar2.b(), rectFB.top);
            float fK5 = k(kVar4.a(), rectFB.right);
            float fK6 = k(kVar4.b(), rectFB.bottom);
            float fK7 = k(kVar3.a(), rectFB.left);
            float fK8 = k(kVar3.b(), rectFB.bottom);
            RectF rectF11 = this.f1519w;
            if (rectF11 != null && (path3 = this.f1514r) != null) {
                path3.addRoundRect(rectF11, new float[]{fK, fK2, fK3, fK4, fK5, fK6, fK7, fK8}, Path.Direction.CW);
                r rVar14 = r.f10584a;
            }
            RectF rectF12 = this.f1520x;
            if (rectF12 != null && (path2 = this.f1513q) != null) {
                path2.addRoundRect(rectF12, new float[]{kVar.a(), kVar.b(), kVar2.a(), kVar2.b(), kVar4.a(), kVar4.b(), kVar3.a(), kVar3.b()}, Path.Direction.CW);
                r rVar15 = r.f10584a;
            }
            C0468z0 c0468z0 = this.f1498b;
            float fA = c0468z0 != null ? c0468z0.a(8) / 2.0f : 0.0f;
            Path path8 = this.f1511o;
            if (path8 != null) {
                path8.addRoundRect(new RectF(getBounds()), new float[]{kVar.a() + fA, kVar.b() + fA, kVar2.a() + fA, kVar2.b() + fA, kVar4.a() + fA, kVar4.b() + fA, kVar3.a() + fA, kVar3.b() + fA}, Path.Direction.CW);
                r rVar16 = r.f10584a;
            }
            R1.j jVar4 = this.f1504h;
            if (jVar4 == null || !jVar4.f()) {
                Path path9 = this.f1512p;
                if (path9 == null) {
                    path9 = new Path();
                }
                this.f1512p = path9;
                path9.reset();
                r rVar17 = r.f10584a;
                RectF rectF13 = this.f1521y;
                if (rectF13 != null && (path = this.f1512p) != null) {
                    path.addRoundRect(rectF13, new float[]{kVar.a() - (rectFB.left * 0.5f), kVar.b() - (rectFB.top * 0.5f), kVar2.a() - (rectFB.right * 0.5f), kVar2.b() - (rectFB.top * 0.5f), kVar4.a() - (rectFB.right * 0.5f), kVar4.b() - (rectFB.bottom * 0.5f), kVar3.a() - (rectFB.left * 0.5f), kVar3.b() - (rectFB.bottom * 0.5f)}, Path.Direction.CW);
                    r rVar18 = r.f10584a;
                }
            }
            RectF rectF14 = this.f1519w;
            RectF rectF15 = this.f1520x;
            if (rectF14 == null || rectF15 == null) {
                return;
            }
            PointF pointF = this.f1517u;
            if (pointF == null) {
                pointF = new PointF();
            }
            PointF pointF2 = pointF;
            this.f1517u = pointF2;
            pointF2.x = rectF14.left;
            r rVar19 = r.f10584a;
            pointF2.y = rectF14.top;
            r rVar20 = r.f10584a;
            float f3 = rectF14.left;
            float f4 = rectF14.top;
            float f5 = 2;
            i(f3, f4, (fK * f5) + f3, (f5 * fK2) + f4, rectF15.left, rectF15.top, f3, f4, pointF2);
            r rVar21 = r.f10584a;
            PointF pointF3 = this.f1515s;
            if (pointF3 == null) {
                pointF3 = new PointF();
            }
            PointF pointF4 = pointF3;
            this.f1515s = pointF4;
            pointF4.x = rectF14.left;
            r rVar22 = r.f10584a;
            pointF4.y = rectF14.bottom;
            r rVar23 = r.f10584a;
            float f6 = rectF14.left;
            float f7 = rectF14.bottom;
            float f8 = 2;
            i(f6, f7 - (fK8 * f8), (f8 * fK7) + f6, f7, rectF15.left, rectF15.bottom, f6, f7, pointF4);
            r rVar24 = r.f10584a;
            PointF pointF5 = this.f1518v;
            if (pointF5 == null) {
                pointF5 = new PointF();
            }
            PointF pointF6 = pointF5;
            this.f1518v = pointF6;
            pointF6.x = rectF14.right;
            r rVar25 = r.f10584a;
            pointF6.y = rectF14.top;
            r rVar26 = r.f10584a;
            float f9 = rectF14.right;
            float f10 = 2;
            float f11 = rectF14.top;
            i(f9 - (fK3 * f10), f11, f9, (f10 * fK4) + f11, rectF15.right, rectF15.top, f9, f11, pointF6);
            r rVar27 = r.f10584a;
            PointF pointF7 = this.f1516t;
            if (pointF7 == null) {
                pointF7 = new PointF();
            }
            PointF pointF8 = pointF7;
            this.f1516t = pointF8;
            pointF8.x = rectF14.right;
            r rVar28 = r.f10584a;
            pointF8.y = rectF14.bottom;
            r rVar29 = r.f10584a;
            float f12 = rectF14.right;
            float f13 = 2;
            float f14 = rectF14.bottom;
            i(f12 - (fK5 * f13), f14 - (f13 * fK6), f12, f14, rectF15.right, rectF15.bottom, f12, f14, pointF8);
            r rVar30 = r.f10584a;
        }
    }

    private final void u() {
        R1.f fVarH = h();
        if (fVarH != null) {
            this.f1508l.setPathEffect(h() != null ? l(fVarH, j()) : null);
        }
    }

    private final void v(int i3) {
        R1.f fVarH = h();
        if (fVarH != null) {
            this.f1508l.setPathEffect(h() != null ? l(fVarH, i3) : null);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        R1.h hVarC;
        D2.h.f(canvas, "canvas");
        u();
        Integer[] numArr = this.f1502f;
        if (numArr == null || (hVarC = R1.b.c(numArr, getLayoutDirection(), this.f1497a)) == null) {
            hVarC = this.f1503g;
        }
        this.f1503g = hVarC;
        R1.e eVar = this.f1499c;
        if (eVar == null || !eVar.c()) {
            d(canvas);
        } else {
            e(canvas);
        }
    }

    public final int g(n nVar) {
        Integer num;
        D2.h.f(nVar, "position");
        Integer[] numArr = this.f1502f;
        if (numArr == null || (num = numArr[nVar.ordinal()]) == null) {
            return -16777216;
        }
        return num.intValue();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        if (AbstractC0746a.d(Color.alpha(n(this.f1503g.b(), this.f1505i)), Color.alpha(n(this.f1503g.d(), this.f1505i)), Color.alpha(n(this.f1503g.c(), this.f1505i)), Color.alpha(n(this.f1503g.a(), this.f1505i))) == 0) {
            return -2;
        }
        return AbstractC0746a.e(Color.alpha(n(this.f1503g.b(), this.f1505i)), Color.alpha(n(this.f1503g.d(), this.f1505i)), Color.alpha(n(this.f1503g.c(), this.f1505i)), Color.alpha(n(this.f1503g.a(), this.f1505i))) == 255 ? -1 : -3;
    }

    public final R1.f h() {
        return (R1.f) this.f1501e.a(this, f1496z[0]);
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        this.f1509m = true;
        super.invalidateSelf();
    }

    public final void o(n nVar, Integer num) {
        D2.h.f(nVar, "position");
        Integer[] numArrB = this.f1502f;
        if (numArrB == null) {
            numArrB = R1.b.b(null, 1, null);
        }
        this.f1502f = numArrB;
        if (numArrB != null) {
            numArrB[nVar.ordinal()] = num;
        }
        this.f1509m = true;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        D2.h.f(rect, "bounds");
        super.onBoundsChange(rect);
        this.f1509m = true;
    }

    public final void p(R1.c cVar) {
        this.f1500d = cVar;
    }

    public final void q(R1.e eVar) {
        this.f1499c = eVar;
    }

    public final void r(R1.f fVar) {
        this.f1501e.b(this, f1496z[0], fVar);
    }

    public final void s(int i3, float f3) {
        C0468z0 c0468z0 = this.f1498b;
        if (L.b(c0468z0 != null ? Float.valueOf(c0468z0.b(i3)) : null, Float.valueOf(f3))) {
            return;
        }
        C0468z0 c0468z02 = this.f1498b;
        if (c0468z02 != null) {
            c0468z02.c(i3, f3);
        }
        if (i3 == 0 || i3 == 1 || i3 == 2 || i3 == 3 || i3 == 4 || i3 == 5 || i3 == 8) {
            this.f1509m = true;
        }
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.f1505i = i3;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }
}
