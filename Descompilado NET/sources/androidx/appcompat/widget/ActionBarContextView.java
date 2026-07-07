package androidx.appcompat.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.C0254i0;
import d.AbstractC0487a;

/* JADX INFO: loaded from: classes.dex */
public class ActionBarContextView extends AbstractC0212a {

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private CharSequence f3668j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private CharSequence f3669k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private View f3670l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private View f3671m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private View f3672n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private LinearLayout f3673o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private TextView f3674p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private TextView f3675q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f3676r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private int f3677s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private boolean f3678t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private int f3679u;

    class a implements View.OnClickListener {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ androidx.appcompat.view.b f3680b;

        a(androidx.appcompat.view.b bVar) {
            this.f3680b = bVar;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            this.f3680b.c();
        }
    }

    public ActionBarContextView(Context context) {
        this(context, null);
    }

    private void i() {
        if (this.f3673o == null) {
            LayoutInflater.from(getContext()).inflate(d.g.f8810a, this);
            LinearLayout linearLayout = (LinearLayout) getChildAt(getChildCount() - 1);
            this.f3673o = linearLayout;
            this.f3674p = (TextView) linearLayout.findViewById(d.f.f8788e);
            this.f3675q = (TextView) this.f3673o.findViewById(d.f.f8787d);
            if (this.f3676r != 0) {
                this.f3674p.setTextAppearance(getContext(), this.f3676r);
            }
            if (this.f3677s != 0) {
                this.f3675q.setTextAppearance(getContext(), this.f3677s);
            }
        }
        this.f3674p.setText(this.f3668j);
        this.f3675q.setText(this.f3669k);
        boolean zIsEmpty = TextUtils.isEmpty(this.f3668j);
        boolean zIsEmpty2 = TextUtils.isEmpty(this.f3669k);
        this.f3675q.setVisibility(!zIsEmpty2 ? 0 : 8);
        this.f3673o.setVisibility((zIsEmpty && zIsEmpty2) ? 8 : 0);
        if (this.f3673o.getParent() == null) {
            addView(this.f3673o);
        }
    }

    @Override // androidx.appcompat.widget.AbstractC0212a
    public /* bridge */ /* synthetic */ C0254i0 f(int i3, long j3) {
        return super.f(i3, j3);
    }

    public void g() {
        if (this.f3670l == null) {
            k();
        }
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(-1, -2);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ViewGroup.MarginLayoutParams(getContext(), attributeSet);
    }

    @Override // androidx.appcompat.widget.AbstractC0212a
    public /* bridge */ /* synthetic */ int getAnimatedVisibility() {
        return super.getAnimatedVisibility();
    }

    @Override // androidx.appcompat.widget.AbstractC0212a
    public /* bridge */ /* synthetic */ int getContentHeight() {
        return super.getContentHeight();
    }

    public CharSequence getSubtitle() {
        return this.f3669k;
    }

    public CharSequence getTitle() {
        return this.f3668j;
    }

    public void h(androidx.appcompat.view.b bVar) {
        View view = this.f3670l;
        if (view == null) {
            View viewInflate = LayoutInflater.from(getContext()).inflate(this.f3679u, (ViewGroup) this, false);
            this.f3670l = viewInflate;
            addView(viewInflate);
        } else if (view.getParent() == null) {
            addView(this.f3670l);
        }
        View viewFindViewById = this.f3670l.findViewById(d.f.f8792i);
        this.f3671m = viewFindViewById;
        viewFindViewById.setOnClickListener(new a(bVar));
        androidx.appcompat.view.menu.e eVar = (androidx.appcompat.view.menu.e) bVar.e();
        C0214c c0214c = this.f4058e;
        if (c0214c != null) {
            c0214c.y();
        }
        C0214c c0214c2 = new C0214c(getContext());
        this.f4058e = c0214c2;
        c0214c2.J(true);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -1);
        eVar.c(this.f4058e, this.f4056c);
        ActionMenuView actionMenuView = (ActionMenuView) this.f4058e.o(this);
        this.f4057d = actionMenuView;
        actionMenuView.setBackground(null);
        addView(this.f4057d, layoutParams);
    }

    public boolean j() {
        return this.f3678t;
    }

    public void k() {
        removeAllViews();
        this.f3672n = null;
        this.f4057d = null;
        this.f4058e = null;
        View view = this.f3671m;
        if (view != null) {
            view.setOnClickListener(null);
        }
    }

    public boolean l() {
        C0214c c0214c = this.f4058e;
        if (c0214c != null) {
            return c0214c.K();
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        C0214c c0214c = this.f4058e;
        if (c0214c != null) {
            c0214c.B();
            this.f4058e.C();
        }
    }

    @Override // androidx.appcompat.widget.AbstractC0212a, android.view.View
    public /* bridge */ /* synthetic */ boolean onHoverEvent(MotionEvent motionEvent) {
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        boolean zB = s0.b(this);
        int paddingRight = zB ? (i5 - i3) - getPaddingRight() : getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingTop2 = ((i6 - i4) - getPaddingTop()) - getPaddingBottom();
        View view = this.f3670l;
        if (view != null && view.getVisibility() != 8) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.f3670l.getLayoutParams();
            int i7 = zB ? marginLayoutParams.rightMargin : marginLayoutParams.leftMargin;
            int i8 = zB ? marginLayoutParams.leftMargin : marginLayoutParams.rightMargin;
            int iD = AbstractC0212a.d(paddingRight, i7, zB);
            paddingRight = AbstractC0212a.d(iD + e(this.f3670l, iD, paddingTop, paddingTop2, zB), i8, zB);
        }
        int iE = paddingRight;
        LinearLayout linearLayout = this.f3673o;
        if (linearLayout != null && this.f3672n == null && linearLayout.getVisibility() != 8) {
            iE += e(this.f3673o, iE, paddingTop, paddingTop2, zB);
        }
        int i9 = iE;
        View view2 = this.f3672n;
        if (view2 != null) {
            e(view2, i9, paddingTop, paddingTop2, zB);
        }
        int paddingLeft = zB ? getPaddingLeft() : (i5 - i3) - getPaddingRight();
        ActionMenuView actionMenuView = this.f4057d;
        if (actionMenuView != null) {
            e(actionMenuView, paddingLeft, paddingTop, paddingTop2, !zB);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i3, int i4) {
        if (View.MeasureSpec.getMode(i3) != 1073741824) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with android:layout_width=\"match_parent\" (or fill_parent)");
        }
        if (View.MeasureSpec.getMode(i4) == 0) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with android:layout_height=\"wrap_content\"");
        }
        int size = View.MeasureSpec.getSize(i3);
        int size2 = this.f4059f;
        if (size2 <= 0) {
            size2 = View.MeasureSpec.getSize(i4);
        }
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
        int iMin = size2 - paddingTop;
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(iMin, Integer.MIN_VALUE);
        View view = this.f3670l;
        if (view != null) {
            int iC = c(view, paddingLeft, iMakeMeasureSpec, 0);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.f3670l.getLayoutParams();
            paddingLeft = iC - (marginLayoutParams.leftMargin + marginLayoutParams.rightMargin);
        }
        ActionMenuView actionMenuView = this.f4057d;
        if (actionMenuView != null && actionMenuView.getParent() == this) {
            paddingLeft = c(this.f4057d, paddingLeft, iMakeMeasureSpec, 0);
        }
        LinearLayout linearLayout = this.f3673o;
        if (linearLayout != null && this.f3672n == null) {
            if (this.f3678t) {
                this.f3673o.measure(View.MeasureSpec.makeMeasureSpec(0, 0), iMakeMeasureSpec);
                int measuredWidth = this.f3673o.getMeasuredWidth();
                boolean z3 = measuredWidth <= paddingLeft;
                if (z3) {
                    paddingLeft -= measuredWidth;
                }
                this.f3673o.setVisibility(z3 ? 0 : 8);
            } else {
                paddingLeft = c(linearLayout, paddingLeft, iMakeMeasureSpec, 0);
            }
        }
        View view2 = this.f3672n;
        if (view2 != null) {
            ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
            int i5 = layoutParams.width;
            int i6 = i5 != -2 ? 1073741824 : Integer.MIN_VALUE;
            if (i5 >= 0) {
                paddingLeft = Math.min(i5, paddingLeft);
            }
            int i7 = layoutParams.height;
            int i8 = i7 == -2 ? Integer.MIN_VALUE : 1073741824;
            if (i7 >= 0) {
                iMin = Math.min(i7, iMin);
            }
            this.f3672n.measure(View.MeasureSpec.makeMeasureSpec(paddingLeft, i6), View.MeasureSpec.makeMeasureSpec(iMin, i8));
        }
        if (this.f4059f > 0) {
            setMeasuredDimension(size, size2);
            return;
        }
        int childCount = getChildCount();
        int i9 = 0;
        for (int i10 = 0; i10 < childCount; i10++) {
            int measuredHeight = getChildAt(i10).getMeasuredHeight() + paddingTop;
            if (measuredHeight > i9) {
                i9 = measuredHeight;
            }
        }
        setMeasuredDimension(size, i9);
    }

    @Override // androidx.appcompat.widget.AbstractC0212a, android.view.View
    public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    @Override // androidx.appcompat.widget.AbstractC0212a
    public void setContentHeight(int i3) {
        this.f4059f = i3;
    }

    public void setCustomView(View view) {
        LinearLayout linearLayout;
        View view2 = this.f3672n;
        if (view2 != null) {
            removeView(view2);
        }
        this.f3672n = view;
        if (view != null && (linearLayout = this.f3673o) != null) {
            removeView(linearLayout);
            this.f3673o = null;
        }
        if (view != null) {
            addView(view);
        }
        requestLayout();
    }

    public void setSubtitle(CharSequence charSequence) {
        this.f3669k = charSequence;
        i();
    }

    public void setTitle(CharSequence charSequence) {
        this.f3668j = charSequence;
        i();
        androidx.core.view.Z.a0(this, charSequence);
    }

    public void setTitleOptional(boolean z3) {
        if (z3 != this.f3678t) {
            requestLayout();
        }
        this.f3678t = z3;
    }

    @Override // androidx.appcompat.widget.AbstractC0212a, android.view.View
    public /* bridge */ /* synthetic */ void setVisibility(int i3) {
        super.setVisibility(i3);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8683k);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        h0 h0VarU = h0.u(context, attributeSet, d.j.f9053y, i3, 0);
        setBackground(h0VarU.f(d.j.f9057z));
        this.f3676r = h0VarU.m(d.j.f8860D, 0);
        this.f3677s = h0VarU.m(d.j.f8856C, 0);
        this.f4059f = h0VarU.l(d.j.f8852B, 0);
        this.f3679u = h0VarU.m(d.j.f8848A, d.g.f8813d);
        h0VarU.w();
    }
}
