package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.CheckedTextView;
import d.AbstractC0487a;
import e.AbstractC0521a;

/* JADX INFO: renamed from: androidx.appcompat.widget.h, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0219h extends CheckedTextView {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0220i f4223b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0216e f4224c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final C f4225d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private C0225n f4226e;

    public C0219h(Context context) {
        this(context, null);
    }

    private C0225n getEmojiTextViewHelper() {
        if (this.f4226e == null) {
            this.f4226e = new C0225n(this);
        }
        return this.f4226e;
    }

    @Override // android.widget.CheckedTextView, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        C c4 = this.f4225d;
        if (c4 != null) {
            c4.b();
        }
        C0216e c0216e = this.f4224c;
        if (c0216e != null) {
            c0216e.b();
        }
        C0220i c0220i = this.f4223b;
        if (c0220i != null) {
            c0220i.a();
        }
    }

    @Override // android.widget.TextView
    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        return androidx.core.widget.i.n(super.getCustomSelectionActionModeCallback());
    }

    public ColorStateList getSupportBackgroundTintList() {
        C0216e c0216e = this.f4224c;
        if (c0216e != null) {
            return c0216e.c();
        }
        return null;
    }

    public PorterDuff.Mode getSupportBackgroundTintMode() {
        C0216e c0216e = this.f4224c;
        if (c0216e != null) {
            return c0216e.d();
        }
        return null;
    }

    public ColorStateList getSupportCheckMarkTintList() {
        C0220i c0220i = this.f4223b;
        if (c0220i != null) {
            return c0220i.b();
        }
        return null;
    }

    public PorterDuff.Mode getSupportCheckMarkTintMode() {
        C0220i c0220i = this.f4223b;
        if (c0220i != null) {
            return c0220i.c();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f4225d.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f4225d.k();
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        return AbstractC0226o.a(super.onCreateInputConnection(editorInfo), editorInfo, this);
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean z3) {
        super.setAllCaps(z3);
        getEmojiTextViewHelper().d(z3);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        C0216e c0216e = this.f4224c;
        if (c0216e != null) {
            c0216e.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i3) {
        super.setBackgroundResource(i3);
        C0216e c0216e = this.f4224c;
        if (c0216e != null) {
            c0216e.g(i3);
        }
    }

    @Override // android.widget.CheckedTextView
    public void setCheckMarkDrawable(Drawable drawable) {
        super.setCheckMarkDrawable(drawable);
        C0220i c0220i = this.f4223b;
        if (c0220i != null) {
            c0220i.e();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f4225d;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f4225d;
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

    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        C0216e c0216e = this.f4224c;
        if (c0216e != null) {
            c0216e.i(colorStateList);
        }
    }

    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        C0216e c0216e = this.f4224c;
        if (c0216e != null) {
            c0216e.j(mode);
        }
    }

    public void setSupportCheckMarkTintList(ColorStateList colorStateList) {
        C0220i c0220i = this.f4223b;
        if (c0220i != null) {
            c0220i.f(colorStateList);
        }
    }

    public void setSupportCheckMarkTintMode(PorterDuff.Mode mode) {
        C0220i c0220i = this.f4223b;
        if (c0220i != null) {
            c0220i.g(mode);
        }
    }

    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f4225d.w(colorStateList);
        this.f4225d.b();
    }

    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f4225d.x(mode);
        this.f4225d.b();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i3) {
        super.setTextAppearance(context, i3);
        C c4 = this.f4225d;
        if (c4 != null) {
            c4.q(context, i3);
        }
    }

    public C0219h(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8692t);
    }

    public C0219h(Context context, AttributeSet attributeSet, int i3) {
        super(e0.b(context), attributeSet, i3);
        d0.a(this, getContext());
        C c4 = new C(this);
        this.f4225d = c4;
        c4.m(attributeSet, i3);
        c4.b();
        C0216e c0216e = new C0216e(this);
        this.f4224c = c0216e;
        c0216e.e(attributeSet, i3);
        C0220i c0220i = new C0220i(this);
        this.f4223b = c0220i;
        c0220i.d(attributeSet, i3);
        getEmojiTextViewHelper().c(attributeSet, i3);
    }

    @Override // android.widget.CheckedTextView
    public void setCheckMarkDrawable(int i3) {
        setCheckMarkDrawable(AbstractC0521a.b(getContext(), i3));
    }
}
