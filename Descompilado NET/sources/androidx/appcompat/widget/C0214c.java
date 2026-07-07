package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.j;
import androidx.appcompat.view.menu.k;
import androidx.appcompat.widget.ActionMenuView;
import androidx.core.view.AbstractC0239b;
import d.AbstractC0487a;
import java.util.ArrayList;

/* JADX INFO: renamed from: androidx.appcompat.widget.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0214c extends androidx.appcompat.view.menu.a implements AbstractC0239b.a {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    a f4111A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    RunnableC0055c f4112B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private b f4113C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    final f f4114D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    int f4115E;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    d f4116l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private Drawable f4117m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f4118n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private boolean f4119o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private boolean f4120p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private int f4121q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f4122r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private int f4123s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private boolean f4124t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private boolean f4125u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private boolean f4126v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private boolean f4127w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private int f4128x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private final SparseBooleanArray f4129y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    e f4130z;

    /* JADX INFO: renamed from: androidx.appcompat.widget.c$a */
    private class a extends androidx.appcompat.view.menu.i {
        public a(Context context, androidx.appcompat.view.menu.m mVar, View view) {
            super(context, mVar, view, false, AbstractC0487a.f8685m);
            if (!((androidx.appcompat.view.menu.g) mVar.getItem()).l()) {
                View view2 = C0214c.this.f4116l;
                f(view2 == null ? (View) ((androidx.appcompat.view.menu.a) C0214c.this).f3471j : view2);
            }
            j(C0214c.this.f4114D);
        }

        @Override // androidx.appcompat.view.menu.i
        protected void e() {
            C0214c c0214c = C0214c.this;
            c0214c.f4111A = null;
            c0214c.f4115E = 0;
            super.e();
        }
    }

    /* JADX INFO: renamed from: androidx.appcompat.widget.c$b */
    private class b extends ActionMenuItemView.b {
        b() {
        }

        @Override // androidx.appcompat.view.menu.ActionMenuItemView.b
        public i.e a() {
            a aVar = C0214c.this.f4111A;
            if (aVar != null) {
                return aVar.c();
            }
            return null;
        }
    }

    /* JADX INFO: renamed from: androidx.appcompat.widget.c$c, reason: collision with other inner class name */
    private class RunnableC0055c implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private e f4133b;

        public RunnableC0055c(e eVar) {
            this.f4133b = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (((androidx.appcompat.view.menu.a) C0214c.this).f3465d != null) {
                ((androidx.appcompat.view.menu.a) C0214c.this).f3465d.d();
            }
            View view = (View) ((androidx.appcompat.view.menu.a) C0214c.this).f3471j;
            if (view != null && view.getWindowToken() != null && this.f4133b.m()) {
                C0214c.this.f4130z = this.f4133b;
            }
            C0214c.this.f4112B = null;
        }
    }

    /* JADX INFO: renamed from: androidx.appcompat.widget.c$d */
    private class d extends r implements ActionMenuView.a {

        /* JADX INFO: renamed from: androidx.appcompat.widget.c$d$a */
        class a extends S {

            /* JADX INFO: renamed from: k, reason: collision with root package name */
            final /* synthetic */ C0214c f4136k;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(View view, C0214c c0214c) {
                super(view);
                this.f4136k = c0214c;
            }

            @Override // androidx.appcompat.widget.S
            public i.e b() {
                e eVar = C0214c.this.f4130z;
                if (eVar == null) {
                    return null;
                }
                return eVar.c();
            }

            @Override // androidx.appcompat.widget.S
            public boolean c() {
                C0214c.this.K();
                return true;
            }

            @Override // androidx.appcompat.widget.S
            public boolean d() {
                C0214c c0214c = C0214c.this;
                if (c0214c.f4112B != null) {
                    return false;
                }
                c0214c.B();
                return true;
            }
        }

        public d(Context context) {
            super(context, null, AbstractC0487a.f8684l);
            setClickable(true);
            setFocusable(true);
            setVisibility(0);
            setEnabled(true);
            m0.a(this, getContentDescription());
            setOnTouchListener(new a(this, C0214c.this));
        }

        @Override // androidx.appcompat.widget.ActionMenuView.a
        public boolean b() {
            return false;
        }

        @Override // androidx.appcompat.widget.ActionMenuView.a
        public boolean d() {
            return false;
        }

        @Override // android.view.View
        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            playSoundEffect(0);
            C0214c.this.K();
            return true;
        }

        @Override // android.widget.ImageView
        protected boolean setFrame(int i3, int i4, int i5, int i6) {
            boolean frame = super.setFrame(i3, i4, i5, i6);
            Drawable drawable = getDrawable();
            Drawable background = getBackground();
            if (drawable != null && background != null) {
                int width = getWidth();
                int height = getHeight();
                int iMax = Math.max(width, height) / 2;
                int paddingLeft = (width + (getPaddingLeft() - getPaddingRight())) / 2;
                int paddingTop = (height + (getPaddingTop() - getPaddingBottom())) / 2;
                androidx.core.graphics.drawable.a.d(background, paddingLeft - iMax, paddingTop - iMax, paddingLeft + iMax, paddingTop + iMax);
            }
            return frame;
        }
    }

    /* JADX INFO: renamed from: androidx.appcompat.widget.c$e */
    private class e extends androidx.appcompat.view.menu.i {
        public e(Context context, androidx.appcompat.view.menu.e eVar, View view, boolean z3) {
            super(context, eVar, view, z3, AbstractC0487a.f8685m);
            h(8388613);
            j(C0214c.this.f4114D);
        }

        @Override // androidx.appcompat.view.menu.i
        protected void e() {
            if (((androidx.appcompat.view.menu.a) C0214c.this).f3465d != null) {
                ((androidx.appcompat.view.menu.a) C0214c.this).f3465d.close();
            }
            C0214c.this.f4130z = null;
            super.e();
        }
    }

    /* JADX INFO: renamed from: androidx.appcompat.widget.c$f */
    private class f implements j.a {
        f() {
        }

        @Override // androidx.appcompat.view.menu.j.a
        public void c(androidx.appcompat.view.menu.e eVar, boolean z3) {
            if (eVar instanceof androidx.appcompat.view.menu.m) {
                eVar.D().e(false);
            }
            j.a aVarM = C0214c.this.m();
            if (aVarM != null) {
                aVarM.c(eVar, z3);
            }
        }

        @Override // androidx.appcompat.view.menu.j.a
        public boolean d(androidx.appcompat.view.menu.e eVar) {
            if (eVar == ((androidx.appcompat.view.menu.a) C0214c.this).f3465d) {
                return false;
            }
            C0214c.this.f4115E = ((androidx.appcompat.view.menu.m) eVar).getItem().getItemId();
            j.a aVarM = C0214c.this.m();
            if (aVarM != null) {
                return aVarM.d(eVar);
            }
            return false;
        }
    }

    public C0214c(Context context) {
        super(context, d.g.f8812c, d.g.f8811b);
        this.f4129y = new SparseBooleanArray();
        this.f4114D = new f();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private View z(MenuItem menuItem) {
        ViewGroup viewGroup = (ViewGroup) this.f3471j;
        if (viewGroup == null) {
            return null;
        }
        int childCount = viewGroup.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = viewGroup.getChildAt(i3);
            if ((childAt instanceof k.a) && ((k.a) childAt).getItemData() == menuItem) {
                return childAt;
            }
        }
        return null;
    }

    public Drawable A() {
        d dVar = this.f4116l;
        if (dVar != null) {
            return dVar.getDrawable();
        }
        if (this.f4118n) {
            return this.f4117m;
        }
        return null;
    }

    public boolean B() {
        Object obj;
        RunnableC0055c runnableC0055c = this.f4112B;
        if (runnableC0055c != null && (obj = this.f3471j) != null) {
            ((View) obj).removeCallbacks(runnableC0055c);
            this.f4112B = null;
            return true;
        }
        e eVar = this.f4130z;
        if (eVar == null) {
            return false;
        }
        eVar.b();
        return true;
    }

    public boolean C() {
        a aVar = this.f4111A;
        if (aVar == null) {
            return false;
        }
        aVar.b();
        return true;
    }

    public boolean D() {
        return this.f4112B != null || E();
    }

    public boolean E() {
        e eVar = this.f4130z;
        return eVar != null && eVar.d();
    }

    public void F(Configuration configuration) {
        if (!this.f4124t) {
            this.f4123s = androidx.appcompat.view.a.b(this.f3464c).d();
        }
        androidx.appcompat.view.menu.e eVar = this.f3465d;
        if (eVar != null) {
            eVar.L(true);
        }
    }

    public void G(boolean z3) {
        this.f4127w = z3;
    }

    public void H(ActionMenuView actionMenuView) {
        this.f3471j = actionMenuView;
        actionMenuView.b(this.f3465d);
    }

    public void I(Drawable drawable) {
        d dVar = this.f4116l;
        if (dVar != null) {
            dVar.setImageDrawable(drawable);
        } else {
            this.f4118n = true;
            this.f4117m = drawable;
        }
    }

    public void J(boolean z3) {
        this.f4119o = z3;
        this.f4120p = true;
    }

    public boolean K() {
        androidx.appcompat.view.menu.e eVar;
        if (!this.f4119o || E() || (eVar = this.f3465d) == null || this.f3471j == null || this.f4112B != null || eVar.z().isEmpty()) {
            return false;
        }
        RunnableC0055c runnableC0055c = new RunnableC0055c(new e(this.f3464c, this.f3465d, this.f4116l, true));
        this.f4112B = runnableC0055c;
        ((View) this.f3471j).post(runnableC0055c);
        return true;
    }

    @Override // androidx.appcompat.view.menu.a
    public void b(androidx.appcompat.view.menu.g gVar, k.a aVar) {
        aVar.e(gVar, 0);
        ActionMenuItemView actionMenuItemView = (ActionMenuItemView) aVar;
        actionMenuItemView.setItemInvoker((ActionMenuView) this.f3471j);
        if (this.f4113C == null) {
            this.f4113C = new b();
        }
        actionMenuItemView.setPopupCallback(this.f4113C);
    }

    @Override // androidx.appcompat.view.menu.a, androidx.appcompat.view.menu.j
    public void c(androidx.appcompat.view.menu.e eVar, boolean z3) {
        y();
        super.c(eVar, z3);
    }

    @Override // androidx.appcompat.view.menu.a, androidx.appcompat.view.menu.j
    public void d(Context context, androidx.appcompat.view.menu.e eVar) {
        super.d(context, eVar);
        Resources resources = context.getResources();
        androidx.appcompat.view.a aVarB = androidx.appcompat.view.a.b(context);
        if (!this.f4120p) {
            this.f4119o = aVarB.h();
        }
        if (!this.f4126v) {
            this.f4121q = aVarB.c();
        }
        if (!this.f4124t) {
            this.f4123s = aVarB.d();
        }
        int measuredWidth = this.f4121q;
        if (this.f4119o) {
            if (this.f4116l == null) {
                d dVar = new d(this.f3463b);
                this.f4116l = dVar;
                if (this.f4118n) {
                    dVar.setImageDrawable(this.f4117m);
                    this.f4117m = null;
                    this.f4118n = false;
                }
                int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                this.f4116l.measure(iMakeMeasureSpec, iMakeMeasureSpec);
            }
            measuredWidth -= this.f4116l.getMeasuredWidth();
        } else {
            this.f4116l = null;
        }
        this.f4122r = measuredWidth;
        this.f4128x = (int) (resources.getDisplayMetrics().density * 56.0f);
    }

    @Override // androidx.appcompat.view.menu.a, androidx.appcompat.view.menu.j
    public boolean e(androidx.appcompat.view.menu.m mVar) {
        boolean z3 = false;
        if (!mVar.hasVisibleItems()) {
            return false;
        }
        androidx.appcompat.view.menu.m mVar2 = mVar;
        while (mVar2.f0() != this.f3465d) {
            mVar2 = (androidx.appcompat.view.menu.m) mVar2.f0();
        }
        View viewZ = z(mVar2.getItem());
        if (viewZ == null) {
            return false;
        }
        this.f4115E = mVar.getItem().getItemId();
        int size = mVar.size();
        int i3 = 0;
        while (true) {
            if (i3 >= size) {
                break;
            }
            MenuItem item = mVar.getItem(i3);
            if (item.isVisible() && item.getIcon() != null) {
                z3 = true;
                break;
            }
            i3++;
        }
        a aVar = new a(this.f3464c, mVar, viewZ);
        this.f4111A = aVar;
        aVar.g(z3);
        this.f4111A.k();
        super.e(mVar);
        return true;
    }

    @Override // androidx.appcompat.view.menu.a, androidx.appcompat.view.menu.j
    public void f(boolean z3) {
        super.f(z3);
        ((View) this.f3471j).requestLayout();
        androidx.appcompat.view.menu.e eVar = this.f3465d;
        boolean z4 = false;
        if (eVar != null) {
            ArrayList arrayListS = eVar.s();
            int size = arrayListS.size();
            for (int i3 = 0; i3 < size; i3++) {
                AbstractC0239b abstractC0239bB = ((androidx.appcompat.view.menu.g) arrayListS.get(i3)).b();
                if (abstractC0239bB != null) {
                    abstractC0239bB.i(this);
                }
            }
        }
        androidx.appcompat.view.menu.e eVar2 = this.f3465d;
        ArrayList arrayListZ = eVar2 != null ? eVar2.z() : null;
        if (this.f4119o && arrayListZ != null) {
            int size2 = arrayListZ.size();
            if (size2 == 1) {
                z4 = !((androidx.appcompat.view.menu.g) arrayListZ.get(0)).isActionViewExpanded();
            } else if (size2 > 0) {
                z4 = true;
            }
        }
        if (z4) {
            if (this.f4116l == null) {
                this.f4116l = new d(this.f3463b);
            }
            ViewGroup viewGroup = (ViewGroup) this.f4116l.getParent();
            if (viewGroup != this.f3471j) {
                if (viewGroup != null) {
                    viewGroup.removeView(this.f4116l);
                }
                ActionMenuView actionMenuView = (ActionMenuView) this.f3471j;
                actionMenuView.addView(this.f4116l, actionMenuView.D());
            }
        } else {
            d dVar = this.f4116l;
            if (dVar != null) {
                Object parent = dVar.getParent();
                Object obj = this.f3471j;
                if (parent == obj) {
                    ((ViewGroup) obj).removeView(this.f4116l);
                }
            }
        }
        ((ActionMenuView) this.f3471j).setOverflowReserved(this.f4119o);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [androidx.appcompat.widget.c] */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference failed for: r15v1, types: [androidx.appcompat.view.menu.g] */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v1, types: [int] */
    /* JADX WARN: Type inference failed for: r3v12 */
    @Override // androidx.appcompat.view.menu.a, androidx.appcompat.view.menu.j
    public boolean h() {
        ArrayList arrayListE;
        int size;
        int i3;
        int iJ;
        ?? r02;
        int i4;
        C0214c c0214c = this;
        androidx.appcompat.view.menu.e eVar = c0214c.f3465d;
        View view = null;
        ?? r3 = 0;
        if (eVar != null) {
            arrayListE = eVar.E();
            size = arrayListE.size();
        } else {
            arrayListE = null;
            size = 0;
        }
        int i5 = c0214c.f4123s;
        int i6 = c0214c.f4122r;
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        ViewGroup viewGroup = (ViewGroup) c0214c.f3471j;
        boolean z3 = false;
        int i7 = 0;
        int i8 = 0;
        for (int i9 = 0; i9 < size; i9++) {
            androidx.appcompat.view.menu.g gVar = (androidx.appcompat.view.menu.g) arrayListE.get(i9);
            if (gVar.o()) {
                i7++;
            } else if (gVar.n()) {
                i8++;
            } else {
                z3 = true;
            }
            if (c0214c.f4127w && gVar.isActionViewExpanded()) {
                i5 = 0;
            }
        }
        if (c0214c.f4119o && (z3 || i8 + i7 > i5)) {
            i5--;
        }
        int i10 = i5 - i7;
        SparseBooleanArray sparseBooleanArray = c0214c.f4129y;
        sparseBooleanArray.clear();
        if (c0214c.f4125u) {
            int i11 = c0214c.f4128x;
            iJ = i6 / i11;
            i3 = i11 + ((i6 % i11) / iJ);
        } else {
            i3 = 0;
            iJ = 0;
        }
        int i12 = 0;
        int i13 = 0;
        ?? r03 = c0214c;
        while (i12 < size) {
            ?? r15 = (androidx.appcompat.view.menu.g) arrayListE.get(i12);
            if (r15.o()) {
                View viewN = r03.n(r15, view, viewGroup);
                if (r03.f4125u) {
                    iJ -= ActionMenuView.J(viewN, i3, iJ, iMakeMeasureSpec, r3);
                } else {
                    viewN.measure(iMakeMeasureSpec, iMakeMeasureSpec);
                }
                int measuredWidth = viewN.getMeasuredWidth();
                i6 -= measuredWidth;
                if (i13 == 0) {
                    i13 = measuredWidth;
                }
                int groupId = r15.getGroupId();
                if (groupId != 0) {
                    sparseBooleanArray.put(groupId, true);
                }
                r15.u(true);
                r02 = r3;
                i4 = size;
            } else if (r15.n()) {
                int groupId2 = r15.getGroupId();
                boolean z4 = sparseBooleanArray.get(groupId2);
                boolean z5 = (i10 > 0 || z4) && i6 > 0 && (!r03.f4125u || iJ > 0);
                boolean z6 = z5;
                i4 = size;
                if (z5) {
                    View viewN2 = r03.n(r15, null, viewGroup);
                    if (r03.f4125u) {
                        int iJ2 = ActionMenuView.J(viewN2, i3, iJ, iMakeMeasureSpec, 0);
                        iJ -= iJ2;
                        if (iJ2 == 0) {
                            z6 = false;
                        }
                    } else {
                        viewN2.measure(iMakeMeasureSpec, iMakeMeasureSpec);
                    }
                    boolean z7 = z6;
                    int measuredWidth2 = viewN2.getMeasuredWidth();
                    i6 -= measuredWidth2;
                    if (i13 == 0) {
                        i13 = measuredWidth2;
                    }
                    z5 = z7 & (!r03.f4125u ? i6 + i13 <= 0 : i6 < 0);
                }
                if (z5 && groupId2 != 0) {
                    sparseBooleanArray.put(groupId2, true);
                } else if (z4) {
                    sparseBooleanArray.put(groupId2, false);
                    for (int i14 = 0; i14 < i12; i14++) {
                        androidx.appcompat.view.menu.g gVar2 = (androidx.appcompat.view.menu.g) arrayListE.get(i14);
                        if (gVar2.getGroupId() == groupId2) {
                            if (gVar2.l()) {
                                i10++;
                            }
                            gVar2.u(false);
                        }
                    }
                }
                if (z5) {
                    i10--;
                }
                r15.u(z5);
                r02 = 0;
            } else {
                r02 = r3;
                i4 = size;
                r15.u(r02);
            }
            i12++;
            r3 = r02;
            size = i4;
            view = null;
            r03 = this;
        }
        return true;
    }

    @Override // androidx.appcompat.view.menu.a
    public boolean l(ViewGroup viewGroup, int i3) {
        if (viewGroup.getChildAt(i3) == this.f4116l) {
            return false;
        }
        return super.l(viewGroup, i3);
    }

    @Override // androidx.appcompat.view.menu.a
    public View n(androidx.appcompat.view.menu.g gVar, View view, ViewGroup viewGroup) {
        View actionView = gVar.getActionView();
        if (actionView == null || gVar.j()) {
            actionView = super.n(gVar, view, viewGroup);
        }
        actionView.setVisibility(gVar.isActionViewExpanded() ? 8 : 0);
        ActionMenuView actionMenuView = (ActionMenuView) viewGroup;
        ViewGroup.LayoutParams layoutParams = actionView.getLayoutParams();
        if (!actionMenuView.checkLayoutParams(layoutParams)) {
            actionView.setLayoutParams(actionMenuView.generateLayoutParams(layoutParams));
        }
        return actionView;
    }

    @Override // androidx.appcompat.view.menu.a
    public androidx.appcompat.view.menu.k o(ViewGroup viewGroup) {
        androidx.appcompat.view.menu.k kVar = this.f3471j;
        androidx.appcompat.view.menu.k kVarO = super.o(viewGroup);
        if (kVar != kVarO) {
            ((ActionMenuView) kVarO).setPresenter(this);
        }
        return kVarO;
    }

    @Override // androidx.appcompat.view.menu.a
    public boolean q(int i3, androidx.appcompat.view.menu.g gVar) {
        return gVar.l();
    }

    public boolean y() {
        return B() | C();
    }
}
