package i;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.core.view.AbstractC0239b;
import n.InterfaceMenuItemC0616b;

/* JADX INFO: renamed from: i.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0558a implements InterfaceMenuItemC0616b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f9487a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f9488b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f9489c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private CharSequence f9490d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private CharSequence f9491e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Intent f9492f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private char f9493g;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private char f9495i;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private Drawable f9497k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private Context f9498l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private MenuItem.OnMenuItemClickListener f9499m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private CharSequence f9500n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private CharSequence f9501o;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f9494h = 4096;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f9496j = 4096;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private ColorStateList f9502p = null;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private PorterDuff.Mode f9503q = null;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private boolean f9504r = false;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private boolean f9505s = false;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private int f9506t = 16;

    public C0558a(Context context, int i3, int i4, int i5, int i6, CharSequence charSequence) {
        this.f9498l = context;
        this.f9487a = i4;
        this.f9488b = i3;
        this.f9489c = i6;
        this.f9490d = charSequence;
    }

    private void c() {
        Drawable drawable = this.f9497k;
        if (drawable != null) {
            if (this.f9504r || this.f9505s) {
                Drawable drawableJ = androidx.core.graphics.drawable.a.j(drawable);
                this.f9497k = drawableJ;
                Drawable drawableMutate = drawableJ.mutate();
                this.f9497k = drawableMutate;
                if (this.f9504r) {
                    androidx.core.graphics.drawable.a.g(drawableMutate, this.f9502p);
                }
                if (this.f9505s) {
                    androidx.core.graphics.drawable.a.h(this.f9497k, this.f9503q);
                }
            }
        }
    }

    @Override // n.InterfaceMenuItemC0616b
    public InterfaceMenuItemC0616b a(AbstractC0239b abstractC0239b) {
        throw new UnsupportedOperationException();
    }

    @Override // n.InterfaceMenuItemC0616b
    public AbstractC0239b b() {
        return null;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public boolean collapseActionView() {
        return false;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    /* JADX INFO: renamed from: d, reason: merged with bridge method [inline-methods] */
    public InterfaceMenuItemC0616b setActionView(int i3) {
        throw new UnsupportedOperationException();
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
    public InterfaceMenuItemC0616b setActionView(View view) {
        throw new UnsupportedOperationException();
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public boolean expandActionView() {
        return false;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    /* JADX INFO: renamed from: f, reason: merged with bridge method [inline-methods] */
    public InterfaceMenuItemC0616b setShowAsActionFlags(int i3) {
        setShowAsAction(i3);
        return this;
    }

    @Override // android.view.MenuItem
    public ActionProvider getActionProvider() {
        throw new UnsupportedOperationException();
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public View getActionView() {
        return null;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public int getAlphabeticModifiers() {
        return this.f9496j;
    }

    @Override // android.view.MenuItem
    public char getAlphabeticShortcut() {
        return this.f9495i;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public CharSequence getContentDescription() {
        return this.f9500n;
    }

    @Override // android.view.MenuItem
    public int getGroupId() {
        return this.f9488b;
    }

    @Override // android.view.MenuItem
    public Drawable getIcon() {
        return this.f9497k;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public ColorStateList getIconTintList() {
        return this.f9502p;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public PorterDuff.Mode getIconTintMode() {
        return this.f9503q;
    }

    @Override // android.view.MenuItem
    public Intent getIntent() {
        return this.f9492f;
    }

    @Override // android.view.MenuItem
    public int getItemId() {
        return this.f9487a;
    }

    @Override // android.view.MenuItem
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public int getNumericModifiers() {
        return this.f9494h;
    }

    @Override // android.view.MenuItem
    public char getNumericShortcut() {
        return this.f9493g;
    }

    @Override // android.view.MenuItem
    public int getOrder() {
        return this.f9489c;
    }

    @Override // android.view.MenuItem
    public SubMenu getSubMenu() {
        return null;
    }

    @Override // android.view.MenuItem
    public CharSequence getTitle() {
        return this.f9490d;
    }

    @Override // android.view.MenuItem
    public CharSequence getTitleCondensed() {
        CharSequence charSequence = this.f9491e;
        return charSequence != null ? charSequence : this.f9490d;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public CharSequence getTooltipText() {
        return this.f9501o;
    }

    @Override // android.view.MenuItem
    public boolean hasSubMenu() {
        return false;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public boolean isActionViewExpanded() {
        return false;
    }

    @Override // android.view.MenuItem
    public boolean isCheckable() {
        return (this.f9506t & 1) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isChecked() {
        return (this.f9506t & 2) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isEnabled() {
        return (this.f9506t & 16) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isVisible() {
        return (this.f9506t & 8) == 0;
    }

    @Override // android.view.MenuItem
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        throw new UnsupportedOperationException();
    }

    @Override // android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c4) {
        this.f9495i = Character.toLowerCase(c4);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setCheckable(boolean z3) {
        this.f9506t = (z3 ? 1 : 0) | (this.f9506t & (-2));
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setChecked(boolean z3) {
        this.f9506t = (z3 ? 2 : 0) | (this.f9506t & (-3));
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setEnabled(boolean z3) {
        this.f9506t = (z3 ? 16 : 0) | (this.f9506t & (-17));
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(Drawable drawable) {
        this.f9497k = drawable;
        c();
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public MenuItem setIconTintList(ColorStateList colorStateList) {
        this.f9502p = colorStateList;
        this.f9504r = true;
        c();
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public MenuItem setIconTintMode(PorterDuff.Mode mode) {
        this.f9503q = mode;
        this.f9505s = true;
        c();
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIntent(Intent intent) {
        this.f9492f = intent;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setNumericShortcut(char c4) {
        this.f9493g = c4;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        throw new UnsupportedOperationException();
    }

    @Override // android.view.MenuItem
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.f9499m = onMenuItemClickListener;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setShortcut(char c4, char c5) {
        this.f9493g = c4;
        this.f9495i = Character.toLowerCase(c5);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(CharSequence charSequence) {
        this.f9490d = charSequence;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.f9491e = charSequence;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setVisible(boolean z3) {
        this.f9506t = (this.f9506t & 8) | (z3 ? 0 : 8);
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c4, int i3) {
        this.f9495i = Character.toLowerCase(c4);
        this.f9496j = KeyEvent.normalizeMetaState(i3);
        return this;
    }

    @Override // android.view.MenuItem
    public InterfaceMenuItemC0616b setContentDescription(CharSequence charSequence) {
        this.f9500n = charSequence;
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public MenuItem setNumericShortcut(char c4, int i3) {
        this.f9493g = c4;
        this.f9494h = KeyEvent.normalizeMetaState(i3);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(int i3) {
        this.f9490d = this.f9498l.getResources().getString(i3);
        return this;
    }

    @Override // android.view.MenuItem
    public InterfaceMenuItemC0616b setTooltipText(CharSequence charSequence) {
        this.f9501o = charSequence;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(int i3) {
        this.f9497k = androidx.core.content.a.d(this.f9498l, i3);
        c();
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public MenuItem setShortcut(char c4, char c5, int i3, int i4) {
        this.f9493g = c4;
        this.f9494h = KeyEvent.normalizeMetaState(i3);
        this.f9495i = Character.toLowerCase(c5);
        this.f9496j = KeyEvent.normalizeMetaState(i4);
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public void setShowAsAction(int i3) {
    }
}
