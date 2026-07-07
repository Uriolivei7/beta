package Z1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

/* JADX INFO: loaded from: classes.dex */
public final class q extends ReplacementSpan implements i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2840a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f2841b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f2842c;

    public q(int i3, int i4, int i5) {
        this.f2840a = i3;
        this.f2841b = i4;
        this.f2842c = i5;
    }

    public final int a() {
        return this.f2842c;
    }

    public final int b() {
        return this.f2840a;
    }

    public final int c() {
        return this.f2841b;
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int i3, int i4, float f3, int i5, int i6, int i7, Paint paint) {
        D2.h.f(canvas, "canvas");
        D2.h.f(paint, "paint");
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int i3, int i4, Paint.FontMetricsInt fontMetricsInt) {
        D2.h.f(paint, "paint");
        if (fontMetricsInt != null) {
            int i5 = -this.f2842c;
            fontMetricsInt.ascent = i5;
            fontMetricsInt.descent = 0;
            fontMetricsInt.top = i5;
            fontMetricsInt.bottom = 0;
        }
        return this.f2841b;
    }
}
