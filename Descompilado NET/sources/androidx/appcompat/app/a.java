package androidx.appcompat.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.view.b;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {

    public static abstract class b {
        public abstract CharSequence a();

        public abstract View b();

        public abstract Drawable c();

        public abstract CharSequence d();

        public abstract void e();
    }

    public boolean g() {
        return false;
    }

    public boolean h() {
        return false;
    }

    public void i(boolean z3) {
    }

    public abstract int j();

    public Context k() {
        return null;
    }

    public boolean l() {
        return false;
    }

    public void m(Configuration configuration) {
    }

    void n() {
    }

    public boolean o(int i3, KeyEvent keyEvent) {
        return false;
    }

    public boolean p(KeyEvent keyEvent) {
        return false;
    }

    public boolean q() {
        return false;
    }

    public void r(boolean z3) {
    }

    public void s(boolean z3) {
    }

    public void t(CharSequence charSequence) {
    }

    public androidx.appcompat.view.b u(b.a aVar) {
        return null;
    }

    /* JADX INFO: renamed from: androidx.appcompat.app.a$a, reason: collision with other inner class name */
    public static class C0051a extends ViewGroup.MarginLayoutParams {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public int f3161a;

        public C0051a(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f3161a = 0;
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, d.j.f9033t);
            this.f3161a = typedArrayObtainStyledAttributes.getInt(d.j.f9037u, 0);
            typedArrayObtainStyledAttributes.recycle();
        }

        public C0051a(int i3, int i4) {
            super(i3, i4);
            this.f3161a = 8388627;
        }

        public C0051a(int i3, int i4, int i5) {
            super(i3, i4);
            this.f3161a = i5;
        }

        public C0051a(int i3) {
            this(-2, -1, i3);
        }

        public C0051a(C0051a c0051a) {
            super((ViewGroup.MarginLayoutParams) c0051a);
            this.f3161a = 0;
            this.f3161a = c0051a.f3161a;
        }

        public C0051a(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f3161a = 0;
        }
    }
}
