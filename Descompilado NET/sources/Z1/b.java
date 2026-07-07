package Z1;

import android.graphics.Paint;
import android.text.style.LineHeightSpan;

/* JADX INFO: loaded from: classes.dex */
public final class b implements LineHeightSpan, i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2818a;

    public b(float f3) {
        this.f2818a = (int) Math.ceil(f3);
    }

    @Override // android.text.style.LineHeightSpan
    public void chooseHeight(CharSequence charSequence, int i3, int i4, int i5, int i6, Paint.FontMetricsInt fontMetricsInt) {
        D2.h.f(charSequence, "text");
        D2.h.f(fontMetricsInt, "fm");
        int i7 = this.f2818a;
        double d4 = (i7 - ((-r9) + fontMetricsInt.descent)) / 2.0f;
        fontMetricsInt.ascent = fontMetricsInt.ascent - ((int) Math.ceil(d4));
        fontMetricsInt.descent += (int) Math.floor(d4);
        if (i3 == 0) {
            fontMetricsInt.top = fontMetricsInt.ascent;
        }
        if (i4 == charSequence.length()) {
            fontMetricsInt.bottom = fontMetricsInt.descent;
        }
    }
}
