package androidx.appcompat.app;

import a.InterfaceC0206b;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.appcompat.view.b;
import androidx.appcompat.widget.r0;
import androidx.core.app.n;
import androidx.fragment.app.ActivityC0288j;
import androidx.lifecycle.I;
import androidx.lifecycle.J;
import androidx.savedstate.a;

/* JADX INFO: loaded from: classes.dex */
public class c extends ActivityC0288j implements d, n.a {

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private f f3165B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private Resources f3166C;

    class a implements a.c {
        a() {
        }

        @Override // androidx.savedstate.a.c
        public Bundle a() {
            Bundle bundle = new Bundle();
            c.this.d0().D(bundle);
            return bundle;
        }
    }

    class b implements InterfaceC0206b {
        b() {
        }

        @Override // a.InterfaceC0206b
        public void a(Context context) {
            f fVarD0 = c.this.d0();
            fVarD0.u();
            fVarD0.z(c.this.b().b("androidx:appcompat"));
        }
    }

    public c() {
        f0();
    }

    private void I() {
        I.a(getWindow().getDecorView(), this);
        J.a(getWindow().getDecorView(), this);
        G.e.a(getWindow().getDecorView(), this);
        androidx.activity.r.a(getWindow().getDecorView(), this);
    }

    private void f0() {
        b().h("androidx:appcompat", new a());
        E(new b());
    }

    private boolean m0(KeyEvent keyEvent) {
        Window window;
        return (Build.VERSION.SDK_INT >= 26 || keyEvent.isCtrlPressed() || KeyEvent.metaStateHasNoModifiers(keyEvent.getMetaState()) || keyEvent.getRepeatCount() != 0 || KeyEvent.isModifierKey(keyEvent.getKeyCode()) || (window = getWindow()) == null || window.getDecorView() == null || !window.getDecorView().dispatchKeyShortcutEvent(keyEvent)) ? false : true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        I();
        d0().e(view, layoutParams);
    }

    @Override // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(d0().i(context));
    }

    @Override // android.app.Activity
    public void closeOptionsMenu() {
        androidx.appcompat.app.a aVarE0 = e0();
        if (getWindow().hasFeature(0)) {
            if (aVarE0 == null || !aVarE0.g()) {
                super.closeOptionsMenu();
            }
        }
    }

    public f d0() {
        if (this.f3165B == null) {
            this.f3165B = f.j(this, this);
        }
        return this.f3165B;
    }

    @Override // androidx.core.app.f, android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        androidx.appcompat.app.a aVarE0 = e0();
        if (keyCode == 82 && aVarE0 != null && aVarE0.p(keyEvent)) {
            return true;
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    public androidx.appcompat.app.a e0() {
        return d0().t();
    }

    @Override // androidx.appcompat.app.d
    public void f(androidx.appcompat.view.b bVar) {
    }

    @Override // android.app.Activity
    public View findViewById(int i3) {
        return d0().l(i3);
    }

    public void g0(androidx.core.app.n nVar) {
        nVar.b(this);
    }

    @Override // android.app.Activity
    public MenuInflater getMenuInflater() {
        return d0().r();
    }

    @Override // android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        if (this.f3166C == null && r0.c()) {
            this.f3166C = new r0(this, super.getResources());
        }
        Resources resources = this.f3166C;
        return resources == null ? super.getResources() : resources;
    }

    @Override // androidx.appcompat.app.d
    public void h(androidx.appcompat.view.b bVar) {
    }

    protected void h0(androidx.core.os.e eVar) {
    }

    protected void i0(int i3) {
    }

    @Override // android.app.Activity
    public void invalidateOptionsMenu() {
        d0().v();
    }

    public void j0(androidx.core.app.n nVar) {
    }

    public void k0() {
    }

    public boolean l0() {
        Intent intentP = p();
        if (intentP == null) {
            return false;
        }
        if (!o0(intentP)) {
            n0(intentP);
            return true;
        }
        androidx.core.app.n nVarE = androidx.core.app.n.e(this);
        g0(nVarE);
        j0(nVarE);
        nVarE.f();
        try {
            androidx.core.app.b.i(this);
            return true;
        } catch (IllegalStateException unused) {
            finish();
            return true;
        }
    }

    public void n0(Intent intent) {
        androidx.core.app.h.e(this, intent);
    }

    public boolean o0(Intent intent) {
        return androidx.core.app.h.f(this, intent);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        d0().y(configuration);
        if (this.f3166C != null) {
            this.f3166C.updateConfiguration(super.getResources().getConfiguration(), super.getResources().getDisplayMetrics());
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onContentChanged() {
        k0();
    }

    @Override // androidx.fragment.app.ActivityC0288j, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        d0().A();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i3, KeyEvent keyEvent) {
        if (m0(keyEvent)) {
            return true;
        }
        return super.onKeyDown(i3, keyEvent);
    }

    @Override // androidx.fragment.app.ActivityC0288j, androidx.activity.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public final boolean onMenuItemSelected(int i3, MenuItem menuItem) {
        if (super.onMenuItemSelected(i3, menuItem)) {
            return true;
        }
        androidx.appcompat.app.a aVarE0 = e0();
        if (menuItem.getItemId() != 16908332 || aVarE0 == null || (aVarE0.j() & 4) == 0) {
            return false;
        }
        return l0();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onMenuOpened(int i3, Menu menu) {
        return super.onMenuOpened(i3, menu);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public void onPanelClosed(int i3, Menu menu) {
        super.onPanelClosed(i3, menu);
    }

    @Override // android.app.Activity
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        d0().B(bundle);
    }

    @Override // androidx.fragment.app.ActivityC0288j, android.app.Activity
    protected void onPostResume() {
        super.onPostResume();
        d0().C();
    }

    @Override // androidx.fragment.app.ActivityC0288j, android.app.Activity
    protected void onStart() {
        super.onStart();
        d0().E();
    }

    @Override // androidx.fragment.app.ActivityC0288j, android.app.Activity
    protected void onStop() {
        super.onStop();
        d0().F();
    }

    @Override // android.app.Activity
    protected void onTitleChanged(CharSequence charSequence, int i3) {
        super.onTitleChanged(charSequence, i3);
        d0().P(charSequence);
    }

    @Override // android.app.Activity
    public void openOptionsMenu() {
        androidx.appcompat.app.a aVarE0 = e0();
        if (getWindow().hasFeature(0)) {
            if (aVarE0 == null || !aVarE0.q()) {
                super.openOptionsMenu();
            }
        }
    }

    @Override // androidx.core.app.n.a
    public Intent p() {
        return androidx.core.app.h.a(this);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(int i3) {
        I();
        d0().J(i3);
    }

    @Override // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public void setTheme(int i3) {
        super.setTheme(i3);
        d0().O(i3);
    }

    @Override // androidx.appcompat.app.d
    public androidx.appcompat.view.b w(b.a aVar) {
        return null;
    }

    public c(int i3) {
        super(i3);
        f0();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(View view) {
        I();
        d0().K(view);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        I();
        d0().L(view, layoutParams);
    }
}
