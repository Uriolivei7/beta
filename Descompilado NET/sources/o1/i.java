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
import java.util.ArrayList;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class i extends Drawable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f1566a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f1567b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final float f1568c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final float f1569d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final float f1570e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float f1571f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private R1.c f1572g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private R1.e f1573h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final Paint f1574i;

    public /* synthetic */ i(Context context, int i3, float f3, float f4, float f5, float f6, R1.c cVar, R1.e eVar, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, i3, f3, f4, f5, f6, (i4 & 64) != 0 ? null : cVar, (i4 & 128) != 0 ? null : eVar);
    }

    private final RectF a() {
        RectF rectFA;
        R1.c cVar = this.f1572g;
        if (cVar == null || (rectFA = cVar.a(getLayoutDirection(), this.f1566a)) == null) {
            return null;
        }
        C0429f0 c0429f0 = C0429f0.f7476a;
        return new RectF(c0429f0.b(rectFA.left), c0429f0.b(rectFA.top), c0429f0.b(rectFA.right), c0429f0.b(rectFA.bottom));
    }

    private final R1.j b() {
        R1.j jVarD;
        R1.e eVar = this.f1573h;
        if (eVar != null) {
            int layoutDirection = getLayoutDirection();
            Context context = this.f1566a;
            C0429f0 c0429f0 = C0429f0.f7476a;
            jVarD = eVar.d(layoutDirection, context, c0429f0.d(getBounds().width()), c0429f0.d(getBounds().height()));
        } else {
            jVarD = null;
        }
        if (jVarD == null || !jVarD.e()) {
            return null;
        }
        C0429f0 c0429f02 = C0429f0.f7476a;
        return new R1.j(new R1.k(c0429f02.b(jVarD.c().a()), c0429f02.b(jVarD.c().b())), new R1.k(c0429f02.b(jVarD.d().a()), c0429f02.b(jVarD.d().b())), new R1.k(c0429f02.b(jVarD.a().a()), c0429f02.b(jVarD.a().b())), new R1.k(c0429f02.b(jVarD.b().a()), c0429f02.b(jVarD.b().b())));
    }

    private final float c(float f3, Float f4) {
        return H2.d.b(f3 - (f4 != null ? f4.floatValue() : 0.0f), 0.0f);
    }

    public final void d(R1.c cVar) {
        this.f1572g = cVar;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        D2.h.f(canvas, "canvas");
        R1.j jVarB = b();
        RectF rectFA = a();
        RectF rectF = new RectF(getBounds().left + (rectFA != null ? rectFA.left : 0.0f), getBounds().top + (rectFA != null ? rectFA.top : 0.0f), getBounds().right - (rectFA != null ? rectFA.right : 0.0f), getBounds().bottom - (rectFA != null ? rectFA.bottom : 0.0f));
        float[] fArr = jVarB != null ? new float[]{c(jVarB.c().a(), rectFA != null ? Float.valueOf(rectFA.left) : null), c(jVarB.c().b(), rectFA != null ? Float.valueOf(rectFA.top) : null), c(jVarB.d().a(), rectFA != null ? Float.valueOf(rectFA.right) : null), c(jVarB.d().b(), rectFA != null ? Float.valueOf(rectFA.top) : null), c(jVarB.b().a(), rectFA != null ? Float.valueOf(rectFA.right) : null), c(jVarB.b().b(), rectFA != null ? Float.valueOf(rectFA.bottom) : null), c(jVarB.a().a(), rectFA != null ? Float.valueOf(rectFA.left) : null), c(jVarB.a().b(), rectFA != null ? Float.valueOf(rectFA.bottom) : null)} : null;
        C0429f0 c0429f0 = C0429f0.f7476a;
        float fB = c0429f0.b(this.f1568c);
        float fB2 = c0429f0.b(this.f1569d);
        float fB3 = c0429f0.b(this.f1571f);
        RectF rectF2 = new RectF(rectF);
        rectF2.inset(fB3, fB3);
        rectF2.offset(fB, fB2);
        float fX = K.f7255a.x(this.f1570e);
        RectF rectF3 = new RectF(rectF);
        float f3 = -fX;
        rectF3.inset(f3, f3);
        if (fB3 < 0.0f) {
            rectF3.inset(fB3, fB3);
        }
        RectF rectF4 = new RectF(rectF3);
        rectF4.offset(-fB, -fB2);
        rectF3.union(rectF4);
        int iSave = canvas.save();
        if (fArr != null) {
            Path path = new Path();
            path.addRoundRect(rectF, fArr, Path.Direction.CW);
            canvas.clipPath(path);
            ArrayList arrayList = new ArrayList(fArr.length);
            for (float f4 : fArr) {
                arrayList.add(Float.valueOf(d.a(f4, -fB3)));
            }
            canvas.drawDoubleRoundRect(rectF3, j.f1575a, rectF2, AbstractC0717n.d0(arrayList), this.f1574i);
        } else {
            canvas.clipRect(rectF);
            canvas.drawDoubleRoundRect(rectF3, j.f1575a, rectF2, j.f1575a, this.f1574i);
        }
        canvas.restoreToCount(iSave);
    }

    public final void e(R1.e eVar) {
        this.f1573h = eVar;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        int alpha = this.f1574i.getAlpha();
        if (alpha == 255) {
            return -1;
        }
        return (1 > alpha || alpha >= 255) ? -2 : -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.f1574i.setAlpha(F2.a.c((i3 / 255.0f) * (Color.alpha(this.f1567b) / 255.0f) * 255.0f));
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f1574i.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public i(Context context, int i3, float f3, float f4, float f5, float f6, R1.c cVar, R1.e eVar) {
        D2.h.f(context, "context");
        this.f1566a = context;
        this.f1567b = i3;
        this.f1568c = f3;
        this.f1569d = f4;
        this.f1570e = f5;
        this.f1571f = f6;
        this.f1572g = cVar;
        this.f1573h = eVar;
        Paint paint = new Paint();
        paint.setColor(i3);
        float fX = K.f7255a.x(f5 * 0.5f);
        if (fX > 0.0f) {
            paint.setMaskFilter(new BlurMaskFilter(fX, BlurMaskFilter.Blur.NORMAL));
        }
        this.f1574i = paint;
    }
}
