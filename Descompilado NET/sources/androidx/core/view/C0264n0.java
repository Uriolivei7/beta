package androidx.core.view;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/* JADX INFO: renamed from: androidx.core.view.n0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0264n0 {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final C0264n0 f4625b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final l f4626a;

    /* JADX INFO: renamed from: androidx.core.view.n0$a */
    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static Field f4627a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private static Field f4628b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private static Field f4629c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private static boolean f4630d;

        static {
            try {
                Field declaredField = View.class.getDeclaredField("mAttachInfo");
                f4627a = declaredField;
                declaredField.setAccessible(true);
                Class<?> cls = Class.forName("android.view.View$AttachInfo");
                Field declaredField2 = cls.getDeclaredField("mStableInsets");
                f4628b = declaredField2;
                declaredField2.setAccessible(true);
                Field declaredField3 = cls.getDeclaredField("mContentInsets");
                f4629c = declaredField3;
                declaredField3.setAccessible(true);
                f4630d = true;
            } catch (ReflectiveOperationException e4) {
                Log.w("WindowInsetsCompat", "Failed to get visible insets from AttachInfo " + e4.getMessage(), e4);
            }
        }

        public static C0264n0 a(View view) {
            if (f4630d && view.isAttachedToWindow()) {
                try {
                    Object obj = f4627a.get(view.getRootView());
                    if (obj != null) {
                        Rect rect = (Rect) f4628b.get(obj);
                        Rect rect2 = (Rect) f4629c.get(obj);
                        if (rect != null && rect2 != null) {
                            C0264n0 c0264n0A = new b().b(androidx.core.graphics.b.c(rect)).c(androidx.core.graphics.b.c(rect2)).a();
                            c0264n0A.s(c0264n0A);
                            c0264n0A.d(view.getRootView());
                            return c0264n0A;
                        }
                    }
                } catch (IllegalAccessException e4) {
                    Log.w("WindowInsetsCompat", "Failed to get insets from AttachInfo. " + e4.getMessage(), e4);
                }
            }
            return null;
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$e */
    private static class e extends d {
        e() {
        }

        e(C0264n0 c0264n0) {
            super(c0264n0);
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$f */
    private static class f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final C0264n0 f4639a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        androidx.core.graphics.b[] f4640b;

        f() {
            this(new C0264n0((C0264n0) null));
        }

        protected final void a() {
            androidx.core.graphics.b[] bVarArr = this.f4640b;
            if (bVarArr != null) {
                androidx.core.graphics.b bVarF = bVarArr[m.b(1)];
                androidx.core.graphics.b bVarF2 = this.f4640b[m.b(2)];
                if (bVarF2 == null) {
                    bVarF2 = this.f4639a.f(2);
                }
                if (bVarF == null) {
                    bVarF = this.f4639a.f(1);
                }
                f(androidx.core.graphics.b.a(bVarF, bVarF2));
                androidx.core.graphics.b bVar = this.f4640b[m.b(16)];
                if (bVar != null) {
                    e(bVar);
                }
                androidx.core.graphics.b bVar2 = this.f4640b[m.b(32)];
                if (bVar2 != null) {
                    c(bVar2);
                }
                androidx.core.graphics.b bVar3 = this.f4640b[m.b(64)];
                if (bVar3 != null) {
                    g(bVar3);
                }
            }
        }

        abstract C0264n0 b();

        void c(androidx.core.graphics.b bVar) {
        }

        abstract void d(androidx.core.graphics.b bVar);

        void e(androidx.core.graphics.b bVar) {
        }

        abstract void f(androidx.core.graphics.b bVar);

        void g(androidx.core.graphics.b bVar) {
        }

        f(C0264n0 c0264n0) {
            this.f4639a = c0264n0;
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$i */
    private static class i extends h {
        i(C0264n0 c0264n0, WindowInsets windowInsets) {
            super(c0264n0, windowInsets);
        }

        @Override // androidx.core.view.C0264n0.l
        C0264n0 a() {
            return C0264n0.v(this.f4646c.consumeDisplayCutout());
        }

        @Override // androidx.core.view.C0264n0.g, androidx.core.view.C0264n0.l
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof i)) {
                return false;
            }
            i iVar = (i) obj;
            return Objects.equals(this.f4646c, iVar.f4646c) && Objects.equals(this.f4650g, iVar.f4650g);
        }

        @Override // androidx.core.view.C0264n0.l
        C0274v f() {
            return C0274v.f(this.f4646c.getDisplayCutout());
        }

        @Override // androidx.core.view.C0264n0.l
        public int hashCode() {
            return this.f4646c.hashCode();
        }

        i(C0264n0 c0264n0, i iVar) {
            super(c0264n0, iVar);
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$k */
    private static class k extends j {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        static final C0264n0 f4655q = C0264n0.v(WindowInsets.CONSUMED);

        k(C0264n0 c0264n0, WindowInsets windowInsets) {
            super(c0264n0, windowInsets);
        }

        @Override // androidx.core.view.C0264n0.g, androidx.core.view.C0264n0.l
        final void d(View view) {
        }

        @Override // androidx.core.view.C0264n0.g, androidx.core.view.C0264n0.l
        public androidx.core.graphics.b g(int i3) {
            return androidx.core.graphics.b.d(this.f4646c.getInsets(n.a(i3)));
        }

        @Override // androidx.core.view.C0264n0.g, androidx.core.view.C0264n0.l
        public boolean p(int i3) {
            return this.f4646c.isVisible(n.a(i3));
        }

        k(C0264n0 c0264n0, k kVar) {
            super(c0264n0, kVar);
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$l */
    private static class l {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        static final C0264n0 f4656b = new b().a().a().b().c();

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final C0264n0 f4657a;

        l(C0264n0 c0264n0) {
            this.f4657a = c0264n0;
        }

        C0264n0 a() {
            return this.f4657a;
        }

        C0264n0 b() {
            return this.f4657a;
        }

        C0264n0 c() {
            return this.f4657a;
        }

        void d(View view) {
        }

        void e(C0264n0 c0264n0) {
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof l)) {
                return false;
            }
            l lVar = (l) obj;
            return o() == lVar.o() && n() == lVar.n() && q.c.a(k(), lVar.k()) && q.c.a(i(), lVar.i()) && q.c.a(f(), lVar.f());
        }

        C0274v f() {
            return null;
        }

        androidx.core.graphics.b g(int i3) {
            return androidx.core.graphics.b.f4471e;
        }

        androidx.core.graphics.b h() {
            return k();
        }

        public int hashCode() {
            return q.c.b(Boolean.valueOf(o()), Boolean.valueOf(n()), k(), i(), f());
        }

        androidx.core.graphics.b i() {
            return androidx.core.graphics.b.f4471e;
        }

        androidx.core.graphics.b j() {
            return k();
        }

        androidx.core.graphics.b k() {
            return androidx.core.graphics.b.f4471e;
        }

        androidx.core.graphics.b l() {
            return k();
        }

        C0264n0 m(int i3, int i4, int i5, int i6) {
            return f4656b;
        }

        boolean n() {
            return false;
        }

        boolean o() {
            return false;
        }

        boolean p(int i3) {
            return true;
        }

        public void q(androidx.core.graphics.b[] bVarArr) {
        }

        void r(androidx.core.graphics.b bVar) {
        }

        void s(C0264n0 c0264n0) {
        }

        public void t(androidx.core.graphics.b bVar) {
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$m */
    public static final class m {
        public static int a() {
            return 128;
        }

        static int b(int i3) {
            if (i3 == 1) {
                return 0;
            }
            if (i3 == 2) {
                return 1;
            }
            if (i3 == 4) {
                return 2;
            }
            if (i3 == 8) {
                return 3;
            }
            if (i3 == 16) {
                return 4;
            }
            if (i3 == 32) {
                return 5;
            }
            if (i3 == 64) {
                return 6;
            }
            if (i3 == 128) {
                return 7;
            }
            if (i3 == 256) {
                return 8;
            }
            throw new IllegalArgumentException("type needs to be >= FIRST and <= LAST, type=" + i3);
        }

        public static int c() {
            return 2;
        }

        public static int d() {
            return 1;
        }

        public static int e() {
            return 7;
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$n */
    private static final class n {
        static int a(int i3) {
            int iStatusBars;
            int i4 = 0;
            for (int i5 = 1; i5 <= 256; i5 <<= 1) {
                if ((i3 & i5) != 0) {
                    if (i5 == 1) {
                        iStatusBars = WindowInsets.Type.statusBars();
                    } else if (i5 == 2) {
                        iStatusBars = WindowInsets.Type.navigationBars();
                    } else if (i5 == 4) {
                        iStatusBars = WindowInsets.Type.captionBar();
                    } else if (i5 == 8) {
                        iStatusBars = WindowInsets.Type.ime();
                    } else if (i5 == 16) {
                        iStatusBars = WindowInsets.Type.systemGestures();
                    } else if (i5 == 32) {
                        iStatusBars = WindowInsets.Type.mandatorySystemGestures();
                    } else if (i5 == 64) {
                        iStatusBars = WindowInsets.Type.tappableElement();
                    } else if (i5 == 128) {
                        iStatusBars = WindowInsets.Type.displayCutout();
                    }
                    i4 |= iStatusBars;
                }
            }
            return i4;
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 30) {
            f4625b = k.f4655q;
        } else {
            f4625b = l.f4656b;
        }
    }

    private C0264n0(WindowInsets windowInsets) {
        int i3 = Build.VERSION.SDK_INT;
        if (i3 >= 30) {
            this.f4626a = new k(this, windowInsets);
            return;
        }
        if (i3 >= 29) {
            this.f4626a = new j(this, windowInsets);
        } else if (i3 >= 28) {
            this.f4626a = new i(this, windowInsets);
        } else {
            this.f4626a = new h(this, windowInsets);
        }
    }

    static androidx.core.graphics.b m(androidx.core.graphics.b bVar, int i3, int i4, int i5, int i6) {
        int iMax = Math.max(0, bVar.f4472a - i3);
        int iMax2 = Math.max(0, bVar.f4473b - i4);
        int iMax3 = Math.max(0, bVar.f4474c - i5);
        int iMax4 = Math.max(0, bVar.f4475d - i6);
        return (iMax == i3 && iMax2 == i4 && iMax3 == i5 && iMax4 == i6) ? bVar : androidx.core.graphics.b.b(iMax, iMax2, iMax3, iMax4);
    }

    public static C0264n0 v(WindowInsets windowInsets) {
        return w(windowInsets, null);
    }

    public static C0264n0 w(WindowInsets windowInsets, View view) {
        C0264n0 c0264n0 = new C0264n0((WindowInsets) q.g.g(windowInsets));
        if (view != null && view.isAttachedToWindow()) {
            c0264n0.s(Z.y(view));
            c0264n0.d(view.getRootView());
        }
        return c0264n0;
    }

    public C0264n0 a() {
        return this.f4626a.a();
    }

    public C0264n0 b() {
        return this.f4626a.b();
    }

    public C0264n0 c() {
        return this.f4626a.c();
    }

    void d(View view) {
        this.f4626a.d(view);
    }

    public C0274v e() {
        return this.f4626a.f();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof C0264n0) {
            return q.c.a(this.f4626a, ((C0264n0) obj).f4626a);
        }
        return false;
    }

    public androidx.core.graphics.b f(int i3) {
        return this.f4626a.g(i3);
    }

    public androidx.core.graphics.b g() {
        return this.f4626a.i();
    }

    public int h() {
        return this.f4626a.k().f4475d;
    }

    public int hashCode() {
        l lVar = this.f4626a;
        if (lVar == null) {
            return 0;
        }
        return lVar.hashCode();
    }

    public int i() {
        return this.f4626a.k().f4472a;
    }

    public int j() {
        return this.f4626a.k().f4474c;
    }

    public int k() {
        return this.f4626a.k().f4473b;
    }

    public C0264n0 l(int i3, int i4, int i5, int i6) {
        return this.f4626a.m(i3, i4, i5, i6);
    }

    public boolean n() {
        return this.f4626a.n();
    }

    public boolean o(int i3) {
        return this.f4626a.p(i3);
    }

    public C0264n0 p(int i3, int i4, int i5, int i6) {
        return new b(this).c(androidx.core.graphics.b.b(i3, i4, i5, i6)).a();
    }

    void q(androidx.core.graphics.b[] bVarArr) {
        this.f4626a.q(bVarArr);
    }

    void r(androidx.core.graphics.b bVar) {
        this.f4626a.r(bVar);
    }

    void s(C0264n0 c0264n0) {
        this.f4626a.s(c0264n0);
    }

    void t(androidx.core.graphics.b bVar) {
        this.f4626a.t(bVar);
    }

    public WindowInsets u() {
        l lVar = this.f4626a;
        if (lVar instanceof g) {
            return ((g) lVar).f4646c;
        }
        return null;
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$c */
    private static class c extends f {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private static Field f4632e = null;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private static boolean f4633f = false;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private static Constructor f4634g = null;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private static boolean f4635h = false;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private WindowInsets f4636c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private androidx.core.graphics.b f4637d;

        c() {
            this.f4636c = h();
        }

        private static WindowInsets h() {
            if (!f4633f) {
                try {
                    f4632e = WindowInsets.class.getDeclaredField("CONSUMED");
                } catch (ReflectiveOperationException e4) {
                    Log.i("WindowInsetsCompat", "Could not retrieve WindowInsets.CONSUMED field", e4);
                }
                f4633f = true;
            }
            Field field = f4632e;
            if (field != null) {
                try {
                    WindowInsets windowInsets = (WindowInsets) field.get(null);
                    if (windowInsets != null) {
                        return new WindowInsets(windowInsets);
                    }
                } catch (ReflectiveOperationException e5) {
                    Log.i("WindowInsetsCompat", "Could not get value from WindowInsets.CONSUMED field", e5);
                }
            }
            if (!f4635h) {
                try {
                    f4634g = WindowInsets.class.getConstructor(Rect.class);
                } catch (ReflectiveOperationException e6) {
                    Log.i("WindowInsetsCompat", "Could not retrieve WindowInsets(Rect) constructor", e6);
                }
                f4635h = true;
            }
            Constructor constructor = f4634g;
            if (constructor != null) {
                try {
                    return (WindowInsets) constructor.newInstance(new Rect());
                } catch (ReflectiveOperationException e7) {
                    Log.i("WindowInsetsCompat", "Could not invoke WindowInsets(Rect) constructor", e7);
                }
            }
            return null;
        }

        @Override // androidx.core.view.C0264n0.f
        C0264n0 b() {
            a();
            C0264n0 c0264n0V = C0264n0.v(this.f4636c);
            c0264n0V.q(this.f4640b);
            c0264n0V.t(this.f4637d);
            return c0264n0V;
        }

        @Override // androidx.core.view.C0264n0.f
        void d(androidx.core.graphics.b bVar) {
            this.f4637d = bVar;
        }

        @Override // androidx.core.view.C0264n0.f
        void f(androidx.core.graphics.b bVar) {
            WindowInsets windowInsets = this.f4636c;
            if (windowInsets != null) {
                this.f4636c = windowInsets.replaceSystemWindowInsets(bVar.f4472a, bVar.f4473b, bVar.f4474c, bVar.f4475d);
            }
        }

        c(C0264n0 c0264n0) {
            super(c0264n0);
            this.f4636c = c0264n0.u();
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$d */
    private static class d extends f {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final WindowInsets.Builder f4638c;

        d() {
            this.f4638c = v0.a();
        }

        @Override // androidx.core.view.C0264n0.f
        C0264n0 b() {
            a();
            C0264n0 c0264n0V = C0264n0.v(this.f4638c.build());
            c0264n0V.q(this.f4640b);
            return c0264n0V;
        }

        @Override // androidx.core.view.C0264n0.f
        void c(androidx.core.graphics.b bVar) {
            this.f4638c.setMandatorySystemGestureInsets(bVar.e());
        }

        @Override // androidx.core.view.C0264n0.f
        void d(androidx.core.graphics.b bVar) {
            this.f4638c.setStableInsets(bVar.e());
        }

        @Override // androidx.core.view.C0264n0.f
        void e(androidx.core.graphics.b bVar) {
            this.f4638c.setSystemGestureInsets(bVar.e());
        }

        @Override // androidx.core.view.C0264n0.f
        void f(androidx.core.graphics.b bVar) {
            this.f4638c.setSystemWindowInsets(bVar.e());
        }

        @Override // androidx.core.view.C0264n0.f
        void g(androidx.core.graphics.b bVar) {
            this.f4638c.setTappableElementInsets(bVar.e());
        }

        d(C0264n0 c0264n0) {
            WindowInsets.Builder builderA;
            super(c0264n0);
            WindowInsets windowInsetsU = c0264n0.u();
            if (windowInsetsU != null) {
                builderA = u0.a(windowInsetsU);
            } else {
                builderA = v0.a();
            }
            this.f4638c = builderA;
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$h */
    private static class h extends g {

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        private androidx.core.graphics.b f4651m;

        h(C0264n0 c0264n0, WindowInsets windowInsets) {
            super(c0264n0, windowInsets);
            this.f4651m = null;
        }

        @Override // androidx.core.view.C0264n0.l
        C0264n0 b() {
            return C0264n0.v(this.f4646c.consumeStableInsets());
        }

        @Override // androidx.core.view.C0264n0.l
        C0264n0 c() {
            return C0264n0.v(this.f4646c.consumeSystemWindowInsets());
        }

        @Override // androidx.core.view.C0264n0.l
        final androidx.core.graphics.b i() {
            if (this.f4651m == null) {
                this.f4651m = androidx.core.graphics.b.b(this.f4646c.getStableInsetLeft(), this.f4646c.getStableInsetTop(), this.f4646c.getStableInsetRight(), this.f4646c.getStableInsetBottom());
            }
            return this.f4651m;
        }

        @Override // androidx.core.view.C0264n0.l
        boolean n() {
            return this.f4646c.isConsumed();
        }

        @Override // androidx.core.view.C0264n0.l
        public void t(androidx.core.graphics.b bVar) {
            this.f4651m = bVar;
        }

        h(C0264n0 c0264n0, h hVar) {
            super(c0264n0, hVar);
            this.f4651m = null;
            this.f4651m = hVar.f4651m;
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$g */
    private static class g extends l {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private static boolean f4641h = false;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private static Method f4642i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private static Class f4643j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private static Field f4644k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        private static Field f4645l;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final WindowInsets f4646c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private androidx.core.graphics.b[] f4647d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private androidx.core.graphics.b f4648e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private C0264n0 f4649f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        androidx.core.graphics.b f4650g;

        g(C0264n0 c0264n0, WindowInsets windowInsets) {
            super(c0264n0);
            this.f4648e = null;
            this.f4646c = windowInsets;
        }

        private androidx.core.graphics.b u(int i3, boolean z3) {
            androidx.core.graphics.b bVarA = androidx.core.graphics.b.f4471e;
            for (int i4 = 1; i4 <= 256; i4 <<= 1) {
                if ((i3 & i4) != 0) {
                    bVarA = androidx.core.graphics.b.a(bVarA, v(i4, z3));
                }
            }
            return bVarA;
        }

        private androidx.core.graphics.b w() {
            C0264n0 c0264n0 = this.f4649f;
            return c0264n0 != null ? c0264n0.g() : androidx.core.graphics.b.f4471e;
        }

        private androidx.core.graphics.b x(View view) {
            if (Build.VERSION.SDK_INT >= 30) {
                throw new UnsupportedOperationException("getVisibleInsets() should not be called on API >= 30. Use WindowInsets.isVisible() instead.");
            }
            if (!f4641h) {
                z();
            }
            Method method = f4642i;
            if (method != null && f4643j != null && f4644k != null) {
                try {
                    Object objInvoke = method.invoke(view, new Object[0]);
                    if (objInvoke == null) {
                        Log.w("WindowInsetsCompat", "Failed to get visible insets. getViewRootImpl() returned null from the provided view. This means that the view is either not attached or the method has been overridden", new NullPointerException());
                        return null;
                    }
                    Rect rect = (Rect) f4644k.get(f4645l.get(objInvoke));
                    if (rect != null) {
                        return androidx.core.graphics.b.c(rect);
                    }
                    return null;
                } catch (ReflectiveOperationException e4) {
                    Log.e("WindowInsetsCompat", "Failed to get visible insets. (Reflection error). " + e4.getMessage(), e4);
                }
            }
            return null;
        }

        private static void z() {
            try {
                f4642i = View.class.getDeclaredMethod("getViewRootImpl", new Class[0]);
                Class<?> cls = Class.forName("android.view.View$AttachInfo");
                f4643j = cls;
                f4644k = cls.getDeclaredField("mVisibleInsets");
                f4645l = Class.forName("android.view.ViewRootImpl").getDeclaredField("mAttachInfo");
                f4644k.setAccessible(true);
                f4645l.setAccessible(true);
            } catch (ReflectiveOperationException e4) {
                Log.e("WindowInsetsCompat", "Failed to get visible insets. (Reflection error). " + e4.getMessage(), e4);
            }
            f4641h = true;
        }

        @Override // androidx.core.view.C0264n0.l
        void d(View view) {
            androidx.core.graphics.b bVarX = x(view);
            if (bVarX == null) {
                bVarX = androidx.core.graphics.b.f4471e;
            }
            r(bVarX);
        }

        @Override // androidx.core.view.C0264n0.l
        void e(C0264n0 c0264n0) {
            c0264n0.s(this.f4649f);
            c0264n0.r(this.f4650g);
        }

        @Override // androidx.core.view.C0264n0.l
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                return Objects.equals(this.f4650g, ((g) obj).f4650g);
            }
            return false;
        }

        @Override // androidx.core.view.C0264n0.l
        public androidx.core.graphics.b g(int i3) {
            return u(i3, false);
        }

        @Override // androidx.core.view.C0264n0.l
        final androidx.core.graphics.b k() {
            if (this.f4648e == null) {
                this.f4648e = androidx.core.graphics.b.b(this.f4646c.getSystemWindowInsetLeft(), this.f4646c.getSystemWindowInsetTop(), this.f4646c.getSystemWindowInsetRight(), this.f4646c.getSystemWindowInsetBottom());
            }
            return this.f4648e;
        }

        @Override // androidx.core.view.C0264n0.l
        C0264n0 m(int i3, int i4, int i5, int i6) {
            b bVar = new b(C0264n0.v(this.f4646c));
            bVar.c(C0264n0.m(k(), i3, i4, i5, i6));
            bVar.b(C0264n0.m(i(), i3, i4, i5, i6));
            return bVar.a();
        }

        @Override // androidx.core.view.C0264n0.l
        boolean o() {
            return this.f4646c.isRound();
        }

        @Override // androidx.core.view.C0264n0.l
        boolean p(int i3) {
            for (int i4 = 1; i4 <= 256; i4 <<= 1) {
                if ((i3 & i4) != 0 && !y(i4)) {
                    return false;
                }
            }
            return true;
        }

        @Override // androidx.core.view.C0264n0.l
        public void q(androidx.core.graphics.b[] bVarArr) {
            this.f4647d = bVarArr;
        }

        @Override // androidx.core.view.C0264n0.l
        void r(androidx.core.graphics.b bVar) {
            this.f4650g = bVar;
        }

        @Override // androidx.core.view.C0264n0.l
        void s(C0264n0 c0264n0) {
            this.f4649f = c0264n0;
        }

        protected androidx.core.graphics.b v(int i3, boolean z3) {
            androidx.core.graphics.b bVarG;
            int i4;
            if (i3 == 1) {
                return z3 ? androidx.core.graphics.b.b(0, Math.max(w().f4473b, k().f4473b), 0, 0) : androidx.core.graphics.b.b(0, k().f4473b, 0, 0);
            }
            if (i3 == 2) {
                if (z3) {
                    androidx.core.graphics.b bVarW = w();
                    androidx.core.graphics.b bVarI = i();
                    return androidx.core.graphics.b.b(Math.max(bVarW.f4472a, bVarI.f4472a), 0, Math.max(bVarW.f4474c, bVarI.f4474c), Math.max(bVarW.f4475d, bVarI.f4475d));
                }
                androidx.core.graphics.b bVarK = k();
                C0264n0 c0264n0 = this.f4649f;
                bVarG = c0264n0 != null ? c0264n0.g() : null;
                int iMin = bVarK.f4475d;
                if (bVarG != null) {
                    iMin = Math.min(iMin, bVarG.f4475d);
                }
                return androidx.core.graphics.b.b(bVarK.f4472a, 0, bVarK.f4474c, iMin);
            }
            if (i3 != 8) {
                if (i3 == 16) {
                    return j();
                }
                if (i3 == 32) {
                    return h();
                }
                if (i3 == 64) {
                    return l();
                }
                if (i3 != 128) {
                    return androidx.core.graphics.b.f4471e;
                }
                C0264n0 c0264n02 = this.f4649f;
                C0274v c0274vE = c0264n02 != null ? c0264n02.e() : f();
                return c0274vE != null ? androidx.core.graphics.b.b(c0274vE.c(), c0274vE.e(), c0274vE.d(), c0274vE.b()) : androidx.core.graphics.b.f4471e;
            }
            androidx.core.graphics.b[] bVarArr = this.f4647d;
            bVarG = bVarArr != null ? bVarArr[m.b(8)] : null;
            if (bVarG != null) {
                return bVarG;
            }
            androidx.core.graphics.b bVarK2 = k();
            androidx.core.graphics.b bVarW2 = w();
            int i5 = bVarK2.f4475d;
            if (i5 > bVarW2.f4475d) {
                return androidx.core.graphics.b.b(0, 0, 0, i5);
            }
            androidx.core.graphics.b bVar = this.f4650g;
            return (bVar == null || bVar.equals(androidx.core.graphics.b.f4471e) || (i4 = this.f4650g.f4475d) <= bVarW2.f4475d) ? androidx.core.graphics.b.f4471e : androidx.core.graphics.b.b(0, 0, 0, i4);
        }

        protected boolean y(int i3) {
            if (i3 != 1 && i3 != 2) {
                if (i3 == 4) {
                    return false;
                }
                if (i3 != 8 && i3 != 128) {
                    return true;
                }
            }
            return !v(i3, false).equals(androidx.core.graphics.b.f4471e);
        }

        g(C0264n0 c0264n0, g gVar) {
            this(c0264n0, new WindowInsets(gVar.f4646c));
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$j */
    private static class j extends i {

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        private androidx.core.graphics.b f4652n;

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        private androidx.core.graphics.b f4653o;

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        private androidx.core.graphics.b f4654p;

        j(C0264n0 c0264n0, WindowInsets windowInsets) {
            super(c0264n0, windowInsets);
            this.f4652n = null;
            this.f4653o = null;
            this.f4654p = null;
        }

        @Override // androidx.core.view.C0264n0.l
        androidx.core.graphics.b h() {
            if (this.f4653o == null) {
                this.f4653o = androidx.core.graphics.b.d(this.f4646c.getMandatorySystemGestureInsets());
            }
            return this.f4653o;
        }

        @Override // androidx.core.view.C0264n0.l
        androidx.core.graphics.b j() {
            if (this.f4652n == null) {
                this.f4652n = androidx.core.graphics.b.d(this.f4646c.getSystemGestureInsets());
            }
            return this.f4652n;
        }

        @Override // androidx.core.view.C0264n0.l
        androidx.core.graphics.b l() {
            if (this.f4654p == null) {
                this.f4654p = androidx.core.graphics.b.d(this.f4646c.getTappableElementInsets());
            }
            return this.f4654p;
        }

        @Override // androidx.core.view.C0264n0.g, androidx.core.view.C0264n0.l
        C0264n0 m(int i3, int i4, int i5, int i6) {
            return C0264n0.v(this.f4646c.inset(i3, i4, i5, i6));
        }

        @Override // androidx.core.view.C0264n0.h, androidx.core.view.C0264n0.l
        public void t(androidx.core.graphics.b bVar) {
        }

        j(C0264n0 c0264n0, j jVar) {
            super(c0264n0, jVar);
            this.f4652n = null;
            this.f4653o = null;
            this.f4654p = null;
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.n0$b */
    public static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final f f4631a;

        public b() {
            int i3 = Build.VERSION.SDK_INT;
            if (i3 >= 30) {
                this.f4631a = new e();
            } else if (i3 >= 29) {
                this.f4631a = new d();
            } else {
                this.f4631a = new c();
            }
        }

        public C0264n0 a() {
            return this.f4631a.b();
        }

        public b b(androidx.core.graphics.b bVar) {
            this.f4631a.d(bVar);
            return this;
        }

        public b c(androidx.core.graphics.b bVar) {
            this.f4631a.f(bVar);
            return this;
        }

        public b(C0264n0 c0264n0) {
            int i3 = Build.VERSION.SDK_INT;
            if (i3 >= 30) {
                this.f4631a = new e(c0264n0);
            } else if (i3 >= 29) {
                this.f4631a = new d(c0264n0);
            } else {
                this.f4631a = new c(c0264n0);
            }
        }
    }

    public C0264n0(C0264n0 c0264n0) {
        if (c0264n0 != null) {
            l lVar = c0264n0.f4626a;
            int i3 = Build.VERSION.SDK_INT;
            if (i3 >= 30 && (lVar instanceof k)) {
                this.f4626a = new k(this, (k) lVar);
            } else if (i3 >= 29 && (lVar instanceof j)) {
                this.f4626a = new j(this, (j) lVar);
            } else if (i3 >= 28 && (lVar instanceof i)) {
                this.f4626a = new i(this, (i) lVar);
            } else if (lVar instanceof h) {
                this.f4626a = new h(this, (h) lVar);
            } else if (lVar instanceof g) {
                this.f4626a = new g(this, (g) lVar);
            } else {
                this.f4626a = new l(this);
            }
            lVar.e(this);
            return;
        }
        this.f4626a = new l(this);
    }
}
