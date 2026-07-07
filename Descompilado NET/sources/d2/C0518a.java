package d2;

import D2.h;
import com.facebook.systrace.TraceListener;
import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX INFO: renamed from: d2.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0518a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0518a f9332a = new C0518a();

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* JADX INFO: renamed from: d2.a$a, reason: collision with other inner class name */
    public static final class EnumC0124a {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final EnumC0124a f9333c = new EnumC0124a("THREAD", 0, 't');

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public static final EnumC0124a f9334d = new EnumC0124a("PROCESS", 1, 'p');

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public static final EnumC0124a f9335e = new EnumC0124a("GLOBAL", 2, 'g');

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private static final /* synthetic */ EnumC0124a[] f9336f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private static final /* synthetic */ EnumEntries f9337g;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final char f9338b;

        static {
            EnumC0124a[] enumC0124aArrA = a();
            f9336f = enumC0124aArrA;
            f9337g = AbstractC0764a.a(enumC0124aArrA);
        }

        private EnumC0124a(String str, int i3, char c4) {
            this.f9338b = c4;
        }

        private static final /* synthetic */ EnumC0124a[] a() {
            return new EnumC0124a[]{f9333c, f9334d, f9335e};
        }

        public static EnumC0124a valueOf(String str) {
            return (EnumC0124a) Enum.valueOf(EnumC0124a.class, str);
        }

        public static EnumC0124a[] values() {
            return (EnumC0124a[]) f9336f.clone();
        }
    }

    private C0518a() {
    }

    public static final void a(long j3, String str, int i3) {
        h.f(str, "sectionName");
        J.a.a(str, i3);
    }

    public static final void b(long j3, String str, int i3, long j4) {
        h.f(str, "sectionName");
        a(j3, str, i3);
    }

    public static final void c(long j3, String str) {
        h.f(str, "sectionName");
        J.a.c(str);
    }

    public static final void d(long j3, String str, String[] strArr, int i3) {
        h.f(str, "sectionName");
        h.f(strArr, "args");
        J.a.c(str + "|" + f9332a.e(strArr, i3));
    }

    private final String e(String[] strArr, int i3) {
        StringBuilder sb = new StringBuilder();
        for (int i4 = 1; i4 < i3; i4 += 2) {
            String str = strArr[i4 - 1];
            String str2 = strArr[i4];
            sb.append(str);
            sb.append('=');
            sb.append(str2);
            if (i4 < i3 - 1) {
                sb.append(';');
            }
        }
        String string = sb.toString();
        h.e(string, "toString(...)");
        return string;
    }

    public static final void f(long j3, String str, int i3) {
        h.f(str, "sectionName");
        g(j3, str, i3);
    }

    public static final void g(long j3, String str, int i3) {
        h.f(str, "sectionName");
        J.a.d(str, i3);
    }

    public static final void h(long j3, String str, int i3, long j4) {
        h.f(str, "sectionName");
        g(j3, str, i3);
    }

    public static final void i(long j3) {
        J.a.f();
    }

    public static final boolean j(long j3) {
        return false;
    }

    public static final void l(long j3, String str, int i3) {
        h.f(str, "sectionName");
        a(j3, str, i3);
    }

    public static final void m(long j3, String str, int i3) {
        h.f(str, "counterName");
        J.a.j(str, i3);
    }

    public static final void o(long j3, String str, Runnable runnable) {
        h.f(str, "sectionName");
        h.f(runnable, "block");
        c(j3, str);
        try {
            runnable.run();
        } finally {
            i(j3);
        }
    }

    public static final void k(TraceListener traceListener) {
    }

    public static final void p(TraceListener traceListener) {
    }

    public static final void n(long j3, String str, EnumC0124a enumC0124a) {
    }
}
