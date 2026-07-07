package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

/* JADX INFO: renamed from: androidx.fragment.app.l, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0290l {
    public Fragment e(Context context, String str, Bundle bundle) {
        return Fragment.V(context, str, bundle);
    }

    public abstract View f(int i3);

    public abstract boolean h();
}
