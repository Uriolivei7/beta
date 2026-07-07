package androidx.core.view;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

/* JADX INFO: renamed from: androidx.core.view.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0239b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f4596a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private a f4597b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private InterfaceC0067b f4598c;

    /* JADX INFO: renamed from: androidx.core.view.b$a */
    public interface a {
    }

    /* JADX INFO: renamed from: androidx.core.view.b$b, reason: collision with other inner class name */
    public interface InterfaceC0067b {
        void onActionProviderVisibilityChanged(boolean z3);
    }

    public AbstractC0239b(Context context) {
        this.f4596a = context;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return true;
    }

    public abstract View c();

    public View d(MenuItem menuItem) {
        return c();
    }

    public boolean e() {
        return false;
    }

    public void f(SubMenu subMenu) {
    }

    public boolean g() {
        return false;
    }

    public void h() {
        this.f4598c = null;
        this.f4597b = null;
    }

    public void i(a aVar) {
        this.f4597b = aVar;
    }

    public void j(InterfaceC0067b interfaceC0067b) {
        if (this.f4598c != null && interfaceC0067b != null) {
            Log.w("ActionProvider(support)", "setVisibilityListener: Setting a new ActionProvider.VisibilityListener when one is already set. Are you reusing this " + getClass().getSimpleName() + " instance while it is still in use somewhere else?");
        }
        this.f4598c = interfaceC0067b;
    }
}
