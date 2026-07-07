package androidx.appcompat.view.menu;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.view.menu.j;
import androidx.appcompat.widget.V;
import androidx.appcompat.widget.W;
import androidx.core.view.AbstractC0275w;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class b extends h implements j, View.OnKeyListener, PopupWindow.OnDismissListener {

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private static final int f3473C = d.g.f8814e;

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private PopupWindow.OnDismissListener f3474A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    boolean f3475B;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Context f3476c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f3477d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final int f3478e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final int f3479f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final boolean f3480g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    final Handler f3481h;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private View f3489p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    View f3490q;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private boolean f3492s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private boolean f3493t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private int f3494u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private int f3495v;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private boolean f3497x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private j.a f3498y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    ViewTreeObserver f3499z;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final List f3482i = new ArrayList();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    final List f3483j = new ArrayList();

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    final ViewTreeObserver.OnGlobalLayoutListener f3484k = new a();

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final View.OnAttachStateChangeListener f3485l = new ViewOnAttachStateChangeListenerC0053b();

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final V f3486m = new c();

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f3487n = 0;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private int f3488o = 0;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private boolean f3496w = false;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f3491r = D();

    class a implements ViewTreeObserver.OnGlobalLayoutListener {
        a() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (!b.this.a() || b.this.f3483j.size() <= 0 || ((d) b.this.f3483j.get(0)).f3507a.x()) {
                return;
            }
            View view = b.this.f3490q;
            if (view == null || !view.isShown()) {
                b.this.dismiss();
                return;
            }
            Iterator it = b.this.f3483j.iterator();
            while (it.hasNext()) {
                ((d) it.next()).f3507a.b();
            }
        }
    }

    /* JADX INFO: renamed from: androidx.appcompat.view.menu.b$b, reason: collision with other inner class name */
    class ViewOnAttachStateChangeListenerC0053b implements View.OnAttachStateChangeListener {
        ViewOnAttachStateChangeListenerC0053b() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            ViewTreeObserver viewTreeObserver = b.this.f3499z;
            if (viewTreeObserver != null) {
                if (!viewTreeObserver.isAlive()) {
                    b.this.f3499z = view.getViewTreeObserver();
                }
                b bVar = b.this;
                bVar.f3499z.removeGlobalOnLayoutListener(bVar.f3484k);
            }
            view.removeOnAttachStateChangeListener(this);
        }
    }

    class c implements V {

        class a implements Runnable {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ d f3503b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            final /* synthetic */ MenuItem f3504c;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            final /* synthetic */ e f3505d;

            a(d dVar, MenuItem menuItem, e eVar) {
                this.f3503b = dVar;
                this.f3504c = menuItem;
                this.f3505d = eVar;
            }

            @Override // java.lang.Runnable
            public void run() {
                d dVar = this.f3503b;
                if (dVar != null) {
                    b.this.f3475B = true;
                    dVar.f3508b.e(false);
                    b.this.f3475B = false;
                }
                if (this.f3504c.isEnabled() && this.f3504c.hasSubMenu()) {
                    this.f3505d.M(this.f3504c, 4);
                }
            }
        }

        c() {
        }

        @Override // androidx.appcompat.widget.V
        public void d(e eVar, MenuItem menuItem) {
            b.this.f3481h.removeCallbacksAndMessages(null);
            int size = b.this.f3483j.size();
            int i3 = 0;
            while (true) {
                if (i3 >= size) {
                    i3 = -1;
                    break;
                } else if (eVar == ((d) b.this.f3483j.get(i3)).f3508b) {
                    break;
                } else {
                    i3++;
                }
            }
            if (i3 == -1) {
                return;
            }
            int i4 = i3 + 1;
            b.this.f3481h.postAtTime(new a(i4 < b.this.f3483j.size() ? (d) b.this.f3483j.get(i4) : null, menuItem, eVar), eVar, SystemClock.uptimeMillis() + 200);
        }

        @Override // androidx.appcompat.widget.V
        public void e(e eVar, MenuItem menuItem) {
            b.this.f3481h.removeCallbacksAndMessages(eVar);
        }
    }

    private static class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final W f3507a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final e f3508b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final int f3509c;

        public d(W w3, e eVar, int i3) {
            this.f3507a = w3;
            this.f3508b = eVar;
            this.f3509c = i3;
        }

        public ListView a() {
            return this.f3507a.g();
        }
    }

    public b(Context context, View view, int i3, int i4, boolean z3) {
        this.f3476c = context;
        this.f3489p = view;
        this.f3478e = i3;
        this.f3479f = i4;
        this.f3480g = z3;
        Resources resources = context.getResources();
        this.f3477d = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(d.d.f8711d));
        this.f3481h = new Handler();
    }

    private int A(e eVar) {
        int size = this.f3483j.size();
        for (int i3 = 0; i3 < size; i3++) {
            if (eVar == ((d) this.f3483j.get(i3)).f3508b) {
                return i3;
            }
        }
        return -1;
    }

    private MenuItem B(e eVar, e eVar2) {
        int size = eVar.size();
        for (int i3 = 0; i3 < size; i3++) {
            MenuItem item = eVar.getItem(i3);
            if (item.hasSubMenu() && eVar2 == item.getSubMenu()) {
                return item;
            }
        }
        return null;
    }

    private View C(d dVar, e eVar) {
        androidx.appcompat.view.menu.d dVar2;
        int headersCount;
        int firstVisiblePosition;
        MenuItem menuItemB = B(dVar.f3508b, eVar);
        if (menuItemB == null) {
            return null;
        }
        ListView listViewA = dVar.a();
        ListAdapter adapter = listViewA.getAdapter();
        int i3 = 0;
        if (adapter instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
            headersCount = headerViewListAdapter.getHeadersCount();
            dVar2 = (androidx.appcompat.view.menu.d) headerViewListAdapter.getWrappedAdapter();
        } else {
            dVar2 = (androidx.appcompat.view.menu.d) adapter;
            headersCount = 0;
        }
        int count = dVar2.getCount();
        while (true) {
            if (i3 >= count) {
                i3 = -1;
                break;
            }
            if (menuItemB == dVar2.getItem(i3)) {
                break;
            }
            i3++;
        }
        if (i3 != -1 && (firstVisiblePosition = (i3 + headersCount) - listViewA.getFirstVisiblePosition()) >= 0 && firstVisiblePosition < listViewA.getChildCount()) {
            return listViewA.getChildAt(firstVisiblePosition);
        }
        return null;
    }

    private int D() {
        return this.f3489p.getLayoutDirection() == 1 ? 0 : 1;
    }

    private int E(int i3) {
        List list = this.f3483j;
        ListView listViewA = ((d) list.get(list.size() - 1)).a();
        int[] iArr = new int[2];
        listViewA.getLocationOnScreen(iArr);
        Rect rect = new Rect();
        this.f3490q.getWindowVisibleDisplayFrame(rect);
        return this.f3491r == 1 ? (iArr[0] + listViewA.getWidth()) + i3 > rect.right ? 0 : 1 : iArr[0] - i3 < 0 ? 1 : 0;
    }

    private void F(e eVar) {
        d dVar;
        View viewC;
        int i3;
        int i4;
        int i5;
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.f3476c);
        androidx.appcompat.view.menu.d dVar2 = new androidx.appcompat.view.menu.d(eVar, layoutInflaterFrom, this.f3480g, f3473C);
        if (!a() && this.f3496w) {
            dVar2.d(true);
        } else if (a()) {
            dVar2.d(h.x(eVar));
        }
        int iO = h.o(dVar2, null, this.f3476c, this.f3477d);
        W wZ = z();
        wZ.p(dVar2);
        wZ.B(iO);
        wZ.C(this.f3488o);
        if (this.f3483j.size() > 0) {
            List list = this.f3483j;
            dVar = (d) list.get(list.size() - 1);
            viewC = C(dVar, eVar);
        } else {
            dVar = null;
            viewC = null;
        }
        if (viewC != null) {
            wZ.Q(false);
            wZ.N(null);
            int iE = E(iO);
            boolean z3 = iE == 1;
            this.f3491r = iE;
            if (Build.VERSION.SDK_INT >= 26) {
                wZ.z(viewC);
                i4 = 0;
                i3 = 0;
            } else {
                int[] iArr = new int[2];
                this.f3489p.getLocationOnScreen(iArr);
                int[] iArr2 = new int[2];
                viewC.getLocationOnScreen(iArr2);
                if ((this.f3488o & 7) == 5) {
                    iArr[0] = iArr[0] + this.f3489p.getWidth();
                    iArr2[0] = iArr2[0] + viewC.getWidth();
                }
                i3 = iArr2[0] - iArr[0];
                i4 = iArr2[1] - iArr[1];
            }
            if ((this.f3488o & 5) == 5) {
                if (!z3) {
                    iO = viewC.getWidth();
                    i5 = i3 - iO;
                }
                i5 = i3 + iO;
            } else {
                if (z3) {
                    iO = viewC.getWidth();
                    i5 = i3 + iO;
                }
                i5 = i3 - iO;
            }
            wZ.l(i5);
            wZ.I(true);
            wZ.j(i4);
        } else {
            if (this.f3492s) {
                wZ.l(this.f3494u);
            }
            if (this.f3493t) {
                wZ.j(this.f3495v);
            }
            wZ.D(n());
        }
        this.f3483j.add(new d(wZ, eVar, this.f3491r));
        wZ.b();
        ListView listViewG = wZ.g();
        listViewG.setOnKeyListener(this);
        if (dVar == null && this.f3497x && eVar.x() != null) {
            FrameLayout frameLayout = (FrameLayout) layoutInflaterFrom.inflate(d.g.f8821l, (ViewGroup) listViewG, false);
            TextView textView = (TextView) frameLayout.findViewById(R.id.title);
            frameLayout.setEnabled(false);
            textView.setText(eVar.x());
            listViewG.addHeaderView(frameLayout, null, false);
            wZ.b();
        }
    }

    private W z() {
        W w3 = new W(this.f3476c, null, this.f3478e, this.f3479f);
        w3.P(this.f3486m);
        w3.H(this);
        w3.G(this);
        w3.z(this.f3489p);
        w3.C(this.f3488o);
        w3.F(true);
        w3.E(2);
        return w3;
    }

    @Override // i.e
    public boolean a() {
        return this.f3483j.size() > 0 && ((d) this.f3483j.get(0)).f3507a.a();
    }

    @Override // i.e
    public void b() {
        if (a()) {
            return;
        }
        Iterator it = this.f3482i.iterator();
        while (it.hasNext()) {
            F((e) it.next());
        }
        this.f3482i.clear();
        View view = this.f3489p;
        this.f3490q = view;
        if (view != null) {
            boolean z3 = this.f3499z == null;
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            this.f3499z = viewTreeObserver;
            if (z3) {
                viewTreeObserver.addOnGlobalLayoutListener(this.f3484k);
            }
            this.f3490q.addOnAttachStateChangeListener(this.f3485l);
        }
    }

    @Override // androidx.appcompat.view.menu.j
    public void c(e eVar, boolean z3) {
        int iA = A(eVar);
        if (iA < 0) {
            return;
        }
        int i3 = iA + 1;
        if (i3 < this.f3483j.size()) {
            ((d) this.f3483j.get(i3)).f3508b.e(false);
        }
        d dVar = (d) this.f3483j.remove(iA);
        dVar.f3508b.P(this);
        if (this.f3475B) {
            dVar.f3507a.O(null);
            dVar.f3507a.A(0);
        }
        dVar.f3507a.dismiss();
        int size = this.f3483j.size();
        if (size > 0) {
            this.f3491r = ((d) this.f3483j.get(size - 1)).f3509c;
        } else {
            this.f3491r = D();
        }
        if (size != 0) {
            if (z3) {
                ((d) this.f3483j.get(0)).f3508b.e(false);
                return;
            }
            return;
        }
        dismiss();
        j.a aVar = this.f3498y;
        if (aVar != null) {
            aVar.c(eVar, true);
        }
        ViewTreeObserver viewTreeObserver = this.f3499z;
        if (viewTreeObserver != null) {
            if (viewTreeObserver.isAlive()) {
                this.f3499z.removeGlobalOnLayoutListener(this.f3484k);
            }
            this.f3499z = null;
        }
        this.f3490q.removeOnAttachStateChangeListener(this.f3485l);
        this.f3474A.onDismiss();
    }

    @Override // i.e
    public void dismiss() {
        int size = this.f3483j.size();
        if (size > 0) {
            d[] dVarArr = (d[]) this.f3483j.toArray(new d[size]);
            for (int i3 = size - 1; i3 >= 0; i3--) {
                d dVar = dVarArr[i3];
                if (dVar.f3507a.a()) {
                    dVar.f3507a.dismiss();
                }
            }
        }
    }

    @Override // androidx.appcompat.view.menu.j
    public boolean e(m mVar) {
        for (d dVar : this.f3483j) {
            if (mVar == dVar.f3508b) {
                dVar.a().requestFocus();
                return true;
            }
        }
        if (!mVar.hasVisibleItems()) {
            return false;
        }
        l(mVar);
        j.a aVar = this.f3498y;
        if (aVar != null) {
            aVar.d(mVar);
        }
        return true;
    }

    @Override // androidx.appcompat.view.menu.j
    public void f(boolean z3) {
        Iterator it = this.f3483j.iterator();
        while (it.hasNext()) {
            h.y(((d) it.next()).a().getAdapter()).notifyDataSetChanged();
        }
    }

    @Override // i.e
    public ListView g() {
        if (this.f3483j.isEmpty()) {
            return null;
        }
        return ((d) this.f3483j.get(r0.size() - 1)).a();
    }

    @Override // androidx.appcompat.view.menu.j
    public boolean h() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.j
    public void k(j.a aVar) {
        this.f3498y = aVar;
    }

    @Override // androidx.appcompat.view.menu.h
    public void l(e eVar) {
        eVar.c(this, this.f3476c);
        if (a()) {
            F(eVar);
        } else {
            this.f3482i.add(eVar);
        }
    }

    @Override // androidx.appcompat.view.menu.h
    protected boolean m() {
        return false;
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        d dVar;
        int size = this.f3483j.size();
        int i3 = 0;
        while (true) {
            if (i3 >= size) {
                dVar = null;
                break;
            }
            dVar = (d) this.f3483j.get(i3);
            if (!dVar.f3507a.a()) {
                break;
            } else {
                i3++;
            }
        }
        if (dVar != null) {
            dVar.f3508b.e(false);
        }
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i3, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1 || i3 != 82) {
            return false;
        }
        dismiss();
        return true;
    }

    @Override // androidx.appcompat.view.menu.h
    public void p(View view) {
        if (this.f3489p != view) {
            this.f3489p = view;
            this.f3488o = AbstractC0275w.a(this.f3487n, view.getLayoutDirection());
        }
    }

    @Override // androidx.appcompat.view.menu.h
    public void r(boolean z3) {
        this.f3496w = z3;
    }

    @Override // androidx.appcompat.view.menu.h
    public void s(int i3) {
        if (this.f3487n != i3) {
            this.f3487n = i3;
            this.f3488o = AbstractC0275w.a(i3, this.f3489p.getLayoutDirection());
        }
    }

    @Override // androidx.appcompat.view.menu.h
    public void t(int i3) {
        this.f3492s = true;
        this.f3494u = i3;
    }

    @Override // androidx.appcompat.view.menu.h
    public void u(PopupWindow.OnDismissListener onDismissListener) {
        this.f3474A = onDismissListener;
    }

    @Override // androidx.appcompat.view.menu.h
    public void v(boolean z3) {
        this.f3497x = z3;
    }

    @Override // androidx.appcompat.view.menu.h
    public void w(int i3) {
        this.f3493t = true;
        this.f3495v = i3;
    }
}
