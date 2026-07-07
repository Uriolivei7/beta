package Z1;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

/* JADX INFO: loaded from: classes.dex */
public final class h extends CharacterStyle implements UpdateAppearance, i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final float f2828a;

    public h(float f3) {
        this.f2828a = f3;
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        D2.h.f(textPaint, "paint");
        textPaint.setAlpha(F2.a.c(Color.alpha(textPaint.getColor()) * this.f2828a));
        if (textPaint.bgColor != 0) {
            textPaint.bgColor = Color.argb(F2.a.c(Color.alpha(r0) * this.f2828a), Color.red(textPaint.bgColor), Color.green(textPaint.bgColor), Color.blue(textPaint.bgColor));
        }
    }
}
