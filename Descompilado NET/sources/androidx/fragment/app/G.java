package androidx.fragment.app;

import android.view.View;
import java.util.ArrayList;
import l.C0589a;

/* JADX INFO: loaded from: classes.dex */
abstract class G {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static final I f4996a = new H();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    static final I f4997b = c();

    static void a(Fragment fragment, Fragment fragment2, boolean z3, C0589a c0589a, boolean z4) {
        androidx.core.app.m mVarU = z3 ? fragment2.u() : fragment.u();
        if (mVarU != null) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            int size = c0589a == null ? 0 : c0589a.size();
            for (int i3 = 0; i3 < size; i3++) {
                arrayList2.add((String) c0589a.i(i3));
                arrayList.add((View) c0589a.m(i3));
            }
            if (z4) {
                mVarU.c(arrayList2, arrayList, null);
            } else {
                mVarU.b(arrayList2, arrayList, null);
            }
        }
    }

    static String b(C0589a c0589a, String str) {
        int size = c0589a.size();
        for (int i3 = 0; i3 < size; i3++) {
            if (str.equals(c0589a.m(i3))) {
                return (String) c0589a.i(i3);
            }
        }
        return null;
    }

    private static I c() {
        try {
            return (I) Class.forName("androidx.transition.FragmentTransitionSupport").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    static void d(C0589a c0589a, C0589a c0589a2) {
        for (int size = c0589a.size() - 1; size >= 0; size--) {
            if (!c0589a2.containsKey((String) c0589a.m(size))) {
                c0589a.k(size);
            }
        }
    }

    static void e(ArrayList arrayList, int i3) {
        if (arrayList == null) {
            return;
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            ((View) arrayList.get(size)).setVisibility(i3);
        }
    }
}
