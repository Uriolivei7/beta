package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;

/* JADX INFO: loaded from: classes.dex */
public class T extends ViewGroup {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f3915b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f3916c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f3917d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f3918e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f3919f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f3920g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private float f3921h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f3922i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int[] f3923j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int[] f3924k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private Drawable f3925l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f3926m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f3927n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private int f3928o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private int f3929p;

    public static class a extends LinearLayout.LayoutParams {
        public a(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public a(int i3, int i4) {
            super(i3, i4);
        }

        public a(int i3, int i4, float f3) {
            super(i3, i4, f3);
        }

        public a(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public a(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }
    }

    public T(Context context) {
        this(context, null);
    }

    private void i(int i3, int i4) {
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i5 = 0; i5 < i3; i5++) {
            View viewQ = q(i5);
            if (viewQ.getVisibility() != 8) {
                a aVar = (a) viewQ.getLayoutParams();
                if (((LinearLayout.LayoutParams) aVar).height == -1) {
                    int i6 = ((LinearLayout.LayoutParams) aVar).width;
                    ((LinearLayout.LayoutParams) aVar).width = viewQ.getMeasuredWidth();
                    measureChildWithMargins(viewQ, i4, 0, iMakeMeasureSpec, 0);
                    ((LinearLayout.LayoutParams) aVar).width = i6;
                }
            }
        }
    }

    private void j(int i3, int i4) {
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i5 = 0; i5 < i3; i5++) {
            View viewQ = q(i5);
            if (viewQ.getVisibility() != 8) {
                a aVar = (a) viewQ.getLayoutParams();
                if (((LinearLayout.LayoutParams) aVar).width == -1) {
                    int i6 = ((LinearLayout.LayoutParams) aVar).height;
                    ((LinearLayout.LayoutParams) aVar).height = viewQ.getMeasuredHeight();
                    measureChildWithMargins(viewQ, iMakeMeasureSpec, 0, i4, 0);
                    ((LinearLayout.LayoutParams) aVar).height = i6;
                }
            }
        }
    }

    private void y(View view, int i3, int i4, int i5, int i6) {
        view.layout(i3, i4, i5 + i3, i6 + i4);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof a;
    }

    void e(Canvas canvas) {
        int right;
        int left;
        int i3;
        int virtualChildCount = getVirtualChildCount();
        boolean zB = s0.b(this);
        for (int i4 = 0; i4 < virtualChildCount; i4++) {
            View viewQ = q(i4);
            if (viewQ != null && viewQ.getVisibility() != 8 && r(i4)) {
                a aVar = (a) viewQ.getLayoutParams();
                h(canvas, zB ? viewQ.getRight() + ((LinearLayout.LayoutParams) aVar).rightMargin : (viewQ.getLeft() - ((LinearLayout.LayoutParams) aVar).leftMargin) - this.f3926m);
            }
        }
        if (r(virtualChildCount)) {
            View viewQ2 = q(virtualChildCount - 1);
            if (viewQ2 != null) {
                a aVar2 = (a) viewQ2.getLayoutParams();
                if (zB) {
                    left = viewQ2.getLeft() - ((LinearLayout.LayoutParams) aVar2).leftMargin;
                    i3 = this.f3926m;
                    right = left - i3;
                } else {
                    right = viewQ2.getRight() + ((LinearLayout.LayoutParams) aVar2).rightMargin;
                }
            } else if (zB) {
                right = getPaddingLeft();
            } else {
                left = getWidth() - getPaddingRight();
                i3 = this.f3926m;
                right = left - i3;
            }
            h(canvas, right);
        }
    }

    void f(Canvas canvas) {
        int virtualChildCount = getVirtualChildCount();
        for (int i3 = 0; i3 < virtualChildCount; i3++) {
            View viewQ = q(i3);
            if (viewQ != null && viewQ.getVisibility() != 8 && r(i3)) {
                g(canvas, (viewQ.getTop() - ((LinearLayout.LayoutParams) ((a) viewQ.getLayoutParams())).topMargin) - this.f3927n);
            }
        }
        if (r(virtualChildCount)) {
            View viewQ2 = q(virtualChildCount - 1);
            g(canvas, viewQ2 == null ? (getHeight() - getPaddingBottom()) - this.f3927n : viewQ2.getBottom() + ((LinearLayout.LayoutParams) ((a) viewQ2.getLayoutParams())).bottomMargin);
        }
    }

    void g(Canvas canvas, int i3) {
        this.f3925l.setBounds(getPaddingLeft() + this.f3929p, i3, (getWidth() - getPaddingRight()) - this.f3929p, this.f3927n + i3);
        this.f3925l.draw(canvas);
    }

    @Override // android.view.View
    public int getBaseline() {
        int i3;
        if (this.f3916c < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i4 = this.f3916c;
        if (childCount <= i4) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
        View childAt = getChildAt(i4);
        int baseline = childAt.getBaseline();
        if (baseline == -1) {
            if (this.f3916c == 0) {
                return -1;
            }
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
        }
        int bottom = this.f3917d;
        if (this.f3918e == 1 && (i3 = this.f3919f & 112) != 48) {
            if (i3 == 16) {
                bottom += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.f3920g) / 2;
            } else if (i3 == 80) {
                bottom = ((getBottom() - getTop()) - getPaddingBottom()) - this.f3920g;
            }
        }
        return bottom + ((LinearLayout.LayoutParams) ((a) childAt.getLayoutParams())).topMargin + baseline;
    }

    public int getBaselineAlignedChildIndex() {
        return this.f3916c;
    }

    public Drawable getDividerDrawable() {
        return this.f3925l;
    }

    public int getDividerPadding() {
        return this.f3929p;
    }

    public int getDividerWidth() {
        return this.f3926m;
    }

    public int getGravity() {
        return this.f3919f;
    }

    public int getOrientation() {
        return this.f3918e;
    }

    public int getShowDividers() {
        return this.f3928o;
    }

    int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.f3921h;
    }

    void h(Canvas canvas, int i3) {
        this.f3925l.setBounds(i3, getPaddingTop() + this.f3929p, this.f3926m + i3, (getHeight() - getPaddingBottom()) - this.f3929p);
        this.f3925l.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* JADX INFO: renamed from: k, reason: merged with bridge method [inline-methods] */
    public a generateDefaultLayoutParams() {
        int i3 = this.f3918e;
        if (i3 == 0) {
            return new a(-2, -2);
        }
        if (i3 == 1) {
            return new a(-1, -2);
        }
        return null;
    }

    @Override // android.view.ViewGroup
    /* JADX INFO: renamed from: l, reason: merged with bridge method [inline-methods] */
    public a generateLayoutParams(AttributeSet attributeSet) {
        return new a(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* JADX INFO: renamed from: m, reason: merged with bridge method [inline-methods] */
    public a generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof a ? new a((ViewGroup.MarginLayoutParams) layoutParams) : layoutParams instanceof ViewGroup.MarginLayoutParams ? new a((ViewGroup.MarginLayoutParams) layoutParams) : new a(layoutParams);
    }

    int n(View view, int i3) {
        return 0;
    }

    int o(View view) {
        return 0;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.f3925l == null) {
            return;
        }
        if (this.f3918e == 1) {
            f(canvas);
        } else {
            e(canvas);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        if (this.f3918e == 1) {
            t(i3, i4, i5, i6);
        } else {
            s(i3, i4, i5, i6);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i3, int i4) {
        if (this.f3918e == 1) {
            x(i3, i4);
        } else {
            v(i3, i4);
        }
    }

    int p(View view) {
        return 0;
    }

    View q(int i3) {
        return getChildAt(i3);
    }

    protected boolean r(int i3) {
        if (i3 == 0) {
            return (this.f3928o & 1) != 0;
        }
        if (i3 == getChildCount()) {
            return (this.f3928o & 4) != 0;
        }
        if ((this.f3928o & 2) == 0) {
            return false;
        }
        for (int i4 = i3 - 1; i4 >= 0; i4--) {
            if (getChildAt(i4).getVisibility() != 8) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0100  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void s(int r25, int r26, int r27, int r28) {
        /*
            Method dump skipped, instruction units count: 331
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.T.s(int, int, int, int):void");
    }

    public void setBaselineAligned(boolean z3) {
        this.f3915b = z3;
    }

    public void setBaselineAlignedChildIndex(int i3) {
        if (i3 >= 0 && i3 < getChildCount()) {
            this.f3916c = i3;
            return;
        }
        throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable == this.f3925l) {
            return;
        }
        this.f3925l = drawable;
        if (drawable != null) {
            this.f3926m = drawable.getIntrinsicWidth();
            this.f3927n = drawable.getIntrinsicHeight();
        } else {
            this.f3926m = 0;
            this.f3927n = 0;
        }
        setWillNotDraw(drawable == null);
        requestLayout();
    }

    public void setDividerPadding(int i3) {
        this.f3929p = i3;
    }

    public void setGravity(int i3) {
        if (this.f3919f != i3) {
            if ((8388615 & i3) == 0) {
                i3 |= 8388611;
            }
            if ((i3 & 112) == 0) {
                i3 |= 48;
            }
            this.f3919f = i3;
            requestLayout();
        }
    }

    public void setHorizontalGravity(int i3) {
        int i4 = i3 & 8388615;
        int i5 = this.f3919f;
        if ((8388615 & i5) != i4) {
            this.f3919f = i4 | ((-8388616) & i5);
            requestLayout();
        }
    }

    public void setMeasureWithLargestChildEnabled(boolean z3) {
        this.f3922i = z3;
    }

    public void setOrientation(int i3) {
        if (this.f3918e != i3) {
            this.f3918e = i3;
            requestLayout();
        }
    }

    public void setShowDividers(int i3) {
        if (i3 != this.f3928o) {
            requestLayout();
        }
        this.f3928o = i3;
    }

    public void setVerticalGravity(int i3) {
        int i4 = i3 & 112;
        int i5 = this.f3919f;
        if ((i5 & 112) != i4) {
            this.f3919f = i4 | (i5 & (-113));
            requestLayout();
        }
    }

    public void setWeightSum(float f3) {
        this.f3921h = Math.max(0.0f, f3);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00a1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void t(int r18, int r19, int r20, int r21) {
        /*
            Method dump skipped, instruction units count: 204
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.T.t(int, int, int, int):void");
    }

    void u(View view, int i3, int i4, int i5, int i6, int i7) {
        measureChildWithMargins(view, i4, i5, i6, i7);
    }

    /* JADX WARN: Removed duplicated region for block: B:200:0x045b  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0175  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01c3  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01d9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void v(int r40, int r41) {
        /*
            Method dump skipped, instruction units count: 1293
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.T.v(int, int):void");
    }

    int w(int i3) {
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:152:0x032f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void x(int r34, int r35) {
        /*
            Method dump skipped, instruction units count: 910
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.T.x(int, int):void");
    }

    public T(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public T(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.f3915b = true;
        this.f3916c = -1;
        this.f3917d = 0;
        this.f3919f = 8388659;
        h0 h0VarU = h0.u(context, attributeSet, d.j.f8954a1, i3, 0);
        androidx.core.view.Z.V(this, context, d.j.f8954a1, attributeSet, h0VarU.q(), i3, 0);
        int iJ = h0VarU.j(d.j.f8964c1, -1);
        if (iJ >= 0) {
            setOrientation(iJ);
        }
        int iJ2 = h0VarU.j(d.j.f8959b1, -1);
        if (iJ2 >= 0) {
            setGravity(iJ2);
        }
        boolean zA = h0VarU.a(d.j.f8969d1, true);
        if (!zA) {
            setBaselineAligned(zA);
        }
        this.f3921h = h0VarU.h(d.j.f8979f1, -1.0f);
        this.f3916c = h0VarU.j(d.j.f8974e1, -1);
        this.f3922i = h0VarU.a(d.j.f8991i1, false);
        setDividerDrawable(h0VarU.f(d.j.f8983g1));
        this.f3928o = h0VarU.j(d.j.f8995j1, 0);
        this.f3929p = h0VarU.e(d.j.f8987h1, 0);
        h0VarU.w();
    }
}
