package androidx.swiperefreshlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.animation.Animation;
import android.widget.ImageView;
import androidx.core.view.Z;

/* JADX INFO: loaded from: classes.dex */
class a extends ImageView {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Animation.AnimationListener f5461b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f5462c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f5463d;

    /* JADX INFO: renamed from: androidx.swiperefreshlayout.widget.a$a, reason: collision with other inner class name */
    private static class C0084a extends OvalShape {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private Paint f5464b = new Paint();

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f5465c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private a f5466d;

        C0084a(a aVar, int i3) {
            this.f5466d = aVar;
            this.f5465c = i3;
            b((int) rect().width());
        }

        private void b(int i3) {
            float f3 = i3 / 2;
            this.f5464b.setShader(new RadialGradient(f3, f3, this.f5465c, new int[]{1023410176, 0}, (float[]) null, Shader.TileMode.CLAMP));
        }

        @Override // android.graphics.drawable.shapes.OvalShape, android.graphics.drawable.shapes.RectShape, android.graphics.drawable.shapes.Shape
        public void draw(Canvas canvas, Paint paint) {
            float width = this.f5466d.getWidth() / 2;
            float height = this.f5466d.getHeight() / 2;
            canvas.drawCircle(width, height, width, this.f5464b);
            canvas.drawCircle(width, height, r0 - this.f5465c, paint);
        }

        @Override // android.graphics.drawable.shapes.RectShape, android.graphics.drawable.shapes.Shape
        protected void onResize(float f3, float f4) {
            super.onResize(f3, f4);
            b((int) f3);
        }
    }

    a(Context context) {
        ShapeDrawable shapeDrawable;
        super(context);
        float f3 = getContext().getResources().getDisplayMetrics().density;
        int i3 = (int) (1.75f * f3);
        int i4 = (int) (0.0f * f3);
        this.f5462c = (int) (3.5f * f3);
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(I.a.f383f);
        this.f5463d = typedArrayObtainStyledAttributes.getColor(I.a.f384g, -328966);
        typedArrayObtainStyledAttributes.recycle();
        if (a()) {
            shapeDrawable = new ShapeDrawable(new OvalShape());
            Z.e0(this, f3 * 4.0f);
        } else {
            ShapeDrawable shapeDrawable2 = new ShapeDrawable(new C0084a(this, this.f5462c));
            setLayerType(1, shapeDrawable2.getPaint());
            shapeDrawable2.getPaint().setShadowLayer(this.f5462c, i4, i3, 503316480);
            int i5 = this.f5462c;
            setPadding(i5, i5, i5, i5);
            shapeDrawable = shapeDrawable2;
        }
        shapeDrawable.getPaint().setColor(this.f5463d);
        Z.b0(this, shapeDrawable);
    }

    private boolean a() {
        return true;
    }

    public void b(Animation.AnimationListener animationListener) {
        this.f5461b = animationListener;
    }

    @Override // android.view.View
    public void onAnimationEnd() {
        super.onAnimationEnd();
        Animation.AnimationListener animationListener = this.f5461b;
        if (animationListener != null) {
            animationListener.onAnimationEnd(getAnimation());
        }
    }

    @Override // android.view.View
    public void onAnimationStart() {
        super.onAnimationStart();
        Animation.AnimationListener animationListener = this.f5461b;
        if (animationListener != null) {
            animationListener.onAnimationStart(getAnimation());
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        if (a()) {
            return;
        }
        setMeasuredDimension(getMeasuredWidth() + (this.f5462c * 2), getMeasuredHeight() + (this.f5462c * 2));
    }

    @Override // android.view.View
    public void setBackgroundColor(int i3) {
        if (getBackground() instanceof ShapeDrawable) {
            ((ShapeDrawable) getBackground()).getPaint().setColor(i3);
            this.f5463d = i3;
        }
    }
}
