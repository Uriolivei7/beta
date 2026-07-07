package androidx.appcompat.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextClassifier;
import android.widget.TextView;
import androidx.core.text.l;
import e.AbstractC0521a;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes.dex */
public class D extends TextView {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0216e f3774b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C f3775c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final B f3776d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private C0225n f3777e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f3778f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private a f3779g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private Future f3780h;

    private interface a {
        void a(int[] iArr, int i3);

        void b(TextClassifier textClassifier);

        int[] c();

        void d(int i3);

        TextClassifier e();

        int f();

        void g(int i3, int i4, int i5, int i6);

        int h();

        int i();

        void j(int i3);

        int k();

        void l(int i3);

        void m(int i3, float f3);
    }

    class b implements a {
        b() {
        }

        @Override // androidx.appcompat.widget.D.a
        public void a(int[] iArr, int i3) {
            D.super.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i3);
        }

        @Override // androidx.appcompat.widget.D.a
        public void b(TextClassifier textClassifier) {
            D.super.setTextClassifier(textClassifier);
        }

        @Override // androidx.appcompat.widget.D.a
        public int[] c() {
            return D.super.getAutoSizeTextAvailableSizes();
        }

        @Override // androidx.appcompat.widget.D.a
        public void d(int i3) {
        }

        @Override // androidx.appcompat.widget.D.a
        public TextClassifier e() {
            return D.super.getTextClassifier();
        }

        @Override // androidx.appcompat.widget.D.a
        public int f() {
            return D.super.getAutoSizeMaxTextSize();
        }

        @Override // androidx.appcompat.widget.D.a
        public void g(int i3, int i4, int i5, int i6) {
            D.super.setAutoSizeTextTypeUniformWithConfiguration(i3, i4, i5, i6);
        }

        @Override // androidx.appcompat.widget.D.a
        public int h() {
            return D.super.getAutoSizeTextType();
        }

        @Override // androidx.appcompat.widget.D.a
        public int i() {
            return D.super.getAutoSizeMinTextSize();
        }

        @Override // androidx.appcompat.widget.D.a
        public void j(int i3) {
        }

        @Override // androidx.appcompat.widget.D.a
        public int k() {
            return D.super.getAutoSizeStepGranularity();
        }

        @Override // androidx.appcompat.widget.D.a
        public void l(int i3) {
            D.super.setAutoSizeTextTypeWithDefaults(i3);
        }

        @Override // androidx.appcompat.widget.D.a
        public void m(int i3, float f3) {
        }
    }

    class c extends b {
        c() {
            super();
        }

        @Override // androidx.appcompat.widget.D.b, androidx.appcompat.widget.D.a
        public void d(int i3) {
            D.super.setLastBaselineToBottomHeight(i3);
        }

        @Override // androidx.appcompat.widget.D.b, androidx.appcompat.widget.D.a
        public void j(int i3) {
            D.super.setFirstBaselineToTopHeight(i3);
        }
    }

    class d extends c {
        d() {
            super();
        }

        @Override // androidx.appcompat.widget.D.b, androidx.appcompat.widget.D.a
        public void m(int i3, float f3) {
            D.super.setLineHeight(i3, f3);
        }
    }

    public D(Context context) {
        this(context, null);
    }

    private C0225n getEmojiTextViewHelper() {
        if (this.f3777e == null) {
            this.f3777e = new C0225n(this);
        }
        return this.f3777e;
    }

    private void s() {
        Future future = this.f3780h;
        if (future != null) {
            try {
                this.f3780h = null;
                androidx.activity.result.d.a(future.get());
                androidx.core.widget.i.l(this, null);
            } catch (InterruptedException | ExecutionException unused) {
            }
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        C0216e c0216e = this.f3774b;
        if (c0216e != null) {
            c0216e.b();
        }
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.b();
        }
    }

    @Override // android.widget.TextView
    public int getAutoSizeMaxTextSize() {
        if (s0.f4327c) {
            return getSuperCaller().f();
        }
        C c4 = this.f3775c;
        if (c4 != null) {
            return c4.e();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int getAutoSizeMinTextSize() {
        if (s0.f4327c) {
            return getSuperCaller().i();
        }
        C c4 = this.f3775c;
        if (c4 != null) {
            return c4.f();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int getAutoSizeStepGranularity() {
        if (s0.f4327c) {
            return getSuperCaller().k();
        }
        C c4 = this.f3775c;
        if (c4 != null) {
            return c4.g();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int[] getAutoSizeTextAvailableSizes() {
        if (s0.f4327c) {
            return getSuperCaller().c();
        }
        C c4 = this.f3775c;
        return c4 != null ? c4.h() : new int[0];
    }

    @Override // android.widget.TextView
    public int getAutoSizeTextType() {
        if (s0.f4327c) {
            return getSuperCaller().h() == 1 ? 1 : 0;
        }
        C c4 = this.f3775c;
        if (c4 != null) {
            return c4.i();
        }
        return 0;
    }

    @Override // android.widget.TextView
    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        return androidx.core.widget.i.n(super.getCustomSelectionActionModeCallback());
    }

    @Override // android.widget.TextView
    public int getFirstBaselineToTopHeight() {
        return androidx.core.widget.i.a(this);
    }

    @Override // android.widget.TextView
    public int getLastBaselineToBottomHeight() {
        return androidx.core.widget.i.b(this);
    }

    a getSuperCaller() {
        if (this.f3779g == null) {
            int i3 = Build.VERSION.SDK_INT;
            if (i3 >= 34) {
                this.f3779g = new d();
            } else if (i3 >= 28) {
                this.f3779g = new c();
            } else if (i3 >= 26) {
                this.f3779g = new b();
            }
        }
        return this.f3779g;
    }

    public ColorStateList getSupportBackgroundTintList() {
        C0216e c0216e = this.f3774b;
        if (c0216e != null) {
            return c0216e.c();
        }
        return null;
    }

    public PorterDuff.Mode getSupportBackgroundTintMode() {
        C0216e c0216e = this.f3774b;
        if (c0216e != null) {
            return c0216e.d();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f3775c.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f3775c.k();
    }

    @Override // android.widget.TextView
    public CharSequence getText() {
        s();
        return super.getText();
    }

    @Override // android.widget.TextView
    public TextClassifier getTextClassifier() {
        B b4;
        return (Build.VERSION.SDK_INT >= 28 || (b4 = this.f3776d) == null) ? getSuperCaller().e() : b4.a();
    }

    public l.a getTextMetricsParamsCompat() {
        return androidx.core.widget.i.e(this);
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        InputConnection inputConnectionOnCreateInputConnection = super.onCreateInputConnection(editorInfo);
        this.f3775c.r(this, inputConnectionOnCreateInputConnection, editorInfo);
        return AbstractC0226o.a(inputConnectionOnCreateInputConnection, editorInfo, this);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        int i3 = Build.VERSION.SDK_INT;
        if (i3 < 30 || i3 >= 33 || !onCheckIsTextEditor()) {
            return;
        }
        ((InputMethodManager) getContext().getSystemService("input_method")).isActive(this);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        super.onLayout(z3, i3, i4, i5, i6);
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.o(z3, i3, i4, i5, i6);
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i3, int i4) {
        s();
        super.onMeasure(i3, i4);
    }

    @Override // android.widget.TextView
    protected void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        super.onTextChanged(charSequence, i3, i4, i5);
        C c4 = this.f3775c;
        if (c4 == null || s0.f4327c || !c4.l()) {
            return;
        }
        this.f3775c.c();
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean z3) {
        super.setAllCaps(z3);
        getEmojiTextViewHelper().d(z3);
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeUniformWithConfiguration(int i3, int i4, int i5, int i6) {
        if (s0.f4327c) {
            getSuperCaller().g(i3, i4, i5, i6);
            return;
        }
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.t(i3, i4, i5, i6);
        }
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeUniformWithPresetSizes(int[] iArr, int i3) {
        if (s0.f4327c) {
            getSuperCaller().a(iArr, i3);
            return;
        }
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.u(iArr, i3);
        }
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeWithDefaults(int i3) {
        if (s0.f4327c) {
            getSuperCaller().l(i3);
            return;
        }
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.v(i3);
        }
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        C0216e c0216e = this.f3774b;
        if (c0216e != null) {
            c0216e.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i3) {
        super.setBackgroundResource(i3);
        C0216e c0216e = this.f3774b;
        if (c0216e != null) {
            c0216e.g(i3);
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(androidx.core.widget.i.o(this, callback));
    }

    public void setEmojiCompatEnabled(boolean z3) {
        getEmojiTextViewHelper().e(z3);
    }

    @Override // android.widget.TextView
    public void setFilters(InputFilter[] inputFilterArr) {
        super.setFilters(getEmojiTextViewHelper().a(inputFilterArr));
    }

    @Override // android.widget.TextView
    public void setFirstBaselineToTopHeight(int i3) {
        if (Build.VERSION.SDK_INT >= 28) {
            getSuperCaller().j(i3);
        } else {
            androidx.core.widget.i.h(this, i3);
        }
    }

    @Override // android.widget.TextView
    public void setLastBaselineToBottomHeight(int i3) {
        if (Build.VERSION.SDK_INT >= 28) {
            getSuperCaller().d(i3);
        } else {
            androidx.core.widget.i.i(this, i3);
        }
    }

    @Override // android.widget.TextView
    public void setLineHeight(int i3) {
        androidx.core.widget.i.j(this, i3);
    }

    public void setPrecomputedText(androidx.core.text.l lVar) {
        androidx.core.widget.i.l(this, lVar);
    }

    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        C0216e c0216e = this.f3774b;
        if (c0216e != null) {
            c0216e.i(colorStateList);
        }
    }

    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        C0216e c0216e = this.f3774b;
        if (c0216e != null) {
            c0216e.j(mode);
        }
    }

    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f3775c.w(colorStateList);
        this.f3775c.b();
    }

    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f3775c.x(mode);
        this.f3775c.b();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i3) {
        super.setTextAppearance(context, i3);
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.q(context, i3);
        }
    }

    @Override // android.widget.TextView
    public void setTextClassifier(TextClassifier textClassifier) {
        B b4;
        if (Build.VERSION.SDK_INT >= 28 || (b4 = this.f3776d) == null) {
            getSuperCaller().b(textClassifier);
        } else {
            b4.b(textClassifier);
        }
    }

    public void setTextFuture(Future<androidx.core.text.l> future) {
        this.f3780h = future;
        if (future != null) {
            requestLayout();
        }
    }

    public void setTextMetricsParamsCompat(l.a aVar) {
        androidx.core.widget.i.m(this, aVar);
    }

    @Override // android.widget.TextView
    public void setTextSize(int i3, float f3) {
        if (s0.f4327c) {
            super.setTextSize(i3, f3);
            return;
        }
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.A(i3, f3);
        }
    }

    @Override // android.widget.TextView
    public void setTypeface(Typeface typeface, int i3) {
        if (this.f3778f) {
            return;
        }
        Typeface typefaceA = (typeface == null || i3 <= 0) ? null : androidx.core.graphics.d.a(getContext(), typeface, i3);
        this.f3778f = true;
        if (typefaceA != null) {
            typeface = typefaceA;
        }
        try {
            super.setTypeface(typeface, i3);
        } finally {
            this.f3778f = false;
        }
    }

    public D(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.textViewStyle);
    }

    @Override // android.widget.TextView
    public void setLineHeight(int i3, float f3) {
        if (Build.VERSION.SDK_INT >= 34) {
            getSuperCaller().m(i3, f3);
        } else {
            androidx.core.widget.i.k(this, i3, f3);
        }
    }

    public D(Context context, AttributeSet attributeSet, int i3) {
        super(e0.b(context), attributeSet, i3);
        this.f3778f = false;
        this.f3779g = null;
        d0.a(this, getContext());
        C0216e c0216e = new C0216e(this);
        this.f3774b = c0216e;
        c0216e.e(attributeSet, i3);
        C c4 = new C(this);
        this.f3775c = c4;
        c4.m(attributeSet, i3);
        c4.b();
        this.f3776d = new B(this);
        getEmojiTextViewHelper().c(attributeSet, i3);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int i3, int i4, int i5, int i6) {
        Context context = getContext();
        setCompoundDrawablesRelativeWithIntrinsicBounds(i3 != 0 ? AbstractC0521a.b(context, i3) : null, i4 != 0 ? AbstractC0521a.b(context, i4) : null, i5 != 0 ? AbstractC0521a.b(context, i5) : null, i6 != 0 ? AbstractC0521a.b(context, i6) : null);
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(int i3, int i4, int i5, int i6) {
        Context context = getContext();
        setCompoundDrawablesWithIntrinsicBounds(i3 != 0 ? AbstractC0521a.b(context, i3) : null, i4 != 0 ? AbstractC0521a.b(context, i4) : null, i5 != 0 ? AbstractC0521a.b(context, i5) : null, i6 != 0 ? AbstractC0521a.b(context, i6) : null);
        C c4 = this.f3775c;
        if (c4 != null) {
            c4.p();
        }
    }
}
