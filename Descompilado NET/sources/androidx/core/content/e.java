package androidx.core.content;

import android.content.Context;
import android.os.Process;

/* JADX INFO: loaded from: classes.dex */
public abstract class e {
    public static int a(Context context, String str, int i3, int i4, String str2) {
        if (context.checkPermission(str, i3, i4) == -1) {
            return -1;
        }
        String strC = androidx.core.app.e.c(str);
        if (strC == null) {
            return 0;
        }
        if (str2 == null) {
            String[] packagesForUid = context.getPackageManager().getPackagesForUid(i4);
            if (packagesForUid == null || packagesForUid.length <= 0) {
                return -1;
            }
            str2 = packagesForUid[0];
        }
        return ((Process.myUid() != i4 || !q.c.a(context.getPackageName(), str2)) ? androidx.core.app.e.b(context, strC, str2) : androidx.core.app.e.a(context, i4, strC, str2)) == 0 ? 0 : -2;
    }

    public static int b(Context context, String str) {
        return a(context, str, Process.myPid(), Process.myUid(), context.getPackageName());
    }
}
