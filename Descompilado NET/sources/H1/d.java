package H1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public class d {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final a f352e = new a(null);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final String f353f = d.class.getSimpleName();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f354a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final SharedPreferences f355b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f356c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Map f357d;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public d(Context context) {
        D2.h.f(context, "appContext");
        this.f354a = context;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        D2.h.e(defaultSharedPreferences, "getDefaultSharedPreferences(...)");
        this.f355b = defaultSharedPreferences;
        String packageName = context.getPackageName();
        D2.h.e(packageName, "getPackageName(...)");
        this.f356c = packageName;
        this.f357d = new LinkedHashMap();
    }

    public final Map a() {
        return this.f357d;
    }

    public String b() {
        String string = this.f355b.getString("debug_http_host", null);
        if (string != null && string.length() > 0) {
            return string;
        }
        String strH = com.facebook.react.modules.systeminfo.a.h(this.f354a);
        if (D2.h.b(strH, "localhost")) {
            Y.a.I(f353f, "You seem to be running on device. Run '" + com.facebook.react.modules.systeminfo.a.b(this.f354a) + "' to forward the debug server's port to the device.");
        }
        return strH;
    }

    public final String c() {
        return this.f356c;
    }

    public void d(String str) {
        D2.h.f(str, "host");
        this.f355b.edit().putString("debug_http_host", str).apply();
    }
}
