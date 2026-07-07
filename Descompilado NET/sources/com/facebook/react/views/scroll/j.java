package com.facebook.react.views.scroll;

import K2.o;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.A0;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.events.EventDispatcher;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class j {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final boolean f7860c = false;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static boolean f7864g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final j f7858a = new j();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f7859b = com.facebook.react.views.scroll.g.class.getSimpleName();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final CopyOnWriteArrayList f7861d = new CopyOnWriteArrayList();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final CopyOnWriteArrayList f7862e = new CopyOnWriteArrayList();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static int f7863f = 250;

    public interface a {
        void a(int i3, int i4);

        ValueAnimator getFlingAnimator();
    }

    public interface b {
        long getLastScrollDispatchTime();

        int getScrollEventThrottle();

        void setLastScrollDispatchTime(long j3);
    }

    public interface c {
        g getReactScrollViewScrollState();
    }

    public interface d {
        void b(int i3, int i4);

        void f(int i3, int i4);
    }

    public interface e {
        A0 getStateWrapper();
    }

    private static final class f extends OverScroller {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f7865a;

        public f(Context context) {
            super(context);
            this.f7865a = 250;
        }

        public final int a() {
            super.startScroll(0, 0, 0, 0);
            return this.f7865a;
        }

        @Override // android.widget.OverScroller
        public void startScroll(int i3, int i4, int i5, int i6, int i7) {
            this.f7865a = i7;
        }
    }

    public static final class g {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f7867b;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f7869d;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Point f7866a = new Point();

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final Point f7868c = new Point(-1, -1);

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f7870e = true;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private float f7871f = 0.985f;

        public final float a() {
            return this.f7871f;
        }

        public final Point b() {
            return this.f7866a;
        }

        public final Point c() {
            return this.f7868c;
        }

        public final int d() {
            return this.f7867b;
        }

        public final boolean e() {
            return this.f7869d;
        }

        public final boolean f() {
            return this.f7870e;
        }

        public final void g(boolean z3) {
            this.f7869d = z3;
        }

        public final void h(float f3) {
            this.f7871f = f3;
        }

        public final g i(int i3, int i4) {
            this.f7866a.set(i3, i4);
            return this;
        }

        public final void j(boolean z3) {
            this.f7870e = z3;
        }

        public final g k(int i3, int i4) {
            this.f7868c.set(i3, i4);
            return this;
        }

        public final void l(int i3) {
            this.f7867b = i3;
        }
    }

    public static final class h implements Animator.AnimatorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewGroup f7872a;

        h(ViewGroup viewGroup) {
            this.f7872a = viewGroup;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            D2.h.f(animator, "animator");
            j.j(this.f7872a);
            animator.removeListener(this);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            D2.h.f(animator, "animator");
            j.j(this.f7872a);
            animator.removeListener(this);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            D2.h.f(animator, "animator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            D2.h.f(animator, "animator");
        }
    }

    public static final class i implements Animator.AnimatorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewGroup f7873a;

        i(ViewGroup viewGroup) {
            this.f7873a = viewGroup;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            D2.h.f(animator, "animator");
            ((c) this.f7873a).getReactScrollViewScrollState().g(true);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            D2.h.f(animator, "animator");
            ((c) this.f7873a).getReactScrollViewScrollState().j(true);
            j.s(this.f7873a);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            D2.h.f(animator, "animator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            D2.h.f(animator, "animator");
            g reactScrollViewScrollState = ((c) this.f7873a).getReactScrollViewScrollState();
            reactScrollViewScrollState.g(false);
            reactScrollViewScrollState.j(false);
        }
    }

    private j() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final void a(ViewGroup viewGroup) {
        ((a) viewGroup).getFlingAnimator().addListener(new h(viewGroup));
    }

    public static final void b(ViewGroup viewGroup) {
        D2.h.f(viewGroup, "scrollView");
        Iterator it = f7862e.iterator();
        D2.h.e(it, "iterator(...)");
        while (it.hasNext()) {
            androidx.activity.result.d.a(((WeakReference) it.next()).get());
        }
    }

    public static final void c(ViewGroup viewGroup) {
        D2.h.f(viewGroup, "scrollView");
        Iterator it = f7861d.iterator();
        D2.h.e(it, "iterator(...)");
        while (it.hasNext()) {
            androidx.activity.result.d.a(((WeakReference) it.next()).get());
        }
    }

    public static final void d(ViewGroup viewGroup) {
        f7858a.g(viewGroup, l.f7888c);
    }

    public static final void e(ViewGroup viewGroup, float f3, float f4) {
        f7858a.h(viewGroup, l.f7889d, f3, f4);
    }

    public static final void f(ViewGroup viewGroup, float f3, float f4) {
        f7858a.h(viewGroup, l.f7890e, f3, f4);
    }

    private final void g(ViewGroup viewGroup, l lVar) {
        h(viewGroup, lVar, 0.0f, 0.0f);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void h(ViewGroup viewGroup, l lVar, float f3, float f4) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (lVar == l.f7890e) {
            if (r1.getScrollEventThrottle() >= Math.max(17L, jCurrentTimeMillis - ((b) viewGroup).getLastScrollDispatchTime())) {
                return;
            }
        }
        View childAt = viewGroup.getChildAt(0);
        if (childAt == null) {
            return;
        }
        Iterator it = AbstractC0717n.e0(f7861d).iterator();
        while (it.hasNext()) {
            androidx.activity.result.d.a(((WeakReference) it.next()).get());
        }
        Context context = viewGroup.getContext();
        D2.h.d(context, "null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
        ReactContext reactContext = (ReactContext) context;
        int iE = H0.e(reactContext);
        EventDispatcher eventDispatcherC = H0.c(reactContext, viewGroup.getId());
        if (eventDispatcherC != null) {
            eventDispatcherC.b(k.f7874r.a(iE, viewGroup.getId(), lVar, viewGroup.getScrollX(), viewGroup.getScrollY(), f3, f4, childAt.getWidth(), childAt.getHeight(), viewGroup.getWidth(), viewGroup.getHeight()));
            if (lVar == l.f7890e) {
                ((b) viewGroup).setLastScrollDispatchTime(jCurrentTimeMillis);
            }
        }
    }

    public static final void i(ViewGroup viewGroup, int i3, int i4) {
        f7858a.h(viewGroup, l.f7891f, i3, i4);
    }

    public static final void j(ViewGroup viewGroup) {
        f7858a.g(viewGroup, l.f7892g);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final void k(ViewGroup viewGroup) {
        g reactScrollViewScrollState = ((c) viewGroup).getReactScrollViewScrollState();
        int iD = reactScrollViewScrollState.d();
        Point pointC = reactScrollViewScrollState.c();
        int i3 = pointC.x;
        int i4 = pointC.y;
        if (f7860c) {
            Y.a.u(f7859b, "updateFabricScrollState[%d] scrollX %d scrollY %d", Integer.valueOf(viewGroup.getId()), Integer.valueOf(i3), Integer.valueOf(i4));
        }
        A0 stateWrapper = ((e) viewGroup).getStateWrapper();
        if (stateWrapper != null) {
            WritableNativeMap writableNativeMap = new WritableNativeMap();
            writableNativeMap.putDouble("contentOffsetLeft", C0429f0.f(i3));
            writableNativeMap.putDouble("contentOffsetTop", C0429f0.f(i4));
            writableNativeMap.putDouble("scrollAwayPaddingTop", C0429f0.f(iD));
            stateWrapper.b(writableNativeMap);
        }
    }

    public static final int l(Context context) {
        if (!f7864g) {
            f7864g = true;
            try {
                f7863f = new f(context).a();
            } catch (Throwable unused) {
            }
        }
        return f7863f;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final int m(ViewGroup viewGroup, int i3, int i4, int i5) {
        g reactScrollViewScrollState = ((c) viewGroup).getReactScrollViewScrollState();
        return (!reactScrollViewScrollState.f() || (reactScrollViewScrollState.e() && ((i5 != 0 ? i5 / Math.abs(i5) : 0) * (i4 - i3) > 0))) ? i4 : i3;
    }

    public static final int n(String str) {
        if (str == null) {
            return 1;
        }
        int iHashCode = str.hashCode();
        if (iHashCode != -1414557169) {
            if (iHashCode != 3005871) {
                if (iHashCode == 104712844 && str.equals("never")) {
                    return 2;
                }
            } else if (str.equals("auto")) {
                return 1;
            }
        } else if (str.equals("always")) {
            return 0;
        }
        Y.a.I("ReactNative", "wrong overScrollMode: " + str);
        return 1;
    }

    public static final int o(String str) {
        if (str == null) {
            return 0;
        }
        if (o.n("start", str, true)) {
            return 1;
        }
        if (o.n("center", str, true)) {
            return 2;
        }
        if (D2.h.b("end", str)) {
            return 3;
        }
        Y.a.I("ReactNative", "wrong snap alignment value: " + str);
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final Point p(ViewGroup viewGroup, int i3, int i4, int i5, int i6) {
        g reactScrollViewScrollState = ((c) viewGroup).getReactScrollViewScrollState();
        OverScroller overScroller = new OverScroller(viewGroup.getContext());
        overScroller.setFriction(1.0f - reactScrollViewScrollState.a());
        int width = (viewGroup.getWidth() - viewGroup.getPaddingStart()) - viewGroup.getPaddingEnd();
        int height = (viewGroup.getHeight() - viewGroup.getPaddingBottom()) - viewGroup.getPaddingTop();
        Point pointB = reactScrollViewScrollState.b();
        overScroller.fling(m(viewGroup, viewGroup.getScrollX(), pointB.x, i3), m(viewGroup, viewGroup.getScrollY(), pointB.y, i4), i3, i4, 0, i5, 0, i6, width / 2, height / 2);
        return new Point(overScroller.getFinalX(), overScroller.getFinalY());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final void r(ViewGroup viewGroup, int i3, int i4) {
        if (f7860c) {
            Y.a.u(f7859b, "smoothScrollTo[%d] x %d y %d", Integer.valueOf(viewGroup.getId()), Integer.valueOf(i3), Integer.valueOf(i4));
        }
        a aVar = (a) viewGroup;
        ValueAnimator flingAnimator = aVar.getFlingAnimator();
        if (flingAnimator.getListeners() == null || flingAnimator.getListeners().size() == 0) {
            f7858a.q(viewGroup);
        }
        ((c) viewGroup).getReactScrollViewScrollState().i(i3, i4);
        int scrollX = viewGroup.getScrollX();
        int scrollY = viewGroup.getScrollY();
        if (scrollX != i3) {
            aVar.a(scrollX, i3);
        }
        if (scrollY != i4) {
            aVar.a(scrollY, i4);
        }
    }

    public static final void s(ViewGroup viewGroup) {
        f7858a.t(viewGroup, viewGroup.getScrollX(), viewGroup.getScrollY());
    }

    public static final void u(ViewGroup viewGroup, float f3, float f4) {
        f7858a.t(viewGroup, viewGroup.getScrollX(), viewGroup.getScrollY());
        f(viewGroup, f3, f4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void q(ViewGroup viewGroup) {
        ((a) viewGroup).getFlingAnimator().addListener(new i(viewGroup));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void t(ViewGroup viewGroup, int i3, int i4) {
        if (f7860c) {
            Y.a.u(f7859b, "updateFabricScrollState[%d] scrollX %d scrollY %d", Integer.valueOf(viewGroup.getId()), Integer.valueOf(i3), Integer.valueOf(i4));
        }
        if (M1.a.a(viewGroup.getId()) == 1) {
            return;
        }
        g reactScrollViewScrollState = ((c) viewGroup).getReactScrollViewScrollState();
        if (reactScrollViewScrollState.c().equals(i3, i4)) {
            return;
        }
        reactScrollViewScrollState.k(i3, i4);
        k(viewGroup);
    }
}
