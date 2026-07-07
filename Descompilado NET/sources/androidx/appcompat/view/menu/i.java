package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import androidx.appcompat.view.menu.j;
import androidx.core.view.AbstractC0275w;
import d.AbstractC0487a;

/* JADX INFO: loaded from: classes.dex */
public class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f3591a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final e f3592b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final boolean f3593c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f3594d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final int f3595e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private View f3596f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f3597g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f3598h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private j.a f3599i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private h f3600j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private PopupWindow.OnDismissListener f3601k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final PopupWindow.OnDismissListener f3602l;

    class a implements PopupWindow.OnDismissListener {
        a() {
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            i.this.e();
        }
    }

    public i(Context context, e eVar) {
        this(context, eVar, null, false, AbstractC0487a.f8663H, 0);
    }

    private h a() {
        Display defaultDisplay = ((WindowManager) this.f3591a.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getRealSize(point);
        h bVar = Math.min(point.x, point.y) >= this.f3591a.getResources().getDimensionPixelSize(d.d.f8710c) ? new b(this.f3591a, this.f3596f, this.f3594d, this.f3595e, this.f3593c) : new l(this.f3591a, this.f3592b, this.f3596f, this.f3594d, this.f3595e, this.f3593c);
        bVar.l(this.f3592b);
        bVar.u(this.f3602l);
        bVar.p(this.f3596f);
        bVar.k(this.f3599i);
        bVar.r(this.f3598h);
        bVar.s(this.f3597g);
        return bVar;
    }

    private void l(int i3, int i4, boolean z3, boolean z4) {
        h hVarC = c();
        hVarC.v(z4);
        if (z3) {
            if ((AbstractC0275w.a(this.f3597g, this.f3596f.getLayoutDirection()) & 7) == 5) {
                i3 -= this.f3596f.getWidth();
            }
            hVarC.t(i3);
            hVarC.w(i4);
            int i5 = (int) ((this.f3591a.getResources().getDisplayMetrics().density * 48.0f) / 2.0f);
            hVarC.q(new Rect(i3 - i5, i4 - i5, i3 + i5, i4 + i5));
        }
        hVarC.b();
    }

    public void b() {
        if (d()) {
            this.f3600j.dismiss();
        }
    }

    public h c() {
        if (this.f3600j == null) {
            this.f3600j = a();
        }
        return this.f3600j;
    }

    public boolean d() {
        h hVar = this.f3600j;
        return hVar != null && hVar.a();
    }

    protected void e() {
        this.f3600j = null;
        PopupWindow.OnDismissListener onDismissListener = this.f3601k;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    public void f(View view) {
        this.f3596f = view;
    }

    public void g(boolean z3) {
        this.f3598h = z3;
        h hVar = this.f3600j;
        if (hVar != null) {
            hVar.r(z3);
        }
    }

    public void h(int i3) {
        this.f3597g = i3;
    }

    public void i(PopupWindow.OnDismissListener onDismissListener) {
        this.f3601k = onDismissListener;
    }

    public void j(j.a aVar) {
        this.f3599i = aVar;
        h hVar = this.f3600j;
        if (hVar != null) {
            hVar.k(aVar);
        }
    }

    public void k() {
        if (!m()) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }

    public boolean m() {
        if (d()) {
            return true;
        }
        if (this.f3596f == null) {
            return false;
        }
        l(0, 0, false, false);
        return true;
    }

    public boolean n(int i3, int i4) {
        if (d()) {
            return true;
        }
        if (this.f3596f == null) {
            return false;
        }
        l(i3, i4, true, true);
        return true;
    }

    public i(Context context, e eVar, View view) {
        this(context, eVar, view, false, AbstractC0487a.f8663H, 0);
    }

    public i(Context context, e eVar, View view, boolean z3, int i3) {
        this(context, eVar, view, z3, i3, 0);
    }

    public i(Context context, e eVar, View view, boolean z3, int i3, int i4) {
        this.f3597g = 8388611;
        this.f3602l = new a();
        this.f3591a = context;
        this.f3592b = eVar;
        this.f3596f = view;
        this.f3593c = z3;
        this.f3594d = i3;
        this.f3595e = i4;
    }
}
