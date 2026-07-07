package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import d.AbstractC0487a;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class U implements i.e {

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private static Method f3985H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private static Method f3986I;

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private final e f3987A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private Runnable f3988B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    final Handler f3989C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private final Rect f3990D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private Rect f3991E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private boolean f3992F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    PopupWindow f3993G;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Context f3994b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private ListAdapter f3995c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    P f3996d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f3997e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f3998f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f3999g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f4000h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f4001i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f4002j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f4003k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f4004l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f4005m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f4006n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private boolean f4007o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    int f4008p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private View f4009q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f4010r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private DataSetObserver f4011s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private View f4012t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private Drawable f4013u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private AdapterView.OnItemClickListener f4014v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private AdapterView.OnItemSelectedListener f4015w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    final i f4016x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private final h f4017y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private final g f4018z;

    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            View viewT = U.this.t();
            if (viewT == null || viewT.getWindowToken() == null) {
                return;
            }
            U.this.b();
        }
    }

    class b implements AdapterView.OnItemSelectedListener {
        b() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i3, long j3) {
            P p3;
            if (i3 == -1 || (p3 = U.this.f3996d) == null) {
                return;
            }
            p3.setListSelectionHidden(false);
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }
    }

    static class c {
        static int a(PopupWindow popupWindow, View view, int i3, boolean z3) {
            return popupWindow.getMaxAvailableHeight(view, i3, z3);
        }
    }

    static class d {
        static void a(PopupWindow popupWindow, Rect rect) {
            popupWindow.setEpicenterBounds(rect);
        }

        static void b(PopupWindow popupWindow, boolean z3) {
            popupWindow.setIsClippedToScreen(z3);
        }
    }

    private class e implements Runnable {
        e() {
        }

        @Override // java.lang.Runnable
        public void run() {
            U.this.r();
        }
    }

    private class f extends DataSetObserver {
        f() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            if (U.this.a()) {
                U.this.b();
            }
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            U.this.dismiss();
        }
    }

    private class g implements AbsListView.OnScrollListener {
        g() {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView absListView, int i3, int i4, int i5) {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView absListView, int i3) {
            if (i3 != 1 || U.this.w() || U.this.f3993G.getContentView() == null) {
                return;
            }
            U u3 = U.this;
            u3.f3989C.removeCallbacks(u3.f4016x);
            U.this.f4016x.run();
        }
    }

    private class h implements View.OnTouchListener {
        h() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            PopupWindow popupWindow;
            int action = motionEvent.getAction();
            int x3 = (int) motionEvent.getX();
            int y3 = (int) motionEvent.getY();
            if (action == 0 && (popupWindow = U.this.f3993G) != null && popupWindow.isShowing() && x3 >= 0 && x3 < U.this.f3993G.getWidth() && y3 >= 0 && y3 < U.this.f3993G.getHeight()) {
                U u3 = U.this;
                u3.f3989C.postDelayed(u3.f4016x, 250L);
                return false;
            }
            if (action != 1) {
                return false;
            }
            U u4 = U.this;
            u4.f3989C.removeCallbacks(u4.f4016x);
            return false;
        }
    }

    private class i implements Runnable {
        i() {
        }

        @Override // java.lang.Runnable
        public void run() {
            P p3 = U.this.f3996d;
            if (p3 == null || !p3.isAttachedToWindow() || U.this.f3996d.getCount() <= U.this.f3996d.getChildCount()) {
                return;
            }
            int childCount = U.this.f3996d.getChildCount();
            U u3 = U.this;
            if (childCount <= u3.f4008p) {
                u3.f3993G.setInputMethodMode(2);
                U.this.b();
            }
        }
    }

    static {
        if (Build.VERSION.SDK_INT <= 28) {
            try {
                f3985H = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", Boolean.TYPE);
            } catch (NoSuchMethodException unused) {
                Log.i("ListPopupWindow", "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
            }
            try {
                f3986I = PopupWindow.class.getDeclaredMethod("setEpicenterBounds", Rect.class);
            } catch (NoSuchMethodException unused2) {
                Log.i("ListPopupWindow", "Could not find method setEpicenterBounds(Rect) on PopupWindow. Oh well.");
            }
        }
    }

    public U(Context context) {
        this(context, null, AbstractC0487a.f8661F);
    }

    private void J(boolean z3) {
        if (Build.VERSION.SDK_INT > 28) {
            d.b(this.f3993G, z3);
            return;
        }
        Method method = f3985H;
        if (method != null) {
            try {
                method.invoke(this.f3993G, Boolean.valueOf(z3));
            } catch (Exception unused) {
                Log.i("ListPopupWindow", "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
            }
        }
    }

    private int q() {
        int measuredHeight;
        int i3;
        int iMakeMeasureSpec;
        View view;
        int i4;
        if (this.f3996d == null) {
            Context context = this.f3994b;
            this.f3988B = new a();
            P pS = s(context, !this.f3992F);
            this.f3996d = pS;
            Drawable drawable = this.f4013u;
            if (drawable != null) {
                pS.setSelector(drawable);
            }
            this.f3996d.setAdapter(this.f3995c);
            this.f3996d.setOnItemClickListener(this.f4014v);
            this.f3996d.setFocusable(true);
            this.f3996d.setFocusableInTouchMode(true);
            this.f3996d.setOnItemSelectedListener(new b());
            this.f3996d.setOnScrollListener(this.f4018z);
            AdapterView.OnItemSelectedListener onItemSelectedListener = this.f4015w;
            if (onItemSelectedListener != null) {
                this.f3996d.setOnItemSelectedListener(onItemSelectedListener);
            }
            P p3 = this.f3996d;
            View view2 = this.f4009q;
            if (view2 != null) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(1);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 0, 1.0f);
                int i5 = this.f4010r;
                if (i5 == 0) {
                    linearLayout.addView(view2);
                    linearLayout.addView(p3, layoutParams);
                } else if (i5 != 1) {
                    Log.e("ListPopupWindow", "Invalid hint position " + this.f4010r);
                } else {
                    linearLayout.addView(p3, layoutParams);
                    linearLayout.addView(view2);
                }
                int i6 = this.f3998f;
                if (i6 >= 0) {
                    i4 = Integer.MIN_VALUE;
                } else {
                    i6 = 0;
                    i4 = 0;
                }
                view2.measure(View.MeasureSpec.makeMeasureSpec(i6, i4), 0);
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) view2.getLayoutParams();
                measuredHeight = view2.getMeasuredHeight() + layoutParams2.topMargin + layoutParams2.bottomMargin;
                view = linearLayout;
            } else {
                measuredHeight = 0;
                view = p3;
            }
            this.f3993G.setContentView(view);
        } else {
            View view3 = this.f4009q;
            if (view3 != null) {
                LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) view3.getLayoutParams();
                measuredHeight = view3.getMeasuredHeight() + layoutParams3.topMargin + layoutParams3.bottomMargin;
            } else {
                measuredHeight = 0;
            }
        }
        Drawable background = this.f3993G.getBackground();
        if (background != null) {
            background.getPadding(this.f3990D);
            Rect rect = this.f3990D;
            int i7 = rect.top;
            i3 = rect.bottom + i7;
            if (!this.f4002j) {
                this.f4000h = -i7;
            }
        } else {
            this.f3990D.setEmpty();
            i3 = 0;
        }
        int iU = u(t(), this.f4000h, this.f3993G.getInputMethodMode() == 2);
        if (this.f4006n || this.f3997e == -1) {
            return iU + i3;
        }
        int i8 = this.f3998f;
        if (i8 == -2) {
            int i9 = this.f3994b.getResources().getDisplayMetrics().widthPixels;
            Rect rect2 = this.f3990D;
            iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i9 - (rect2.left + rect2.right), Integer.MIN_VALUE);
        } else if (i8 != -1) {
            iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i8, 1073741824);
        } else {
            int i10 = this.f3994b.getResources().getDisplayMetrics().widthPixels;
            Rect rect3 = this.f3990D;
            iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i10 - (rect3.left + rect3.right), 1073741824);
        }
        int iD = this.f3996d.d(iMakeMeasureSpec, 0, -1, iU - measuredHeight, -1);
        if (iD > 0) {
            measuredHeight += i3 + this.f3996d.getPaddingTop() + this.f3996d.getPaddingBottom();
        }
        return iD + measuredHeight;
    }

    private int u(View view, int i3, boolean z3) {
        return c.a(this.f3993G, view, i3, z3);
    }

    private void y() {
        View view = this.f4009q;
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.f4009q);
            }
        }
    }

    public void A(int i3) {
        this.f3993G.setAnimationStyle(i3);
    }

    public void B(int i3) {
        Drawable background = this.f3993G.getBackground();
        if (background == null) {
            M(i3);
            return;
        }
        background.getPadding(this.f3990D);
        Rect rect = this.f3990D;
        this.f3998f = rect.left + rect.right + i3;
    }

    public void C(int i3) {
        this.f4005m = i3;
    }

    public void D(Rect rect) {
        this.f3991E = rect != null ? new Rect(rect) : null;
    }

    public void E(int i3) {
        this.f3993G.setInputMethodMode(i3);
    }

    public void F(boolean z3) {
        this.f3992F = z3;
        this.f3993G.setFocusable(z3);
    }

    public void G(PopupWindow.OnDismissListener onDismissListener) {
        this.f3993G.setOnDismissListener(onDismissListener);
    }

    public void H(AdapterView.OnItemClickListener onItemClickListener) {
        this.f4014v = onItemClickListener;
    }

    public void I(boolean z3) {
        this.f4004l = true;
        this.f4003k = z3;
    }

    public void K(int i3) {
        this.f4010r = i3;
    }

    public void L(int i3) {
        P p3 = this.f3996d;
        if (!a() || p3 == null) {
            return;
        }
        p3.setListSelectionHidden(false);
        p3.setSelection(i3);
        if (p3.getChoiceMode() != 0) {
            p3.setItemChecked(i3, true);
        }
    }

    public void M(int i3) {
        this.f3998f = i3;
    }

    @Override // i.e
    public boolean a() {
        return this.f3993G.isShowing();
    }

    @Override // i.e
    public void b() {
        int iQ = q();
        boolean zW = w();
        androidx.core.widget.h.b(this.f3993G, this.f4001i);
        if (this.f3993G.isShowing()) {
            if (t().isAttachedToWindow()) {
                int width = this.f3998f;
                if (width == -1) {
                    width = -1;
                } else if (width == -2) {
                    width = t().getWidth();
                }
                int i3 = this.f3997e;
                if (i3 == -1) {
                    if (!zW) {
                        iQ = -1;
                    }
                    if (zW) {
                        this.f3993G.setWidth(this.f3998f == -1 ? -1 : 0);
                        this.f3993G.setHeight(0);
                    } else {
                        this.f3993G.setWidth(this.f3998f == -1 ? -1 : 0);
                        this.f3993G.setHeight(-1);
                    }
                } else if (i3 != -2) {
                    iQ = i3;
                }
                this.f3993G.setOutsideTouchable((this.f4007o || this.f4006n) ? false : true);
                this.f3993G.update(t(), this.f3999g, this.f4000h, width < 0 ? -1 : width, iQ < 0 ? -1 : iQ);
                return;
            }
            return;
        }
        int width2 = this.f3998f;
        if (width2 == -1) {
            width2 = -1;
        } else if (width2 == -2) {
            width2 = t().getWidth();
        }
        int i4 = this.f3997e;
        if (i4 == -1) {
            iQ = -1;
        } else if (i4 != -2) {
            iQ = i4;
        }
        this.f3993G.setWidth(width2);
        this.f3993G.setHeight(iQ);
        J(true);
        this.f3993G.setOutsideTouchable((this.f4007o || this.f4006n) ? false : true);
        this.f3993G.setTouchInterceptor(this.f4017y);
        if (this.f4004l) {
            androidx.core.widget.h.a(this.f3993G, this.f4003k);
        }
        if (Build.VERSION.SDK_INT <= 28) {
            Method method = f3986I;
            if (method != null) {
                try {
                    method.invoke(this.f3993G, this.f3991E);
                } catch (Exception e4) {
                    Log.e("ListPopupWindow", "Could not invoke setEpicenterBounds on PopupWindow", e4);
                }
            }
        } else {
            d.a(this.f3993G, this.f3991E);
        }
        androidx.core.widget.h.c(this.f3993G, t(), this.f3999g, this.f4000h, this.f4005m);
        this.f3996d.setSelection(-1);
        if (!this.f3992F || this.f3996d.isInTouchMode()) {
            r();
        }
        if (this.f3992F) {
            return;
        }
        this.f3989C.post(this.f3987A);
    }

    public int c() {
        return this.f3999g;
    }

    @Override // i.e
    public void dismiss() {
        this.f3993G.dismiss();
        y();
        this.f3993G.setContentView(null);
        this.f3996d = null;
        this.f3989C.removeCallbacks(this.f4016x);
    }

    public Drawable f() {
        return this.f3993G.getBackground();
    }

    @Override // i.e
    public ListView g() {
        return this.f3996d;
    }

    public void i(Drawable drawable) {
        this.f3993G.setBackgroundDrawable(drawable);
    }

    public void j(int i3) {
        this.f4000h = i3;
        this.f4002j = true;
    }

    public void l(int i3) {
        this.f3999g = i3;
    }

    public int n() {
        if (this.f4002j) {
            return this.f4000h;
        }
        return 0;
    }

    public void p(ListAdapter listAdapter) {
        DataSetObserver dataSetObserver = this.f4011s;
        if (dataSetObserver == null) {
            this.f4011s = new f();
        } else {
            ListAdapter listAdapter2 = this.f3995c;
            if (listAdapter2 != null) {
                listAdapter2.unregisterDataSetObserver(dataSetObserver);
            }
        }
        this.f3995c = listAdapter;
        if (listAdapter != null) {
            listAdapter.registerDataSetObserver(this.f4011s);
        }
        P p3 = this.f3996d;
        if (p3 != null) {
            p3.setAdapter(this.f3995c);
        }
    }

    public void r() {
        P p3 = this.f3996d;
        if (p3 != null) {
            p3.setListSelectionHidden(true);
            p3.requestLayout();
        }
    }

    P s(Context context, boolean z3) {
        return new P(context, z3);
    }

    public View t() {
        return this.f4012t;
    }

    public int v() {
        return this.f3998f;
    }

    public boolean w() {
        return this.f3993G.getInputMethodMode() == 2;
    }

    public boolean x() {
        return this.f3992F;
    }

    public void z(View view) {
        this.f4012t = view;
    }

    public U(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8661F);
    }

    public U(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public U(Context context, AttributeSet attributeSet, int i3, int i4) {
        this.f3997e = -2;
        this.f3998f = -2;
        this.f4001i = 1002;
        this.f4005m = 0;
        this.f4006n = false;
        this.f4007o = false;
        this.f4008p = Integer.MAX_VALUE;
        this.f4010r = 0;
        this.f4016x = new i();
        this.f4017y = new h();
        this.f4018z = new g();
        this.f3987A = new e();
        this.f3990D = new Rect();
        this.f3994b = context;
        this.f3989C = new Handler(context.getMainLooper());
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, d.j.f9003l1, i3, i4);
        this.f3999g = typedArrayObtainStyledAttributes.getDimensionPixelOffset(d.j.f9007m1, 0);
        int dimensionPixelOffset = typedArrayObtainStyledAttributes.getDimensionPixelOffset(d.j.f9011n1, 0);
        this.f4000h = dimensionPixelOffset;
        if (dimensionPixelOffset != 0) {
            this.f4002j = true;
        }
        typedArrayObtainStyledAttributes.recycle();
        C0230t c0230t = new C0230t(context, attributeSet, i3, i4);
        this.f3993G = c0230t;
        c0230t.setInputMethodMode(1);
    }
}
