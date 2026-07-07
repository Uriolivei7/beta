package androidx.appcompat.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public abstract class b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Object f3359b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f3360c;

    public interface a {
        boolean a(b bVar, Menu menu);

        void b(b bVar);

        boolean c(b bVar, MenuItem menuItem);

        boolean d(b bVar, Menu menu);
    }

    public abstract void c();

    public abstract View d();

    public abstract Menu e();

    public abstract MenuInflater f();

    public abstract CharSequence g();

    public Object h() {
        return this.f3359b;
    }

    public abstract CharSequence i();

    public boolean j() {
        return this.f3360c;
    }

    public abstract void k();

    public boolean l() {
        return false;
    }

    public abstract void m(View view);

    public abstract void n(int i3);

    public abstract void o(CharSequence charSequence);

    public void p(Object obj) {
        this.f3359b = obj;
    }

    public abstract void q(int i3);

    public abstract void r(CharSequence charSequence);

    public void s(boolean z3) {
        this.f3360c = z3;
    }
}
