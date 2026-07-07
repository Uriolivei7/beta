package androidx.core.app;

import android.app.PendingIntent;
import androidx.core.graphics.drawable.IconCompat;

/* JADX INFO: loaded from: classes.dex */
public class RemoteActionCompatParcelizer {
    public static RemoteActionCompat read(androidx.versionedparcelable.a aVar) {
        RemoteActionCompat remoteActionCompat = new RemoteActionCompat();
        remoteActionCompat.f4369a = (IconCompat) aVar.v(remoteActionCompat.f4369a, 1);
        remoteActionCompat.f4370b = aVar.l(remoteActionCompat.f4370b, 2);
        remoteActionCompat.f4371c = aVar.l(remoteActionCompat.f4371c, 3);
        remoteActionCompat.f4372d = (PendingIntent) aVar.r(remoteActionCompat.f4372d, 4);
        remoteActionCompat.f4373e = aVar.h(remoteActionCompat.f4373e, 5);
        remoteActionCompat.f4374f = aVar.h(remoteActionCompat.f4374f, 6);
        return remoteActionCompat;
    }

    public static void write(RemoteActionCompat remoteActionCompat, androidx.versionedparcelable.a aVar) {
        aVar.x(false, false);
        aVar.M(remoteActionCompat.f4369a, 1);
        aVar.D(remoteActionCompat.f4370b, 2);
        aVar.D(remoteActionCompat.f4371c, 3);
        aVar.H(remoteActionCompat.f4372d, 4);
        aVar.z(remoteActionCompat.f4373e, 5);
        aVar.z(remoteActionCompat.f4374f, 6);
    }
}
