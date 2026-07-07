package O1;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import com.facebook.react.uimanager.C0429f0;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class g extends LayerDrawable {

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public static final a f1554m = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Context f1555b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Drawable f1556c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final List f1557d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final e f1558e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final O1.a f1559f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final c f1560g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Drawable f1561h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final List f1562i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final k f1563j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private R1.c f1564k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private R1.e f1565l;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Drawable[] b(Drawable drawable, List list, e eVar, O1.a aVar, c cVar, Drawable drawable2, List list2, k kVar) {
            ArrayList arrayList = new ArrayList();
            if (drawable != null) {
                arrayList.add(drawable);
            }
            arrayList.addAll(AbstractC0717n.B(list));
            if (eVar != null) {
                arrayList.add(eVar);
            }
            if (aVar != null) {
                arrayList.add(aVar);
            }
            if (cVar != null) {
                arrayList.add(cVar);
            }
            if (drawable2 != null) {
                arrayList.add(drawable2);
            }
            arrayList.addAll(AbstractC0717n.B(list2));
            if (kVar != null) {
                arrayList.add(kVar);
            }
            return (Drawable[]) arrayList.toArray(new Drawable[0]);
        }

        private a() {
        }
    }

    public /* synthetic */ g(Context context, Drawable drawable, List list, e eVar, O1.a aVar, c cVar, Drawable drawable2, List list2, k kVar, R1.c cVar2, R1.e eVar2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : drawable, (i3 & 4) != 0 ? AbstractC0717n.g() : list, (i3 & 8) != 0 ? null : eVar, (i3 & 16) != 0 ? null : aVar, (i3 & 32) != 0 ? null : cVar, (i3 & 64) != 0 ? null : drawable2, (i3 & 128) != 0 ? AbstractC0717n.g() : list2, (i3 & 256) != 0 ? null : kVar, (i3 & 512) != 0 ? null : cVar2, (i3 & 1024) == 0 ? eVar2 : null);
    }

    public final O1.a a() {
        return this.f1559f;
    }

    public final c b() {
        return this.f1560g;
    }

    public final R1.c c() {
        return this.f1564k;
    }

    public final R1.e d() {
        return this.f1565l;
    }

    public final e e() {
        return this.f1558e;
    }

    public final List f() {
        return this.f1562i;
    }

    public final Drawable g() {
        return this.f1556c;
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        D2.h.f(outline, "outline");
        R1.e eVar = this.f1565l;
        if (eVar == null || !eVar.c()) {
            outline.setRect(getBounds());
            return;
        }
        Path path = new Path();
        R1.e eVar2 = this.f1565l;
        R1.j jVarD = eVar2 != null ? eVar2.d(getLayoutDirection(), this.f1555b, getBounds().width(), getBounds().height()) : null;
        R1.c cVar = this.f1564k;
        RectF rectFA = cVar != null ? cVar.a(getLayoutDirection(), this.f1555b) : null;
        if (jVarD != null) {
            RectF rectF = new RectF(getBounds());
            C0429f0 c0429f0 = C0429f0.f7476a;
            path.addRoundRect(rectF, new float[]{c0429f0.b(jVarD.c().a() + (rectFA != null ? rectFA.left : 0.0f)), c0429f0.b(jVarD.c().b() + (rectFA != null ? rectFA.top : 0.0f)), c0429f0.b(jVarD.d().a() + (rectFA != null ? rectFA.right : 0.0f)), c0429f0.b(jVarD.d().b() + (rectFA != null ? rectFA.top : 0.0f)), c0429f0.b(jVarD.b().a() + (rectFA != null ? rectFA.right : 0.0f)), c0429f0.b(jVarD.b().b() + (rectFA != null ? rectFA.bottom : 0.0f)), c0429f0.b(jVarD.a().a() + (rectFA != null ? rectFA.left : 0.0f)), c0429f0.b(jVarD.a().b() + (rectFA != null ? rectFA.bottom : 0.0f))}, Path.Direction.CW);
        }
        if (Build.VERSION.SDK_INT >= 30) {
            outline.setPath(path);
        } else {
            outline.setConvexPath(path);
        }
    }

    public final List h() {
        return this.f1557d;
    }

    public final k i() {
        return this.f1563j;
    }

    public final void j(R1.c cVar) {
        this.f1564k = cVar;
    }

    public final void k(R1.e eVar) {
        this.f1565l = eVar;
    }

    public final g l(O1.a aVar) {
        return new g(this.f1555b, this.f1556c, this.f1557d, this.f1558e, aVar, this.f1560g, this.f1561h, this.f1562i, this.f1563j, this.f1564k, this.f1565l);
    }

    public final g m(c cVar) {
        D2.h.f(cVar, "border");
        return new g(this.f1555b, this.f1556c, this.f1557d, this.f1558e, this.f1559f, cVar, this.f1561h, this.f1562i, this.f1563j, this.f1564k, this.f1565l);
    }

    public final g n(e eVar) {
        return new g(this.f1555b, this.f1556c, this.f1557d, eVar, this.f1559f, this.f1560g, this.f1561h, this.f1562i, this.f1563j, this.f1564k, this.f1565l);
    }

    public final g o(Drawable drawable) {
        return new g(this.f1555b, this.f1556c, this.f1557d, this.f1558e, this.f1559f, this.f1560g, drawable, this.f1562i, this.f1563j, this.f1564k, this.f1565l);
    }

    public final g p(k kVar) {
        D2.h.f(kVar, "outline");
        return new g(this.f1555b, this.f1556c, this.f1557d, this.f1558e, this.f1559f, this.f1560g, this.f1561h, this.f1562i, kVar, this.f1564k, this.f1565l);
    }

    public final g q(List list, List list2) {
        D2.h.f(list, "outerShadows");
        D2.h.f(list2, "innerShadows");
        return new g(this.f1555b, this.f1556c, list, this.f1558e, this.f1559f, this.f1560g, this.f1561h, list2, this.f1563j, this.f1564k, this.f1565l);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g(Context context, Drawable drawable, List<? extends Drawable> list, e eVar, O1.a aVar, c cVar, Drawable drawable2, List<? extends Drawable> list2, k kVar, R1.c cVar2, R1.e eVar2) {
        super(f1554m.b(drawable, list, eVar, aVar, cVar, drawable2, list2, kVar));
        D2.h.f(context, "context");
        D2.h.f(list, "outerShadows");
        D2.h.f(list2, "innerShadows");
        this.f1555b = context;
        this.f1556c = drawable;
        this.f1557d = list;
        this.f1558e = eVar;
        this.f1559f = aVar;
        this.f1560g = cVar;
        this.f1561h = drawable2;
        this.f1562i = list2;
        this.f1563j = kVar;
        this.f1564k = cVar2;
        this.f1565l = eVar2;
        setPaddingMode(1);
    }
}
