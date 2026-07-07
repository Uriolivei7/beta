package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.appcompat.view.menu.e;

/* JADX INFO: loaded from: classes.dex */
public class m extends e implements SubMenu {

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private e f3627B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private g f3628C;

    public m(Context context, e eVar, g gVar) {
        super(context);
        this.f3627B = eVar;
        this.f3628C = gVar;
    }

    @Override // androidx.appcompat.view.menu.e
    public e D() {
        return this.f3627B.D();
    }

    @Override // androidx.appcompat.view.menu.e
    public boolean G() {
        return this.f3627B.G();
    }

    @Override // androidx.appcompat.view.menu.e
    public boolean H() {
        return this.f3627B.H();
    }

    @Override // androidx.appcompat.view.menu.e
    public boolean I() {
        return this.f3627B.I();
    }

    @Override // androidx.appcompat.view.menu.e
    public void S(e.a aVar) {
        this.f3627B.S(aVar);
    }

    @Override // androidx.appcompat.view.menu.e
    public boolean f(g gVar) {
        return this.f3627B.f(gVar);
    }

    public Menu f0() {
        return this.f3627B;
    }

    @Override // android.view.SubMenu
    public MenuItem getItem() {
        return this.f3628C;
    }

    @Override // androidx.appcompat.view.menu.e
    boolean h(e eVar, MenuItem menuItem) {
        return super.h(eVar, menuItem) || this.f3627B.h(eVar, menuItem);
    }

    @Override // androidx.appcompat.view.menu.e
    public boolean k(g gVar) {
        return this.f3627B.k(gVar);
    }

    @Override // androidx.appcompat.view.menu.e, android.view.Menu
    public void setGroupDividerEnabled(boolean z3) {
        this.f3627B.setGroupDividerEnabled(z3);
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderIcon(Drawable drawable) {
        return (SubMenu) super.W(drawable);
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderTitle(CharSequence charSequence) {
        return (SubMenu) super.Z(charSequence);
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderView(View view) {
        return (SubMenu) super.a0(view);
    }

    @Override // android.view.SubMenu
    public SubMenu setIcon(Drawable drawable) {
        this.f3628C.setIcon(drawable);
        return this;
    }

    @Override // androidx.appcompat.view.menu.e, android.view.Menu
    public void setQwertyMode(boolean z3) {
        this.f3627B.setQwertyMode(z3);
    }

    @Override // androidx.appcompat.view.menu.e
    public String t() {
        g gVar = this.f3628C;
        int itemId = gVar != null ? gVar.getItemId() : 0;
        if (itemId == 0) {
            return null;
        }
        return super.t() + ":" + itemId;
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderIcon(int i3) {
        return (SubMenu) super.V(i3);
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderTitle(int i3) {
        return (SubMenu) super.Y(i3);
    }

    @Override // android.view.SubMenu
    public SubMenu setIcon(int i3) {
        this.f3628C.setIcon(i3);
        return this;
    }
}
