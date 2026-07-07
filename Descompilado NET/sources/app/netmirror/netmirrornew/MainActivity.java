package app.netmirror.netmirrornew;

import android.os.Bundle;
import androidx.core.view.AbstractC0262m0;
import androidx.core.view.C0264n0;
import androidx.core.view.M0;
import com.facebook.react.defaults.b;
import d1.AbstractActivityC0510s;
import d1.C0514w;

/* JADX INFO: loaded from: classes.dex */
public final class MainActivity extends AbstractActivityC0510s {
    @Override // d1.AbstractActivityC0510s, androidx.fragment.app.ActivityC0288j, androidx.activity.ComponentActivity, androidx.core.app.f, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRequestedOrientation(10);
        AbstractC0262m0.b(getWindow(), false);
        getWindow().setFlags(512, 512);
        M0 m02 = new M0(getWindow(), getWindow().getDecorView());
        m02.a(C0264n0.m.e());
        m02.e(2);
        getWindow().setNavigationBarColor(0);
        getWindow().setStatusBarColor(0);
    }

    @Override // d1.AbstractActivityC0510s, android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z3) {
        super.onWindowFocusChanged(z3);
        if (z3) {
            AbstractC0262m0.a(getWindow(), getWindow().getDecorView()).a(C0264n0.m.e());
        }
    }

    @Override // d1.AbstractActivityC0510s
    protected C0514w p0() {
        return new b(this, q0(), false, 4, null);
    }

    protected String q0() {
        return "netmirror_beta";
    }
}
