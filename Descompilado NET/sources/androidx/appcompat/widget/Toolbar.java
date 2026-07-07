package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.appcompat.app.a;
import androidx.appcompat.view.menu.e;
import androidx.appcompat.view.menu.j;
import androidx.appcompat.widget.ActionMenuView;
import androidx.core.view.AbstractC0275w;
import androidx.core.view.InterfaceC0278z;
import d.AbstractC0487a;
import e.AbstractC0521a;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import w.AbstractC0758a;

/* JADX INFO: loaded from: classes.dex */
public class Toolbar extends ViewGroup implements InterfaceC0278z {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private ColorStateList f3930A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private ColorStateList f3931B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private boolean f3932C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private boolean f3933D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private final ArrayList f3934E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private final ArrayList f3935F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private final int[] f3936G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    final androidx.core.view.A f3937H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private ArrayList f3938I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private final ActionMenuView.e f3939J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private l0 f3940K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    private C0214c f3941L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    private f f3942M;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private j.a f3943N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    e.a f3944O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private boolean f3945P;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private OnBackInvokedCallback f3946Q;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private OnBackInvokedDispatcher f3947R;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private boolean f3948S;

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    private final Runnable f3949T;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    ActionMenuView f3950b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private TextView f3951c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private TextView f3952d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private ImageButton f3953e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private ImageView f3954f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private Drawable f3955g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private CharSequence f3956h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    ImageButton f3957i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    View f3958j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private Context f3959k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f3960l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f3961m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f3962n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    int f3963o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private int f3964p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private int f3965q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f3966r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private int f3967s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private int f3968t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private Z f3969u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private int f3970v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private int f3971w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private int f3972x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private CharSequence f3973y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private CharSequence f3974z;

    class a implements ActionMenuView.e {
        a() {
        }

        @Override // androidx.appcompat.widget.ActionMenuView.e
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (Toolbar.this.f3937H.d(menuItem)) {
                return true;
            }
            Toolbar.this.getClass();
            return false;
        }
    }

    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Toolbar.this.R();
        }
    }

    class c implements e.a {
        c() {
        }

        @Override // androidx.appcompat.view.menu.e.a
        public boolean a(androidx.appcompat.view.menu.e eVar, MenuItem menuItem) {
            e.a aVar = Toolbar.this.f3944O;
            return aVar != null && aVar.a(eVar, menuItem);
        }

        @Override // androidx.appcompat.view.menu.e.a
        public void b(androidx.appcompat.view.menu.e eVar) {
            if (!Toolbar.this.f3950b.H()) {
                Toolbar.this.f3937H.e(eVar);
            }
            e.a aVar = Toolbar.this.f3944O;
            if (aVar != null) {
                aVar.b(eVar);
            }
        }
    }

    class d implements View.OnClickListener {
        d() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Toolbar.this.f();
        }
    }

    static class e {
        static OnBackInvokedDispatcher a(View view) {
            return view.findOnBackInvokedDispatcher();
        }

        static OnBackInvokedCallback b(final Runnable runnable) {
            Objects.requireNonNull(runnable);
            return new OnBackInvokedCallback() { // from class: androidx.appcompat.widget.k0
                @Override // android.window.OnBackInvokedCallback
                public final void onBackInvoked() {
                    runnable.run();
                }
            };
        }

        static void c(Object obj, Object obj2) {
            ((OnBackInvokedDispatcher) obj).registerOnBackInvokedCallback(1000000, (OnBackInvokedCallback) obj2);
        }

        static void d(Object obj, Object obj2) {
            ((OnBackInvokedDispatcher) obj).unregisterOnBackInvokedCallback((OnBackInvokedCallback) obj2);
        }
    }

    private class f implements androidx.appcompat.view.menu.j {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        androidx.appcompat.view.menu.e f3979b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        androidx.appcompat.view.menu.g f3980c;

        f() {
        }

        @Override // androidx.appcompat.view.menu.j
        public void c(androidx.appcompat.view.menu.e eVar, boolean z3) {
        }

        @Override // androidx.appcompat.view.menu.j
        public void d(Context context, androidx.appcompat.view.menu.e eVar) {
            androidx.appcompat.view.menu.g gVar;
            androidx.appcompat.view.menu.e eVar2 = this.f3979b;
            if (eVar2 != null && (gVar = this.f3980c) != null) {
                eVar2.f(gVar);
            }
            this.f3979b = eVar;
        }

        @Override // androidx.appcompat.view.menu.j
        public boolean e(androidx.appcompat.view.menu.m mVar) {
            return false;
        }

        @Override // androidx.appcompat.view.menu.j
        public void f(boolean z3) {
            if (this.f3980c != null) {
                androidx.appcompat.view.menu.e eVar = this.f3979b;
                if (eVar != null) {
                    int size = eVar.size();
                    for (int i3 = 0; i3 < size; i3++) {
                        if (this.f3979b.getItem(i3) == this.f3980c) {
                            return;
                        }
                    }
                }
                i(this.f3979b, this.f3980c);
            }
        }

        @Override // androidx.appcompat.view.menu.j
        public boolean h() {
            return false;
        }

        @Override // androidx.appcompat.view.menu.j
        public boolean i(androidx.appcompat.view.menu.e eVar, androidx.appcompat.view.menu.g gVar) {
            KeyEvent.Callback callback = Toolbar.this.f3958j;
            if (callback instanceof androidx.appcompat.view.c) {
                ((androidx.appcompat.view.c) callback).d();
            }
            Toolbar toolbar = Toolbar.this;
            toolbar.removeView(toolbar.f3958j);
            Toolbar toolbar2 = Toolbar.this;
            toolbar2.removeView(toolbar2.f3957i);
            Toolbar toolbar3 = Toolbar.this;
            toolbar3.f3958j = null;
            toolbar3.a();
            this.f3980c = null;
            Toolbar.this.requestLayout();
            gVar.r(false);
            Toolbar.this.S();
            return true;
        }

        @Override // androidx.appcompat.view.menu.j
        public boolean j(androidx.appcompat.view.menu.e eVar, androidx.appcompat.view.menu.g gVar) {
            Toolbar.this.h();
            ViewParent parent = Toolbar.this.f3957i.getParent();
            Toolbar toolbar = Toolbar.this;
            if (parent != toolbar) {
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(toolbar.f3957i);
                }
                Toolbar toolbar2 = Toolbar.this;
                toolbar2.addView(toolbar2.f3957i);
            }
            Toolbar.this.f3958j = gVar.getActionView();
            this.f3980c = gVar;
            ViewParent parent2 = Toolbar.this.f3958j.getParent();
            Toolbar toolbar3 = Toolbar.this;
            if (parent2 != toolbar3) {
                if (parent2 instanceof ViewGroup) {
                    ((ViewGroup) parent2).removeView(toolbar3.f3958j);
                }
                g gVarGenerateDefaultLayoutParams = Toolbar.this.generateDefaultLayoutParams();
                Toolbar toolbar4 = Toolbar.this;
                gVarGenerateDefaultLayoutParams.f3161a = (toolbar4.f3963o & 112) | 8388611;
                gVarGenerateDefaultLayoutParams.f3982b = 2;
                toolbar4.f3958j.setLayoutParams(gVarGenerateDefaultLayoutParams);
                Toolbar toolbar5 = Toolbar.this;
                toolbar5.addView(toolbar5.f3958j);
            }
            Toolbar.this.K();
            Toolbar.this.requestLayout();
            gVar.r(true);
            KeyEvent.Callback callback = Toolbar.this.f3958j;
            if (callback instanceof androidx.appcompat.view.c) {
                ((androidx.appcompat.view.c) callback).c();
            }
            Toolbar.this.S();
            return true;
        }
    }

    public interface h {
    }

    public static class i extends AbstractC0758a {
        public static final Parcelable.Creator<i> CREATOR = new a();

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        int f3983c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        boolean f3984d;

        class a implements Parcelable.ClassLoaderCreator {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public i createFromParcel(Parcel parcel) {
                return new i(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
            public i createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new i(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
            public i[] newArray(int i3) {
                return new i[i3];
            }
        }

        public i(Parcel parcel) {
            this(parcel, null);
        }

        @Override // w.AbstractC0758a, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeInt(this.f3983c);
            parcel.writeInt(this.f3984d ? 1 : 0);
        }

        public i(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f3983c = parcel.readInt();
            this.f3984d = parcel.readInt() != 0;
        }

        public i(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public Toolbar(Context context) {
        this(context, null);
    }

    private boolean B(View view) {
        return view.getParent() == this || this.f3935F.contains(view);
    }

    private int E(View view, int i3, int[] iArr, int i4) {
        g gVar = (g) view.getLayoutParams();
        int i5 = ((ViewGroup.MarginLayoutParams) gVar).leftMargin - iArr[0];
        int iMax = i3 + Math.max(0, i5);
        iArr[0] = Math.max(0, -i5);
        int iS = s(view, i4);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(iMax, iS, iMax + measuredWidth, view.getMeasuredHeight() + iS);
        return iMax + measuredWidth + ((ViewGroup.MarginLayoutParams) gVar).rightMargin;
    }

    private int F(View view, int i3, int[] iArr, int i4) {
        g gVar = (g) view.getLayoutParams();
        int i5 = ((ViewGroup.MarginLayoutParams) gVar).rightMargin - iArr[1];
        int iMax = i3 - Math.max(0, i5);
        iArr[1] = Math.max(0, -i5);
        int iS = s(view, i4);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(iMax - measuredWidth, iS, iMax, view.getMeasuredHeight() + iS);
        return iMax - (measuredWidth + ((ViewGroup.MarginLayoutParams) gVar).leftMargin);
    }

    private int G(View view, int i3, int i4, int i5, int i6, int[] iArr) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int i7 = marginLayoutParams.leftMargin - iArr[0];
        int i8 = marginLayoutParams.rightMargin - iArr[1];
        int iMax = Math.max(0, i7) + Math.max(0, i8);
        iArr[0] = Math.max(0, -i7);
        iArr[1] = Math.max(0, -i8);
        view.measure(ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + iMax + i4, marginLayoutParams.width), ViewGroup.getChildMeasureSpec(i5, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i6, marginLayoutParams.height));
        return view.getMeasuredWidth() + iMax;
    }

    private void H(View view, int i3, int i4, int i5, int i6, int i7) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i4, marginLayoutParams.width);
        int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i5, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i6, marginLayoutParams.height);
        int mode = View.MeasureSpec.getMode(childMeasureSpec2);
        if (mode != 1073741824 && i7 >= 0) {
            if (mode != 0) {
                i7 = Math.min(View.MeasureSpec.getSize(childMeasureSpec2), i7);
            }
            childMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i7, 1073741824);
        }
        view.measure(childMeasureSpec, childMeasureSpec2);
    }

    private void I() {
        Menu menu = getMenu();
        ArrayList<MenuItem> currentMenuItems = getCurrentMenuItems();
        this.f3937H.b(menu, getMenuInflater());
        ArrayList<MenuItem> currentMenuItems2 = getCurrentMenuItems();
        currentMenuItems2.removeAll(currentMenuItems);
        this.f3938I = currentMenuItems2;
    }

    private void J() {
        removeCallbacks(this.f3949T);
        post(this.f3949T);
    }

    private boolean P() {
        if (!this.f3945P) {
            return false;
        }
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (Q(childAt) && childAt.getMeasuredWidth() > 0 && childAt.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean Q(View view) {
        return (view == null || view.getParent() != this || view.getVisibility() == 8) ? false : true;
    }

    private void b(List list, int i3) {
        boolean z3 = getLayoutDirection() == 1;
        int childCount = getChildCount();
        int iA = AbstractC0275w.a(i3, getLayoutDirection());
        list.clear();
        if (!z3) {
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = getChildAt(i4);
                g gVar = (g) childAt.getLayoutParams();
                if (gVar.f3982b == 0 && Q(childAt) && r(gVar.f3161a) == iA) {
                    list.add(childAt);
                }
            }
            return;
        }
        for (int i5 = childCount - 1; i5 >= 0; i5--) {
            View childAt2 = getChildAt(i5);
            g gVar2 = (g) childAt2.getLayoutParams();
            if (gVar2.f3982b == 0 && Q(childAt2) && r(gVar2.f3161a) == iA) {
                list.add(childAt2);
            }
        }
    }

    private void c(View view, boolean z3) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        g gVarGenerateDefaultLayoutParams = layoutParams == null ? generateDefaultLayoutParams() : !checkLayoutParams(layoutParams) ? generateLayoutParams(layoutParams) : (g) layoutParams;
        gVarGenerateDefaultLayoutParams.f3982b = 1;
        if (!z3 || this.f3958j == null) {
            addView(view, gVarGenerateDefaultLayoutParams);
        } else {
            view.setLayoutParams(gVarGenerateDefaultLayoutParams);
            this.f3935F.add(view);
        }
    }

    private ArrayList<MenuItem> getCurrentMenuItems() {
        ArrayList<MenuItem> arrayList = new ArrayList<>();
        Menu menu = getMenu();
        for (int i3 = 0; i3 < menu.size(); i3++) {
            arrayList.add(menu.getItem(i3));
        }
        return arrayList;
    }

    private MenuInflater getMenuInflater() {
        return new androidx.appcompat.view.g(getContext());
    }

    private void i() {
        if (this.f3969u == null) {
            this.f3969u = new Z();
        }
    }

    private void j() {
        if (this.f3954f == null) {
            this.f3954f = new r(getContext());
        }
    }

    private void k() {
        l();
        if (this.f3950b.L() == null) {
            androidx.appcompat.view.menu.e eVar = (androidx.appcompat.view.menu.e) this.f3950b.getMenu();
            if (this.f3942M == null) {
                this.f3942M = new f();
            }
            this.f3950b.setExpandedActionViewsExclusive(true);
            eVar.c(this.f3942M, this.f3959k);
            S();
        }
    }

    private void l() {
        if (this.f3950b == null) {
            ActionMenuView actionMenuView = new ActionMenuView(getContext());
            this.f3950b = actionMenuView;
            actionMenuView.setPopupTheme(this.f3960l);
            this.f3950b.setOnMenuItemClickListener(this.f3939J);
            this.f3950b.M(this.f3943N, new c());
            g gVarGenerateDefaultLayoutParams = generateDefaultLayoutParams();
            gVarGenerateDefaultLayoutParams.f3161a = (this.f3963o & 112) | 8388613;
            this.f3950b.setLayoutParams(gVarGenerateDefaultLayoutParams);
            c(this.f3950b, false);
        }
    }

    private void m() {
        if (this.f3953e == null) {
            this.f3953e = new C0227p(getContext(), null, AbstractC0487a.f8671P);
            g gVarGenerateDefaultLayoutParams = generateDefaultLayoutParams();
            gVarGenerateDefaultLayoutParams.f3161a = (this.f3963o & 112) | 8388611;
            this.f3953e.setLayoutParams(gVarGenerateDefaultLayoutParams);
        }
    }

    private int r(int i3) {
        int layoutDirection = getLayoutDirection();
        int iA = AbstractC0275w.a(i3, layoutDirection) & 7;
        return (iA == 1 || iA == 3 || iA == 5) ? iA : layoutDirection == 1 ? 5 : 3;
    }

    private int s(View view, int i3) {
        g gVar = (g) view.getLayoutParams();
        int measuredHeight = view.getMeasuredHeight();
        int i4 = i3 > 0 ? (measuredHeight - i3) / 2 : 0;
        int iT = t(gVar.f3161a);
        if (iT == 48) {
            return getPaddingTop() - i4;
        }
        if (iT == 80) {
            return (((getHeight() - getPaddingBottom()) - measuredHeight) - ((ViewGroup.MarginLayoutParams) gVar).bottomMargin) - i4;
        }
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int height = getHeight();
        int iMax = (((height - paddingTop) - paddingBottom) - measuredHeight) / 2;
        int i5 = ((ViewGroup.MarginLayoutParams) gVar).topMargin;
        if (iMax < i5) {
            iMax = i5;
        } else {
            int i6 = (((height - paddingBottom) - measuredHeight) - iMax) - paddingTop;
            int i7 = ((ViewGroup.MarginLayoutParams) gVar).bottomMargin;
            if (i6 < i7) {
                iMax = Math.max(0, iMax - (i7 - i6));
            }
        }
        return paddingTop + iMax;
    }

    private int t(int i3) {
        int i4 = i3 & 112;
        return (i4 == 16 || i4 == 48 || i4 == 80) ? i4 : this.f3972x & 112;
    }

    private int u(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return marginLayoutParams.getMarginStart() + marginLayoutParams.getMarginEnd();
    }

    private int v(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    private int w(List list, int[] iArr) {
        int i3 = iArr[0];
        int i4 = iArr[1];
        int size = list.size();
        int i5 = 0;
        int measuredWidth = 0;
        while (i5 < size) {
            View view = (View) list.get(i5);
            g gVar = (g) view.getLayoutParams();
            int i6 = ((ViewGroup.MarginLayoutParams) gVar).leftMargin - i3;
            int i7 = ((ViewGroup.MarginLayoutParams) gVar).rightMargin - i4;
            int iMax = Math.max(0, i6);
            int iMax2 = Math.max(0, i7);
            int iMax3 = Math.max(0, -i6);
            int iMax4 = Math.max(0, -i7);
            measuredWidth += iMax + view.getMeasuredWidth() + iMax2;
            i5++;
            i4 = iMax4;
            i3 = iMax3;
        }
        return measuredWidth;
    }

    public void A() {
        Iterator it = this.f3938I.iterator();
        while (it.hasNext()) {
            getMenu().removeItem(((MenuItem) it.next()).getItemId());
        }
        I();
    }

    public boolean C() {
        ActionMenuView actionMenuView = this.f3950b;
        return actionMenuView != null && actionMenuView.G();
    }

    public boolean D() {
        ActionMenuView actionMenuView = this.f3950b;
        return actionMenuView != null && actionMenuView.H();
    }

    void K() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (((g) childAt.getLayoutParams()).f3982b != 2 && childAt != this.f3950b) {
                removeViewAt(childCount);
                this.f3935F.add(childAt);
            }
        }
    }

    public void L(int i3, int i4) {
        i();
        this.f3969u.g(i3, i4);
    }

    public void M(androidx.appcompat.view.menu.e eVar, C0214c c0214c) {
        if (eVar == null && this.f3950b == null) {
            return;
        }
        l();
        androidx.appcompat.view.menu.e eVarL = this.f3950b.L();
        if (eVarL == eVar) {
            return;
        }
        if (eVarL != null) {
            eVarL.P(this.f3941L);
            eVarL.P(this.f3942M);
        }
        if (this.f3942M == null) {
            this.f3942M = new f();
        }
        c0214c.G(true);
        if (eVar != null) {
            eVar.c(c0214c, this.f3959k);
            eVar.c(this.f3942M, this.f3959k);
        } else {
            c0214c.d(this.f3959k, null);
            this.f3942M.d(this.f3959k, null);
            c0214c.f(true);
            this.f3942M.f(true);
        }
        this.f3950b.setPopupTheme(this.f3960l);
        this.f3950b.setPresenter(c0214c);
        this.f3941L = c0214c;
        S();
    }

    public void N(Context context, int i3) {
        this.f3962n = i3;
        TextView textView = this.f3952d;
        if (textView != null) {
            textView.setTextAppearance(context, i3);
        }
    }

    public void O(Context context, int i3) {
        this.f3961m = i3;
        TextView textView = this.f3951c;
        if (textView != null) {
            textView.setTextAppearance(context, i3);
        }
    }

    public boolean R() {
        ActionMenuView actionMenuView = this.f3950b;
        return actionMenuView != null && actionMenuView.N();
    }

    void S() {
        OnBackInvokedDispatcher onBackInvokedDispatcher;
        if (Build.VERSION.SDK_INT >= 33) {
            OnBackInvokedDispatcher onBackInvokedDispatcherA = e.a(this);
            boolean z3 = x() && onBackInvokedDispatcherA != null && isAttachedToWindow() && this.f3948S;
            if (z3 && this.f3947R == null) {
                if (this.f3946Q == null) {
                    this.f3946Q = e.b(new Runnable() { // from class: androidx.appcompat.widget.i0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f4236b.f();
                        }
                    });
                }
                e.c(onBackInvokedDispatcherA, this.f3946Q);
                this.f3947R = onBackInvokedDispatcherA;
                return;
            }
            if (z3 || (onBackInvokedDispatcher = this.f3947R) == null) {
                return;
            }
            e.d(onBackInvokedDispatcher, this.f3946Q);
            this.f3947R = null;
        }
    }

    void a() {
        for (int size = this.f3935F.size() - 1; size >= 0; size--) {
            addView((View) this.f3935F.get(size));
        }
        this.f3935F.clear();
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.checkLayoutParams(layoutParams) && (layoutParams instanceof g);
    }

    @Override // androidx.core.view.InterfaceC0278z
    public void d(androidx.core.view.C c4) {
        this.f3937H.f(c4);
    }

    public boolean e() {
        ActionMenuView actionMenuView;
        return getVisibility() == 0 && (actionMenuView = this.f3950b) != null && actionMenuView.I();
    }

    public void f() {
        f fVar = this.f3942M;
        androidx.appcompat.view.menu.g gVar = fVar == null ? null : fVar.f3980c;
        if (gVar != null) {
            gVar.collapseActionView();
        }
    }

    public void g() {
        ActionMenuView actionMenuView = this.f3950b;
        if (actionMenuView != null) {
            actionMenuView.z();
        }
    }

    public CharSequence getCollapseContentDescription() {
        ImageButton imageButton = this.f3957i;
        if (imageButton != null) {
            return imageButton.getContentDescription();
        }
        return null;
    }

    public Drawable getCollapseIcon() {
        ImageButton imageButton = this.f3957i;
        if (imageButton != null) {
            return imageButton.getDrawable();
        }
        return null;
    }

    public int getContentInsetEnd() {
        Z z3 = this.f3969u;
        if (z3 != null) {
            return z3.a();
        }
        return 0;
    }

    public int getContentInsetEndWithActions() {
        int i3 = this.f3971w;
        return i3 != Integer.MIN_VALUE ? i3 : getContentInsetEnd();
    }

    public int getContentInsetLeft() {
        Z z3 = this.f3969u;
        if (z3 != null) {
            return z3.b();
        }
        return 0;
    }

    public int getContentInsetRight() {
        Z z3 = this.f3969u;
        if (z3 != null) {
            return z3.c();
        }
        return 0;
    }

    public int getContentInsetStart() {
        Z z3 = this.f3969u;
        if (z3 != null) {
            return z3.d();
        }
        return 0;
    }

    public int getContentInsetStartWithNavigation() {
        int i3 = this.f3970v;
        return i3 != Integer.MIN_VALUE ? i3 : getContentInsetStart();
    }

    public int getCurrentContentInsetEnd() {
        androidx.appcompat.view.menu.e eVarL;
        ActionMenuView actionMenuView = this.f3950b;
        return (actionMenuView == null || (eVarL = actionMenuView.L()) == null || !eVarL.hasVisibleItems()) ? getContentInsetEnd() : Math.max(getContentInsetEnd(), Math.max(this.f3971w, 0));
    }

    public int getCurrentContentInsetLeft() {
        return getLayoutDirection() == 1 ? getCurrentContentInsetEnd() : getCurrentContentInsetStart();
    }

    public int getCurrentContentInsetRight() {
        return getLayoutDirection() == 1 ? getCurrentContentInsetStart() : getCurrentContentInsetEnd();
    }

    public int getCurrentContentInsetStart() {
        return getNavigationIcon() != null ? Math.max(getContentInsetStart(), Math.max(this.f3970v, 0)) : getContentInsetStart();
    }

    public Drawable getLogo() {
        ImageView imageView = this.f3954f;
        if (imageView != null) {
            return imageView.getDrawable();
        }
        return null;
    }

    public CharSequence getLogoDescription() {
        ImageView imageView = this.f3954f;
        if (imageView != null) {
            return imageView.getContentDescription();
        }
        return null;
    }

    public Menu getMenu() {
        k();
        return this.f3950b.getMenu();
    }

    View getNavButtonView() {
        return this.f3953e;
    }

    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.f3953e;
        if (imageButton != null) {
            return imageButton.getContentDescription();
        }
        return null;
    }

    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.f3953e;
        if (imageButton != null) {
            return imageButton.getDrawable();
        }
        return null;
    }

    C0214c getOuterActionMenuPresenter() {
        return this.f3941L;
    }

    public Drawable getOverflowIcon() {
        k();
        return this.f3950b.getOverflowIcon();
    }

    Context getPopupContext() {
        return this.f3959k;
    }

    public int getPopupTheme() {
        return this.f3960l;
    }

    public CharSequence getSubtitle() {
        return this.f3974z;
    }

    final TextView getSubtitleTextView() {
        return this.f3952d;
    }

    public CharSequence getTitle() {
        return this.f3973y;
    }

    public int getTitleMarginBottom() {
        return this.f3968t;
    }

    public int getTitleMarginEnd() {
        return this.f3966r;
    }

    public int getTitleMarginStart() {
        return this.f3965q;
    }

    public int getTitleMarginTop() {
        return this.f3967s;
    }

    final TextView getTitleTextView() {
        return this.f3951c;
    }

    public J getWrapper() {
        if (this.f3940K == null) {
            this.f3940K = new l0(this, true);
        }
        return this.f3940K;
    }

    void h() {
        if (this.f3957i == null) {
            C0227p c0227p = new C0227p(getContext(), null, AbstractC0487a.f8671P);
            this.f3957i = c0227p;
            c0227p.setImageDrawable(this.f3955g);
            this.f3957i.setContentDescription(this.f3956h);
            g gVarGenerateDefaultLayoutParams = generateDefaultLayoutParams();
            gVarGenerateDefaultLayoutParams.f3161a = (this.f3963o & 112) | 8388611;
            gVarGenerateDefaultLayoutParams.f3982b = 2;
            this.f3957i.setLayoutParams(gVarGenerateDefaultLayoutParams);
            this.f3957i.setOnClickListener(new d());
        }
    }

    @Override // androidx.core.view.InterfaceC0278z
    public void n(androidx.core.view.C c4) {
        this.f3937H.a(c4);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* JADX INFO: renamed from: o, reason: merged with bridge method [inline-methods] */
    public g generateDefaultLayoutParams() {
        return new g(-2, -2);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        S();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.f3949T);
        S();
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.f3933D = false;
        }
        if (!this.f3933D) {
            boolean zOnHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !zOnHoverEvent) {
                this.f3933D = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.f3933D = false;
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:107:0x02a1 A[LOOP:0: B:106:0x029f->B:107:0x02a1, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x02c3 A[LOOP:1: B:109:0x02c1->B:110:0x02c3, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:114:0x02ed  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x02fc A[LOOP:2: B:118:0x02fa->B:119:0x02fc, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00ff  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x011c  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0167  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01a5  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01b6  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0227  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void onLayout(boolean r20, int r21, int r22, int r23, int r24) {
        /*
            Method dump skipped, instruction units count: 785
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.Toolbar.onLayout(boolean, int, int, int, int):void");
    }

    @Override // android.view.View
    protected void onMeasure(int i3, int i4) {
        int measuredWidth;
        int iMax;
        int iCombineMeasuredStates;
        int measuredWidth2;
        int measuredHeight;
        int iCombineMeasuredStates2;
        int iMax2;
        int[] iArr = this.f3936G;
        boolean zB = s0.b(this);
        int i5 = !zB ? 1 : 0;
        if (Q(this.f3953e)) {
            H(this.f3953e, i3, 0, i4, 0, this.f3964p);
            measuredWidth = this.f3953e.getMeasuredWidth() + u(this.f3953e);
            iMax = Math.max(0, this.f3953e.getMeasuredHeight() + v(this.f3953e));
            iCombineMeasuredStates = View.combineMeasuredStates(0, this.f3953e.getMeasuredState());
        } else {
            measuredWidth = 0;
            iMax = 0;
            iCombineMeasuredStates = 0;
        }
        if (Q(this.f3957i)) {
            H(this.f3957i, i3, 0, i4, 0, this.f3964p);
            measuredWidth = this.f3957i.getMeasuredWidth() + u(this.f3957i);
            iMax = Math.max(iMax, this.f3957i.getMeasuredHeight() + v(this.f3957i));
            iCombineMeasuredStates = View.combineMeasuredStates(iCombineMeasuredStates, this.f3957i.getMeasuredState());
        }
        int currentContentInsetStart = getCurrentContentInsetStart();
        int iMax3 = Math.max(currentContentInsetStart, measuredWidth);
        iArr[zB ? 1 : 0] = Math.max(0, currentContentInsetStart - measuredWidth);
        if (Q(this.f3950b)) {
            H(this.f3950b, i3, iMax3, i4, 0, this.f3964p);
            measuredWidth2 = this.f3950b.getMeasuredWidth() + u(this.f3950b);
            iMax = Math.max(iMax, this.f3950b.getMeasuredHeight() + v(this.f3950b));
            iCombineMeasuredStates = View.combineMeasuredStates(iCombineMeasuredStates, this.f3950b.getMeasuredState());
        } else {
            measuredWidth2 = 0;
        }
        int currentContentInsetEnd = getCurrentContentInsetEnd();
        int iMax4 = iMax3 + Math.max(currentContentInsetEnd, measuredWidth2);
        iArr[i5] = Math.max(0, currentContentInsetEnd - measuredWidth2);
        if (Q(this.f3958j)) {
            iMax4 += G(this.f3958j, i3, iMax4, i4, 0, iArr);
            iMax = Math.max(iMax, this.f3958j.getMeasuredHeight() + v(this.f3958j));
            iCombineMeasuredStates = View.combineMeasuredStates(iCombineMeasuredStates, this.f3958j.getMeasuredState());
        }
        if (Q(this.f3954f)) {
            iMax4 += G(this.f3954f, i3, iMax4, i4, 0, iArr);
            iMax = Math.max(iMax, this.f3954f.getMeasuredHeight() + v(this.f3954f));
            iCombineMeasuredStates = View.combineMeasuredStates(iCombineMeasuredStates, this.f3954f.getMeasuredState());
        }
        int childCount = getChildCount();
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (((g) childAt.getLayoutParams()).f3982b == 0 && Q(childAt)) {
                iMax4 += G(childAt, i3, iMax4, i4, 0, iArr);
                iMax = Math.max(iMax, childAt.getMeasuredHeight() + v(childAt));
                iCombineMeasuredStates = View.combineMeasuredStates(iCombineMeasuredStates, childAt.getMeasuredState());
            }
        }
        int i7 = this.f3967s + this.f3968t;
        int i8 = this.f3965q + this.f3966r;
        if (Q(this.f3951c)) {
            G(this.f3951c, i3, iMax4 + i8, i4, i7, iArr);
            int measuredWidth3 = this.f3951c.getMeasuredWidth() + u(this.f3951c);
            measuredHeight = this.f3951c.getMeasuredHeight() + v(this.f3951c);
            iCombineMeasuredStates2 = View.combineMeasuredStates(iCombineMeasuredStates, this.f3951c.getMeasuredState());
            iMax2 = measuredWidth3;
        } else {
            measuredHeight = 0;
            iCombineMeasuredStates2 = iCombineMeasuredStates;
            iMax2 = 0;
        }
        if (Q(this.f3952d)) {
            iMax2 = Math.max(iMax2, G(this.f3952d, i3, iMax4 + i8, i4, measuredHeight + i7, iArr));
            measuredHeight += this.f3952d.getMeasuredHeight() + v(this.f3952d);
            iCombineMeasuredStates2 = View.combineMeasuredStates(iCombineMeasuredStates2, this.f3952d.getMeasuredState());
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(iMax4 + iMax2 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i3, (-16777216) & iCombineMeasuredStates2), P() ? 0 : View.resolveSizeAndState(Math.max(Math.max(iMax, measuredHeight) + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i4, iCombineMeasuredStates2 << 16));
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        MenuItem menuItemFindItem;
        if (!(parcelable instanceof i)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        i iVar = (i) parcelable;
        super.onRestoreInstanceState(iVar.a());
        ActionMenuView actionMenuView = this.f3950b;
        androidx.appcompat.view.menu.e eVarL = actionMenuView != null ? actionMenuView.L() : null;
        int i3 = iVar.f3983c;
        if (i3 != 0 && this.f3942M != null && eVarL != null && (menuItemFindItem = eVarL.findItem(i3)) != null) {
            menuItemFindItem.expandActionView();
        }
        if (iVar.f3984d) {
            J();
        }
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i3) {
        super.onRtlPropertiesChanged(i3);
        i();
        this.f3969u.f(i3 == 1);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        androidx.appcompat.view.menu.g gVar;
        i iVar = new i(super.onSaveInstanceState());
        f fVar = this.f3942M;
        if (fVar != null && (gVar = fVar.f3980c) != null) {
            iVar.f3983c = gVar.getItemId();
        }
        iVar.f3984d = D();
        return iVar;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.f3932C = false;
        }
        if (!this.f3932C) {
            boolean zOnTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !zOnTouchEvent) {
                this.f3932C = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.f3932C = false;
        }
        return true;
    }

    @Override // android.view.ViewGroup
    /* JADX INFO: renamed from: p, reason: merged with bridge method [inline-methods] */
    public g generateLayoutParams(AttributeSet attributeSet) {
        return new g(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
    public g generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof g ? new g((g) layoutParams) : layoutParams instanceof a.C0051a ? new g((a.C0051a) layoutParams) : layoutParams instanceof ViewGroup.MarginLayoutParams ? new g((ViewGroup.MarginLayoutParams) layoutParams) : new g(layoutParams);
    }

    public void setBackInvokedCallbackEnabled(boolean z3) {
        if (this.f3948S != z3) {
            this.f3948S = z3;
            S();
        }
    }

    public void setCollapseContentDescription(int i3) {
        setCollapseContentDescription(i3 != 0 ? getContext().getText(i3) : null);
    }

    public void setCollapseIcon(int i3) {
        setCollapseIcon(AbstractC0521a.b(getContext(), i3));
    }

    public void setCollapsible(boolean z3) {
        this.f3945P = z3;
        requestLayout();
    }

    public void setContentInsetEndWithActions(int i3) {
        if (i3 < 0) {
            i3 = Integer.MIN_VALUE;
        }
        if (i3 != this.f3971w) {
            this.f3971w = i3;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public void setContentInsetStartWithNavigation(int i3) {
        if (i3 < 0) {
            i3 = Integer.MIN_VALUE;
        }
        if (i3 != this.f3970v) {
            this.f3970v = i3;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public void setLogo(int i3) {
        setLogo(AbstractC0521a.b(getContext(), i3));
    }

    public void setLogoDescription(int i3) {
        setLogoDescription(getContext().getText(i3));
    }

    public void setNavigationContentDescription(int i3) {
        setNavigationContentDescription(i3 != 0 ? getContext().getText(i3) : null);
    }

    public void setNavigationIcon(int i3) {
        setNavigationIcon(AbstractC0521a.b(getContext(), i3));
    }

    public void setNavigationOnClickListener(View.OnClickListener onClickListener) {
        m();
        this.f3953e.setOnClickListener(onClickListener);
    }

    public void setOnMenuItemClickListener(h hVar) {
    }

    public void setOverflowIcon(Drawable drawable) {
        k();
        this.f3950b.setOverflowIcon(drawable);
    }

    public void setPopupTheme(int i3) {
        if (this.f3960l != i3) {
            this.f3960l = i3;
            if (i3 == 0) {
                this.f3959k = getContext();
            } else {
                this.f3959k = new ContextThemeWrapper(getContext(), i3);
            }
        }
    }

    public void setSubtitle(int i3) {
        setSubtitle(getContext().getText(i3));
    }

    public void setSubtitleTextColor(int i3) {
        setSubtitleTextColor(ColorStateList.valueOf(i3));
    }

    public void setTitle(int i3) {
        setTitle(getContext().getText(i3));
    }

    public void setTitleMarginBottom(int i3) {
        this.f3968t = i3;
        requestLayout();
    }

    public void setTitleMarginEnd(int i3) {
        this.f3966r = i3;
        requestLayout();
    }

    public void setTitleMarginStart(int i3) {
        this.f3965q = i3;
        requestLayout();
    }

    public void setTitleMarginTop(int i3) {
        this.f3967s = i3;
        requestLayout();
    }

    public void setTitleTextColor(int i3) {
        setTitleTextColor(ColorStateList.valueOf(i3));
    }

    public boolean x() {
        f fVar = this.f3942M;
        return (fVar == null || fVar.f3980c == null) ? false : true;
    }

    public boolean y() {
        ActionMenuView actionMenuView = this.f3950b;
        return actionMenuView != null && actionMenuView.F();
    }

    public void z(int i3) {
        getMenuInflater().inflate(i3, getMenu());
    }

    public static class g extends a.C0051a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        int f3982b;

        public g(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f3982b = 0;
        }

        void a(ViewGroup.MarginLayoutParams marginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) this).leftMargin = marginLayoutParams.leftMargin;
            ((ViewGroup.MarginLayoutParams) this).topMargin = marginLayoutParams.topMargin;
            ((ViewGroup.MarginLayoutParams) this).rightMargin = marginLayoutParams.rightMargin;
            ((ViewGroup.MarginLayoutParams) this).bottomMargin = marginLayoutParams.bottomMargin;
        }

        public g(int i3, int i4) {
            super(i3, i4);
            this.f3982b = 0;
            this.f3161a = 8388627;
        }

        public g(int i3, int i4, int i5) {
            super(i3, i4);
            this.f3982b = 0;
            this.f3161a = i5;
        }

        public g(int i3) {
            this(-2, -1, i3);
        }

        public g(g gVar) {
            super((a.C0051a) gVar);
            this.f3982b = 0;
            this.f3982b = gVar.f3982b;
        }

        public g(a.C0051a c0051a) {
            super(c0051a);
            this.f3982b = 0;
        }

        public g(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f3982b = 0;
            a(marginLayoutParams);
        }

        public g(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f3982b = 0;
        }
    }

    public Toolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8672Q);
    }

    public void setCollapseContentDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            h();
        }
        ImageButton imageButton = this.f3957i;
        if (imageButton != null) {
            imageButton.setContentDescription(charSequence);
        }
    }

    public void setCollapseIcon(Drawable drawable) {
        if (drawable != null) {
            h();
            this.f3957i.setImageDrawable(drawable);
        } else {
            ImageButton imageButton = this.f3957i;
            if (imageButton != null) {
                imageButton.setImageDrawable(this.f3955g);
            }
        }
    }

    public void setLogo(Drawable drawable) {
        if (drawable != null) {
            j();
            if (!B(this.f3954f)) {
                c(this.f3954f, true);
            }
        } else {
            ImageView imageView = this.f3954f;
            if (imageView != null && B(imageView)) {
                removeView(this.f3954f);
                this.f3935F.remove(this.f3954f);
            }
        }
        ImageView imageView2 = this.f3954f;
        if (imageView2 != null) {
            imageView2.setImageDrawable(drawable);
        }
    }

    public void setLogoDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            j();
        }
        ImageView imageView = this.f3954f;
        if (imageView != null) {
            imageView.setContentDescription(charSequence);
        }
    }

    public void setNavigationContentDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            m();
        }
        ImageButton imageButton = this.f3953e;
        if (imageButton != null) {
            imageButton.setContentDescription(charSequence);
            m0.a(this.f3953e, charSequence);
        }
    }

    public void setNavigationIcon(Drawable drawable) {
        if (drawable != null) {
            m();
            if (!B(this.f3953e)) {
                c(this.f3953e, true);
            }
        } else {
            ImageButton imageButton = this.f3953e;
            if (imageButton != null && B(imageButton)) {
                removeView(this.f3953e);
                this.f3935F.remove(this.f3953e);
            }
        }
        ImageButton imageButton2 = this.f3953e;
        if (imageButton2 != null) {
            imageButton2.setImageDrawable(drawable);
        }
    }

    public void setSubtitle(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            TextView textView = this.f3952d;
            if (textView != null && B(textView)) {
                removeView(this.f3952d);
                this.f3935F.remove(this.f3952d);
            }
        } else {
            if (this.f3952d == null) {
                Context context = getContext();
                D d4 = new D(context);
                this.f3952d = d4;
                d4.setSingleLine();
                this.f3952d.setEllipsize(TextUtils.TruncateAt.END);
                int i3 = this.f3962n;
                if (i3 != 0) {
                    this.f3952d.setTextAppearance(context, i3);
                }
                ColorStateList colorStateList = this.f3931B;
                if (colorStateList != null) {
                    this.f3952d.setTextColor(colorStateList);
                }
            }
            if (!B(this.f3952d)) {
                c(this.f3952d, true);
            }
        }
        TextView textView2 = this.f3952d;
        if (textView2 != null) {
            textView2.setText(charSequence);
        }
        this.f3974z = charSequence;
    }

    public void setSubtitleTextColor(ColorStateList colorStateList) {
        this.f3931B = colorStateList;
        TextView textView = this.f3952d;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public void setTitle(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            TextView textView = this.f3951c;
            if (textView != null && B(textView)) {
                removeView(this.f3951c);
                this.f3935F.remove(this.f3951c);
            }
        } else {
            if (this.f3951c == null) {
                Context context = getContext();
                D d4 = new D(context);
                this.f3951c = d4;
                d4.setSingleLine();
                this.f3951c.setEllipsize(TextUtils.TruncateAt.END);
                int i3 = this.f3961m;
                if (i3 != 0) {
                    this.f3951c.setTextAppearance(context, i3);
                }
                ColorStateList colorStateList = this.f3930A;
                if (colorStateList != null) {
                    this.f3951c.setTextColor(colorStateList);
                }
            }
            if (!B(this.f3951c)) {
                c(this.f3951c, true);
            }
        }
        TextView textView2 = this.f3951c;
        if (textView2 != null) {
            textView2.setText(charSequence);
        }
        this.f3973y = charSequence;
    }

    public void setTitleTextColor(ColorStateList colorStateList) {
        this.f3930A = colorStateList;
        TextView textView = this.f3951c;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public Toolbar(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.f3972x = 8388627;
        this.f3934E = new ArrayList();
        this.f3935F = new ArrayList();
        this.f3936G = new int[2];
        this.f3937H = new androidx.core.view.A(new Runnable() { // from class: androidx.appcompat.widget.j0
            @Override // java.lang.Runnable
            public final void run() {
                this.f4243b.A();
            }
        });
        this.f3938I = new ArrayList();
        this.f3939J = new a();
        this.f3949T = new b();
        h0 h0VarU = h0.u(getContext(), attributeSet, d.j.f8971d3, i3, 0);
        androidx.core.view.Z.V(this, context, d.j.f8971d3, attributeSet, h0VarU.q(), i3, 0);
        this.f3961m = h0VarU.m(d.j.F3, 0);
        this.f3962n = h0VarU.m(d.j.w3, 0);
        this.f3972x = h0VarU.k(d.j.f8976e3, this.f3972x);
        this.f3963o = h0VarU.k(d.j.f3, 48);
        int iD = h0VarU.d(d.j.z3, 0);
        iD = h0VarU.r(d.j.E3) ? h0VarU.d(d.j.E3, iD) : iD;
        this.f3968t = iD;
        this.f3967s = iD;
        this.f3966r = iD;
        this.f3965q = iD;
        int iD2 = h0VarU.d(d.j.C3, -1);
        if (iD2 >= 0) {
            this.f3965q = iD2;
        }
        int iD3 = h0VarU.d(d.j.B3, -1);
        if (iD3 >= 0) {
            this.f3966r = iD3;
        }
        int iD4 = h0VarU.d(d.j.D3, -1);
        if (iD4 >= 0) {
            this.f3967s = iD4;
        }
        int iD5 = h0VarU.d(d.j.A3, -1);
        if (iD5 >= 0) {
            this.f3968t = iD5;
        }
        this.f3964p = h0VarU.e(d.j.q3, -1);
        int iD6 = h0VarU.d(d.j.m3, Integer.MIN_VALUE);
        int iD7 = h0VarU.d(d.j.i3, Integer.MIN_VALUE);
        int iE = h0VarU.e(d.j.k3, 0);
        int iE2 = h0VarU.e(d.j.l3, 0);
        i();
        this.f3969u.e(iE, iE2);
        if (iD6 != Integer.MIN_VALUE || iD7 != Integer.MIN_VALUE) {
            this.f3969u.g(iD6, iD7);
        }
        this.f3970v = h0VarU.d(d.j.n3, Integer.MIN_VALUE);
        this.f3971w = h0VarU.d(d.j.j3, Integer.MIN_VALUE);
        this.f3955g = h0VarU.f(d.j.h3);
        this.f3956h = h0VarU.o(d.j.g3);
        CharSequence charSequenceO = h0VarU.o(d.j.y3);
        if (!TextUtils.isEmpty(charSequenceO)) {
            setTitle(charSequenceO);
        }
        CharSequence charSequenceO2 = h0VarU.o(d.j.v3);
        if (!TextUtils.isEmpty(charSequenceO2)) {
            setSubtitle(charSequenceO2);
        }
        this.f3959k = getContext();
        setPopupTheme(h0VarU.m(d.j.u3, 0));
        Drawable drawableF = h0VarU.f(d.j.t3);
        if (drawableF != null) {
            setNavigationIcon(drawableF);
        }
        CharSequence charSequenceO3 = h0VarU.o(d.j.s3);
        if (!TextUtils.isEmpty(charSequenceO3)) {
            setNavigationContentDescription(charSequenceO3);
        }
        Drawable drawableF2 = h0VarU.f(d.j.o3);
        if (drawableF2 != null) {
            setLogo(drawableF2);
        }
        CharSequence charSequenceO4 = h0VarU.o(d.j.p3);
        if (!TextUtils.isEmpty(charSequenceO4)) {
            setLogoDescription(charSequenceO4);
        }
        if (h0VarU.r(d.j.G3)) {
            setTitleTextColor(h0VarU.c(d.j.G3));
        }
        if (h0VarU.r(d.j.x3)) {
            setSubtitleTextColor(h0VarU.c(d.j.x3));
        }
        if (h0VarU.r(d.j.r3)) {
            z(h0VarU.m(d.j.r3, 0));
        }
        h0VarU.w();
    }
}
