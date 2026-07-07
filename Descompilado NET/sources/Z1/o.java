package Z1;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

/* JADX INFO: loaded from: classes.dex */
public final class o extends CharacterStyle implements i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final float f2835a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final float f2836b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final float f2837c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f2838d;

    public o(float f3, float f4, float f5, int i3) {
        this.f2835a = f3;
        this.f2836b = f4;
        this.f2837c = f5;
        this.f2838d = i3;
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        D2.h.f(textPaint, "textPaint");
        textPaint.setShadowLayer(this.f2837c, this.f2835a, this.f2836b, this.f2838d);
    }
}
