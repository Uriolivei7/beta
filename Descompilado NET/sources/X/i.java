package X;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public abstract class i {

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final String f2735a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final C0044a f2736b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private C0044a f2737c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f2738d;

        /* JADX INFO: renamed from: X.i$a$a, reason: collision with other inner class name */
        private static final class C0044a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            String f2739a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            Object f2740b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            C0044a f2741c;

            private C0044a() {
            }
        }

        private C0044a d() {
            C0044a c0044a = new C0044a();
            this.f2737c.f2741c = c0044a;
            this.f2737c = c0044a;
            return c0044a;
        }

        private a e(String str, Object obj) {
            C0044a c0044aD = d();
            c0044aD.f2740b = obj;
            c0044aD.f2739a = (String) k.g(str);
            return this;
        }

        public a a(String str, int i3) {
            return e(str, String.valueOf(i3));
        }

        public a b(String str, Object obj) {
            return e(str, obj);
        }

        public a c(String str, boolean z3) {
            return e(str, String.valueOf(z3));
        }

        public String toString() {
            boolean z3 = this.f2738d;
            StringBuilder sb = new StringBuilder(32);
            sb.append(this.f2735a);
            sb.append('{');
            String str = "";
            for (C0044a c0044a = this.f2736b.f2741c; c0044a != null; c0044a = c0044a.f2741c) {
                Object obj = c0044a.f2740b;
                if (!z3 || obj != null) {
                    sb.append(str);
                    String str2 = c0044a.f2739a;
                    if (str2 != null) {
                        sb.append(str2);
                        sb.append('=');
                    }
                    if (obj == null || !obj.getClass().isArray()) {
                        sb.append(obj);
                    } else {
                        String strDeepToString = Arrays.deepToString(new Object[]{obj});
                        sb.append((CharSequence) strDeepToString, 1, strDeepToString.length() - 1);
                    }
                    str = ", ";
                }
            }
            sb.append('}');
            return sb.toString();
        }

        private a(String str) {
            C0044a c0044a = new C0044a();
            this.f2736b = c0044a;
            this.f2737c = c0044a;
            this.f2738d = false;
            this.f2735a = (String) k.g(str);
        }
    }

    public static boolean a(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static a b(Object obj) {
        return new a(obj.getClass().getSimpleName());
    }
}
