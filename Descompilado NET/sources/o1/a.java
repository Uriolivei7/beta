package O1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import com.facebook.react.uimanager.C0429f0;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class a extends Drawable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f1484a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private R1.e f1485b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private R1.c f1486c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final float f1487d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private RectF f1488e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private R1.j f1489f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f1490g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f1491h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private RectF f1492i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private Path f1493j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private List f1494k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final Paint f1495l;

    public /* synthetic */ a(Context context, R1.e eVar, R1.c cVar, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : eVar, (i3 & 4) != 0 ? null : cVar);
    }

    private final RectF a() {
        R1.c cVar = this.f1486c;
        RectF rectFA = cVar != null ? cVar.a(getLayoutDirection(), this.f1484a) : null;
        return new RectF(rectFA != null ? C0429f0.f7476a.b(rectFA.left) : 0.0f, rectFA != null ? C0429f0.f7476a.b(rectFA.top) : 0.0f, rectFA != null ? C0429f0.f7476a.b(rectFA.right) : 0.0f, rectFA != null ? C0429f0.f7476a.b(rectFA.bottom) : 0.0f);
    }

    private final Shader c() {
        List<R1.a> list = this.f1494k;
        Shader composeShader = null;
        if (list != null) {
            for (R1.a aVar : list) {
                Rect bounds = getBounds();
                D2.h.e(bounds, "getBounds(...)");
                Shader shaderA = aVar.a(bounds);
                if (shaderA != null) {
                    composeShader = composeShader == null ? shaderA : new ComposeShader(shaderA, composeShader, PorterDuff.Mode.SRC_OVER);
                }
            }
        }
        return composeShader;
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x008f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final void h() {
        /*
            Method dump skipped, instruction units count: 448
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: O1.a.h():void");
    }

    public final int b() {
        return this.f1491h;
    }

    public final void d(int i3) {
        if (this.f1491h != i3) {
            this.f1491h = i3;
            this.f1495l.setColor(i3);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        R1.e eVar;
        R1.k kVarC;
        R1.k kVarC2;
        R1.e eVar2;
        R1.k kVarC3;
        R1.k kVarC4;
        D2.h.f(canvas, "canvas");
        h();
        canvas.save();
        float fB = 0.0f;
        if (this.f1495l.getAlpha() != 0) {
            R1.j jVar = this.f1489f;
            if (jVar == null || !jVar.f() || (eVar2 = this.f1485b) == null || !eVar2.c()) {
                R1.e eVar3 = this.f1485b;
                if (eVar3 == null || !eVar3.c()) {
                    canvas.drawRect(this.f1492i, this.f1495l);
                } else {
                    Path path = this.f1493j;
                    if (path == null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    canvas.drawPath(path, this.f1495l);
                }
            } else {
                RectF rectF = this.f1492i;
                R1.j jVar2 = this.f1489f;
                float fB2 = (jVar2 == null || (kVarC4 = jVar2.c()) == null) ? 0.0f : C0429f0.f7476a.b(kVarC4.a());
                R1.j jVar3 = this.f1489f;
                canvas.drawRoundRect(rectF, fB2, (jVar3 == null || (kVarC3 = jVar3.c()) == null) ? 0.0f : C0429f0.f7476a.b(kVarC3.b()), this.f1495l);
            }
        }
        List list = this.f1494k;
        if (list != null && list != null && (!list.isEmpty())) {
            this.f1495l.setShader(c());
            R1.j jVar4 = this.f1489f;
            if (jVar4 == null || !jVar4.f() || (eVar = this.f1485b) == null || !eVar.c()) {
                R1.e eVar4 = this.f1485b;
                if (eVar4 == null || !eVar4.c()) {
                    canvas.drawRect(this.f1492i, this.f1495l);
                } else {
                    Path path2 = this.f1493j;
                    if (path2 == null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    canvas.drawPath(path2, this.f1495l);
                }
            } else {
                RectF rectF2 = this.f1492i;
                R1.j jVar5 = this.f1489f;
                float fB3 = (jVar5 == null || (kVarC2 = jVar5.c()) == null) ? 0.0f : C0429f0.f7476a.b(kVarC2.a());
                R1.j jVar6 = this.f1489f;
                if (jVar6 != null && (kVarC = jVar6.c()) != null) {
                    fB = C0429f0.f7476a.b(kVarC.b());
                }
                canvas.drawRoundRect(rectF2, fB3, fB, this.f1495l);
            }
            this.f1495l.setShader(null);
        }
        canvas.restore();
    }

    public final void e(List list) {
        if (D2.h.b(this.f1494k, list)) {
            return;
        }
        this.f1494k = list;
        invalidateSelf();
    }

    public final void f(R1.c cVar) {
        this.f1486c = cVar;
    }

    public final void g(R1.e eVar) {
        this.f1485b = eVar;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        int alpha = this.f1495l.getAlpha();
        if (alpha == 255) {
            return -1;
        }
        return (1 > alpha || alpha >= 255) ? -2 : -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        this.f1490g = true;
        super.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        D2.h.f(rect, "bounds");
        super.onBoundsChange(rect);
        this.f1490g = true;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.f1495l.setAlpha(F2.a.c((i3 / 255.0f) * (Color.alpha(this.f1491h) / 255.0f) * 255.0f));
        invalidateSelf();
    }

    public a(Context context, R1.e eVar, R1.c cVar) {
        D2.h.f(context, "context");
        this.f1484a = context;
        this.f1485b = eVar;
        this.f1486c = cVar;
        this.f1487d = 0.8f;
        this.f1490g = true;
        this.f1492i = new RectF();
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(this.f1491h);
        this.f1495l = paint;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }
}
