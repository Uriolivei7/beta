package androidx.appcompat.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.ToggleButton;

/* JADX INFO: loaded from: classes.dex */
public class H extends ToggleButton {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0216e f3799b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C f3800c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private C0225n f3801d;

    public H(Context context) {
        this(context, null);
    }

    private C0225n getEmojiTextViewHelper() {
        if (this.f3801d == null) {
            this.f3801d = new C0225n(this);
        }
        return this.f3801d;
    }

    @Override // android.widget.ToggleButton, android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        C0216e c0216e = this.f3799b;
        if (c0216e != null) {
            c0216e.b();
        }
        C c4 = this.f3800c;
        if (c4 != null) {
            c4.b();
        }
    }

    public ColorStateList getSupportBackgroundTintList() {
        C0216e c0216e = this.f3799b;
        if (c0216e != null) {
            return c0216e.c();
        }
        return null;
    }

    public PorterDuff.Mode getSupportBackgroundTintMode() {
        C0216e c0216e = this.f3799b;
        if (c0216e != null) {
            return c0216e.d();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f3800c.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f3800c.k();
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean z3) {
        super.setAllCaps(z3);
        getEmojiTextViewHelper().d(z3);
    }

    @Override // android.widget.ToggleButton, android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        C0216e c0216e = this.f3799b;
        if (c0216e != null) {
            c0216e.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i3) {
        super.setBackgroundResource(i3);
        C0216e c0216e = this.f3799b;
        if (c0216e != null) {
            c0216e.g(i3);
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f3800c;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f3800c;
        if (c4 != null) {
            c4.p();
        }
    }

    public void setEmojiCompatEnabled(boolean z3) {
        getEmojiTextViewHelper().e(z3);
    }

    @Override // android.widget.TextView
    public void setFilters(InputFilter[] inputFilterArr) {
        super.setFilters(getEmojiTextViewHelper().a(inputFilterArr));
    }

    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        C0216e c0216e = this.f3799b;
        if (c0216e != null) {
            c0216e.i(colorStateList);
        }
    }

    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        C0216e c0216e = this.f3799b;
        if (c0216e != null) {
            c0216e.j(mode);
        }
    }

    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f3800c.w(colorStateList);
        this.f3800c.b();
    }

    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f3800c.x(mode);
        this.f3800c.b();
    }

    public H(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.buttonStyleToggle);
    }

    public H(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        d0.a(this, getContext());
        C0216e c0216e = new C0216e(this);
        this.f3799b = c0216e;
        c0216e.e(attributeSet, i3);
        C c4 = new C(this);
        this.f3800c = c4;
        c4.m(attributeSet, i3);
        getEmojiTextViewHelper().c(attributeSet, i3);
    }
}
