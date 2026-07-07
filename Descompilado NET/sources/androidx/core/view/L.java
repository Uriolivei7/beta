package androidx.core.view;

import android.view.View;
import android.view.ViewTreeObserver;

/* JADX INFO: loaded from: classes.dex */
public final class L implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final View f4550b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private ViewTreeObserver f4551c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Runnable f4552d;

    private L(View view, Runnable runnable) {
        this.f4550b = view;
        this.f4551c = view.getViewTreeObserver();
        this.f4552d = runnable;
    }

    public static L a(View view, Runnable runnable) {
        if (view == null) {
            throw new NullPointerException("view == null");
        }
        if (runnable == null) {
            throw new NullPointerException("runnable == null");
        }
        L l3 = new L(view, runnable);
        view.getViewTreeObserver().addOnPreDrawListener(l3);
        view.addOnAttachStateChangeListener(l3);
        return l3;
    }

    public void b() {
        if (this.f4551c.isAlive()) {
            this.f4551c.removeOnPreDrawListener(this);
        } else {
            this.f4550b.getViewTreeObserver().removeOnPreDrawListener(this);
        }
        this.f4550b.removeOnAttachStateChangeListener(this);
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        b();
        this.f4552d.run();
        return true;
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
        this.f4551c = view.getViewTreeObserver();
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        b();
    }
}
