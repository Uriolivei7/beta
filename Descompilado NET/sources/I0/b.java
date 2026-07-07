package I0;

import D2.u;
import X.k;
import java.util.Arrays;
import java.util.regex.Pattern;
import kotlin.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.AbstractC0681d;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f385c = new a(null);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final Lazy f386d = AbstractC0681d.a(new C2.a() { // from class: I0.a
        @Override // C2.a
        public final Object a() {
            return b.e();
        }
    });

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f387a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f388b;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final Pattern d() {
            Object value = b.f386d.getValue();
            D2.h.e(value, "getValue(...)");
            return (Pattern) value;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String f(int i3) {
            return i3 == Integer.MAX_VALUE ? "" : String.valueOf(i3);
        }

        public final b b(int i3) {
            k.b(Boolean.valueOf(i3 >= 0));
            return new b(i3, Integer.MAX_VALUE);
        }

        public final b c(String str) {
            if (str == null) {
                return null;
            }
            try {
                String[] strArrSplit = d().split(str);
                k.b(Boolean.valueOf(strArrSplit.length == 4));
                k.b(Boolean.valueOf(D2.h.b(strArrSplit[0], "bytes")));
                String str2 = strArrSplit[1];
                D2.h.e(str2, "get(...)");
                int i3 = Integer.parseInt(str2);
                String str3 = strArrSplit[2];
                D2.h.e(str3, "get(...)");
                int i4 = Integer.parseInt(str3);
                String str4 = strArrSplit[3];
                D2.h.e(str4, "get(...)");
                int i5 = Integer.parseInt(str4);
                k.b(Boolean.valueOf(i4 > i3));
                k.b(Boolean.valueOf(i5 > i4));
                return i4 < i5 - 1 ? new b(i3, i4) : new b(i3, Integer.MAX_VALUE);
            } catch (IllegalArgumentException e4) {
                u uVar = u.f192a;
                String str5 = String.format(null, "Invalid Content-Range header value: \"%s\"", Arrays.copyOf(new Object[]{str}, 1));
                D2.h.e(str5, "format(...)");
                throw new IllegalArgumentException(str5, e4);
            }
        }

        public final b e(int i3) {
            k.b(Boolean.valueOf(i3 > 0));
            return new b(0, i3);
        }

        private a() {
        }
    }

    public b(int i3, int i4) {
        this.f387a = i3;
        this.f388b = i4;
    }

    public static final b d(int i3) {
        return f385c.b(i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Pattern e() {
        return Pattern.compile("[-/ ]");
    }

    public static final b g(int i3) {
        return f385c.e(i3);
    }

    public final boolean c(b bVar) {
        return bVar != null && this.f387a <= bVar.f387a && bVar.f388b <= this.f388b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!D2.h.b(b.class, obj != null ? obj.getClass() : null)) {
            return false;
        }
        D2.h.d(obj, "null cannot be cast to non-null type com.facebook.imagepipeline.common.BytesRange");
        b bVar = (b) obj;
        return this.f387a == bVar.f387a && this.f388b == bVar.f388b;
    }

    public final String f() {
        u uVar = u.f192a;
        a aVar = f385c;
        String str = String.format(null, "bytes=%s-%s", Arrays.copyOf(new Object[]{aVar.f(this.f387a), aVar.f(this.f388b)}, 2));
        D2.h.e(str, "format(...)");
        return str;
    }

    public int hashCode() {
        return (this.f387a * 31) + this.f388b;
    }

    public String toString() {
        u uVar = u.f192a;
        a aVar = f385c;
        String str = String.format(null, "%s-%s", Arrays.copyOf(new Object[]{aVar.f(this.f387a), aVar.f(this.f388b)}, 2));
        D2.h.e(str, "format(...)");
        return str;
    }
}
