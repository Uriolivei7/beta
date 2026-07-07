package K2;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class A extends z {
    public static final String x0(String str, int i3) {
        D2.h.f(str, "<this>");
        if (i3 >= 0) {
            String strSubstring = str.substring(H2.d.e(i3, str.length()));
            D2.h.e(strSubstring, "substring(...)");
            return strSubstring;
        }
        throw new IllegalArgumentException(("Requested character count " + i3 + " is less than zero.").toString());
    }

    public static String y0(String str, int i3) {
        D2.h.f(str, "<this>");
        if (i3 >= 0) {
            String strSubstring = str.substring(0, H2.d.e(i3, str.length()));
            D2.h.e(strSubstring, "substring(...)");
            return strSubstring;
        }
        throw new IllegalArgumentException(("Requested character count " + i3 + " is less than zero.").toString());
    }
}
