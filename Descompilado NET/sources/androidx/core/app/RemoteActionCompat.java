package androidx.core.app;

import android.app.PendingIntent;
import androidx.core.graphics.drawable.IconCompat;

/* JADX INFO: loaded from: classes.dex */
public final class RemoteActionCompat implements L.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public IconCompat f4369a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public CharSequence f4370b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public CharSequence f4371c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public PendingIntent f4372d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public boolean f4373e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public boolean f4374f;

    public RemoteActionCompat(IconCompat iconCompat, CharSequence charSequence, CharSequence charSequence2, PendingIntent pendingIntent) {
        this.f4369a = (IconCompat) q.g.g(iconCompat);
        this.f4370b = (CharSequence) q.g.g(charSequence);
        this.f4371c = (CharSequence) q.g.g(charSequence2);
        this.f4372d = (PendingIntent) q.g.g(pendingIntent);
        this.f4373e = true;
        this.f4374f = true;
    }

    public RemoteActionCompat() {
    }

    public RemoteActionCompat(RemoteActionCompat remoteActionCompat) {
        q.g.g(remoteActionCompat);
        this.f4369a = remoteActionCompat.f4369a;
        this.f4370b = remoteActionCompat.f4370b;
        this.f4371c = remoteActionCompat.f4371c;
        this.f4372d = remoteActionCompat.f4372d;
        this.f4373e = remoteActionCompat.f4373e;
        this.f4374f = remoteActionCompat.f4374f;
    }
}
