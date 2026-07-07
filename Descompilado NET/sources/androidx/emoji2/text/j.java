package androidx.emoji2.text;

import android.graphics.Paint;
import android.text.style.ReplacementSpan;

/* JADX INFO: loaded from: classes.dex */
public abstract class j extends ReplacementSpan {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final q f4809b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Paint.FontMetricsInt f4808a = new Paint.FontMetricsInt();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private short f4810c = -1;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private short f4811d = -1;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private float f4812e = 1.0f;

    j(q qVar) {
        q.g.h(qVar, "rasterizer cannot be null");
        this.f4809b = qVar;
    }

    public final q a() {
        return this.f4809b;
    }

    final int b() {
        return this.f4810c;
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int i3, int i4, Paint.FontMetricsInt fontMetricsInt) {
        paint.getFontMetricsInt(this.f4808a);
        Paint.FontMetricsInt fontMetricsInt2 = this.f4808a;
        this.f4812e = (Math.abs(fontMetricsInt2.descent - fontMetricsInt2.ascent) * 1.0f) / this.f4809b.e();
        this.f4811d = (short) (this.f4809b.e() * this.f4812e);
        short sI = (short) (this.f4809b.i() * this.f4812e);
        this.f4810c = sI;
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fontMetricsInt3 = this.f4808a;
            fontMetricsInt.ascent = fontMetricsInt3.ascent;
            fontMetricsInt.descent = fontMetricsInt3.descent;
            fontMetricsInt.top = fontMetricsInt3.top;
            fontMetricsInt.bottom = fontMetricsInt3.bottom;
        }
        return sI;
    }
}
