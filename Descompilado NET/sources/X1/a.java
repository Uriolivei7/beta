package X1;

import D2.h;
import android.content.Context;
import android.net.Uri;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final C0045a f2747f = new C0045a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f2748a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final E1.a f2749b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Uri f2750c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final double f2751d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f2752e;

    /* JADX INFO: renamed from: X1.a$a, reason: collision with other inner class name */
    public static final class C0045a {
        public /* synthetic */ C0045a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final a a(Context context) {
            h.f(context, "context");
            return new a(context, "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=", 0.0d, 0.0d, E1.a.f196b, 12, null);
        }

        private C0045a() {
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public a(Context context, String str) {
        this(context, str, 0.0d, 0.0d, null, 28, null);
        h.f(context, "context");
    }

    private final Uri a(Context context) {
        this.f2752e = true;
        return c.f2756b.a().g(context, this.f2748a);
    }

    private final Uri b(Context context) {
        try {
            Uri uri = Uri.parse(this.f2748a);
            return uri.getScheme() == null ? a(context) : uri;
        } catch (NullPointerException unused) {
            return a(context);
        }
    }

    public final E1.a c() {
        return this.f2749b;
    }

    public final double d() {
        return this.f2751d;
    }

    public final String e() {
        return this.f2748a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !h.b(getClass(), obj.getClass())) {
            return false;
        }
        a aVar = (a) obj;
        return Double.compare(aVar.f2751d, this.f2751d) == 0 && g() == aVar.g() && h.b(f(), aVar.f()) && h.b(this.f2748a, aVar.f2748a) && this.f2749b == aVar.f2749b;
    }

    public Uri f() {
        return this.f2750c;
    }

    public boolean g() {
        return this.f2752e;
    }

    public int hashCode() {
        return Objects.hash(f(), this.f2748a, Double.valueOf(this.f2751d), Boolean.valueOf(g()), this.f2749b);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public a(Context context, String str, double d4) {
        this(context, str, d4, 0.0d, null, 24, null);
        h.f(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public a(Context context, String str, double d4, double d5) {
        this(context, str, d4, d5, null, 16, null);
        h.f(context, "context");
    }

    public a(Context context, String str, double d4, double d5, E1.a aVar) {
        h.f(context, "context");
        h.f(aVar, "cacheControl");
        this.f2748a = str;
        this.f2749b = aVar;
        this.f2750c = b(context);
        this.f2751d = d4 * d5;
    }

    public /* synthetic */ a(Context context, String str, double d4, double d5, E1.a aVar, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, str, (i3 & 4) != 0 ? 0.0d : d4, (i3 & 8) != 0 ? 0.0d : d5, (i3 & 16) != 0 ? E1.a.f196b : aVar);
    }
}
