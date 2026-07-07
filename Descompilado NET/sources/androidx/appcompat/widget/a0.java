package androidx.appcompat.widget;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.appcompat.app.a;
import androidx.appcompat.widget.T;
import d.AbstractC0487a;

/* JADX INFO: loaded from: classes.dex */
public class a0 extends HorizontalScrollView implements AdapterView.OnItemSelectedListener {

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static final Interpolator f4066m = new DecelerateInterpolator();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    Runnable f4067b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private c f4068c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    T f4069d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Spinner f4070e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f4071f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    int f4072g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    int f4073h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f4074i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f4075j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    protected ViewPropertyAnimator f4076k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    protected final e f4077l;

    class a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f4078b;

        a(View view) {
            this.f4078b = view;
        }

        @Override // java.lang.Runnable
        public void run() {
            a0.this.smoothScrollTo(this.f4078b.getLeft() - ((a0.this.getWidth() - this.f4078b.getWidth()) / 2), 0);
            a0.this.f4067b = null;
        }
    }

    private class b extends BaseAdapter {
        b() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return a0.this.f4069d.getChildCount();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i3) {
            return ((d) a0.this.f4069d.getChildAt(i3)).b();
        }

        @Override // android.widget.Adapter
        public long getItemId(int i3) {
            return i3;
        }

        @Override // android.widget.Adapter
        public View getView(int i3, View view, ViewGroup viewGroup) {
            if (view == null) {
                return a0.this.d((a.b) getItem(i3), true);
            }
            ((d) view).a((a.b) getItem(i3));
            return view;
        }
    }

    private class c implements View.OnClickListener {
        c() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ((d) view).b().e();
            int childCount = a0.this.f4069d.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = a0.this.f4069d.getChildAt(i3);
                childAt.setSelected(childAt == view);
            }
        }
    }

    private class d extends LinearLayout {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int[] f4082b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private a.b f4083c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private TextView f4084d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private ImageView f4085e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private View f4086f;

        public d(Context context, a.b bVar, boolean z3) {
            super(context, null, AbstractC0487a.f8677e);
            int[] iArr = {R.attr.background};
            this.f4082b = iArr;
            this.f4083c = bVar;
            h0 h0VarU = h0.u(context, null, iArr, AbstractC0487a.f8677e, 0);
            if (h0VarU.r(0)) {
                setBackgroundDrawable(h0VarU.f(0));
            }
            h0VarU.w();
            if (z3) {
                setGravity(8388627);
            }
            c();
        }

        public void a(a.b bVar) {
            this.f4083c = bVar;
            c();
        }

        public a.b b() {
            return this.f4083c;
        }

        public void c() {
            a.b bVar = this.f4083c;
            View viewB = bVar.b();
            if (viewB != null) {
                ViewParent parent = viewB.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(viewB);
                    }
                    addView(viewB);
                }
                this.f4086f = viewB;
                TextView textView = this.f4084d;
                if (textView != null) {
                    textView.setVisibility(8);
                }
                ImageView imageView = this.f4085e;
                if (imageView != null) {
                    imageView.setVisibility(8);
                    this.f4085e.setImageDrawable(null);
                    return;
                }
                return;
            }
            View view = this.f4086f;
            if (view != null) {
                removeView(view);
                this.f4086f = null;
            }
            Drawable drawableC = bVar.c();
            CharSequence charSequenceD = bVar.d();
            if (drawableC != null) {
                if (this.f4085e == null) {
                    r rVar = new r(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                    layoutParams.gravity = 16;
                    rVar.setLayoutParams(layoutParams);
                    addView(rVar, 0);
                    this.f4085e = rVar;
                }
                this.f4085e.setImageDrawable(drawableC);
                this.f4085e.setVisibility(0);
            } else {
                ImageView imageView2 = this.f4085e;
                if (imageView2 != null) {
                    imageView2.setVisibility(8);
                    this.f4085e.setImageDrawable(null);
                }
            }
            boolean zIsEmpty = TextUtils.isEmpty(charSequenceD);
            if (zIsEmpty) {
                TextView textView2 = this.f4084d;
                if (textView2 != null) {
                    textView2.setVisibility(8);
                    this.f4084d.setText((CharSequence) null);
                }
            } else {
                if (this.f4084d == null) {
                    D d4 = new D(getContext(), null, AbstractC0487a.f8678f);
                    d4.setEllipsize(TextUtils.TruncateAt.END);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
                    layoutParams2.gravity = 16;
                    d4.setLayoutParams(layoutParams2);
                    addView(d4);
                    this.f4084d = d4;
                }
                this.f4084d.setText(charSequenceD);
                this.f4084d.setVisibility(0);
            }
            ImageView imageView3 = this.f4085e;
            if (imageView3 != null) {
                imageView3.setContentDescription(bVar.a());
            }
            m0.a(this, zIsEmpty ? bVar.a() : null);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName("androidx.appcompat.app.ActionBar$Tab");
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName("androidx.appcompat.app.ActionBar$Tab");
        }

        @Override // android.widget.LinearLayout, android.view.View
        public void onMeasure(int i3, int i4) {
            super.onMeasure(i3, i4);
            if (a0.this.f4072g > 0) {
                int measuredWidth = getMeasuredWidth();
                int i5 = a0.this.f4072g;
                if (measuredWidth > i5) {
                    super.onMeasure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), i4);
                }
            }
        }

        @Override // android.view.View
        public void setSelected(boolean z3) {
            boolean z4 = isSelected() != z3;
            super.setSelected(z3);
            if (z4 && z3) {
                sendAccessibilityEvent(4);
            }
        }
    }

    protected class e extends AnimatorListenerAdapter {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean f4088a = false;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f4089b;

        protected e() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f4088a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f4088a) {
                return;
            }
            a0 a0Var = a0.this;
            a0Var.f4076k = null;
            a0Var.setVisibility(this.f4089b);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            a0.this.setVisibility(0);
            this.f4088a = false;
        }
    }

    public a0(Context context) {
        super(context);
        this.f4077l = new e();
        setHorizontalScrollBarEnabled(false);
        androidx.appcompat.view.a aVarB = androidx.appcompat.view.a.b(context);
        setContentHeight(aVarB.f());
        this.f4073h = aVarB.e();
        T tC = c();
        this.f4069d = tC;
        addView(tC, new ViewGroup.LayoutParams(-2, -1));
    }

    private Spinner b() {
        A a4 = new A(getContext(), null, AbstractC0487a.f8681i);
        a4.setLayoutParams(new T.a(-2, -1));
        a4.setOnItemSelectedListener(this);
        return a4;
    }

    private T c() {
        T t3 = new T(getContext(), null, AbstractC0487a.f8676d);
        t3.setMeasureWithLargestChildEnabled(true);
        t3.setGravity(17);
        t3.setLayoutParams(new T.a(-2, -1));
        return t3;
    }

    private boolean e() {
        Spinner spinner = this.f4070e;
        return spinner != null && spinner.getParent() == this;
    }

    private void f() {
        if (e()) {
            return;
        }
        if (this.f4070e == null) {
            this.f4070e = b();
        }
        removeView(this.f4069d);
        addView(this.f4070e, new ViewGroup.LayoutParams(-2, -1));
        if (this.f4070e.getAdapter() == null) {
            this.f4070e.setAdapter((SpinnerAdapter) new b());
        }
        Runnable runnable = this.f4067b;
        if (runnable != null) {
            removeCallbacks(runnable);
            this.f4067b = null;
        }
        this.f4070e.setSelection(this.f4075j);
    }

    private boolean g() {
        if (!e()) {
            return false;
        }
        removeView(this.f4070e);
        addView(this.f4069d, new ViewGroup.LayoutParams(-2, -1));
        setTabSelected(this.f4070e.getSelectedItemPosition());
        return false;
    }

    public void a(int i3) {
        View childAt = this.f4069d.getChildAt(i3);
        Runnable runnable = this.f4067b;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        a aVar = new a(childAt);
        this.f4067b = aVar;
        post(aVar);
    }

    d d(a.b bVar, boolean z3) {
        d dVar = new d(getContext(), bVar, z3);
        if (z3) {
            dVar.setBackgroundDrawable(null);
            dVar.setLayoutParams(new AbsListView.LayoutParams(-1, this.f4074i));
        } else {
            dVar.setFocusable(true);
            if (this.f4068c == null) {
                this.f4068c = new c();
            }
            dVar.setOnClickListener(this.f4068c);
        }
        return dVar;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Runnable runnable = this.f4067b;
        if (runnable != null) {
            post(runnable);
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        androidx.appcompat.view.a aVarB = androidx.appcompat.view.a.b(getContext());
        setContentHeight(aVarB.f());
        this.f4073h = aVarB.e();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.f4067b;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView adapterView, View view, int i3, long j3) {
        ((d) view).b().e();
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        int mode = View.MeasureSpec.getMode(i3);
        boolean z3 = mode == 1073741824;
        setFillViewport(z3);
        int childCount = this.f4069d.getChildCount();
        if (childCount <= 1 || !(mode == 1073741824 || mode == Integer.MIN_VALUE)) {
            this.f4072g = -1;
        } else {
            if (childCount > 2) {
                this.f4072g = (int) (View.MeasureSpec.getSize(i3) * 0.4f);
            } else {
                this.f4072g = View.MeasureSpec.getSize(i3) / 2;
            }
            this.f4072g = Math.min(this.f4072g, this.f4073h);
        }
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.f4074i, 1073741824);
        if (z3 || !this.f4071f) {
            g();
        } else {
            this.f4069d.measure(0, iMakeMeasureSpec);
            if (this.f4069d.getMeasuredWidth() > View.MeasureSpec.getSize(i3)) {
                f();
            } else {
                g();
            }
        }
        int measuredWidth = getMeasuredWidth();
        super.onMeasure(i3, iMakeMeasureSpec);
        int measuredWidth2 = getMeasuredWidth();
        if (!z3 || measuredWidth == measuredWidth2) {
            return;
        }
        setTabSelected(this.f4075j);
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView adapterView) {
    }

    public void setAllowCollapse(boolean z3) {
        this.f4071f = z3;
    }

    public void setContentHeight(int i3) {
        this.f4074i = i3;
        requestLayout();
    }

    public void setTabSelected(int i3) {
        this.f4075j = i3;
        int childCount = this.f4069d.getChildCount();
        int i4 = 0;
        while (i4 < childCount) {
            View childAt = this.f4069d.getChildAt(i4);
            boolean z3 = i4 == i3;
            childAt.setSelected(z3);
            if (z3) {
                a(i3);
            }
            i4++;
        }
        Spinner spinner = this.f4070e;
        if (spinner == null || i3 < 0) {
            return;
        }
        spinner.setSelection(i3);
    }
}
