package androidx.appcompat.widget;

import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityManager;
import androidx.core.view.AbstractC0244d0;

/* JADX INFO: loaded from: classes.dex */
class p0 implements View.OnLongClickListener, View.OnHoverListener, View.OnAttachStateChangeListener {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static p0 f4292l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static p0 f4293m;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final View f4294b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final CharSequence f4295c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f4296d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Runnable f4297e = new Runnable() { // from class: androidx.appcompat.widget.n0
        @Override // java.lang.Runnable
        public final void run() {
            this.f4287b.e();
        }
    };

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Runnable f4298f = new Runnable() { // from class: androidx.appcompat.widget.o0
        @Override // java.lang.Runnable
        public final void run() {
            this.f4288b.d();
        }
    };

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f4299g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f4300h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private q0 f4301i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f4302j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f4303k;

    private p0(View view, CharSequence charSequence) {
        this.f4294b = view;
        this.f4295c = charSequence;
        this.f4296d = AbstractC0244d0.e(ViewConfiguration.get(view.getContext()));
        c();
        view.setOnLongClickListener(this);
        view.setOnHoverListener(this);
    }

    private void b() {
        this.f4294b.removeCallbacks(this.f4297e);
    }

    private void c() {
        this.f4303k = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e() {
        i(false);
    }

    private void f() {
        this.f4294b.postDelayed(this.f4297e, ViewConfiguration.getLongPressTimeout());
    }

    private static void g(p0 p0Var) {
        p0 p0Var2 = f4292l;
        if (p0Var2 != null) {
            p0Var2.b();
        }
        f4292l = p0Var;
        if (p0Var != null) {
            p0Var.f();
        }
    }

    public static void h(View view, CharSequence charSequence) {
        p0 p0Var = f4292l;
        if (p0Var != null && p0Var.f4294b == view) {
            g(null);
        }
        if (!TextUtils.isEmpty(charSequence)) {
            new p0(view, charSequence);
            return;
        }
        p0 p0Var2 = f4293m;
        if (p0Var2 != null && p0Var2.f4294b == view) {
            p0Var2.d();
        }
        view.setOnLongClickListener(null);
        view.setLongClickable(false);
        view.setOnHoverListener(null);
    }

    private boolean j(MotionEvent motionEvent) {
        int x3 = (int) motionEvent.getX();
        int y3 = (int) motionEvent.getY();
        if (!this.f4303k && Math.abs(x3 - this.f4299g) <= this.f4296d && Math.abs(y3 - this.f4300h) <= this.f4296d) {
            return false;
        }
        this.f4299g = x3;
        this.f4300h = y3;
        this.f4303k = false;
        return true;
    }

    void d() {
        if (f4293m == this) {
            f4293m = null;
            q0 q0Var = this.f4301i;
            if (q0Var != null) {
                q0Var.c();
                this.f4301i = null;
                c();
                this.f4294b.removeOnAttachStateChangeListener(this);
            } else {
                Log.e("TooltipCompatHandler", "sActiveHandler.mPopup == null");
            }
        }
        if (f4292l == this) {
            g(null);
        }
        this.f4294b.removeCallbacks(this.f4298f);
    }

    void i(boolean z3) {
        long longPressTimeout;
        long j3;
        long j4;
        if (this.f4294b.isAttachedToWindow()) {
            g(null);
            p0 p0Var = f4293m;
            if (p0Var != null) {
                p0Var.d();
            }
            f4293m = this;
            this.f4302j = z3;
            q0 q0Var = new q0(this.f4294b.getContext());
            this.f4301i = q0Var;
            q0Var.e(this.f4294b, this.f4299g, this.f4300h, this.f4302j, this.f4295c);
            this.f4294b.addOnAttachStateChangeListener(this);
            if (this.f4302j) {
                j4 = 2500;
            } else {
                if ((androidx.core.view.Z.B(this.f4294b) & 1) == 1) {
                    longPressTimeout = ViewConfiguration.getLongPressTimeout();
                    j3 = 3000;
                } else {
                    longPressTimeout = ViewConfiguration.getLongPressTimeout();
                    j3 = 15000;
                }
                j4 = j3 - longPressTimeout;
            }
            this.f4294b.removeCallbacks(this.f4298f);
            this.f4294b.postDelayed(this.f4298f, j4);
        }
    }

    @Override // android.view.View.OnHoverListener
    public boolean onHover(View view, MotionEvent motionEvent) {
        if (this.f4301i != null && this.f4302j) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.f4294b.getContext().getSystemService("accessibility");
        if (accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action != 7) {
            if (action == 10) {
                c();
                d();
            }
        } else if (this.f4294b.isEnabled() && this.f4301i == null && j(motionEvent)) {
            g(this);
        }
        return false;
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        this.f4299g = view.getWidth() / 2;
        this.f4300h = view.getHeight() / 2;
        i(true);
        return true;
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        d();
    }
}
