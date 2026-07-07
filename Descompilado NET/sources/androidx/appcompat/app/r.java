package androidx.appcompat.app;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.view.b;
import androidx.core.view.AbstractC0276x;
import androidx.lifecycle.I;
import d.AbstractC0487a;

/* JADX INFO: loaded from: classes.dex */
public class r extends androidx.activity.i implements d {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private f f3285e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final AbstractC0276x.a f3286f;

    public r(Context context) {
        this(context, 0);
    }

    private void e() {
        I.a(getWindow().getDecorView(), this);
        G.e.a(getWindow().getDecorView(), this);
        androidx.activity.r.a(getWindow().getDecorView(), this);
    }

    private static int j(Context context, int i3) {
        if (i3 != 0) {
            return i3;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(AbstractC0487a.f8656A, typedValue, true);
        return typedValue.resourceId;
    }

    @Override // androidx.activity.i, android.app.Dialog
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        i().e(view, layoutParams);
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        i().A();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return AbstractC0276x.e(this.f3286f, getWindow().getDecorView(), this, keyEvent);
    }

    @Override // androidx.appcompat.app.d
    public void f(androidx.appcompat.view.b bVar) {
    }

    @Override // android.app.Dialog
    public View findViewById(int i3) {
        return i().l(i3);
    }

    @Override // androidx.appcompat.app.d
    public void h(androidx.appcompat.view.b bVar) {
    }

    public f i() {
        if (this.f3285e == null) {
            this.f3285e = f.k(this, this);
        }
        return this.f3285e;
    }

    @Override // android.app.Dialog
    public void invalidateOptionsMenu() {
        i().v();
    }

    boolean k(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean l(int i3) {
        return i().I(i3);
    }

    @Override // androidx.activity.i, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        i().u();
        super.onCreate(bundle);
        i().z(bundle);
    }

    @Override // androidx.activity.i, android.app.Dialog
    protected void onStop() {
        super.onStop();
        i().F();
    }

    @Override // androidx.activity.i, android.app.Dialog
    public void setContentView(int i3) {
        e();
        i().J(i3);
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        i().P(charSequence);
    }

    @Override // androidx.appcompat.app.d
    public androidx.appcompat.view.b w(b.a aVar) {
        return null;
    }

    public r(Context context, int i3) {
        super(context, j(context, i3));
        this.f3286f = new AbstractC0276x.a() { // from class: androidx.appcompat.app.q
            @Override // androidx.core.view.AbstractC0276x.a
            public final boolean e(KeyEvent keyEvent) {
                return this.f3284b.k(keyEvent);
            }
        };
        f fVarI = i();
        fVarI.O(j(context, i3));
        fVarI.z(null);
    }

    @Override // androidx.activity.i, android.app.Dialog
    public void setContentView(View view) {
        e();
        i().K(view);
    }

    @Override // android.app.Dialog
    public void setTitle(int i3) {
        super.setTitle(i3);
        i().P(getContext().getString(i3));
    }

    @Override // androidx.activity.i, android.app.Dialog
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        e();
        i().L(view, layoutParams);
    }
}
