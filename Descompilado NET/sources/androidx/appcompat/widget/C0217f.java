package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import d.AbstractC0487a;

/* JADX INFO: renamed from: androidx.appcompat.widget.f, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0217f extends Button {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0216e f4211b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C f4212c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private C0225n f4213d;

    public C0217f(Context context) {
        this(context, null);
    }

    private C0225n getEmojiTextViewHelper() {
        if (this.f4213d == null) {
            this.f4213d = new C0225n(this);
        }
        return this.f4213d;
    }

    @Override // android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        C0216e c0216e = this.f4211b;
        if (c0216e != null) {
            c0216e.b();
        }
        C c4 = this.f4212c;
        if (c4 != null) {
            c4.b();
        }
    }

    @Override // android.widget.TextView
    public int getAutoSizeMaxTextSize() {
        if (s0.f4327c) {
            return super.getAutoSizeMaxTextSize();
        }
        C c4 = this.f4212c;
        if (c4 != null) {
            return c4.e();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int getAutoSizeMinTextSize() {
        if (s0.f4327c) {
            return super.getAutoSizeMinTextSize();
        }
        C c4 = this.f4212c;
        if (c4 != null) {
            return c4.f();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int getAutoSizeStepGranularity() {
        if (s0.f4327c) {
            return super.getAutoSizeStepGranularity();
        }
        C c4 = this.f4212c;
        if (c4 != null) {
            return c4.g();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int[] getAutoSizeTextAvailableSizes() {
        if (s0.f4327c) {
            return super.getAutoSizeTextAvailableSizes();
        }
        C c4 = this.f4212c;
        return c4 != null ? c4.h() : new int[0];
    }

    @Override // android.widget.TextView
    public int getAutoSizeTextType() {
        if (s0.f4327c) {
            return super.getAutoSizeTextType() == 1 ? 1 : 0;
        }
        C c4 = this.f4212c;
        if (c4 != null) {
            return c4.i();
        }
        return 0;
    }

    @Override // android.widget.TextView
    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        return androidx.core.widget.i.n(super.getCustomSelectionActionModeCallback());
    }

    public ColorStateList getSupportBackgroundTintList() {
        C0216e c0216e = this.f4211b;
        if (c0216e != null) {
            return c0216e.c();
        }
        return null;
    }

    public PorterDuff.Mode getSupportBackgroundTintMode() {
        C0216e c0216e = this.f4211b;
        if (c0216e != null) {
            return c0216e.d();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f4212c.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f4212c.k();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(Button.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(Button.class.getName());
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        super.onLayout(z3, i3, i4, i5, i6);
        C c4 = this.f4212c;
        if (c4 != null) {
            c4.o(z3, i3, i4, i5, i6);
        }
    }

    @Override // android.widget.TextView
    protected void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        super.onTextChanged(charSequence, i3, i4, i5);
        C c4 = this.f4212c;
        if (c4 == null || s0.f4327c || !c4.l()) {
            return;
        }
        this.f4212c.c();
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean z3) {
        super.setAllCaps(z3);
        getEmojiTextViewHelper().d(z3);
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeUniformWithConfiguration(int i3, int i4, int i5, int i6) {
        if (s0.f4327c) {
            super.setAutoSizeTextTypeUniformWithConfiguration(i3, i4, i5, i6);
            return;
        }
        C c4 = this.f4212c;
        if (c4 != null) {
            c4.t(i3, i4, i5, i6);
        }
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeUniformWithPresetSizes(int[] iArr, int i3) {
        if (s0.f4327c) {
            super.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i3);
            return;
        }
        C c4 = this.f4212c;
        if (c4 != null) {
            c4.u(iArr, i3);
        }
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeWithDefaults(int i3) {
        if (s0.f4327c) {
            super.setAutoSizeTextTypeWithDefaults(i3);
            return;
        }
        C c4 = this.f4212c;
        if (c4 != null) {
            c4.v(i3);
        }
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        C0216e c0216e = this.f4211b;
        if (c0216e != null) {
            c0216e.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i3) {
        super.setBackgroundResource(i3);
        C0216e c0216e = this.f4211b;
        if (c0216e != null) {
            c0216e.g(i3);
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

    public void setSupportAllCaps(boolean z3) {
        C c4 = this.f4212c;
        if (c4 != null) {
            c4.s(z3);
        }
    }

    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        C0216e c0216e = this.f4211b;
        if (c0216e != null) {
            c0216e.i(colorStateList);
        }
    }

    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        C0216e c0216e = this.f4211b;
        if (c0216e != null) {
            c0216e.j(mode);
        }
    }

    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f4212c.w(colorStateList);
        this.f4212c.b();
    }

    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f4212c.x(mode);
        this.f4212c.b();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i3) {
        super.setTextAppearance(context, i3);
        C c4 = this.f4212c;
        if (c4 != null) {
            c4.q(context, i3);
        }
    }

    @Override // android.widget.TextView
    public void setTextSize(int i3, float f3) {
        if (s0.f4327c) {
            super.setTextSize(i3, f3);
            return;
        }
        C c4 = this.f4212c;
        if (c4 != null) {
            c4.A(i3, f3);
        }
    }

    public C0217f(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8690r);
    }

    public C0217f(Context context, AttributeSet attributeSet, int i3) {
        super(e0.b(context), attributeSet, i3);
        d0.a(this, getContext());
        C0216e c0216e = new C0216e(this);
        this.f4211b = c0216e;
        c0216e.e(attributeSet, i3);
        C c4 = new C(this);
        this.f4212c = c4;
        c4.m(attributeSet, i3);
        c4.b();
        getEmojiTextViewHelper().c(attributeSet, i3);
    }
}
