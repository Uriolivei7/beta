package Z1;

import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class c extends MetricAffectingSpan implements i {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final a f2819f = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2820a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f2821b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f2822c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String f2823d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final AssetManager f2824e;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void b(Paint paint, int i3, int i4, String str, String str2, AssetManager assetManager) {
            Typeface typefaceA = com.facebook.react.views.text.p.a(paint.getTypeface(), i3, i4, str2, assetManager);
            paint.setFontFeatureSettings(str);
            paint.setTypeface(typefaceA);
            paint.setSubpixelText(true);
        }

        private a() {
        }
    }

    public c(int i3, int i4, String str, String str2, AssetManager assetManager) {
        D2.h.f(assetManager, "assetManager");
        this.f2820a = i3;
        this.f2821b = i4;
        this.f2822c = str;
        this.f2823d = str2;
        this.f2824e = assetManager;
    }

    public final String a() {
        return this.f2823d;
    }

    public final String b() {
        return this.f2822c;
    }

    public final int c() {
        int i3 = this.f2820a;
        if (i3 == -1) {
            return 0;
        }
        return i3;
    }

    public final int d() {
        int i3 = this.f2821b;
        if (i3 == -1) {
            return 400;
        }
        return i3;
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        D2.h.f(textPaint, "ds");
        f2819f.b(textPaint, this.f2820a, this.f2821b, this.f2822c, this.f2823d, this.f2824e);
    }

    @Override // android.text.style.MetricAffectingSpan
    public void updateMeasureState(TextPaint textPaint) {
        D2.h.f(textPaint, "paint");
        f2819f.b(textPaint, this.f2820a, this.f2821b, this.f2822c, this.f2823d, this.f2824e);
    }
}
