package Q1;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/* JADX INFO: loaded from: classes.dex */
class l extends Animation {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final View f1841b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final float f1842c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final float f1843d;

    public l(View view, float f3, float f4) {
        this.f1841b = view;
        this.f1842c = f3;
        this.f1843d = f4 - f3;
        setAnimationListener(new a(view));
    }

    @Override // android.view.animation.Animation
    protected void applyTransformation(float f3, Transformation transformation) {
        this.f1841b.setAlpha(this.f1842c + (this.f1843d * f3));
    }

    @Override // android.view.animation.Animation
    public boolean willChangeBounds() {
        return false;
    }

    static class a implements Animation.AnimationListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final View f1844a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private boolean f1845b = false;

        public a(View view) {
            this.f1844a = view;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            if (this.f1845b) {
                this.f1844a.setLayerType(0, null);
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            if (this.f1844a.hasOverlappingRendering() && this.f1844a.getLayerType() == 0) {
                this.f1845b = true;
                this.f1844a.setLayerType(2, null);
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }
    }
}
