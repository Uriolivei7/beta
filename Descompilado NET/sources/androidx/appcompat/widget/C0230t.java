package androidx.appcompat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

/* JADX INFO: renamed from: androidx.appcompat.widget.t, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0230t extends PopupWindow {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final boolean f4328b = false;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f4329a;

    public C0230t(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        a(context, attributeSet, i3, 0);
    }

    private void a(Context context, AttributeSet attributeSet, int i3, int i4) {
        h0 h0VarU = h0.u(context, attributeSet, d.j.f8946Y1, i3, i4);
        if (h0VarU.r(d.j.f8955a2)) {
            b(h0VarU.a(d.j.f8955a2, false));
        }
        setBackgroundDrawable(h0VarU.f(d.j.f8950Z1));
        h0VarU.w();
    }

    private void b(boolean z3) {
        if (f4328b) {
            this.f4329a = z3;
        } else {
            androidx.core.widget.h.a(this, z3);
        }
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i3, int i4) {
        if (f4328b && this.f4329a) {
            i4 -= view.getHeight();
        }
        super.showAsDropDown(view, i3, i4);
    }

    @Override // android.widget.PopupWindow
    public void update(View view, int i3, int i4, int i5, int i6) {
        if (f4328b && this.f4329a) {
            i4 -= view.getHeight();
        }
        super.update(view, i3, i4, i5, i6);
    }

    public C0230t(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        a(context, attributeSet, i3, i4);
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i3, int i4, int i5) {
        if (f4328b && this.f4329a) {
            i4 -= view.getHeight();
        }
        super.showAsDropDown(view, i3, i4, i5);
    }
}
