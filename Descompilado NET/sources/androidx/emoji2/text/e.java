package androidx.emoji2.text;

import android.text.TextPaint;
import androidx.emoji2.text.f;

/* JADX INFO: loaded from: classes.dex */
class e implements f.e {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final ThreadLocal f4753b = new ThreadLocal();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final TextPaint f4754a;

    e() {
        TextPaint textPaint = new TextPaint();
        this.f4754a = textPaint;
        textPaint.setTextSize(10.0f);
    }

    private static StringBuilder b() {
        ThreadLocal threadLocal = f4753b;
        if (threadLocal.get() == null) {
            threadLocal.set(new StringBuilder());
        }
        return (StringBuilder) threadLocal.get();
    }

    @Override // androidx.emoji2.text.f.e
    public boolean a(CharSequence charSequence, int i3, int i4, int i5) {
        StringBuilder sbB = b();
        sbB.setLength(0);
        while (i3 < i4) {
            sbB.append(charSequence.charAt(i3));
            i3++;
        }
        return androidx.core.graphics.c.a(this.f4754a, sbB.toString());
    }
}
