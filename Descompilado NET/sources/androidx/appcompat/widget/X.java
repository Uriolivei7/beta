package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import g.AbstractC0539a;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public final class X {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static X f4037i;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private WeakHashMap f4039a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private l.g f4040b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private l.h f4041c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final WeakHashMap f4042d = new WeakHashMap(0);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private TypedValue f4043e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f4044f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private c f4045g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final PorterDuff.Mode f4036h = PorterDuff.Mode.SRC_IN;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final a f4038j = new a(6);

    private static class a extends l.e {
        public a(int i3) {
            super(i3);
        }

        private static int h(int i3, PorterDuff.Mode mode) {
            return ((i3 + 31) * 31) + mode.hashCode();
        }

        PorterDuffColorFilter i(int i3, PorterDuff.Mode mode) {
            return (PorterDuffColorFilter) c(Integer.valueOf(h(i3, mode)));
        }

        PorterDuffColorFilter j(int i3, PorterDuff.Mode mode, PorterDuffColorFilter porterDuffColorFilter) {
            return (PorterDuffColorFilter) d(Integer.valueOf(h(i3, mode)), porterDuffColorFilter);
        }
    }

    private interface b {
        Drawable a(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme);
    }

    public interface c {
        boolean a(Context context, int i3, Drawable drawable);

        PorterDuff.Mode b(int i3);

        Drawable c(X x3, Context context, int i3);

        ColorStateList d(Context context, int i3);

        boolean e(Context context, int i3, Drawable drawable);
    }

    private synchronized boolean a(Context context, long j3, Drawable drawable) {
        try {
            Drawable.ConstantState constantState = drawable.getConstantState();
            if (constantState == null) {
                return false;
            }
            l.d dVar = (l.d) this.f4042d.get(context);
            if (dVar == null) {
                dVar = new l.d();
                this.f4042d.put(context, dVar);
            }
            dVar.h(j3, new WeakReference(constantState));
            return true;
        } catch (Throwable th) {
            throw th;
        }
    }

    private void b(Context context, int i3, ColorStateList colorStateList) {
        if (this.f4039a == null) {
            this.f4039a = new WeakHashMap();
        }
        l.h hVar = (l.h) this.f4039a.get(context);
        if (hVar == null) {
            hVar = new l.h();
            this.f4039a.put(context, hVar);
        }
        hVar.b(i3, colorStateList);
    }

    private void c(Context context) {
        if (this.f4044f) {
            return;
        }
        this.f4044f = true;
        Drawable drawableI = i(context, AbstractC0539a.f9413a);
        if (drawableI == null || !p(drawableI)) {
            this.f4044f = false;
            throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
        }
    }

    private static long d(TypedValue typedValue) {
        return (((long) typedValue.assetCookie) << 32) | ((long) typedValue.data);
    }

    private Drawable e(Context context, int i3) {
        if (this.f4043e == null) {
            this.f4043e = new TypedValue();
        }
        TypedValue typedValue = this.f4043e;
        context.getResources().getValue(i3, typedValue, true);
        long jD = d(typedValue);
        Drawable drawableH = h(context, jD);
        if (drawableH != null) {
            return drawableH;
        }
        c cVar = this.f4045g;
        Drawable drawableC = cVar == null ? null : cVar.c(this, context, i3);
        if (drawableC != null) {
            drawableC.setChangingConfigurations(typedValue.changingConfigurations);
            a(context, jD, drawableC);
        }
        return drawableC;
    }

    private static PorterDuffColorFilter f(ColorStateList colorStateList, PorterDuff.Mode mode, int[] iArr) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return k(colorStateList.getColorForState(iArr, 0), mode);
    }

    public static synchronized X g() {
        try {
            if (f4037i == null) {
                X x3 = new X();
                f4037i = x3;
                o(x3);
            }
        } catch (Throwable th) {
            throw th;
        }
        return f4037i;
    }

    private synchronized Drawable h(Context context, long j3) {
        l.d dVar = (l.d) this.f4042d.get(context);
        if (dVar == null) {
            return null;
        }
        WeakReference weakReference = (WeakReference) dVar.e(j3);
        if (weakReference != null) {
            Drawable.ConstantState constantState = (Drawable.ConstantState) weakReference.get();
            if (constantState != null) {
                return constantState.newDrawable(context.getResources());
            }
            dVar.j(j3);
        }
        return null;
    }

    public static synchronized PorterDuffColorFilter k(int i3, PorterDuff.Mode mode) {
        PorterDuffColorFilter porterDuffColorFilterI;
        a aVar = f4038j;
        porterDuffColorFilterI = aVar.i(i3, mode);
        if (porterDuffColorFilterI == null) {
            porterDuffColorFilterI = new PorterDuffColorFilter(i3, mode);
            aVar.j(i3, mode, porterDuffColorFilterI);
        }
        return porterDuffColorFilterI;
    }

    private ColorStateList m(Context context, int i3) {
        l.h hVar;
        WeakHashMap weakHashMap = this.f4039a;
        if (weakHashMap == null || (hVar = (l.h) weakHashMap.get(context)) == null) {
            return null;
        }
        return (ColorStateList) hVar.g(i3);
    }

    private static void o(X x3) {
    }

    private static boolean p(Drawable drawable) {
        return (drawable instanceof K.b) || "android.graphics.drawable.VectorDrawable".equals(drawable.getClass().getName());
    }

    private Drawable q(Context context, int i3) {
        int next;
        l.g gVar = this.f4040b;
        if (gVar == null || gVar.isEmpty()) {
            return null;
        }
        l.h hVar = this.f4041c;
        if (hVar != null) {
            String str = (String) hVar.g(i3);
            if ("appcompat_skip_skip".equals(str) || (str != null && this.f4040b.get(str) == null)) {
                return null;
            }
        } else {
            this.f4041c = new l.h();
        }
        if (this.f4043e == null) {
            this.f4043e = new TypedValue();
        }
        TypedValue typedValue = this.f4043e;
        Resources resources = context.getResources();
        resources.getValue(i3, typedValue, true);
        long jD = d(typedValue);
        Drawable drawableH = h(context, jD);
        if (drawableH != null) {
            return drawableH;
        }
        CharSequence charSequence = typedValue.string;
        if (charSequence != null && charSequence.toString().endsWith(".xml")) {
            try {
                XmlResourceParser xml = resources.getXml(i3);
                AttributeSet attributeSetAsAttributeSet = Xml.asAttributeSet(xml);
                do {
                    next = xml.next();
                    if (next == 2) {
                        break;
                    }
                } while (next != 1);
                if (next != 2) {
                    throw new XmlPullParserException("No start tag found");
                }
                String name = xml.getName();
                this.f4041c.b(i3, name);
                b bVar = (b) this.f4040b.get(name);
                if (bVar != null) {
                    drawableH = bVar.a(context, xml, attributeSetAsAttributeSet, context.getTheme());
                }
                if (drawableH != null) {
                    drawableH.setChangingConfigurations(typedValue.changingConfigurations);
                    a(context, jD, drawableH);
                }
            } catch (Exception e4) {
                Log.e("ResourceManagerInternal", "Exception while inflating drawable", e4);
            }
        }
        if (drawableH == null) {
            this.f4041c.b(i3, "appcompat_skip_skip");
        }
        return drawableH;
    }

    private Drawable u(Context context, int i3, boolean z3, Drawable drawable) {
        ColorStateList colorStateListL = l(context, i3);
        if (colorStateListL != null) {
            Drawable drawableJ = androidx.core.graphics.drawable.a.j(drawable.mutate());
            androidx.core.graphics.drawable.a.g(drawableJ, colorStateListL);
            PorterDuff.Mode modeN = n(i3);
            if (modeN == null) {
                return drawableJ;
            }
            androidx.core.graphics.drawable.a.h(drawableJ, modeN);
            return drawableJ;
        }
        c cVar = this.f4045g;
        if ((cVar == null || !cVar.e(context, i3, drawable)) && !w(context, i3, drawable) && z3) {
            return null;
        }
        return drawable;
    }

    static void v(Drawable drawable, f0 f0Var, int[] iArr) {
        int[] state = drawable.getState();
        if (drawable.mutate() != drawable) {
            Log.d("ResourceManagerInternal", "Mutated drawable is not the same instance as the input.");
            return;
        }
        if ((drawable instanceof LayerDrawable) && drawable.isStateful()) {
            drawable.setState(new int[0]);
            drawable.setState(state);
        }
        boolean z3 = f0Var.f4217d;
        if (z3 || f0Var.f4216c) {
            drawable.setColorFilter(f(z3 ? f0Var.f4214a : null, f0Var.f4216c ? f0Var.f4215b : f4036h, iArr));
        } else {
            drawable.clearColorFilter();
        }
    }

    public synchronized Drawable i(Context context, int i3) {
        return j(context, i3, false);
    }

    synchronized Drawable j(Context context, int i3, boolean z3) {
        Drawable drawableQ;
        try {
            c(context);
            drawableQ = q(context, i3);
            if (drawableQ == null) {
                drawableQ = e(context, i3);
            }
            if (drawableQ == null) {
                drawableQ = androidx.core.content.a.d(context, i3);
            }
            if (drawableQ != null) {
                drawableQ = u(context, i3, z3, drawableQ);
            }
            if (drawableQ != null) {
                O.a(drawableQ);
            }
        } catch (Throwable th) {
            throw th;
        }
        return drawableQ;
    }

    synchronized ColorStateList l(Context context, int i3) {
        ColorStateList colorStateListM;
        colorStateListM = m(context, i3);
        if (colorStateListM == null) {
            c cVar = this.f4045g;
            colorStateListM = cVar == null ? null : cVar.d(context, i3);
            if (colorStateListM != null) {
                b(context, i3, colorStateListM);
            }
        }
        return colorStateListM;
    }

    PorterDuff.Mode n(int i3) {
        c cVar = this.f4045g;
        if (cVar == null) {
            return null;
        }
        return cVar.b(i3);
    }

    public synchronized void r(Context context) {
        l.d dVar = (l.d) this.f4042d.get(context);
        if (dVar != null) {
            dVar.b();
        }
    }

    synchronized Drawable s(Context context, r0 r0Var, int i3) {
        try {
            Drawable drawableQ = q(context, i3);
            if (drawableQ == null) {
                drawableQ = r0Var.a(i3);
            }
            if (drawableQ == null) {
                return null;
            }
            return u(context, i3, false, drawableQ);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void t(c cVar) {
        this.f4045g = cVar;
    }

    boolean w(Context context, int i3, Drawable drawable) {
        c cVar = this.f4045g;
        return cVar != null && cVar.a(context, i3, drawable);
    }
}
