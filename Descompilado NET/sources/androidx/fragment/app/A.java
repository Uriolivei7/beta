package androidx.fragment.app;

import android.util.Log;
import androidx.lifecycle.E;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
final class A extends androidx.lifecycle.D {

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static final E.b f4849k = new a();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final boolean f4853g;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final HashMap f4850d = new HashMap();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final HashMap f4851e = new HashMap();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final HashMap f4852f = new HashMap();

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f4854h = false;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f4855i = false;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f4856j = false;

    class a implements E.b {
        a() {
        }

        @Override // androidx.lifecycle.E.b
        public androidx.lifecycle.D a(Class cls) {
            return new A(true);
        }
    }

    A(boolean z3) {
        this.f4853g = z3;
    }

    private void i(String str) {
        A a4 = (A) this.f4851e.get(str);
        if (a4 != null) {
            a4.d();
            this.f4851e.remove(str);
        }
        androidx.lifecycle.G g3 = (androidx.lifecycle.G) this.f4852f.get(str);
        if (g3 != null) {
            g3.a();
            this.f4852f.remove(str);
        }
    }

    static A l(androidx.lifecycle.G g3) {
        return (A) new androidx.lifecycle.E(g3, f4849k).a(A.class);
    }

    @Override // androidx.lifecycle.D
    protected void d() {
        if (x.G0(3)) {
            Log.d("FragmentManager", "onCleared called for " + this);
        }
        this.f4854h = true;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || A.class != obj.getClass()) {
            return false;
        }
        A a4 = (A) obj;
        return this.f4850d.equals(a4.f4850d) && this.f4851e.equals(a4.f4851e) && this.f4852f.equals(a4.f4852f);
    }

    void f(Fragment fragment) {
        if (this.f4856j) {
            if (x.G0(2)) {
                Log.v("FragmentManager", "Ignoring addRetainedFragment as the state is already saved");
            }
        } else {
            if (this.f4850d.containsKey(fragment.f4944g)) {
                return;
            }
            this.f4850d.put(fragment.f4944g, fragment);
            if (x.G0(2)) {
                Log.v("FragmentManager", "Updating retained Fragments: Added " + fragment);
            }
        }
    }

    void g(Fragment fragment) {
        if (x.G0(3)) {
            Log.d("FragmentManager", "Clearing non-config state for " + fragment);
        }
        i(fragment.f4944g);
    }

    void h(String str) {
        if (x.G0(3)) {
            Log.d("FragmentManager", "Clearing non-config state for saved state of Fragment " + str);
        }
        i(str);
    }

    public int hashCode() {
        return (((this.f4850d.hashCode() * 31) + this.f4851e.hashCode()) * 31) + this.f4852f.hashCode();
    }

    Fragment j(String str) {
        return (Fragment) this.f4850d.get(str);
    }

    A k(Fragment fragment) {
        A a4 = (A) this.f4851e.get(fragment.f4944g);
        if (a4 != null) {
            return a4;
        }
        A a5 = new A(this.f4853g);
        this.f4851e.put(fragment.f4944g, a5);
        return a5;
    }

    Collection m() {
        return new ArrayList(this.f4850d.values());
    }

    androidx.lifecycle.G n(Fragment fragment) {
        androidx.lifecycle.G g3 = (androidx.lifecycle.G) this.f4852f.get(fragment.f4944g);
        if (g3 != null) {
            return g3;
        }
        androidx.lifecycle.G g4 = new androidx.lifecycle.G();
        this.f4852f.put(fragment.f4944g, g4);
        return g4;
    }

    boolean o() {
        return this.f4854h;
    }

    void p(Fragment fragment) {
        if (this.f4856j) {
            if (x.G0(2)) {
                Log.v("FragmentManager", "Ignoring removeRetainedFragment as the state is already saved");
            }
        } else {
            if (this.f4850d.remove(fragment.f4944g) == null || !x.G0(2)) {
                return;
            }
            Log.v("FragmentManager", "Updating retained Fragments: Removed " + fragment);
        }
    }

    void q(boolean z3) {
        this.f4856j = z3;
    }

    boolean r(Fragment fragment) {
        if (this.f4850d.containsKey(fragment.f4944g)) {
            return this.f4853g ? this.f4854h : !this.f4855i;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("FragmentManagerViewModel{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append("} Fragments (");
        Iterator it = this.f4850d.values().iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(") Child Non Config (");
        Iterator it2 = this.f4851e.keySet().iterator();
        while (it2.hasNext()) {
            sb.append((String) it2.next());
            if (it2.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(") ViewModelStores (");
        Iterator it3 = this.f4852f.keySet().iterator();
        while (it3.hasNext()) {
            sb.append((String) it3.next());
            if (it3.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
