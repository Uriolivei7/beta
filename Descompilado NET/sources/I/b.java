package i;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import l.g;
import n.InterfaceMenuItemC0616b;

/* JADX INFO: loaded from: classes.dex */
abstract class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final Context f9507a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private g f9508b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private g f9509c;

    b(Context context) {
        this.f9507a = context;
    }

    final MenuItem c(MenuItem menuItem) {
        if (!(menuItem instanceof InterfaceMenuItemC0616b)) {
            return menuItem;
        }
        InterfaceMenuItemC0616b interfaceMenuItemC0616b = (InterfaceMenuItemC0616b) menuItem;
        if (this.f9508b == null) {
            this.f9508b = new g();
        }
        MenuItem menuItem2 = (MenuItem) this.f9508b.get(interfaceMenuItemC0616b);
        if (menuItem2 != null) {
            return menuItem2;
        }
        c cVar = new c(this.f9507a, interfaceMenuItemC0616b);
        this.f9508b.put(interfaceMenuItemC0616b, cVar);
        return cVar;
    }

    final void e() {
        g gVar = this.f9508b;
        if (gVar != null) {
            gVar.clear();
        }
        g gVar2 = this.f9509c;
        if (gVar2 != null) {
            gVar2.clear();
        }
    }

    final void f(int i3) {
        if (this.f9508b == null) {
            return;
        }
        int i4 = 0;
        while (i4 < this.f9508b.size()) {
            if (((InterfaceMenuItemC0616b) this.f9508b.i(i4)).getGroupId() == i3) {
                this.f9508b.k(i4);
                i4--;
            }
            i4++;
        }
    }

    final void g(int i3) {
        if (this.f9508b == null) {
            return;
        }
        for (int i4 = 0; i4 < this.f9508b.size(); i4++) {
            if (((InterfaceMenuItemC0616b) this.f9508b.i(i4)).getItemId() == i3) {
                this.f9508b.k(i4);
                return;
            }
        }
    }

    final SubMenu d(SubMenu subMenu) {
        return subMenu;
    }
}
