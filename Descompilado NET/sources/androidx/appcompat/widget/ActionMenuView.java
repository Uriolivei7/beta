package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.e;
import androidx.appcompat.view.menu.j;
import androidx.appcompat.widget.T;

/* JADX INFO: loaded from: classes.dex */
public class ActionMenuView extends T implements e.b, androidx.appcompat.view.menu.k {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private int f3720A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    e f3721B;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private androidx.appcompat.view.menu.e f3722q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private Context f3723r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private int f3724s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private boolean f3725t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private C0214c f3726u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private j.a f3727v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    e.a f3728w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private boolean f3729x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private int f3730y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private int f3731z;

    public interface a {
        boolean b();

        boolean d();
    }

    private static class b implements j.a {
        b() {
        }

        @Override // androidx.appcompat.view.menu.j.a
        public void c(androidx.appcompat.view.menu.e eVar, boolean z3) {
        }

        @Override // androidx.appcompat.view.menu.j.a
        public boolean d(androidx.appcompat.view.menu.e eVar) {
            return false;
        }
    }

    public static class c extends T.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public boolean f3732a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public int f3733b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public int f3734c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public boolean f3735d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public boolean f3736e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        boolean f3737f;

        public c(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public c(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public c(c cVar) {
            super((ViewGroup.LayoutParams) cVar);
            this.f3732a = cVar.f3732a;
        }

        public c(int i3, int i4) {
            super(i3, i4);
            this.f3732a = false;
        }
    }

    private class d implements e.a {
        d() {
        }

        @Override // androidx.appcompat.view.menu.e.a
        public boolean a(androidx.appcompat.view.menu.e eVar, MenuItem menuItem) {
            e eVar2 = ActionMenuView.this.f3721B;
            return eVar2 != null && eVar2.onMenuItemClick(menuItem);
        }

        @Override // androidx.appcompat.view.menu.e.a
        public void b(androidx.appcompat.view.menu.e eVar) {
            e.a aVar = ActionMenuView.this.f3728w;
            if (aVar != null) {
                aVar.b(eVar);
            }
        }
    }

    public interface e {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public ActionMenuView(Context context) {
        this(context, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static int J(android.view.View r5, int r6, int r7, int r8, int r9) {
        /*
            android.view.ViewGroup$LayoutParams r0 = r5.getLayoutParams()
            androidx.appcompat.widget.ActionMenuView$c r0 = (androidx.appcompat.widget.ActionMenuView.c) r0
            int r1 = android.view.View.MeasureSpec.getSize(r8)
            int r1 = r1 - r9
            int r8 = android.view.View.MeasureSpec.getMode(r8)
            int r8 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r8)
            boolean r9 = r5 instanceof androidx.appcompat.view.menu.ActionMenuItemView
            if (r9 == 0) goto L1b
            r9 = r5
            androidx.appcompat.view.menu.ActionMenuItemView r9 = (androidx.appcompat.view.menu.ActionMenuItemView) r9
            goto L1c
        L1b:
            r9 = 0
        L1c:
            r1 = 0
            r2 = 1
            if (r9 == 0) goto L28
            boolean r9 = r9.t()
            if (r9 == 0) goto L28
            r9 = r2
            goto L29
        L28:
            r9 = r1
        L29:
            if (r7 <= 0) goto L4c
            r3 = 2
            if (r9 == 0) goto L30
            if (r7 < r3) goto L4c
        L30:
            int r7 = r7 * r6
            r4 = -2147483648(0xffffffff80000000, float:-0.0)
            int r7 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r4)
            r5.measure(r7, r8)
            int r7 = r5.getMeasuredWidth()
            int r4 = r7 / r6
            int r7 = r7 % r6
            if (r7 == 0) goto L45
            int r4 = r4 + 1
        L45:
            if (r9 == 0) goto L4a
            if (r4 >= r3) goto L4a
            goto L4d
        L4a:
            r3 = r4
            goto L4d
        L4c:
            r3 = r1
        L4d:
            boolean r7 = r0.f3732a
            if (r7 != 0) goto L54
            if (r9 == 0) goto L54
            r1 = r2
        L54:
            r0.f3735d = r1
            r0.f3733b = r3
            int r6 = r6 * r3
            r7 = 1073741824(0x40000000, float:2.0)
            int r6 = android.view.View.MeasureSpec.makeMeasureSpec(r6, r7)
            r5.measure(r6, r8)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ActionMenuView.J(android.view.View, int, int, int, int):int");
    }

    /* JADX WARN: Type inference failed for: r14v10 */
    /* JADX WARN: Type inference failed for: r14v11, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r14v14 */
    private void K(int i3, int i4) {
        int i5;
        int i6;
        boolean z3;
        int i7;
        int i8;
        boolean z4;
        boolean z5;
        int i9;
        ?? r14;
        int mode = View.MeasureSpec.getMode(i4);
        int size = View.MeasureSpec.getSize(i3);
        int size2 = View.MeasureSpec.getSize(i4);
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i4, paddingTop, -2);
        int i10 = size - paddingLeft;
        int i11 = this.f3731z;
        int i12 = i10 / i11;
        int i13 = i10 % i11;
        if (i12 == 0) {
            setMeasuredDimension(i10, 0);
            return;
        }
        int i14 = i11 + (i13 / i12);
        int childCount = getChildCount();
        int iMax = 0;
        int i15 = 0;
        boolean z6 = false;
        int i16 = 0;
        int iMax2 = 0;
        int i17 = 0;
        long j3 = 0;
        while (i15 < childCount) {
            View childAt = getChildAt(i15);
            int i18 = size2;
            if (childAt.getVisibility() != 8) {
                boolean z7 = childAt instanceof ActionMenuItemView;
                int i19 = i16 + 1;
                if (z7) {
                    int i20 = this.f3720A;
                    i9 = i19;
                    r14 = 0;
                    childAt.setPadding(i20, 0, i20, 0);
                } else {
                    i9 = i19;
                    r14 = 0;
                }
                c cVar = (c) childAt.getLayoutParams();
                cVar.f3737f = r14;
                cVar.f3734c = r14;
                cVar.f3733b = r14;
                cVar.f3735d = r14;
                ((LinearLayout.LayoutParams) cVar).leftMargin = r14;
                ((LinearLayout.LayoutParams) cVar).rightMargin = r14;
                cVar.f3736e = z7 && ((ActionMenuItemView) childAt).t();
                int iJ = J(childAt, i14, cVar.f3732a ? 1 : i12, childMeasureSpec, paddingTop);
                iMax2 = Math.max(iMax2, iJ);
                if (cVar.f3735d) {
                    i17++;
                }
                if (cVar.f3732a) {
                    z6 = true;
                }
                i12 -= iJ;
                iMax = Math.max(iMax, childAt.getMeasuredHeight());
                if (iJ == 1) {
                    j3 |= (long) (1 << i15);
                    iMax = iMax;
                }
                i16 = i9;
            }
            i15++;
            size2 = i18;
        }
        int i21 = size2;
        boolean z8 = z6 && i16 == 2;
        boolean z9 = false;
        while (i17 > 0 && i12 > 0) {
            int i22 = Integer.MAX_VALUE;
            int i23 = 0;
            int i24 = 0;
            long j4 = 0;
            while (i24 < childCount) {
                boolean z10 = z9;
                c cVar2 = (c) getChildAt(i24).getLayoutParams();
                int i25 = iMax;
                if (cVar2.f3735d) {
                    int i26 = cVar2.f3733b;
                    if (i26 < i22) {
                        j4 = 1 << i24;
                        i22 = i26;
                        i23 = 1;
                    } else if (i26 == i22) {
                        i23++;
                        j4 |= 1 << i24;
                    }
                }
                i24++;
                iMax = i25;
                z9 = z10;
            }
            z3 = z9;
            i7 = iMax;
            j3 |= j4;
            if (i23 > i12) {
                i5 = mode;
                i6 = i10;
                break;
            }
            int i27 = i22 + 1;
            int i28 = 0;
            while (i28 < childCount) {
                View childAt2 = getChildAt(i28);
                c cVar3 = (c) childAt2.getLayoutParams();
                int i29 = i10;
                int i30 = mode;
                long j5 = 1 << i28;
                if ((j4 & j5) == 0) {
                    if (cVar3.f3733b == i27) {
                        j3 |= j5;
                    }
                    z5 = z8;
                } else {
                    if (z8 && cVar3.f3736e && i12 == 1) {
                        int i31 = this.f3720A;
                        z5 = z8;
                        childAt2.setPadding(i31 + i14, 0, i31, 0);
                    } else {
                        z5 = z8;
                    }
                    cVar3.f3733b++;
                    cVar3.f3737f = true;
                    i12--;
                }
                i28++;
                mode = i30;
                i10 = i29;
                z8 = z5;
            }
            iMax = i7;
            z9 = true;
        }
        i5 = mode;
        i6 = i10;
        z3 = z9;
        i7 = iMax;
        boolean z11 = !z6 && i16 == 1;
        if (i12 <= 0 || j3 == 0 || (i12 >= i16 - 1 && !z11 && iMax2 <= 1)) {
            i8 = 0;
            z4 = z3;
        } else {
            float fBitCount = Long.bitCount(j3);
            if (z11) {
                i8 = 0;
            } else {
                i8 = 0;
                if ((j3 & 1) != 0 && !((c) getChildAt(0).getLayoutParams()).f3736e) {
                    fBitCount -= 0.5f;
                }
                int i32 = childCount - 1;
                if ((j3 & ((long) (1 << i32))) != 0 && !((c) getChildAt(i32).getLayoutParams()).f3736e) {
                    fBitCount -= 0.5f;
                }
            }
            int i33 = fBitCount > 0.0f ? (int) ((i12 * i14) / fBitCount) : i8;
            z4 = z3;
            for (int i34 = i8; i34 < childCount; i34++) {
                if ((j3 & ((long) (1 << i34))) != 0) {
                    View childAt3 = getChildAt(i34);
                    c cVar4 = (c) childAt3.getLayoutParams();
                    if (childAt3 instanceof ActionMenuItemView) {
                        cVar4.f3734c = i33;
                        cVar4.f3737f = true;
                        if (i34 == 0 && !cVar4.f3736e) {
                            ((LinearLayout.LayoutParams) cVar4).leftMargin = (-i33) / 2;
                        }
                        z4 = true;
                    } else if (cVar4.f3732a) {
                        cVar4.f3734c = i33;
                        cVar4.f3737f = true;
                        ((LinearLayout.LayoutParams) cVar4).rightMargin = (-i33) / 2;
                        z4 = true;
                    } else {
                        if (i34 != 0) {
                            ((LinearLayout.LayoutParams) cVar4).leftMargin = i33 / 2;
                        }
                        if (i34 != childCount - 1) {
                            ((LinearLayout.LayoutParams) cVar4).rightMargin = i33 / 2;
                        }
                    }
                }
            }
        }
        if (z4) {
            for (int i35 = i8; i35 < childCount; i35++) {
                View childAt4 = getChildAt(i35);
                c cVar5 = (c) childAt4.getLayoutParams();
                if (cVar5.f3737f) {
                    childAt4.measure(View.MeasureSpec.makeMeasureSpec((cVar5.f3733b * i14) + cVar5.f3734c, 1073741824), childMeasureSpec);
                }
            }
        }
        setMeasuredDimension(i6, i5 != 1073741824 ? i7 : i21);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.T
    /* JADX INFO: renamed from: A, reason: merged with bridge method [inline-methods] */
    public c generateDefaultLayoutParams() {
        c cVar = new c(-2, -2);
        ((LinearLayout.LayoutParams) cVar).gravity = 16;
        return cVar;
    }

    @Override // androidx.appcompat.widget.T
    /* JADX INFO: renamed from: B, reason: merged with bridge method [inline-methods] */
    public c generateLayoutParams(AttributeSet attributeSet) {
        return new c(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.T
    /* JADX INFO: renamed from: C, reason: merged with bridge method [inline-methods] */
    public c generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams == null) {
            return generateDefaultLayoutParams();
        }
        c cVar = layoutParams instanceof c ? new c((c) layoutParams) : new c(layoutParams);
        if (((LinearLayout.LayoutParams) cVar).gravity <= 0) {
            ((LinearLayout.LayoutParams) cVar).gravity = 16;
        }
        return cVar;
    }

    public c D() {
        c cVarGenerateDefaultLayoutParams = generateDefaultLayoutParams();
        cVarGenerateDefaultLayoutParams.f3732a = true;
        return cVarGenerateDefaultLayoutParams;
    }

    protected boolean E(int i3) {
        boolean zB = false;
        if (i3 == 0) {
            return false;
        }
        KeyEvent.Callback childAt = getChildAt(i3 - 1);
        KeyEvent.Callback childAt2 = getChildAt(i3);
        if (i3 < getChildCount() && (childAt instanceof a)) {
            zB = ((a) childAt).b();
        }
        return (i3 <= 0 || !(childAt2 instanceof a)) ? zB : zB | ((a) childAt2).d();
    }

    public boolean F() {
        C0214c c0214c = this.f3726u;
        return c0214c != null && c0214c.B();
    }

    public boolean G() {
        C0214c c0214c = this.f3726u;
        return c0214c != null && c0214c.D();
    }

    public boolean H() {
        C0214c c0214c = this.f3726u;
        return c0214c != null && c0214c.E();
    }

    public boolean I() {
        return this.f3725t;
    }

    public androidx.appcompat.view.menu.e L() {
        return this.f3722q;
    }

    public void M(j.a aVar, e.a aVar2) {
        this.f3727v = aVar;
        this.f3728w = aVar2;
    }

    public boolean N() {
        C0214c c0214c = this.f3726u;
        return c0214c != null && c0214c.K();
    }

    @Override // androidx.appcompat.view.menu.e.b
    public boolean a(androidx.appcompat.view.menu.g gVar) {
        return this.f3722q.M(gVar, 0);
    }

    @Override // androidx.appcompat.view.menu.k
    public void b(androidx.appcompat.view.menu.e eVar) {
        this.f3722q = eVar;
    }

    @Override // androidx.appcompat.widget.T, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof c;
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    public Menu getMenu() {
        if (this.f3722q == null) {
            Context context = getContext();
            androidx.appcompat.view.menu.e eVar = new androidx.appcompat.view.menu.e(context);
            this.f3722q = eVar;
            eVar.S(new d());
            C0214c c0214c = new C0214c(context);
            this.f3726u = c0214c;
            c0214c.J(true);
            C0214c c0214c2 = this.f3726u;
            j.a bVar = this.f3727v;
            if (bVar == null) {
                bVar = new b();
            }
            c0214c2.k(bVar);
            this.f3722q.c(this.f3726u, this.f3723r);
            this.f3726u.H(this);
        }
        return this.f3722q;
    }

    public Drawable getOverflowIcon() {
        getMenu();
        return this.f3726u.A();
    }

    public int getPopupTheme() {
        return this.f3724s;
    }

    public int getWindowAnimations() {
        return 0;
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        C0214c c0214c = this.f3726u;
        if (c0214c != null) {
            c0214c.f(false);
            if (this.f3726u.E()) {
                this.f3726u.B();
                this.f3726u.K();
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        z();
    }

    @Override // androidx.appcompat.widget.T, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        int width;
        int paddingLeft;
        if (!this.f3729x) {
            super.onLayout(z3, i3, i4, i5, i6);
            return;
        }
        int childCount = getChildCount();
        int i7 = (i6 - i4) / 2;
        int dividerWidth = getDividerWidth();
        int i8 = i5 - i3;
        int paddingRight = (i8 - getPaddingRight()) - getPaddingLeft();
        boolean zB = s0.b(this);
        int i9 = 0;
        int i10 = 0;
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = getChildAt(i11);
            if (childAt.getVisibility() != 8) {
                c cVar = (c) childAt.getLayoutParams();
                if (cVar.f3732a) {
                    int measuredWidth = childAt.getMeasuredWidth();
                    if (E(i11)) {
                        measuredWidth += dividerWidth;
                    }
                    int measuredHeight = childAt.getMeasuredHeight();
                    if (zB) {
                        paddingLeft = getPaddingLeft() + ((LinearLayout.LayoutParams) cVar).leftMargin;
                        width = paddingLeft + measuredWidth;
                    } else {
                        width = (getWidth() - getPaddingRight()) - ((LinearLayout.LayoutParams) cVar).rightMargin;
                        paddingLeft = width - measuredWidth;
                    }
                    int i12 = i7 - (measuredHeight / 2);
                    childAt.layout(paddingLeft, i12, width, measuredHeight + i12);
                    paddingRight -= measuredWidth;
                    i9 = 1;
                } else {
                    paddingRight -= (childAt.getMeasuredWidth() + ((LinearLayout.LayoutParams) cVar).leftMargin) + ((LinearLayout.LayoutParams) cVar).rightMargin;
                    E(i11);
                    i10++;
                }
            }
        }
        if (childCount == 1 && i9 == 0) {
            View childAt2 = getChildAt(0);
            int measuredWidth2 = childAt2.getMeasuredWidth();
            int measuredHeight2 = childAt2.getMeasuredHeight();
            int i13 = (i8 / 2) - (measuredWidth2 / 2);
            int i14 = i7 - (measuredHeight2 / 2);
            childAt2.layout(i13, i14, measuredWidth2 + i13, measuredHeight2 + i14);
            return;
        }
        int i15 = i10 - (i9 ^ 1);
        int iMax = Math.max(0, i15 > 0 ? paddingRight / i15 : 0);
        if (zB) {
            int width2 = getWidth() - getPaddingRight();
            for (int i16 = 0; i16 < childCount; i16++) {
                View childAt3 = getChildAt(i16);
                c cVar2 = (c) childAt3.getLayoutParams();
                if (childAt3.getVisibility() != 8 && !cVar2.f3732a) {
                    int i17 = width2 - ((LinearLayout.LayoutParams) cVar2).rightMargin;
                    int measuredWidth3 = childAt3.getMeasuredWidth();
                    int measuredHeight3 = childAt3.getMeasuredHeight();
                    int i18 = i7 - (measuredHeight3 / 2);
                    childAt3.layout(i17 - measuredWidth3, i18, i17, measuredHeight3 + i18);
                    width2 = i17 - ((measuredWidth3 + ((LinearLayout.LayoutParams) cVar2).leftMargin) + iMax);
                }
            }
            return;
        }
        int paddingLeft2 = getPaddingLeft();
        for (int i19 = 0; i19 < childCount; i19++) {
            View childAt4 = getChildAt(i19);
            c cVar3 = (c) childAt4.getLayoutParams();
            if (childAt4.getVisibility() != 8 && !cVar3.f3732a) {
                int i20 = paddingLeft2 + ((LinearLayout.LayoutParams) cVar3).leftMargin;
                int measuredWidth4 = childAt4.getMeasuredWidth();
                int measuredHeight4 = childAt4.getMeasuredHeight();
                int i21 = i7 - (measuredHeight4 / 2);
                childAt4.layout(i20, i21, i20 + measuredWidth4, measuredHeight4 + i21);
                paddingLeft2 = i20 + measuredWidth4 + ((LinearLayout.LayoutParams) cVar3).rightMargin + iMax;
            }
        }
    }

    @Override // androidx.appcompat.widget.T, android.view.View
    protected void onMeasure(int i3, int i4) {
        androidx.appcompat.view.menu.e eVar;
        boolean z3 = this.f3729x;
        boolean z4 = View.MeasureSpec.getMode(i3) == 1073741824;
        this.f3729x = z4;
        if (z3 != z4) {
            this.f3730y = 0;
        }
        int size = View.MeasureSpec.getSize(i3);
        if (this.f3729x && (eVar = this.f3722q) != null && size != this.f3730y) {
            this.f3730y = size;
            eVar.L(true);
        }
        int childCount = getChildCount();
        if (this.f3729x && childCount > 0) {
            K(i3, i4);
            return;
        }
        for (int i5 = 0; i5 < childCount; i5++) {
            c cVar = (c) getChildAt(i5).getLayoutParams();
            ((LinearLayout.LayoutParams) cVar).rightMargin = 0;
            ((LinearLayout.LayoutParams) cVar).leftMargin = 0;
        }
        super.onMeasure(i3, i4);
    }

    public void setExpandedActionViewsExclusive(boolean z3) {
        this.f3726u.G(z3);
    }

    public void setOnMenuItemClickListener(e eVar) {
        this.f3721B = eVar;
    }

    public void setOverflowIcon(Drawable drawable) {
        getMenu();
        this.f3726u.I(drawable);
    }

    public void setOverflowReserved(boolean z3) {
        this.f3725t = z3;
    }

    public void setPopupTheme(int i3) {
        if (this.f3724s != i3) {
            this.f3724s = i3;
            if (i3 == 0) {
                this.f3723r = getContext();
            } else {
                this.f3723r = new ContextThemeWrapper(getContext(), i3);
            }
        }
    }

    public void setPresenter(C0214c c0214c) {
        this.f3726u = c0214c;
        c0214c.H(this);
    }

    public void z() {
        C0214c c0214c = this.f3726u;
        if (c0214c != null) {
            c0214c.y();
        }
    }

    public ActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBaselineAligned(false);
        float f3 = context.getResources().getDisplayMetrics().density;
        this.f3731z = (int) (56.0f * f3);
        this.f3720A = (int) (f3 * 4.0f);
        this.f3723r = context;
        this.f3724s = 0;
    }
}
