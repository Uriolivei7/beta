package androidx.appcompat.view.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.view.menu.k;
import androidx.core.view.AbstractC0239b;
import e.AbstractC0521a;
import n.InterfaceMenuItemC0616b;

/* JADX INFO: loaded from: classes.dex */
public final class g implements InterfaceMenuItemC0616b {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private View f3558A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private AbstractC0239b f3559B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private MenuItem.OnActionExpandListener f3560C;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private ContextMenu.ContextMenuInfo f3562E;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f3563a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f3564b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f3565c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f3566d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private CharSequence f3567e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private CharSequence f3568f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private Intent f3569g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private char f3570h;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private char f3572j;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private Drawable f3574l;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    e f3576n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private m f3577o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private Runnable f3578p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private MenuItem.OnMenuItemClickListener f3579q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private CharSequence f3580r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private CharSequence f3581s;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private int f3588z;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f3571i = 4096;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f3573k = 4096;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f3575m = 0;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private ColorStateList f3582t = null;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private PorterDuff.Mode f3583u = null;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private boolean f3584v = false;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private boolean f3585w = false;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private boolean f3586x = false;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private int f3587y = 16;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private boolean f3561D = false;

    class a implements AbstractC0239b.InterfaceC0067b {
        a() {
        }

        @Override // androidx.core.view.AbstractC0239b.InterfaceC0067b
        public void onActionProviderVisibilityChanged(boolean z3) {
            g gVar = g.this;
            gVar.f3576n.K(gVar);
        }
    }

    g(e eVar, int i3, int i4, int i5, int i6, CharSequence charSequence, int i7) {
        this.f3576n = eVar;
        this.f3563a = i4;
        this.f3564b = i3;
        this.f3565c = i5;
        this.f3566d = i6;
        this.f3567e = charSequence;
        this.f3588z = i7;
    }

    private static void d(StringBuilder sb, int i3, int i4, String str) {
        if ((i3 & i4) == i4) {
            sb.append(str);
        }
    }

    private Drawable e(Drawable drawable) {
        if (drawable != null && this.f3586x && (this.f3584v || this.f3585w)) {
            drawable = androidx.core.graphics.drawable.a.j(drawable).mutate();
            if (this.f3584v) {
                androidx.core.graphics.drawable.a.g(drawable, this.f3582t);
            }
            if (this.f3585w) {
                androidx.core.graphics.drawable.a.h(drawable, this.f3583u);
            }
            this.f3586x = false;
        }
        return drawable;
    }

    boolean A() {
        return this.f3576n.I() && g() != 0;
    }

    public boolean B() {
        return (this.f3588z & 4) == 4;
    }

    @Override // n.InterfaceMenuItemC0616b
    public InterfaceMenuItemC0616b a(AbstractC0239b abstractC0239b) {
        AbstractC0239b abstractC0239b2 = this.f3559B;
        if (abstractC0239b2 != null) {
            abstractC0239b2.h();
        }
        this.f3558A = null;
        this.f3559B = abstractC0239b;
        this.f3576n.L(true);
        AbstractC0239b abstractC0239b3 = this.f3559B;
        if (abstractC0239b3 != null) {
            abstractC0239b3.j(new a());
        }
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b
    public AbstractC0239b b() {
        return this.f3559B;
    }

    public void c() {
        this.f3576n.J(this);
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public boolean collapseActionView() {
        if ((this.f3588z & 8) == 0) {
            return false;
        }
        if (this.f3558A == null) {
            return true;
        }
        MenuItem.OnActionExpandListener onActionExpandListener = this.f3560C;
        if (onActionExpandListener == null || onActionExpandListener.onMenuItemActionCollapse(this)) {
            return this.f3576n.f(this);
        }
        return false;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public boolean expandActionView() {
        if (!j()) {
            return false;
        }
        MenuItem.OnActionExpandListener onActionExpandListener = this.f3560C;
        if (onActionExpandListener == null || onActionExpandListener.onMenuItemActionExpand(this)) {
            return this.f3576n.k(this);
        }
        return false;
    }

    public int f() {
        return this.f3566d;
    }

    char g() {
        return this.f3576n.H() ? this.f3572j : this.f3570h;
    }

    @Override // android.view.MenuItem
    public ActionProvider getActionProvider() {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.getActionProvider()");
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public View getActionView() {
        View view = this.f3558A;
        if (view != null) {
            return view;
        }
        AbstractC0239b abstractC0239b = this.f3559B;
        if (abstractC0239b == null) {
            return null;
        }
        View viewD = abstractC0239b.d(this);
        this.f3558A = viewD;
        return viewD;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public int getAlphabeticModifiers() {
        return this.f3573k;
    }

    @Override // android.view.MenuItem
    public char getAlphabeticShortcut() {
        return this.f3572j;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public CharSequence getContentDescription() {
        return this.f3580r;
    }

    @Override // android.view.MenuItem
    public int getGroupId() {
        return this.f3564b;
    }

    @Override // android.view.MenuItem
    public Drawable getIcon() {
        Drawable drawable = this.f3574l;
        if (drawable != null) {
            return e(drawable);
        }
        if (this.f3575m == 0) {
            return null;
        }
        Drawable drawableB = AbstractC0521a.b(this.f3576n.u(), this.f3575m);
        this.f3575m = 0;
        this.f3574l = drawableB;
        return e(drawableB);
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public ColorStateList getIconTintList() {
        return this.f3582t;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public PorterDuff.Mode getIconTintMode() {
        return this.f3583u;
    }

    @Override // android.view.MenuItem
    public Intent getIntent() {
        return this.f3569g;
    }

    @Override // android.view.MenuItem
    public int getItemId() {
        return this.f3563a;
    }

    @Override // android.view.MenuItem
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return this.f3562E;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public int getNumericModifiers() {
        return this.f3571i;
    }

    @Override // android.view.MenuItem
    public char getNumericShortcut() {
        return this.f3570h;
    }

    @Override // android.view.MenuItem
    public int getOrder() {
        return this.f3565c;
    }

    @Override // android.view.MenuItem
    public SubMenu getSubMenu() {
        return this.f3577o;
    }

    @Override // android.view.MenuItem
    public CharSequence getTitle() {
        return this.f3567e;
    }

    @Override // android.view.MenuItem
    public CharSequence getTitleCondensed() {
        CharSequence charSequence = this.f3568f;
        return charSequence != null ? charSequence : this.f3567e;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public CharSequence getTooltipText() {
        return this.f3581s;
    }

    String h() {
        char cG = g();
        if (cG == 0) {
            return "";
        }
        Resources resources = this.f3576n.u().getResources();
        StringBuilder sb = new StringBuilder();
        if (ViewConfiguration.get(this.f3576n.u()).hasPermanentMenuKey()) {
            sb.append(resources.getString(d.h.f8842m));
        }
        int i3 = this.f3576n.H() ? this.f3573k : this.f3571i;
        d(sb, i3, 65536, resources.getString(d.h.f8838i));
        d(sb, i3, 4096, resources.getString(d.h.f8834e));
        d(sb, i3, 2, resources.getString(d.h.f8833d));
        d(sb, i3, 1, resources.getString(d.h.f8839j));
        d(sb, i3, 4, resources.getString(d.h.f8841l));
        d(sb, i3, 8, resources.getString(d.h.f8837h));
        if (cG == '\b') {
            sb.append(resources.getString(d.h.f8835f));
        } else if (cG == '\n') {
            sb.append(resources.getString(d.h.f8836g));
        } else if (cG != ' ') {
            sb.append(cG);
        } else {
            sb.append(resources.getString(d.h.f8840k));
        }
        return sb.toString();
    }

    @Override // android.view.MenuItem
    public boolean hasSubMenu() {
        return this.f3577o != null;
    }

    CharSequence i(k.a aVar) {
        return (aVar == null || !aVar.a()) ? getTitle() : getTitleCondensed();
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public boolean isActionViewExpanded() {
        return this.f3561D;
    }

    @Override // android.view.MenuItem
    public boolean isCheckable() {
        return (this.f3587y & 1) == 1;
    }

    @Override // android.view.MenuItem
    public boolean isChecked() {
        return (this.f3587y & 2) == 2;
    }

    @Override // android.view.MenuItem
    public boolean isEnabled() {
        return (this.f3587y & 16) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isVisible() {
        AbstractC0239b abstractC0239b = this.f3559B;
        return (abstractC0239b == null || !abstractC0239b.g()) ? (this.f3587y & 8) == 0 : (this.f3587y & 8) == 0 && this.f3559B.b();
    }

    public boolean j() {
        AbstractC0239b abstractC0239b;
        if ((this.f3588z & 8) == 0) {
            return false;
        }
        if (this.f3558A == null && (abstractC0239b = this.f3559B) != null) {
            this.f3558A = abstractC0239b.d(this);
        }
        return this.f3558A != null;
    }

    public boolean k() {
        MenuItem.OnMenuItemClickListener onMenuItemClickListener = this.f3579q;
        if (onMenuItemClickListener != null && onMenuItemClickListener.onMenuItemClick(this)) {
            return true;
        }
        e eVar = this.f3576n;
        if (eVar.h(eVar, this)) {
            return true;
        }
        Runnable runnable = this.f3578p;
        if (runnable != null) {
            runnable.run();
            return true;
        }
        if (this.f3569g != null) {
            try {
                this.f3576n.u().startActivity(this.f3569g);
                return true;
            } catch (ActivityNotFoundException e4) {
                Log.e("MenuItemImpl", "Can't find activity to handle intent; ignoring", e4);
            }
        }
        AbstractC0239b abstractC0239b = this.f3559B;
        return abstractC0239b != null && abstractC0239b.e();
    }

    public boolean l() {
        return (this.f3587y & 32) == 32;
    }

    public boolean m() {
        return (this.f3587y & 4) != 0;
    }

    public boolean n() {
        return (this.f3588z & 1) == 1;
    }

    public boolean o() {
        return (this.f3588z & 2) == 2;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    /* JADX INFO: renamed from: p, reason: merged with bridge method [inline-methods] */
    public InterfaceMenuItemC0616b setActionView(int i3) {
        Context contextU = this.f3576n.u();
        setActionView(LayoutInflater.from(contextU).inflate(i3, (ViewGroup) new LinearLayout(contextU), false));
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
    public InterfaceMenuItemC0616b setActionView(View view) {
        int i3;
        this.f3558A = view;
        this.f3559B = null;
        if (view != null && view.getId() == -1 && (i3 = this.f3563a) > 0) {
            view.setId(i3);
        }
        this.f3576n.J(this);
        return this;
    }

    public void r(boolean z3) {
        this.f3561D = z3;
        this.f3576n.L(false);
    }

    void s(boolean z3) {
        int i3 = this.f3587y;
        int i4 = (z3 ? 2 : 0) | (i3 & (-3));
        this.f3587y = i4;
        if (i3 != i4) {
            this.f3576n.L(false);
        }
    }

    @Override // android.view.MenuItem
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.setActionProvider()");
    }

    @Override // android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c4) {
        if (this.f3572j == c4) {
            return this;
        }
        this.f3572j = Character.toLowerCase(c4);
        this.f3576n.L(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setCheckable(boolean z3) {
        int i3 = this.f3587y;
        int i4 = (z3 ? 1 : 0) | (i3 & (-2));
        this.f3587y = i4;
        if (i3 != i4) {
            this.f3576n.L(false);
        }
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setChecked(boolean z3) {
        if ((this.f3587y & 4) != 0) {
            this.f3576n.U(this);
        } else {
            s(z3);
        }
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setEnabled(boolean z3) {
        if (z3) {
            this.f3587y |= 16;
        } else {
            this.f3587y &= -17;
        }
        this.f3576n.L(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(Drawable drawable) {
        this.f3575m = 0;
        this.f3574l = drawable;
        this.f3586x = true;
        this.f3576n.L(false);
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public MenuItem setIconTintList(ColorStateList colorStateList) {
        this.f3582t = colorStateList;
        this.f3584v = true;
        this.f3586x = true;
        this.f3576n.L(false);
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public MenuItem setIconTintMode(PorterDuff.Mode mode) {
        this.f3583u = mode;
        this.f3585w = true;
        this.f3586x = true;
        this.f3576n.L(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIntent(Intent intent) {
        this.f3569g = intent;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setNumericShortcut(char c4) {
        if (this.f3570h == c4) {
            return this;
        }
        this.f3570h = c4;
        this.f3576n.L(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        this.f3560C = onActionExpandListener;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.f3579q = onMenuItemClickListener;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setShortcut(char c4, char c5) {
        this.f3570h = c4;
        this.f3572j = Character.toLowerCase(c5);
        this.f3576n.L(false);
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public void setShowAsAction(int i3) {
        int i4 = i3 & 3;
        if (i4 != 0 && i4 != 1 && i4 != 2) {
            throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
        }
        this.f3588z = i3;
        this.f3576n.J(this);
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(CharSequence charSequence) {
        this.f3567e = charSequence;
        this.f3576n.L(false);
        m mVar = this.f3577o;
        if (mVar != null) {
            mVar.setHeaderTitle(charSequence);
        }
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.f3568f = charSequence;
        this.f3576n.L(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setVisible(boolean z3) {
        if (y(z3)) {
            this.f3576n.K(this);
        }
        return this;
    }

    public void t(boolean z3) {
        this.f3587y = (z3 ? 4 : 0) | (this.f3587y & (-5));
    }

    public String toString() {
        CharSequence charSequence = this.f3567e;
        if (charSequence != null) {
            return charSequence.toString();
        }
        return null;
    }

    public void u(boolean z3) {
        if (z3) {
            this.f3587y |= 32;
        } else {
            this.f3587y &= -33;
        }
    }

    void v(ContextMenu.ContextMenuInfo contextMenuInfo) {
        this.f3562E = contextMenuInfo;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    /* JADX INFO: renamed from: w, reason: merged with bridge method [inline-methods] */
    public InterfaceMenuItemC0616b setShowAsActionFlags(int i3) {
        setShowAsAction(i3);
        return this;
    }

    public void x(m mVar) {
        this.f3577o = mVar;
        mVar.setHeaderTitle(getTitle());
    }

    boolean y(boolean z3) {
        int i3 = this.f3587y;
        int i4 = (z3 ? 0 : 8) | (i3 & (-9));
        this.f3587y = i4;
        return i3 != i4;
    }

    public boolean z() {
        return this.f3576n.A();
    }

    @Override // android.view.MenuItem
    public InterfaceMenuItemC0616b setContentDescription(CharSequence charSequence) {
        this.f3580r = charSequence;
        this.f3576n.L(false);
        return this;
    }

    @Override // android.view.MenuItem
    public InterfaceMenuItemC0616b setTooltipText(CharSequence charSequence) {
        this.f3581s = charSequence;
        this.f3576n.L(false);
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c4, int i3) {
        if (this.f3572j == c4 && this.f3573k == i3) {
            return this;
        }
        this.f3572j = Character.toLowerCase(c4);
        this.f3573k = KeyEvent.normalizeMetaState(i3);
        this.f3576n.L(false);
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public MenuItem setNumericShortcut(char c4, int i3) {
        if (this.f3570h == c4 && this.f3571i == i3) {
            return this;
        }
        this.f3570h = c4;
        this.f3571i = KeyEvent.normalizeMetaState(i3);
        this.f3576n.L(false);
        return this;
    }

    @Override // n.InterfaceMenuItemC0616b, android.view.MenuItem
    public MenuItem setShortcut(char c4, char c5, int i3, int i4) {
        this.f3570h = c4;
        this.f3571i = KeyEvent.normalizeMetaState(i3);
        this.f3572j = Character.toLowerCase(c5);
        this.f3573k = KeyEvent.normalizeMetaState(i4);
        this.f3576n.L(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(int i3) {
        this.f3574l = null;
        this.f3575m = i3;
        this.f3586x = true;
        this.f3576n.L(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(int i3) {
        return setTitle(this.f3576n.u().getString(i3));
    }
}
