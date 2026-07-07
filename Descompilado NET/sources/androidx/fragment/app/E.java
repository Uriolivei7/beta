package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class E {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ArrayList f4878a = new ArrayList();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final HashMap f4879b = new HashMap();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final HashMap f4880c = new HashMap();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private A f4881d;

    E() {
    }

    void A(A a4) {
        this.f4881d = a4;
    }

    C B(String str, C c4) {
        return c4 != null ? (C) this.f4880c.put(str, c4) : (C) this.f4880c.remove(str);
    }

    void a(Fragment fragment) {
        if (this.f4878a.contains(fragment)) {
            throw new IllegalStateException("Fragment already added: " + fragment);
        }
        synchronized (this.f4878a) {
            this.f4878a.add(fragment);
        }
        fragment.f4950m = true;
    }

    void b() {
        this.f4879b.values().removeAll(Collections.singleton(null));
    }

    boolean c(String str) {
        return this.f4879b.get(str) != null;
    }

    void d(int i3) {
        for (D d4 : this.f4879b.values()) {
            if (d4 != null) {
                d4.t(i3);
            }
        }
    }

    void e(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str2 = str + "    ";
        if (!this.f4879b.isEmpty()) {
            printWriter.print(str);
            printWriter.println("Active Fragments:");
            for (D d4 : this.f4879b.values()) {
                printWriter.print(str);
                if (d4 != null) {
                    Fragment fragmentK = d4.k();
                    printWriter.println(fragmentK);
                    fragmentK.e(str2, fileDescriptor, printWriter, strArr);
                } else {
                    printWriter.println("null");
                }
            }
        }
        int size = this.f4878a.size();
        if (size > 0) {
            printWriter.print(str);
            printWriter.println("Added Fragments:");
            for (int i3 = 0; i3 < size; i3++) {
                Fragment fragment = (Fragment) this.f4878a.get(i3);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i3);
                printWriter.print(": ");
                printWriter.println(fragment.toString());
            }
        }
    }

    Fragment f(String str) {
        D d4 = (D) this.f4879b.get(str);
        if (d4 != null) {
            return d4.k();
        }
        return null;
    }

    Fragment g(int i3) {
        for (int size = this.f4878a.size() - 1; size >= 0; size--) {
            Fragment fragment = (Fragment) this.f4878a.get(size);
            if (fragment != null && fragment.f4962y == i3) {
                return fragment;
            }
        }
        for (D d4 : this.f4879b.values()) {
            if (d4 != null) {
                Fragment fragmentK = d4.k();
                if (fragmentK.f4962y == i3) {
                    return fragmentK;
                }
            }
        }
        return null;
    }

    Fragment h(String str) {
        if (str != null) {
            for (int size = this.f4878a.size() - 1; size >= 0; size--) {
                Fragment fragment = (Fragment) this.f4878a.get(size);
                if (fragment != null && str.equals(fragment.f4911A)) {
                    return fragment;
                }
            }
        }
        if (str == null) {
            return null;
        }
        for (D d4 : this.f4879b.values()) {
            if (d4 != null) {
                Fragment fragmentK = d4.k();
                if (str.equals(fragmentK.f4911A)) {
                    return fragmentK;
                }
            }
        }
        return null;
    }

    Fragment i(String str) {
        Fragment fragmentG;
        for (D d4 : this.f4879b.values()) {
            if (d4 != null && (fragmentG = d4.k().g(str)) != null) {
                return fragmentG;
            }
        }
        return null;
    }

    int j(Fragment fragment) {
        View view;
        View view2;
        ViewGroup viewGroup = fragment.f4919I;
        if (viewGroup == null) {
            return -1;
        }
        int iIndexOf = this.f4878a.indexOf(fragment);
        for (int i3 = iIndexOf - 1; i3 >= 0; i3--) {
            Fragment fragment2 = (Fragment) this.f4878a.get(i3);
            if (fragment2.f4919I == viewGroup && (view2 = fragment2.f4920J) != null) {
                return viewGroup.indexOfChild(view2) + 1;
            }
        }
        while (true) {
            iIndexOf++;
            if (iIndexOf >= this.f4878a.size()) {
                return -1;
            }
            Fragment fragment3 = (Fragment) this.f4878a.get(iIndexOf);
            if (fragment3.f4919I == viewGroup && (view = fragment3.f4920J) != null) {
                return viewGroup.indexOfChild(view);
            }
        }
    }

    List k() {
        ArrayList arrayList = new ArrayList();
        for (D d4 : this.f4879b.values()) {
            if (d4 != null) {
                arrayList.add(d4);
            }
        }
        return arrayList;
    }

    List l() {
        ArrayList arrayList = new ArrayList();
        for (D d4 : this.f4879b.values()) {
            if (d4 != null) {
                arrayList.add(d4.k());
            } else {
                arrayList.add(null);
            }
        }
        return arrayList;
    }

    ArrayList m() {
        return new ArrayList(this.f4880c.values());
    }

    D n(String str) {
        return (D) this.f4879b.get(str);
    }

    List o() {
        ArrayList arrayList;
        if (this.f4878a.isEmpty()) {
            return Collections.emptyList();
        }
        synchronized (this.f4878a) {
            arrayList = new ArrayList(this.f4878a);
        }
        return arrayList;
    }

    A p() {
        return this.f4881d;
    }

    C q(String str) {
        return (C) this.f4880c.get(str);
    }

    void r(D d4) {
        Fragment fragmentK = d4.k();
        if (c(fragmentK.f4944g)) {
            return;
        }
        this.f4879b.put(fragmentK.f4944g, d4);
        if (fragmentK.f4915E) {
            if (fragmentK.f4914D) {
                this.f4881d.f(fragmentK);
            } else {
                this.f4881d.p(fragmentK);
            }
            fragmentK.f4915E = false;
        }
        if (x.G0(2)) {
            Log.v("FragmentManager", "Added fragment to active set " + fragmentK);
        }
    }

    void s(D d4) {
        Fragment fragmentK = d4.k();
        if (fragmentK.f4914D) {
            this.f4881d.p(fragmentK);
        }
        if (((D) this.f4879b.put(fragmentK.f4944g, null)) != null && x.G0(2)) {
            Log.v("FragmentManager", "Removed fragment from active set " + fragmentK);
        }
    }

    void t() {
        Iterator it = this.f4878a.iterator();
        while (it.hasNext()) {
            D d4 = (D) this.f4879b.get(((Fragment) it.next()).f4944g);
            if (d4 != null) {
                d4.m();
            }
        }
        for (D d5 : this.f4879b.values()) {
            if (d5 != null) {
                d5.m();
                Fragment fragmentK = d5.k();
                if (fragmentK.f4951n && !fragmentK.Y()) {
                    if (fragmentK.f4952o && !this.f4880c.containsKey(fragmentK.f4944g)) {
                        d5.r();
                    }
                    s(d5);
                }
            }
        }
    }

    void u(Fragment fragment) {
        synchronized (this.f4878a) {
            this.f4878a.remove(fragment);
        }
        fragment.f4950m = false;
    }

    void v() {
        this.f4879b.clear();
    }

    void w(List list) {
        this.f4878a.clear();
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Fragment fragmentF = f(str);
                if (fragmentF == null) {
                    throw new IllegalStateException("No instantiated fragment for (" + str + ")");
                }
                if (x.G0(2)) {
                    Log.v("FragmentManager", "restoreSaveState: added (" + str + "): " + fragmentF);
                }
                a(fragmentF);
            }
        }
    }

    void x(ArrayList arrayList) {
        this.f4880c.clear();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            C c4 = (C) it.next();
            this.f4880c.put(c4.f4858b, c4);
        }
    }

    ArrayList y() {
        ArrayList arrayList = new ArrayList(this.f4879b.size());
        for (D d4 : this.f4879b.values()) {
            if (d4 != null) {
                Fragment fragmentK = d4.k();
                d4.r();
                arrayList.add(fragmentK.f4944g);
                if (x.G0(2)) {
                    Log.v("FragmentManager", "Saved state of " + fragmentK + ": " + fragmentK.f4940c);
                }
            }
        }
        return arrayList;
    }

    ArrayList z() {
        synchronized (this.f4878a) {
            try {
                if (this.f4878a.isEmpty()) {
                    return null;
                }
                ArrayList arrayList = new ArrayList(this.f4878a.size());
                for (Fragment fragment : this.f4878a) {
                    arrayList.add(fragment.f4944g);
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "saveAllState: adding fragment (" + fragment.f4944g + "): " + fragment);
                    }
                }
                return arrayList;
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
