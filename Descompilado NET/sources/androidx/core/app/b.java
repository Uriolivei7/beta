package androidx.core.app;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;

/* JADX INFO: loaded from: classes.dex */
public abstract class b extends androidx.core.content.a {
    public static void i(Activity activity) {
        activity.finishAffinity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void j(Activity activity) {
        if (activity.isFinishing() || c.i(activity)) {
            return;
        }
        activity.recreate();
    }

    public static void k(final Activity activity) {
        if (Build.VERSION.SDK_INT >= 28) {
            activity.recreate();
        } else {
            new Handler(activity.getMainLooper()).post(new Runnable() { // from class: androidx.core.app.a
                @Override // java.lang.Runnable
                public final void run() {
                    b.j(activity);
                }
            });
        }
    }
}
