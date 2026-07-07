package androidx.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.window.OnBackInvokedDispatcher;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.I;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public class i extends Dialog implements androidx.lifecycle.l, o, G.d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private androidx.lifecycle.m f3022b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final G.c f3023c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final OnBackPressedDispatcher f3024d;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public i(Context context) {
        this(context, 0, 2, null);
        D2.h.f(context, "context");
    }

    private final androidx.lifecycle.m d() {
        androidx.lifecycle.m mVar = this.f3022b;
        if (mVar != null) {
            return mVar;
        }
        androidx.lifecycle.m mVar2 = new androidx.lifecycle.m(this);
        this.f3022b = mVar2;
        return mVar2;
    }

    private final void e() {
        Window window = getWindow();
        D2.h.c(window);
        View decorView = window.getDecorView();
        D2.h.e(decorView, "window!!.decorView");
        I.a(decorView, this);
        Window window2 = getWindow();
        D2.h.c(window2);
        View decorView2 = window2.getDecorView();
        D2.h.e(decorView2, "window!!.decorView");
        r.a(decorView2, this);
        Window window3 = getWindow();
        D2.h.c(window3);
        View decorView3 = window3.getDecorView();
        D2.h.e(decorView3, "window!!.decorView");
        G.e.a(decorView3, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void g(i iVar) {
        D2.h.f(iVar, "this$0");
        super.onBackPressed();
    }

    @Override // androidx.activity.o
    public final OnBackPressedDispatcher a() {
        return this.f3024d;
    }

    @Override // android.app.Dialog
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        D2.h.f(view, "view");
        e();
        super.addContentView(view, layoutParams);
    }

    @Override // G.d
    public androidx.savedstate.a b() {
        return this.f3023c.b();
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        this.f3024d.e();
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 33) {
            OnBackPressedDispatcher onBackPressedDispatcher = this.f3024d;
            OnBackInvokedDispatcher onBackInvokedDispatcher = getOnBackInvokedDispatcher();
            D2.h.e(onBackInvokedDispatcher, "onBackInvokedDispatcher");
            onBackPressedDispatcher.f(onBackInvokedDispatcher);
        }
        this.f3023c.d(bundle);
        d().h(AbstractC0299g.a.ON_CREATE);
    }

    @Override // android.app.Dialog
    public Bundle onSaveInstanceState() {
        Bundle bundleOnSaveInstanceState = super.onSaveInstanceState();
        D2.h.e(bundleOnSaveInstanceState, "super.onSaveInstanceState()");
        this.f3023c.e(bundleOnSaveInstanceState);
        return bundleOnSaveInstanceState;
    }

    @Override // android.app.Dialog
    protected void onStart() {
        super.onStart();
        d().h(AbstractC0299g.a.ON_RESUME);
    }

    @Override // android.app.Dialog
    protected void onStop() {
        d().h(AbstractC0299g.a.ON_DESTROY);
        this.f3022b = null;
        super.onStop();
    }

    @Override // android.app.Dialog
    public void setContentView(int i3) {
        e();
        super.setContentView(i3);
    }

    @Override // androidx.lifecycle.l
    public AbstractC0299g t() {
        return d();
    }

    public /* synthetic */ i(Context context, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i4 & 2) != 0 ? 0 : i3);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public i(Context context, int i3) {
        super(context, i3);
        D2.h.f(context, "context");
        this.f3023c = G.c.f249d.a(this);
        this.f3024d = new OnBackPressedDispatcher(new Runnable() { // from class: androidx.activity.h
            @Override // java.lang.Runnable
            public final void run() {
                i.g(this.f3021b);
            }
        });
    }

    @Override // android.app.Dialog
    public void setContentView(View view) {
        D2.h.f(view, "view");
        e();
        super.setContentView(view);
    }

    @Override // android.app.Dialog
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        D2.h.f(view, "view");
        e();
        super.setContentView(view, layoutParams);
    }
}
