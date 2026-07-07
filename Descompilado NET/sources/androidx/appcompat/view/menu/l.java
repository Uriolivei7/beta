package androidx.appcompat.view.menu;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.view.menu.j;
import androidx.appcompat.widget.W;

/* JADX INFO: loaded from: classes.dex */
final class l extends h implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener, j, View.OnKeyListener {

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private static final int f3604w = d.g.f8822m;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Context f3605c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final e f3606d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final d f3607e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final boolean f3608f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final int f3609g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final int f3610h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final int f3611i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    final W f3612j;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private PopupWindow.OnDismissListener f3615m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private View f3616n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    View f3617o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private j.a f3618p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    ViewTreeObserver f3619q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private boolean f3620r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private boolean f3621s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private int f3622t;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private boolean f3624v;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    final ViewTreeObserver.OnGlobalLayoutListener f3613k = new a();

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final View.OnAttachStateChangeListener f3614l = new b();

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private int f3623u = 0;

    class a implements ViewTreeObserver.OnGlobalLayoutListener {
        a() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (!l.this.a() || l.this.f3612j.x()) {
                return;
            }
            View view = l.this.f3617o;
            if (view == null || !view.isShown()) {
                l.this.dismiss();
            } else {
                l.this.f3612j.b();
            }
        }
    }

    class b implements View.OnAttachStateChangeListener {
        b() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            ViewTreeObserver viewTreeObserver = l.this.f3619q;
            if (viewTreeObserver != null) {
                if (!viewTreeObserver.isAlive()) {
                    l.this.f3619q = view.getViewTreeObserver();
                }
                l lVar = l.this;
                lVar.f3619q.removeGlobalOnLayoutListener(lVar.f3613k);
            }
            view.removeOnAttachStateChangeListener(this);
        }
    }

    public l(Context context, e eVar, View view, int i3, int i4, boolean z3) {
        this.f3605c = context;
        this.f3606d = eVar;
        this.f3608f = z3;
        this.f3607e = new d(eVar, LayoutInflater.from(context), z3, f3604w);
        this.f3610h = i3;
        this.f3611i = i4;
        Resources resources = context.getResources();
        this.f3609g = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(d.d.f8711d));
        this.f3616n = view;
        this.f3612j = new W(context, null, i3, i4);
        eVar.c(this, context);
    }

    private boolean z() {
        View view;
        if (a()) {
            return true;
        }
        if (this.f3620r || (view = this.f3616n) == null) {
            return false;
        }
        this.f3617o = view;
        this.f3612j.G(this);
        this.f3612j.H(this);
        this.f3612j.F(true);
        View view2 = this.f3617o;
        boolean z3 = this.f3619q == null;
        ViewTreeObserver viewTreeObserver = view2.getViewTreeObserver();
        this.f3619q = viewTreeObserver;
        if (z3) {
            viewTreeObserver.addOnGlobalLayoutListener(this.f3613k);
        }
        view2.addOnAttachStateChangeListener(this.f3614l);
        this.f3612j.z(view2);
        this.f3612j.C(this.f3623u);
        if (!this.f3621s) {
            this.f3622t = h.o(this.f3607e, null, this.f3605c, this.f3609g);
            this.f3621s = true;
        }
        this.f3612j.B(this.f3622t);
        this.f3612j.E(2);
        this.f3612j.D(n());
        this.f3612j.b();
        ListView listViewG = this.f3612j.g();
        listViewG.setOnKeyListener(this);
        if (this.f3624v && this.f3606d.x() != null) {
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this.f3605c).inflate(d.g.f8821l, (ViewGroup) listViewG, false);
            TextView textView = (TextView) frameLayout.findViewById(R.id.title);
            if (textView != null) {
                textView.setText(this.f3606d.x());
            }
            frameLayout.setEnabled(false);
            listViewG.addHeaderView(frameLayout, null, false);
        }
        this.f3612j.p(this.f3607e);
        this.f3612j.b();
        return true;
    }

    @Override // i.e
    public boolean a() {
        return !this.f3620r && this.f3612j.a();
    }

    @Override // i.e
    public void b() {
        if (!z()) {
            throw new IllegalStateException("StandardMenuPopup cannot be used without an anchor");
        }
    }

    @Override // androidx.appcompat.view.menu.j
    public void c(e eVar, boolean z3) {
        if (eVar != this.f3606d) {
            return;
        }
        dismiss();
        j.a aVar = this.f3618p;
        if (aVar != null) {
            aVar.c(eVar, z3);
        }
    }

    @Override // i.e
    public void dismiss() {
        if (a()) {
            this.f3612j.dismiss();
        }
    }

    @Override // androidx.appcompat.view.menu.j
    public boolean e(m mVar) {
        if (mVar.hasVisibleItems()) {
            i iVar = new i(this.f3605c, mVar, this.f3617o, this.f3608f, this.f3610h, this.f3611i);
            iVar.j(this.f3618p);
            iVar.g(h.x(mVar));
            iVar.i(this.f3615m);
            this.f3615m = null;
            this.f3606d.e(false);
            int iC = this.f3612j.c();
            int iN = this.f3612j.n();
            if ((Gravity.getAbsoluteGravity(this.f3623u, this.f3616n.getLayoutDirection()) & 7) == 5) {
                iC += this.f3616n.getWidth();
            }
            if (iVar.n(iC, iN)) {
                j.a aVar = this.f3618p;
                if (aVar == null) {
                    return true;
                }
                aVar.d(mVar);
                return true;
            }
        }
        return false;
    }

    @Override // androidx.appcompat.view.menu.j
    public void f(boolean z3) {
        this.f3621s = false;
        d dVar = this.f3607e;
        if (dVar != null) {
            dVar.notifyDataSetChanged();
        }
    }

    @Override // i.e
    public ListView g() {
        return this.f3612j.g();
    }

    @Override // androidx.appcompat.view.menu.j
    public boolean h() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.j
    public void k(j.a aVar) {
        this.f3618p = aVar;
    }

    @Override // androidx.appcompat.view.menu.h
    public void l(e eVar) {
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        this.f3620r = true;
        this.f3606d.close();
        ViewTreeObserver viewTreeObserver = this.f3619q;
        if (viewTreeObserver != null) {
            if (!viewTreeObserver.isAlive()) {
                this.f3619q = this.f3617o.getViewTreeObserver();
            }
            this.f3619q.removeGlobalOnLayoutListener(this.f3613k);
            this.f3619q = null;
        }
        this.f3617o.removeOnAttachStateChangeListener(this.f3614l);
        PopupWindow.OnDismissListener onDismissListener = this.f3615m;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
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
        this.f3616n = view;
    }

    @Override // androidx.appcompat.view.menu.h
    public void r(boolean z3) {
        this.f3607e.d(z3);
    }

    @Override // androidx.appcompat.view.menu.h
    public void s(int i3) {
        this.f3623u = i3;
    }

    @Override // androidx.appcompat.view.menu.h
    public void t(int i3) {
        this.f3612j.l(i3);
    }

    @Override // androidx.appcompat.view.menu.h
    public void u(PopupWindow.OnDismissListener onDismissListener) {
        this.f3615m = onDismissListener;
    }

    @Override // androidx.appcompat.view.menu.h
    public void v(boolean z3) {
        this.f3624v = z3;
    }

    @Override // androidx.appcompat.view.menu.h
    public void w(int i3) {
        this.f3612j.j(i3);
    }
}
