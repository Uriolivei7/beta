package j2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;

/* JADX INFO: renamed from: j2.f, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0576f {
    public static void a(Context context, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, boolean z3) {
        if (Build.VERSION.SDK_INT < 34 || context.getApplicationInfo().targetSdkVersion < 34) {
            context.registerReceiver(broadcastReceiver, intentFilter);
        } else {
            context.registerReceiver(broadcastReceiver, intentFilter, z3 ? 2 : 4);
        }
    }

    public static boolean b(Context context) {
        return androidx.core.content.a.a(context, "android.permission.ACCESS_WIFI_STATE") == 0;
    }

    public static void c(byte[] bArr) {
        for (int i3 = 0; i3 < bArr.length / 2; i3++) {
            byte b4 = bArr[i3];
            bArr[i3] = bArr[(bArr.length - i3) - 1];
            bArr[(bArr.length - i3) - 1] = b4;
        }
    }
}
