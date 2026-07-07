package androidx.core.text;

import android.os.Build;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class l implements Spannable {

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final TextPaint f4529a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final TextDirectionHeuristic f4530b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f4531c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final int f4532d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final PrecomputedText.Params f4533e;

        /* JADX INFO: renamed from: androidx.core.text.l$a$a, reason: collision with other inner class name */
        public static class C0065a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            private final TextPaint f4534a;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            private int f4536c = 1;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            private int f4537d = 1;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            private TextDirectionHeuristic f4535b = TextDirectionHeuristics.FIRSTSTRONG_LTR;

            public C0065a(TextPaint textPaint) {
                this.f4534a = textPaint;
            }

            public a a() {
                return new a(this.f4534a, this.f4535b, this.f4536c, this.f4537d);
            }

            public C0065a b(int i3) {
                this.f4536c = i3;
                return this;
            }

            public C0065a c(int i3) {
                this.f4537d = i3;
                return this;
            }

            public C0065a d(TextDirectionHeuristic textDirectionHeuristic) {
                this.f4535b = textDirectionHeuristic;
                return this;
            }
        }

        a(TextPaint textPaint, TextDirectionHeuristic textDirectionHeuristic, int i3, int i4) {
            if (Build.VERSION.SDK_INT >= 29) {
                this.f4533e = k.a(textPaint).setBreakStrategy(i3).setHyphenationFrequency(i4).setTextDirection(textDirectionHeuristic).build();
            } else {
                this.f4533e = null;
            }
            this.f4529a = textPaint;
            this.f4530b = textDirectionHeuristic;
            this.f4531c = i3;
            this.f4532d = i4;
        }

        public boolean a(a aVar) {
            if (this.f4531c == aVar.b() && this.f4532d == aVar.c() && this.f4529a.getTextSize() == aVar.e().getTextSize() && this.f4529a.getTextScaleX() == aVar.e().getTextScaleX() && this.f4529a.getTextSkewX() == aVar.e().getTextSkewX() && this.f4529a.getLetterSpacing() == aVar.e().getLetterSpacing() && TextUtils.equals(this.f4529a.getFontFeatureSettings(), aVar.e().getFontFeatureSettings()) && this.f4529a.getFlags() == aVar.e().getFlags() && this.f4529a.getTextLocales().equals(aVar.e().getTextLocales())) {
                return this.f4529a.getTypeface() == null ? aVar.e().getTypeface() == null : this.f4529a.getTypeface().equals(aVar.e().getTypeface());
            }
            return false;
        }

        public int b() {
            return this.f4531c;
        }

        public int c() {
            return this.f4532d;
        }

        public TextDirectionHeuristic d() {
            return this.f4530b;
        }

        public TextPaint e() {
            return this.f4529a;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return a(aVar) && this.f4530b == aVar.d();
        }

        public int hashCode() {
            return q.c.b(Float.valueOf(this.f4529a.getTextSize()), Float.valueOf(this.f4529a.getTextScaleX()), Float.valueOf(this.f4529a.getTextSkewX()), Float.valueOf(this.f4529a.getLetterSpacing()), Integer.valueOf(this.f4529a.getFlags()), this.f4529a.getTextLocales(), this.f4529a.getTypeface(), Boolean.valueOf(this.f4529a.isElegantTextHeight()), this.f4530b, Integer.valueOf(this.f4531c), Integer.valueOf(this.f4532d));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("textSize=" + this.f4529a.getTextSize());
            sb.append(", textScaleX=" + this.f4529a.getTextScaleX());
            sb.append(", textSkewX=" + this.f4529a.getTextSkewX());
            int i3 = Build.VERSION.SDK_INT;
            sb.append(", letterSpacing=" + this.f4529a.getLetterSpacing());
            sb.append(", elegantTextHeight=" + this.f4529a.isElegantTextHeight());
            sb.append(", textLocale=" + this.f4529a.getTextLocales());
            sb.append(", typeface=" + this.f4529a.getTypeface());
            if (i3 >= 26) {
                sb.append(", variationSettings=" + this.f4529a.getFontVariationSettings());
            }
            sb.append(", textDir=" + this.f4530b);
            sb.append(", breakStrategy=" + this.f4531c);
            sb.append(", hyphenationFrequency=" + this.f4532d);
            sb.append("}");
            return sb.toString();
        }

        public a(PrecomputedText.Params params) {
            this.f4529a = params.getTextPaint();
            this.f4530b = params.getTextDirection();
            this.f4531c = params.getBreakStrategy();
            this.f4532d = params.getHyphenationFrequency();
            this.f4533e = Build.VERSION.SDK_INT < 29 ? null : params;
        }
    }
}
