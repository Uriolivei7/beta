package Z1;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.style.ReplacementSpan;
import android.widget.TextView;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public abstract class p extends ReplacementSpan implements i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f2839a = new a(null);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void a(Spannable spannable, TextView textView) {
            D2.h.f(spannable, "spannable");
            Object[] spans = spannable.getSpans(0, spannable.length(), p.class);
            D2.h.e(spans, "getSpans(...)");
            for (Object obj : spans) {
                p pVar = (p) obj;
                pVar.c();
                pVar.h(textView);
            }
        }

        private a() {
        }
    }

    public static final void g(Spannable spannable, TextView textView) {
        f2839a.a(spannable, textView);
    }

    public abstract Drawable a();

    public abstract int b();

    public abstract void c();

    public abstract void d();

    public abstract void e();

    public abstract void f();

    public abstract void h(TextView textView);
}
