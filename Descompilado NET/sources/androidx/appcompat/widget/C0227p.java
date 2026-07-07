package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageButton;
import d.AbstractC0487a;

/* JADX INFO: renamed from: androidx.appcompat.widget.p, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0227p extends ImageButton {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0216e f4289b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0228q f4290c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f4291d;

    public C0227p(Context context) {
        this(context, null);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        C0216e c0216e = this.f4289b;
        if (c0216e != null) {
            c0216e.b();
        }
        C0228q c0228q = this.f4290c;
        if (c0228q != null) {
            c0228q.c();
        }
    }

    public ColorStateList getSupportBackgroundTintList() {
        C0216e c0216e = this.f4289b;
        if (c0216e != null) {
            return c0216e.c();
        }
        return null;
    }

    public PorterDuff.Mode getSupportBackgroundTintMode() {
        C0216e c0216e = this.f4289b;
        if (c0216e != null) {
            return c0216e.d();
        }
        return null;
    }

    public ColorStateList getSupportImageTintList() {
        C0228q c0228q = this.f4290c;
        if (c0228q != null) {
            return c0228q.d();
        }
        return null;
    }

    public PorterDuff.Mode getSupportImageTintMode() {
        C0228q c0228q = this.f4290c;
        if (c0228q != null) {
            return c0228q.e();
        }
        return null;
    }

    @Override // android.widget.ImageView, android.view.View
    public boolean hasOverlappingRendering() {
        return this.f4290c.f() && super.hasOverlappingRendering();
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        C0216e c0216e = this.f4289b;
        if (c0216e != null) {
            c0216e.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i3) {
        super.setBackgroundResource(i3);
        C0216e c0216e = this.f4289b;
        if (c0216e != null) {
            c0216e.g(i3);
        }
    }

    @Override // android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        C0228q c0228q = this.f4290c;
        if (c0228q != null) {
            c0228q.c();
        }
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        C0228q c0228q = this.f4290c;
        if (c0228q != null && drawable != null && !this.f4291d) {
            c0228q.h(drawable);
        }
        super.setImageDrawable(drawable);
        C0228q c0228q2 = this.f4290c;
        if (c0228q2 != null) {
            c0228q2.c();
            if (this.f4291d) {
                return;
            }
            this.f4290c.b();
        }
    }

    @Override // android.widget.ImageView
    public void setImageLevel(int i3) {
        super.setImageLevel(i3);
        this.f4291d = true;
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i3) {
        this.f4290c.i(i3);
    }

    @Override // android.widget.ImageView
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        C0228q c0228q = this.f4290c;
        if (c0228q != null) {
            c0228q.c();
        }
    }

    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        C0216e c0216e = this.f4289b;
        if (c0216e != null) {
            c0216e.i(colorStateList);
        }
    }

    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        C0216e c0216e = this.f4289b;
        if (c0216e != null) {
            c0216e.j(mode);
        }
    }

    public void setSupportImageTintList(ColorStateList colorStateList) {
        C0228q c0228q = this.f4290c;
        if (c0228q != null) {
            c0228q.j(colorStateList);
        }
    }

    public void setSupportImageTintMode(PorterDuff.Mode mode) {
        C0228q c0228q = this.f4290c;
        if (c0228q != null) {
            c0228q.k(mode);
        }
    }

    public C0227p(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8659D);
    }

    public C0227p(Context context, AttributeSet attributeSet, int i3) {
        super(e0.b(context), attributeSet, i3);
        this.f4291d = false;
        d0.a(this, getContext());
        C0216e c0216e = new C0216e(this);
        this.f4289b = c0216e;
        c0216e.e(attributeSet, i3);
        C0228q c0228q = new C0228q(this);
        this.f4290c = c0228q;
        c0228q.g(attributeSet, i3);
    }
}
