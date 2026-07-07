package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.LocaleList;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import androidx.core.content.res.f;
import java.lang.ref.WeakReference;
import u.C0733c;

/* JADX INFO: loaded from: classes.dex */
class C {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final TextView f3745a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private f0 f3746b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private f0 f3747c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private f0 f3748d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private f0 f3749e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private f0 f3750f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private f0 f3751g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private f0 f3752h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final E f3753i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f3754j = 0;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f3755k = -1;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private Typeface f3756l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f3757m;

    class a extends f.e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f3758a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f3759b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ WeakReference f3760c;

        a(int i3, int i4, WeakReference weakReference) {
            this.f3758a = i3;
            this.f3759b = i4;
            this.f3760c = weakReference;
        }

        @Override // androidx.core.content.res.f.e
        /* JADX INFO: renamed from: h */
        public void f(int i3) {
        }

        @Override // androidx.core.content.res.f.e
        /* JADX INFO: renamed from: i */
        public void g(Typeface typeface) {
            int i3;
            if (Build.VERSION.SDK_INT >= 28 && (i3 = this.f3758a) != -1) {
                typeface = e.a(typeface, i3, (this.f3759b & 2) != 0);
            }
            C.this.n(this.f3760c, typeface);
        }
    }

    class b implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ TextView f3762b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Typeface f3763c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f3764d;

        b(TextView textView, Typeface typeface, int i3) {
            this.f3762b = textView;
            this.f3763c = typeface;
            this.f3764d = i3;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f3762b.setTypeface(this.f3763c, this.f3764d);
        }
    }

    static class c {
        static LocaleList a(String str) {
            return LocaleList.forLanguageTags(str);
        }

        static void b(TextView textView, LocaleList localeList) {
            textView.setTextLocales(localeList);
        }
    }

    static class d {
        static int a(TextView textView) {
            return textView.getAutoSizeStepGranularity();
        }

        static void b(TextView textView, int i3, int i4, int i5, int i6) {
            textView.setAutoSizeTextTypeUniformWithConfiguration(i3, i4, i5, i6);
        }

        static void c(TextView textView, int[] iArr, int i3) {
            textView.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i3);
        }

        static boolean d(TextView textView, String str) {
            return textView.setFontVariationSettings(str);
        }
    }

    static class e {
        static Typeface a(Typeface typeface, int i3, boolean z3) {
            return Typeface.create(typeface, i3, z3);
        }
    }

    C(TextView textView) {
        this.f3745a = textView;
        this.f3753i = new E(textView);
    }

    private void B(int i3, float f3) {
        this.f3753i.t(i3, f3);
    }

    private void C(Context context, h0 h0Var) {
        String strN;
        this.f3754j = h0Var.j(d.j.f8935V2, this.f3754j);
        int i3 = Build.VERSION.SDK_INT;
        if (i3 >= 28) {
            int iJ = h0Var.j(d.j.f8947Y2, -1);
            this.f3755k = iJ;
            if (iJ != -1) {
                this.f3754j &= 2;
            }
        }
        if (!h0Var.r(d.j.f8943X2) && !h0Var.r(d.j.f8951Z2)) {
            if (h0Var.r(d.j.f8931U2)) {
                this.f3757m = false;
                int iJ2 = h0Var.j(d.j.f8931U2, 1);
                if (iJ2 == 1) {
                    this.f3756l = Typeface.SANS_SERIF;
                    return;
                } else if (iJ2 == 2) {
                    this.f3756l = Typeface.SERIF;
                    return;
                } else {
                    if (iJ2 != 3) {
                        return;
                    }
                    this.f3756l = Typeface.MONOSPACE;
                    return;
                }
            }
            return;
        }
        this.f3756l = null;
        int i4 = h0Var.r(d.j.f8951Z2) ? d.j.f8951Z2 : d.j.f8943X2;
        int i5 = this.f3755k;
        int i6 = this.f3754j;
        if (!context.isRestricted()) {
            try {
                Typeface typefaceI = h0Var.i(i4, this.f3754j, new a(i5, i6, new WeakReference(this.f3745a)));
                if (typefaceI != null) {
                    if (i3 < 28 || this.f3755k == -1) {
                        this.f3756l = typefaceI;
                    } else {
                        this.f3756l = e.a(Typeface.create(typefaceI, 0), this.f3755k, (this.f3754j & 2) != 0);
                    }
                }
                this.f3757m = this.f3756l == null;
            } catch (Resources.NotFoundException | UnsupportedOperationException unused) {
            }
        }
        if (this.f3756l != null || (strN = h0Var.n(i4)) == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 28 || this.f3755k == -1) {
            this.f3756l = Typeface.create(strN, this.f3754j);
        } else {
            this.f3756l = e.a(Typeface.create(strN, 0), this.f3755k, (this.f3754j & 2) != 0);
        }
    }

    private void a(Drawable drawable, f0 f0Var) {
        if (drawable == null || f0Var == null) {
            return;
        }
        C0222k.i(drawable, f0Var, this.f3745a.getDrawableState());
    }

    private static f0 d(Context context, C0222k c0222k, int i3) {
        ColorStateList colorStateListF = c0222k.f(context, i3);
        if (colorStateListF == null) {
            return null;
        }
        f0 f0Var = new f0();
        f0Var.f4217d = true;
        f0Var.f4214a = colorStateListF;
        return f0Var;
    }

    private void y(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5, Drawable drawable6) {
        if (drawable5 != null || drawable6 != null) {
            Drawable[] compoundDrawablesRelative = this.f3745a.getCompoundDrawablesRelative();
            if (drawable5 == null) {
                drawable5 = compoundDrawablesRelative[0];
            }
            if (drawable2 == null) {
                drawable2 = compoundDrawablesRelative[1];
            }
            if (drawable6 == null) {
                drawable6 = compoundDrawablesRelative[2];
            }
            TextView textView = this.f3745a;
            if (drawable4 == null) {
                drawable4 = compoundDrawablesRelative[3];
            }
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable5, drawable2, drawable6, drawable4);
            return;
        }
        if (drawable == null && drawable2 == null && drawable3 == null && drawable4 == null) {
            return;
        }
        Drawable[] compoundDrawablesRelative2 = this.f3745a.getCompoundDrawablesRelative();
        Drawable drawable7 = compoundDrawablesRelative2[0];
        if (drawable7 != null || compoundDrawablesRelative2[2] != null) {
            if (drawable2 == null) {
                drawable2 = compoundDrawablesRelative2[1];
            }
            if (drawable4 == null) {
                drawable4 = compoundDrawablesRelative2[3];
            }
            this.f3745a.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable7, drawable2, compoundDrawablesRelative2[2], drawable4);
            return;
        }
        Drawable[] compoundDrawables = this.f3745a.getCompoundDrawables();
        TextView textView2 = this.f3745a;
        if (drawable == null) {
            drawable = compoundDrawables[0];
        }
        if (drawable2 == null) {
            drawable2 = compoundDrawables[1];
        }
        if (drawable3 == null) {
            drawable3 = compoundDrawables[2];
        }
        if (drawable4 == null) {
            drawable4 = compoundDrawables[3];
        }
        textView2.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
    }

    private void z() {
        f0 f0Var = this.f3752h;
        this.f3746b = f0Var;
        this.f3747c = f0Var;
        this.f3748d = f0Var;
        this.f3749e = f0Var;
        this.f3750f = f0Var;
        this.f3751g = f0Var;
    }

    void A(int i3, float f3) {
        if (s0.f4327c || l()) {
            return;
        }
        B(i3, f3);
    }

    void b() {
        if (this.f3746b != null || this.f3747c != null || this.f3748d != null || this.f3749e != null) {
            Drawable[] compoundDrawables = this.f3745a.getCompoundDrawables();
            a(compoundDrawables[0], this.f3746b);
            a(compoundDrawables[1], this.f3747c);
            a(compoundDrawables[2], this.f3748d);
            a(compoundDrawables[3], this.f3749e);
        }
        if (this.f3750f == null && this.f3751g == null) {
            return;
        }
        Drawable[] compoundDrawablesRelative = this.f3745a.getCompoundDrawablesRelative();
        a(compoundDrawablesRelative[0], this.f3750f);
        a(compoundDrawablesRelative[2], this.f3751g);
    }

    void c() {
        this.f3753i.a();
    }

    int e() {
        return this.f3753i.f();
    }

    int f() {
        return this.f3753i.g();
    }

    int g() {
        return this.f3753i.h();
    }

    int[] h() {
        return this.f3753i.i();
    }

    int i() {
        return this.f3753i.j();
    }

    ColorStateList j() {
        f0 f0Var = this.f3752h;
        if (f0Var != null) {
            return f0Var.f4214a;
        }
        return null;
    }

    PorterDuff.Mode k() {
        f0 f0Var = this.f3752h;
        if (f0Var != null) {
            return f0Var.f4215b;
        }
        return null;
    }

    boolean l() {
        return this.f3753i.n();
    }

    /* JADX WARN: Removed duplicated region for block: B:125:0x029b  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x02a2  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x02ab  */
    /* JADX WARN: Removed duplicated region for block: B:134:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void m(android.util.AttributeSet r17, int r18) {
        /*
            Method dump skipped, instruction units count: 698
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.C.m(android.util.AttributeSet, int):void");
    }

    void n(WeakReference weakReference, Typeface typeface) {
        if (this.f3757m) {
            this.f3756l = typeface;
            TextView textView = (TextView) weakReference.get();
            if (textView != null) {
                if (textView.isAttachedToWindow()) {
                    textView.post(new b(textView, typeface, this.f3754j));
                } else {
                    textView.setTypeface(typeface, this.f3754j);
                }
            }
        }
    }

    void o(boolean z3, int i3, int i4, int i5, int i6) {
        if (s0.f4327c) {
            return;
        }
        c();
    }

    void p() {
        b();
    }

    void q(Context context, int i3) {
        String strN;
        h0 h0VarS = h0.s(context, i3, d.j.f8923S2);
        if (h0VarS.r(d.j.f8961b3)) {
            s(h0VarS.a(d.j.f8961b3, false));
        }
        int i4 = Build.VERSION.SDK_INT;
        if (h0VarS.r(d.j.f8927T2) && h0VarS.e(d.j.f8927T2, -1) == 0) {
            this.f3745a.setTextSize(0, 0.0f);
        }
        C(context, h0VarS);
        if (i4 >= 26 && h0VarS.r(d.j.f8956a3) && (strN = h0VarS.n(d.j.f8956a3)) != null) {
            d.d(this.f3745a, strN);
        }
        h0VarS.w();
        Typeface typeface = this.f3756l;
        if (typeface != null) {
            this.f3745a.setTypeface(typeface, this.f3754j);
        }
    }

    void r(TextView textView, InputConnection inputConnection, EditorInfo editorInfo) {
        if (Build.VERSION.SDK_INT >= 30 || inputConnection == null) {
            return;
        }
        C0733c.f(editorInfo, textView.getText());
    }

    void s(boolean z3) {
        this.f3745a.setAllCaps(z3);
    }

    void t(int i3, int i4, int i5, int i6) {
        this.f3753i.p(i3, i4, i5, i6);
    }

    void u(int[] iArr, int i3) {
        this.f3753i.q(iArr, i3);
    }

    void v(int i3) {
        this.f3753i.r(i3);
    }

    void w(ColorStateList colorStateList) {
        if (this.f3752h == null) {
            this.f3752h = new f0();
        }
        f0 f0Var = this.f3752h;
        f0Var.f4214a = colorStateList;
        f0Var.f4217d = colorStateList != null;
        z();
    }

    void x(PorterDuff.Mode mode) {
        if (this.f3752h == null) {
            this.f3752h = new f0();
        }
        f0 f0Var = this.f3752h;
        f0Var.f4215b = mode;
        f0Var.f4216c = mode != null;
        z();
    }
}
