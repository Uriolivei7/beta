package androidx.core.app;

import android.app.NotificationManager;
import android.content.Context;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final Object f4400c = new Object();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static Set f4401d = new HashSet();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final Object f4402e = new Object();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f4403a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final NotificationManager f4404b;

    static class a {
        static boolean a(NotificationManager notificationManager) {
            return notificationManager.areNotificationsEnabled();
        }

        static int b(NotificationManager notificationManager) {
            return notificationManager.getImportance();
        }
    }

    private i(Context context) {
        this.f4403a = context;
        this.f4404b = (NotificationManager) context.getSystemService("notification");
    }

    public static i b(Context context) {
        return new i(context);
    }

    public boolean a() {
        return a.a(this.f4404b);
    }
}
