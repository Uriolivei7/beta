package androidx.core.os;

import android.os.Build;
import android.os.ext.SdkExtensions;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f4509a = new a();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final int f4510b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final int f4511c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final int f4512d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final int f4513e;

    /* JADX INFO: renamed from: androidx.core.os.a$a, reason: collision with other inner class name */
    private static final class C0063a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final C0063a f4514a = new C0063a();

        private C0063a() {
        }

        public final int a(int i3) {
            return SdkExtensions.getExtensionVersion(i3);
        }
    }

    static {
        int i3 = Build.VERSION.SDK_INT;
        f4510b = i3 >= 30 ? C0063a.f4514a.a(30) : 0;
        f4511c = i3 >= 30 ? C0063a.f4514a.a(31) : 0;
        f4512d = i3 >= 30 ? C0063a.f4514a.a(33) : 0;
        f4513e = i3 >= 30 ? C0063a.f4514a.a(1000000) : 0;
    }

    private a() {
    }

    public static final boolean a(String str, String str2) {
        D2.h.f(str, "codename");
        D2.h.f(str2, "buildCodename");
        if (D2.h.b("REL", str2)) {
            return false;
        }
        Locale locale = Locale.ROOT;
        String upperCase = str2.toUpperCase(locale);
        D2.h.e(upperCase, "this as java.lang.String).toUpperCase(Locale.ROOT)");
        String upperCase2 = str.toUpperCase(locale);
        D2.h.e(upperCase2, "this as java.lang.String).toUpperCase(Locale.ROOT)");
        return upperCase.compareTo(upperCase2) >= 0;
    }

    public static final boolean b() {
        int i3 = Build.VERSION.SDK_INT;
        if (i3 < 33) {
            if (i3 >= 32) {
                String str = Build.VERSION.CODENAME;
                D2.h.e(str, "CODENAME");
                if (a("Tiramisu", str)) {
                }
            }
            return false;
        }
        return true;
    }
}
