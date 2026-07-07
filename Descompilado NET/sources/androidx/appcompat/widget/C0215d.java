package androidx.appcompat.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.AutoCompleteTextView;
import d.AbstractC0487a;
import e.AbstractC0521a;

/* JADX INFO: renamed from: androidx.appcompat.widget.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0215d extends AutoCompleteTextView {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final int[] f4187e = {R.attr.popupBackground};

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0216e f4188b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C f4189c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final C0224m f4190d;

    public C0215d(Context context) {
        this(context, null);
    }

    void a(C0224m c0224m) {
        KeyListener keyListener = getKeyListener();
        if (c0224m.b(keyListener)) {
            boolean zIsFocusable = super.isFocusable();
            boolean zIsClickable = super.isClickable();
            boolean zIsLongClickable = super.isLongClickable();
            int inputType = super.getInputType();
            KeyListener keyListenerA = c0224m.a(keyListener);
            if (keyListenerA == keyListener) {
                return;
            }
            super.setKeyListener(keyListenerA);
            super.setRawInputType(inputType);
            super.setFocusable(zIsFocusable);
            super.setClickable(zIsClickable);
            super.setLongClickable(zIsLongClickable);
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        C0216e c0216e = this.f4188b;
        if (c0216e != null) {
            c0216e.b();
        }
        C c4 = this.f4189c;
        if (c4 != null) {
            c4.b();
        }
    }

    @Override // android.widget.TextView
    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        return androidx.core.widget.i.n(super.getCustomSelectionActionModeCallback());
    }

    public ColorStateList getSupportBackgroundTintList() {
        C0216e c0216e = this.f4188b;
        if (c0216e != null) {
            return c0216e.c();
        }
        return null;
    }

    public PorterDuff.Mode getSupportBackgroundTintMode() {
        C0216e c0216e = this.f4188b;
        if (c0216e != null) {
            return c0216e.d();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f4189c.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f4189c.k();
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        return this.f4190d.d(AbstractC0226o.a(super.onCreateInputConnection(editorInfo), editorInfo, this), editorInfo);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        C0216e c0216e = this.f4188b;
        if (c0216e != null) {
            c0216e.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i3) {
        super.setBackgroundResource(i3);
        C0216e c0216e = this.f4188b;
        if (c0216e != null) {
            c0216e.g(i3);
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f4189c;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f4189c;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(androidx.core.widget.i.o(this, callback));
    }

    @Override // android.widget.AutoCompleteTextView
    public void setDropDownBackgroundResource(int i3) {
        setDropDownBackgroundDrawable(AbstractC0521a.b(getContext(), i3));
    }

    public void setEmojiCompatEnabled(boolean z3) {
        this.f4190d.e(z3);
    }

    @Override // android.widget.TextView
    public void setKeyListener(KeyListener keyListener) {
        super.setKeyListener(this.f4190d.a(keyListener));
    }

    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        C0216e c0216e = this.f4188b;
        if (c0216e != null) {
            c0216e.i(colorStateList);
        }
    }

    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        C0216e c0216e = this.f4188b;
        if (c0216e != null) {
            c0216e.j(mode);
        }
    }

    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f4189c.w(colorStateList);
        this.f4189c.b();
    }

    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f4189c.x(mode);
        this.f4189c.b();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i3) {
        super.setTextAppearance(context, i3);
        C c4 = this.f4189c;
        if (c4 != null) {
            c4.q(context, i3);
        }
    }

    public C0215d(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8689q);
    }

    public C0215d(Context context, AttributeSet attributeSet, int i3) {
        super(e0.b(context), attributeSet, i3);
        d0.a(this, getContext());
        h0 h0VarU = h0.u(getContext(), attributeSet, f4187e, i3, 0);
        if (h0VarU.r(0)) {
            setDropDownBackgroundDrawable(h0VarU.f(0));
        }
        h0VarU.w();
        C0216e c0216e = new C0216e(this);
        this.f4188b = c0216e;
        c0216e.e(attributeSet, i3);
        C c4 = new C(this);
        this.f4189c = c4;
        c4.m(attributeSet, i3);
        c4.b();
        C0224m c0224m = new C0224m(this);
        this.f4190d = c0224m;
        c0224m.c(attributeSet, i3);
        a(c0224m);
    }
}
