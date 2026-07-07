package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.core.os.d;
import androidx.core.view.AbstractC0246e0;
import androidx.core.view.Z;
import androidx.fragment.app.AbstractC0289k;
import androidx.fragment.app.L;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import l.C0589a;

/* JADX INFO: renamed from: androidx.fragment.app.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0282d extends L {

    /* JADX INFO: renamed from: androidx.fragment.app.d$a */
    static /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f5076a;

        static {
            int[] iArr = new int[L.e.c.values().length];
            f5076a = iArr;
            try {
                iArr[L.e.c.GONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5076a[L.e.c.INVISIBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5076a[L.e.c.REMOVED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f5076a[L.e.c.VISIBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$b */
    class b implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ List f5077b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ L.e f5078c;

        b(List list, L.e eVar) {
            this.f5077b = list;
            this.f5078c = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f5077b.contains(this.f5078c)) {
                this.f5077b.remove(this.f5078c);
                C0282d.this.s(this.f5078c);
            }
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$c */
    class c extends AnimatorListenerAdapter {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewGroup f5080a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f5081b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ boolean f5082c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ L.e f5083d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ k f5084e;

        c(ViewGroup viewGroup, View view, boolean z3, L.e eVar, k kVar) {
            this.f5080a = viewGroup;
            this.f5081b = view;
            this.f5082c = z3;
            this.f5083d = eVar;
            this.f5084e = kVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f5080a.endViewTransition(this.f5081b);
            if (this.f5082c) {
                this.f5083d.e().a(this.f5081b);
            }
            this.f5084e.a();
            if (x.G0(2)) {
                Log.v("FragmentManager", "Animator from operation " + this.f5083d + " has ended.");
            }
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$d, reason: collision with other inner class name */
    class C0073d implements d.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Animator f5086a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ L.e f5087b;

        C0073d(Animator animator, L.e eVar) {
            this.f5086a = animator;
            this.f5087b = eVar;
        }

        @Override // androidx.core.os.d.a
        public void a() {
            this.f5086a.end();
            if (x.G0(2)) {
                Log.v("FragmentManager", "Animator from operation " + this.f5087b + " has been canceled.");
            }
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$e */
    class e implements Animation.AnimationListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ L.e f5089a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ ViewGroup f5090b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ View f5091c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ k f5092d;

        /* JADX INFO: renamed from: androidx.fragment.app.d$e$a */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                e eVar = e.this;
                eVar.f5090b.endViewTransition(eVar.f5091c);
                e.this.f5092d.a();
            }
        }

        e(L.e eVar, ViewGroup viewGroup, View view, k kVar) {
            this.f5089a = eVar;
            this.f5090b = viewGroup;
            this.f5091c = view;
            this.f5092d = kVar;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            this.f5090b.post(new a());
            if (x.G0(2)) {
                Log.v("FragmentManager", "Animation from operation " + this.f5089a + " has ended.");
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            if (x.G0(2)) {
                Log.v("FragmentManager", "Animation from operation " + this.f5089a + " has reached onAnimationStart.");
            }
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$f */
    class f implements d.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ View f5095a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ ViewGroup f5096b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ k f5097c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ L.e f5098d;

        f(View view, ViewGroup viewGroup, k kVar, L.e eVar) {
            this.f5095a = view;
            this.f5096b = viewGroup;
            this.f5097c = kVar;
            this.f5098d = eVar;
        }

        @Override // androidx.core.os.d.a
        public void a() {
            this.f5095a.clearAnimation();
            this.f5096b.endViewTransition(this.f5095a);
            this.f5097c.a();
            if (x.G0(2)) {
                Log.v("FragmentManager", "Animation from operation " + this.f5098d + " has been cancelled.");
            }
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$g */
    class g implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ L.e f5100b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ L.e f5101c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ boolean f5102d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ C0589a f5103e;

        g(L.e eVar, L.e eVar2, boolean z3, C0589a c0589a) {
            this.f5100b = eVar;
            this.f5101c = eVar2;
            this.f5102d = z3;
            this.f5103e = c0589a;
        }

        @Override // java.lang.Runnable
        public void run() {
            G.a(this.f5100b.f(), this.f5101c.f(), this.f5102d, this.f5103e, false);
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$h */
    class h implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ I f5105b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ View f5106c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ Rect f5107d;

        h(I i3, View view, Rect rect) {
            this.f5105b = i3;
            this.f5106c = view;
            this.f5107d = rect;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f5105b.h(this.f5106c, this.f5107d);
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$i */
    class i implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ ArrayList f5109b;

        i(ArrayList arrayList) {
            this.f5109b = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            G.e(this.f5109b, 4);
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$j */
    class j implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ m f5111b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ L.e f5112c;

        j(m mVar, L.e eVar) {
            this.f5111b = mVar;
            this.f5112c = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f5111b.a();
            if (x.G0(2)) {
                Log.v("FragmentManager", "Transition for operation " + this.f5112c + "has completed");
            }
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$k */
    private static class k extends l {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f5114c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f5115d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private AbstractC0289k.a f5116e;

        k(L.e eVar, androidx.core.os.d dVar, boolean z3) {
            super(eVar, dVar);
            this.f5115d = false;
            this.f5114c = z3;
        }

        AbstractC0289k.a e(Context context) {
            if (this.f5115d) {
                return this.f5116e;
            }
            AbstractC0289k.a aVarB = AbstractC0289k.b(context, b().f(), b().e() == L.e.c.VISIBLE, this.f5114c);
            this.f5116e = aVarB;
            this.f5115d = true;
            return aVarB;
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$l */
    private static class l {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final L.e f5117a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final androidx.core.os.d f5118b;

        l(L.e eVar, androidx.core.os.d dVar) {
            this.f5117a = eVar;
            this.f5118b = dVar;
        }

        void a() {
            this.f5117a.d(this.f5118b);
        }

        L.e b() {
            return this.f5117a;
        }

        androidx.core.os.d c() {
            return this.f5118b;
        }

        boolean d() {
            L.e.c cVar;
            L.e.c cVarC = L.e.c.c(this.f5117a.f().f4920J);
            L.e.c cVarE = this.f5117a.e();
            return cVarC == cVarE || !(cVarC == (cVar = L.e.c.VISIBLE) || cVarE == cVar);
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.d$m */
    private static class m extends l {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final Object f5119c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final boolean f5120d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final Object f5121e;

        m(L.e eVar, androidx.core.os.d dVar, boolean z3, boolean z4) {
            super(eVar, dVar);
            if (eVar.e() == L.e.c.VISIBLE) {
                this.f5119c = z3 ? eVar.f().J() : eVar.f().r();
                this.f5120d = z3 ? eVar.f().l() : eVar.f().i();
            } else {
                this.f5119c = z3 ? eVar.f().L() : eVar.f().w();
                this.f5120d = true;
            }
            if (!z4) {
                this.f5121e = null;
            } else if (z3) {
                this.f5121e = eVar.f().N();
            } else {
                this.f5121e = eVar.f().M();
            }
        }

        private I f(Object obj) {
            if (obj == null) {
                return null;
            }
            I i3 = G.f4996a;
            if (i3 != null && i3.e(obj)) {
                return i3;
            }
            I i4 = G.f4997b;
            if (i4 != null && i4.e(obj)) {
                return i4;
            }
            throw new IllegalArgumentException("Transition " + obj + " for fragment " + b().f() + " is not a valid framework Transition or AndroidX Transition");
        }

        I e() {
            I iF = f(this.f5119c);
            I iF2 = f(this.f5121e);
            if (iF == null || iF2 == null || iF == iF2) {
                return iF != null ? iF : iF2;
            }
            throw new IllegalArgumentException("Mixing framework transitions and AndroidX transitions is not allowed. Fragment " + b().f() + " returned Transition " + this.f5119c + " which uses a different Transition  type than its shared element transition " + this.f5121e);
        }

        public Object g() {
            return this.f5121e;
        }

        Object h() {
            return this.f5119c;
        }

        public boolean i() {
            return this.f5121e != null;
        }

        boolean j() {
            return this.f5120d;
        }
    }

    C0282d(ViewGroup viewGroup) {
        super(viewGroup);
    }

    private void w(List list, List list2, boolean z3, Map map) {
        int i3;
        boolean z4;
        Context context;
        View view;
        int i4;
        L.e eVar;
        ViewGroup viewGroupM = m();
        Context context2 = viewGroupM.getContext();
        ArrayList<k> arrayList = new ArrayList();
        Iterator it = list.iterator();
        boolean z5 = false;
        while (true) {
            i3 = 2;
            if (!it.hasNext()) {
                break;
            }
            k kVar = (k) it.next();
            if (kVar.d()) {
                kVar.a();
            } else {
                AbstractC0289k.a aVarE = kVar.e(context2);
                if (aVarE == null) {
                    kVar.a();
                } else {
                    Animator animator = aVarE.f5155b;
                    if (animator == null) {
                        arrayList.add(kVar);
                    } else {
                        L.e eVarB = kVar.b();
                        Fragment fragmentF = eVarB.f();
                        if (Boolean.TRUE.equals(map.get(eVarB))) {
                            if (x.G0(2)) {
                                Log.v("FragmentManager", "Ignoring Animator set on " + fragmentF + " as this Fragment was involved in a Transition.");
                            }
                            kVar.a();
                        } else {
                            boolean z6 = eVarB.e() == L.e.c.GONE;
                            if (z6) {
                                list2.remove(eVarB);
                            }
                            View view2 = fragmentF.f4920J;
                            viewGroupM.startViewTransition(view2);
                            animator.addListener(new c(viewGroupM, view2, z6, eVarB, kVar));
                            animator.setTarget(view2);
                            animator.start();
                            if (x.G0(2)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Animator from operation ");
                                eVar = eVarB;
                                sb.append(eVar);
                                sb.append(" has started.");
                                Log.v("FragmentManager", sb.toString());
                            } else {
                                eVar = eVarB;
                            }
                            kVar.c().b(new C0073d(animator, eVar));
                            z5 = true;
                        }
                    }
                }
            }
        }
        for (k kVar2 : arrayList) {
            L.e eVarB2 = kVar2.b();
            Fragment fragmentF2 = eVarB2.f();
            if (z3) {
                if (x.G0(i3)) {
                    Log.v("FragmentManager", "Ignoring Animation set on " + fragmentF2 + " as Animations cannot run alongside Transitions.");
                }
                kVar2.a();
            } else if (z5) {
                if (x.G0(i3)) {
                    Log.v("FragmentManager", "Ignoring Animation set on " + fragmentF2 + " as Animations cannot run alongside Animators.");
                }
                kVar2.a();
            } else {
                View view3 = fragmentF2.f4920J;
                Animation animation = (Animation) q.g.g(((AbstractC0289k.a) q.g.g(kVar2.e(context2))).f5154a);
                if (eVarB2.e() != L.e.c.REMOVED) {
                    view3.startAnimation(animation);
                    kVar2.a();
                    z4 = z5;
                    context = context2;
                    i4 = i3;
                    view = view3;
                } else {
                    viewGroupM.startViewTransition(view3);
                    AbstractC0289k.b bVar = new AbstractC0289k.b(animation, viewGroupM, view3);
                    z4 = z5;
                    context = context2;
                    view = view3;
                    bVar.setAnimationListener(new e(eVarB2, viewGroupM, view3, kVar2));
                    view.startAnimation(bVar);
                    i4 = 2;
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "Animation from operation " + eVarB2 + " has started.");
                    }
                }
                kVar2.c().b(new f(view, viewGroupM, kVar2, eVarB2));
                i3 = i4;
                z5 = z4;
                context2 = context;
            }
        }
    }

    private Map x(List list, List list2, boolean z3, L.e eVar, L.e eVar2) {
        String str;
        String str2;
        String str3;
        View view;
        Object objK;
        ArrayList arrayList;
        Object objK2;
        ArrayList arrayList2;
        L.e eVar3;
        L.e eVar4;
        View view2;
        C0589a c0589a;
        L.e eVar5;
        HashMap map;
        ArrayList arrayList3;
        View view3;
        I i3;
        ArrayList arrayList4;
        L.e eVar6;
        Rect rect;
        androidx.core.app.m mVarU;
        androidx.core.app.m mVarX;
        ArrayList arrayList5;
        int i4;
        View view4;
        String strB;
        ArrayList arrayList6;
        boolean z4 = z3;
        L.e eVar7 = eVar;
        L.e eVar8 = eVar2;
        HashMap map2 = new HashMap();
        Iterator it = list.iterator();
        I i5 = null;
        while (it.hasNext()) {
            m mVar = (m) it.next();
            if (!mVar.d()) {
                I iE = mVar.e();
                if (i5 == null) {
                    i5 = iE;
                } else if (iE != null && i5 != iE) {
                    throw new IllegalArgumentException("Mixing framework transitions and AndroidX transitions is not allowed. Fragment " + mVar.b().f() + " returned Transition " + mVar.h() + " which uses a different Transition  type than other Fragments.");
                }
            }
        }
        if (i5 == null) {
            Iterator it2 = list.iterator();
            while (it2.hasNext()) {
                m mVar2 = (m) it2.next();
                map2.put(mVar2.b(), Boolean.FALSE);
                mVar2.a();
            }
            return map2;
        }
        View view5 = new View(m().getContext());
        Rect rect2 = new Rect();
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        C0589a c0589a2 = new C0589a();
        Iterator it3 = list.iterator();
        Object obj = null;
        View view6 = null;
        boolean z5 = false;
        while (true) {
            str = "FragmentManager";
            if (!it3.hasNext()) {
                break;
            }
            m mVar3 = (m) it3.next();
            if (!mVar3.i() || eVar7 == null || eVar8 == null) {
                c0589a = c0589a2;
                eVar5 = eVar7;
                map = map2;
                arrayList3 = arrayList7;
                view3 = view5;
                i3 = i5;
                arrayList4 = arrayList8;
                eVar6 = eVar8;
                rect = rect2;
                view6 = view6;
            } else {
                Object objU = i5.u(i5.f(mVar3.g()));
                ArrayList arrayListO = eVar2.f().O();
                ArrayList arrayListO2 = eVar.f().O();
                ArrayList arrayListP = eVar.f().P();
                View view7 = view6;
                HashMap map3 = map2;
                int i6 = 0;
                while (i6 < arrayListP.size()) {
                    int iIndexOf = arrayListO.indexOf(arrayListP.get(i6));
                    ArrayList arrayList9 = arrayListP;
                    if (iIndexOf != -1) {
                        arrayListO.set(iIndexOf, (String) arrayListO2.get(i6));
                    }
                    i6++;
                    arrayListP = arrayList9;
                }
                ArrayList arrayListP2 = eVar2.f().P();
                if (z4) {
                    mVarU = eVar.f().u();
                    mVarX = eVar2.f().x();
                } else {
                    mVarU = eVar.f().x();
                    mVarX = eVar2.f().u();
                }
                int size = arrayListO.size();
                View view8 = view5;
                int i7 = 0;
                while (i7 < size) {
                    c0589a2.put((String) arrayListO.get(i7), (String) arrayListP2.get(i7));
                    i7++;
                    size = size;
                    rect2 = rect2;
                }
                Rect rect3 = rect2;
                if (x.G0(2)) {
                    Log.v("FragmentManager", ">>> entering view names <<<");
                    for (Iterator it4 = arrayListP2.iterator(); it4.hasNext(); it4 = it4) {
                        Log.v("FragmentManager", "Name: " + ((String) it4.next()));
                    }
                    Log.v("FragmentManager", ">>> exiting view names <<<");
                    for (Iterator it5 = arrayListO.iterator(); it5.hasNext(); it5 = it5) {
                        Log.v("FragmentManager", "Name: " + ((String) it5.next()));
                    }
                }
                C0589a c0589a3 = new C0589a();
                u(c0589a3, eVar.f().f4920J);
                c0589a3.o(arrayListO);
                if (mVarU != null) {
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "Executing exit callback for operation " + eVar7);
                    }
                    mVarU.a(arrayListO, c0589a3);
                    int size2 = arrayListO.size() - 1;
                    while (size2 >= 0) {
                        String str4 = (String) arrayListO.get(size2);
                        View view9 = (View) c0589a3.get(str4);
                        if (view9 == null) {
                            c0589a2.remove(str4);
                            arrayList6 = arrayListO;
                        } else {
                            arrayList6 = arrayListO;
                            if (!str4.equals(Z.A(view9))) {
                                c0589a2.put(Z.A(view9), (String) c0589a2.remove(str4));
                            }
                        }
                        size2--;
                        arrayListO = arrayList6;
                    }
                    arrayList5 = arrayListO;
                } else {
                    arrayList5 = arrayListO;
                    c0589a2.o(c0589a3.keySet());
                }
                C0589a c0589a4 = new C0589a();
                u(c0589a4, eVar2.f().f4920J);
                c0589a4.o(arrayListP2);
                c0589a4.o(c0589a2.values());
                if (mVarX != null) {
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "Executing enter callback for operation " + eVar8);
                    }
                    mVarX.a(arrayListP2, c0589a4);
                    for (int size3 = arrayListP2.size() - 1; size3 >= 0; size3--) {
                        String str5 = (String) arrayListP2.get(size3);
                        View view10 = (View) c0589a4.get(str5);
                        if (view10 == null) {
                            String strB2 = G.b(c0589a2, str5);
                            if (strB2 != null) {
                                c0589a2.remove(strB2);
                            }
                        } else if (!str5.equals(Z.A(view10)) && (strB = G.b(c0589a2, str5)) != null) {
                            c0589a2.put(strB, Z.A(view10));
                        }
                    }
                } else {
                    G.d(c0589a2, c0589a4);
                }
                v(c0589a3, c0589a2.keySet());
                v(c0589a4, c0589a2.values());
                if (c0589a2.isEmpty()) {
                    arrayList7.clear();
                    arrayList8.clear();
                    c0589a = c0589a2;
                    arrayList4 = arrayList8;
                    eVar5 = eVar7;
                    arrayList3 = arrayList7;
                    i3 = i5;
                    view6 = view7;
                    view3 = view8;
                    map = map3;
                    rect = rect3;
                    obj = null;
                    eVar6 = eVar8;
                } else {
                    G.a(eVar2.f(), eVar.f(), z4, c0589a3, true);
                    c0589a = c0589a2;
                    ArrayList arrayList10 = arrayList8;
                    androidx.core.view.L.a(m(), new g(eVar2, eVar, z3, c0589a4));
                    arrayList7.addAll(c0589a3.values());
                    if (arrayList5.isEmpty()) {
                        i4 = 0;
                        view6 = view7;
                    } else {
                        i4 = 0;
                        view6 = (View) c0589a3.get((String) arrayList5.get(0));
                        i5.p(objU, view6);
                    }
                    arrayList10.addAll(c0589a4.values());
                    if (arrayListP2.isEmpty() || (view4 = (View) c0589a4.get((String) arrayListP2.get(i4))) == null) {
                        rect = rect3;
                        view3 = view8;
                    } else {
                        rect = rect3;
                        androidx.core.view.L.a(m(), new h(i5, view4, rect));
                        view3 = view8;
                        z5 = true;
                    }
                    i5.s(objU, view3, arrayList7);
                    arrayList3 = arrayList7;
                    i3 = i5;
                    i5.n(objU, null, null, null, null, objU, arrayList10);
                    Boolean bool = Boolean.TRUE;
                    eVar5 = eVar;
                    arrayList4 = arrayList10;
                    map = map3;
                    map.put(eVar5, bool);
                    eVar6 = eVar2;
                    map.put(eVar6, bool);
                    obj = objU;
                }
            }
            view5 = view3;
            rect2 = rect;
            arrayList7 = arrayList3;
            arrayList8 = arrayList4;
            eVar8 = eVar6;
            z4 = z3;
            map2 = map;
            i5 = i3;
            eVar7 = eVar5;
            c0589a2 = c0589a;
        }
        View view11 = view6;
        C0589a c0589a5 = c0589a2;
        L.e eVar9 = eVar7;
        HashMap map4 = map2;
        ArrayList arrayList11 = arrayList7;
        View view12 = view5;
        I i8 = i5;
        ArrayList arrayList12 = arrayList8;
        L.e eVar10 = eVar8;
        Rect rect4 = rect2;
        ArrayList arrayList13 = new ArrayList();
        Iterator it6 = list.iterator();
        Object obj2 = null;
        Object obj3 = null;
        while (it6.hasNext()) {
            m mVar4 = (m) it6.next();
            if (mVar4.d()) {
                map4.put(mVar4.b(), Boolean.FALSE);
                mVar4.a();
                it6 = it6;
            } else {
                Iterator it7 = it6;
                Object objF = i8.f(mVar4.h());
                L.e eVarB = mVar4.b();
                boolean z6 = obj != null && (eVarB == eVar9 || eVarB == eVar10);
                if (objF == null) {
                    if (!z6) {
                        map4.put(eVarB, Boolean.FALSE);
                        mVar4.a();
                    }
                    view = view12;
                    str3 = str;
                    arrayList = arrayList11;
                    arrayList2 = arrayList12;
                    objK = obj2;
                    objK2 = obj3;
                    eVar3 = eVar10;
                    view2 = view11;
                } else {
                    str3 = str;
                    ArrayList arrayList14 = new ArrayList();
                    Object obj4 = obj2;
                    t(arrayList14, eVarB.f().f4920J);
                    if (z6) {
                        if (eVarB == eVar9) {
                            arrayList14.removeAll(arrayList11);
                        } else {
                            arrayList14.removeAll(arrayList12);
                        }
                    }
                    if (arrayList14.isEmpty()) {
                        i8.a(objF, view12);
                        view = view12;
                        arrayList = arrayList11;
                        arrayList2 = arrayList12;
                        objK2 = obj3;
                        eVar4 = eVarB;
                        eVar3 = eVar10;
                        objK = obj4;
                    } else {
                        i8.b(objF, arrayList14);
                        view = view12;
                        objK = obj4;
                        arrayList = arrayList11;
                        objK2 = obj3;
                        arrayList2 = arrayList12;
                        eVar3 = eVar10;
                        i8.n(objF, objF, arrayList14, null, null, null, null);
                        if (eVarB.e() == L.e.c.GONE) {
                            eVar4 = eVarB;
                            list2.remove(eVar4);
                            ArrayList arrayList15 = new ArrayList(arrayList14);
                            arrayList15.remove(eVar4.f().f4920J);
                            i8.m(objF, eVar4.f().f4920J, arrayList15);
                            androidx.core.view.L.a(m(), new i(arrayList14));
                        } else {
                            eVar4 = eVarB;
                        }
                    }
                    if (eVar4.e() == L.e.c.VISIBLE) {
                        arrayList13.addAll(arrayList14);
                        if (z5) {
                            i8.o(objF, rect4);
                        }
                        view2 = view11;
                    } else {
                        view2 = view11;
                        i8.p(objF, view2);
                    }
                    map4.put(eVar4, Boolean.TRUE);
                    if (mVar4.j()) {
                        objK2 = i8.k(objK2, objF, null);
                    } else {
                        objK = i8.k(objK, objF, null);
                    }
                }
                it6 = it7;
                obj2 = objK;
                view11 = view2;
                obj3 = objK2;
                eVar10 = eVar3;
                str = str3;
                view12 = view;
                arrayList11 = arrayList;
                arrayList12 = arrayList2;
            }
        }
        String str6 = str;
        ArrayList<View> arrayList16 = arrayList11;
        ArrayList<View> arrayList17 = arrayList12;
        L.e eVar11 = eVar10;
        Object objJ = i8.j(obj3, obj2, obj);
        if (objJ == null) {
            return map4;
        }
        Iterator it8 = list.iterator();
        while (it8.hasNext()) {
            m mVar5 = (m) it8.next();
            if (!mVar5.d()) {
                Object objH = mVar5.h();
                L.e eVarB2 = mVar5.b();
                boolean z7 = obj != null && (eVarB2 == eVar9 || eVarB2 == eVar11);
                if (objH == null && !z7) {
                    str2 = str6;
                } else if (Z.F(m())) {
                    str2 = str6;
                    i8.q(mVar5.b().f(), objJ, mVar5.c(), new j(mVar5, eVarB2));
                } else {
                    if (x.G0(2)) {
                        str2 = str6;
                        Log.v(str2, "SpecialEffectsController: Container " + m() + " has not been laid out. Completing operation " + eVarB2);
                    } else {
                        str2 = str6;
                    }
                    mVar5.a();
                }
                str6 = str2;
            }
        }
        String str7 = str6;
        if (!Z.F(m())) {
            return map4;
        }
        G.e(arrayList13, 4);
        ArrayList arrayListL = i8.l(arrayList17);
        if (x.G0(2)) {
            Log.v(str7, ">>>>> Beginning transition <<<<<");
            Log.v(str7, ">>>>> SharedElementFirstOutViews <<<<<");
            for (View view13 : arrayList16) {
                Log.v(str7, "View: " + view13 + " Name: " + Z.A(view13));
            }
            Log.v(str7, ">>>>> SharedElementLastInViews <<<<<");
            for (View view14 : arrayList17) {
                Log.v(str7, "View: " + view14 + " Name: " + Z.A(view14));
            }
        }
        i8.c(m(), objJ);
        i8.r(m(), arrayList16, arrayList17, arrayListL, c0589a5);
        G.e(arrayList13, 0);
        i8.t(obj, arrayList16, arrayList17);
        return map4;
    }

    private void y(List list) {
        Fragment fragmentF = ((L.e) list.get(list.size() - 1)).f();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            L.e eVar = (L.e) it.next();
            eVar.f().f4923M.f4973c = fragmentF.f4923M.f4973c;
            eVar.f().f4923M.f4974d = fragmentF.f4923M.f4974d;
            eVar.f().f4923M.f4975e = fragmentF.f4923M.f4975e;
            eVar.f().f4923M.f4976f = fragmentF.f4923M.f4976f;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00a8  */
    @Override // androidx.fragment.app.L
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void f(java.util.List r14, boolean r15) {
        /*
            Method dump skipped, instruction units count: 263
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.C0282d.f(java.util.List, boolean):void");
    }

    void s(L.e eVar) {
        eVar.e().a(eVar.f().f4920J);
    }

    void t(ArrayList arrayList, View view) {
        if (!(view instanceof ViewGroup)) {
            if (arrayList.contains(view)) {
                return;
            }
            arrayList.add(view);
            return;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        if (AbstractC0246e0.a(viewGroup)) {
            if (arrayList.contains(view)) {
                return;
            }
            arrayList.add(viewGroup);
            return;
        }
        int childCount = viewGroup.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = viewGroup.getChildAt(i3);
            if (childAt.getVisibility() == 0) {
                t(arrayList, childAt);
            }
        }
    }

    void u(Map map, View view) {
        String strA = Z.A(view);
        if (strA != null) {
            map.put(strA, view);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = viewGroup.getChildAt(i3);
                if (childAt.getVisibility() == 0) {
                    u(map, childAt);
                }
            }
        }
    }

    void v(C0589a c0589a, Collection collection) {
        Iterator it = c0589a.entrySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(Z.A((View) ((Map.Entry) it.next()).getValue()))) {
                it.remove();
            }
        }
    }
}
