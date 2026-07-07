package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.C0254i0;
import androidx.core.view.InterfaceC0256j0;
import d.AbstractC0487a;

/* JADX INFO: renamed from: androidx.appcompat.widget.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
abstract class AbstractC0212a extends ViewGroup {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected final C0054a f4055b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected final Context f4056c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected ActionMenuView f4057d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    protected C0214c f4058e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    protected int f4059f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    protected C0254i0 f4060g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f4061h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f4062i;

    /* JADX INFO: renamed from: androidx.appcompat.widget.a$a, reason: collision with other inner class name */
    protected class C0054a implements InterfaceC0256j0 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean f4063a = false;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        int f4064b;

        protected C0054a() {
        }

        @Override // androidx.core.view.InterfaceC0256j0
        public void a(View view) {
            this.f4063a = true;
        }

        @Override // androidx.core.view.InterfaceC0256j0
        public void b(View view) {
            if (this.f4063a) {
                return;
            }
            AbstractC0212a abstractC0212a = AbstractC0212a.this;
            abstractC0212a.f4060g = null;
            AbstractC0212a.super.setVisibility(this.f4064b);
        }

        @Override // androidx.core.view.InterfaceC0256j0
        public void c(View view) {
            AbstractC0212a.super.setVisibility(0);
            this.f4063a = false;
        }

        public C0054a d(C0254i0 c0254i0, int i3) {
            AbstractC0212a.this.f4060g = c0254i0;
            this.f4064b = i3;
            return this;
        }
    }

    AbstractC0212a(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.f4055b = new C0054a();
        TypedValue typedValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(AbstractC0487a.f8673a, typedValue, true) || typedValue.resourceId == 0) {
            this.f4056c = context;
        } else {
            this.f4056c = new ContextThemeWrapper(context, typedValue.resourceId);
        }
    }

    protected static int d(int i3, int i4, boolean z3) {
        return z3 ? i3 - i4 : i3 + i4;
    }

    protected int c(View view, int i3, int i4, int i5) {
        view.measure(View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE), i4);
        return Math.max(0, (i3 - view.getMeasuredWidth()) - i5);
    }

    protected int e(View view, int i3, int i4, int i5, boolean z3) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int i6 = i4 + ((i5 - measuredHeight) / 2);
        if (z3) {
            view.layout(i3 - measuredWidth, i6, i3, measuredHeight + i6);
        } else {
            view.layout(i3, i6, i3 + measuredWidth, measuredHeight + i6);
        }
        return z3 ? -measuredWidth : measuredWidth;
    }

    public C0254i0 f(int i3, long j3) {
        C0254i0 c0254i0 = this.f4060g;
        if (c0254i0 != null) {
            c0254i0.c();
        }
        if (i3 != 0) {
            C0254i0 c0254i0B = androidx.core.view.Z.c(this).b(0.0f);
            c0254i0B.f(j3);
            c0254i0B.h(this.f4055b.d(c0254i0B, i3));
            return c0254i0B;
        }
        if (getVisibility() != 0) {
            setAlpha(0.0f);
        }
        C0254i0 c0254i0B2 = androidx.core.view.Z.c(this).b(1.0f);
        c0254i0B2.f(j3);
        c0254i0B2.h(this.f4055b.d(c0254i0B2, i3));
        return c0254i0B2;
    }

    public int getAnimatedVisibility() {
        return this.f4060g != null ? this.f4055b.f4064b : getVisibility();
    }

    public int getContentHeight() {
        return this.f4059f;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, d.j.f8952a, AbstractC0487a.f8675c, 0);
        setContentHeight(typedArrayObtainStyledAttributes.getLayoutDimension(d.j.f8993j, 0));
        typedArrayObtainStyledAttributes.recycle();
        C0214c c0214c = this.f4058e;
        if (c0214c != null) {
            c0214c.F(configuration);
        }
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.f4062i = false;
        }
        if (!this.f4062i) {
            boolean zOnHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !zOnHoverEvent) {
                this.f4062i = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.f4062i = false;
        }
        return true;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.f4061h = false;
        }
        if (!this.f4061h) {
            boolean zOnTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !zOnTouchEvent) {
                this.f4061h = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.f4061h = false;
        }
        return true;
    }

    public abstract void setContentHeight(int i3);

    @Override // android.view.View
    public void setVisibility(int i3) {
        if (i3 != getVisibility()) {
            C0254i0 c0254i0 = this.f4060g;
            if (c0254i0 != null) {
                c0254i0.c();
            }
            super.setVisibility(i3);
        }
    }
}
