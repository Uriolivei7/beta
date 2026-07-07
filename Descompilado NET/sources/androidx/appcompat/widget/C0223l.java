package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextClassifier;
import android.widget.EditText;
import androidx.core.view.C0243d;
import d.AbstractC0487a;
import u.C0733c;

/* JADX INFO: renamed from: androidx.appcompat.widget.l, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0223l extends EditText implements androidx.core.view.K {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0216e f4254b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C f4255c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final B f4256d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final androidx.core.widget.j f4257e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final C0224m f4258f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private a f4259g;

    /* JADX INFO: renamed from: androidx.appcompat.widget.l$a */
    class a {
        a() {
        }

        public TextClassifier a() {
            return C0223l.super.getTextClassifier();
        }

        public void b(TextClassifier textClassifier) {
            C0223l.super.setTextClassifier(textClassifier);
        }
    }

    public C0223l(Context context) {
        this(context, null);
    }

    private a getSuperCaller() {
        if (this.f4259g == null) {
            this.f4259g = new a();
        }
        return this.f4259g;
    }

    @Override // androidx.core.view.K
    public C0243d a(C0243d c0243d) {
        return this.f4257e.a(this, c0243d);
    }

    void d(C0224m c0224m) {
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
        C0216e c0216e = this.f4254b;
        if (c0216e != null) {
            c0216e.b();
        }
        C c4 = this.f4255c;
        if (c4 != null) {
            c4.b();
        }
    }

    @Override // android.widget.TextView
    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        return androidx.core.widget.i.n(super.getCustomSelectionActionModeCallback());
    }

    public ColorStateList getSupportBackgroundTintList() {
        C0216e c0216e = this.f4254b;
        if (c0216e != null) {
            return c0216e.c();
        }
        return null;
    }

    public PorterDuff.Mode getSupportBackgroundTintMode() {
        C0216e c0216e = this.f4254b;
        if (c0216e != null) {
            return c0216e.d();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f4255c.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f4255c.k();
    }

    @Override // android.widget.TextView
    public TextClassifier getTextClassifier() {
        B b4;
        return (Build.VERSION.SDK_INT >= 28 || (b4 = this.f4256d) == null) ? getSuperCaller().a() : b4.a();
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        String[] strArrU;
        InputConnection inputConnectionOnCreateInputConnection = super.onCreateInputConnection(editorInfo);
        this.f4255c.r(this, inputConnectionOnCreateInputConnection, editorInfo);
        InputConnection inputConnectionA = AbstractC0226o.a(inputConnectionOnCreateInputConnection, editorInfo, this);
        if (inputConnectionA != null && Build.VERSION.SDK_INT <= 30 && (strArrU = androidx.core.view.Z.u(this)) != null) {
            C0733c.d(editorInfo, strArrU);
            inputConnectionA = u.e.c(this, inputConnectionA, editorInfo);
        }
        return this.f4258f.d(inputConnectionA, editorInfo);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        int i3 = Build.VERSION.SDK_INT;
        if (i3 < 30 || i3 >= 33) {
            return;
        }
        ((InputMethodManager) getContext().getSystemService("input_method")).isActive(this);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onDragEvent(DragEvent dragEvent) {
        if (AbstractC0234x.a(this, dragEvent)) {
            return true;
        }
        return super.onDragEvent(dragEvent);
    }

    @Override // android.widget.EditText, android.widget.TextView
    public boolean onTextContextMenuItem(int i3) {
        if (AbstractC0234x.b(this, i3)) {
            return true;
        }
        return super.onTextContextMenuItem(i3);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        C0216e c0216e = this.f4254b;
        if (c0216e != null) {
            c0216e.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i3) {
        super.setBackgroundResource(i3);
        C0216e c0216e = this.f4254b;
        if (c0216e != null) {
            c0216e.g(i3);
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f4255c;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        C c4 = this.f4255c;
        if (c4 != null) {
            c4.p();
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(androidx.core.widget.i.o(this, callback));
    }

    public void setEmojiCompatEnabled(boolean z3) {
        this.f4258f.e(z3);
    }

    @Override // android.widget.TextView
    public void setKeyListener(KeyListener keyListener) {
        super.setKeyListener(this.f4258f.a(keyListener));
    }

    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        C0216e c0216e = this.f4254b;
        if (c0216e != null) {
            c0216e.i(colorStateList);
        }
    }

    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        C0216e c0216e = this.f4254b;
        if (c0216e != null) {
            c0216e.j(mode);
        }
    }

    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f4255c.w(colorStateList);
        this.f4255c.b();
    }

    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f4255c.x(mode);
        this.f4255c.b();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i3) {
        super.setTextAppearance(context, i3);
        C c4 = this.f4255c;
        if (c4 != null) {
            c4.q(context, i3);
        }
    }

    @Override // android.widget.TextView
    public void setTextClassifier(TextClassifier textClassifier) {
        B b4;
        if (Build.VERSION.SDK_INT >= 28 || (b4 = this.f4256d) == null) {
            getSuperCaller().b(textClassifier);
        } else {
            b4.b(textClassifier);
        }
    }

    public C0223l(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8658C);
    }

    @Override // android.widget.EditText, android.widget.TextView
    public Editable getText() {
        return Build.VERSION.SDK_INT >= 28 ? super.getText() : super.getEditableText();
    }

    public C0223l(Context context, AttributeSet attributeSet, int i3) {
        super(e0.b(context), attributeSet, i3);
        d0.a(this, getContext());
        C0216e c0216e = new C0216e(this);
        this.f4254b = c0216e;
        c0216e.e(attributeSet, i3);
        C c4 = new C(this);
        this.f4255c = c4;
        c4.m(attributeSet, i3);
        c4.b();
        this.f4256d = new B(this);
        this.f4257e = new androidx.core.widget.j();
        C0224m c0224m = new C0224m(this);
        this.f4258f = c0224m;
        c0224m.c(attributeSet, i3);
        d(c0224m);
    }
}
