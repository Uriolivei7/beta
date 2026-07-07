package androidx.fragment.app;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import androidx.core.view.C0264n0;
import androidx.core.view.Z;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: androidx.fragment.app.m, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0291m extends FrameLayout {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final List f5161b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final List f5162c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private View.OnApplyWindowInsetsListener f5163d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f5164e;

    /* JADX INFO: renamed from: androidx.fragment.app.m$a */
    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final a f5165a = new a();

        private a() {
        }

        public final WindowInsets a(View.OnApplyWindowInsetsListener onApplyWindowInsetsListener, View view, WindowInsets windowInsets) {
            D2.h.f(onApplyWindowInsetsListener, "onApplyWindowInsetsListener");
            D2.h.f(view, "v");
            D2.h.f(windowInsets, "insets");
            WindowInsets windowInsetsOnApplyWindowInsets = onApplyWindowInsetsListener.onApplyWindowInsets(view, windowInsets);
            D2.h.e(windowInsetsOnApplyWindowInsets, "onApplyWindowInsetsListe…lyWindowInsets(v, insets)");
            return windowInsetsOnApplyWindowInsets;
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public C0291m(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        D2.h.f(context, "context");
    }

    private final void a(View view) {
        if (this.f5162c.contains(view)) {
            this.f5161b.add(view);
        }
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i3, ViewGroup.LayoutParams layoutParams) {
        D2.h.f(view, "child");
        if (x.A0(view) != null) {
            super.addView(view, i3, layoutParams);
            return;
        }
        throw new IllegalStateException(("Views added to a FragmentContainerView must be associated with a Fragment. View " + view + " is not associated with a Fragment.").toString());
    }

    @Override // android.view.ViewGroup, android.view.View
    public WindowInsets dispatchApplyWindowInsets(WindowInsets windowInsets) {
        C0264n0 c0264n0M;
        D2.h.f(windowInsets, "insets");
        C0264n0 c0264n0V = C0264n0.v(windowInsets);
        D2.h.e(c0264n0V, "toWindowInsetsCompat(insets)");
        View.OnApplyWindowInsetsListener onApplyWindowInsetsListener = this.f5163d;
        if (onApplyWindowInsetsListener != null) {
            a aVar = a.f5165a;
            D2.h.c(onApplyWindowInsetsListener);
            c0264n0M = C0264n0.v(aVar.a(onApplyWindowInsetsListener, this, windowInsets));
        } else {
            c0264n0M = Z.M(this, c0264n0V);
        }
        D2.h.e(c0264n0M, "if (applyWindowInsetsLis…, insetsCompat)\n        }");
        if (!c0264n0M.n()) {
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                Z.e(getChildAt(i3), c0264n0M);
            }
        }
        return windowInsets;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        D2.h.f(canvas, "canvas");
        if (this.f5164e) {
            Iterator it = this.f5161b.iterator();
            while (it.hasNext()) {
                super.drawChild(canvas, (View) it.next(), getDrawingTime());
            }
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j3) {
        D2.h.f(canvas, "canvas");
        D2.h.f(view, "child");
        if (this.f5164e && !this.f5161b.isEmpty() && this.f5161b.contains(view)) {
            return false;
        }
        return super.drawChild(canvas, view, j3);
    }

    @Override // android.view.ViewGroup
    public void endViewTransition(View view) {
        D2.h.f(view, "view");
        this.f5162c.remove(view);
        if (this.f5161b.remove(view)) {
            this.f5164e = true;
        }
        super.endViewTransition(view);
    }

    public final <F extends Fragment> F getFragment() {
        return (F) x.j0(this).g0(getId());
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        D2.h.f(windowInsets, "insets");
        return windowInsets;
    }

    @Override // android.view.ViewGroup
    public void removeAllViewsInLayout() {
        int childCount = getChildCount();
        while (true) {
            childCount--;
            if (-1 >= childCount) {
                super.removeAllViewsInLayout();
                return;
            } else {
                View childAt = getChildAt(childCount);
                D2.h.e(childAt, "view");
                a(childAt);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        D2.h.f(view, "view");
        a(view);
        super.removeView(view);
    }

    @Override // android.view.ViewGroup
    public void removeViewAt(int i3) {
        View childAt = getChildAt(i3);
        D2.h.e(childAt, "view");
        a(childAt);
        super.removeViewAt(i3);
    }

    @Override // android.view.ViewGroup
    public void removeViewInLayout(View view) {
        D2.h.f(view, "view");
        a(view);
        super.removeViewInLayout(view);
    }

    @Override // android.view.ViewGroup
    public void removeViews(int i3, int i4) {
        int i5 = i3 + i4;
        for (int i6 = i3; i6 < i5; i6++) {
            View childAt = getChildAt(i6);
            D2.h.e(childAt, "view");
            a(childAt);
        }
        super.removeViews(i3, i4);
    }

    @Override // android.view.ViewGroup
    public void removeViewsInLayout(int i3, int i4) {
        int i5 = i3 + i4;
        for (int i6 = i3; i6 < i5; i6++) {
            View childAt = getChildAt(i6);
            D2.h.e(childAt, "view");
            a(childAt);
        }
        super.removeViewsInLayout(i3, i4);
    }

    public final void setDrawDisappearingViewsLast(boolean z3) {
        this.f5164e = z3;
    }

    @Override // android.view.ViewGroup
    public void setLayoutTransition(LayoutTransition layoutTransition) {
        throw new UnsupportedOperationException("FragmentContainerView does not support Layout Transitions or animateLayoutChanges=\"true\".");
    }

    @Override // android.view.View
    public void setOnApplyWindowInsetsListener(View.OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        D2.h.f(onApplyWindowInsetsListener, "listener");
        this.f5163d = onApplyWindowInsetsListener;
    }

    @Override // android.view.ViewGroup
    public void startViewTransition(View view) {
        D2.h.f(view, "view");
        if (view.getParent() == this) {
            this.f5162c.add(view);
        }
        super.startViewTransition(view);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0291m(Context context) {
        super(context);
        D2.h.f(context, "context");
        this.f5161b = new ArrayList();
        this.f5162c = new ArrayList();
        this.f5164e = true;
    }

    public /* synthetic */ C0291m(Context context, AttributeSet attributeSet, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, attributeSet, (i4 & 4) != 0 ? 0 : i3);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0291m(Context context, AttributeSet attributeSet, int i3) {
        String str;
        super(context, attributeSet, i3);
        D2.h.f(context, "context");
        this.f5161b = new ArrayList();
        this.f5162c = new ArrayList();
        this.f5164e = true;
        if (attributeSet != null) {
            String classAttribute = attributeSet.getClassAttribute();
            int[] iArr = B.c.f49e;
            D2.h.e(iArr, "FragmentContainerView");
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, 0, 0);
            if (classAttribute == null) {
                classAttribute = typedArrayObtainStyledAttributes.getString(B.c.f50f);
                str = "android:name";
            } else {
                str = "class";
            }
            typedArrayObtainStyledAttributes.recycle();
            if (classAttribute == null || isInEditMode()) {
                return;
            }
            throw new UnsupportedOperationException("FragmentContainerView must be within a FragmentActivity to use " + str + "=\"" + classAttribute + '\"');
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0291m(Context context, AttributeSet attributeSet, x xVar) {
        String str;
        super(context, attributeSet);
        D2.h.f(context, "context");
        D2.h.f(attributeSet, "attrs");
        D2.h.f(xVar, "fm");
        this.f5161b = new ArrayList();
        this.f5162c = new ArrayList();
        this.f5164e = true;
        String classAttribute = attributeSet.getClassAttribute();
        int[] iArr = B.c.f49e;
        D2.h.e(iArr, "FragmentContainerView");
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, 0, 0);
        classAttribute = classAttribute == null ? typedArrayObtainStyledAttributes.getString(B.c.f50f) : classAttribute;
        String string = typedArrayObtainStyledAttributes.getString(B.c.f51g);
        typedArrayObtainStyledAttributes.recycle();
        int id = getId();
        Fragment fragmentG0 = xVar.g0(id);
        if (classAttribute != null && fragmentG0 == null) {
            if (id == -1) {
                if (string != null) {
                    str = " with tag " + string;
                } else {
                    str = "";
                }
                throw new IllegalStateException("FragmentContainerView must have an android:id to add Fragment " + classAttribute + str);
            }
            Fragment fragmentA = xVar.r0().a(context.getClassLoader(), classAttribute);
            D2.h.e(fragmentA, "fm.fragmentFactory.insta…ontext.classLoader, name)");
            fragmentA.w0(context, attributeSet, null);
            xVar.o().m(true).c(this, fragmentA, string).i();
        }
        xVar.V0(this);
    }
}
