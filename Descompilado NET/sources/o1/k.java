package O1;

import R1.o;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.facebook.react.uimanager.C0429f0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;

/* JADX INFO: loaded from: classes.dex */
public final class k extends Drawable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f1576a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private R1.e f1577b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final float f1578c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private float f1579d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private o f1580e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f1581f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private float f1582g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Paint f1583h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private R1.j f1584i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private RectF f1585j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final Path f1586k;

    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f1587a;

        static {
            int[] iArr = new int[o.values().length];
            try {
                iArr[o.f2090c.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[o.f2091d.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[o.f2092e.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f1587a = iArr;
        }
    }

    public /* synthetic */ k(Context context, R1.e eVar, int i3, float f3, o oVar, float f4, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i4 & 2) != 0 ? null : eVar, i3, f3, oVar, f4);
    }

    private final float a(float f3, float f4) {
        if (f3 == 0.0f) {
            return 0.0f;
        }
        return f3 + (f4 * 0.5f);
    }

    private final void b(Canvas canvas) {
        this.f1586k.addRect(this.f1585j, Path.Direction.CW);
        canvas.drawPath(this.f1586k, this.f1583h);
    }

    private final void c(Canvas canvas) {
        R1.k kVar;
        R1.k kVar2;
        R1.k kVar3;
        R1.k kVar4;
        R1.k kVarB;
        R1.k kVarA;
        R1.k kVarD;
        R1.k kVarC;
        R1.j jVar = this.f1584i;
        if (jVar == null || (kVarC = jVar.c()) == null || (kVar = kVarC.c()) == null) {
            kVar = new R1.k(0.0f, 0.0f);
        }
        R1.j jVar2 = this.f1584i;
        if (jVar2 == null || (kVarD = jVar2.d()) == null || (kVar2 = kVarD.c()) == null) {
            kVar2 = new R1.k(0.0f, 0.0f);
        }
        R1.j jVar3 = this.f1584i;
        if (jVar3 == null || (kVarA = jVar3.a()) == null || (kVar3 = kVarA.c()) == null) {
            kVar3 = new R1.k(0.0f, 0.0f);
        }
        R1.j jVar4 = this.f1584i;
        if (jVar4 == null || (kVarB = jVar4.b()) == null || (kVar4 = kVarB.c()) == null) {
            kVar4 = new R1.k(0.0f, 0.0f);
        }
        this.f1586k.addRoundRect(this.f1585j, new float[]{a(kVar.a(), this.f1582g), a(kVar.b(), this.f1582g), a(kVar2.a(), this.f1582g), a(kVar2.b(), this.f1582g), a(kVar4.a(), this.f1582g), a(kVar4.b(), this.f1582g), a(kVar3.a(), this.f1582g), a(kVar3.b(), this.f1582g)}, Path.Direction.CW);
        canvas.drawPath(this.f1586k, this.f1583h);
    }

    private final PathEffect d(o oVar, float f3) {
        int i3 = a.f1587a[oVar.ordinal()];
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

    private final void j() {
        this.f1585j.set(getBounds());
        RectF rectF = this.f1585j;
        float f3 = rectF.top;
        float f4 = this.f1582g;
        float f5 = this.f1579d;
        float f6 = this.f1578c;
        rectF.top = f3 - (((f4 * 0.5f) + f5) - f6);
        rectF.bottom += ((f4 * 0.5f) + f5) - f6;
        rectF.left -= ((f4 * 0.5f) + f5) - f6;
        rectF.right += ((f4 * 0.5f) + f5) - f6;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        R1.j jVarD;
        D2.h.f(canvas, "canvas");
        if (this.f1582g == 0.0f) {
            return;
        }
        this.f1586k.reset();
        R1.e eVar = this.f1577b;
        if (eVar != null) {
            int layoutDirection = getLayoutDirection();
            Context context = this.f1576a;
            C0429f0 c0429f0 = C0429f0.f7476a;
            jVarD = eVar.d(layoutDirection, context, c0429f0.e(getBounds().width()), c0429f0.e(getBounds().height()));
        } else {
            jVarD = null;
        }
        this.f1584i = jVarD;
        j();
        R1.j jVar = this.f1584i;
        if (jVar == null || jVar == null || !jVar.e()) {
            b(canvas);
        } else {
            c(canvas);
        }
    }

    public final void e(R1.e eVar) {
        this.f1577b = eVar;
    }

    public final void f(int i3) {
        if (i3 != this.f1581f) {
            this.f1581f = i3;
            this.f1583h.setColor(i3);
            invalidateSelf();
        }
    }

    public final void g(float f3) {
        if (f3 == this.f1579d) {
            return;
        }
        this.f1579d = f3;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        int alpha = this.f1583h.getAlpha();
        if (alpha == 255) {
            return -1;
        }
        return (1 > alpha || alpha >= 255) ? -2 : -3;
    }

    public final void h(o oVar) {
        D2.h.f(oVar, "value");
        if (oVar != this.f1580e) {
            this.f1580e = oVar;
            this.f1583h.setPathEffect(d(oVar, this.f1582g));
            invalidateSelf();
        }
    }

    public final void i(float f3) {
        if (f3 == this.f1582g) {
            return;
        }
        this.f1582g = f3;
        this.f1583h.setStrokeWidth(f3);
        this.f1583h.setPathEffect(d(this.f1580e, f3));
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.f1583h.setAlpha(F2.a.c((i3 / 255.0f) * (Color.alpha(this.f1581f) / 255.0f) * 255.0f));
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f1583h.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public k(Context context, R1.e eVar, int i3, float f3, o oVar, float f4) {
        D2.h.f(context, "context");
        D2.h.f(oVar, "outlineStyle");
        this.f1576a = context;
        this.f1577b = eVar;
        this.f1578c = 0.8f;
        this.f1579d = f3;
        this.f1580e = oVar;
        this.f1581f = i3;
        this.f1582g = f4;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(i3);
        paint.setStrokeWidth(f4);
        paint.setPathEffect(d(oVar, f4));
        this.f1583h = paint;
        this.f1585j = new RectF();
        this.f1586k = new Path();
    }
}
