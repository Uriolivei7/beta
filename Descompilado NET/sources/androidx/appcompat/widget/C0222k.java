package androidx.appcompat.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import androidx.appcompat.widget.X;
import d.AbstractC0487a;
import e.AbstractC0521a;

/* JADX INFO: renamed from: androidx.appcompat.widget.k, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0222k {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final PorterDuff.Mode f4244b = PorterDuff.Mode.SRC_IN;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static C0222k f4245c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private X f4246a;

    /* JADX INFO: renamed from: androidx.appcompat.widget.k$a */
    class a implements X.c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int[] f4247a = {d.e.f8740R, d.e.f8738P, d.e.f8742a};

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int[] f4248b = {d.e.f8756o, d.e.f8724B, d.e.f8761t, d.e.f8757p, d.e.f8758q, d.e.f8760s, d.e.f8759r};

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int[] f4249c = {d.e.f8737O, d.e.f8739Q, d.e.f8752k, d.e.f8733K, d.e.f8734L, d.e.f8735M, d.e.f8736N};

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final int[] f4250d = {d.e.f8764w, d.e.f8750i, d.e.f8763v};

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final int[] f4251e = {d.e.f8732J, d.e.f8741S};

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final int[] f4252f = {d.e.f8744c, d.e.f8748g, d.e.f8745d, d.e.f8749h};

        a() {
        }

        private boolean f(int[] iArr, int i3) {
            for (int i4 : iArr) {
                if (i4 == i3) {
                    return true;
                }
            }
            return false;
        }

        private ColorStateList g(Context context) {
            return h(context, 0);
        }

        private ColorStateList h(Context context, int i3) {
            int iC = d0.c(context, AbstractC0487a.f8696x);
            return new ColorStateList(new int[][]{d0.f4192b, d0.f4195e, d0.f4193c, d0.f4199i}, new int[]{d0.b(context, AbstractC0487a.f8694v), androidx.core.graphics.a.d(iC, i3), androidx.core.graphics.a.d(iC, i3), i3});
        }

        private ColorStateList i(Context context) {
            return h(context, d0.c(context, AbstractC0487a.f8693u));
        }

        private ColorStateList j(Context context) {
            return h(context, d0.c(context, AbstractC0487a.f8694v));
        }

        private ColorStateList k(Context context) {
            int[][] iArr = new int[3][];
            int[] iArr2 = new int[3];
            ColorStateList colorStateListE = d0.e(context, AbstractC0487a.f8698z);
            if (colorStateListE == null || !colorStateListE.isStateful()) {
                iArr[0] = d0.f4192b;
                iArr2[0] = d0.b(context, AbstractC0487a.f8698z);
                iArr[1] = d0.f4196f;
                iArr2[1] = d0.c(context, AbstractC0487a.f8695w);
                iArr[2] = d0.f4199i;
                iArr2[2] = d0.c(context, AbstractC0487a.f8698z);
            } else {
                int[] iArr3 = d0.f4192b;
                iArr[0] = iArr3;
                iArr2[0] = colorStateListE.getColorForState(iArr3, 0);
                iArr[1] = d0.f4196f;
                iArr2[1] = d0.c(context, AbstractC0487a.f8695w);
                iArr[2] = d0.f4199i;
                iArr2[2] = colorStateListE.getDefaultColor();
            }
            return new ColorStateList(iArr, iArr2);
        }

        private LayerDrawable l(X x3, Context context, int i3) {
            BitmapDrawable bitmapDrawable;
            BitmapDrawable bitmapDrawable2;
            BitmapDrawable bitmapDrawable3;
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(i3);
            Drawable drawableI = x3.i(context, d.e.f8728F);
            Drawable drawableI2 = x3.i(context, d.e.f8729G);
            if ((drawableI instanceof BitmapDrawable) && drawableI.getIntrinsicWidth() == dimensionPixelSize && drawableI.getIntrinsicHeight() == dimensionPixelSize) {
                bitmapDrawable = (BitmapDrawable) drawableI;
                bitmapDrawable2 = new BitmapDrawable(bitmapDrawable.getBitmap());
            } else {
                Bitmap bitmapCreateBitmap = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmapCreateBitmap);
                drawableI.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                drawableI.draw(canvas);
                bitmapDrawable = new BitmapDrawable(bitmapCreateBitmap);
                bitmapDrawable2 = new BitmapDrawable(bitmapCreateBitmap);
            }
            bitmapDrawable2.setTileModeX(Shader.TileMode.REPEAT);
            if ((drawableI2 instanceof BitmapDrawable) && drawableI2.getIntrinsicWidth() == dimensionPixelSize && drawableI2.getIntrinsicHeight() == dimensionPixelSize) {
                bitmapDrawable3 = (BitmapDrawable) drawableI2;
            } else {
                Bitmap bitmapCreateBitmap2 = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(bitmapCreateBitmap2);
                drawableI2.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                drawableI2.draw(canvas2);
                bitmapDrawable3 = new BitmapDrawable(bitmapCreateBitmap2);
            }
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{bitmapDrawable, bitmapDrawable3, bitmapDrawable2});
            layerDrawable.setId(0, R.id.background);
            layerDrawable.setId(1, R.id.secondaryProgress);
            layerDrawable.setId(2, R.id.progress);
            return layerDrawable;
        }

        private void m(Drawable drawable, int i3, PorterDuff.Mode mode) {
            Drawable drawableMutate = drawable.mutate();
            if (mode == null) {
                mode = C0222k.f4244b;
            }
            drawableMutate.setColorFilter(C0222k.e(i3, mode));
        }

        /* JADX WARN: Removed duplicated region for block: B:22:0x0051  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0066 A[RETURN] */
        @Override // androidx.appcompat.widget.X.c
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean a(android.content.Context r8, int r9, android.graphics.drawable.Drawable r10) {
            /*
                r7 = this;
                android.graphics.PorterDuff$Mode r0 = androidx.appcompat.widget.C0222k.a()
                int[] r1 = r7.f4247a
                boolean r1 = r7.f(r1, r9)
                r2 = 1
                r3 = 0
                r4 = -1
                if (r1 == 0) goto L15
                int r9 = d.AbstractC0487a.f8697y
            L11:
                r1 = r0
                r5 = r2
            L13:
                r0 = r4
                goto L4f
            L15:
                int[] r1 = r7.f4249c
                boolean r1 = r7.f(r1, r9)
                if (r1 == 0) goto L20
                int r9 = d.AbstractC0487a.f8695w
                goto L11
            L20:
                int[] r1 = r7.f4250d
                boolean r1 = r7.f(r1, r9)
                r5 = 16842801(0x1010031, float:2.3693695E-38)
                if (r1 == 0) goto L32
                android.graphics.PorterDuff$Mode r0 = android.graphics.PorterDuff.Mode.MULTIPLY
            L2d:
                r1 = r0
                r0 = r4
                r9 = r5
                r5 = r2
                goto L4f
            L32:
                int r1 = d.e.f8762u
                if (r9 != r1) goto L46
                r9 = 1109603123(0x42233333, float:40.8)
                int r9 = java.lang.Math.round(r9)
                r1 = 16842800(0x1010030, float:2.3693693E-38)
                r5 = r2
                r6 = r0
                r0 = r9
                r9 = r1
                r1 = r6
                goto L4f
            L46:
                int r1 = d.e.f8753l
                if (r9 != r1) goto L4b
                goto L2d
            L4b:
                r1 = r0
                r9 = r3
                r5 = r9
                goto L13
            L4f:
                if (r5 == 0) goto L66
                android.graphics.drawable.Drawable r10 = r10.mutate()
                int r8 = androidx.appcompat.widget.d0.c(r8, r9)
                android.graphics.PorterDuffColorFilter r8 = androidx.appcompat.widget.C0222k.e(r8, r1)
                r10.setColorFilter(r8)
                if (r0 == r4) goto L65
                r10.setAlpha(r0)
            L65:
                return r2
            L66:
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.C0222k.a.a(android.content.Context, int, android.graphics.drawable.Drawable):boolean");
        }

        @Override // androidx.appcompat.widget.X.c
        public PorterDuff.Mode b(int i3) {
            if (i3 == d.e.f8730H) {
                return PorterDuff.Mode.MULTIPLY;
            }
            return null;
        }

        @Override // androidx.appcompat.widget.X.c
        public Drawable c(X x3, Context context, int i3) {
            if (i3 == d.e.f8751j) {
                return new LayerDrawable(new Drawable[]{x3.i(context, d.e.f8750i), x3.i(context, d.e.f8752k)});
            }
            if (i3 == d.e.f8766y) {
                return l(x3, context, d.d.f8716i);
            }
            if (i3 == d.e.f8765x) {
                return l(x3, context, d.d.f8717j);
            }
            if (i3 == d.e.f8767z) {
                return l(x3, context, d.d.f8718k);
            }
            return null;
        }

        @Override // androidx.appcompat.widget.X.c
        public ColorStateList d(Context context, int i3) {
            if (i3 == d.e.f8754m) {
                return AbstractC0521a.a(context, d.c.f8704e);
            }
            if (i3 == d.e.f8731I) {
                return AbstractC0521a.a(context, d.c.f8707h);
            }
            if (i3 == d.e.f8730H) {
                return k(context);
            }
            if (i3 == d.e.f8747f) {
                return j(context);
            }
            if (i3 == d.e.f8743b) {
                return g(context);
            }
            if (i3 == d.e.f8746e) {
                return i(context);
            }
            if (i3 == d.e.f8726D || i3 == d.e.f8727E) {
                return AbstractC0521a.a(context, d.c.f8706g);
            }
            if (f(this.f4248b, i3)) {
                return d0.e(context, AbstractC0487a.f8697y);
            }
            if (f(this.f4251e, i3)) {
                return AbstractC0521a.a(context, d.c.f8703d);
            }
            if (f(this.f4252f, i3)) {
                return AbstractC0521a.a(context, d.c.f8702c);
            }
            if (i3 == d.e.f8723A) {
                return AbstractC0521a.a(context, d.c.f8705f);
            }
            return null;
        }

        @Override // androidx.appcompat.widget.X.c
        public boolean e(Context context, int i3, Drawable drawable) {
            if (i3 == d.e.f8725C) {
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                m(layerDrawable.findDrawableByLayerId(R.id.background), d0.c(context, AbstractC0487a.f8697y), C0222k.f4244b);
                m(layerDrawable.findDrawableByLayerId(R.id.secondaryProgress), d0.c(context, AbstractC0487a.f8697y), C0222k.f4244b);
                m(layerDrawable.findDrawableByLayerId(R.id.progress), d0.c(context, AbstractC0487a.f8695w), C0222k.f4244b);
                return true;
            }
            if (i3 != d.e.f8766y && i3 != d.e.f8765x && i3 != d.e.f8767z) {
                return false;
            }
            LayerDrawable layerDrawable2 = (LayerDrawable) drawable;
            m(layerDrawable2.findDrawableByLayerId(R.id.background), d0.b(context, AbstractC0487a.f8697y), C0222k.f4244b);
            m(layerDrawable2.findDrawableByLayerId(R.id.secondaryProgress), d0.c(context, AbstractC0487a.f8695w), C0222k.f4244b);
            m(layerDrawable2.findDrawableByLayerId(R.id.progress), d0.c(context, AbstractC0487a.f8695w), C0222k.f4244b);
            return true;
        }
    }

    public static synchronized C0222k b() {
        try {
            if (f4245c == null) {
                h();
            }
        } catch (Throwable th) {
            throw th;
        }
        return f4245c;
    }

    public static synchronized PorterDuffColorFilter e(int i3, PorterDuff.Mode mode) {
        return X.k(i3, mode);
    }

    public static synchronized void h() {
        if (f4245c == null) {
            C0222k c0222k = new C0222k();
            f4245c = c0222k;
            c0222k.f4246a = X.g();
            f4245c.f4246a.t(new a());
        }
    }

    static void i(Drawable drawable, f0 f0Var, int[] iArr) {
        X.v(drawable, f0Var, iArr);
    }

    public synchronized Drawable c(Context context, int i3) {
        return this.f4246a.i(context, i3);
    }

    synchronized Drawable d(Context context, int i3, boolean z3) {
        return this.f4246a.j(context, i3, z3);
    }

    synchronized ColorStateList f(Context context, int i3) {
        return this.f4246a.l(context, i3);
    }

    public synchronized void g(Context context) {
        this.f4246a.r(context);
    }
}
