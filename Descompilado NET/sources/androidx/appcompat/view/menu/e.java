package androidx.appcompat.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.core.view.AbstractC0239b;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import n.InterfaceMenuC0615a;

/* JADX INFO: loaded from: classes.dex */
public class e implements InterfaceMenuC0615a {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private static final int[] f3527A = {1, 4, 5, 3, 2, 0};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f3528a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Resources f3529b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f3530c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f3531d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private a f3532e;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private ContextMenu.ContextMenuInfo f3540m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    CharSequence f3541n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    Drawable f3542o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    View f3543p;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private g f3551x;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private boolean f3553z;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f3539l = 0;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private boolean f3544q = false;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private boolean f3545r = false;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private boolean f3546s = false;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private boolean f3547t = false;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private boolean f3548u = false;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private ArrayList f3549v = new ArrayList();

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private CopyOnWriteArrayList f3550w = new CopyOnWriteArrayList();

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private boolean f3552y = false;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private ArrayList f3533f = new ArrayList();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private ArrayList f3534g = new ArrayList();

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f3535h = true;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private ArrayList f3536i = new ArrayList();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private ArrayList f3537j = new ArrayList();

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f3538k = true;

    public interface a {
        boolean a(e eVar, MenuItem menuItem);

        void b(e eVar);
    }

    public interface b {
        boolean a(g gVar);
    }

    public e(Context context) {
        this.f3528a = context;
        this.f3529b = context.getResources();
        c0(true);
    }

    private static int B(int i3) {
        int i4 = ((-65536) & i3) >> 16;
        if (i4 >= 0) {
            int[] iArr = f3527A;
            if (i4 < iArr.length) {
                return (i3 & 65535) | (iArr[i4] << 16);
            }
        }
        throw new IllegalArgumentException("order does not contain a valid category.");
    }

    private void O(int i3, boolean z3) {
        if (i3 < 0 || i3 >= this.f3533f.size()) {
            return;
        }
        this.f3533f.remove(i3);
        if (z3) {
            L(true);
        }
    }

    private void X(int i3, CharSequence charSequence, int i4, Drawable drawable, View view) {
        Resources resourcesC = C();
        if (view != null) {
            this.f3543p = view;
            this.f3541n = null;
            this.f3542o = null;
        } else {
            if (i3 > 0) {
                this.f3541n = resourcesC.getText(i3);
            } else if (charSequence != null) {
                this.f3541n = charSequence;
            }
            if (i4 > 0) {
                this.f3542o = androidx.core.content.a.d(u(), i4);
            } else if (drawable != null) {
                this.f3542o = drawable;
            }
            this.f3543p = null;
        }
        L(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x001c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void c0(boolean r3) {
        /*
            r2 = this;
            if (r3 == 0) goto L1c
            android.content.res.Resources r3 = r2.f3529b
            android.content.res.Configuration r3 = r3.getConfiguration()
            int r3 = r3.keyboard
            r0 = 1
            if (r3 == r0) goto L1c
            android.content.Context r3 = r2.f3528a
            android.view.ViewConfiguration r3 = android.view.ViewConfiguration.get(r3)
            android.content.Context r1 = r2.f3528a
            boolean r3 = androidx.core.view.AbstractC0244d0.i(r3, r1)
            if (r3 == 0) goto L1c
            goto L1d
        L1c:
            r0 = 0
        L1d:
            r2.f3531d = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.view.menu.e.c0(boolean):void");
    }

    private g g(int i3, int i4, int i5, int i6, CharSequence charSequence, int i7) {
        return new g(this, i3, i4, i5, i6, charSequence, i7);
    }

    private void i(boolean z3) {
        if (this.f3550w.isEmpty()) {
            return;
        }
        e0();
        for (WeakReference weakReference : this.f3550w) {
            j jVar = (j) weakReference.get();
            if (jVar == null) {
                this.f3550w.remove(weakReference);
            } else {
                jVar.f(z3);
            }
        }
        d0();
    }

    private boolean j(m mVar, j jVar) {
        if (this.f3550w.isEmpty()) {
            return false;
        }
        boolean zE = jVar != null ? jVar.e(mVar) : false;
        for (WeakReference weakReference : this.f3550w) {
            j jVar2 = (j) weakReference.get();
            if (jVar2 == null) {
                this.f3550w.remove(weakReference);
            } else if (!zE) {
                zE = jVar2.e(mVar);
            }
        }
        return zE;
    }

    private static int n(ArrayList arrayList, int i3) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (((g) arrayList.get(size)).f() <= i3) {
                return size + 1;
            }
        }
        return 0;
    }

    boolean A() {
        return this.f3547t;
    }

    Resources C() {
        return this.f3529b;
    }

    public e D() {
        return this;
    }

    public ArrayList E() {
        if (!this.f3535h) {
            return this.f3534g;
        }
        this.f3534g.clear();
        int size = this.f3533f.size();
        for (int i3 = 0; i3 < size; i3++) {
            g gVar = (g) this.f3533f.get(i3);
            if (gVar.isVisible()) {
                this.f3534g.add(gVar);
            }
        }
        this.f3535h = false;
        this.f3538k = true;
        return this.f3534g;
    }

    public boolean F() {
        return !this.f3544q;
    }

    public boolean G() {
        return this.f3552y;
    }

    boolean H() {
        return this.f3530c;
    }

    public boolean I() {
        return this.f3531d;
    }

    void J(g gVar) {
        this.f3538k = true;
        L(true);
    }

    void K(g gVar) {
        this.f3535h = true;
        L(true);
    }

    public void L(boolean z3) {
        if (this.f3544q) {
            this.f3545r = true;
            if (z3) {
                this.f3546s = true;
                return;
            }
            return;
        }
        if (z3) {
            this.f3535h = true;
            this.f3538k = true;
        }
        i(z3);
    }

    public boolean M(MenuItem menuItem, int i3) {
        return N(menuItem, null, i3);
    }

    public boolean N(MenuItem menuItem, j jVar, int i3) {
        g gVar = (g) menuItem;
        if (gVar == null || !gVar.isEnabled()) {
            return false;
        }
        boolean zK = gVar.k();
        AbstractC0239b abstractC0239bB = gVar.b();
        boolean z3 = abstractC0239bB != null && abstractC0239bB.a();
        if (gVar.j()) {
            zK |= gVar.expandActionView();
            if (zK) {
                e(true);
            }
        } else if (gVar.hasSubMenu() || z3) {
            if ((i3 & 4) == 0) {
                e(false);
            }
            if (!gVar.hasSubMenu()) {
                gVar.x(new m(u(), this, gVar));
            }
            m mVar = (m) gVar.getSubMenu();
            if (z3) {
                abstractC0239bB.f(mVar);
            }
            zK |= j(mVar, jVar);
            if (!zK) {
                e(true);
            }
        } else if ((i3 & 1) == 0) {
            e(true);
        }
        return zK;
    }

    public void P(j jVar) {
        for (WeakReference weakReference : this.f3550w) {
            j jVar2 = (j) weakReference.get();
            if (jVar2 == null || jVar2 == jVar) {
                this.f3550w.remove(weakReference);
            }
        }
    }

    public void Q(Bundle bundle) {
        MenuItem menuItemFindItem;
        if (bundle == null) {
            return;
        }
        SparseArray<Parcelable> sparseParcelableArray = bundle.getSparseParcelableArray(t());
        int size = size();
        for (int i3 = 0; i3 < size; i3++) {
            MenuItem item = getItem(i3);
            View actionView = item.getActionView();
            if (actionView != null && actionView.getId() != -1) {
                actionView.restoreHierarchyState(sparseParcelableArray);
            }
            if (item.hasSubMenu()) {
                ((m) item.getSubMenu()).Q(bundle);
            }
        }
        int i4 = bundle.getInt("android:menu:expandedactionview");
        if (i4 <= 0 || (menuItemFindItem = findItem(i4)) == null) {
            return;
        }
        menuItemFindItem.expandActionView();
    }

    public void R(Bundle bundle) {
        int size = size();
        SparseArray<? extends Parcelable> sparseArray = null;
        for (int i3 = 0; i3 < size; i3++) {
            MenuItem item = getItem(i3);
            View actionView = item.getActionView();
            if (actionView != null && actionView.getId() != -1) {
                if (sparseArray == null) {
                    sparseArray = new SparseArray<>();
                }
                actionView.saveHierarchyState(sparseArray);
                if (item.isActionViewExpanded()) {
                    bundle.putInt("android:menu:expandedactionview", item.getItemId());
                }
            }
            if (item.hasSubMenu()) {
                ((m) item.getSubMenu()).R(bundle);
            }
        }
        if (sparseArray != null) {
            bundle.putSparseParcelableArray(t(), sparseArray);
        }
    }

    public void S(a aVar) {
        this.f3532e = aVar;
    }

    public e T(int i3) {
        this.f3539l = i3;
        return this;
    }

    void U(MenuItem menuItem) {
        int groupId = menuItem.getGroupId();
        int size = this.f3533f.size();
        e0();
        for (int i3 = 0; i3 < size; i3++) {
            g gVar = (g) this.f3533f.get(i3);
            if (gVar.getGroupId() == groupId && gVar.m() && gVar.isCheckable()) {
                gVar.s(gVar == menuItem);
            }
        }
        d0();
    }

    protected e V(int i3) {
        X(0, null, i3, null, null);
        return this;
    }

    protected e W(Drawable drawable) {
        X(0, null, 0, drawable, null);
        return this;
    }

    protected e Y(int i3) {
        X(i3, null, 0, null, null);
        return this;
    }

    protected e Z(CharSequence charSequence) {
        X(0, charSequence, 0, null, null);
        return this;
    }

    protected MenuItem a(int i3, int i4, int i5, CharSequence charSequence) {
        int iB = B(i5);
        g gVarG = g(i3, i4, i5, iB, charSequence, this.f3539l);
        ContextMenu.ContextMenuInfo contextMenuInfo = this.f3540m;
        if (contextMenuInfo != null) {
            gVarG.v(contextMenuInfo);
        }
        ArrayList arrayList = this.f3533f;
        arrayList.add(n(arrayList, iB), gVarG);
        L(true);
        return gVarG;
    }

    protected e a0(View view) {
        X(0, null, 0, null, view);
        return this;
    }

    @Override // android.view.Menu
    public MenuItem add(CharSequence charSequence) {
        return a(0, 0, 0, charSequence);
    }

    @Override // android.view.Menu
    public int addIntentOptions(int i3, int i4, int i5, ComponentName componentName, Intent[] intentArr, Intent intent, int i6, MenuItem[] menuItemArr) {
        int i7;
        PackageManager packageManager = this.f3528a.getPackageManager();
        List<ResolveInfo> listQueryIntentActivityOptions = packageManager.queryIntentActivityOptions(componentName, intentArr, intent, 0);
        int size = listQueryIntentActivityOptions != null ? listQueryIntentActivityOptions.size() : 0;
        if ((i6 & 1) == 0) {
            removeGroup(i3);
        }
        for (int i8 = 0; i8 < size; i8++) {
            ResolveInfo resolveInfo = listQueryIntentActivityOptions.get(i8);
            int i9 = resolveInfo.specificIndex;
            Intent intent2 = new Intent(i9 < 0 ? intent : intentArr[i9]);
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            intent2.setComponent(new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name));
            MenuItem intent3 = add(i3, i4, i5, resolveInfo.loadLabel(packageManager)).setIcon(resolveInfo.loadIcon(packageManager)).setIntent(intent2);
            if (menuItemArr != null && (i7 = resolveInfo.specificIndex) >= 0) {
                menuItemArr[i7] = intent3;
            }
        }
        return size;
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(CharSequence charSequence) {
        return addSubMenu(0, 0, 0, charSequence);
    }

    public void b(j jVar) {
        c(jVar, this.f3528a);
    }

    public void b0(boolean z3) {
        this.f3553z = z3;
    }

    public void c(j jVar, Context context) {
        this.f3550w.add(new WeakReference(jVar));
        jVar.d(context, this);
        this.f3538k = true;
    }

    @Override // android.view.Menu
    public void clear() {
        g gVar = this.f3551x;
        if (gVar != null) {
            f(gVar);
        }
        this.f3533f.clear();
        L(true);
    }

    public void clearHeader() {
        this.f3542o = null;
        this.f3541n = null;
        this.f3543p = null;
        L(false);
    }

    @Override // android.view.Menu
    public void close() {
        e(true);
    }

    public void d() {
        a aVar = this.f3532e;
        if (aVar != null) {
            aVar.b(this);
        }
    }

    public void d0() {
        this.f3544q = false;
        if (this.f3545r) {
            this.f3545r = false;
            L(this.f3546s);
        }
    }

    public final void e(boolean z3) {
        if (this.f3548u) {
            return;
        }
        this.f3548u = true;
        for (WeakReference weakReference : this.f3550w) {
            j jVar = (j) weakReference.get();
            if (jVar == null) {
                this.f3550w.remove(weakReference);
            } else {
                jVar.c(this, z3);
            }
        }
        this.f3548u = false;
    }

    public void e0() {
        if (this.f3544q) {
            return;
        }
        this.f3544q = true;
        this.f3545r = false;
        this.f3546s = false;
    }

    public boolean f(g gVar) {
        boolean zI = false;
        if (!this.f3550w.isEmpty() && this.f3551x == gVar) {
            e0();
            for (WeakReference weakReference : this.f3550w) {
                j jVar = (j) weakReference.get();
                if (jVar != null) {
                    zI = jVar.i(this, gVar);
                    if (zI) {
                        break;
                    }
                } else {
                    this.f3550w.remove(weakReference);
                }
            }
            d0();
            if (zI) {
                this.f3551x = null;
            }
        }
        return zI;
    }

    @Override // android.view.Menu
    public MenuItem findItem(int i3) {
        MenuItem menuItemFindItem;
        int size = size();
        for (int i4 = 0; i4 < size; i4++) {
            g gVar = (g) this.f3533f.get(i4);
            if (gVar.getItemId() == i3) {
                return gVar;
            }
            if (gVar.hasSubMenu() && (menuItemFindItem = gVar.getSubMenu().findItem(i3)) != null) {
                return menuItemFindItem;
            }
        }
        return null;
    }

    @Override // android.view.Menu
    public MenuItem getItem(int i3) {
        return (MenuItem) this.f3533f.get(i3);
    }

    boolean h(e eVar, MenuItem menuItem) {
        a aVar = this.f3532e;
        return aVar != null && aVar.a(eVar, menuItem);
    }

    @Override // android.view.Menu
    public boolean hasVisibleItems() {
        if (this.f3553z) {
            return true;
        }
        int size = size();
        for (int i3 = 0; i3 < size; i3++) {
            if (((g) this.f3533f.get(i3)).isVisible()) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.Menu
    public boolean isShortcutKey(int i3, KeyEvent keyEvent) {
        return p(i3, keyEvent) != null;
    }

    public boolean k(g gVar) {
        boolean zJ = false;
        if (this.f3550w.isEmpty()) {
            return false;
        }
        e0();
        for (WeakReference weakReference : this.f3550w) {
            j jVar = (j) weakReference.get();
            if (jVar != null) {
                zJ = jVar.j(this, gVar);
                if (zJ) {
                    break;
                }
            } else {
                this.f3550w.remove(weakReference);
            }
        }
        d0();
        if (zJ) {
            this.f3551x = gVar;
        }
        return zJ;
    }

    public int l(int i3) {
        return m(i3, 0);
    }

    public int m(int i3, int i4) {
        int size = size();
        if (i4 < 0) {
            i4 = 0;
        }
        while (i4 < size) {
            if (((g) this.f3533f.get(i4)).getGroupId() == i3) {
                return i4;
            }
            i4++;
        }
        return -1;
    }

    public int o(int i3) {
        int size = size();
        for (int i4 = 0; i4 < size; i4++) {
            if (((g) this.f3533f.get(i4)).getItemId() == i3) {
                return i4;
            }
        }
        return -1;
    }

    g p(int i3, KeyEvent keyEvent) {
        ArrayList arrayList = this.f3549v;
        arrayList.clear();
        q(arrayList, i3, keyEvent);
        if (arrayList.isEmpty()) {
            return null;
        }
        int metaState = keyEvent.getMetaState();
        KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
        keyEvent.getKeyData(keyData);
        int size = arrayList.size();
        if (size == 1) {
            return (g) arrayList.get(0);
        }
        boolean zH = H();
        for (int i4 = 0; i4 < size; i4++) {
            g gVar = (g) arrayList.get(i4);
            char alphabeticShortcut = zH ? gVar.getAlphabeticShortcut() : gVar.getNumericShortcut();
            char[] cArr = keyData.meta;
            if ((alphabeticShortcut == cArr[0] && (metaState & 2) == 0) || ((alphabeticShortcut == cArr[2] && (metaState & 2) != 0) || (zH && alphabeticShortcut == '\b' && i3 == 67))) {
                return gVar;
            }
        }
        return null;
    }

    @Override // android.view.Menu
    public boolean performIdentifierAction(int i3, int i4) {
        return M(findItem(i3), i4);
    }

    @Override // android.view.Menu
    public boolean performShortcut(int i3, KeyEvent keyEvent, int i4) {
        g gVarP = p(i3, keyEvent);
        boolean zM = gVarP != null ? M(gVarP, i4) : false;
        if ((i4 & 2) != 0) {
            e(true);
        }
        return zM;
    }

    void q(List list, int i3, KeyEvent keyEvent) {
        boolean zH = H();
        int modifiers = keyEvent.getModifiers();
        KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
        if (keyEvent.getKeyData(keyData) || i3 == 67) {
            int size = this.f3533f.size();
            for (int i4 = 0; i4 < size; i4++) {
                g gVar = (g) this.f3533f.get(i4);
                if (gVar.hasSubMenu()) {
                    ((e) gVar.getSubMenu()).q(list, i3, keyEvent);
                }
                char alphabeticShortcut = zH ? gVar.getAlphabeticShortcut() : gVar.getNumericShortcut();
                if ((modifiers & 69647) == ((zH ? gVar.getAlphabeticModifiers() : gVar.getNumericModifiers()) & 69647) && alphabeticShortcut != 0) {
                    char[] cArr = keyData.meta;
                    if ((alphabeticShortcut == cArr[0] || alphabeticShortcut == cArr[2] || (zH && alphabeticShortcut == '\b' && i3 == 67)) && gVar.isEnabled()) {
                        list.add(gVar);
                    }
                }
            }
        }
    }

    public void r() {
        ArrayList arrayListE = E();
        if (this.f3538k) {
            boolean zH = false;
            for (WeakReference weakReference : this.f3550w) {
                j jVar = (j) weakReference.get();
                if (jVar == null) {
                    this.f3550w.remove(weakReference);
                } else {
                    zH |= jVar.h();
                }
            }
            if (zH) {
                this.f3536i.clear();
                this.f3537j.clear();
                int size = arrayListE.size();
                for (int i3 = 0; i3 < size; i3++) {
                    g gVar = (g) arrayListE.get(i3);
                    if (gVar.l()) {
                        this.f3536i.add(gVar);
                    } else {
                        this.f3537j.add(gVar);
                    }
                }
            } else {
                this.f3536i.clear();
                this.f3537j.clear();
                this.f3537j.addAll(E());
            }
            this.f3538k = false;
        }
    }

    @Override // android.view.Menu
    public void removeGroup(int i3) {
        int iL = l(i3);
        if (iL >= 0) {
            int size = this.f3533f.size() - iL;
            int i4 = 0;
            while (true) {
                int i5 = i4 + 1;
                if (i4 >= size || ((g) this.f3533f.get(iL)).getGroupId() != i3) {
                    break;
                }
                O(iL, false);
                i4 = i5;
            }
            L(true);
        }
    }

    @Override // android.view.Menu
    public void removeItem(int i3) {
        O(o(i3), true);
    }

    public ArrayList s() {
        r();
        return this.f3536i;
    }

    @Override // android.view.Menu
    public void setGroupCheckable(int i3, boolean z3, boolean z4) {
        int size = this.f3533f.size();
        for (int i4 = 0; i4 < size; i4++) {
            g gVar = (g) this.f3533f.get(i4);
            if (gVar.getGroupId() == i3) {
                gVar.t(z4);
                gVar.setCheckable(z3);
            }
        }
    }

    @Override // android.view.Menu
    public void setGroupDividerEnabled(boolean z3) {
        this.f3552y = z3;
    }

    @Override // android.view.Menu
    public void setGroupEnabled(int i3, boolean z3) {
        int size = this.f3533f.size();
        for (int i4 = 0; i4 < size; i4++) {
            g gVar = (g) this.f3533f.get(i4);
            if (gVar.getGroupId() == i3) {
                gVar.setEnabled(z3);
            }
        }
    }

    @Override // android.view.Menu
    public void setGroupVisible(int i3, boolean z3) {
        int size = this.f3533f.size();
        boolean z4 = false;
        for (int i4 = 0; i4 < size; i4++) {
            g gVar = (g) this.f3533f.get(i4);
            if (gVar.getGroupId() == i3 && gVar.y(z3)) {
                z4 = true;
            }
        }
        if (z4) {
            L(true);
        }
    }

    @Override // android.view.Menu
    public void setQwertyMode(boolean z3) {
        this.f3530c = z3;
        L(false);
    }

    @Override // android.view.Menu
    public int size() {
        return this.f3533f.size();
    }

    protected String t() {
        return "android:menu:actionviewstates";
    }

    public Context u() {
        return this.f3528a;
    }

    public g v() {
        return this.f3551x;
    }

    public Drawable w() {
        return this.f3542o;
    }

    public CharSequence x() {
        return this.f3541n;
    }

    public View y() {
        return this.f3543p;
    }

    public ArrayList z() {
        r();
        return this.f3537j;
    }

    @Override // android.view.Menu
    public MenuItem add(int i3) {
        return a(0, 0, 0, this.f3529b.getString(i3));
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i3) {
        return addSubMenu(0, 0, 0, this.f3529b.getString(i3));
    }

    @Override // android.view.Menu
    public MenuItem add(int i3, int i4, int i5, CharSequence charSequence) {
        return a(i3, i4, i5, charSequence);
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i3, int i4, int i5, CharSequence charSequence) {
        g gVar = (g) a(i3, i4, i5, charSequence);
        m mVar = new m(this.f3528a, this, gVar);
        gVar.x(mVar);
        return mVar;
    }

    @Override // android.view.Menu
    public MenuItem add(int i3, int i4, int i5, int i6) {
        return a(i3, i4, i5, this.f3529b.getString(i6));
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i3, int i4, int i5, int i6) {
        return addSubMenu(i3, i4, i5, this.f3529b.getString(i6));
    }
}
