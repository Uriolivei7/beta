package androidx.fragment.app;

import android.view.ViewGroup;
import androidx.lifecycle.AbstractC0299g;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public abstract class F {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final o f4882a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ClassLoader f4883b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    ArrayList f4884c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    int f4885d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    int f4886e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    int f4887f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    int f4888g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    int f4889h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    boolean f4890i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    boolean f4891j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    String f4892k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    int f4893l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    CharSequence f4894m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    int f4895n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    CharSequence f4896o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    ArrayList f4897p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    ArrayList f4898q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    boolean f4899r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    ArrayList f4900s;

    static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        int f4901a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        Fragment f4902b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        boolean f4903c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        int f4904d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        int f4905e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        int f4906f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        int f4907g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        AbstractC0299g.b f4908h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        AbstractC0299g.b f4909i;

        a() {
        }

        a(int i3, Fragment fragment) {
            this.f4901a = i3;
            this.f4902b = fragment;
            this.f4903c = false;
            AbstractC0299g.b bVar = AbstractC0299g.b.RESUMED;
            this.f4908h = bVar;
            this.f4909i = bVar;
        }

        a(int i3, Fragment fragment, boolean z3) {
            this.f4901a = i3;
            this.f4902b = fragment;
            this.f4903c = z3;
            AbstractC0299g.b bVar = AbstractC0299g.b.RESUMED;
            this.f4908h = bVar;
            this.f4909i = bVar;
        }
    }

    @Deprecated
    public F() {
        this.f4884c = new ArrayList();
        this.f4891j = true;
        this.f4899r = false;
        this.f4882a = null;
        this.f4883b = null;
    }

    public F b(int i3, Fragment fragment, String str) {
        k(i3, fragment, str, 1);
        return this;
    }

    F c(ViewGroup viewGroup, Fragment fragment, String str) {
        fragment.f4919I = viewGroup;
        return b(viewGroup.getId(), fragment, str);
    }

    public F d(Fragment fragment, String str) {
        k(0, fragment, str, 1);
        return this;
    }

    void e(a aVar) {
        this.f4884c.add(aVar);
        aVar.f4904d = this.f4885d;
        aVar.f4905e = this.f4886e;
        aVar.f4906f = this.f4887f;
        aVar.f4907g = this.f4888g;
    }

    public abstract int f();

    public abstract int g();

    public abstract void h();

    public abstract void i();

    public F j() {
        if (this.f4890i) {
            throw new IllegalStateException("This transaction is already being added to the back stack");
        }
        this.f4891j = false;
        return this;
    }

    void k(int i3, Fragment fragment, String str, int i4) {
        String str2 = fragment.f4928R;
        if (str2 != null) {
            C.c.f(fragment, str2);
        }
        Class<?> cls = fragment.getClass();
        int modifiers = cls.getModifiers();
        if (cls.isAnonymousClass() || !Modifier.isPublic(modifiers) || (cls.isMemberClass() && !Modifier.isStatic(modifiers))) {
            throw new IllegalStateException("Fragment " + cls.getCanonicalName() + " must be a public static class to be  properly recreated from instance state.");
        }
        if (str != null) {
            String str3 = fragment.f4911A;
            if (str3 != null && !str.equals(str3)) {
                throw new IllegalStateException("Can't change tag of fragment " + fragment + ": was " + fragment.f4911A + " now " + str);
            }
            fragment.f4911A = str;
        }
        if (i3 != 0) {
            if (i3 == -1) {
                throw new IllegalArgumentException("Can't add fragment " + fragment + " with tag " + str + " to container view with no id");
            }
            int i5 = fragment.f4962y;
            if (i5 != 0 && i5 != i3) {
                throw new IllegalStateException("Can't change container ID of fragment " + fragment + ": was " + fragment.f4962y + " now " + i3);
            }
            fragment.f4962y = i3;
            fragment.f4963z = i3;
        }
        e(new a(i4, fragment));
    }

    public F l(Fragment fragment) {
        e(new a(3, fragment));
        return this;
    }

    public F m(boolean z3) {
        this.f4899r = z3;
        return this;
    }

    F(o oVar, ClassLoader classLoader) {
        this.f4884c = new ArrayList();
        this.f4891j = true;
        this.f4899r = false;
        this.f4882a = oVar;
        this.f4883b = classLoader;
    }
}
