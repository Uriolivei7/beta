package e1;

import K2.o;
import android.net.Uri;
import java.util.List;
import java.util.ListIterator;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.json.JSONException;
import org.json.JSONObject;
import s2.AbstractC0711h;
import s2.AbstractC0717n;

/* JADX INFO: renamed from: e1.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0526c extends RuntimeException {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f9349c = new a(null);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final String f9350d = "\n\nTry the following to fix the issue:\n\\u2022 Ensure that Metro is running\n\\u2022 Ensure that your device/emulator is connected to your machine and has USB debugging enabled - run 'adb devices' to see a list of connected devices\n\\u2022 Ensure Airplane Mode is disabled\n\\u2022 If you're on a physical device connected to the same machine, run 'adb reverse tcp:<PORT> tcp:<PORT> to forward requests from your device\n\\u2022 If your device is on the same Wi-Fi network, set 'Debug server host & port for device' in 'Dev settings' to your machine's IP address and the port of the local dev server - e.g. 10.0.1.1:<PORT>\n\n";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f9351b;

    /* JADX INFO: renamed from: e1.c$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final String d(String str) {
            List listG;
            List listD = new K2.k("/").d(str, 0);
            if (listD.isEmpty()) {
                listG = AbstractC0717n.g();
            } else {
                ListIterator listIterator = listD.listIterator(listD.size());
                while (listIterator.hasPrevious()) {
                    if (((String) listIterator.previous()).length() != 0) {
                        listG = AbstractC0717n.b0(listD, listIterator.nextIndex() + 1);
                        break;
                    }
                }
                listG = AbstractC0717n.g();
            }
            return (String) AbstractC0711h.x((String[]) listG.toArray(new String[0]));
        }

        public final C0526c a(String str, String str2, String str3, Throwable th) {
            D2.h.f(str, "url");
            D2.h.f(str2, "reason");
            D2.h.f(str3, "extra");
            return new C0526c(str2 + o.v(C0526c.f9350d, "<PORT>", String.valueOf(Uri.parse(str).getPort()), false, 4, null) + str3, th);
        }

        public final C0526c b(String str, String str2, Throwable th) {
            D2.h.f(str, "url");
            D2.h.f(str2, "reason");
            return a(str, str2, "", th);
        }

        public final C0526c c(String str, String str2) {
            if (str2 != null && str2.length() != 0) {
                try {
                    JSONObject jSONObject = new JSONObject(str2);
                    String string = jSONObject.getString("filename");
                    String string2 = jSONObject.getString("message");
                    D2.h.e(string2, "getString(...)");
                    D2.h.c(string);
                    return new C0526c(string2, d(string), jSONObject.getInt("lineNumber"), jSONObject.getInt("column"), null);
                } catch (JSONException e4) {
                    Y.a.J("ReactNative", "Could not parse DebugServerException from: " + str2, e4);
                }
            }
            return null;
        }

        private a() {
        }
    }

    public /* synthetic */ C0526c(String str, String str2, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, i3, i4);
    }

    public static final C0526c b(String str, String str2, String str3, Throwable th) {
        return f9349c.a(str, str2, str3, th);
    }

    public static final C0526c c(String str, String str2, Throwable th) {
        return f9349c.b(str, str2, th);
    }

    public static final C0526c d(String str, String str2) {
        return f9349c.c(str, str2);
    }

    private C0526c(String str, String str2, int i3, int i4) {
        super(str + "\n  at " + str2 + ":" + i3 + ":" + i4);
        this.f9351b = str;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0526c(String str) {
        super(str);
        D2.h.f(str, "description");
        this.f9351b = str;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0526c(String str, Throwable th) {
        super(str, th);
        D2.h.f(str, "detailMessage");
        this.f9351b = str;
    }
}
