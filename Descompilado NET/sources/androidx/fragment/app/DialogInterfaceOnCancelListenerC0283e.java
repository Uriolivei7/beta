package androidx.fragment.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/* JADX INFO: renamed from: androidx.fragment.app.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class DialogInterfaceOnCancelListenerC0283e extends Fragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    /* JADX INFO: renamed from: d0, reason: collision with root package name */
    private Handler f5122d0;

    /* JADX INFO: renamed from: e0, reason: collision with root package name */
    private Runnable f5123e0;

    /* JADX INFO: renamed from: f0, reason: collision with root package name */
    private DialogInterface.OnCancelListener f5124f0;

    /* JADX INFO: renamed from: g0, reason: collision with root package name */
    private DialogInterface.OnDismissListener f5125g0;

    /* JADX INFO: renamed from: h0, reason: collision with root package name */
    private int f5126h0;

    /* JADX INFO: renamed from: i0, reason: collision with root package name */
    private int f5127i0;

    /* JADX INFO: renamed from: j0, reason: collision with root package name */
    private boolean f5128j0;

    /* JADX INFO: renamed from: k0, reason: collision with root package name */
    private boolean f5129k0;

    /* JADX INFO: renamed from: l0, reason: collision with root package name */
    private int f5130l0;

    /* JADX INFO: renamed from: m0, reason: collision with root package name */
    private boolean f5131m0;

    /* JADX INFO: renamed from: n0, reason: collision with root package name */
    private androidx.lifecycle.q f5132n0;

    /* JADX INFO: renamed from: o0, reason: collision with root package name */
    private Dialog f5133o0;

    /* JADX INFO: renamed from: p0, reason: collision with root package name */
    private boolean f5134p0;

    /* JADX INFO: renamed from: q0, reason: collision with root package name */
    private boolean f5135q0;

    /* JADX INFO: renamed from: r0, reason: collision with root package name */
    private boolean f5136r0;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    private boolean f5137s0;

    /* JADX INFO: renamed from: androidx.fragment.app.e$a */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            DialogInterfaceOnCancelListenerC0283e.this.f5125g0.onDismiss(DialogInterfaceOnCancelListenerC0283e.this.f5133o0);
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.e$b */
    class b implements DialogInterface.OnCancelListener {
        b() {
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            if (DialogInterfaceOnCancelListenerC0283e.this.f5133o0 != null) {
                DialogInterfaceOnCancelListenerC0283e dialogInterfaceOnCancelListenerC0283e = DialogInterfaceOnCancelListenerC0283e.this;
                dialogInterfaceOnCancelListenerC0283e.onCancel(dialogInterfaceOnCancelListenerC0283e.f5133o0);
            }
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.e$c */
    class c implements DialogInterface.OnDismissListener {
        c() {
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            if (DialogInterfaceOnCancelListenerC0283e.this.f5133o0 != null) {
                DialogInterfaceOnCancelListenerC0283e dialogInterfaceOnCancelListenerC0283e = DialogInterfaceOnCancelListenerC0283e.this;
                dialogInterfaceOnCancelListenerC0283e.onDismiss(dialogInterfaceOnCancelListenerC0283e.f5133o0);
            }
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.e$d */
    class d implements androidx.lifecycle.q {
        d() {
        }

        @Override // androidx.lifecycle.q
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(androidx.lifecycle.l lVar) {
            if (lVar == null || !DialogInterfaceOnCancelListenerC0283e.this.f5129k0) {
                return;
            }
            View viewN1 = DialogInterfaceOnCancelListenerC0283e.this.n1();
            if (viewN1.getParent() != null) {
                throw new IllegalStateException("DialogFragment can not be attached to a container view");
            }
            if (DialogInterfaceOnCancelListenerC0283e.this.f5133o0 != null) {
                if (x.G0(3)) {
                    Log.d("FragmentManager", "DialogFragment " + this + " setting the content view on " + DialogInterfaceOnCancelListenerC0283e.this.f5133o0);
                }
                DialogInterfaceOnCancelListenerC0283e.this.f5133o0.setContentView(viewN1);
            }
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.e$e, reason: collision with other inner class name */
    class C0074e extends AbstractC0290l {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ AbstractC0290l f5142b;

        C0074e(AbstractC0290l abstractC0290l) {
            this.f5142b = abstractC0290l;
        }

        @Override // androidx.fragment.app.AbstractC0290l
        public View f(int i3) {
            return this.f5142b.h() ? this.f5142b.f(i3) : DialogInterfaceOnCancelListenerC0283e.this.G1(i3);
        }

        @Override // androidx.fragment.app.AbstractC0290l
        public boolean h() {
            return this.f5142b.h() || DialogInterfaceOnCancelListenerC0283e.this.H1();
        }
    }

    public DialogInterfaceOnCancelListenerC0283e() {
        this.f5123e0 = new a();
        this.f5124f0 = new b();
        this.f5125g0 = new c();
        this.f5126h0 = 0;
        this.f5127i0 = 0;
        this.f5128j0 = true;
        this.f5129k0 = true;
        this.f5130l0 = -1;
        this.f5132n0 = new d();
        this.f5137s0 = false;
    }

    private void D1(boolean z3, boolean z4, boolean z5) {
        if (this.f5135q0) {
            return;
        }
        this.f5135q0 = true;
        this.f5136r0 = false;
        Dialog dialog = this.f5133o0;
        if (dialog != null) {
            dialog.setOnDismissListener(null);
            this.f5133o0.dismiss();
            if (!z4) {
                if (Looper.myLooper() == this.f5122d0.getLooper()) {
                    onDismiss(this.f5133o0);
                } else {
                    this.f5122d0.post(this.f5123e0);
                }
            }
        }
        this.f5134p0 = true;
        if (this.f5130l0 >= 0) {
            if (z5) {
                E().Z0(this.f5130l0, 1);
            } else {
                E().X0(this.f5130l0, 1, z3);
            }
            this.f5130l0 = -1;
            return;
        }
        F fO = E().o();
        fO.m(true);
        fO.l(this);
        if (z5) {
            fO.h();
        } else if (z3) {
            fO.g();
        } else {
            fO.f();
        }
    }

    private void I1(Bundle bundle) {
        if (this.f5129k0 && !this.f5137s0) {
            try {
                this.f5131m0 = true;
                Dialog dialogF1 = F1(bundle);
                this.f5133o0 = dialogF1;
                if (this.f5129k0) {
                    K1(dialogF1, this.f5126h0);
                    Context contextP = p();
                    if (contextP instanceof Activity) {
                        this.f5133o0.setOwnerActivity((Activity) contextP);
                    }
                    this.f5133o0.setCancelable(this.f5128j0);
                    this.f5133o0.setOnCancelListener(this.f5124f0);
                    this.f5133o0.setOnDismissListener(this.f5125g0);
                    this.f5137s0 = true;
                } else {
                    this.f5133o0 = null;
                }
                this.f5131m0 = false;
            } catch (Throwable th) {
                this.f5131m0 = false;
                throw th;
            }
        }
    }

    public void C1() {
        D1(false, false, false);
    }

    public int E1() {
        return this.f5127i0;
    }

    public Dialog F1(Bundle bundle) {
        if (x.G0(3)) {
            Log.d("FragmentManager", "onCreateDialog called for DialogFragment " + this);
        }
        return new androidx.activity.i(m1(), E1());
    }

    @Override // androidx.fragment.app.Fragment
    public void G0(Bundle bundle) {
        super.G0(bundle);
        Dialog dialog = this.f5133o0;
        if (dialog != null) {
            Bundle bundleOnSaveInstanceState = dialog.onSaveInstanceState();
            bundleOnSaveInstanceState.putBoolean("android:dialogShowing", false);
            bundle.putBundle("android:savedDialogState", bundleOnSaveInstanceState);
        }
        int i3 = this.f5126h0;
        if (i3 != 0) {
            bundle.putInt("android:style", i3);
        }
        int i4 = this.f5127i0;
        if (i4 != 0) {
            bundle.putInt("android:theme", i4);
        }
        boolean z3 = this.f5128j0;
        if (!z3) {
            bundle.putBoolean("android:cancelable", z3);
        }
        boolean z4 = this.f5129k0;
        if (!z4) {
            bundle.putBoolean("android:showsDialog", z4);
        }
        int i5 = this.f5130l0;
        if (i5 != -1) {
            bundle.putInt("android:backStackId", i5);
        }
    }

    View G1(int i3) {
        Dialog dialog = this.f5133o0;
        if (dialog != null) {
            return dialog.findViewById(i3);
        }
        return null;
    }

    @Override // androidx.fragment.app.Fragment
    public void H0() {
        super.H0();
        Dialog dialog = this.f5133o0;
        if (dialog != null) {
            this.f5134p0 = false;
            dialog.show();
            View decorView = this.f5133o0.getWindow().getDecorView();
            androidx.lifecycle.I.a(decorView, this);
            androidx.lifecycle.J.a(decorView, this);
            G.e.a(decorView, this);
        }
    }

    boolean H1() {
        return this.f5137s0;
    }

    @Override // androidx.fragment.app.Fragment
    public void I0() {
        super.I0();
        Dialog dialog = this.f5133o0;
        if (dialog != null) {
            dialog.hide();
        }
    }

    public void J1(boolean z3) {
        this.f5128j0 = z3;
        Dialog dialog = this.f5133o0;
        if (dialog != null) {
            dialog.setCancelable(z3);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void K0(Bundle bundle) {
        Bundle bundle2;
        super.K0(bundle);
        if (this.f5133o0 == null || bundle == null || (bundle2 = bundle.getBundle("android:savedDialogState")) == null) {
            return;
        }
        this.f5133o0.onRestoreInstanceState(bundle2);
    }

    public void K1(Dialog dialog, int i3) {
        if (i3 != 1 && i3 != 2) {
            if (i3 != 3) {
                return;
            }
            Window window = dialog.getWindow();
            if (window != null) {
                window.addFlags(24);
            }
        }
        dialog.requestWindowFeature(1);
    }

    public void L1(x xVar, String str) {
        this.f5135q0 = false;
        this.f5136r0 = true;
        F fO = xVar.o();
        fO.m(true);
        fO.d(this, str);
        fO.f();
    }

    @Override // androidx.fragment.app.Fragment
    void R0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Bundle bundle2;
        super.R0(layoutInflater, viewGroup, bundle);
        if (this.f4920J != null || this.f5133o0 == null || bundle == null || (bundle2 = bundle.getBundle("android:savedDialogState")) == null) {
            return;
        }
        this.f5133o0.onRestoreInstanceState(bundle2);
    }

    @Override // androidx.fragment.app.Fragment
    AbstractC0290l d() {
        return new C0074e(super.d());
    }

    @Override // androidx.fragment.app.Fragment
    public void e0(Bundle bundle) {
        super.e0(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void h0(Context context) {
        super.h0(context);
        S().e(this.f5132n0);
        if (this.f5136r0) {
            return;
        }
        this.f5135q0 = false;
    }

    @Override // androidx.fragment.app.Fragment
    public void k0(Bundle bundle) {
        super.k0(bundle);
        this.f5122d0 = new Handler();
        this.f5129k0 = this.f4963z == 0;
        if (bundle != null) {
            this.f5126h0 = bundle.getInt("android:style", 0);
            this.f5127i0 = bundle.getInt("android:theme", 0);
            this.f5128j0 = bundle.getBoolean("android:cancelable", true);
            this.f5129k0 = bundle.getBoolean("android:showsDialog", this.f5129k0);
            this.f5130l0 = bundle.getInt("android:backStackId", -1);
        }
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        if (this.f5134p0) {
            return;
        }
        if (x.G0(3)) {
            Log.d("FragmentManager", "onDismiss called for DialogFragment " + this);
        }
        D1(true, true, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void r0() {
        super.r0();
        Dialog dialog = this.f5133o0;
        if (dialog != null) {
            this.f5134p0 = true;
            dialog.setOnDismissListener(null);
            this.f5133o0.dismiss();
            if (!this.f5135q0) {
                onDismiss(this.f5133o0);
            }
            this.f5133o0 = null;
            this.f5137s0 = false;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void s0() {
        super.s0();
        if (!this.f5136r0 && !this.f5135q0) {
            this.f5135q0 = true;
        }
        S().h(this.f5132n0);
    }

    @Override // androidx.fragment.app.Fragment
    public LayoutInflater t0(Bundle bundle) {
        LayoutInflater layoutInflaterT0 = super.t0(bundle);
        if (this.f5129k0 && !this.f5131m0) {
            I1(bundle);
            if (x.G0(2)) {
                Log.d("FragmentManager", "get layout inflater for DialogFragment " + this + " from dialog context");
            }
            Dialog dialog = this.f5133o0;
            return dialog != null ? layoutInflaterT0.cloneInContext(dialog.getContext()) : layoutInflaterT0;
        }
        if (x.G0(2)) {
            String str = "getting layout inflater for DialogFragment " + this;
            if (this.f5129k0) {
                Log.d("FragmentManager", "mCreatingDialog = true: " + str);
            } else {
                Log.d("FragmentManager", "mShowsDialog = false: " + str);
            }
        }
        return layoutInflaterT0;
    }

    public DialogInterfaceOnCancelListenerC0283e(int i3) {
        super(i3);
        this.f5123e0 = new a();
        this.f5124f0 = new b();
        this.f5125g0 = new c();
        this.f5126h0 = 0;
        this.f5127i0 = 0;
        this.f5128j0 = true;
        this.f5129k0 = true;
        this.f5130l0 = -1;
        this.f5132n0 = new d();
        this.f5137s0 = false;
    }
}
