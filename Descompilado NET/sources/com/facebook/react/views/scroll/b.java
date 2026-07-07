package com.facebook.react.views.scroll;

import a1.C0210a;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UIManagerListener;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.uimanager.H0;
import com.facebook.react.views.scroll.j;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
class b implements UIManagerListener {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ViewGroup f7752b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final boolean f7753c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private C0115b f7754d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private WeakReference f7755e = null;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Rect f7756f = null;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f7757g = false;

    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            b.this.b();
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.views.scroll.b$b, reason: collision with other inner class name */
    public static class C0115b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final int f7759a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final Integer f7760b;

        C0115b(int i3, Integer num) {
            this.f7759a = i3;
            this.f7760b = num;
        }

        static C0115b a(ReadableMap readableMap) {
            return new C0115b(readableMap.getInt("minIndexForVisible"), readableMap.hasKey("autoscrollToTopThreshold") ? Integer.valueOf(readableMap.getInt("autoscrollToTopThreshold")) : null);
        }
    }

    public b(ViewGroup viewGroup, boolean z3) {
        this.f7752b = viewGroup;
        this.f7753c = z3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        com.facebook.react.views.view.g gVarC;
        float y3;
        int height;
        if (this.f7754d == null || (gVarC = c()) == null) {
            return;
        }
        int scrollX = this.f7753c ? this.f7752b.getScrollX() : this.f7752b.getScrollY();
        for (int i3 = this.f7754d.f7759a; i3 < gVarC.getChildCount(); i3++) {
            View childAt = gVarC.getChildAt(i3);
            if (this.f7753c) {
                y3 = childAt.getX();
                height = childAt.getWidth();
            } else {
                y3 = childAt.getY();
                height = childAt.getHeight();
            }
            if (y3 + height > scrollX || i3 == gVarC.getChildCount() - 1) {
                this.f7755e = new WeakReference(childAt);
                Rect rect = new Rect();
                childAt.getHitRect(rect);
                this.f7756f = rect;
                return;
            }
        }
    }

    private com.facebook.react.views.view.g c() {
        return (com.facebook.react.views.view.g) this.f7752b.getChildAt(0);
    }

    private UIManager d() {
        return (UIManager) C0210a.c(H0.g((ReactContext) this.f7752b.getContext(), M1.a.a(this.f7752b.getId())));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void i() {
        WeakReference weakReference;
        View view;
        if (this.f7754d == null || (weakReference = this.f7755e) == null || this.f7756f == null || (view = (View) weakReference.get()) == null) {
            return;
        }
        Rect rect = new Rect();
        view.getHitRect(rect);
        if (this.f7753c) {
            int i3 = rect.left - this.f7756f.left;
            if (i3 != 0) {
                int scrollX = this.f7752b.getScrollX();
                ViewGroup viewGroup = this.f7752b;
                ((j.d) viewGroup).b(i3 + scrollX, viewGroup.getScrollY());
                this.f7756f = rect;
                Integer num = this.f7754d.f7760b;
                if (num == null || scrollX > num.intValue()) {
                    return;
                }
                ViewGroup viewGroup2 = this.f7752b;
                ((j.d) viewGroup2).f(0, viewGroup2.getScrollY());
                return;
            }
            return;
        }
        int i4 = rect.top - this.f7756f.top;
        if (i4 != 0) {
            int scrollY = this.f7752b.getScrollY();
            ViewGroup viewGroup3 = this.f7752b;
            ((j.d) viewGroup3).b(viewGroup3.getScrollX(), i4 + scrollY);
            this.f7756f = rect;
            Integer num2 = this.f7754d.f7760b;
            if (num2 == null || scrollY > num2.intValue()) {
                return;
            }
            ViewGroup viewGroup4 = this.f7752b;
            ((j.d) viewGroup4).f(viewGroup4.getScrollX(), 0);
        }
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void didDispatchMountItems(UIManager uIManager) {
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void didMountItems(UIManager uIManager) {
        i();
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void didScheduleMountItems(UIManager uIManager) {
    }

    public void e(C0115b c0115b) {
        this.f7754d = c0115b;
    }

    public void f() {
        if (this.f7757g) {
            return;
        }
        this.f7757g = true;
        d().addUIManagerEventListener(this);
    }

    public void g() {
        if (this.f7757g) {
            this.f7757g = false;
            d().removeUIManagerEventListener(this);
        }
    }

    public void h() {
        if (M1.a.a(this.f7752b.getId()) == 2) {
            return;
        }
        i();
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void willDispatchViewUpdates(UIManager uIManager) {
        UiThreadUtil.runOnUiThread(new a());
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void willMountItems(UIManager uIManager) {
        b();
    }
}
