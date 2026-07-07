package i;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import n.InterfaceMenuC0615a;

/* JADX INFO: loaded from: classes.dex */
public class d extends b implements Menu {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final InterfaceMenuC0615a f9520d;

    public d(Context context, InterfaceMenuC0615a interfaceMenuC0615a) {
        super(context);
        if (interfaceMenuC0615a == null) {
            throw new IllegalArgumentException("Wrapped Object can not be null.");
        }
        this.f9520d = interfaceMenuC0615a;
    }

    @Override // android.view.Menu
    public MenuItem add(CharSequence charSequence) {
        return c(this.f9520d.add(charSequence));
    }

    @Override // android.view.Menu
    public int addIntentOptions(int i3, int i4, int i5, ComponentName componentName, Intent[] intentArr, Intent intent, int i6, MenuItem[] menuItemArr) {
        MenuItem[] menuItemArr2 = menuItemArr != null ? new MenuItem[menuItemArr.length] : null;
        int iAddIntentOptions = this.f9520d.addIntentOptions(i3, i4, i5, componentName, intentArr, intent, i6, menuItemArr2);
        if (menuItemArr2 != null) {
            int length = menuItemArr2.length;
            for (int i7 = 0; i7 < length; i7++) {
                menuItemArr[i7] = c(menuItemArr2[i7]);
            }
        }
        return iAddIntentOptions;
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(CharSequence charSequence) {
        return d(this.f9520d.addSubMenu(charSequence));
    }

    @Override // android.view.Menu
    public void clear() {
        e();
        this.f9520d.clear();
    }

    @Override // android.view.Menu
    public void close() {
        this.f9520d.close();
    }

    @Override // android.view.Menu
    public MenuItem findItem(int i3) {
        return c(this.f9520d.findItem(i3));
    }

    @Override // android.view.Menu
    public MenuItem getItem(int i3) {
        return c(this.f9520d.getItem(i3));
    }

    @Override // android.view.Menu
    public boolean hasVisibleItems() {
        return this.f9520d.hasVisibleItems();
    }

    @Override // android.view.Menu
    public boolean isShortcutKey(int i3, KeyEvent keyEvent) {
        return this.f9520d.isShortcutKey(i3, keyEvent);
    }

    @Override // android.view.Menu
    public boolean performIdentifierAction(int i3, int i4) {
        return this.f9520d.performIdentifierAction(i3, i4);
    }

    @Override // android.view.Menu
    public boolean performShortcut(int i3, KeyEvent keyEvent, int i4) {
        return this.f9520d.performShortcut(i3, keyEvent, i4);
    }

    @Override // android.view.Menu
    public void removeGroup(int i3) {
        f(i3);
        this.f9520d.removeGroup(i3);
    }

    @Override // android.view.Menu
    public void removeItem(int i3) {
        g(i3);
        this.f9520d.removeItem(i3);
    }

    @Override // android.view.Menu
    public void setGroupCheckable(int i3, boolean z3, boolean z4) {
        this.f9520d.setGroupCheckable(i3, z3, z4);
    }

    @Override // android.view.Menu
    public void setGroupEnabled(int i3, boolean z3) {
        this.f9520d.setGroupEnabled(i3, z3);
    }

    @Override // android.view.Menu
    public void setGroupVisible(int i3, boolean z3) {
        this.f9520d.setGroupVisible(i3, z3);
    }

    @Override // android.view.Menu
    public void setQwertyMode(boolean z3) {
        this.f9520d.setQwertyMode(z3);
    }

    @Override // android.view.Menu
    public int size() {
        return this.f9520d.size();
    }

    @Override // android.view.Menu
    public MenuItem add(int i3) {
        return c(this.f9520d.add(i3));
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i3) {
        return d(this.f9520d.addSubMenu(i3));
    }

    @Override // android.view.Menu
    public MenuItem add(int i3, int i4, int i5, CharSequence charSequence) {
        return c(this.f9520d.add(i3, i4, i5, charSequence));
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i3, int i4, int i5, CharSequence charSequence) {
        return d(this.f9520d.addSubMenu(i3, i4, i5, charSequence));
    }

    @Override // android.view.Menu
    public MenuItem add(int i3, int i4, int i5, int i6) {
        return c(this.f9520d.add(i3, i4, i5, i6));
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i3, int i4, int i5, int i6) {
        return d(this.f9520d.addSubMenu(i3, i4, i5, i6));
    }
}
