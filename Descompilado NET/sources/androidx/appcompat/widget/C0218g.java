package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.CheckBox;
import d.AbstractC0487a;
import e.AbstractC0521a;

/* JADX INFO: renamed from: androidx.appcompat.widget.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0218g extends CheckBox {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0221j f4218b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0216e f4219c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final C f4220d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private C0225n f4221e;

    public C0218g(Context context) {
        this(context, null);
    }

    private C0225n getEmojiTextViewHelper() {
        if (this.f4221e == null) {
            this.f4221e = new C0225n(this);
        }
        return this.f4221e;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        C0216e c0216e = this.f4219c;
        if (c0216e != null) {
            c0216e.b();
        }
        C c4 = this.f4220d;
        if (c4 != null) {
            c4.b();
        }
    }

    public ColorStateList getSupportBackgroundTintList() {
        C0216e c0216e = this.f4219c;
        if (c0216e != null) {
            return c0216e.c();
        }
        return null;
    }

    public PorterDuff.Mode getSupportBackgroundTintMode() {
        C0216e c0216e = this.f4219c;
        if (c0216e != null) {
            return c0216e.d();
        }
        return null;
    }

    public ColorStateList getSupportButtonTintList() {
        C0221j c0221j = this.f4218b;
        if (c0221j != null) {
            return c0221j.b();
        }
        return null;
    }

    public PorterDuff.Mode getSupportButtonTintMode() {
        C0221j c0221j = this.f4218b;
        if (c0221j != null) {
            return c0221j.c();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f4220d.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f4220d.k();
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean z3) {
        super.setAllCaps(z3);
        getEmojiTextViewHelper().d(z3);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        C0216e c0216e = this.f4219c;
        if (c0216e != null) {
            c0216e.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i3) {
        super.setBackgroundResource(i3);
        C0216e c0216e = this.f4219c;
        if (c0216e != null) {
            c0216e.g(i3);
        }
    }

    @Override // android.widget.CompoundButton
    public void setButtonDrawable(Drawable drawable) {
        super.setButtonDrawable(drawable);
        C0221j c0221j = this.f4218b;
        if (c0221j != null) {
            c0221j.e();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f4220d;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f4220d;
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
        C0216e c0216e = this.f4219c;
        if (c0216e != null) {
            c0216e.i(colorStateList);
        }
    }

    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        C0216e c0216e = this.f4219c;
        if (c0216e != null) {
            c0216e.j(mode);
        }
    }

    public void setSupportButtonTintList(ColorStateList colorStateList) {
        C0221j c0221j = this.f4218b;
        if (c0221j != null) {
            c0221j.f(colorStateList);
        }
    }

    public void setSupportButtonTintMode(PorterDuff.Mode mode) {
        C0221j c0221j = this.f4218b;
        if (c0221j != null) {
            c0221j.g(mode);
        }
    }

    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f4220d.w(colorStateList);
        this.f4220d.b();
    }

    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f4220d.x(mode);
        this.f4220d.b();
    }

    public C0218g(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8691s);
    }

    public C0218g(Context context, AttributeSet attributeSet, int i3) {
        super(e0.b(context), attributeSet, i3);
        d0.a(this, getContext());
        C0221j c0221j = new C0221j(this);
        this.f4218b = c0221j;
        c0221j.d(attributeSet, i3);
        C0216e c0216e = new C0216e(this);
        this.f4219c = c0216e;
        c0216e.e(attributeSet, i3);
        C c4 = new C(this);
        this.f4220d = c4;
        c4.m(attributeSet, i3);
        getEmojiTextViewHelper().c(attributeSet, i3);
    }

    @Override // android.widget.CompoundButton
    public void setButtonDrawable(int i3) {
        setButtonDrawable(AbstractC0521a.b(getContext(), i3));
    }
}
