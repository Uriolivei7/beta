package d1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import p1.InterfaceC0636a;

/* JADX INFO: renamed from: d1.A, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public interface InterfaceC0491A {
    InterfaceC0636a a(Context context, String str, Bundle bundle);

    k1.e b();

    void c(Context context);

    void d(Activity activity);

    void e(Activity activity, B1.a aVar);

    void f(Activity activity);

    boolean g();

    void h(Activity activity);

    void onActivityResult(Activity activity, int i3, int i4, Intent intent);

    void onNewIntent(Intent intent);

    void onWindowFocusChange(boolean z3);
}
