package androidx.core.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;

/* JADX INFO: renamed from: androidx.core.view.i0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0254i0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final WeakReference f4621a;

    /* JADX INFO: renamed from: androidx.core.view.i0$a */
    class a extends AnimatorListenerAdapter {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ InterfaceC0256j0 f4622a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f4623b;

        a(InterfaceC0256j0 interfaceC0256j0, View view) {
            this.f4622a = interfaceC0256j0;
            this.f4623b = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f4622a.a(this.f4623b);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f4622a.b(this.f4623b);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.f4622a.c(this.f4623b);
        }
    }

    C0254i0(View view) {
        this.f4621a = new WeakReference(view);
    }

    private void i(View view, InterfaceC0256j0 interfaceC0256j0) {
        if (interfaceC0256j0 != null) {
            view.animate().setListener(new a(interfaceC0256j0, view));
        } else {
            view.animate().setListener(null);
        }
    }

    public C0254i0 b(float f3) {
        View view = (View) this.f4621a.get();
        if (view != null) {
            view.animate().alpha(f3);
        }
        return this;
    }

    public void c() {
        View view = (View) this.f4621a.get();
        if (view != null) {
            view.animate().cancel();
        }
    }

    public long d() {
        View view = (View) this.f4621a.get();
        if (view != null) {
            return view.animate().getDuration();
        }
        return 0L;
    }

    public C0254i0 f(long j3) {
        View view = (View) this.f4621a.get();
        if (view != null) {
            view.animate().setDuration(j3);
        }
        return this;
    }

    public C0254i0 g(Interpolator interpolator) {
        View view = (View) this.f4621a.get();
        if (view != null) {
            view.animate().setInterpolator(interpolator);
        }
        return this;
    }

    public C0254i0 h(InterfaceC0256j0 interfaceC0256j0) {
        View view = (View) this.f4621a.get();
        if (view != null) {
            i(view, interfaceC0256j0);
        }
        return this;
    }

    public C0254i0 j(long j3) {
        View view = (View) this.f4621a.get();
        if (view != null) {
            view.animate().setStartDelay(j3);
        }
        return this;
    }

    public C0254i0 k(final InterfaceC0260l0 interfaceC0260l0) {
        final View view = (View) this.f4621a.get();
        if (view != null) {
            view.animate().setUpdateListener(interfaceC0260l0 != null ? new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.core.view.h0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    interfaceC0260l0.a(view);
                }
            } : null);
        }
        return this;
    }

    public void l() {
        View view = (View) this.f4621a.get();
        if (view != null) {
            view.animate().start();
        }
    }

    public C0254i0 m(float f3) {
        View view = (View) this.f4621a.get();
        if (view != null) {
            view.animate().translationY(f3);
        }
        return this;
    }
}
