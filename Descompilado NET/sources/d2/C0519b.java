package d2;

import D2.h;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: renamed from: d2.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0519b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0519b f9339a = new C0519b();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static boolean f9340b;

    /* JADX INFO: renamed from: d2.b$a */
    public static abstract class a {
        public abstract a a(String str, int i3);

        public abstract a b(String str, Object obj);

        public abstract void c();
    }

    /* JADX INFO: renamed from: d2.b$b, reason: collision with other inner class name */
    private static final class C0125b extends a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final long f9341a;

        public C0125b(long j3) {
            this.f9341a = j3;
        }

        @Override // d2.C0519b.a
        public a a(String str, int i3) {
            h.f(str, "key");
            return this;
        }

        @Override // d2.C0519b.a
        public a b(String str, Object obj) {
            h.f(str, "key");
            h.f(obj, "value");
            return this;
        }

        @Override // d2.C0519b.a
        public void c() {
            C0518a.i(this.f9341a);
        }
    }

    /* JADX INFO: renamed from: d2.b$c */
    private static final class c extends a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final long f9342a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final String f9343b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final List f9344c;

        public c(long j3, String str) {
            h.f(str, "sectionName");
            this.f9342a = j3;
            this.f9343b = str;
            this.f9344c = new ArrayList();
        }

        private final void d(String str, String str2) {
            this.f9344c.add(str + ": " + str2);
        }

        @Override // d2.C0519b.a
        public a a(String str, int i3) {
            h.f(str, "key");
            d(str, String.valueOf(i3));
            return this;
        }

        @Override // d2.C0519b.a
        public a b(String str, Object obj) {
            h.f(str, "key");
            h.f(obj, "value");
            d(str, obj.toString());
            return this;
        }

        @Override // d2.C0519b.a
        public void c() {
            String str;
            long j3 = this.f9342a;
            String str2 = this.f9343b;
            if (!C0519b.f9340b || this.f9344c.isEmpty()) {
                str = "";
            } else {
                str = " (" + AbstractC0520c.a(", ", this.f9344c) + ")";
            }
            C0518a.c(j3, str2 + str);
        }
    }

    private C0519b() {
    }

    public static final a a(long j3, String str) {
        h.f(str, "sectionName");
        return new c(j3, str);
    }

    public static final a b(long j3) {
        return new C0125b(j3);
    }
}
