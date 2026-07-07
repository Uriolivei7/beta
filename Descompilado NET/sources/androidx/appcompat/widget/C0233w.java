package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;
import d.AbstractC0487a;

/* JADX INFO: renamed from: androidx.appcompat.widget.w, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0233w extends RatingBar {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0231u f4337b;

    public C0233w(Context context) {
        this(context, null);
    }

    @Override // android.widget.RatingBar, android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    protected synchronized void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        Bitmap bitmapB = this.f4337b.b();
        if (bitmapB != null) {
            setMeasuredDimension(View.resolveSizeAndState(bitmapB.getWidth() * getNumStars(), i3, 0), getMeasuredHeight());
        }
    }

    public C0233w(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8665J);
    }

    public C0233w(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        d0.a(this, getContext());
        C0231u c0231u = new C0231u(this);
        this.f4337b = c0231u;
        c0231u.c(attributeSet, i3);
    }
}
