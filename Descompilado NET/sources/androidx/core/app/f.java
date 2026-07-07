package androidx.core.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import androidx.core.view.AbstractC0276x;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.u;

/* JADX INFO: loaded from: classes.dex */
public class f extends Activity implements androidx.lifecycle.l, AbstractC0276x.a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final l.g f4396b = new l.g();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final androidx.lifecycle.m f4397c = new androidx.lifecycle.m(this);

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final boolean z(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return false;
        }
        String str = strArr[0];
        switch (str.hashCode()) {
            case -645125871:
                return str.equals("--translation") && Build.VERSION.SDK_INT >= 31;
            case 100470631:
                if (!str.equals("--dump-dumpable")) {
                    return false;
                }
                break;
            case 472614934:
                if (!str.equals("--list-dumpables")) {
                    return false;
                }
                break;
            case 1159329357:
                return str.equals("--contentcapture") && Build.VERSION.SDK_INT >= 29;
            case 1455016274:
                return str.equals("--autofill") && Build.VERSION.SDK_INT >= 26;
            default:
                return false;
        }
        return Build.VERSION.SDK_INT >= 33;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        D2.h.f(keyEvent, "event");
        View decorView = getWindow().getDecorView();
        D2.h.e(decorView, "window.decorView");
        if (AbstractC0276x.d(decorView, keyEvent)) {
            return true;
        }
        return AbstractC0276x.e(this, decorView, this, keyEvent);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
        D2.h.f(keyEvent, "event");
        View decorView = getWindow().getDecorView();
        D2.h.e(decorView, "window.decorView");
        if (AbstractC0276x.d(decorView, keyEvent)) {
            return true;
        }
        return super.dispatchKeyShortcutEvent(keyEvent);
    }

    @Override // androidx.core.view.AbstractC0276x.a
    public boolean e(KeyEvent keyEvent) {
        D2.h.f(keyEvent, "event");
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        u.f5358c.c(this);
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        D2.h.f(bundle, "outState");
        this.f4397c.m(AbstractC0299g.b.CREATED);
        super.onSaveInstanceState(bundle);
    }

    public AbstractC0299g t() {
        return this.f4397c;
    }

    protected final boolean y(String[] strArr) {
        return !z(strArr);
    }
}
