package S2;

import K2.o;
import M2.A;
import java.net.ProtocolException;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class k {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f2338d = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final A f2339a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f2340b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f2341c;

    public static final class a {
        private a() {
        }

        public final k a(String str) throws ProtocolException {
            A a4;
            int i3;
            String strSubstring;
            D2.h.f(str, "statusLine");
            if (o.z(str, "HTTP/1.", false, 2, null)) {
                i3 = 9;
                if (str.length() < 9 || str.charAt(8) != ' ') {
                    throw new ProtocolException("Unexpected status line: " + str);
                }
                int iCharAt = str.charAt(7) - '0';
                if (iCharAt == 0) {
                    a4 = A.HTTP_1_0;
                } else {
                    if (iCharAt != 1) {
                        throw new ProtocolException("Unexpected status line: " + str);
                    }
                    a4 = A.HTTP_1_1;
                }
            } else {
                if (!o.z(str, "ICY ", false, 2, null)) {
                    throw new ProtocolException("Unexpected status line: " + str);
                }
                a4 = A.HTTP_1_0;
                i3 = 4;
            }
            int i4 = i3 + 3;
            if (str.length() < i4) {
                throw new ProtocolException("Unexpected status line: " + str);
            }
            try {
                String strSubstring2 = str.substring(i3, i4);
                D2.h.e(strSubstring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                int i5 = Integer.parseInt(strSubstring2);
                if (str.length() <= i4) {
                    strSubstring = "";
                } else {
                    if (str.charAt(i4) != ' ') {
                        throw new ProtocolException("Unexpected status line: " + str);
                    }
                    strSubstring = str.substring(i3 + 4);
                    D2.h.e(strSubstring, "(this as java.lang.String).substring(startIndex)");
                }
                return new k(a4, i5, strSubstring);
            } catch (NumberFormatException unused) {
                throw new ProtocolException("Unexpected status line: " + str);
            }
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public k(A a4, int i3, String str) {
        D2.h.f(a4, "protocol");
        D2.h.f(str, "message");
        this.f2339a = a4;
        this.f2340b = i3;
        this.f2341c = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.f2339a == A.HTTP_1_0) {
            sb.append("HTTP/1.0");
        } else {
            sb.append("HTTP/1.1");
        }
        sb.append(' ');
        sb.append(this.f2340b);
        sb.append(' ');
        sb.append(this.f2341c);
        String string = sb.toString();
        D2.h.e(string, "StringBuilder().apply(builderAction).toString()");
        return string;
    }
}
