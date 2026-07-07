package androidx.fragment.app;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.view.Z;
import androidx.fragment.app.L;
import androidx.lifecycle.AbstractC0299g;
import org.chromium.support_lib_boundary.WebSettingsBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
class D {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final r f4870a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final E f4871b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Fragment f4872c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f4873d = false;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f4874e = -1;

    class a implements View.OnAttachStateChangeListener {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f4875b;

        a(View view) {
            this.f4875b = view;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            this.f4875b.removeOnAttachStateChangeListener(this);
            Z.U(this.f4875b);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
        }
    }

    static /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f4877a;

        static {
            int[] iArr = new int[AbstractC0299g.b.values().length];
            f4877a = iArr;
            try {
                iArr[AbstractC0299g.b.RESUMED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f4877a[AbstractC0299g.b.STARTED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f4877a[AbstractC0299g.b.CREATED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f4877a[AbstractC0299g.b.INITIALIZED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    D(r rVar, E e4, Fragment fragment) {
        this.f4870a = rVar;
        this.f4871b = e4;
        this.f4872c = fragment;
    }

    private boolean l(View view) {
        if (view == this.f4872c.f4920J) {
            return true;
        }
        for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent()) {
            if (parent == this.f4872c.f4920J) {
                return true;
            }
        }
        return false;
    }

    private Bundle q() {
        Bundle bundle = new Bundle();
        this.f4872c.f1(bundle);
        this.f4870a.j(this.f4872c, bundle, false);
        if (bundle.isEmpty()) {
            bundle = null;
        }
        if (this.f4872c.f4920J != null) {
            s();
        }
        if (this.f4872c.f4941d != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray("android:view_state", this.f4872c.f4941d);
        }
        if (this.f4872c.f4942e != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBundle("android:view_registry_state", this.f4872c.f4942e);
        }
        if (!this.f4872c.f4922L) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean("android:user_visible_hint", this.f4872c.f4922L);
        }
        return bundle;
    }

    void a() {
        if (x.G0(3)) {
            Log.d("FragmentManager", "moveto ACTIVITY_CREATED: " + this.f4872c);
        }
        Fragment fragment = this.f4872c;
        fragment.L0(fragment.f4940c);
        r rVar = this.f4870a;
        Fragment fragment2 = this.f4872c;
        rVar.a(fragment2, fragment2.f4940c, false);
    }

    void b() {
        int iJ = this.f4871b.j(this.f4872c);
        Fragment fragment = this.f4872c;
        fragment.f4919I.addView(fragment.f4920J, iJ);
    }

    void c() {
        if (x.G0(3)) {
            Log.d("FragmentManager", "moveto ATTACHED: " + this.f4872c);
        }
        Fragment fragment = this.f4872c;
        Fragment fragment2 = fragment.f4946i;
        D dN = null;
        if (fragment2 != null) {
            D dN2 = this.f4871b.n(fragment2.f4944g);
            if (dN2 == null) {
                throw new IllegalStateException("Fragment " + this.f4872c + " declared target fragment " + this.f4872c.f4946i + " that does not belong to this FragmentManager!");
            }
            Fragment fragment3 = this.f4872c;
            fragment3.f4947j = fragment3.f4946i.f4944g;
            fragment3.f4946i = null;
            dN = dN2;
        } else {
            String str = fragment.f4947j;
            if (str != null && (dN = this.f4871b.n(str)) == null) {
                throw new IllegalStateException("Fragment " + this.f4872c + " declared target fragment " + this.f4872c.f4947j + " that does not belong to this FragmentManager!");
            }
        }
        if (dN != null) {
            dN.m();
        }
        Fragment fragment4 = this.f4872c;
        fragment4.f4959v = fragment4.f4958u.t0();
        Fragment fragment5 = this.f4872c;
        fragment5.f4961x = fragment5.f4958u.w0();
        this.f4870a.g(this.f4872c, false);
        this.f4872c.M0();
        this.f4870a.b(this.f4872c, false);
    }

    int d() {
        Fragment fragment = this.f4872c;
        if (fragment.f4958u == null) {
            return fragment.f4938b;
        }
        int iMin = this.f4874e;
        int i3 = b.f4877a[fragment.f4929S.ordinal()];
        if (i3 != 1) {
            iMin = i3 != 2 ? i3 != 3 ? i3 != 4 ? Math.min(iMin, -1) : Math.min(iMin, 0) : Math.min(iMin, 1) : Math.min(iMin, 5);
        }
        Fragment fragment2 = this.f4872c;
        if (fragment2.f4953p) {
            if (fragment2.f4954q) {
                iMin = Math.max(this.f4874e, 2);
                View view = this.f4872c.f4920J;
                if (view != null && view.getParent() == null) {
                    iMin = Math.min(iMin, 2);
                }
            } else {
                iMin = this.f4874e < 4 ? Math.min(iMin, fragment2.f4938b) : Math.min(iMin, 1);
            }
        }
        if (!this.f4872c.f4950m) {
            iMin = Math.min(iMin, 1);
        }
        Fragment fragment3 = this.f4872c;
        ViewGroup viewGroup = fragment3.f4919I;
        L.e.b bVarL = viewGroup != null ? L.n(viewGroup, fragment3.E()).l(this) : null;
        if (bVarL == L.e.b.ADDING) {
            iMin = Math.min(iMin, 6);
        } else if (bVarL == L.e.b.REMOVING) {
            iMin = Math.max(iMin, 3);
        } else {
            Fragment fragment4 = this.f4872c;
            if (fragment4.f4951n) {
                iMin = fragment4.Y() ? Math.min(iMin, 1) : Math.min(iMin, -1);
            }
        }
        Fragment fragment5 = this.f4872c;
        if (fragment5.f4921K && fragment5.f4938b < 5) {
            iMin = Math.min(iMin, 4);
        }
        if (x.G0(2)) {
            Log.v("FragmentManager", "computeExpectedState() of " + iMin + " for " + this.f4872c);
        }
        return iMin;
    }

    void e() {
        if (x.G0(3)) {
            Log.d("FragmentManager", "moveto CREATED: " + this.f4872c);
        }
        Fragment fragment = this.f4872c;
        if (fragment.f4927Q) {
            fragment.o1(fragment.f4940c);
            this.f4872c.f4938b = 1;
            return;
        }
        this.f4870a.h(fragment, fragment.f4940c, false);
        Fragment fragment2 = this.f4872c;
        fragment2.P0(fragment2.f4940c);
        r rVar = this.f4870a;
        Fragment fragment3 = this.f4872c;
        rVar.c(fragment3, fragment3.f4940c, false);
    }

    void f() {
        String resourceName;
        if (this.f4872c.f4953p) {
            return;
        }
        if (x.G0(3)) {
            Log.d("FragmentManager", "moveto CREATE_VIEW: " + this.f4872c);
        }
        Fragment fragment = this.f4872c;
        LayoutInflater layoutInflaterV0 = fragment.V0(fragment.f4940c);
        Fragment fragment2 = this.f4872c;
        ViewGroup viewGroup = fragment2.f4919I;
        if (viewGroup == null) {
            int i3 = fragment2.f4963z;
            if (i3 == 0) {
                viewGroup = null;
            } else {
                if (i3 == -1) {
                    throw new IllegalArgumentException("Cannot create fragment " + this.f4872c + " for a container view with no id");
                }
                viewGroup = (ViewGroup) fragment2.f4958u.p0().f(this.f4872c.f4963z);
                if (viewGroup == null) {
                    Fragment fragment3 = this.f4872c;
                    if (!fragment3.f4955r) {
                        try {
                            resourceName = fragment3.K().getResourceName(this.f4872c.f4963z);
                        } catch (Resources.NotFoundException unused) {
                            resourceName = "unknown";
                        }
                        throw new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(this.f4872c.f4963z) + " (" + resourceName + ") for fragment " + this.f4872c);
                    }
                } else if (!(viewGroup instanceof C0291m)) {
                    C.c.i(this.f4872c, viewGroup);
                }
            }
        }
        Fragment fragment4 = this.f4872c;
        fragment4.f4919I = viewGroup;
        fragment4.R0(layoutInflaterV0, viewGroup, fragment4.f4940c);
        View view = this.f4872c.f4920J;
        if (view != null) {
            view.setSaveFromParentEnabled(false);
            Fragment fragment5 = this.f4872c;
            fragment5.f4920J.setTag(B.b.f42a, fragment5);
            if (viewGroup != null) {
                b();
            }
            Fragment fragment6 = this.f4872c;
            if (fragment6.f4912B) {
                fragment6.f4920J.setVisibility(8);
            }
            if (Z.E(this.f4872c.f4920J)) {
                Z.U(this.f4872c.f4920J);
            } else {
                View view2 = this.f4872c.f4920J;
                view2.addOnAttachStateChangeListener(new a(view2));
            }
            this.f4872c.i1();
            r rVar = this.f4870a;
            Fragment fragment7 = this.f4872c;
            rVar.m(fragment7, fragment7.f4920J, fragment7.f4940c, false);
            int visibility = this.f4872c.f4920J.getVisibility();
            this.f4872c.w1(this.f4872c.f4920J.getAlpha());
            Fragment fragment8 = this.f4872c;
            if (fragment8.f4919I != null && visibility == 0) {
                View viewFindFocus = fragment8.f4920J.findFocus();
                if (viewFindFocus != null) {
                    this.f4872c.t1(viewFindFocus);
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "requestFocus: Saved focused view " + viewFindFocus + " for Fragment " + this.f4872c);
                    }
                }
                this.f4872c.f4920J.setAlpha(0.0f);
            }
        }
        this.f4872c.f4938b = 2;
    }

    void g() {
        Fragment fragmentF;
        if (x.G0(3)) {
            Log.d("FragmentManager", "movefrom CREATED: " + this.f4872c);
        }
        Fragment fragment = this.f4872c;
        boolean zIsChangingConfigurations = true;
        boolean z3 = fragment.f4951n && !fragment.Y();
        if (z3) {
            Fragment fragment2 = this.f4872c;
            if (!fragment2.f4952o) {
                this.f4871b.B(fragment2.f4944g, null);
            }
        }
        if (!z3 && !this.f4871b.p().r(this.f4872c)) {
            String str = this.f4872c.f4947j;
            if (str != null && (fragmentF = this.f4871b.f(str)) != null && fragmentF.f4914D) {
                this.f4872c.f4946i = fragmentF;
            }
            this.f4872c.f4938b = 0;
            return;
        }
        p pVar = this.f4872c.f4959v;
        if (pVar instanceof androidx.lifecycle.H) {
            zIsChangingConfigurations = this.f4871b.p().o();
        } else if (pVar.k() instanceof Activity) {
            zIsChangingConfigurations = true ^ ((Activity) pVar.k()).isChangingConfigurations();
        }
        if ((z3 && !this.f4872c.f4952o) || zIsChangingConfigurations) {
            this.f4871b.p().g(this.f4872c);
        }
        this.f4872c.S0();
        this.f4870a.d(this.f4872c, false);
        for (D d4 : this.f4871b.k()) {
            if (d4 != null) {
                Fragment fragmentK = d4.k();
                if (this.f4872c.f4944g.equals(fragmentK.f4947j)) {
                    fragmentK.f4946i = this.f4872c;
                    fragmentK.f4947j = null;
                }
            }
        }
        Fragment fragment3 = this.f4872c;
        String str2 = fragment3.f4947j;
        if (str2 != null) {
            fragment3.f4946i = this.f4871b.f(str2);
        }
        this.f4871b.s(this);
    }

    void h() {
        View view;
        if (x.G0(3)) {
            Log.d("FragmentManager", "movefrom CREATE_VIEW: " + this.f4872c);
        }
        Fragment fragment = this.f4872c;
        ViewGroup viewGroup = fragment.f4919I;
        if (viewGroup != null && (view = fragment.f4920J) != null) {
            viewGroup.removeView(view);
        }
        this.f4872c.T0();
        this.f4870a.n(this.f4872c, false);
        Fragment fragment2 = this.f4872c;
        fragment2.f4919I = null;
        fragment2.f4920J = null;
        fragment2.f4931U = null;
        fragment2.f4932V.i(null);
        this.f4872c.f4954q = false;
    }

    void i() {
        if (x.G0(3)) {
            Log.d("FragmentManager", "movefrom ATTACHED: " + this.f4872c);
        }
        this.f4872c.U0();
        this.f4870a.e(this.f4872c, false);
        Fragment fragment = this.f4872c;
        fragment.f4938b = -1;
        fragment.f4959v = null;
        fragment.f4961x = null;
        fragment.f4958u = null;
        if ((!fragment.f4951n || fragment.Y()) && !this.f4871b.p().r(this.f4872c)) {
            return;
        }
        if (x.G0(3)) {
            Log.d("FragmentManager", "initState called for fragment: " + this.f4872c);
        }
        this.f4872c.U();
    }

    void j() {
        Fragment fragment = this.f4872c;
        if (fragment.f4953p && fragment.f4954q && !fragment.f4956s) {
            if (x.G0(3)) {
                Log.d("FragmentManager", "moveto CREATE_VIEW: " + this.f4872c);
            }
            Fragment fragment2 = this.f4872c;
            fragment2.R0(fragment2.V0(fragment2.f4940c), null, this.f4872c.f4940c);
            View view = this.f4872c.f4920J;
            if (view != null) {
                view.setSaveFromParentEnabled(false);
                Fragment fragment3 = this.f4872c;
                fragment3.f4920J.setTag(B.b.f42a, fragment3);
                Fragment fragment4 = this.f4872c;
                if (fragment4.f4912B) {
                    fragment4.f4920J.setVisibility(8);
                }
                this.f4872c.i1();
                r rVar = this.f4870a;
                Fragment fragment5 = this.f4872c;
                rVar.m(fragment5, fragment5.f4920J, fragment5.f4940c, false);
                this.f4872c.f4938b = 2;
            }
        }
    }

    Fragment k() {
        return this.f4872c;
    }

    void m() {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        ViewGroup viewGroup3;
        if (this.f4873d) {
            if (x.G0(2)) {
                Log.v("FragmentManager", "Ignoring re-entrant call to moveToExpectedState() for " + k());
                return;
            }
            return;
        }
        try {
            this.f4873d = true;
            boolean z3 = false;
            while (true) {
                int iD = d();
                Fragment fragment = this.f4872c;
                int i3 = fragment.f4938b;
                if (iD == i3) {
                    if (!z3 && i3 == -1 && fragment.f4951n && !fragment.Y() && !this.f4872c.f4952o) {
                        if (x.G0(3)) {
                            Log.d("FragmentManager", "Cleaning up state of never attached fragment: " + this.f4872c);
                        }
                        this.f4871b.p().g(this.f4872c);
                        this.f4871b.s(this);
                        if (x.G0(3)) {
                            Log.d("FragmentManager", "initState called for fragment: " + this.f4872c);
                        }
                        this.f4872c.U();
                    }
                    Fragment fragment2 = this.f4872c;
                    if (fragment2.f4925O) {
                        if (fragment2.f4920J != null && (viewGroup = fragment2.f4919I) != null) {
                            L lN = L.n(viewGroup, fragment2.E());
                            if (this.f4872c.f4912B) {
                                lN.c(this);
                            } else {
                                lN.e(this);
                            }
                        }
                        Fragment fragment3 = this.f4872c;
                        x xVar = fragment3.f4958u;
                        if (xVar != null) {
                            xVar.E0(fragment3);
                        }
                        Fragment fragment4 = this.f4872c;
                        fragment4.f4925O = false;
                        fragment4.u0(fragment4.f4912B);
                        this.f4872c.f4960w.I();
                    }
                    this.f4873d = false;
                    return;
                }
                if (iD <= i3) {
                    switch (i3 - 1) {
                        case -1:
                            i();
                            break;
                        case WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY /* 0 */:
                            if (fragment.f4952o && this.f4871b.q(fragment.f4944g) == null) {
                                r();
                            }
                            g();
                            break;
                        case 1:
                            h();
                            this.f4872c.f4938b = 1;
                            break;
                        case 2:
                            fragment.f4954q = false;
                            fragment.f4938b = 2;
                            break;
                        case 3:
                            if (x.G0(3)) {
                                Log.d("FragmentManager", "movefrom ACTIVITY_CREATED: " + this.f4872c);
                            }
                            Fragment fragment5 = this.f4872c;
                            if (fragment5.f4952o) {
                                r();
                            } else if (fragment5.f4920J != null && fragment5.f4941d == null) {
                                s();
                            }
                            Fragment fragment6 = this.f4872c;
                            if (fragment6.f4920J != null && (viewGroup2 = fragment6.f4919I) != null) {
                                L.n(viewGroup2, fragment6.E()).d(this);
                            }
                            this.f4872c.f4938b = 3;
                            break;
                        case 4:
                            v();
                            break;
                        case 5:
                            fragment.f4938b = 5;
                            break;
                        case 6:
                            n();
                            break;
                    }
                } else {
                    switch (i3 + 1) {
                        case WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY /* 0 */:
                            c();
                            break;
                        case 1:
                            e();
                            break;
                        case 2:
                            j();
                            f();
                            break;
                        case 3:
                            a();
                            break;
                        case 4:
                            if (fragment.f4920J != null && (viewGroup3 = fragment.f4919I) != null) {
                                L.n(viewGroup3, fragment.E()).b(L.e.c.b(this.f4872c.f4920J.getVisibility()), this);
                            }
                            this.f4872c.f4938b = 4;
                            break;
                        case 5:
                            u();
                            break;
                        case 6:
                            fragment.f4938b = 6;
                            break;
                        case 7:
                            p();
                            break;
                    }
                }
                z3 = true;
            }
        } catch (Throwable th) {
            this.f4873d = false;
            throw th;
        }
    }

    void n() {
        if (x.G0(3)) {
            Log.d("FragmentManager", "movefrom RESUMED: " + this.f4872c);
        }
        this.f4872c.a1();
        this.f4870a.f(this.f4872c, false);
    }

    void o(ClassLoader classLoader) {
        Bundle bundle = this.f4872c.f4940c;
        if (bundle == null) {
            return;
        }
        bundle.setClassLoader(classLoader);
        Fragment fragment = this.f4872c;
        fragment.f4941d = fragment.f4940c.getSparseParcelableArray("android:view_state");
        Fragment fragment2 = this.f4872c;
        fragment2.f4942e = fragment2.f4940c.getBundle("android:view_registry_state");
        Fragment fragment3 = this.f4872c;
        fragment3.f4947j = fragment3.f4940c.getString("android:target_state");
        Fragment fragment4 = this.f4872c;
        if (fragment4.f4947j != null) {
            fragment4.f4948k = fragment4.f4940c.getInt("android:target_req_state", 0);
        }
        Fragment fragment5 = this.f4872c;
        Boolean bool = fragment5.f4943f;
        if (bool != null) {
            fragment5.f4922L = bool.booleanValue();
            this.f4872c.f4943f = null;
        } else {
            fragment5.f4922L = fragment5.f4940c.getBoolean("android:user_visible_hint", true);
        }
        Fragment fragment6 = this.f4872c;
        if (fragment6.f4922L) {
            return;
        }
        fragment6.f4921K = true;
    }

    void p() {
        if (x.G0(3)) {
            Log.d("FragmentManager", "moveto RESUMED: " + this.f4872c);
        }
        View viewY = this.f4872c.y();
        if (viewY != null && l(viewY)) {
            boolean zRequestFocus = viewY.requestFocus();
            if (x.G0(2)) {
                StringBuilder sb = new StringBuilder();
                sb.append("requestFocus: Restoring focused view ");
                sb.append(viewY);
                sb.append(" ");
                sb.append(zRequestFocus ? "succeeded" : "failed");
                sb.append(" on Fragment ");
                sb.append(this.f4872c);
                sb.append(" resulting in focused view ");
                sb.append(this.f4872c.f4920J.findFocus());
                Log.v("FragmentManager", sb.toString());
            }
        }
        this.f4872c.t1(null);
        this.f4872c.e1();
        this.f4870a.i(this.f4872c, false);
        Fragment fragment = this.f4872c;
        fragment.f4940c = null;
        fragment.f4941d = null;
        fragment.f4942e = null;
    }

    void r() {
        C c4 = new C(this.f4872c);
        Fragment fragment = this.f4872c;
        if (fragment.f4938b <= -1 || c4.f4869m != null) {
            c4.f4869m = fragment.f4940c;
        } else {
            Bundle bundleQ = q();
            c4.f4869m = bundleQ;
            if (this.f4872c.f4947j != null) {
                if (bundleQ == null) {
                    c4.f4869m = new Bundle();
                }
                c4.f4869m.putString("android:target_state", this.f4872c.f4947j);
                int i3 = this.f4872c.f4948k;
                if (i3 != 0) {
                    c4.f4869m.putInt("android:target_req_state", i3);
                }
            }
        }
        this.f4871b.B(this.f4872c.f4944g, c4);
    }

    void s() {
        if (this.f4872c.f4920J == null) {
            return;
        }
        if (x.G0(2)) {
            Log.v("FragmentManager", "Saving view state for fragment " + this.f4872c + " with view " + this.f4872c.f4920J);
        }
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        this.f4872c.f4920J.saveHierarchyState(sparseArray);
        if (sparseArray.size() > 0) {
            this.f4872c.f4941d = sparseArray;
        }
        Bundle bundle = new Bundle();
        this.f4872c.f4931U.g(bundle);
        if (bundle.isEmpty()) {
            return;
        }
        this.f4872c.f4942e = bundle;
    }

    void t(int i3) {
        this.f4874e = i3;
    }

    void u() {
        if (x.G0(3)) {
            Log.d("FragmentManager", "moveto STARTED: " + this.f4872c);
        }
        this.f4872c.g1();
        this.f4870a.k(this.f4872c, false);
    }

    void v() {
        if (x.G0(3)) {
            Log.d("FragmentManager", "movefrom STARTED: " + this.f4872c);
        }
        this.f4872c.h1();
        this.f4870a.l(this.f4872c, false);
    }

    D(r rVar, E e4, ClassLoader classLoader, o oVar, C c4) {
        this.f4870a = rVar;
        this.f4871b = e4;
        Fragment fragmentA = c4.a(oVar, classLoader);
        this.f4872c = fragmentA;
        if (x.G0(2)) {
            Log.v("FragmentManager", "Instantiated fragment " + fragmentA);
        }
    }

    D(r rVar, E e4, Fragment fragment, C c4) {
        this.f4870a = rVar;
        this.f4871b = e4;
        this.f4872c = fragment;
        fragment.f4941d = null;
        fragment.f4942e = null;
        fragment.f4957t = 0;
        fragment.f4954q = false;
        fragment.f4950m = false;
        Fragment fragment2 = fragment.f4946i;
        fragment.f4947j = fragment2 != null ? fragment2.f4944g : null;
        fragment.f4946i = null;
        Bundle bundle = c4.f4869m;
        if (bundle != null) {
            fragment.f4940c = bundle;
        } else {
            fragment.f4940c = new Bundle();
        }
    }
}
