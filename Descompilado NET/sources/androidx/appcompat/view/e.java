package androidx.appcompat.view;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.b;
import androidx.appcompat.view.menu.e;
import androidx.appcompat.widget.ActionBarContextView;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class e extends b implements e.a {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Context f3367d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private ActionBarContextView f3368e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private b.a f3369f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private WeakReference f3370g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f3371h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f3372i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private androidx.appcompat.view.menu.e f3373j;

    public e(Context context, ActionBarContextView actionBarContextView, b.a aVar, boolean z3) {
        this.f3367d = context;
        this.f3368e = actionBarContextView;
        this.f3369f = aVar;
        androidx.appcompat.view.menu.e eVarT = new androidx.appcompat.view.menu.e(actionBarContextView.getContext()).T(1);
        this.f3373j = eVarT;
        eVarT.S(this);
        this.f3372i = z3;
    }

    @Override // androidx.appcompat.view.menu.e.a
    public boolean a(androidx.appcompat.view.menu.e eVar, MenuItem menuItem) {
        return this.f3369f.c(this, menuItem);
    }

    @Override // androidx.appcompat.view.menu.e.a
    public void b(androidx.appcompat.view.menu.e eVar) {
        k();
        this.f3368e.l();
    }

    @Override // androidx.appcompat.view.b
    public void c() {
        if (this.f3371h) {
            return;
        }
        this.f3371h = true;
        this.f3369f.b(this);
    }

    @Override // androidx.appcompat.view.b
    public View d() {
        WeakReference weakReference = this.f3370g;
        if (weakReference != null) {
            return (View) weakReference.get();
        }
        return null;
    }

    @Override // androidx.appcompat.view.b
    public Menu e() {
        return this.f3373j;
    }

    @Override // androidx.appcompat.view.b
    public MenuInflater f() {
        return new g(this.f3368e.getContext());
    }

    @Override // androidx.appcompat.view.b
    public CharSequence g() {
        return this.f3368e.getSubtitle();
    }

    @Override // androidx.appcompat.view.b
    public CharSequence i() {
        return this.f3368e.getTitle();
    }

    @Override // androidx.appcompat.view.b
    public void k() {
        this.f3369f.a(this, this.f3373j);
    }

    @Override // androidx.appcompat.view.b
    public boolean l() {
        return this.f3368e.j();
    }

    @Override // androidx.appcompat.view.b
    public void m(View view) {
        this.f3368e.setCustomView(view);
        this.f3370g = view != null ? new WeakReference(view) : null;
    }

    @Override // androidx.appcompat.view.b
    public void n(int i3) {
        o(this.f3367d.getString(i3));
    }

    @Override // androidx.appcompat.view.b
    public void o(CharSequence charSequence) {
        this.f3368e.setSubtitle(charSequence);
    }

    @Override // androidx.appcompat.view.b
    public void q(int i3) {
        r(this.f3367d.getString(i3));
    }

    @Override // androidx.appcompat.view.b
    public void r(CharSequence charSequence) {
        this.f3368e.setTitle(charSequence);
    }

    @Override // androidx.appcompat.view.b
    public void s(boolean z3) {
        super.s(z3);
        this.f3368e.setTitleOptional(z3);
    }
}
