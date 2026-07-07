package O1;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.K;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class m extends Drawable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f1588a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f1589b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final float f1590c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final float f1591d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final float f1592e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float f1593f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private R1.e f1594g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Paint f1595h;

    public /* synthetic */ m(Context context, int i3, float f3, float f4, float f5, float f6, R1.e eVar, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, i3, f3, f4, f5, f6, (i4 & 64) != 0 ? null : eVar);
    }

    private final void a(Canvas canvas, RectF rectF) {
        canvas.clipOutRect(getBounds());
        canvas.drawRect(rectF, this.f1595h);
    }

    private final void b(Canvas canvas, RectF rectF, float f3, R1.j jVar) {
        RectF rectF2 = new RectF(getBounds());
        rectF2.inset(0.4f, 0.4f);
        Path path = new Path();
        float[] fArr = {jVar.c().a(), jVar.c().b(), jVar.d().a(), jVar.d().b(), jVar.b().a(), jVar.b().b(), jVar.a().a(), jVar.a().b()};
        Path.Direction direction = Path.Direction.CW;
        path.addRoundRect(rectF2, fArr, direction);
        canvas.clipOutPath(path);
        Path path2 = new Path();
        path2.addRoundRect(rectF, new float[]{d.a(jVar.c().a(), f3), d.a(jVar.c().b(), f3), d.a(jVar.d().a(), f3), d.a(jVar.d().b(), f3), d.a(jVar.b().a(), f3), d.a(jVar.b().b(), f3), d.a(jVar.a().a(), f3), d.a(jVar.a().b(), f3)}, direction);
        canvas.drawPath(path2, this.f1595h);
    }

    public final void c(R1.e eVar) {
        this.f1594g = eVar;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        R1.j jVarD;
        D2.h.f(canvas, "canvas");
        C0429f0 c0429f0 = C0429f0.f7476a;
        float fD = c0429f0.d(getBounds().width());
        float fD2 = c0429f0.d(getBounds().height());
        R1.e eVar = this.f1594g;
        R1.j jVar = (eVar == null || (jVarD = eVar.d(getLayoutDirection(), this.f1588a, fD, fD2)) == null) ? null : new R1.j(new R1.k(c0429f0.b(jVarD.c().a()), c0429f0.b(jVarD.c().b())), new R1.k(c0429f0.b(jVarD.d().a()), c0429f0.b(jVarD.d().b())), new R1.k(c0429f0.b(jVarD.a().a()), c0429f0.b(jVarD.a().b())), new R1.k(c0429f0.b(jVarD.b().a()), c0429f0.b(jVarD.b().b())));
        float fB = c0429f0.b(this.f1593f);
        RectF rectF = new RectF(getBounds());
        float f3 = -fB;
        rectF.inset(f3, f3);
        rectF.offset(c0429f0.b(this.f1590c), c0429f0.b(this.f1591d));
        int iSave = canvas.save();
        if (jVar == null || !jVar.e()) {
            a(canvas, rectF);
        } else {
            b(canvas, rectF, fB, jVar);
        }
        canvas.restoreToCount(iSave);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        int alpha = this.f1595h.getAlpha();
        if (alpha == 255) {
            return -1;
        }
        return (1 > alpha || alpha >= 255) ? -2 : -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.f1595h.setAlpha(F2.a.c((i3 / 255.0f) * (Color.alpha(this.f1589b) / 255.0f) * 255.0f));
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f1595h.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public m(Context context, int i3, float f3, float f4, float f5, float f6, R1.e eVar) {
        D2.h.f(context, "context");
        this.f1588a = context;
        this.f1589b = i3;
        this.f1590c = f3;
        this.f1591d = f4;
        this.f1592e = f5;
        this.f1593f = f6;
        this.f1594g = eVar;
        Paint paint = new Paint();
        paint.setColor(i3);
        float fX = K.f7255a.x(f5 * 0.5f);
        if (fX > 0.0f) {
            paint.setMaskFilter(new BlurMaskFilter(fX, BlurMaskFilter.Blur.NORMAL));
        }
        this.f1595h = paint;
    }
}
