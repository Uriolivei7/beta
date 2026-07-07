package androidx.appcompat.view.menu;

import android.content.DialogInterface;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.b;
import androidx.appcompat.view.menu.j;

/* JADX INFO: loaded from: classes.dex */
class f implements DialogInterface.OnKeyListener, DialogInterface.OnClickListener, DialogInterface.OnDismissListener, j.a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private e f3554b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private androidx.appcompat.app.b f3555c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    c f3556d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private j.a f3557e;

    public f(e eVar) {
        this.f3554b = eVar;
    }

    public void a() {
        androidx.appcompat.app.b bVar = this.f3555c;
        if (bVar != null) {
            bVar.dismiss();
        }
    }

    public void b(IBinder iBinder) {
        e eVar = this.f3554b;
        b.a aVar = new b.a(eVar.u());
        c cVar = new c(aVar.b(), d.g.f8819j);
        this.f3556d = cVar;
        cVar.k(this);
        this.f3554b.b(this.f3556d);
        aVar.c(this.f3556d.a(), this);
        View viewY = eVar.y();
        if (viewY != null) {
            aVar.d(viewY);
        } else {
            aVar.e(eVar.w()).m(eVar.x());
        }
        aVar.j(this);
        androidx.appcompat.app.b bVarA = aVar.a();
        this.f3555c = bVarA;
        bVarA.setOnDismissListener(this);
        WindowManager.LayoutParams attributes = this.f3555c.getWindow().getAttributes();
        attributes.type = 1003;
        if (iBinder != null) {
            attributes.token = iBinder;
        }
        attributes.flags |= 131072;
        this.f3555c.show();
    }

    @Override // androidx.appcompat.view.menu.j.a
    public void c(e eVar, boolean z3) {
        if (z3 || eVar == this.f3554b) {
            a();
        }
        j.a aVar = this.f3557e;
        if (aVar != null) {
            aVar.c(eVar, z3);
        }
    }

    @Override // androidx.appcompat.view.menu.j.a
    public boolean d(e eVar) {
        j.a aVar = this.f3557e;
        if (aVar != null) {
            return aVar.d(eVar);
        }
        return false;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i3) {
        this.f3554b.M((g) this.f3556d.a().getItem(i3), 0);
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        this.f3556d.c(this.f3554b, true);
    }

    @Override // android.content.DialogInterface.OnKeyListener
    public boolean onKey(DialogInterface dialogInterface, int i3, KeyEvent keyEvent) {
        Window window;
        View decorView;
        KeyEvent.DispatcherState keyDispatcherState;
        View decorView2;
        KeyEvent.DispatcherState keyDispatcherState2;
        if (i3 == 82 || i3 == 4) {
            if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                Window window2 = this.f3555c.getWindow();
                if (window2 != null && (decorView2 = window2.getDecorView()) != null && (keyDispatcherState2 = decorView2.getKeyDispatcherState()) != null) {
                    keyDispatcherState2.startTracking(keyEvent, this);
                    return true;
                }
            } else if (keyEvent.getAction() == 1 && !keyEvent.isCanceled() && (window = this.f3555c.getWindow()) != null && (decorView = window.getDecorView()) != null && (keyDispatcherState = decorView.getKeyDispatcherState()) != null && keyDispatcherState.isTracking(keyEvent)) {
                this.f3554b.e(true);
                dialogInterface.dismiss();
                return true;
            }
        }
        return this.f3554b.performShortcut(i3, keyEvent, 0);
    }
}
