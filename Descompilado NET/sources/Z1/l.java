package Z1;

import android.text.TextPaint;

/* JADX INFO: loaded from: classes.dex */
public final class l implements i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final TextPaint f2830a;

    public l(TextPaint textPaint) {
        D2.h.f(textPaint, "textPaint");
        this.f2830a = textPaint;
    }

    public final TextPaint a() {
        return this.f2830a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof l) && D2.h.b(this.f2830a, ((l) obj).f2830a);
    }

    public int hashCode() {
        return this.f2830a.hashCode();
    }

    public String toString() {
        return "ReactTextPaintHolderSpan(textPaint=" + this.f2830a + ")";
    }
}
