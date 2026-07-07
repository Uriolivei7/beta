package Q1;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/* JADX INFO: loaded from: classes.dex */
class m extends Animation implements j {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final View f1846b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private float f1847c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private float f1848d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private float f1849e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private float f1850f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f1851g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f1852h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f1853i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f1854j;

    public m(View view, int i3, int i4, int i5, int i6) {
        this.f1846b = view;
        c(i3, i4, i5, i6);
    }

    private void c(int i3, int i4, int i5, int i6) {
        this.f1847c = this.f1846b.getX() - this.f1846b.getTranslationX();
        this.f1848d = this.f1846b.getY() - this.f1846b.getTranslationY();
        this.f1851g = this.f1846b.getWidth();
        int height = this.f1846b.getHeight();
        this.f1852h = height;
        this.f1849e = i3 - this.f1847c;
        this.f1850f = i4 - this.f1848d;
        this.f1853i = i5 - this.f1851g;
        this.f1854j = i6 - height;
    }

    @Override // android.view.animation.Animation
    protected void applyTransformation(float f3, Transformation transformation) {
        float f4 = this.f1847c + (this.f1849e * f3);
        float f5 = this.f1848d + (this.f1850f * f3);
        this.f1846b.layout(Math.round(f4), Math.round(f5), Math.round(f4 + this.f1851g + (this.f1853i * f3)), Math.round(f5 + this.f1852h + (this.f1854j * f3)));
    }

    @Override // Q1.j
    public void b(int i3, int i4, int i5, int i6) {
        c(i3, i4, i5, i6);
    }

    @Override // android.view.animation.Animation
    public boolean willChangeBounds() {
        return true;
    }
}
