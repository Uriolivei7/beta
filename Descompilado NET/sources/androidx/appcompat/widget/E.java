package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
class E {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final RectF f3784l = new RectF();

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static ConcurrentHashMap f3785m = new ConcurrentHashMap();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f3786a = 0;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f3787b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private float f3788c = -1.0f;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private float f3789d = -1.0f;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private float f3790e = -1.0f;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int[] f3791f = new int[0];

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f3792g = false;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private TextPaint f3793h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final TextView f3794i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final Context f3795j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final d f3796k;

    private static final class a {
        static StaticLayout a(CharSequence charSequence, Layout.Alignment alignment, int i3, int i4, TextView textView, TextPaint textPaint, d dVar) {
            StaticLayout.Builder builderObtain = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint, i3);
            StaticLayout.Builder hyphenationFrequency = builderObtain.setAlignment(alignment).setLineSpacing(textView.getLineSpacingExtra(), textView.getLineSpacingMultiplier()).setIncludePad(textView.getIncludeFontPadding()).setBreakStrategy(textView.getBreakStrategy()).setHyphenationFrequency(textView.getHyphenationFrequency());
            if (i4 == -1) {
                i4 = Integer.MAX_VALUE;
            }
            hyphenationFrequency.setMaxLines(i4);
            try {
                dVar.a(builderObtain, textView);
            } catch (ClassCastException unused) {
                Log.w("ACTVAutoSizeHelper", "Failed to obtain TextDirectionHeuristic, auto size may be incorrect");
            }
            return builderObtain.build();
        }
    }

    private static class b extends d {
        b() {
        }

        @Override // androidx.appcompat.widget.E.d
        void a(StaticLayout.Builder builder, TextView textView) {
            builder.setTextDirection((TextDirectionHeuristic) E.m(textView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR));
        }
    }

    private static class c extends b {
        c() {
        }

        @Override // androidx.appcompat.widget.E.b, androidx.appcompat.widget.E.d
        void a(StaticLayout.Builder builder, TextView textView) {
            builder.setTextDirection(textView.getTextDirectionHeuristic());
        }

        @Override // androidx.appcompat.widget.E.d
        boolean b(TextView textView) {
            return textView.isHorizontallyScrollable();
        }
    }

    private static class d {
        d() {
        }

        abstract void a(StaticLayout.Builder builder, TextView textView);

        boolean b(TextView textView) {
            return ((Boolean) E.m(textView, "getHorizontallyScrolling", Boolean.FALSE)).booleanValue();
        }
    }

    E(TextView textView) {
        this.f3794i = textView;
        this.f3795j = textView.getContext();
        if (Build.VERSION.SDK_INT >= 29) {
            this.f3796k = new c();
        } else {
            this.f3796k = new b();
        }
    }

    private int[] b(int[] iArr) {
        int length = iArr.length;
        if (length == 0) {
            return iArr;
        }
        Arrays.sort(iArr);
        ArrayList arrayList = new ArrayList();
        for (int i3 : iArr) {
            if (i3 > 0 && Collections.binarySearch(arrayList, Integer.valueOf(i3)) < 0) {
                arrayList.add(Integer.valueOf(i3));
            }
        }
        if (length == arrayList.size()) {
            return iArr;
        }
        int size = arrayList.size();
        int[] iArr2 = new int[size];
        for (int i4 = 0; i4 < size; i4++) {
            iArr2[i4] = ((Integer) arrayList.get(i4)).intValue();
        }
        return iArr2;
    }

    private void c() {
        this.f3786a = 0;
        this.f3789d = -1.0f;
        this.f3790e = -1.0f;
        this.f3788c = -1.0f;
        this.f3791f = new int[0];
        this.f3787b = false;
    }

    private int e(RectF rectF) {
        int length = this.f3791f.length;
        if (length == 0) {
            throw new IllegalStateException("No available text sizes to choose from.");
        }
        int i3 = 1;
        int i4 = length - 1;
        int i5 = 0;
        while (i3 <= i4) {
            int i6 = (i3 + i4) / 2;
            if (x(this.f3791f[i6], rectF)) {
                int i7 = i6 + 1;
                i5 = i3;
                i3 = i7;
            } else {
                i5 = i6 - 1;
                i4 = i5;
            }
        }
        return this.f3791f[i5];
    }

    private static Method k(String str) {
        try {
            Method declaredMethod = (Method) f3785m.get(str);
            if (declaredMethod == null && (declaredMethod = TextView.class.getDeclaredMethod(str, new Class[0])) != null) {
                declaredMethod.setAccessible(true);
                f3785m.put(str, declaredMethod);
            }
            return declaredMethod;
        } catch (Exception e4) {
            Log.w("ACTVAutoSizeHelper", "Failed to retrieve TextView#" + str + "() method", e4);
            return null;
        }
    }

    static Object m(Object obj, String str, Object obj2) {
        try {
            return k(str).invoke(obj, new Object[0]);
        } catch (Exception e4) {
            Log.w("ACTVAutoSizeHelper", "Failed to invoke TextView#" + str + "() method", e4);
            return obj2;
        }
    }

    private void s(float f3) {
        if (f3 != this.f3794i.getPaint().getTextSize()) {
            this.f3794i.getPaint().setTextSize(f3);
            boolean zIsInLayout = this.f3794i.isInLayout();
            if (this.f3794i.getLayout() != null) {
                this.f3787b = false;
                try {
                    Method methodK = k("nullLayouts");
                    if (methodK != null) {
                        methodK.invoke(this.f3794i, new Object[0]);
                    }
                } catch (Exception e4) {
                    Log.w("ACTVAutoSizeHelper", "Failed to invoke TextView#nullLayouts() method", e4);
                }
                if (zIsInLayout) {
                    this.f3794i.forceLayout();
                } else {
                    this.f3794i.requestLayout();
                }
                this.f3794i.invalidate();
            }
        }
    }

    private boolean u() {
        if (y() && this.f3786a == 1) {
            if (!this.f3792g || this.f3791f.length == 0) {
                int iFloor = ((int) Math.floor((this.f3790e - this.f3789d) / this.f3788c)) + 1;
                int[] iArr = new int[iFloor];
                for (int i3 = 0; i3 < iFloor; i3++) {
                    iArr[i3] = Math.round(this.f3789d + (i3 * this.f3788c));
                }
                this.f3791f = b(iArr);
            }
            this.f3787b = true;
        } else {
            this.f3787b = false;
        }
        return this.f3787b;
    }

    private void v(TypedArray typedArray) {
        int length = typedArray.length();
        int[] iArr = new int[length];
        if (length > 0) {
            for (int i3 = 0; i3 < length; i3++) {
                iArr[i3] = typedArray.getDimensionPixelSize(i3, -1);
            }
            this.f3791f = b(iArr);
            w();
        }
    }

    private boolean w() {
        boolean z3 = this.f3791f.length > 0;
        this.f3792g = z3;
        if (z3) {
            this.f3786a = 1;
            this.f3789d = r0[0];
            this.f3790e = r0[r1 - 1];
            this.f3788c = -1.0f;
        }
        return z3;
    }

    private boolean x(int i3, RectF rectF) {
        CharSequence transformation;
        CharSequence text = this.f3794i.getText();
        TransformationMethod transformationMethod = this.f3794i.getTransformationMethod();
        if (transformationMethod != null && (transformation = transformationMethod.getTransformation(text, this.f3794i)) != null) {
            text = transformation;
        }
        int maxLines = this.f3794i.getMaxLines();
        l(i3);
        StaticLayout staticLayoutD = d(text, (Layout.Alignment) m(this.f3794i, "getLayoutAlignment", Layout.Alignment.ALIGN_NORMAL), Math.round(rectF.right), maxLines);
        return (maxLines == -1 || (staticLayoutD.getLineCount() <= maxLines && staticLayoutD.getLineEnd(staticLayoutD.getLineCount() - 1) == text.length())) && ((float) staticLayoutD.getHeight()) <= rectF.bottom;
    }

    private boolean y() {
        return !(this.f3794i instanceof C0223l);
    }

    private void z(float f3, float f4, float f5) {
        if (f3 <= 0.0f) {
            throw new IllegalArgumentException("Minimum auto-size text size (" + f3 + "px) is less or equal to (0px)");
        }
        if (f4 <= f3) {
            throw new IllegalArgumentException("Maximum auto-size text size (" + f4 + "px) is less or equal to minimum auto-size text size (" + f3 + "px)");
        }
        if (f5 <= 0.0f) {
            throw new IllegalArgumentException("The auto-size step granularity (" + f5 + "px) is less or equal to (0px)");
        }
        this.f3786a = 1;
        this.f3789d = f3;
        this.f3790e = f4;
        this.f3788c = f5;
        this.f3792g = false;
    }

    void a() {
        if (n()) {
            if (this.f3787b) {
                if (this.f3794i.getMeasuredHeight() <= 0 || this.f3794i.getMeasuredWidth() <= 0) {
                    return;
                }
                int measuredWidth = this.f3796k.b(this.f3794i) ? 1048576 : (this.f3794i.getMeasuredWidth() - this.f3794i.getTotalPaddingLeft()) - this.f3794i.getTotalPaddingRight();
                int height = (this.f3794i.getHeight() - this.f3794i.getCompoundPaddingBottom()) - this.f3794i.getCompoundPaddingTop();
                if (measuredWidth <= 0 || height <= 0) {
                    return;
                }
                RectF rectF = f3784l;
                synchronized (rectF) {
                    try {
                        rectF.setEmpty();
                        rectF.right = measuredWidth;
                        rectF.bottom = height;
                        float fE = e(rectF);
                        if (fE != this.f3794i.getTextSize()) {
                            t(0, fE);
                        }
                    } finally {
                    }
                }
            }
            this.f3787b = true;
        }
    }

    StaticLayout d(CharSequence charSequence, Layout.Alignment alignment, int i3, int i4) {
        return a.a(charSequence, alignment, i3, i4, this.f3794i, this.f3793h, this.f3796k);
    }

    int f() {
        return Math.round(this.f3790e);
    }

    int g() {
        return Math.round(this.f3789d);
    }

    int h() {
        return Math.round(this.f3788c);
    }

    int[] i() {
        return this.f3791f;
    }

    int j() {
        return this.f3786a;
    }

    void l(int i3) {
        TextPaint textPaint = this.f3793h;
        if (textPaint == null) {
            this.f3793h = new TextPaint();
        } else {
            textPaint.reset();
        }
        this.f3793h.set(this.f3794i.getPaint());
        this.f3793h.setTextSize(i3);
    }

    boolean n() {
        return y() && this.f3786a != 0;
    }

    void o(AttributeSet attributeSet, int i3) {
        int resourceId;
        TypedArray typedArrayObtainStyledAttributes = this.f3795j.obtainStyledAttributes(attributeSet, d.j.f8982g0, i3, 0);
        TextView textView = this.f3794i;
        androidx.core.view.Z.V(textView, textView.getContext(), d.j.f8982g0, attributeSet, typedArrayObtainStyledAttributes, i3, 0);
        if (typedArrayObtainStyledAttributes.hasValue(d.j.f9002l0)) {
            this.f3786a = typedArrayObtainStyledAttributes.getInt(d.j.f9002l0, 0);
        }
        float dimension = typedArrayObtainStyledAttributes.hasValue(d.j.f8998k0) ? typedArrayObtainStyledAttributes.getDimension(d.j.f8998k0, -1.0f) : -1.0f;
        float dimension2 = typedArrayObtainStyledAttributes.hasValue(d.j.f8990i0) ? typedArrayObtainStyledAttributes.getDimension(d.j.f8990i0, -1.0f) : -1.0f;
        float dimension3 = typedArrayObtainStyledAttributes.hasValue(d.j.f8986h0) ? typedArrayObtainStyledAttributes.getDimension(d.j.f8986h0, -1.0f) : -1.0f;
        if (typedArrayObtainStyledAttributes.hasValue(d.j.f8994j0) && (resourceId = typedArrayObtainStyledAttributes.getResourceId(d.j.f8994j0, 0)) > 0) {
            TypedArray typedArrayObtainTypedArray = typedArrayObtainStyledAttributes.getResources().obtainTypedArray(resourceId);
            v(typedArrayObtainTypedArray);
            typedArrayObtainTypedArray.recycle();
        }
        typedArrayObtainStyledAttributes.recycle();
        if (!y()) {
            this.f3786a = 0;
            return;
        }
        if (this.f3786a == 1) {
            if (!this.f3792g) {
                DisplayMetrics displayMetrics = this.f3795j.getResources().getDisplayMetrics();
                if (dimension2 == -1.0f) {
                    dimension2 = TypedValue.applyDimension(2, 12.0f, displayMetrics);
                }
                if (dimension3 == -1.0f) {
                    dimension3 = TypedValue.applyDimension(2, 112.0f, displayMetrics);
                }
                if (dimension == -1.0f) {
                    dimension = 1.0f;
                }
                z(dimension2, dimension3, dimension);
            }
            u();
        }
    }

    void p(int i3, int i4, int i5, int i6) {
        if (y()) {
            DisplayMetrics displayMetrics = this.f3795j.getResources().getDisplayMetrics();
            z(TypedValue.applyDimension(i6, i3, displayMetrics), TypedValue.applyDimension(i6, i4, displayMetrics), TypedValue.applyDimension(i6, i5, displayMetrics));
            if (u()) {
                a();
            }
        }
    }

    void q(int[] iArr, int i3) {
        if (y()) {
            int length = iArr.length;
            if (length > 0) {
                int[] iArrCopyOf = new int[length];
                if (i3 == 0) {
                    iArrCopyOf = Arrays.copyOf(iArr, length);
                } else {
                    DisplayMetrics displayMetrics = this.f3795j.getResources().getDisplayMetrics();
                    for (int i4 = 0; i4 < length; i4++) {
                        iArrCopyOf[i4] = Math.round(TypedValue.applyDimension(i3, iArr[i4], displayMetrics));
                    }
                }
                this.f3791f = b(iArrCopyOf);
                if (!w()) {
                    throw new IllegalArgumentException("None of the preset sizes is valid: " + Arrays.toString(iArr));
                }
            } else {
                this.f3792g = false;
            }
            if (u()) {
                a();
            }
        }
    }

    void r(int i3) {
        if (y()) {
            if (i3 == 0) {
                c();
                return;
            }
            if (i3 != 1) {
                throw new IllegalArgumentException("Unknown auto-size text type: " + i3);
            }
            DisplayMetrics displayMetrics = this.f3795j.getResources().getDisplayMetrics();
            z(TypedValue.applyDimension(2, 12.0f, displayMetrics), TypedValue.applyDimension(2, 112.0f, displayMetrics), 1.0f);
            if (u()) {
                a();
            }
        }
    }

    void t(int i3, float f3) {
        Context context = this.f3795j;
        s(TypedValue.applyDimension(i3, f3, (context == null ? Resources.getSystem() : context.getResources()).getDisplayMetrics()));
    }
}
