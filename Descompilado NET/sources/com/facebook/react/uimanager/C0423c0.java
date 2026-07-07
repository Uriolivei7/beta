package com.facebook.react.uimanager;

import a1.C0210a;
import android.util.SparseBooleanArray;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.views.view.ReactViewManager;

/* JADX INFO: renamed from: com.facebook.react.uimanager.c0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0423c0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final M0 f7466a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0466y0 f7467b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final SparseBooleanArray f7468c = new SparseBooleanArray();

    /* JADX INFO: renamed from: com.facebook.react.uimanager.c0$a */
    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final InterfaceC0451q0 f7469a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final int f7470b;

        a(InterfaceC0451q0 interfaceC0451q0, int i3) {
            this.f7469a = interfaceC0451q0;
            this.f7470b = i3;
        }
    }

    public C0423c0(M0 m02, C0466y0 c0466y0) {
        this.f7466a = m02;
        this.f7467b = c0466y0;
    }

    private void a(InterfaceC0451q0 interfaceC0451q0, InterfaceC0451q0 interfaceC0451q02, int i3) {
        C0210a.a(interfaceC0451q02.m() != EnumC0419a0.f7441b);
        for (int i4 = 0; i4 < interfaceC0451q02.C(); i4++) {
            InterfaceC0451q0 interfaceC0451q0N = interfaceC0451q02.N(i4);
            C0210a.a(interfaceC0451q0N.V() == null);
            int iU = interfaceC0451q0.U();
            if (interfaceC0451q0N.m() == EnumC0419a0.f7443d) {
                d(interfaceC0451q0, interfaceC0451q0N, i3);
            } else {
                b(interfaceC0451q0, interfaceC0451q0N, i3);
            }
            i3 += interfaceC0451q0.U() - iU;
        }
    }

    private void b(InterfaceC0451q0 interfaceC0451q0, InterfaceC0451q0 interfaceC0451q02, int i3) {
        interfaceC0451q0.Z(interfaceC0451q02, i3);
        this.f7466a.G(interfaceC0451q0.H(), null, new O0[]{new O0(interfaceC0451q02.H(), i3)}, null);
        if (interfaceC0451q02.m() != EnumC0419a0.f7441b) {
            a(interfaceC0451q0, interfaceC0451q02, i3 + 1);
        }
    }

    private void c(InterfaceC0451q0 interfaceC0451q0, InterfaceC0451q0 interfaceC0451q02, int i3) {
        int iT = interfaceC0451q0.T(interfaceC0451q0.N(i3));
        if (interfaceC0451q0.m() != EnumC0419a0.f7441b) {
            a aVarS = s(interfaceC0451q0, iT);
            if (aVarS == null) {
                return;
            }
            InterfaceC0451q0 interfaceC0451q03 = aVarS.f7469a;
            iT = aVarS.f7470b;
            interfaceC0451q0 = interfaceC0451q03;
        }
        if (interfaceC0451q02.m() != EnumC0419a0.f7443d) {
            b(interfaceC0451q0, interfaceC0451q02, iT);
        } else {
            d(interfaceC0451q0, interfaceC0451q02, iT);
        }
    }

    private void d(InterfaceC0451q0 interfaceC0451q0, InterfaceC0451q0 interfaceC0451q02, int i3) {
        a(interfaceC0451q0, interfaceC0451q02, i3);
    }

    private void e(InterfaceC0451q0 interfaceC0451q0) {
        int iH = interfaceC0451q0.H();
        if (this.f7468c.get(iH)) {
            return;
        }
        this.f7468c.put(iH, true);
        int iD = interfaceC0451q0.D();
        int iJ = interfaceC0451q0.j();
        for (InterfaceC0451q0 parent = interfaceC0451q0.getParent(); parent != null && parent.m() != EnumC0419a0.f7441b; parent = parent.getParent()) {
            if (!parent.R()) {
                iD += Math.round(parent.J());
                iJ += Math.round(parent.A());
            }
        }
        f(interfaceC0451q0, iD, iJ);
    }

    private void f(InterfaceC0451q0 interfaceC0451q0, int i3, int i4) {
        if (interfaceC0451q0.m() != EnumC0419a0.f7443d && interfaceC0451q0.V() != null) {
            this.f7466a.P(interfaceC0451q0.P().H(), interfaceC0451q0.H(), i3, i4, interfaceC0451q0.a(), interfaceC0451q0.b(), interfaceC0451q0.getLayoutDirection());
            return;
        }
        for (int i5 = 0; i5 < interfaceC0451q0.C(); i5++) {
            InterfaceC0451q0 interfaceC0451q0N = interfaceC0451q0.N(i5);
            int iH = interfaceC0451q0N.H();
            if (!this.f7468c.get(iH)) {
                this.f7468c.put(iH, true);
                f(interfaceC0451q0N, interfaceC0451q0N.D() + i3, interfaceC0451q0N.j() + i4);
            }
        }
    }

    public static void j(InterfaceC0451q0 interfaceC0451q0) {
        interfaceC0451q0.L();
    }

    private static boolean n(C0454s0 c0454s0) {
        if (c0454s0 == null) {
            return true;
        }
        if (c0454s0.c("collapsable") && !c0454s0.a("collapsable", true)) {
            return false;
        }
        ReadableMapKeySetIterator readableMapKeySetIteratorKeySetIterator = c0454s0.f7630a.keySetIterator();
        while (readableMapKeySetIteratorKeySetIterator.hasNextKey()) {
            if (!Z0.a(c0454s0.f7630a, readableMapKeySetIteratorKeySetIterator.nextKey())) {
                return false;
            }
        }
        return true;
    }

    private void q(InterfaceC0451q0 interfaceC0451q0, boolean z3) {
        if (interfaceC0451q0.m() != EnumC0419a0.f7441b) {
            for (int iC = interfaceC0451q0.C() - 1; iC >= 0; iC--) {
                q(interfaceC0451q0.N(iC), z3);
            }
        }
        InterfaceC0451q0 interfaceC0451q0V = interfaceC0451q0.V();
        if (interfaceC0451q0V != null) {
            int iY = interfaceC0451q0V.Y(interfaceC0451q0);
            interfaceC0451q0V.I(iY);
            this.f7466a.G(interfaceC0451q0V.H(), new int[]{iY}, null, z3 ? new int[]{interfaceC0451q0.H()} : null);
        }
    }

    private void r(InterfaceC0451q0 interfaceC0451q0, C0454s0 c0454s0) {
        InterfaceC0451q0 parent = interfaceC0451q0.getParent();
        if (parent == null) {
            interfaceC0451q0.W(false);
            return;
        }
        int iT = parent.t(interfaceC0451q0);
        parent.e(iT);
        q(interfaceC0451q0, false);
        interfaceC0451q0.W(false);
        this.f7466a.C(interfaceC0451q0.l(), interfaceC0451q0.H(), interfaceC0451q0.v(), c0454s0);
        parent.o(interfaceC0451q0, iT);
        c(parent, interfaceC0451q0, iT);
        for (int i3 = 0; i3 < interfaceC0451q0.C(); i3++) {
            c(interfaceC0451q0, interfaceC0451q0.N(i3), i3);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Transitioning LayoutOnlyView - tag: ");
        sb.append(interfaceC0451q0.H());
        sb.append(" - rootTag: ");
        sb.append(interfaceC0451q0.n());
        sb.append(" - hasProps: ");
        sb.append(c0454s0 != null);
        sb.append(" - tagsWithLayout.size: ");
        sb.append(this.f7468c.size());
        Y.a.s("NativeViewHierarchyOptimizer", sb.toString());
        C0210a.a(this.f7468c.size() == 0);
        e(interfaceC0451q0);
        for (int i4 = 0; i4 < interfaceC0451q0.C(); i4++) {
            e(interfaceC0451q0.N(i4));
        }
        this.f7468c.clear();
    }

    private a s(InterfaceC0451q0 interfaceC0451q0, int i3) {
        while (interfaceC0451q0.m() != EnumC0419a0.f7441b) {
            InterfaceC0451q0 parent = interfaceC0451q0.getParent();
            if (parent == null) {
                return null;
            }
            i3 = i3 + (interfaceC0451q0.m() == EnumC0419a0.f7442c ? 1 : 0) + parent.T(interfaceC0451q0);
            interfaceC0451q0 = parent;
        }
        return new a(interfaceC0451q0, i3);
    }

    public void g(InterfaceC0451q0 interfaceC0451q0, B0 b02, C0454s0 c0454s0) {
        interfaceC0451q0.W(interfaceC0451q0.v().equals(ReactViewManager.REACT_CLASS) && n(c0454s0));
        if (interfaceC0451q0.m() != EnumC0419a0.f7443d) {
            this.f7466a.C(b02, interfaceC0451q0.H(), interfaceC0451q0.v(), c0454s0);
        }
    }

    public void h(InterfaceC0451q0 interfaceC0451q0) {
        if (interfaceC0451q0.a0()) {
            r(interfaceC0451q0, null);
        }
    }

    public void i(InterfaceC0451q0 interfaceC0451q0, int[] iArr, int[] iArr2, O0[] o0Arr, int[] iArr3) {
        boolean z3;
        for (int i3 : iArr2) {
            int i4 = 0;
            while (true) {
                if (i4 >= iArr3.length) {
                    z3 = false;
                    break;
                } else {
                    if (iArr3[i4] == i3) {
                        z3 = true;
                        break;
                    }
                    i4++;
                }
            }
            q(this.f7467b.c(i3), z3);
        }
        for (O0 o02 : o0Arr) {
            c(interfaceC0451q0, this.f7467b.c(o02.f7352a), o02.f7353b);
        }
    }

    public void k(InterfaceC0451q0 interfaceC0451q0, ReadableArray readableArray) {
        for (int i3 = 0; i3 < readableArray.size(); i3++) {
            c(interfaceC0451q0, this.f7467b.c(readableArray.getInt(i3)), i3);
        }
    }

    public void l(InterfaceC0451q0 interfaceC0451q0) {
        e(interfaceC0451q0);
    }

    public void m(InterfaceC0451q0 interfaceC0451q0, String str, C0454s0 c0454s0) {
        if (interfaceC0451q0.a0() && !n(c0454s0)) {
            r(interfaceC0451q0, c0454s0);
        } else {
            if (interfaceC0451q0.a0()) {
                return;
            }
            this.f7466a.Q(interfaceC0451q0.H(), str, c0454s0);
        }
    }

    public void o() {
        this.f7468c.clear();
    }

    void p(InterfaceC0451q0 interfaceC0451q0) {
        this.f7468c.clear();
    }
}
