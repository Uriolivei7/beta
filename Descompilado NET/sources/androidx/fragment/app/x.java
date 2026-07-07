package androidx.fragment.app;

import C.c;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedDispatcher;
import androidx.core.view.InterfaceC0278z;
import androidx.fragment.app.F;
import androidx.lifecycle.AbstractC0299g;
import androidx.savedstate.a;
import b.AbstractC0303a;
import b.C0304b;
import b.C0305c;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import q.InterfaceC0643a;

/* JADX INFO: loaded from: classes.dex */
public abstract class x {

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private static boolean f5183S = false;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private androidx.activity.result.c f5187D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private androidx.activity.result.c f5188E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private androidx.activity.result.c f5189F;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private boolean f5191H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private boolean f5192I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private boolean f5193J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private boolean f5194K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    private boolean f5195L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    private ArrayList f5196M;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private ArrayList f5197N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private ArrayList f5198O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private A f5199P;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private c.C0001c f5200Q;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f5203b;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    ArrayList f5205d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private ArrayList f5206e;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private OnBackPressedDispatcher f5208g;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private ArrayList f5214m;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private p f5223v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private AbstractC0290l f5224w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private Fragment f5225x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    Fragment f5226y;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ArrayList f5202a = new ArrayList();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final E f5204c = new E();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final q f5207f = new q(this);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final androidx.activity.m f5209h = new b(false);

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final AtomicInteger f5210i = new AtomicInteger();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final Map f5211j = Collections.synchronizedMap(new HashMap());

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final Map f5212k = Collections.synchronizedMap(new HashMap());

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final Map f5213l = Collections.synchronizedMap(new HashMap());

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final r f5215n = new r(this);

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final CopyOnWriteArrayList f5216o = new CopyOnWriteArrayList();

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final InterfaceC0643a f5217p = new InterfaceC0643a() { // from class: androidx.fragment.app.s
        @Override // q.InterfaceC0643a
        public final void a(Object obj) {
            this.f5178a.P0((Configuration) obj);
        }
    };

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final InterfaceC0643a f5218q = new InterfaceC0643a() { // from class: androidx.fragment.app.t
        @Override // q.InterfaceC0643a
        public final void a(Object obj) {
            this.f5179a.Q0((Integer) obj);
        }
    };

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final InterfaceC0643a f5219r = new InterfaceC0643a() { // from class: androidx.fragment.app.u
        @Override // q.InterfaceC0643a
        public final void a(Object obj) {
            this.f5180a.R0((androidx.core.app.g) obj);
        }
    };

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final InterfaceC0643a f5220s = new InterfaceC0643a() { // from class: androidx.fragment.app.v
        @Override // q.InterfaceC0643a
        public final void a(Object obj) {
            this.f5181a.S0((androidx.core.app.l) obj);
        }
    };

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final androidx.core.view.C f5221t = new c();

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    int f5222u = -1;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private o f5227z = null;

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private o f5184A = new d();

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private M f5185B = null;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private M f5186C = new e();

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    ArrayDeque f5190G = new ArrayDeque();

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private Runnable f5201R = new f();

    class a implements androidx.activity.result.b {
        a() {
        }

        @Override // androidx.activity.result.b
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(Map map) {
            String[] strArr = (String[]) map.keySet().toArray(new String[0]);
            ArrayList arrayList = new ArrayList(map.values());
            int[] iArr = new int[arrayList.size()];
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                iArr[i3] = ((Boolean) arrayList.get(i3)).booleanValue() ? 0 : -1;
            }
            k kVar = (k) x.this.f5190G.pollFirst();
            if (kVar == null) {
                Log.w("FragmentManager", "No permissions were requested for " + this);
                return;
            }
            String str = kVar.f5238a;
            int i4 = kVar.f5239b;
            Fragment fragmentI = x.this.f5204c.i(str);
            if (fragmentI != null) {
                fragmentI.E0(i4, strArr, iArr);
                return;
            }
            Log.w("FragmentManager", "Permission request result delivered for unknown Fragment " + str);
        }
    }

    class b extends androidx.activity.m {
        b(boolean z3) {
            super(z3);
        }

        @Override // androidx.activity.m
        public void b() {
            x.this.C0();
        }
    }

    class c implements androidx.core.view.C {
        c() {
        }

        @Override // androidx.core.view.C
        public boolean a(MenuItem menuItem) {
            return x.this.J(menuItem);
        }

        @Override // androidx.core.view.C
        public void b(Menu menu) {
            x.this.K(menu);
        }

        @Override // androidx.core.view.C
        public void c(Menu menu, MenuInflater menuInflater) {
            x.this.C(menu, menuInflater);
        }

        @Override // androidx.core.view.C
        public void d(Menu menu) {
            x.this.O(menu);
        }
    }

    class d extends o {
        d() {
        }

        @Override // androidx.fragment.app.o
        public Fragment a(ClassLoader classLoader, String str) {
            return x.this.t0().e(x.this.t0().k(), str, null);
        }
    }

    class e implements M {
        e() {
        }

        @Override // androidx.fragment.app.M
        public L a(ViewGroup viewGroup) {
            return new C0282d(viewGroup);
        }
    }

    class f implements Runnable {
        f() {
        }

        @Override // java.lang.Runnable
        public void run() {
            x.this.a0(true);
        }
    }

    class g implements B {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Fragment f5234b;

        g(Fragment fragment) {
            this.f5234b = fragment;
        }

        @Override // androidx.fragment.app.B
        public void c(x xVar, Fragment fragment) {
            this.f5234b.i0(fragment);
        }
    }

    class h implements androidx.activity.result.b {
        h() {
        }

        @Override // androidx.activity.result.b
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(androidx.activity.result.a aVar) {
            k kVar = (k) x.this.f5190G.pollFirst();
            if (kVar == null) {
                Log.w("FragmentManager", "No Activities were started for result for " + this);
                return;
            }
            String str = kVar.f5238a;
            int i3 = kVar.f5239b;
            Fragment fragmentI = x.this.f5204c.i(str);
            if (fragmentI != null) {
                fragmentI.f0(i3, aVar.b(), aVar.a());
                return;
            }
            Log.w("FragmentManager", "Activity result delivered for unknown Fragment " + str);
        }
    }

    class i implements androidx.activity.result.b {
        i() {
        }

        @Override // androidx.activity.result.b
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(androidx.activity.result.a aVar) {
            k kVar = (k) x.this.f5190G.pollFirst();
            if (kVar == null) {
                Log.w("FragmentManager", "No IntentSenders were started for " + this);
                return;
            }
            String str = kVar.f5238a;
            int i3 = kVar.f5239b;
            Fragment fragmentI = x.this.f5204c.i(str);
            if (fragmentI != null) {
                fragmentI.f0(i3, aVar.b(), aVar.a());
                return;
            }
            Log.w("FragmentManager", "Intent Sender result delivered for unknown Fragment " + str);
        }
    }

    static class j extends AbstractC0303a {
        j() {
        }

        @Override // b.AbstractC0303a
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public androidx.activity.result.a a(int i3, Intent intent) {
            return new androidx.activity.result.a(i3, intent);
        }
    }

    static class k implements Parcelable {
        public static final Parcelable.Creator<k> CREATOR = new a();

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        String f5238a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        int f5239b;

        class a implements Parcelable.Creator {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public k createFromParcel(Parcel parcel) {
                return new k(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
            public k[] newArray(int i3) {
                return new k[i3];
            }
        }

        k(Parcel parcel) {
            this.f5238a = parcel.readString();
            this.f5239b = parcel.readInt();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            parcel.writeString(this.f5238a);
            parcel.writeInt(this.f5239b);
        }
    }

    interface l {
        boolean a(ArrayList arrayList, ArrayList arrayList2);
    }

    private class m implements l {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final String f5240a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final int f5241b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final int f5242c;

        m(String str, int i3, int i4) {
            this.f5240a = str;
            this.f5241b = i3;
            this.f5242c = i4;
        }

        @Override // androidx.fragment.app.x.l
        public boolean a(ArrayList arrayList, ArrayList arrayList2) {
            Fragment fragment = x.this.f5226y;
            if (fragment == null || this.f5241b >= 0 || this.f5240a != null || !fragment.o().Y0()) {
                return x.this.b1(arrayList, arrayList2, this.f5240a, this.f5241b, this.f5242c);
            }
            return false;
        }
    }

    static Fragment A0(View view) {
        Object tag = view.getTag(B.b.f42a);
        if (tag instanceof Fragment) {
            return (Fragment) tag;
        }
        return null;
    }

    public static boolean G0(int i3) {
        return f5183S || Log.isLoggable("FragmentManager", i3);
    }

    private boolean H0(Fragment fragment) {
        return (fragment.f4916F && fragment.f4917G) || fragment.f4960w.p();
    }

    private boolean I0() {
        Fragment fragment = this.f5225x;
        if (fragment == null) {
            return true;
        }
        return fragment.W() && this.f5225x.E().I0();
    }

    private void L(Fragment fragment) {
        if (fragment == null || !fragment.equals(e0(fragment.f4944g))) {
            return;
        }
        fragment.d1();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void P0(Configuration configuration) {
        if (I0()) {
            z(configuration, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void Q0(Integer num) {
        if (I0() && num.intValue() == 80) {
            F(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void R0(androidx.core.app.g gVar) {
        if (I0()) {
            G(gVar.a(), false);
        }
    }

    private void S(int i3) {
        try {
            this.f5203b = true;
            this.f5204c.d(i3);
            T0(i3, false);
            Iterator it = t().iterator();
            while (it.hasNext()) {
                ((L) it.next()).j();
            }
            this.f5203b = false;
            a0(true);
        } catch (Throwable th) {
            this.f5203b = false;
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void S0(androidx.core.app.l lVar) {
        if (I0()) {
            N(lVar.a(), false);
        }
    }

    private void V() {
        if (this.f5195L) {
            this.f5195L = false;
            o1();
        }
    }

    private void X() {
        Iterator it = t().iterator();
        while (it.hasNext()) {
            ((L) it.next()).j();
        }
    }

    private void Z(boolean z3) {
        if (this.f5203b) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        }
        if (this.f5223v == null) {
            if (!this.f5194K) {
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
            throw new IllegalStateException("FragmentManager has been destroyed");
        }
        if (Looper.myLooper() != this.f5223v.m().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
        if (!z3) {
            q();
        }
        if (this.f5196M == null) {
            this.f5196M = new ArrayList();
            this.f5197N = new ArrayList();
        }
    }

    private boolean a1(String str, int i3, int i4) {
        a0(false);
        Z(true);
        Fragment fragment = this.f5226y;
        if (fragment != null && i3 < 0 && str == null && fragment.o().Y0()) {
            return true;
        }
        boolean zB1 = b1(this.f5196M, this.f5197N, str, i3, i4);
        if (zB1) {
            this.f5203b = true;
            try {
                d1(this.f5196M, this.f5197N);
            } finally {
                r();
            }
        }
        q1();
        V();
        this.f5204c.b();
        return zB1;
    }

    private static void c0(ArrayList arrayList, ArrayList arrayList2, int i3, int i4) {
        while (i3 < i4) {
            C0279a c0279a = (C0279a) arrayList.get(i3);
            if (((Boolean) arrayList2.get(i3)).booleanValue()) {
                c0279a.n(-1);
                c0279a.s();
            } else {
                c0279a.n(1);
                c0279a.r();
            }
            i3++;
        }
    }

    private void d0(ArrayList arrayList, ArrayList arrayList2, int i3, int i4) {
        boolean z3 = ((C0279a) arrayList.get(i3)).f4899r;
        ArrayList arrayList3 = this.f5198O;
        if (arrayList3 == null) {
            this.f5198O = new ArrayList();
        } else {
            arrayList3.clear();
        }
        this.f5198O.addAll(this.f5204c.o());
        Fragment fragmentX0 = x0();
        boolean z4 = false;
        for (int i5 = i3; i5 < i4; i5++) {
            C0279a c0279a = (C0279a) arrayList.get(i5);
            fragmentX0 = !((Boolean) arrayList2.get(i5)).booleanValue() ? c0279a.t(this.f5198O, fragmentX0) : c0279a.w(this.f5198O, fragmentX0);
            z4 = z4 || c0279a.f4890i;
        }
        this.f5198O.clear();
        if (!z3 && this.f5222u >= 1) {
            for (int i6 = i3; i6 < i4; i6++) {
                Iterator it = ((C0279a) arrayList.get(i6)).f4884c.iterator();
                while (it.hasNext()) {
                    Fragment fragment = ((F.a) it.next()).f4902b;
                    if (fragment != null && fragment.f4958u != null) {
                        this.f5204c.r(v(fragment));
                    }
                }
            }
        }
        c0(arrayList, arrayList2, i3, i4);
        boolean zBooleanValue = ((Boolean) arrayList2.get(i4 - 1)).booleanValue();
        for (int i7 = i3; i7 < i4; i7++) {
            C0279a c0279a2 = (C0279a) arrayList.get(i7);
            if (zBooleanValue) {
                for (int size = c0279a2.f4884c.size() - 1; size >= 0; size--) {
                    Fragment fragment2 = ((F.a) c0279a2.f4884c.get(size)).f4902b;
                    if (fragment2 != null) {
                        v(fragment2).m();
                    }
                }
            } else {
                Iterator it2 = c0279a2.f4884c.iterator();
                while (it2.hasNext()) {
                    Fragment fragment3 = ((F.a) it2.next()).f4902b;
                    if (fragment3 != null) {
                        v(fragment3).m();
                    }
                }
            }
        }
        T0(this.f5222u, true);
        for (L l3 : u(arrayList, i3, i4)) {
            l3.r(zBooleanValue);
            l3.p();
            l3.g();
        }
        while (i3 < i4) {
            C0279a c0279a3 = (C0279a) arrayList.get(i3);
            if (((Boolean) arrayList2.get(i3)).booleanValue() && c0279a3.f5058v >= 0) {
                c0279a3.f5058v = -1;
            }
            c0279a3.v();
            i3++;
        }
        if (z4) {
            e1();
        }
    }

    private void d1(ArrayList arrayList, ArrayList arrayList2) {
        if (arrayList.isEmpty()) {
            return;
        }
        if (arrayList.size() != arrayList2.size()) {
            throw new IllegalStateException("Internal error with the back stack records");
        }
        int size = arrayList.size();
        int i3 = 0;
        int i4 = 0;
        while (i3 < size) {
            if (!((C0279a) arrayList.get(i3)).f4899r) {
                if (i4 != i3) {
                    d0(arrayList, arrayList2, i4, i3);
                }
                i4 = i3 + 1;
                if (((Boolean) arrayList2.get(i3)).booleanValue()) {
                    while (i4 < size && ((Boolean) arrayList2.get(i4)).booleanValue() && !((C0279a) arrayList.get(i4)).f4899r) {
                        i4++;
                    }
                }
                d0(arrayList, arrayList2, i3, i4);
                i3 = i4 - 1;
            }
            i3++;
        }
        if (i4 != size) {
            d0(arrayList, arrayList2, i4, size);
        }
    }

    private void e1() {
        ArrayList arrayList = this.f5214m;
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        androidx.activity.result.d.a(this.f5214m.get(0));
        throw null;
    }

    private int f0(String str, int i3, boolean z3) {
        ArrayList arrayList = this.f5205d;
        if (arrayList == null || arrayList.isEmpty()) {
            return -1;
        }
        if (str == null && i3 < 0) {
            if (z3) {
                return 0;
            }
            return this.f5205d.size() - 1;
        }
        int size = this.f5205d.size() - 1;
        while (size >= 0) {
            C0279a c0279a = (C0279a) this.f5205d.get(size);
            if ((str != null && str.equals(c0279a.u())) || (i3 >= 0 && i3 == c0279a.f5058v)) {
                break;
            }
            size--;
        }
        if (size < 0) {
            return size;
        }
        if (!z3) {
            if (size == this.f5205d.size() - 1) {
                return -1;
            }
            return size + 1;
        }
        while (size > 0) {
            C0279a c0279a2 = (C0279a) this.f5205d.get(size - 1);
            if ((str == null || !str.equals(c0279a2.u())) && (i3 < 0 || i3 != c0279a2.f5058v)) {
                return size;
            }
            size--;
        }
        return size;
    }

    static int g1(int i3) {
        int i4 = 4097;
        if (i3 == 4097) {
            return 8194;
        }
        if (i3 != 8194) {
            i4 = 8197;
            if (i3 == 8197) {
                return 4100;
            }
            if (i3 == 4099) {
                return 4099;
            }
            if (i3 != 4100) {
                return 0;
            }
        }
        return i4;
    }

    static x j0(View view) {
        ActivityC0288j activityC0288j;
        Fragment fragmentK0 = k0(view);
        if (fragmentK0 != null) {
            if (fragmentK0.W()) {
                return fragmentK0.o();
            }
            throw new IllegalStateException("The Fragment " + fragmentK0 + " that owns View " + view + " has already been destroyed. Nested fragments should always use the child FragmentManager.");
        }
        Context context = view.getContext();
        while (true) {
            if (!(context instanceof ContextWrapper)) {
                activityC0288j = null;
                break;
            }
            if (context instanceof ActivityC0288j) {
                activityC0288j = (ActivityC0288j) context;
                break;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (activityC0288j != null) {
            return activityC0288j.T();
        }
        throw new IllegalStateException("View " + view + " is not within a subclass of FragmentActivity.");
    }

    private static Fragment k0(View view) {
        while (view != null) {
            Fragment fragmentA0 = A0(view);
            if (fragmentA0 != null) {
                return fragmentA0;
            }
            Object parent = view.getParent();
            view = parent instanceof View ? (View) parent : null;
        }
        return null;
    }

    private void l0() {
        Iterator it = t().iterator();
        while (it.hasNext()) {
            ((L) it.next()).k();
        }
    }

    private boolean m0(ArrayList arrayList, ArrayList arrayList2) {
        synchronized (this.f5202a) {
            if (this.f5202a.isEmpty()) {
                return false;
            }
            try {
                int size = this.f5202a.size();
                boolean zA = false;
                for (int i3 = 0; i3 < size; i3++) {
                    zA |= ((l) this.f5202a.get(i3)).a(arrayList, arrayList2);
                }
                return zA;
            } finally {
                this.f5202a.clear();
                this.f5223v.m().removeCallbacks(this.f5201R);
            }
        }
    }

    private void m1(Fragment fragment) {
        ViewGroup viewGroupQ0 = q0(fragment);
        if (viewGroupQ0 == null || fragment.q() + fragment.v() + fragment.G() + fragment.H() <= 0) {
            return;
        }
        if (viewGroupQ0.getTag(B.b.f44c) == null) {
            viewGroupQ0.setTag(B.b.f44c, fragment);
        }
        ((Fragment) viewGroupQ0.getTag(B.b.f44c)).v1(fragment.F());
    }

    private A o0(Fragment fragment) {
        return this.f5199P.k(fragment);
    }

    private void o1() {
        Iterator it = this.f5204c.k().iterator();
        while (it.hasNext()) {
            W0((D) it.next());
        }
    }

    private void p1(RuntimeException runtimeException) {
        Log.e("FragmentManager", runtimeException.getMessage());
        Log.e("FragmentManager", "Activity state:");
        PrintWriter printWriter = new PrintWriter(new K("FragmentManager"));
        p pVar = this.f5223v;
        if (pVar != null) {
            try {
                pVar.p("  ", null, printWriter, new String[0]);
                throw runtimeException;
            } catch (Exception e4) {
                Log.e("FragmentManager", "Failed dumping state", e4);
                throw runtimeException;
            }
        }
        try {
            W("  ", null, printWriter, new String[0]);
            throw runtimeException;
        } catch (Exception e5) {
            Log.e("FragmentManager", "Failed dumping state", e5);
            throw runtimeException;
        }
    }

    private void q() {
        if (N0()) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
    }

    private ViewGroup q0(Fragment fragment) {
        ViewGroup viewGroup = fragment.f4919I;
        if (viewGroup != null) {
            return viewGroup;
        }
        if (fragment.f4963z > 0 && this.f5224w.h()) {
            View viewF = this.f5224w.f(fragment.f4963z);
            if (viewF instanceof ViewGroup) {
                return (ViewGroup) viewF;
            }
        }
        return null;
    }

    private void q1() {
        synchronized (this.f5202a) {
            try {
                if (this.f5202a.isEmpty()) {
                    this.f5209h.f(n0() > 0 && L0(this.f5225x));
                } else {
                    this.f5209h.f(true);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void r() {
        this.f5203b = false;
        this.f5197N.clear();
        this.f5196M.clear();
    }

    private void s() {
        p pVar = this.f5223v;
        if (pVar instanceof androidx.lifecycle.H ? this.f5204c.p().o() : pVar.k() instanceof Activity ? !((Activity) this.f5223v.k()).isChangingConfigurations() : true) {
            Iterator it = this.f5211j.values().iterator();
            while (it.hasNext()) {
                Iterator it2 = ((C0281c) it.next()).f5074a.iterator();
                while (it2.hasNext()) {
                    this.f5204c.p().h((String) it2.next());
                }
            }
        }
    }

    private Set t() {
        HashSet hashSet = new HashSet();
        Iterator it = this.f5204c.k().iterator();
        while (it.hasNext()) {
            ViewGroup viewGroup = ((D) it.next()).k().f4919I;
            if (viewGroup != null) {
                hashSet.add(L.o(viewGroup, y0()));
            }
        }
        return hashSet;
    }

    private Set u(ArrayList arrayList, int i3, int i4) {
        ViewGroup viewGroup;
        HashSet hashSet = new HashSet();
        while (i3 < i4) {
            Iterator it = ((C0279a) arrayList.get(i3)).f4884c.iterator();
            while (it.hasNext()) {
                Fragment fragment = ((F.a) it.next()).f4902b;
                if (fragment != null && (viewGroup = fragment.f4919I) != null) {
                    hashSet.add(L.n(viewGroup, this));
                }
            }
            i3++;
        }
        return hashSet;
    }

    boolean A(MenuItem menuItem) {
        if (this.f5222u < 1) {
            return false;
        }
        for (Fragment fragment : this.f5204c.o()) {
            if (fragment != null && fragment.O0(menuItem)) {
                return true;
            }
        }
        return false;
    }

    void B() {
        this.f5192I = false;
        this.f5193J = false;
        this.f5199P.q(false);
        S(1);
    }

    androidx.lifecycle.G B0(Fragment fragment) {
        return this.f5199P.n(fragment);
    }

    boolean C(Menu menu, MenuInflater menuInflater) {
        if (this.f5222u < 1) {
            return false;
        }
        ArrayList arrayList = null;
        boolean z3 = false;
        for (Fragment fragment : this.f5204c.o()) {
            if (fragment != null && K0(fragment) && fragment.Q0(menu, menuInflater)) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(fragment);
                z3 = true;
            }
        }
        if (this.f5206e != null) {
            for (int i3 = 0; i3 < this.f5206e.size(); i3++) {
                Fragment fragment2 = (Fragment) this.f5206e.get(i3);
                if (arrayList == null || !arrayList.contains(fragment2)) {
                    fragment2.q0();
                }
            }
        }
        this.f5206e = arrayList;
        return z3;
    }

    void C0() {
        a0(true);
        if (this.f5209h.c()) {
            Y0();
        } else {
            this.f5208g.e();
        }
    }

    void D() {
        this.f5194K = true;
        a0(true);
        X();
        s();
        S(-1);
        Object obj = this.f5223v;
        if (obj instanceof androidx.core.content.d) {
            ((androidx.core.content.d) obj).v(this.f5218q);
        }
        Object obj2 = this.f5223v;
        if (obj2 instanceof androidx.core.content.c) {
            ((androidx.core.content.c) obj2).i(this.f5217p);
        }
        Object obj3 = this.f5223v;
        if (obj3 instanceof androidx.core.app.j) {
            ((androidx.core.app.j) obj3).g(this.f5219r);
        }
        Object obj4 = this.f5223v;
        if (obj4 instanceof androidx.core.app.k) {
            ((androidx.core.app.k) obj4).u(this.f5220s);
        }
        Object obj5 = this.f5223v;
        if (obj5 instanceof InterfaceC0278z) {
            ((InterfaceC0278z) obj5).d(this.f5221t);
        }
        this.f5223v = null;
        this.f5224w = null;
        this.f5225x = null;
        if (this.f5208g != null) {
            this.f5209h.d();
            this.f5208g = null;
        }
        androidx.activity.result.c cVar = this.f5187D;
        if (cVar != null) {
            cVar.a();
            this.f5188E.a();
            this.f5189F.a();
        }
    }

    void D0(Fragment fragment) {
        if (G0(2)) {
            Log.v("FragmentManager", "hide: " + fragment);
        }
        if (fragment.f4912B) {
            return;
        }
        fragment.f4912B = true;
        fragment.f4925O = true ^ fragment.f4925O;
        m1(fragment);
    }

    void E() {
        S(1);
    }

    void E0(Fragment fragment) {
        if (fragment.f4950m && H0(fragment)) {
            this.f5191H = true;
        }
    }

    void F(boolean z3) {
        if (z3 && (this.f5223v instanceof androidx.core.content.d)) {
            p1(new IllegalStateException("Do not call dispatchLowMemory() on host. Host implements OnTrimMemoryProvider and automatically dispatches low memory callbacks to fragments."));
        }
        for (Fragment fragment : this.f5204c.o()) {
            if (fragment != null) {
                fragment.W0();
                if (z3) {
                    fragment.f4960w.F(true);
                }
            }
        }
    }

    public boolean F0() {
        return this.f5194K;
    }

    void G(boolean z3, boolean z4) {
        if (z4 && (this.f5223v instanceof androidx.core.app.j)) {
            p1(new IllegalStateException("Do not call dispatchMultiWindowModeChanged() on host. Host implements OnMultiWindowModeChangedProvider and automatically dispatches multi-window mode changes to fragments."));
        }
        for (Fragment fragment : this.f5204c.o()) {
            if (fragment != null) {
                fragment.X0(z3);
                if (z4) {
                    fragment.f4960w.G(z3, true);
                }
            }
        }
    }

    void H(Fragment fragment) {
        Iterator it = this.f5216o.iterator();
        while (it.hasNext()) {
            ((B) it.next()).c(this, fragment);
        }
    }

    void I() {
        for (Fragment fragment : this.f5204c.l()) {
            if (fragment != null) {
                fragment.u0(fragment.X());
                fragment.f4960w.I();
            }
        }
    }

    boolean J(MenuItem menuItem) {
        if (this.f5222u < 1) {
            return false;
        }
        for (Fragment fragment : this.f5204c.o()) {
            if (fragment != null && fragment.Y0(menuItem)) {
                return true;
            }
        }
        return false;
    }

    boolean J0(Fragment fragment) {
        if (fragment == null) {
            return false;
        }
        return fragment.X();
    }

    void K(Menu menu) {
        if (this.f5222u < 1) {
            return;
        }
        for (Fragment fragment : this.f5204c.o()) {
            if (fragment != null) {
                fragment.Z0(menu);
            }
        }
    }

    boolean K0(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        return fragment.Z();
    }

    boolean L0(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        x xVar = fragment.f4958u;
        return fragment.equals(xVar.x0()) && L0(xVar.f5225x);
    }

    void M() {
        S(5);
    }

    boolean M0(int i3) {
        return this.f5222u >= i3;
    }

    void N(boolean z3, boolean z4) {
        if (z4 && (this.f5223v instanceof androidx.core.app.k)) {
            p1(new IllegalStateException("Do not call dispatchPictureInPictureModeChanged() on host. Host implements OnPictureInPictureModeChangedProvider and automatically dispatches picture-in-picture mode changes to fragments."));
        }
        for (Fragment fragment : this.f5204c.o()) {
            if (fragment != null) {
                fragment.b1(z3);
                if (z4) {
                    fragment.f4960w.N(z3, true);
                }
            }
        }
    }

    public boolean N0() {
        return this.f5192I || this.f5193J;
    }

    boolean O(Menu menu) {
        boolean z3 = false;
        if (this.f5222u < 1) {
            return false;
        }
        for (Fragment fragment : this.f5204c.o()) {
            if (fragment != null && K0(fragment) && fragment.c1(menu)) {
                z3 = true;
            }
        }
        return z3;
    }

    void P() {
        q1();
        L(this.f5226y);
    }

    void Q() {
        this.f5192I = false;
        this.f5193J = false;
        this.f5199P.q(false);
        S(7);
    }

    void R() {
        this.f5192I = false;
        this.f5193J = false;
        this.f5199P.q(false);
        S(5);
    }

    void T() {
        this.f5193J = true;
        this.f5199P.q(true);
        S(4);
    }

    void T0(int i3, boolean z3) {
        p pVar;
        if (this.f5223v == null && i3 != -1) {
            throw new IllegalStateException("No activity");
        }
        if (z3 || i3 != this.f5222u) {
            this.f5222u = i3;
            this.f5204c.t();
            o1();
            if (this.f5191H && (pVar = this.f5223v) != null && this.f5222u == 7) {
                pVar.z();
                this.f5191H = false;
            }
        }
    }

    void U() {
        S(2);
    }

    void U0() {
        if (this.f5223v == null) {
            return;
        }
        this.f5192I = false;
        this.f5193J = false;
        this.f5199P.q(false);
        for (Fragment fragment : this.f5204c.o()) {
            if (fragment != null) {
                fragment.d0();
            }
        }
    }

    void V0(C0291m c0291m) {
        View view;
        for (D d4 : this.f5204c.k()) {
            Fragment fragmentK = d4.k();
            if (fragmentK.f4963z == c0291m.getId() && (view = fragmentK.f4920J) != null && view.getParent() == null) {
                fragmentK.f4919I = c0291m;
                d4.b();
            }
        }
    }

    public void W(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int size;
        int size2;
        String str2 = str + "    ";
        this.f5204c.e(str, fileDescriptor, printWriter, strArr);
        ArrayList arrayList = this.f5206e;
        if (arrayList != null && (size2 = arrayList.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Fragments Created Menus:");
            for (int i3 = 0; i3 < size2; i3++) {
                Fragment fragment = (Fragment) this.f5206e.get(i3);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i3);
                printWriter.print(": ");
                printWriter.println(fragment.toString());
            }
        }
        ArrayList arrayList2 = this.f5205d;
        if (arrayList2 != null && (size = arrayList2.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Back Stack:");
            for (int i4 = 0; i4 < size; i4++) {
                C0279a c0279a = (C0279a) this.f5205d.get(i4);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i4);
                printWriter.print(": ");
                printWriter.println(c0279a.toString());
                c0279a.p(str2, printWriter);
            }
        }
        printWriter.print(str);
        printWriter.println("Back Stack Index: " + this.f5210i.get());
        synchronized (this.f5202a) {
            try {
                int size3 = this.f5202a.size();
                if (size3 > 0) {
                    printWriter.print(str);
                    printWriter.println("Pending Actions:");
                    for (int i5 = 0; i5 < size3; i5++) {
                        l lVar = (l) this.f5202a.get(i5);
                        printWriter.print(str);
                        printWriter.print("  #");
                        printWriter.print(i5);
                        printWriter.print(": ");
                        printWriter.println(lVar);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        printWriter.print(str);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(str);
        printWriter.print("  mHost=");
        printWriter.println(this.f5223v);
        printWriter.print(str);
        printWriter.print("  mContainer=");
        printWriter.println(this.f5224w);
        if (this.f5225x != null) {
            printWriter.print(str);
            printWriter.print("  mParent=");
            printWriter.println(this.f5225x);
        }
        printWriter.print(str);
        printWriter.print("  mCurState=");
        printWriter.print(this.f5222u);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.f5192I);
        printWriter.print(" mStopped=");
        printWriter.print(this.f5193J);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.f5194K);
        if (this.f5191H) {
            printWriter.print(str);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.f5191H);
        }
    }

    void W0(D d4) {
        Fragment fragmentK = d4.k();
        if (fragmentK.f4921K) {
            if (this.f5203b) {
                this.f5195L = true;
            } else {
                fragmentK.f4921K = false;
                d4.m();
            }
        }
    }

    void X0(int i3, int i4, boolean z3) {
        if (i3 >= 0) {
            Y(new m(null, i3, i4), z3);
            return;
        }
        throw new IllegalArgumentException("Bad id: " + i3);
    }

    void Y(l lVar, boolean z3) {
        if (!z3) {
            if (this.f5223v == null) {
                if (!this.f5194K) {
                    throw new IllegalStateException("FragmentManager has not been attached to a host.");
                }
                throw new IllegalStateException("FragmentManager has been destroyed");
            }
            q();
        }
        synchronized (this.f5202a) {
            try {
                if (this.f5223v == null) {
                    if (!z3) {
                        throw new IllegalStateException("Activity has been destroyed");
                    }
                } else {
                    this.f5202a.add(lVar);
                    i1();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean Y0() {
        return a1(null, -1, 0);
    }

    public boolean Z0(int i3, int i4) {
        if (i3 >= 0) {
            return a1(null, i3, i4);
        }
        throw new IllegalArgumentException("Bad id: " + i3);
    }

    boolean a0(boolean z3) {
        Z(z3);
        boolean z4 = false;
        while (m0(this.f5196M, this.f5197N)) {
            z4 = true;
            this.f5203b = true;
            try {
                d1(this.f5196M, this.f5197N);
            } finally {
                r();
            }
        }
        q1();
        V();
        this.f5204c.b();
        return z4;
    }

    void b0(l lVar, boolean z3) {
        if (z3 && (this.f5223v == null || this.f5194K)) {
            return;
        }
        Z(z3);
        if (lVar.a(this.f5196M, this.f5197N)) {
            this.f5203b = true;
            try {
                d1(this.f5196M, this.f5197N);
            } finally {
                r();
            }
        }
        q1();
        V();
        this.f5204c.b();
    }

    boolean b1(ArrayList arrayList, ArrayList arrayList2, String str, int i3, int i4) {
        int iF0 = f0(str, i3, (i4 & 1) != 0);
        if (iF0 < 0) {
            return false;
        }
        for (int size = this.f5205d.size() - 1; size >= iF0; size--) {
            arrayList.add((C0279a) this.f5205d.remove(size));
            arrayList2.add(Boolean.TRUE);
        }
        return true;
    }

    void c1(Fragment fragment) {
        if (G0(2)) {
            Log.v("FragmentManager", "remove: " + fragment + " nesting=" + fragment.f4957t);
        }
        boolean zY = fragment.Y();
        if (fragment.f4913C && zY) {
            return;
        }
        this.f5204c.u(fragment);
        if (H0(fragment)) {
            this.f5191H = true;
        }
        fragment.f4951n = true;
        m1(fragment);
    }

    Fragment e0(String str) {
        return this.f5204c.f(str);
    }

    void f1(Parcelable parcelable) {
        D d4;
        Bundle bundle;
        Bundle bundle2;
        if (parcelable == null) {
            return;
        }
        Bundle bundle3 = (Bundle) parcelable;
        for (String str : bundle3.keySet()) {
            if (str.startsWith("result_") && (bundle2 = bundle3.getBundle(str)) != null) {
                bundle2.setClassLoader(this.f5223v.k().getClassLoader());
                this.f5212k.put(str.substring(7), bundle2);
            }
        }
        ArrayList arrayList = new ArrayList();
        for (String str2 : bundle3.keySet()) {
            if (str2.startsWith("fragment_") && (bundle = bundle3.getBundle(str2)) != null) {
                bundle.setClassLoader(this.f5223v.k().getClassLoader());
                arrayList.add((C) bundle.getParcelable("state"));
            }
        }
        this.f5204c.x(arrayList);
        z zVar = (z) bundle3.getParcelable("state");
        if (zVar == null) {
            return;
        }
        this.f5204c.v();
        Iterator it = zVar.f5244a.iterator();
        while (it.hasNext()) {
            C cB = this.f5204c.B((String) it.next(), null);
            if (cB != null) {
                Fragment fragmentJ = this.f5199P.j(cB.f4858b);
                if (fragmentJ != null) {
                    if (G0(2)) {
                        Log.v("FragmentManager", "restoreSaveState: re-attaching retained " + fragmentJ);
                    }
                    d4 = new D(this.f5215n, this.f5204c, fragmentJ, cB);
                } else {
                    d4 = new D(this.f5215n, this.f5204c, this.f5223v.k().getClassLoader(), r0(), cB);
                }
                Fragment fragmentK = d4.k();
                fragmentK.f4958u = this;
                if (G0(2)) {
                    Log.v("FragmentManager", "restoreSaveState: active (" + fragmentK.f4944g + "): " + fragmentK);
                }
                d4.o(this.f5223v.k().getClassLoader());
                this.f5204c.r(d4);
                d4.t(this.f5222u);
            }
        }
        for (Fragment fragment : this.f5199P.m()) {
            if (!this.f5204c.c(fragment.f4944g)) {
                if (G0(2)) {
                    Log.v("FragmentManager", "Discarding retained Fragment " + fragment + " that was not found in the set of active Fragments " + zVar.f5244a);
                }
                this.f5199P.p(fragment);
                fragment.f4958u = this;
                D d5 = new D(this.f5215n, this.f5204c, fragment);
                d5.t(1);
                d5.m();
                fragment.f4951n = true;
                d5.m();
            }
        }
        this.f5204c.w(zVar.f5245b);
        if (zVar.f5246c != null) {
            this.f5205d = new ArrayList(zVar.f5246c.length);
            int i3 = 0;
            while (true) {
                C0280b[] c0280bArr = zVar.f5246c;
                if (i3 >= c0280bArr.length) {
                    break;
                }
                C0279a c0279aB = c0280bArr[i3].b(this);
                if (G0(2)) {
                    Log.v("FragmentManager", "restoreAllState: back stack #" + i3 + " (index " + c0279aB.f5058v + "): " + c0279aB);
                    PrintWriter printWriter = new PrintWriter(new K("FragmentManager"));
                    c0279aB.q("  ", printWriter, false);
                    printWriter.close();
                }
                this.f5205d.add(c0279aB);
                i3++;
            }
        } else {
            this.f5205d = null;
        }
        this.f5210i.set(zVar.f5247d);
        String str3 = zVar.f5248e;
        if (str3 != null) {
            Fragment fragmentE0 = e0(str3);
            this.f5226y = fragmentE0;
            L(fragmentE0);
        }
        ArrayList arrayList2 = zVar.f5249f;
        if (arrayList2 != null) {
            for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                this.f5211j.put((String) arrayList2.get(i4), (C0281c) zVar.f5250g.get(i4));
            }
        }
        this.f5190G = new ArrayDeque(zVar.f5251h);
    }

    public Fragment g0(int i3) {
        return this.f5204c.g(i3);
    }

    public Fragment h0(String str) {
        return this.f5204c.h(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: h1, reason: merged with bridge method [inline-methods] */
    public Bundle O0() {
        C0280b[] c0280bArr;
        int size;
        Bundle bundle = new Bundle();
        l0();
        X();
        a0(true);
        this.f5192I = true;
        this.f5199P.q(true);
        ArrayList arrayListY = this.f5204c.y();
        ArrayList<C> arrayListM = this.f5204c.m();
        if (!arrayListM.isEmpty()) {
            ArrayList arrayListZ = this.f5204c.z();
            ArrayList arrayList = this.f5205d;
            if (arrayList == null || (size = arrayList.size()) <= 0) {
                c0280bArr = null;
            } else {
                c0280bArr = new C0280b[size];
                for (int i3 = 0; i3 < size; i3++) {
                    c0280bArr[i3] = new C0280b((C0279a) this.f5205d.get(i3));
                    if (G0(2)) {
                        Log.v("FragmentManager", "saveAllState: adding back stack #" + i3 + ": " + this.f5205d.get(i3));
                    }
                }
            }
            z zVar = new z();
            zVar.f5244a = arrayListY;
            zVar.f5245b = arrayListZ;
            zVar.f5246c = c0280bArr;
            zVar.f5247d = this.f5210i.get();
            Fragment fragment = this.f5226y;
            if (fragment != null) {
                zVar.f5248e = fragment.f4944g;
            }
            zVar.f5249f.addAll(this.f5211j.keySet());
            zVar.f5250g.addAll(this.f5211j.values());
            zVar.f5251h = new ArrayList(this.f5190G);
            bundle.putParcelable("state", zVar);
            for (String str : this.f5212k.keySet()) {
                bundle.putBundle("result_" + str, (Bundle) this.f5212k.get(str));
            }
            for (C c4 : arrayListM) {
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("state", c4);
                bundle.putBundle("fragment_" + c4.f4858b, bundle2);
            }
        } else if (G0(2)) {
            Log.v("FragmentManager", "saveAllState: no fragments!");
        }
        return bundle;
    }

    void i(C0279a c0279a) {
        if (this.f5205d == null) {
            this.f5205d = new ArrayList();
        }
        this.f5205d.add(c0279a);
    }

    Fragment i0(String str) {
        return this.f5204c.i(str);
    }

    void i1() {
        synchronized (this.f5202a) {
            try {
                if (this.f5202a.size() == 1) {
                    this.f5223v.m().removeCallbacks(this.f5201R);
                    this.f5223v.m().post(this.f5201R);
                    q1();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    D j(Fragment fragment) {
        String str = fragment.f4928R;
        if (str != null) {
            C.c.f(fragment, str);
        }
        if (G0(2)) {
            Log.v("FragmentManager", "add: " + fragment);
        }
        D dV = v(fragment);
        fragment.f4958u = this;
        this.f5204c.r(dV);
        if (!fragment.f4913C) {
            this.f5204c.a(fragment);
            fragment.f4951n = false;
            if (fragment.f4920J == null) {
                fragment.f4925O = false;
            }
            if (H0(fragment)) {
                this.f5191H = true;
            }
        }
        return dV;
    }

    void j1(Fragment fragment, boolean z3) {
        ViewGroup viewGroupQ0 = q0(fragment);
        if (viewGroupQ0 == null || !(viewGroupQ0 instanceof C0291m)) {
            return;
        }
        ((C0291m) viewGroupQ0).setDrawDisappearingViewsLast(!z3);
    }

    public void k(B b4) {
        this.f5216o.add(b4);
    }

    void k1(Fragment fragment, AbstractC0299g.b bVar) {
        if (fragment.equals(e0(fragment.f4944g)) && (fragment.f4959v == null || fragment.f4958u == this)) {
            fragment.f4929S = bVar;
            return;
        }
        throw new IllegalArgumentException("Fragment " + fragment + " is not an active fragment of FragmentManager " + this);
    }

    int l() {
        return this.f5210i.getAndIncrement();
    }

    void l1(Fragment fragment) {
        if (fragment == null || (fragment.equals(e0(fragment.f4944g)) && (fragment.f4959v == null || fragment.f4958u == this))) {
            Fragment fragment2 = this.f5226y;
            this.f5226y = fragment;
            L(fragment2);
            L(this.f5226y);
            return;
        }
        throw new IllegalArgumentException("Fragment " + fragment + " is not an active fragment of FragmentManager " + this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void m(p pVar, AbstractC0290l abstractC0290l, Fragment fragment) {
        String str;
        if (this.f5223v != null) {
            throw new IllegalStateException("Already attached");
        }
        this.f5223v = pVar;
        this.f5224w = abstractC0290l;
        this.f5225x = fragment;
        if (fragment != null) {
            k(new g(fragment));
        } else if (pVar instanceof B) {
            k((B) pVar);
        }
        if (this.f5225x != null) {
            q1();
        }
        if (pVar instanceof androidx.activity.o) {
            androidx.activity.o oVar = (androidx.activity.o) pVar;
            OnBackPressedDispatcher onBackPressedDispatcherA = oVar.a();
            this.f5208g = onBackPressedDispatcherA;
            androidx.lifecycle.l lVar = oVar;
            if (fragment != null) {
                lVar = fragment;
            }
            onBackPressedDispatcherA.b(lVar, this.f5209h);
        }
        if (fragment != null) {
            this.f5199P = fragment.f4958u.o0(fragment);
        } else if (pVar instanceof androidx.lifecycle.H) {
            this.f5199P = A.l(((androidx.lifecycle.H) pVar).s());
        } else {
            this.f5199P = new A(false);
        }
        this.f5199P.q(N0());
        this.f5204c.A(this.f5199P);
        Object obj = this.f5223v;
        if ((obj instanceof G.d) && fragment == null) {
            androidx.savedstate.a aVarB = ((G.d) obj).b();
            aVarB.h("android:support:fragments", new a.c() { // from class: androidx.fragment.app.w
                @Override // androidx.savedstate.a.c
                public final Bundle a() {
                    return this.f5182a.O0();
                }
            });
            Bundle bundleB = aVarB.b("android:support:fragments");
            if (bundleB != null) {
                f1(bundleB);
            }
        }
        Object obj2 = this.f5223v;
        if (obj2 instanceof androidx.activity.result.f) {
            androidx.activity.result.e eVarO = ((androidx.activity.result.f) obj2).o();
            if (fragment != null) {
                str = fragment.f4944g + ":";
            } else {
                str = "";
            }
            String str2 = "FragmentManager:" + str;
            this.f5187D = eVarO.g(str2 + "StartActivityForResult", new C0305c(), new h());
            this.f5188E = eVarO.g(str2 + "StartIntentSenderForResult", new j(), new i());
            this.f5189F = eVarO.g(str2 + "RequestPermissions", new C0304b(), new a());
        }
        Object obj3 = this.f5223v;
        if (obj3 instanceof androidx.core.content.c) {
            ((androidx.core.content.c) obj3).r(this.f5217p);
        }
        Object obj4 = this.f5223v;
        if (obj4 instanceof androidx.core.content.d) {
            ((androidx.core.content.d) obj4).x(this.f5218q);
        }
        Object obj5 = this.f5223v;
        if (obj5 instanceof androidx.core.app.j) {
            ((androidx.core.app.j) obj5).q(this.f5219r);
        }
        Object obj6 = this.f5223v;
        if (obj6 instanceof androidx.core.app.k) {
            ((androidx.core.app.k) obj6).l(this.f5220s);
        }
        Object obj7 = this.f5223v;
        if ((obj7 instanceof InterfaceC0278z) && fragment == null) {
            ((InterfaceC0278z) obj7).n(this.f5221t);
        }
    }

    void n(Fragment fragment) {
        if (G0(2)) {
            Log.v("FragmentManager", "attach: " + fragment);
        }
        if (fragment.f4913C) {
            fragment.f4913C = false;
            if (fragment.f4950m) {
                return;
            }
            this.f5204c.a(fragment);
            if (G0(2)) {
                Log.v("FragmentManager", "add from attach: " + fragment);
            }
            if (H0(fragment)) {
                this.f5191H = true;
            }
        }
    }

    public int n0() {
        ArrayList arrayList = this.f5205d;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    void n1(Fragment fragment) {
        if (G0(2)) {
            Log.v("FragmentManager", "show: " + fragment);
        }
        if (fragment.f4912B) {
            fragment.f4912B = false;
            fragment.f4925O = !fragment.f4925O;
        }
    }

    public F o() {
        return new C0279a(this);
    }

    boolean p() {
        boolean zH0 = false;
        for (Fragment fragment : this.f5204c.l()) {
            if (fragment != null) {
                zH0 = H0(fragment);
            }
            if (zH0) {
                return true;
            }
        }
        return false;
    }

    AbstractC0290l p0() {
        return this.f5224w;
    }

    public o r0() {
        o oVar = this.f5227z;
        if (oVar != null) {
            return oVar;
        }
        Fragment fragment = this.f5225x;
        return fragment != null ? fragment.f4958u.r0() : this.f5184A;
    }

    public List s0() {
        return this.f5204c.o();
    }

    public p t0() {
        return this.f5223v;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        Fragment fragment = this.f5225x;
        if (fragment != null) {
            sb.append(fragment.getClass().getSimpleName());
            sb.append("{");
            sb.append(Integer.toHexString(System.identityHashCode(this.f5225x)));
            sb.append("}");
        } else {
            p pVar = this.f5223v;
            if (pVar != null) {
                sb.append(pVar.getClass().getSimpleName());
                sb.append("{");
                sb.append(Integer.toHexString(System.identityHashCode(this.f5223v)));
                sb.append("}");
            } else {
                sb.append("null");
            }
        }
        sb.append("}}");
        return sb.toString();
    }

    LayoutInflater.Factory2 u0() {
        return this.f5207f;
    }

    D v(Fragment fragment) {
        D dN = this.f5204c.n(fragment.f4944g);
        if (dN != null) {
            return dN;
        }
        D d4 = new D(this.f5215n, this.f5204c, fragment);
        d4.o(this.f5223v.k().getClassLoader());
        d4.t(this.f5222u);
        return d4;
    }

    r v0() {
        return this.f5215n;
    }

    void w(Fragment fragment) {
        if (G0(2)) {
            Log.v("FragmentManager", "detach: " + fragment);
        }
        if (fragment.f4913C) {
            return;
        }
        fragment.f4913C = true;
        if (fragment.f4950m) {
            if (G0(2)) {
                Log.v("FragmentManager", "remove from detach: " + fragment);
            }
            this.f5204c.u(fragment);
            if (H0(fragment)) {
                this.f5191H = true;
            }
            m1(fragment);
        }
    }

    Fragment w0() {
        return this.f5225x;
    }

    void x() {
        this.f5192I = false;
        this.f5193J = false;
        this.f5199P.q(false);
        S(4);
    }

    public Fragment x0() {
        return this.f5226y;
    }

    void y() {
        this.f5192I = false;
        this.f5193J = false;
        this.f5199P.q(false);
        S(0);
    }

    M y0() {
        M m3 = this.f5185B;
        if (m3 != null) {
            return m3;
        }
        Fragment fragment = this.f5225x;
        return fragment != null ? fragment.f4958u.y0() : this.f5186C;
    }

    void z(Configuration configuration, boolean z3) {
        if (z3 && (this.f5223v instanceof androidx.core.content.c)) {
            p1(new IllegalStateException("Do not call dispatchConfigurationChanged() on host. Host implements OnConfigurationChangedProvider and automatically dispatches configuration changes to fragments."));
        }
        for (Fragment fragment : this.f5204c.o()) {
            if (fragment != null) {
                fragment.N0(configuration);
                if (z3) {
                    fragment.f4960w.z(configuration, true);
                }
            }
        }
    }

    public c.C0001c z0() {
        return this.f5200Q;
    }
}
