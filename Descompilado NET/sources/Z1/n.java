package Z1;

import android.text.SpannableStringBuilder;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class n {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f2831d = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2832a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f2833b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final i f2834c;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public n(int i3, int i4, i iVar) {
        D2.h.f(iVar, "what");
        this.f2832a = i3;
        this.f2833b = i4;
        this.f2834c = iVar;
    }

    public final void a(SpannableStringBuilder spannableStringBuilder, int i3) {
        D2.h.f(spannableStringBuilder, "builder");
        if (i3 < 0) {
            throw new IllegalStateException("Check failed.");
        }
        int i4 = this.f2832a == 0 ? 18 : 34;
        int i5 = 255 - i3;
        if (i5 < 0) {
            Y.a.I("SetSpanOperation", "Text tree size exceeded the limit, styling may become unpredictable");
        }
        spannableStringBuilder.setSpan(this.f2834c, this.f2832a, this.f2833b, ((Math.max(i5, 0) << 16) & 16711680) | (i4 & (-16711681)));
    }
}
